package fr.urssaf.image.sae.indexcounterupdater.dao;

import java.util.Map;
import java.util.UUID;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Permet d'accéder à la table dfce.base
 */
public class BaseDAO {

   private BaseDAO() {
      // Classe statique
   }

   /**
    * Renvoie l'UUID de la base dont le nom est passé en paramètre
    * 
    * @param session
    *           session cassandra
    * @param baseName
    *           nom de la base
    * @return
    */
   public static UUID getBaseUUID(final CqlSession session, final String baseName) {
      final SimpleStatement statement = SimpleStatement.builder("select base_uuid from dfce.base where id = :baseName")
                                                       .addNamedValue("baseName", baseName)
                                                       .build();
      final Row row = session.execute(statement).one();
      return row.getUuid("base_uuid");
   }

   /**
    * Renvoie le nom de la base GNT ou GNS
    * 
    * @param session
    *           session cassandra
    * @return
    */
   public static String getBaseName(final CqlSession session) {
      final ResultSet rs = session.execute("select id, base_uuid from dfce.base");
      for (final Row row : rs) {
         final String baseName = row.getString("id");
         if (baseName.contains("GNT") || baseName.contains("GNS") || baseName.contains("SAE")) {
            return baseName;
         }
      }
      return null;
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
}
