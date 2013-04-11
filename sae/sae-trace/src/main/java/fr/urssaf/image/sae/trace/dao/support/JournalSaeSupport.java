package fr.urssaf.image.sae.trace.dao.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;

/**
 * Service permettant de réaliser des opérations sur les journaux SAE
 * (historique des évènements)
 * 
 */
@Component
public class JournalSaeSupport {

   /**
    * Classe d'accès à DFCE
    */
   private final ServiceProviderSupport serviceProvider;

   /**
    * Constructeur
    * 
    * @param serviceProvider
    *           Classe d'accès à DFCE
    */
   @Autowired
   public JournalSaeSupport(ServiceProviderSupport serviceProvider) {
      super();
      this.serviceProvider = serviceProvider;
   }

   /**
    * Retourne la liste des journaux des évènements SAE compris dans
    * l'intervalle de dates donné
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param nomBase
    *           Nom de la base du SAE
    * @return Liste Liste des journaux des évènements compris dans l'intervalle
    *         de dates donné
    */
   public final List<Journal> findByDates(String dateDebut, String dateFin,
         String nomBase) {

      try {
         // Construction de la requête LUCENE
         String requete = "SM_DOCUMENT_TYPE:7.7.8.8.1 AND itm:[" + dateDebut
               + " TO " + dateFin + "]";

         ToolkitFactory tf = new ToolkitFactory();
         Base base = tf.createBase(nomBase);

         // Lancement de la recherche
         int nbMaxElements = Integer.MAX_VALUE;

         SearchResult resultat = serviceProvider.getSearchService().search(
               requete, nbMaxElements, base);

         List<Journal> listeJournal = new ArrayList<Journal>();

         List<Document> listeDoc = resultat.getDocuments();
         for (Document document : listeDoc) {
            String nomFichier = document.getFilename() + "."
                  + document.getExtension();
            Journal journal = new Journal(document.getCreationDate(), document
                  .getUuid(), nomFichier);
            listeJournal.add(journal);
         }
         return listeJournal;

      } catch (ExceededSearchLimitException e) {
         throw new TraceRuntimeException(e);
      } catch (SearchQueryParseException e) {
         throw new TraceRuntimeException(e);
      }
   }

   /**
    * Récupère le contenu du journal
    * 
    * @param idJournal
    *           L'identifiant unique du journal
    * @param nomBase
    *           Nom de la base du SAE
    * @return Contenu du journal
    */
   public final byte[] getContent(UUID idJournal, String nomBase) {
      ToolkitFactory tf = new ToolkitFactory();
      Base base = tf.createBase(nomBase);

      Document doc = serviceProvider.getSearchService().getDocumentByUUID(base,
            idJournal);
      InputStream inStream = serviceProvider.getStoreService().getDocumentFile(
            doc);
      try {
         return IOUtils.toByteArray(inStream);
      } catch (IOException e) {
         throw new TraceRuntimeException(e);
      }
   }

}
