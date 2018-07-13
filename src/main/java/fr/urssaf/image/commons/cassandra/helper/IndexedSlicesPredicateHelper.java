package fr.urssaf.image.commons.cassandra.helper;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.template.IndexedSlicesPredicate;

import org.apache.cassandra.thrift.IndexClause;

/**
 * Classe qui facilite la manipulation des objets IndexedSlicesPredicate
 * 
 * @author Samuel Carrière
 * 
 */
public final class IndexedSlicesPredicateHelper {

   /**
    * Classe utilitaire sans constructeur
    */
   private IndexedSlicesPredicateHelper() {
   }

   /**
    * Affecte une "start key" vide (tableau d'octet vide) à un prédicat En
    * effet, ce n'est pas faisable par les API proposées par hector 1.0-3.
    * 
    * @param predicate
    *           : prédicat concerné
    */
   public static void setEmptyStartKey(IndexedSlicesPredicate<?, ?, ?> predicate) {
      setStartKey(predicate, BytesArraySerializer.get().toByteBuffer(
            new byte[0]));
   }

   /**
    * Affecte une "start key" à un prédicat En effet, ce n'est pas faisable par
    * les API proposées par hector 1.0-3.
    * 
    * @param predicate
    *           prédicat concerné
    * @param byteBuffer
    *           contient la "start key" à affecter
    */
   public static void setStartKey(
         final IndexedSlicesPredicate<?, ?, ?> predicate,
         final ByteBuffer byteBuffer) {

      AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            try {
               Class<?> theClass = predicate.getClass();
               Field field = theClass.getDeclaredField("indexClause");
               field.setAccessible(true);
               IndexClause clause = (IndexClause) field.get(predicate);
               clause.setStart_key(byteBuffer);
               return null;
            } catch (IllegalAccessException e) {
               throw new IllegalStateException(e);
            } catch (SecurityException e) {
               throw new IllegalStateException(e);
            } catch (NoSuchFieldException e) {
               throw new IllegalStateException(e);
            }
         }
      });
   }

}
