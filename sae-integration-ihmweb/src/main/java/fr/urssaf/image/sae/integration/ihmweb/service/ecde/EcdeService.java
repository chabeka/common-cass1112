package fr.urssaf.image.sae.integration.ihmweb.service.ecde;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSource;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSources;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.resultats.ResultatsType;
import fr.urssaf.image.sae.integration.ihmweb.modele.somres.sommaire.SommaireType;
import fr.urssaf.image.sae.integration.ihmweb.utils.ChecksumUtils;
import fr.urssaf.image.sae.integration.ihmweb.utils.JAXBUtils;

/**
 * Service de manipulation de l'ECDE
 */
@Service
public class EcdeService {

   // @Autowired
   // private TestConfig testConfig;

   @Autowired
   private EcdeSources ecdeSources;
   
   @Autowired
   private TestConfig testConfig;

   /**
    * Convertit une URL ECDE en un chemin de fichier<br>
    * La conversion est faite à LA RACHE
    * 
    * @param urlEcde
    *           l'URL ECDE
    * 
    * @return le chemin de fichier correspondant
    */
   public final String convertUrlEcdeToPath(String urlEcde) {

      if (StringUtils.isBlank(urlEcde)) {
         throw new IntegrationRuntimeException(
               "Impossible de convertir l'URL ECDE en chemin de fichier : l'URL ECDE est vide");
      }

      if (ecdeSources.getSources() == null
            || ecdeSources.getSources().isEmpty()) {
         throw new IntegrationRuntimeException(
               "Aucune correspondance définie dans le fichier");
      }

      List<EcdeSource> tabEcdeSrc = ecdeSources.getSources();
      File cheminReseau = null;
      String debutUrlEcde = null;
      int i = 0;
      while (cheminReseau == null && i < tabEcdeSrc.size()) {
         debutUrlEcde = "ecde://" + tabEcdeSrc.get(i).getHost();
         if (urlEcde.startsWith(debutUrlEcde)) {
            cheminReseau = tabEcdeSrc.get(i).getBasePath();
         }
         i++;
      }

      if (cheminReseau != null) {

         StringBuffer sBuf = new StringBuffer();

         sBuf.append(cheminReseau.getAbsolutePath());
         sBuf.append(StringUtils.removeStart(urlEcde, debutUrlEcde));

         return sBuf.toString();

      } else {
         throw new IntegrationRuntimeException(
               "Impossible de convertir l'URL ECDE '" + urlEcde
                     + "' en chemin de fichier.");
      }

   }

   /**
    * Calcul le SHA-1 du fichier de l'ECDE dont l'URL ECDE est passé en
    * paramètre
    * 
    * @param urlEcde
    *           l'URL ECDE
    * 
    * @return le SHA-1 du fichier, sous la forme d'une chaîne de caractères hexa
    */
   public final String sha1(String urlEcde) {

      String cheminFichier = convertUrlEcdeToPath(urlEcde);

      return ChecksumUtils.sha1(cheminFichier);

   }

   /**
    * Charge un fichier resultats.xml et renvoie l'objet le réprésentant
    * 
    * @param cheminFichierResultats
    *           le chemin du fichier resultats.xml
    * @return l'objet représentant le fichier resultats.xml
    */
   public final ResultatsType chargeResultatsXml(String cheminFichierResultats) {

      try {

         ClassPathResource xsdResultats = new ClassPathResource(
               "xsd_som_res/resultats.xsd");

         File xsdFile = xsdResultats.getFile();

         Class<ResultatsType> docClass = ResultatsType.class;

         FileInputStream input = new FileInputStream(cheminFichierResultats);

         return JAXBUtils.unmarshal(docClass, input, xsdFile, null);

      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      } catch (JAXBException e) {
         throw new IntegrationRuntimeException(e);
      } catch (SAXException e) {
         throw new IntegrationRuntimeException(e);
      }
   }

   /**
    * Charge un fichier sommaire.xml dans un objet SommaireType
    * 
    * @param urlEcdeSommaire
    *           l'URL ECDE du sommaire
    * @return l'objet représentant le sommaire.xml
    */
   public final SommaireType chargeSommaireXml(String urlEcdeSommaire) {

      try {

         ClassPathResource xsdResultats = new ClassPathResource(
               "xsd_som_res/sommaire.xsd");

         File xsdFile = xsdResultats.getFile();

         Class<SommaireType> docClass = SommaireType.class;

         String cheminFichierSommaire = convertUrlEcdeToPath(urlEcdeSommaire);

         FileInputStream input = new FileInputStream(cheminFichierSommaire);

         return JAXBUtils.unmarshal(docClass, input, xsdFile, null);

      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      } catch (JAXBException e) {
         throw new IntegrationRuntimeException(e);
      } catch (SAXException e) {
         throw new IntegrationRuntimeException(e);
      }

   }
   
   
   /**
    * Construit une URL ECDE<br>
    * Objectif : mutualiser le DNS, pour pouvoir le changer facilement
    *            en cas de modification d'ECDE
    * @param apresDns ce qu'il faut mettre après la partie DNS de l'ECDE
    * @return
    */
   public String construitUrlEcde(String apresDns) {
      
      String prefixe = "ecde://";
      
      String dns = testConfig.getDnsEcde();
      
      if (!apresDns.startsWith("/")) {
         apresDns = '/' + apresDns;
      }
      
      String result = prefixe + dns + apresDns;
      
      return result;
      
   }
   
   
   /**
    * Construit une URL ECDE pour une source donnée<br>
    * Objectif : générer une url ECDE pour une source donnée
    * 
    * @param apresDns ce qu'il faut mettre après la partie DNS de l'ECDE
    * @param index la position de la sourc e souhaitée
    * @return
    */
   public String construitUrlEcde(String apresDns, int index) {
      
      String prefixe = "ecde://";
      
      List<EcdeSource> listeSources = ecdeSources.getSources();
      
      String dns = listeSources.get(index).getHost();
      
      if (!apresDns.startsWith("/")) {
         apresDns = '/' + apresDns;
      }
      
      String result = prefixe + dns + apresDns;
      
      return result;
      
   }
   

}
