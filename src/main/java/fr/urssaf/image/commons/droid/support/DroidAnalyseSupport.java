package fr.urssaf.image.commons.droid.support;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import uk.gov.nationalarchives.droid.command.container.AbstractContainerContentIdentifier;
import uk.gov.nationalarchives.droid.command.container.Ole2ContainerContentIdentifier;
import uk.gov.nationalarchives.droid.command.container.ZipContainerContentIdentifier;
import uk.gov.nationalarchives.droid.container.ContainerFileIdentificationRequestFactory;
import uk.gov.nationalarchives.droid.container.ContainerSignatureDefinitions;
import uk.gov.nationalarchives.droid.container.TriggerPuid;
import uk.gov.nationalarchives.droid.container.ole2.Ole2IdentifierEngine;
import uk.gov.nationalarchives.droid.container.zip.ZipIdentifierEngine;
import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationRequest;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResult;
import uk.gov.nationalarchives.droid.core.interfaces.IdentificationResultCollection;
import uk.gov.nationalarchives.droid.core.interfaces.archive.IdentificationRequestFactory;
import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;

/**
 * Classe de support pour l'analyse de format par DROID.<br>
 * <br>
 * L'implémentation est FORTEMENT inspirée de :
 * <ul>
 * <li>uk.gov.nationalarchives.droid.submitter.SubmissionGateway (droid-results)
 * </li>
 * <li>uk.gov.nationalarchives.droid.command.ResultPrinter (droid-command-line)</li>
 * </ul>
 * Ceci afin de conserver le même algorithme que Droid, et donc d'obtenir les
 * mêmes résultats
 */
@Component
public class DroidAnalyseSupport {

   private static final String OLE2_CONTAINER = "OLE2";
   private static final String ZIP_CONTAINER = "ZIP";

   private final BinarySignatureIdentifier binarySignatureIdentifier;
   private final ContainerSignatureDefinitions containerSignatureDefinitions;
   private final boolean matchAllExtensions;

   private final List<TriggerPuid> triggerPuids;

   private final Ole2ContainerContentIdentifier ole2Identifier;
   private final ZipContainerContentIdentifier zipIdentifier;

   /**
    * Constructeur
    * 
    * @param binarySignatureIdentifier
    *           le mécanisme d'identification DROID par signatures binaires
    * @param containerSignatureDefinitions
    *           la définition des signatures des conteneurs
    */
   @Autowired
   public DroidAnalyseSupport(
         BinarySignatureIdentifier binarySignatureIdentifier,
         ContainerSignatureDefinitions containerSignatureDefinitions) {

      // Mémorise les éléments passés en constructeur
      this.binarySignatureIdentifier = binarySignatureIdentifier;
      this.containerSignatureDefinitions = containerSignatureDefinitions;

      // Une constante aujourd'hui, un paramètre demain
      matchAllExtensions = false;

      // Construit une moulinette pour les conteneurs
      triggerPuids = containerSignatureDefinitions.getTiggerPuids();

      // Construit l'analyseur de conteneur Ole2
      IdentificationRequestFactory requestFactoryOle2 = new ContainerFileIdentificationRequestFactory();
      ole2Identifier = new Ole2ContainerContentIdentifier();
      ole2Identifier.init(containerSignatureDefinitions, OLE2_CONTAINER);
      Ole2IdentifierEngine ole2IdentifierEngine = new Ole2IdentifierEngine();
      ole2IdentifierEngine.setRequestFactory(requestFactoryOle2);
      ole2Identifier.setIdentifierEngine(ole2IdentifierEngine);

      // Construit l'analyseur de Conteneur Zip
      IdentificationRequestFactory requestFactoryZip = new ContainerFileIdentificationRequestFactory();
      zipIdentifier = new ZipContainerContentIdentifier();
      zipIdentifier.init(containerSignatureDefinitions, ZIP_CONTAINER);
      ZipIdentifierEngine zipIdentifierEngine = new ZipIdentifierEngine();
      zipIdentifierEngine.setRequestFactory(requestFactoryZip);
      zipIdentifier.setIdentifierEngine(zipIdentifierEngine);

   }

   /**
    * Identifie un format de fichier à l'aide des signatures binaires
    * 
    * @param request
    *           la requête d'identification
    * @return les résultats de l'identification
    */
   public final IdentificationResultCollection handleSignatures(
         IdentificationRequest request) {
      return binarySignatureIdentifier.matchBinarySignatures(request);
   }

   /**
    * Approfondit la première analyse des signatures binaires, avec l'analyse
    * des formats conteneurs
    * 
    * @param request
    *           la requête d'identification
    * @param results
    *           les résultats de l'identification avec les signatures binaires
    * @return les résultats de l'analyse
    */
   @SuppressWarnings("PMD.AvoidDeeplyNestedIfStmts")
   public final IdentificationResultCollection handleContainer(
         IdentificationRequest request, IdentificationResultCollection results) {

      IdentificationResultCollection containerResults = new IdentificationResultCollection(
            request);

      if (results.getResults().size() > 0
            && containerSignatureDefinitions != null) {

         for (IdentificationResult identResult : results.getResults()) {

            String filePuid = identResult.getPuid();

            if (filePuid != null) {

               TriggerPuid containerPuid = getTriggerPuidByPuid(filePuid);
               if (containerPuid != null) {

                  String containerType = containerPuid.getContainerType();

                  AbstractContainerContentIdentifier contentIdentifier = getContainerContentIdentifier(containerType);

                  try {
                     containerResults = contentIdentifier.process(request
                           .getSourceInputStream(), containerResults);
                  } catch (IOException ex) {
                     throw new FormatIdentificationRuntimeException(ex);
                  }

               }
            }
         }

      }

      return containerResults;
   }

   private AbstractContainerContentIdentifier getContainerContentIdentifier(
         String containerType) {

      AbstractContainerContentIdentifier result = null;

      if (OLE2_CONTAINER.equals(containerType)) {
         result = ole2Identifier;
      } else if (ZIP_CONTAINER.equals(containerType)) {
         result = zipIdentifier;
      } else {
         throw new FormatIdentificationRuntimeException(
               "Le type de conteneur suivant est inconnu : " + containerType);
      }

      return result;

   }

   private TriggerPuid getTriggerPuidByPuid(final String puid) {
      for (final TriggerPuid tp : triggerPuids) {
         if (tp.getPuid().equals(puid)) {
            return tp;
         }
      }
      return null;
   }

   /**
    * Analyse à l'aide des extensions de fichiers. S'appuie sur des résultats
    * d'analyse avec les signatures binaires, éventuellement approfondis avec la
    * recherche dans les conteneurs
    * 
    * @param request
    *           la requête d'identification
    * @param results
    *           les résultats précédement obtenus
    * @return les résultats de l'analyse
    */
   public final IdentificationResultCollection handleExtensions(
         IdentificationRequest request, IdentificationResultCollection results) {

      binarySignatureIdentifier.removeLowerPriorityHits(results);

      IdentificationResultCollection extensionResults = results;
      List<IdentificationResult> resultList = results.getResults();
      if (resultList != null && resultList.isEmpty()) {
         // If we call matchExtensions with "true", it will match
         // ALL files formats which have a given extension.
         // If "false", it will only match file formats for which
         // there is no other signature defined.
         IdentificationResultCollection checkExtensionResults = binarySignatureIdentifier
               .matchExtensions(request, matchAllExtensions);
         if (checkExtensionResults != null) {
            extensionResults = checkExtensionResults;
         }
      } else {
         binarySignatureIdentifier.checkForExtensionsMismatches(
               extensionResults, request.getExtension());
      }

      return extensionResults;

   }

   /**
    * Fin de l'analyse : renvoie l'identifiant PRONOM à partir des résultats
    * obtenus
    * 
    * @param request
    *           la requête d'identification
    * @param results
    *           les résultats obtenus
    * @return l'identifiant PRONOM, ou null si le format n'a pas été identifié
    */
   public final String handleResult(IdentificationRequest request,
         IdentificationResultCollection results) {

      if ((results == null) || CollectionUtils.isEmpty(results.getResults())) {
         return null;
      } else {
         return results.getResults().get(0).getPuid();
      }

   }

}
