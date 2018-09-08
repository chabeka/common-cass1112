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

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;

/**
 * Service permettant de réaliser des opérations sur les journaux SAE
 * (historique des événements)
 *
 */
@Component
public class JournalSaeSupport {

   /**
    * Classe d'accès à DFCE
    */
   private final DFCEServices dfceServices;

   /**
    * Constructeur
    *
    * @param serviceProvider
    *           Classe d'accès à DFCE
    */
   @Autowired
   public JournalSaeSupport(final DFCEServices dfceServices) {
      super();
      this.dfceServices = dfceServices;
   }

   /**
    * Retourne la liste des journaux des événements SAE compris dans
    * l'intervalle de dates donné
    *
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param nomBase
    *           Nom de la base du SAE
    * @return Liste Liste des journaux des événements compris dans l'intervalle
    *         de dates donné
    */
   public final List<Journal> findByDates(final String dateDebut, final String dateFin,
                                          final String nomBase) {

      try {
         // Construction de la requête LUCENE
         final String requete = "SM_DOCUMENT_TYPE:7.7.8.8.1 AND itm:[" + dateDebut
               + " TO " + dateFin + "]";

         final ToolkitFactory toolkitFactory = ToolkitFactory.getInstance();
         final Base base = toolkitFactory.createBase(nomBase);

         // Lancement de la recherche
         final int nbMaxElements = Integer.MAX_VALUE;

         final SortedSearchQuery paramSearchQuery = toolkitFactory
               .createMonobaseSortedQuery(requete, base);
         paramSearchQuery.setPageSize(nbMaxElements);
         paramSearchQuery.setOffset(0);

         final SearchResult resultat = dfceServices.search(paramSearchQuery);

         final List<Journal> listeJournal = new ArrayList<Journal>();

         final List<Document> listeDoc = resultat.getDocuments();
         for (final Document document : listeDoc) {
            final String nomFichier = document.getFilename() + "."
                  + document.getExtension();
            final String dateTmp = (String) document.getSingleCriterion("itm")
                  .getWord();

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                                                              Locale.FRENCH);
            final Date dateDebutEvt = sdf.parse(dateTmp);

            final Journal journal = new Journal(document.getCreationDate(), document
                                                .getUuid(), nomFichier, dateDebutEvt, dateDebutEvt, document
                                                .getSize());
            listeJournal.add(journal);
         }
         return listeJournal;

      } catch (final ExceededSearchLimitException e) {
         throw new TraceRuntimeException(e);
      } catch (final SearchQueryParseException e) {
         throw new TraceRuntimeException(e);
      } catch (final ParseException e) {
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
   public final Journal findByUUID(final UUID uuidJournal, final String nomBase) {

      try {
         final ToolkitFactory toolkitFactory = new ToolkitFactory();
         final Base base = toolkitFactory.createBase(nomBase);

         final Document document = dfceServices.getDocumentByUUID(base, uuidJournal);

         if (document != null) {
            final String nomFichier = document.getFilename() + "."
                  + document.getExtension();
            final String dateTmp = (String) document.getSingleCriterion("itm")
                  .getWord();

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                                                              Locale.FRENCH);
            final Date dateDebutEvt = sdf.parse(dateTmp);

            final Journal journal = new Journal(document.getCreationDate(), document
                                                .getUuid(), nomFichier, dateDebutEvt, dateDebutEvt, document
                                                .getSize());

            return journal;
         } else {
            return null;
         }

      } catch (final ParseException e) {
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
   public final byte[] getContent(final UUID idJournal, final String nomBase) {
      final ToolkitFactory toolkitFactory = new ToolkitFactory();
      final Base base = toolkitFactory.createBase(nomBase);

      final Document doc = dfceServices.getDocumentByUUID(base, idJournal);
      final InputStream inStream = dfceServices.getDocumentFile(doc);
      try {
         return IOUtils.toByteArray(inStream);
      } catch (final IOException e) {
         throw new TraceRuntimeException(e);
      }
   }
}
