/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;

/**
 * 
 * 
 */
public class DocInfoResultSet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocInfoResultSet.class);

   private final AllRowsQuery<DocInfoKey, String> rowQuery;

   private Iterator<Row<DocInfoKey, String>> iterator;

   /**
    * Constructeur
    * 
    * @param rowQuery
    *           requete
    */
   public DocInfoResultSet(AllRowsQuery<DocInfoKey, String> rowQuery) {
      this.rowQuery = rowQuery;
      setResults();
   }

   /**
    * retourne la prochaine valeur distincte trouvée dans la liste des résultats
    * 
    * @return la prochaine référence
    */
   public final Map<String, String> getNextRecord() {

      Map<String, String> values = null;

      if (iterator != null && iterator.hasNext()) {

         values = new HashMap<String, String>();
         Row<DocInfoKey, String> row = iterator.next();
         ColumnList<String> columns = row.getColumns();
         Collection<String> keys = columns.getColumnNames();

         for (String key : keys) {
            values.put(key, columns.getStringValue(key, null));
         }
      }

      return values;

   }

   private void setResults() {

      LOGGER.debug("{} - récupération de la liste des données", "setResults");

      try {
         OperationResult<Rows<DocInfoKey, String>> operation = rowQuery
               .execute();

         if (operation != null) {
            Rows<DocInfoKey, String> result = operation.getResult();
            iterator = result.iterator();
         }

      } catch (ConnectionException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

}
