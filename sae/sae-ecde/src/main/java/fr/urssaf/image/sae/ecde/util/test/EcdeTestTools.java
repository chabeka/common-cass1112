package fr.urssaf.image.sae.ecde.util.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Classe de méthodes utilitaires pour les tests unitaires qui ont besoin
 * d'utiliser l'ECDE
 * 
 */
public class EcdeTestTools {

   @Autowired
   private EcdeSources ecdeSources;

   /**
    * Methode qui renvoi un objet EcdeTestSommaire <br>
    * afin d'indiquer l'URL ECDE ainsi que l'arborescence complète <br>
    * du répertoire où se trouve le fichier sommaire.xml <br>
    * exemple de valeur renvoyées :<br>
    * ecde://ecde.testunitaire.recouv/1/20110101/25469444564/sommaire.xml<br>
    * et<br>
    * C:/Documents ans Settings/User/Local Settings/temp/1/20110101/25469444564/<br>
    * 
    * @return EcdeTestSommaire ecdeTestSommaire
    */
   public final EcdeTestSommaire buildEcdeTestSommaire() {

      // Récupération du point de montage de l'ECDE pour les TU
      File pointMontageTu = null;
      for (EcdeSource ecde : ecdeSources.getSources()) {
         if (EcdeTestConfig.DNS_ECDE_TU.equals(ecde.getHost())) {
            pointMontageTu = ecde.getBasePath();
         }
      }
      if (pointMontageTu == null) {
         throw new EcdeRuntimeException(
               "Impossible de retrouver le point de montage de l'ECDE "
                     + EcdeTestConfig.DNS_ECDE_TU);
      }

      // Création du répertoire unique de traitement
      String sousRepTraitement = "/1/20110101/".concat(buildRandom());
      File repTrait = new File(pointMontageTu, sousRepTraitement);
      try {
         FileUtils.forceMkdir(repTrait);
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }

      // Création du sous-répertoire documents
      File repDoc = new File(repTrait, "documents");
      try {
         FileUtils.forceMkdir(repDoc);
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }

      // Construction de l'URL ECDE pointant vers un fichier
      // sommaire.xml pour l'instant inexistant
      URI uri;
      try {
         uri = new URI("ecde", EcdeTestConfig.DNS_ECDE_TU, sousRepTraitement
               .concat("/sommaire.xml"), null);
      } catch (URISyntaxException e) {
         throw new EcdeRuntimeException(e);
      }

      // Valeur de retour
      EcdeTestSommaire ecdeTestSommaire = new EcdeTestSommaire();
      ecdeTestSommaire.setRepEcde(repTrait);
      ecdeTestSommaire.setUrlEcde(uri);
      return ecdeTestSommaire;

   }

   /**
    * Methode qui renvoi un objet EcdeTestSommaire <br>
    * afin d'indiquer l'URL ECDE ainsi que l'arborescence complète <br>
    * du répertoire où se trouve le fichier sommaire.xml <br>
    * exemple de valeur renvoyées :<br>
    * ecde://ecde.testunitaire.recouv/1/20110101/25469444564/repertoire/
    * attestation.pdf<br>
    * et<br>
    * C:/Documents ans Settings/User/Local
    * Settings/temp/1/20110101/25469444564/documents/<br>
    * 
    * @param nomDuFichierDoc
    *           nom du fichier du document
    * @return EcdeTestSommaire ecdeTestSommaire
    */
   public final EcdeTestDocument buildEcdeTestDocument(String nomDuFichierDoc) {

      // Récupération du point de montage de l'ECDE pour les TU
      File pointMontageTu = null;
      for (EcdeSource ecde : ecdeSources.getSources()) {
         if (EcdeTestConfig.DNS_ECDE_TU.equals(ecde.getHost())) {
            pointMontageTu = ecde.getBasePath();
         }
      }
      if (pointMontageTu == null) {
         throw new EcdeRuntimeException(
               "Impossible de retrouver le point de montage de l'ECDE "
                     + EcdeTestConfig.DNS_ECDE_TU);
      }

      // Création du répertoire unique de traitement
      String sousRepTraitement = "/1/20110101/".concat(buildRandom());
      File repTrait = new File(pointMontageTu, sousRepTraitement);
      try {
         FileUtils.forceMkdir(repTrait);
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }

      // Création du sous-répertoire documents
      File repDoc = new File(repTrait, "documents");
      try {
         FileUtils.forceMkdir(repDoc);
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }

      // Construction de l'URL ECDE pointant vers le chemin du fichier document
      URI uri;
      try {
         uri = new URI("ecde", EcdeTestConfig.DNS_ECDE_TU, sousRepTraitement
               .concat("/documents/").concat(nomDuFichierDoc), null);
      } catch (URISyntaxException e) {
         throw new EcdeRuntimeException(e);
      }

      // Valeur de retour
      EcdeTestDocument ecdeTestDocument = new EcdeTestDocument();
      ecdeTestDocument.setRepEcdeDocuments(repDoc);
      ecdeTestDocument.setUrlEcdeDocument(uri);
      return ecdeTestDocument;

   }

   /**
    * Construit un nom unique de fichier temporaire
    * 
    * @param prefixe
    *           un éventuel préfixe
    * @param suffixe
    *           un éventuel suffixe
    * 
    * @return le nom unique de fichier temporaire
    */
   public static String getTemporaryFileName(String prefixe, String suffixe) {

      // NB : Il n'est pas possible de tester unitairement l'intégralité du
      // résultat de cette
      // méthode car elle contient un calcul de nombre aléatoire ainsi qu'une
      // date
      // correspondant à "maintenant"

      // Création de l'objet résultat
      StringBuffer nomFicTemp = new StringBuffer();

      // 1ère partie du nom : le préfixe
      if (prefixe != null) {
         nomFicTemp.append(prefixe);
      }

      // 2ème partie du nom : la date de maintenant, de l'année à la
      // milli-secondes
      final Date dMaintenant = new Date();
      final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS",
            Locale.FRENCH);
      nomFicTemp.append(dateFormat.format(dMaintenant));

      // 3ème partie du nom : un nombre aléatoire
      // L'algorithme utilisé est emprunté à java.io.File.createTempFile
      nomFicTemp.append('_');
      nomFicTemp.append(buildRandom());

      // Dernière partie du nom : le suffixe
      if (suffixe != null) {
         nomFicTemp.append(suffixe);
      }

      // Renvoie du résultat
      return nomFicTemp.toString();

   }

   /**
    * Suppression des répertoires et fichiers temporaires
    * 
    * @param ecdeTestSommaire
    *           ecdeTestSommaire
    * @throws IOException
    *            exception levée en cas d'erreur d'accès au répertoire
    */
   public final void cleanEcdeTestSommaire(EcdeTestSommaire ecdeTestSommaire)
         throws IOException {
      File parent = ecdeTestSommaire.getRepEcde().getParentFile()
            .getParentFile().getParentFile();
      FileUtils.deleteDirectory(parent);
   }

   /**
    * Suppression des répertoires et fichiers temporaires
    * 
    * @param ecdeTestDocument
    *           ecdeTestDocument
    * @throws IOException
    *            exception levée en cas d'erreur d'accès au répertoire
    */
   public final void cleanEcdeTestDocument(EcdeTestDocument ecdeTestDocument)
         throws IOException {
      File parent = ecdeTestDocument.getRepEcdeDocuments().getParentFile()
            .getParentFile().getParentFile().getParentFile();
      FileUtils.deleteDirectory(parent);
   }

   private static String buildRandom() {

      final SecureRandom random = new SecureRandom();
      long nextLong = random.nextLong();
      if (nextLong == Long.MIN_VALUE) {
         nextLong = 0; // corner case
      } else {
         nextLong = Math.abs(nextLong);
      }

      return String.valueOf(nextLong);

   }

}
