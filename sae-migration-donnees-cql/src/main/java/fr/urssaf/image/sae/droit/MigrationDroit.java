/**
 *   (AC75095028) 
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.droit.dao.IGenericDroitTypeDao;

/**
 * (AC75095028) Classe de migration de tous les droits
 */
public class MigrationDroit {

  public String keyspace_tu = "keyspace_tu";

  protected static final Date DATE = new Date();

  @Autowired
  protected IGenericDroitTypeDao genericdao;
}
