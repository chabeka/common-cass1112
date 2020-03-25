/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import fr.urssaf.image.commons.cassandra.exception.CassandraConfigurationException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;

/**
 * TODO (AC75095028) Description du type
 */
public class Utils {

  /**
   * Get bean fields (include super class fields)
   *
   * @param entity
   * @return
   */
  public static List<Field> getEntityFileds(final Class entity) {
    final List<Field> fields = new LinkedList<>();
    for (final Field field : entity.getDeclaredFields()) {
      fields.add(field);
    }
    if (!listFieldsFromSuperClass(entity).isEmpty()) {
      fields.addAll(listFieldsFromSuperClass(entity));
    }
    return fields;
  }

  /**
   * Get bean fields from super class
   *
   * @param class1
   * @return
   */
  public static List<Field> listFieldsFromSuperClass(final Class<?> class1) {
    final List<Field> fields = new LinkedList<>();
    for (final Field field : class1.getSuperclass().getDeclaredFields()) {
      fields.add(field);
    }
    return fields;
  }
  
  /**
	  * Cette methode permet de determiner le mode de connection au cluster en se basant sur 
	  * l'extension du fichier.<br>
	  * On considère dans ce cas-ci que toutes les injection de données via des fichiers
	  * se font de la manière suivante: <br>
	  * <ul>
	  * <li>Pour les injections en mode thritf on utilise uniquement des fichiers xml<br></li>
	  * <li>Pour les injections en mode CQL on utilise uniquement des fichiers avec extention .cql<br></li>
	  * </ul>
	  * @param newDataSets
	  *          Jeu(x) de données à utiliser
	  * @throws Exception
	  *           Une erreur est survenue
	  */
	public static String setAPIConnecterMode(final String... newDataSets) {
	  String mode = "";
	  String ext = "";
	  if (newDataSets.length > 0) {
		  String dataset = newDataSets[0];
		  int pos = dataset.lastIndexOf('.');
		  if(pos > 0) {
			  ext = dataset.substring(pos +1);
		  }
		  if("cql".equals(ext)) {
			  mode = ModeGestionAPI.MODE_API.DATASTAX;
		  } else if("xml".equals(ext)) {
			  mode = ModeGestionAPI.MODE_API.HECTOR;
		  } else {
			  throw new CassandraConfigurationException("le type d'extension du fichier dataset n'est pas pris en compte");
		  }
	  }
	  return mode;
	}
}
