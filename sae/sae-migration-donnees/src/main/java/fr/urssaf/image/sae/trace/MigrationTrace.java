/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.trace.dao.IGenericTraceTypeDao;

/**
 * TODO (AC75095028) Description du type
 */
public class MigrationTrace {

   public String keyspace_tu = "keyspace_tu";

   protected static final Date DATE = new Date();

   @Autowired
   protected IGenericTraceTypeDao genericdao;
}
