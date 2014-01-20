package fr.urssaf.image.sae.webservices.security.igc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.TextUtils;
import fr.urssaf.image.sae.webservices.security.SecurityUtils;
import fr.urssaf.image.sae.webservices.security.igc.exception.LoadCertifsAndCrlException;
import fr.urssaf.image.sae.webservices.security.igc.modele.CertifsAndCrl;
import fr.urssaf.image.sae.webservices.support.TracesWsSupport;
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
public class IgcService {

   private static final Logger LOG = LoggerFactory.getLogger(IgcService.class);

   private CertifsAndCrl certifsAndCrl;

   private final IgcConfigs igcConfigs;

   private final Map<X509Certificate, String> certsAcRacine;

   private static final int VALIDATE_TIME = 24;

   private static final String[] CRL_EXTENSIONS = new String[] { "crl", "Crl",
         "cRl", "crL", "CRl", "CrL", "cRL", "CRL" };

   private final TracesWsSupport tracesWsSupport;

   public static final String CRL_ERROR = "Une erreur s'est produite lors du chargement des CRL";

   public static final String AC_RACINE_ERROR = "Une erreur s'est produite lors du chargement des certificats des AC racine de confiance";

   public static final String AC_RACINE_EMPTY = "Aucun certificat d'AC racine de confiance trouvé pour le fichier ${0}";

   public static final String CRL_FIRST_LOAD = "Chargement des CRL en mémoire depuis les fichiers .crl pour la première fois";

   public static final String CRL_RELOAD = "Rechargement des CRL en mémoire depuis les fichiers .crl car les informations en mémoire sont périmées (date du chargement en mémoire précédent : ${0})";

   public static final String CRL_COUNT = "${0} CRL chargée(s) en mémoire";

   public static final String AC_RACINE_LOAD = "Chargement en mémoire des certificats des AC racine depuis les fichiers .crt";

   public static final String AC_RACINE_COUNT = "${0} certificat(s) d'AC racine de confiance chargé(s) en mémoire";

   /**
    * Instanciation de {@link IgcService}
    * 
    * @param igcConfigs
    *           liste des configuration de l'IGC
    * @param tracesWsSupport
    *           le bean de support de la traçabilité
    */
   @Autowired
   public IgcService(IgcConfigs igcConfigs, TracesWsSupport tracesWsSupport) {

      this.igcConfigs = igcConfigs;
      this.certsAcRacine = new HashMap<X509Certificate, String>();
      this.tracesWsSupport = tracesWsSupport;

   }

   /**
    * 
    * Chargement des certificats d'AC racines<br>
    * Les certificats sont récupérés dans le repertoire indiqué par
    * {@link IgcConfig#getAcRacine()}<br>
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
   protected final void chargementCertificatsACRacine() {

      LOG.info(AC_RACINE_LOAD);

      List<String> certificats = new ArrayList<String>();

      List<File> fichiersAcRacine = new ArrayList<File>();

      for (IgcConfig igcConfig : igcConfigs.getIgcConfigs()) {

         InputStream input = null;
         try {

            LOG.info("Chargement du certificat d'AC racine {}", igcConfig
                  .getAcRacine());

            File crt = new File(igcConfig.getAcRacine());
            fichiersAcRacine.add(crt);
            input = new FileInputStream(crt);

            try {
               certsAcRacine.put(SecurityUtils.loadCertificat(input), igcConfig
                     .getPkiIdent());
               certificats.add(igcConfig.getAcRacine());

            } catch (CertificateException e) {

               LOG.error("erreur lors du chargement du certificat : "
                     + crt.getName());
               throw new IllegalArgumentException(AC_RACINE_ERROR, e);
            }

         } catch (IOException e) {
            throw new IllegalArgumentException(TextUtils.getMessage(
                  AC_RACINE_EMPTY, igcConfig.getAcRacine()), e);

         } finally {
            if (input != null) {
               try {
                  input.close();
               } catch (IOException e) {
                  LOG.info("impossible de fermer le flux");
               }
            }
         }
      }

      if (MapUtils.isEmpty(certsAcRacine)) {
         throw new IllegalArgumentException(TextUtils.getMessage(
               AC_RACINE_EMPTY, StringUtils.join(certificats, ",")));
      }

      LOG.info(TextUtils.getMessage(AC_RACINE_COUNT, String
            .valueOf(this.certsAcRacine.size())));

      // Trace l'événement
      // "WS - Chargement en mémoire des certificats d'AC racine"
      tracesWsSupport.traceChargementCertAcRacine(fichiersAcRacine);

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
    * {@link IgcConfig#getCrlsRep()}<br>
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
      } else if (DateTimeUtils.diffHours(certifsAndCrl.getDateMajCrl(),
            systemDate) > VALIDATE_TIME) {
         mustLoad = true;
         isReload = true;
      }

      // Chargement des certificats d'AC racine si nécessaire
      if (isFirstLoad) {
         chargementCertificatsACRacine();
      }

      // Recharge les CRL si nécessaires
      if (mustLoad) {

         // Ajout d'un log pour indiquer que l'on recharge les CRL
         if (isFirstLoad) {
            LOG.info(CRL_FIRST_LOAD);
         } else if (isReload) {

            DateTimeFormatter fmt = DateTimeFormat
                  .forPattern("dd/MM/yyyy HH'h'mm");

            String dateFormatee = certifsAndCrl.getDateMajCrl().toString(fmt);

            LOG.info(TextUtils.getMessage(CRL_RELOAD, dateFormatee));
         }

         List<X509CRL> crls = new ArrayList<X509CRL>();
         List<File> fichiersCrl = new ArrayList<File>();

         for (IgcConfig igcConfig : igcConfigs.getIgcConfigs()) {

            // Chargement des fichiers .crl dans des objets X509CRL
            crls.addAll(loadCRLResources(igcConfig.getCrlsRep(), fichiersCrl));

         }

         // Construction de l'objet contenant les CRL
         CertifsAndCrl instance = new CertifsAndCrl();

         instance.setDateMajCrl(systemDate);
         instance.setCertsAcRacine(certsAcRacine);
         instance.setCrl(crls);

         this.setCertifsAndCrl(instance);

         // Ajout d'un log pour indiquer le nombre de CRL chargée en mémoire
         LOG.info(TextUtils.getMessage(CRL_COUNT, String
               .valueOf(this.certifsAndCrl.getCrl().size())));

         // Trace l'événement
         // "WS - Chargement en mémoire des CRL"
         tracesWsSupport.traceChargementCRL(fichiersCrl);

      }
   }

   private void setCertifsAndCrl(CertifsAndCrl certifsAndCrl) {

      this.certifsAndCrl = certifsAndCrl;
   }

   private static List<X509CRL> loadCRLResources(String repertoireCRLs,
         List<File> fichiersCrl) throws LoadCertifsAndCrlException {

      FileSystemResource repCRLs = new FileSystemResource(repertoireCRLs);
      List<Resource> resources = ResourceUtils.loadResources(repCRLs,
            CRL_EXTENSIONS);

      List<X509CRL> crls = new ArrayList<X509CRL>();

      for (Resource crl : resources) {

         try {
            LOG.debug("loading CRL:" + crl.getFilename());
            fichiersCrl.add(crl.getFile());
            InputStream input = crl.getInputStream();

            try {
               crls.add(SecurityUtils.loadCRL(input));
            } catch (GeneralSecurityException e) {
               LOG.error(
                     "erreur de chargement du fichier CRL: " + crl.getURI(), e);
               throw new LoadCertifsAndCrlException(CRL_ERROR, e);
            } finally {

               input.close();
            }
         } catch (IOException e) {

            LOG.error("erreur de chargement du fichier CRL: "
                  + crl.getFilename(), e);
            throw new LoadCertifsAndCrlException(CRL_ERROR, e);
         }

      }

      return crls;

   }

   /**
    * Retourne la liste des issuers en fonction des id des PKI
    * 
    * @return la map des issuers
    */
   public final Map<String, List<String>> getPatternIssuers() {

      Map<String, List<String>> map = null;

      if (CollectionUtils.isNotEmpty(igcConfigs.getIgcConfigs())) {
         map = new HashMap<String, List<String>>();
         for (IgcConfig igcConfig : this.igcConfigs.getIgcConfigs()) {
            map.put(igcConfig.getPkiIdent(), igcConfig.getIssuerList()
                  .getIssuers());
         }
      }

      return map;
   }
}
