package fr.urssaf.image.helper;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.service.template.IndexedSlicesPredicate;

import org.apache.cassandra.thrift.IndexClause;


/**
 * Classe qui facilite la manipulation des objets IndexedSlicesPredicate
 * @author Samuel Carrière
 *
 */
public class IndexedSlicesPredicateHelper {

	/**
	 * Affecte une "start key" vide (tableau d'octet vide) à un prédicat
	 * En effet, ce n'est pas faisable par les API proposées par hector 1.0-3. 
	 * @param predicate	: prédicat concerné
	 */
	public static void setEmptyStartKey(IndexedSlicesPredicate<?,?,?> predicate) {
		setStartKey(predicate, BytesArraySerializer.get().toByteBuffer(new byte[0]));
	}
	
	/**
	 * Affecte une "start key" à un prédicat
	 * En effet, ce n'est pas faisable par les API proposées par hector 1.0-3. 
	 * @param predicate	: prédicat concerné
	 */
	public static void setStartKey(IndexedSlicesPredicate<?,?,?> predicate, ByteBuffer byteBuffer) {
		Class<?> theClass = predicate.getClass();
		Field fields[] = theClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName() == "indexClause") {
				try {
					field.setAccessible(true);
					IndexClause clause = (IndexClause) field.get(predicate);
					clause.setStart_key(byteBuffer);
					break;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}		
		
	}
	
}
