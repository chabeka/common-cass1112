package fr.urssaf.image.sae.dfcetools;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.docubase.dfce.exception.runtime.NotFoundDFCEException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.dao.BaseDAO;
import fr.urssaf.image.sae.dfcetools.dao.IndexReference;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.IndexPaginationSearchQuery;
import net.docubase.toolkit.model.search.SearchQuery;

/**
 * Classe permettant de purger des documents, sur critères de date
 */
public class DocumentsDeleter {

   private PreparedStatement deleteStatement;

   private final CqlSession session;

   private final IndexReference indexReference;

   private final UUID baseId;

   public DocumentsDeleter(final CqlSession session) {
      this.session = session;
      prepareDeleteStatement(session);
      baseId = BaseDAO.getBaseUUID(session);
      indexReference = new IndexReference();
      indexReference.readIndexReference(session, baseId, "SM_ARCHIVAGE_DATE", "NOMINAL");
   }

   private void prepareDeleteStatement(final CqlSession session) {
      final SimpleStatement simpleStatement = SimpleStatement.builder("DELETE FROM dfce.term_info_range_datetime"
            + " WHERE index_code=? AND metadata_name='SM_ARCHIVAGE_DATE' AND base_uuid=? AND range_index_id=?"
            + " AND metadata_value= ? AND document_uuid= ? AND document_version='0.0.0'")
            .setConsistencyLevel(DefaultConsistencyLevel.QUORUM)
            .build();
      deleteStatement = session.prepare(simpleStatement);
   }

   public void deleteDocumentsAfterDate(final DFCEServices dfceServices, final String startPurgeDate) throws Exception {
      final String query = "SM_ARCHIVAGE_DATE:[" + startPurgeDate + "1 TO 99990101]";
      // final SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(query, dfceServices.getBase());
      final SearchQuery searchQuery = new IndexPaginationSearchQuery(query, dfceServices.getBase());
      searchQuery.setSearchLimit(100); // Pagination

      final Iterator<Document> iterateur = dfceServices.createDocumentIterator(searchQuery);
      int okCounter = 0;
      int counter = 0;
      int cleanCounter = 0;
      int binCounter = 0;
      while (iterateur.hasNext()) {
         final Document doc = iterateur.next();
         final Date archivageDate = doc.getArchivageDate();
         final UUID uuid = doc.getUuid();
         try {
            dfceServices.deleteDocument(uuid);
            okCounter++;
         }
         catch (final NotFoundDFCEException e) {
            final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String metaValue = dateFormat.format(archivageDate);
            if (e.getMessage().contains("doesn't exist in DEFAULT index")) {
               // Le document est déjà supprimé ou dans la corbeille, mais l'index n'est pas à jour.
               // 1) on tente la suppression de la corbeille
               try {
                  dfceServices.deleteDocumentFromRecycleBin(uuid);
                  binCounter++;
               }
               catch (final NotFoundDFCEException ex) {
                  // Rien à faire
               }
               // 2) on supprime l'entrée d'index
               removeOneIndexEntry("", metaValue, doc.getUuid());
               cleanCounter++;
            } else {
               System.out.println("okCounter=" + okCounter);
               System.out.println("cleanCounter=" + cleanCounter);
               System.out.println("Erreur lors de la tentative de suppression du document suivant :");
               System.out.println("BaseUUID=" + doc.getBaseUUID());
               System.out.println("UUID=" + doc.getUuid());
               System.out.println("MetaValue=" + metaValue);
               System.out.println("Erreur=" + e.getMessage());
               throw new RuntimeException("Fini en erreur");
            }
         }
         counter++;
         if (counter % 1000 == 0) {
            System.out.println(okCounter + "-" + cleanCounter + "-" + binCounter + " " + uuid + " " + archivageDate);
         }
      }
      System.out.println("Terminé. Nombre de documents supprimés =" + okCounter);
      System.out.println("Nombre d'entrées d'index supprimées =" + cleanCounter);
   }

   private void removeOneIndexEntry(final String indexCode, final String metaValue, final UUID docUUID) {
      final int rangeId = indexReference.metaToRangeId(metaValue);
      final BoundStatement bound = deleteStatement.bind()
            .setString(0, indexCode)
            .setUuid(1, baseId)
            .setBigInteger(2, BigInteger.valueOf(rangeId))
            .setString(3, metaValue)
            .setUuid(4, docUUID);
      final ResultSet result = session.execute(bound);
   }
}
