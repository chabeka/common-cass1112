package fr.urssaf.image.sae.webservices.security.igc;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.util.TextUtils;
import fr.urssaf.image.sae.webservices.security.SecurityUtils;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;
import fr.urssaf.image.sae.webservices.security.igc.modele.CertifsAndCrl;
import fr.urssaf.image.sae.webservices.util.DateTimeUtils;
import fr.urssaf.image.sae.webservices.util.ResourceUtils;

/**
 * Service qui charge et renvoie les certificats des AC racines de confiance et
 * les CRL<br>
 * les AC racines ne sont chargés qu'une seule fois tandis que les CRL le sont
 * une fois par jour<br>
 * <br>
 * 
 * 
 * 
 */
@Service
public class IgcService implements InitializingBean {

   private static final Logger LOG = LoggerFactory.getLogger(IgcService.class);

   private CertifsAndCrl certifsAndCrl;

   private final IgcConfig igcConfig;

   private final List<X509Certificate> certsAcRacine;

   private static final int VALIDATE_TIME = 24;
   
   public static final String CRL_ERROR = "Une erreur s'est produite lors du chargement des CRL";

   public static final String AC_RACINE_ERROR = "Une erreur s'est produite lors du chargement des certificats des AC racine de confiance";

   public static final String AC_RACINE_EMPTY = "Aucun certificat d'AC racine de confiance trouvé dans le répertoire ${0}";

   public static final String CRL_FIRST_LOAD = "Chargement des CRL en mémoire depuis les fichiers .crl pour la première fois";
   
   public static final String CRL_RELOAD = "Rechargement des CRL en mémoire depuis les fichiers .crl car les informations en mémoire sont périmées (date du chargement en mémoire précédent : ${0})";
   
   public static final String CRL_COUNT = "${0} CRL chargée(s) en mémoire";
   
   public static final String AC_RACINE_LOAD = "Chargement en mémoire des certificats des AC racine depuis les fichiers .crt";
   
   public static final String AC_RACINE_COUNT = "${0} certificat(s) d'AC racine de confiance chargé(s) en mémoire";

   
   /**
    * Instanciation de {@link IgcService}
    * 
    * @param igcConfig
    *           configuration de l'IGC
    */
   @Autowired
   public IgcService(IgcConfig igcConfig) {

      this.igcConfig = igcConfig;
      this.certsAcRacine = new ArrayList<X509Certificate>();

   }

   /**
    * 
    * Chargement des AC racines<br>
    * Les certificats sont récupérés dans le repertoire indiqué par
    * {@link IgcConfig#getRepertoireACRacines()}<br>
    * Tous les fichier de type *.pem sont chargés en mémoire<br>
    * <br>
    * Une exception {@link IllegalArgumentException} est levée avec le message
    * {@value #AC_RACINE_ERROR} si le chargement des certificats lève une
    * exception ou si l'un des certificats pose problème <br>
    * Si aucun certificat n'est chargé une exception
    * {@link IllegalArgumentException} avec le message {@value #AC_RACINE_EMPTY}
    * est levé où ${0} est le nom du répertoire des AC racine
    * 
    * 
    */
   @Override
   public final void afterPropertiesSet() {

      LOG.info(AC_RACINE_LOAD);
      
      FileSystemResource repACRacine = new FileSystemResource(igcConfig
            .getRepertoireACRacines());

      List<Resource> resources = ResourceUtils.loadResources(repACRacine,
            new String[] { "crt" });

      for (Resource crt : resources) {

         try {

            LOG.debug("loading certificat: " + crt.getFilename());
            InputStream input = crt.getInputStream();

            try {

               certsAcRacine.add(SecurityUtils.loadCertificat(input));

            } catch (CertificateException e) {

               LOG.error("erreur lors du chargement du certificat : " + crt.getFilename());
               throw new IllegalArgumentException(AC_RACINE_ERROR, e);
            }

            finally {
               input.close();
            }

         } catch (IOException e) {
            throw new IllegalArgumentException(AC_RACINE_ERROR, e);
         }
      }

      if (CollectionUtils.isEmpty(certsAcRacine)) {
         throw new IllegalArgumentException(TextUtils.getMessage(
               AC_RACINE_EMPTY, repACRacine.getPath()));
      }
      
      LOG.info(
            TextUtils.getMessage(
                  AC_RACINE_COUNT, 
                  String.valueOf(this.certsAcRacine.size())));

   }

   /**
    * La même instance de {@link CertifsAndCrl} est partagé par tous les Threads
    * ayant accès à cette classe<br>
    * Si l'instance n'existe ou que {@link CertifsAndCrl#getDateMajCrl()} date
    * de plus de 24 heures alors une une nouvelle instance de
    * {@link CertifsAndCrl} est mise à disposition des Threads<br>
    * <br>
    * Lors de l'instanciation les CRL sont de nouveau chargés<br>
    * Les CRL sont récupérés dans le répertoire indiqué par
    * {@link IgcConfig#getRepertoireCRLs()}<br>
    * Tous les fichiers de ce répertoire sont ainsi chargés
    * 
    * 
    * @return instance de {@link CertifsAndCrl}
    * @throws LoadCertifsAndCrlException
    *            échec lors du chargement des CRL ou si l'un CRL pose problème
    *            avec le message {@value #CRL_ERROR}
    */
   public final CertifsAndCrl getInstanceCertifsAndCrl()
         throws LoadCertifsAndCrlException {

      synchronized (IgcService.class) {

         createCertifsAndCrl();
      }

      return certifsAndCrl;
   }

   private void createCertifsAndCrl() throws LoadCertifsAndCrlException {

      DateTime systemDate = new DateTime();

      // Détermine s'il faut charger ou recharger les CRL en mémoire
      // depuis les fichiers .CRL
      boolean mustLoad = false;
      boolean isFirstLoad = false; // utile pour le log un peu plus bas
      boolean isReload = false; // utile pour le log un peu plus bas
      if ((certifsAndCrl == null) || (certifsAndCrl.getDateMajCrl() == null)) {
         mustLoad = true;
         isFirstLoad = true;
      }
      else if (DateTimeUtils.diffHours(certifsAndCrl.getDateMajCrl(),systemDate) > VALIDATE_TIME) {
         mustLoad = true;
         isReload = true;
      }
      
      // Recharge les CRL si nécessaires
      if (mustLoad) {
      
         // Ajout d'un log pour indiquer que l'on recharge les CRL
         if (isFirstLoad) {
            LOG.info(CRL_FIRST_LOAD);
         } else if (isReload) {
            
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH'h'mm");
            
            String dateFormatee = certifsAndCrl.getDateMajCrl().toString(fmt); 
            
            LOG.info(
                  TextUtils.getMessage(
                        CRL_RELOAD, 
                        dateFormatee));
         }
         
         
         // Chargement des fichiers .crl dans des objets X509CRL
         List<X509CRL> crls = loadCRLResources(igcConfig.getRepertoireCRLs());

         
         // Construction de l'objet contenant les CRL
         CertifsAndCrl instance = new CertifsAndCrl();

         instance.setDateMajCrl(systemDate);
         instance.setCertsAcRacine(certsAcRacine);
         instance.setCrl(crls);

         this.setCertifsAndCrl(instance);
         
         // Ajout d'un log pour indiquer le nombre de CRL chargée en mémoire
         LOG.info(
               TextUtils.getMessage(
                     CRL_COUNT, 
                     String.valueOf(this.certifsAndCrl.getCrl().size())));
         
      }
   }

   private void setCertifsAndCrl(CertifsAndCrl certifsAndCrl) {

      this.certifsAndCrl = certifsAndCrl;
   }

   private static List<X509CRL> loadCRLResources(String repertoireCRLs)
         throws LoadCertifsAndCrlException {

      FileSystemResource repCRLs = new FileSystemResource(repertoireCRLs);
      List<Resource> resources = ResourceUtils.loadResources(repCRLs, 
            new String[] { "crl","Crl","cRl","crL","CRl","CrL","cRL","CRL" });
      
      List<X509CRL> crls = new ArrayList<X509CRL>();

      for (Resource crl : resources) {

         try {
            LOG.debug("loading CRL:" + crl.getFilename());
            InputStream input = crl.getInputStream();

            try {
               crls.add(SecurityUtils.loadCRL(input));
            } catch (GeneralSecurityException e) {
               LOG.error("erreur de chargement du fichier CRL: " + crl.getURI(), e);
               throw new LoadCertifsAndCrlException(CRL_ERROR, e);
            } finally {

               input.close();
            }
         } catch (IOException e) {

            LOG.error("erreur de chargement du fichier CRL: " + crl.getFilename(), e);
            throw new LoadCertifsAndCrlException(CRL_ERROR, e);
         }

      }

      return crls;

   }

}
