/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.verification.impl;

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
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.utils.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.flag.DebutTraitementFlagSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.flag.FinTraitementFlagSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.flag.model.DebutTraitementFlag;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.verification.VerificationSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

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

   private static final String LIBELLE_BUL004 = "La modification en masse en mode 'Partiel' a été interrompue. "
         + "Une procédure d'exploitation doit être initialisée afin de rejouer le traitement en echec.";

   private static final String LIBELLE_BUL005 = "Le transfert de masse en mode 'Partiel' a été interrompue. "
         + "Une procédure d'exploitation doit être initialisée afin de rejouer le traitement en echec.";

   private static final String CATCH = "AvoidCatchingThrowable";



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
    * Verification du debut du traitement.
    * 
    * @param repTravail
    *           repertoire père
    * @param typeJob
    *           Type de job
    * @throws UnknownHostException
    *            @{@link UnknownHostException}
    */
   private void checkDebutTraitement(File repTravail, UUID idTraitement,
         TYPES_JOB typeJob)
         throws UnknownHostException {

      File debut = new File(repTravail, "debut_traitement.flag");

      if (!debut.exists()) {

         LOGGER.error("Génération de secours du "
               + "fichier debut_traitement.flag car il n'a "
               + "pas été généré par le job de " + typeJob.name());

         InetAddress hostInfo = InetAddress.getLocalHost();

         DebutTraitementFlag flag = new DebutTraitementFlag();
         flag.setStartDate(new Date());
         flag.setIdTraitement(idTraitement);
         flag.setHostInfo(hostInfo);
         debutSupport.writeDebutTraitementFlag(flag, repTravail);
      }

   }

   /**
    * Controle si le fichier resultat est existant, sinon il le cree avec les
    * informations necessaires.
    * 
    * @param repTravail
    *           Dossier du fichier resultat.xml
    * @param nbreDocs
    *           Nombre de documents total
    * @param nbreStockes
    *           Nombre de documents intégrés
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param listeDocsIntegres
    *           Liste des documents intégrés
    * @param typeJob
    *           Le type du job qui demande le check
    */
   private void checkResultats(File repTravail, Integer nbreDocs,
         Integer nbreStockes, String batchModeTraitement,
         List<Throwable> listeErreurs, File sommaire,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> listeDocsIntegres,
         TYPES_JOB typeJob) {

      File resultats = new File(repTravail, "resultats.xml");

      if (!resultats.exists()) {

         int nbreTotal = checkCountDocs(nbreDocs, sommaire);
         int nbreIntegres = checkIntegratedDocs(nbreStockes);
         // Récupération du mode du batch si non renseigné.
         if (StringUtils.isBlank(batchModeTraitement)) {
            batchModeTraitement = XmlReadUtils.getElementValue(sommaire,
                  Constantes.BATCH_MODE_ELEMENT_NAME);

            if (BatchModeType.fromValue(batchModeTraitement) == null) {
               throw new CaptureMasseRuntimeException(String.format(
                     "Le mode du batch %s est inconnu.", batchModeTraitement));
            }
         }

         CaptureMasseErreur erreur = convertListToErreur(listeErreurs,
               nbreIntegres, typeJob);

         resultatsSupport.writeResultatsFile(repTravail, sommaire, erreur,
               nbreTotal, nbreIntegres, batchModeTraitement, listeDocsIntegres);
      }

   }

   /**
    * Convertit la liste d'exception en bean erreur.
    * 
    * @param listeErreurs
    *           liste des erreurs
    * @param nbreIntegres
    *           Nombre de documents intégrés
    * @param typeJob
    *           Le type du job qui demande le check
    * @return L'erreur de capture de masse
    */
   private CaptureMasseErreur convertListToErreur(List<Throwable> listeErreurs,
         int nbreIntegres, TYPES_JOB typeJob) {

      String messageErreur = null, codeErreur = null;

      String messException = "Génération de secours du fichier resultats.xml car il n'a pas été généré par le job de "
            + typeJob.name() + ". Détails : Erreur ";

      if (nbreIntegres > 0) {
         if (TYPES_JOB.capture_masse.equals(typeJob)) {
            messageErreur = LIBELLE_BUL003;
            codeErreur = Constantes.ERR_BUL003;
            LOGGER.error(messException + Constantes.ERR_BUL003);
         } else if (TYPES_JOB.modification_masse.equals(typeJob)) {
            messageErreur = LIBELLE_BUL004;
            codeErreur = Constantes.ERR_MO_BUL001;
            LOGGER.error(messException + Constantes.ERR_MO_BUL001);
         } else if (TYPES_JOB.transfert_masse.equals(typeJob)) {
            messageErreur = LIBELLE_BUL005;
            codeErreur = Constantes.ERR_TR_BUL001;
            LOGGER.error(messException + Constantes.ERR_TR_BUL001);
         }

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

         LOGGER.error(messException + Constantes.ERR_BUL001
               + " avec le message : " + messageErreur);
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
    * @param typeJob
    */
   private void checkFinTraitement(File repTravail, TYPES_JOB typeJob) {

      File fin = new File(repTravail, "fin_traitement.flag");

      if (!fin.exists()) {

         LOGGER.error("Génération de secours du "
               + "fichier fin_traitement.flag car il n'a "
               + "pas été généré par le job de " + typeJob.name());

         finSupport.writeFinTraitementFlag(repTravail);
      }

   }

   /**
    * @param nbreStockes
    * @param idTraitement
    * @param logPresent
    * @param typeJob
    */
   private void checkLogs(Integer nbreStockes, UUID idTraitement,
         boolean logPresent, TYPES_JOB typeJob) {

      if (!logPresent && nbreStockes != null && nbreStockes > 0) {
         LOGGER.error("Génération de secours du log ERROR "
               + "de rollback par procédure d'exploitation "
               + "car il n'a pas été généré par le job de " + typeJob.name());

         LOGGER.error("Le traitement de masse n°{} doit éventuellement être rollbacké "
               + "par une procédure d'exploitation (il faut faire une analyse au préalable).",
               idTraitement);
      }

   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings(CATCH)
   @Override
   public void checkFinTraitement(
         URI sommaireURL,
         Integer nbreDocs,
         Integer nbreStockes,
         String batchModeTraitement,
         boolean logPresent,
         List<Throwable> erreurs,
         UUID idTraitement,
         ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> listeDocsIntegres,
         TYPES_JOB typeJob) {

      LOGGER.debug("{} - debut", CHECK);

      try {

         File sommaire = checkSommaire(sommaireURL);

         File repTravail = sommaire.getParentFile();

         checkDebutTraitement(repTravail, idTraitement, typeJob);

         checkResultats(repTravail, nbreDocs, nbreStockes, batchModeTraitement,
               erreurs, sommaire, listeDocsIntegres, typeJob);

         checkFinTraitement(repTravail, typeJob);

         checkLogs(nbreStockes, idTraitement, logPresent, typeJob);

         LOGGER.debug("{} - fin", CHECK);

         /* erreurs de vérification du fichier sommaire */
      } catch (Throwable e) {
         LOGGER.warn(
               "une erreur est survenue lors de la vérification de fin de traitement",
               CHECK, e);
      }
   }

}
