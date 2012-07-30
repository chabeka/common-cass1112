package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;

/**
 * Classe d'implémentation du service {@link SearchCriterionDao}
 * 
 * 
 */
@Repository
public class SearchCriterionDaoImpl implements SearchCriterionDao {

   private final JdbcTemplate jdbcTemplate;

   /**
    * 
    * @param dataSource
    *           paramètres des sources
    */
   @Autowired
   public SearchCriterionDaoImpl(DataSource dataSource) {
      this.jdbcTemplate = new JdbcTemplate(dataSource);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SearchCriterion> getSearchCriteria(int firstRecord,
         int recordCount) {

      StrBuilder sql = new StrBuilder();
      sql.append("select lucene,traite,id ");
      sql.append("from criteres ");
      sql.append("where traite = ? ");
      sql.append("order by id asc ");
      sql.append("limit ? ");
      sql.append("offset ? ");

      RowMapper<SearchCriterion> mapper = new RowMapper<SearchCriterion>() {
         public SearchCriterion mapRow(ResultSet resultSet, int rowNum)
               throws SQLException {

            SearchCriterion searchCriterion = new SearchCriterion();
            searchCriterion.setId(resultSet.getBigDecimal("id"));
            searchCriterion.setLucene(resultSet.getString("lucene"));
            searchCriterion.setUpdated(resultSet.getBoolean("traite"));

            return searchCriterion;
         }
      };

      List<SearchCriterion> results = jdbcTemplate.query(sql.toString(),
            new Object[] { false, recordCount, firstRecord }, mapper);

      return results;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional
   public final void updateSearchCriterion(BigDecimal identifiant) {

      StrBuilder sql = new StrBuilder();
      sql.append("update criteres ");
      sql.append("set ");
      sql.append("traite=? ");
      sql.append("where id=? ");

      this.jdbcTemplate.update(sql.toString(),
            new Object[] { true, identifiant });

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SearchCriterion find(BigDecimal identifiant) {

      StrBuilder sql = new StrBuilder();
      sql.append("select lucene,traite,id ");
      sql.append("from criteres ");
      sql.append("where id=? ");

      RowMapper<SearchCriterion> mapper = new RowMapper<SearchCriterion>() {
         public SearchCriterion mapRow(ResultSet resultSet, int rowNum)
               throws SQLException {

            SearchCriterion searchCriterion = new SearchCriterion();
            searchCriterion.setId(resultSet.getBigDecimal("id"));
            searchCriterion.setLucene(resultSet.getString("lucene"));
            searchCriterion.setUpdated(resultSet.getBoolean("traite"));

            return searchCriterion;
         }
      };

      SearchCriterion result = jdbcTemplate.queryForObject(sql.toString(),
            new Object[] { identifiant }, mapper);

      return result;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void save(SearchCriterion searchCriterion) {

      StrBuilder sql = new StrBuilder();
      sql.append("insert into criteres ");
      sql.append("(lucene,traite) ");
      sql.append("values (?, ?) ");

      this.jdbcTemplate.update(sql.toString(), new Object[] {
            searchCriterion.getLucene(), searchCriterion.isUpdated() });

      int idcritere = this.jdbcTemplate.queryForInt("select lastval()");
      searchCriterion.setId(new BigDecimal(idcritere));

   }
}
