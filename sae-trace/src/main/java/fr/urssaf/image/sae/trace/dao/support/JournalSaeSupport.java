package fr.urssaf.image.sae.trace.dao.support;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;

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

         ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
         Base base = toolkitFactory.createBase(nomBase);

         // Lancement de la recherche
         int nbMaxElements = Integer.MAX_VALUE;

         SortedSearchQuery paramSearchQuery = toolkitFactory
               .createMonobaseSortedQuery(requete, base);
         paramSearchQuery.setPageSize(nbMaxElements);
         paramSearchQuery.setOffset(0);

         SearchResult resultat = serviceProvider.getSearchService().search(
               paramSearchQuery);

         List<Journal> listeJournal = new ArrayList<Journal>();

         List<Document> listeDoc = resultat.getDocuments();
         for (Document document : listeDoc) {
            String nomFichier = document.getFilename() + "."
                  + document.getExtension();
            String dateTmp = (String) document.getSingleCriterion("itm")
                  .getWord();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                  Locale.FRENCH);
            Date dateDebutEvt = sdf.parse(dateTmp);

            Journal journal = new Journal(document.getCreationDate(), document
                  .getUuid(), nomFichier, dateDebutEvt, dateDebutEvt, document
                  .getSize());
            listeJournal.add(journal);
         }
         return listeJournal;

      } catch (ExceededSearchLimitException e) {
         throw new TraceRuntimeException(e);
      } catch (SearchQueryParseException e) {
         throw new TraceRuntimeException(e);
      } catch (ParseException e) {
         throw new TraceRuntimeException(e);
      }
   }

   /**
    * Retourne un journal par son UUID
    * 
    * @param uuidJournal
    *           UUID du journal
    * @param nomBase
    *           Nom de la base
    * @return Le journal correspondant à l'UUID
    */
   public final Journal findByUUID(UUID uuidJournal, String nomBase) {

      try {
         ToolkitFactory toolkitFactory = new ToolkitFactory();
         Base base = toolkitFactory.createBase(nomBase);

         Document document = serviceProvider.getSearchService()
               .getDocumentByUUID(base, uuidJournal);

         if (document != null) {
            String nomFichier = document.getFilename() + "."
                  + document.getExtension();
            String dateTmp = (String) document.getSingleCriterion("itm")
                  .getWord();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                  Locale.FRENCH);
            Date dateDebutEvt = sdf.parse(dateTmp);

            Journal journal = new Journal(document.getCreationDate(), document
                  .getUuid(), nomFichier, dateDebutEvt, dateDebutEvt, document
                  .getSize());

            return journal;
         } else {
            return null;
         }

      } catch (ParseException e) {
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
      ToolkitFactory toolkitFactory = new ToolkitFactory();
      Base base = toolkitFactory.createBase(nomBase);

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
