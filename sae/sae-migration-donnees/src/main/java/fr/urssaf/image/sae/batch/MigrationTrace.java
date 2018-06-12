/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.batch;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.trace.daocql.IGenericType;

/**
 * TODO (AC75095028) Description du type
 */
public class MigrationTrace {

  String keyspace = "SAE";

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericType genericdao;
}
