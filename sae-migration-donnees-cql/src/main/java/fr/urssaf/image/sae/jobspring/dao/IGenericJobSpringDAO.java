package fr.urssaf.image.sae.jobspring.dao;

import java.nio.ByteBuffer;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;

public interface IGenericJobSpringDAO extends IGenericDAO<GenericJobSpring, ByteBuffer> {

}