/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.verification.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.verification.VerificationSupport;
import fr.urssaf.image.sae.services.util.XmlReadUtils;

/**
 * Classe d'implémentation du serivce {@link VerificationSupport}
 * 
 */
@Component
public class VerificationSupportImpl implements VerificationSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VerificationSupportImpl.class);

   private static final String CHECK = "checkFinTraitement()";

   @Autowired
   private DebutTraitementFlagSupport debutSupport;

   @Autowired
   private FinTraitementFlagSupport finSupport;

   @Autowired
   private ResultatsFileEchecSupport resultatsSupport;

   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   private static final String LIBELLE_BUL003 = "La capture de masse en mode "
         + "\"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void checkFinTraitement(URI urlEcde, Integer nbreDocs,
         Integer nbreStockes, boolean logPresent, List<Throwable> erreurs,
         UUID idTraitement) {

      LOGGER.debug("{} - debut", CHECK);

      try {

         File sommaire = checkSommaire(urlEcde);

         File repTravail = sommaire.getParentFile();

         checkDebutTraitement(repTravail, idTraitement);

         checkResultats(repTravail, nbreDocs, nbreStockes, erreurs, sommaire);

         checkFinTraitement(repTravail);

         checkLogs(nbreStockes, idTraitement, logPresent);

         LOGGER.debug("{} - fin", CHECK);

      } catch (Throwable e) {
         LOGGER
               .warn(
                     "une erreur est survenue lors de la vérification de fin de traitement",
                     CHECK, e);
      }
   }

   /**
    * @param urlEcde
    * @return
    * @throws CaptureMasseSommaireFileNotFoundException
    * @throws CaptureMasseSommaireEcdeURLException
    */
   private File checkSommaire(URI urlEcde)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {

      return fileSupport.convertURLtoFile(urlEcde);

   }

   /**
    * @param repTravail
    *           repertoire père
    * @throws UnknownHostException
    */
   private void checkDebutTraitement(File repTravail, UUID idTraitement)
         throws UnknownHostException {

      File debut = new File(repTravail, "debut_traitement.flag");

      if (!debut.exists()) {

         LOGGER.error("Génération de secours du "
               + "fichier debut_traitement.flag car il n'a "
               + "pas été généré par le job de capture de masse");

         InetAddress hostInfo = InetAddress.getLocalHost();

         DebutTraitementFlag flag = new DebutTraitementFlag();
         flag.setStartDate(new Date());
         flag.setIdTraitement(idTraitement);
         flag.setHostInfo(hostInfo);
         debutSupport.writeDebutTraitementFlag(flag, repTravail);
      }

   }

   /**
    * @param repTravail
    * @param nbreDocs
    * @param nbreStockes
    */
   private void checkResultats(File repTravail, Integer nbreDocs,
         int nbreStockes, List<Throwable> listeErreurs, File sommaire) {

      File resultats = new File(repTravail, "resultats.xml");

      if (!resultats.exists()) {

         int nbreTotal = checkCountDocs(nbreDocs, sommaire);
         int nbreIntegres = checkIntegratedDocs(nbreStockes);

         CaptureMasseErreur erreur = convertListToErreur(listeErreurs,
               nbreIntegres);

         resultatsSupport.writeResultatsFile(repTravail, sommaire, erreur,
               nbreTotal);
      }

   }

   /**
    * @param listeErreurs
    * @return
    */
   private CaptureMasseErreur convertListToErreur(List<Throwable> listeErreurs,
         int nbreIntegres) {

      String messageErreur, codeErreur;

      if (nbreIntegres > 0) {
         messageErreur = LIBELLE_BUL003;
         codeErreur = Constantes.ERR_BUL003;
         LOGGER.error("Génération de secours du fichier "
               + "resultats.xml car il n'a pas été généré "
               + "par le job de capture de masse. Détails : Erreur "
               + Constantes.ERR_BUL003);

      } else {
         codeErreur = Constantes.ERR_BUL001;

         // concaténation de toutes les erreurs en une seule
         StringBuffer buffer = new StringBuffer();
         StringWriter stringWriter = new StringWriter();

         PrintWriter printWriter = new PrintWriter(stringWriter);

         for (Throwable erreur : listeErreurs) {

            erreur.printStackTrace(printWriter);
         }

         buffer.append(stringWriter.getBuffer());

         printWriter.close();

         try {
            stringWriter.close();
         } catch (IOException e) {
            LOGGER.debug("erreur fermeture stringwriter", e);
         }

         messageErreur = buffer.toString();

         LOGGER.error("Génération de secours du fichier resultats.xml "
               + "car il n'a pas été généré par le job "
               + "de capture de masse. Détails : Erreur "
               + Constantes.ERR_BUL001 + " avec le message : " + messageErreur);
      }

      List<Exception> exceptions = new ArrayList<Exception>();
      exceptions.add(new Exception(messageErreur));

      List<Integer> listIndex = new ArrayList<Integer>();
      listIndex.add(0);

      List<String> listCodes = new ArrayList<String>();
      listCodes.add(codeErreur);

      CaptureMasseErreur captureMasseErreur = new CaptureMasseErreur();
      captureMasseErreur.setListException(exceptions);
      captureMasseErreur.setListIndex(listIndex);
      captureMasseErreur.setListCodes(listCodes);

      return captureMasseErreur;
   }

   private int checkCountDocs(Integer nbreDocs, File sommaire) {
      int total;

      if (nbreDocs == null) {
         total = XmlReadUtils.compterElements(sommaire, "document");
      } else {
         total = nbreDocs.intValue();
      }
      return total;
   }

   private int checkIntegratedDocs(Integer nbreStockes) {
      int total;

      if (nbreStockes == null) {
         total = 0;
      } else {
         total = nbreStockes.intValue();
      }
      return total;
   }

   /**
    * @param repTravail
    */
   private void checkFinTraitement(File repTravail) {

      File fin = new File(repTravail, "fin_traitement.flag");

      if (!fin.exists()) {

         LOGGER.error("Génération de secours du "
               + "fichier fin_traitement.flag car il n'a "
               + "pas été généré par le job de capture de masse");

         finSupport.writeFinTraitementFlag(repTravail);
      }

   }

   /**
    * @param nbreStockes
    * @param idTraitement
    * @param logPresent
    */
   private void checkLogs(Integer nbreStockes, UUID idTraitement,
         boolean logPresent) {

      if (!logPresent && nbreStockes > 0) {
         LOGGER.error("Génération de secours du log ERROR "
               + "de rollback par procédure d'exploitation "
               + "car il n'a pas été généré par le job de capture de masse");

         LOGGER
               .error(

                     "Le traitement de masse n°{} doit éventuellement être rollbacké "
                           + "par une procédure d'exploitation (il faut faire une analyse au préalable).",
                     idTraitement);
      }

   }
}
