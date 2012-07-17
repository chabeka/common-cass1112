package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
