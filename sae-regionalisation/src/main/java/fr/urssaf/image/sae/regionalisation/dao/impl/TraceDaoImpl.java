package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;

/**
 * Implémentation du service {@link TraceDao}
 * 
 * 
 */
@Repository
public class TraceDaoImpl implements TraceDao {

   private final JdbcTemplate jdbcTemplate;

   /**
    * 
    * @param dataSource
    *           paramètres des sources
    */
   @Autowired
   public TraceDaoImpl(DataSource dataSource) {
      this.jdbcTemplate = new JdbcTemplate(dataSource);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional
   public final void addTraceMaj(Trace trace) {

      StrBuilder sql = new StrBuilder();
      sql.append("insert into trace_maj ");
      sql
            .append("(id_document, id_critere, nom_metadata,ancienne_valeur,nouvelle_valeur) ");
      sql.append("values (?, ?, ?, ?, ?) ");

      this.jdbcTemplate.update(sql.toString(), new Object[] {
            trace.getIdDocument(), trace.getIdSearch(), trace.getMetaName(),
            trace.getOldValue(), trace.getNewValue() });

   }

   /***
    * {@inheritDoc}
    */
   @Override
   public final void addTraceRec(BigDecimal idCriterion, int documentCount) {

      StrBuilder sql = new StrBuilder();
      sql.append("insert into trace_rec ");
      sql.append("(id_critere, nbre_doc) ");
      sql.append("values (?, ?) ");

      this.jdbcTemplate.update(sql.toString(), new Object[] { idCriterion,
            documentCount });

   }

   /***
    * {@inheritDoc}
    */
   @Override
   public final int findNbreDocs(BigDecimal idCriterion) {

      StrBuilder sql = new StrBuilder();
      sql.append("select nbre_doc ");
      sql.append("from trace_rec ");
      sql.append("where id =  ");
      sql
            .append("select max(trace.id) from trace_rec trace where id_critere= ?  ");

      int result = jdbcTemplate.queryForInt(sql.toString(),
            new Object[] { idCriterion });

      return result;
   }

   /***
    * {@inheritDoc}
    */
   @Override
   public final List<Trace> findTraceMajByCriterion(BigDecimal idCriterion) {
      StrBuilder sql = new StrBuilder();
      sql
            .append("select id_critere,id_document,nom_metadata,ancienne_valeur,nouvelle_valeur ");
      sql.append("from trace_maj ");
      sql.append("where id_critere=? ");

      RowMapper<Trace> mapper = new RowMapper<Trace>() {
         public Trace mapRow(ResultSet resultSet, int rowNum)
               throws SQLException {

            Trace trace = new Trace();
            trace.setIdDocument(UUID.fromString(resultSet
                  .getString("id_document")));
            trace.setIdSearch(resultSet.getBigDecimal("id_critere"));
            trace.setMetaName(resultSet.getString("nom_metadata"));
            trace.setNewValue(resultSet.getString("nouvelle_valeur"));
            trace.setOldValue(resultSet.getString("ancienne_valeur"));

            return trace;
         }
      };

      List<Trace> results = jdbcTemplate.query(sql.toString(),
            new Object[] { idCriterion }, mapper);

      return results;
   }

}
