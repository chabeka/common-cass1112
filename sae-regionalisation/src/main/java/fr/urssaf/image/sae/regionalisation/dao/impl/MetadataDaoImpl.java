package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.regionalisation.bean.Metadata;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;

/**
 * Classe d'implémentation du service {@link MetadataDao}
 * 
 * 
 */
@Repository
public class MetadataDaoImpl implements MetadataDao {

   private final JdbcTemplate jdbcTemplate;

   /**
    * 
    * @param dataSource
    *           paramètres des sources
    */
   @Autowired
   public MetadataDaoImpl(DataSource dataSource) {
      this.jdbcTemplate = new JdbcTemplate(dataSource);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, Object> getMetadatas(BigDecimal identifiant) {

      Map<String, Object> metadonnees = find(identifiant);

      Map<String, Object> results = new HashMap<String, Object>();

      for (String code : METADATAS) {

         boolean flag = MapUtils.getBooleanValue(metadonnees, code + "_flag");
         if (flag) {
            results.put(code, metadonnees.get(code));
         }
      }

      return results;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, Object> find(BigDecimal identifiant) {

      StrBuilder sql = new StrBuilder();
      sql.append("select * ");
      sql.append("from metadonnees ");
      sql.append("where id_critere = ? ");

      Map<String, Object> results;

      try {
         results = jdbcTemplate.queryForMap(sql.toString(),
               new Object[] { identifiant });
      } catch (EmptyResultDataAccessException e) {
         results = new HashMap<String, Object>();
      }

      return results;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional
   public final void save(BigDecimal idCritere, List<Metadata> metadatas) {

      Assert.notNull(idCritere, "'idCritere' is required");
      Assert.notNull(metadatas, "'metadatas' is required");

      StrBuilder sql = new StrBuilder();
      sql.append("insert into metadonnees ");
      sql.append("(id_critere ");

      List<Object> args = new ArrayList<Object>();
      for (Metadata metadata : metadatas) {

         sql.append("," + metadata.getCode());
         sql.append("," + metadata.getCode() + "_flag");

         args.add(metadata.getValue());
         args.add(metadata.isFlag());
      }

      sql.append(") ");

      sql.append("values (?");
      for (int i = 0; i < metadatas.size(); i++) {

         sql.append(",?,?");

      }
      sql.append(")");

      this.jdbcTemplate.update(sql.toString(), ArrayUtils.add(args.toArray(),
            0, idCritere));

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<Metadata> getAllMetadatas(BigDecimal idCritere) {

      Map<String, Object> metadonnees = find(idCritere);

      List<Metadata> results = new ArrayList<Metadata>();

      for (String code : METADATAS) {

         boolean flag = MapUtils.getBooleanValue(metadonnees, code + "_flag");
         Object value = metadonnees.get(code);

         Metadata metadata = new Metadata();
         metadata.setCode(code);
         metadata.setFlag(flag);
         metadata.setValue(value);

         results.add(metadata);

      }

      return results;
   }
}
