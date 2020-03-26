package fr.urssaf.image.sae.piletravaux.dao;

import java.util.UUID;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
//import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;

/**
 * (AC75095028) Interface pour la manipulation du GenericJobType
 */
public interface IGenericJobTypeDao extends IGenericDAO<GenericJobType, UUID> {

}
