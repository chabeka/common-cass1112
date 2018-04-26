/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;

/**
 * 
 * 
 */
public abstract class AbstractRollbackTasklet {

   /**
    * Nombre max d'éléments renvoyés
    */
   protected static final int MAX_RESULT = 10;

   private static final String TRC_FIND = "trouverDocumentsRestants()";

   private static final String ERREUR_RECHERCHE = "{} - Erreur lors de la recherche des documents restants à rollbacker";

   /**
    * @param idTraitement
    */
   protected final List<UntypedDocument> trouverDocumentsRestants(
         String idTraitement) {

      String requete = "IdTraitementMasseInterne:" + idTraitement;
      List<String> metadata = new ArrayList<String>();

      List<UntypedDocument> listDocs = null;

      try {
         listDocs = getDocumentService().search(requete, metadata, MAX_RESULT);
      } catch (MetaDataUnauthorizedToSearchEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      } catch (UnknownDesiredMetadataEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      } catch (UnknownLuceneMetadataEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      } catch (SyntaxLuceneEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      } catch (SAESearchServiceEx e) {
         getLogger().info(ERREUR_RECHERCHE, TRC_FIND, e);
      }

      return listDocs;

   }

   /**
    * @return le service de traitement des documents
    */
   protected abstract SAEDocumentService getDocumentService();

   /**
    * @return le logger
    */
   protected abstract Logger getLogger();

}