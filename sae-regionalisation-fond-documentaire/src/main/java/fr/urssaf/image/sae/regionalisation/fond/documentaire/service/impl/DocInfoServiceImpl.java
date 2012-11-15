/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.AllRowsQuery;
import com.netflix.astyanax.serializers.StringSerializer;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.resultset.CassandraAllRowResultSet;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocInfoService;

/**
 * Implémentation de l'interface {@link DocInfoService}
 * 
 */
@Component
public class DocInfoServiceImpl implements DocInfoService {

   @Autowired
   private DocInfoDao dao;

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> getCodesOrganismes() throws CassandraException {

      AllRowsQuery<DocInfoKey, String> query = dao.getQuery();
      CassandraAllRowResultSet<DocInfoKey, String> resultSet = new CassandraAllRowResultSet<DocInfoKey, String>(
            query);
      List<String> codes = new ArrayList<String>();

      while (resultSet.hasNext()) {
         resultSet.next();

         List<String> columns = resultSet.getColumnNames();
         for (String columnKey : columns) {
            String value = resultSet.getValue(columnKey,
                  StringSerializer.get(), null);

            if (StringUtils.isNotBlank(value) && !codes.contains(value)) {
               codes.add(value);
            }
         }
      }

      Collections.sort(codes);

      return codes;
   }

}
