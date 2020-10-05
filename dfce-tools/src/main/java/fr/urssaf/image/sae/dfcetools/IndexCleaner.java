package fr.urssaf.image.sae.dfcetools;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import fr.urssaf.image.sae.dfcetools.dao.BaseDAO;
import fr.urssaf.image.sae.dfcetools.dao.IndexReference;
import fr.urssaf.image.sae.dfcetools.helper.ObjectHelper;

/**
 * Classe permettant de vérifier ou nettoyer des entrées d'index, dans les tables
 * term_info et term_info_range_xxx
 */
public class IndexCleaner {

   private static final Logger LOGGER = LoggerFactory.getLogger(IndexCleaner.class);

   private PreparedStatement rangeDeleteStatement;
   private PreparedStatement deleteStatement;

   private PreparedStatement rangeSelectStatement;

   private PreparedStatement selectStatement;

   private final CqlSession session;

   private final String indexName;

   private final IndexReference indexReference;

   private final UUID baseId;

   private final String rangeTableName;

   /**
    * Constructeur
    * 
    * @param session
    *           : session CQL, permettant l'accès aux données dans cassandra
    * @param indexName
    *           : nom de l'index (nom de la méta dans le cas d'un index non composite)
    * @param dataType
    *           : type de données de la méta : STRING, DATE, DATETIME, ...
    */
   public IndexCleaner(final CqlSession session, final String indexName, final String dataType) {
      this.session = session;
      this.indexName = indexName;
      rangeTableName = "term_info_range_" + dataType.toLowerCase();
      prepareDeleteStatement(session);
      prepareSelectStatement(session);
      baseId = BaseDAO.getBaseUUID(session);
      indexReference = new IndexReference();
      indexReference.readIndexReference(session, baseId, indexName, "NOMINAL");
   }

   private void prepareDeleteStatement(final CqlSession session) {
      final SimpleStatement cql1 = SimpleStatement.builder("DELETE FROM dfce." + rangeTableName
            + " WHERE index_code=? AND metadata_name='" + indexName + "' AND base_uuid=? AND range_index_id=?"
            + " AND metadata_value= ? AND document_uuid= ? AND document_version='0.0.0' IF EXISTS")
            .setConsistencyLevel(DefaultConsistencyLevel.QUORUM)
            .build();
      rangeDeleteStatement = session.prepare(cql1);

      final SimpleStatement cql2 = SimpleStatement.builder("DELETE FROM dfce.term_info"
            + " WHERE index_code=? AND metadata_name='" + indexName + "' AND metadata_value=? AND base_uuid=? "
            + " AND document_uuid= ? AND document_version='0.0.0' IF EXISTS")
            .setConsistencyLevel(DefaultConsistencyLevel.QUORUM)
            .build();
      deleteStatement = session.prepare(cql2);
   }

   private void prepareSelectStatement(final CqlSession session) {
      final SimpleStatement cql1 = SimpleStatement.builder("SELECT * FROM dfce." + rangeTableName
            + " WHERE index_code=? AND metadata_name='" + indexName + "' AND base_uuid=? AND range_index_id=?"
            + " AND metadata_value= ? AND document_uuid= ? AND document_version='0.0.0'")
            .setConsistencyLevel(DefaultConsistencyLevel.QUORUM)
            .build();
      rangeSelectStatement = session.prepare(cql1);

      final SimpleStatement cql2 = SimpleStatement.builder("SELECT * FROM dfce.term_info"
            + " WHERE index_code=? AND metadata_name='" + indexName + "' AND metadata_value=? AND base_uuid=? "
            + " AND document_uuid= ? AND document_version='0.0.0'")
            .setConsistencyLevel(DefaultConsistencyLevel.QUORUM)
            .build();
      selectStatement = session.prepare(cql2);
   }

   /**
    * Vérifie la présence des entrées d'index pour un document
    * 
    * @param indexValue
    *           : valeur de l'entrée d'index
    * @param docUUID
    *           : UUID du document
    */
   public void verifyOneEntry(final String indexValue, final UUID docUUID) {
      verifyOneRangeIndexEntry("", indexValue, docUUID);
      verifyOneIndexEntry("", indexValue, docUUID);
   }

   /**
    * Supprime les entrées d'index pour un document
    * 
    * @param indexValue
    *           : valeur de l'entrée d'index
    * @param docUUID
    *           : UUID du document
    */
   public void cleanOneEntry(final String indexValue, final UUID docUUID) {
      final boolean result1 = removeOneRangeIndexEntry("", indexValue, docUUID);
      LOGGER.info("Résultat de la suppression term_info_range : {}", result1);
      final boolean result2 = removeOneIndexEntry("", indexValue, docUUID);
      LOGGER.info("Résultat de la suppression term_info : {}", result2);
   }

   public void verifyOrCleanOneEntry(final String indexValue, final UUID docUUID, final boolean shouldClean) {
      if (shouldClean) {
         cleanOneEntry(indexValue, docUUID);
      } else {
         verifyOneEntry(indexValue, docUUID);
      }
   }

   private boolean removeOneRangeIndexEntry(final String indexCode, final String metaValue, final UUID docUUID) {
      final int rangeId = indexReference.metaToRangeId(metaValue);
      final BoundStatement bound = rangeDeleteStatement.bind()
            .setString(0, indexCode)
            .setUuid(1, baseId)
            .setBigInteger(2, BigInteger.valueOf(rangeId))
            .setString(3, metaValue)
            .setUuid(4, docUUID);
      final ResultSet result = session.execute(bound);
      return result.wasApplied();
   }

   private void verifyOneRangeIndexEntry(final String indexCode, final String metaValue, final UUID docUUID) {
      final int rangeId = indexReference.metaToRangeId(metaValue);
      final BoundStatement bound = rangeSelectStatement.bind()
            .setString(0, indexCode)
            .setUuid(1, baseId)
            .setBigInteger(2, BigInteger.valueOf(rangeId))
            .setString(3, metaValue)
            .setUuid(4, docUUID);
      final ResultSet result = session.execute(bound);
      boolean empty = true;
      for (final Row row : result) {
         final ByteBuffer byteBuffer = row.getByteBuffer("serialized_document");
         final Object value = ObjectHelper.deserialiseObject(byteBuffer);
         LOGGER.info("Valeur trouvée dans term_info_range : {}", value);
         empty = false;
      }
      if (empty) {
         LOGGER.info("Pas de valeur trouvée dans term_info_range");
      }
   }

   private boolean removeOneIndexEntry(final String indexCode, final String metaValue, final UUID docUUID) {
      final BoundStatement bound = deleteStatement.bind()
            .setString(0, indexCode)
            .setString(1, metaValue)
            .setUuid(2, baseId)
            .setUuid(3, docUUID);
      final ResultSet result = session.execute(bound);
      return result.wasApplied();
   }

   private void verifyOneIndexEntry(final String indexCode, final String metaValue, final UUID docUUID) {
      final BoundStatement bound = selectStatement.bind()
            .setString(0, indexCode)
            .setString(1, metaValue)
            .setUuid(2, baseId)
            .setUuid(3, docUUID);
      final ResultSet result = session.execute(bound);
      boolean empty = true;
      for (final Row row : result) {
         final ByteBuffer byteBuffer = row.getByteBuffer("document_metadata_map");
         final Object value = ObjectHelper.deserialiseObject(byteBuffer);
         LOGGER.info("Valeur trouvée dans term_info : {}", value);
         empty = false;
      }
      if (empty) {
         LOGGER.info("Pas de valeur trouvée dans term_info");
      }
   }

}
