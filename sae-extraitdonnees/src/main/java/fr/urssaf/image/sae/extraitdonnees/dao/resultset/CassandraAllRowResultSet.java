package fr.urssaf.image.sae.extraitdonnees.dao.resultset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.netflix.astyanax.Serializer;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.AllRowsQuery;
import com.netflix.astyanax.shallows.EmptyIterator;

import fr.urssaf.image.sae.extraitdonnees.exception.CassandraException;
import fr.urssaf.image.sae.extraitdonnees.exception.ErreurTechniqueException;

/**
 * Classe mère pour la réalisation des opérations sur les requetes CASSANDRA
 * 
 * @param <K>
 *           Classe de la clé de la ligne
 * @param <C>
 *           Classe du nom de la colonne
 * 
 */
public class CassandraAllRowResultSet<K, C> {

   private Row<K, C> currentRow = null;

   private Iterator<Row<K, C>> iterator;

   /**
    * Constructeur
    * 
    * @param rowQuery
    *           requête cassandra
    */
   @SuppressWarnings("unchecked")
   public CassandraAllRowResultSet(AllRowsQuery<K, C> rowQuery) {

      try {
         Rows<K, C> result = rowQuery.execute().getResult();

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
    * @return le boolean déterminant si une ligne est présente après l'actuelle
    */
   public final boolean hasNext() {
      return iterator.hasNext();
   }

   /**
    * se positionne sur l'élément suivant
    * 
    */
   public final void next() {
      currentRow = iterator.next();
   }

   /**
    * @return la clé de la ligne
    * @throws CassandraException
    *            levée si l'enregistrement actuel est null
    */
   public final K getKey() throws CassandraException {
      if (currentRow == null) {
         throw new CassandraException(
               "Impossible de réaliser l'opération getKey : l'enregsitrement actuel est null");

      } else {
         return currentRow.getKey();
      }
   }

   /**
    * @return la liste des noms de colonne
    * @throws CassandraException
    *            exception levée lors d'une erreur d'accès aux données
    */
   public final List<C> getColumnNames() throws CassandraException {
      if (currentRow == null) {
         throw new CassandraException(
               "Impossible de réaliser l'opération getColumnNames : l'enregsitrement actuel est null");

      } else {
         return new ArrayList<C>(currentRow.getColumns().getColumnNames());
      }
   }

   /**
    * 
    * @param <T>
    *           classe de l'objet à retourner
    * @param columnName
    *           identifiant de la colonne
    * @param serializer
    *           serializer à utiliser pour retourner la valeur
    * @param defaultValue
    *           valeur par défaut à retourner
    * @return la valeur de la colonne demandée
    * @throws CassandraException
    *            exception levée en cas d'erreur d'accès à l'enregistrement
    */
   public final <T> T getValue(C columnName, Serializer<T> serializer,
         T defaultValue) throws CassandraException {

      if (currentRow == null) {
         throw new CassandraException(
               "Impossible de réaliser l'opération getValue : l'enregsitrement actuel est null");

      } else {
         return currentRow.getColumns().getValue(columnName, serializer,
               defaultValue);
      }
   }

}
