/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.resultset;

import java.util.Iterator;

import com.netflix.astyanax.Serializer;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.shallows.EmptyIterator;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;

/**
 * Classe mère pour réaliser les opérations sur les résultats de requêtes
 * 
 * @param <K>
 *           Classe de la clé de la ligne
 * @param <C>
 *           Classe du nom de la colonne
 * 
 */
public class CassandraRowResultSet<K, C> {

   private Iterator<Column<C>> iterator;

   private Column<C> currentValue;

   /**
    * Constructeur
    * 
    * @param rowQuery
    *           requête cassandra
    */
   @SuppressWarnings("unchecked")
   public CassandraRowResultSet(RowQuery<K, C> rowQuery) {

      try {
         ColumnList<C> result = rowQuery.execute().getResult();

         if (result == null) {
            iterator = new EmptyIterator();

         } else {
            iterator = result.iterator();
         }

      } catch (ConnectionException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

   /**
    * @return le boolean déterminant si une colonne est présente après
    *         l'actuelle
    */
   public final boolean hasNext() {
      return iterator.hasNext();
   }

   /**
    * se positionne sur l'élément suivant
    */
   public final void next() {
      currentValue = iterator.next();
   }

   /**
    * @param <T>
    *           classe de l'objet à retourner
    * @param serializer
    *           serializer à utiliser pour récupérer la valeur
    * @return la valeur de la colonne
    * @throws CassandraException
    *            erreur levée en cas d'accès impossible à la colonne
    */
   public final <T> T getValue(Serializer<T> serializer)
         throws CassandraException {
      if (currentValue == null) {
         throw new CassandraException(
               "impossible de réaliser l'opération getValue, la colonne actuelle est null");

      } else {
         return currentValue.getValue(serializer);
      }
   }

   /**
    * @return le nom de la colonne
    * @throws CassandraException
    *            erreur soulevée lors d'une erreur d'accès aux données
    */
   public final C getName() throws CassandraException {
      if (currentValue == null) {
         throw new CassandraException(
               "impossible de réaliser l'opération getValue, la colonne actuelle est null");

      } else {
         return currentValue.getName();
      }
   }

}
