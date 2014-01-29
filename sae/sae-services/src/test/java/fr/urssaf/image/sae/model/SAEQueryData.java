package fr.urssaf.image.sae.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe réprésentant une metadonnée de test
 */
@XStreamAlias("query")
public class SAEQueryData {
   private String luceneQuery;
   private String queryType;
   @XStreamImplicit(itemFieldName = "metadata")
   private List<SAEMockMetadata> desiredMetadatas;

   /**
    * @return Type de requête.
    */
   public final String getQueryType() {
      return queryType;
   }

   /**
    * @param queryType
    *           : Type de requête.
    */
   public final void setQueryType(String queryType) {
      this.queryType = queryType;
   }

   /**
    * @return Métadonnées désirées.
    */
   public final List<SAEMockMetadata> getDesiredMetadatas() {
      return desiredMetadatas;
   }

   /**
    * @param desiredMetadats
    *           : Métadonnées désirées.
    */
   public final void setDesiredMetadatas(List<SAEMockMetadata> desiredMetadats) {
      this.desiredMetadatas = desiredMetadats;
   }

   /**
    * @return Requête lucene.
    */
   public final String getLuceneQuery() {
      return luceneQuery;
   }

   /**
    * @param luceneQuery
    *           : Requête lucene.
    */
   public final void setLuceneQuery(String luceneQuery) {
      this.luceneQuery = luceneQuery;
   }

}
