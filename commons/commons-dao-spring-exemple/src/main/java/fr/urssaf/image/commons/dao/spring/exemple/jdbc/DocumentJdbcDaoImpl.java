package fr.urssaf.image.commons.dao.spring.exemple.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Auteur;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;



@Repository
public class DocumentJdbcDaoImpl implements DocumentDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public DocumentJdbcDaoImpl(@Qualifier("dataSource") DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	@SuppressWarnings({"PMD.ShortVariable","PMD.ConsecutiveLiteralAppends", "unchecked"})
	public Document find(Integer id) {

		StringBuffer sql = new StringBuffer(140);
		sql.append("select doc.id,doc.titre,doc.date,doc.id_auteur,aut.nom ");
		sql.append("from document doc ");
		sql.append("left outer join auteur aut on doc.id_auteur=aut.id ");
		sql.append("where doc.id= ? ");

		// String sql =
		// "select id, first_name, last_name from T_ACTOR where id = ?";
		RowMapper mapper = new RowMapper() {
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {

				Document document = new Document();
				document.setId(resultSet.getInt("id"));
				document.setTitre(resultSet.getString("titre"));
            document.setDate(resultSet.getDate("date"));
            
            if (resultSet.getString("id_auteur") != null) {

					Auteur auteur = new Auteur();
					auteur.setId(resultSet.getInt("id_auteur"));
               auteur.setNom(resultSet.getString("nom"));

					document.setAuteur(auteur);

				}

				return document;
			}
		};

		return (Document) jdbcTemplate.queryForObject(sql.toString(),
				new Object[] { id }, mapper);
	}

	@Override
	@SuppressWarnings("PMD.ShortVariable") 
	public Document get(Integer id) {
		return this.find(id);
	}

	@Override
	@Transactional
	@SuppressWarnings({"PMD.ShortVariable","PMD.ConsecutiveLiteralAppends"})
	public void save(Document document) {

		StringBuffer sql = new StringBuffer(63);
		sql.append("insert into document ");
		sql.append("(id_auteur, titre, date) ");
		sql.append("values (?, ?, ?) ");

		this.jdbcTemplate.update(sql.toString(), new Object[] {
				document.getAuteur() == null ? null:document.getAuteur().getId()
						, document.getTitre(), document.getDate() });

		int id = this.jdbcTemplate.queryForInt("CALL IDENTITY();");

		document.setId(id);
	}

	@Override
	public int count() {
		return this.jdbcTemplate.queryForInt("select count(*) from document");
	}
}
