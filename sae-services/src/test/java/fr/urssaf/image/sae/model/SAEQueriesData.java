package fr.urssaf.image.sae.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Classe représentant une liste de requêtes.
 * 
 * @author rhofir
 */
@XStreamAlias("queries")
public class SAEQueriesData {
   @XStreamImplicit(itemFieldName = "query")
   private List<SAEQueryData> queriesData;

   /**
    * @return L'ensemble des requêtes pour les tests unitaires.
    */
   public final List<SAEQueryData> getQueriesData() {
      return queriesData;
   }

   /**
    * @param queriesData
    *           :L'ensemble des requêtes pour les tests unitaires.
    */
   public final void setQueriesData(List<SAEQueryData> queriesData) {
      this.queriesData = queriesData;
   }

}
