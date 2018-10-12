package fr.urssaf.image.sae.commons.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.commons.dao.IGenericIndexDAO;
import fr.urssaf.image.sae.commons.utils.ColumnUtil;

/**
 * @param <T>
 */
public class GenericIndexDAOImpl<T, ID> implements IGenericIndexDAO<T, ID> {

   @Autowired
   protected CassandraCQLClientFactory ccf;

   private MappingManager manager;

   protected Class<? extends T> daoType;

   protected Mapper<T> mapper;

   private static final Logger LOGGER = LoggerFactory.getLogger(GenericIndexDAOImpl.class);

   public GenericIndexDAOImpl() {
      final Type t = getClass().getGenericSuperclass();
      final ParameterizedType pt = (ParameterizedType) t;
      daoType = (Class) pt.getActualTypeArguments()[0];
   }

   /**
    * Mapper le type T à la table cassandra
    *
    * @return
    */
   @Override
   @SuppressWarnings("unchecked")
   public Mapper<T> getMapper() {
      if (mapper == null) {
         manager = new MappingManager(ccf.getSession());
         if (ccf.getStartLocal()) {
            mapper = (Mapper<T>) manager.mapper(daoType, CassandraServerBeanCql.KEYSPACE_TU);
         } else {
            mapper = (Mapper<T>) manager.mapper(daoType);
         }
      }
      return mapper;
   }

   /**
    * @return the daoType
    */
   @Override
   public Class<? extends T> getDaoType() {
      return daoType;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public CassandraCQLClientFactory getCcf() {
      return ccf;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Iterator<T> findAllWithMapperById(final ID id) {
      Assert.notNull(id, "L'id ne peut être null");
      final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
      final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
      Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

      final String keyName = keyField.getName();
      select.where(QueryBuilder.eq(keyName, id));
      return getMapper().map(getSession().execute(select)).iterator();
   }

}
