/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.sae.dao.IMetadataDAO;
import fr.urssaf.image.sae.model.GenericType;
import fr.urssaf.image.sae.model.Metadata;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class MetadataDAOImpl extends GenericDAOImpl<Metadata, String> implements IMetadataDAO {

  /**
   * {@inheritDoc}
   */
  @Override
  public ResultSet findAllMatadatas() {
    final String query0 = "SELECT key, column1, value FROM \"Metadata\";";
    final ResultSet result = getSession().execute(query0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<GenericType> findAllGenericType() {
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), "\"Metadata\"");
    return null;
  }

  @Override
  public void insert(final GenericType entity) {
    final Map<String, Object> map = new LinkedHashMap<>();
   // getTemplate().getConverter().write(entity, map);
    final Insert insert = QueryBuilder.insertInto(ccf.getKeyspace(), "\"Metadata\"");
    map.forEach((k, v) -> {
      if (!k.contains("str")) {
        insert.value(k, v);
      }
    });
    //getCqlOperations().execute(insert);
    // return entity;
  }

}
