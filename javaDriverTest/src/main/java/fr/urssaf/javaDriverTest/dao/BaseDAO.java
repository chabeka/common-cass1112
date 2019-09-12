/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.dao;

import java.util.Map;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO (ac75007394) Description du type
 */
public class BaseDAO {

   public static UUID getBaseUUID(final CqlSession session, final String baseName) throws Exception {
      final SimpleStatement statement = SimpleStatement.builder("select base_uuid from dfce.base where id = :baseName")
                                                       .addNamedValue("baseName", baseName)
                                                       .build();
      final Row row = session.execute(statement).one();
      return row.getUuid("base_uuid");
   }

   public static UUID getBaseUUID(final CqlSession session) throws Exception {
      final ResultSet rs = session.execute("select id, base_uuid from dfce.base");
      for (final Row row : rs) {
         final String baseName = row.getString("id");
         final UUID baseUUID = row.getUuid("base_uuid");
         if (baseName.contains("GNT") || baseName.contains("GNS") || baseName.contains("SAE")) {
            return baseUUID;
         }
      }
      return null;
   }

   public static String getBaseName(final CqlSession session) throws Exception {
      final ResultSet rs = session.execute("select id from dfce.base");
      for (final Row row : rs) {
         final String baseName = row.getString("id");
         if (baseName.contains("GNT") || baseName.contains("GNS") || baseName.contains("SAE")) {
            return baseName;
         }
      }
      throw new Exception("Base non trouvée");
   }

   private static String getNonSystemMetaType(final CqlSession session, final String baseName, final String metaName) {
      final SimpleStatement statement = SimpleStatement.builder("select metadata_constraints from dfce.base where id = :baseName")
                                                       .addNamedValue("baseName", baseName)
                                                       .build();
      final Row row = session.execute(statement).one();
      final Map<String, String> map = row.getMap("metadata_constraints", String.class, String.class);
      final String json = map.get(metaName);
      final ObjectMapper objectMapper = new ObjectMapper();
      try {
         final JsonNode rootNode = objectMapper.readTree(json);
         return rootNode.path("metadata").path("dataType").asText();
      }
      catch (final Exception e) {
         throw new RuntimeException("Erreur lors du parse du json suivant :" + json, e);
      }
   }

   /**
    * Renvoie le type de l'index
    * 
    * @return "STRING", "DATE", "DOUBLE", "BOOLEAN", "UUID", "DATETIME"
    * @throws Exception
    */
   public static String getIndexType(final CqlSession session, final String baseName, final String indexName) {
      if (indexName.startsWith("SM_")) {
         if (indexName.contains("_DATE")) {
            return "DATETIME";
         }
         if (indexName.equals("SM_UUID")) {
            return "UUID";
         }
         if (indexName.equals("SM_IS_FROZEN")) {
            return "BOOLEAN";
         }
         return "STRING";
      } else {
         if (indexName.contains("&")) {
            return "STRING";
         }
         return getNonSystemMetaType(session, baseName, indexName);
      }
   }

   public static String getIndexTable(final CqlSession session,
                                      final String baseName,
                                      final String indexName) {
      final String indexType = getIndexType(session, baseName, indexName);
      switch (indexType) {
      case "INTEGER":
         return "term_info_range_integer";
      case "FLOAT":
         return "term_info_range_float";
      case "DOUBLE":
         return "term_info_range_double";
      case "LONG":
         return "term_info_range_long";
      case "DATE":
         return "term_info_range_date";
      case "DATETIME":
         return "term_info_range_datetime";
      case "STRING":
         return "term_info_range_string";
      case "BOOLEAN":
         return "term_info_range_string"; // A vérifier
      case "UUID":
         return "term_info_range_uuid";
      default:
         throw new RuntimeException("Type d'index inconnu : " + indexType);
      }
   }

}
