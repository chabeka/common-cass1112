/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.datas;

import org.apache.commons.lang.StringUtils;

import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.shallows.EmptyColumnList;

import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringColumn;
import fr.urssaf.image.sae.regionalisation.bean.TermInfoRangeStringKey;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;

/**
 * 
 * 
 */
public class TermInfoResultSet {

   private final RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> rowQuery;

   private int index;

   private Column<TermInfoRangeStringColumn> lastValue = null;

   private ColumnList<TermInfoRangeStringColumn> list;

   /**
    * Constructeur
    * 
    * @param rowQuery
    *           requete
    */
   public TermInfoResultSet(
         RowQuery<TermInfoRangeStringKey, TermInfoRangeStringColumn> rowQuery) {
      this.rowQuery = rowQuery;
      setListResults();
   }

   /**
    * retourne la prochaine valeur distincte trouvée dans la liste des résultats
    * 
    * @return la prochaine référence
    */
   public final String getNextValue() {

      return findNextValue();

   }

   private String findNextValue() {

      String value = null;

      while (index < list.size() && StringUtils.isEmpty(value)) {

         if (lastValue == null
               || !list.getColumnByIndex(index).getName().getCategoryValue()
                     .equals(lastValue.getName().getCategoryValue())) {
            value = list.getColumnByIndex(index).getName().getCategoryValue();
            lastValue = list.getColumnByIndex(index);
         }

         index++;

      }

      if (index == list.size() && list.size() > 0) {
         setListResults();
      }

      if (StringUtils.isEmpty(value) && !list.isEmpty()) {
         value = findNextValue();
      }

      return value;
   }

   private void setListResults() {

      try {
         list = rowQuery.execute().getResult();

         if (list == null) {
            list = new EmptyColumnList<TermInfoRangeStringColumn>();
         }

         index = 0;

      } catch (ConnectionException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

}
