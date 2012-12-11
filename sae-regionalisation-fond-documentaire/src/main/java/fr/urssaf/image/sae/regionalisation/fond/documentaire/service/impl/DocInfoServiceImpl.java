/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocInfoServiceImpl.class);

   @Autowired
   private DocInfoDao dao;

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, Long> getCodesOrganismes()
         throws CassandraException {

      LOGGER.info("Parcours de la Column Family DocInfo ...");

      AllRowsQuery<DocInfoKey, String> query = dao.getQuery();
      CassandraAllRowResultSet<DocInfoKey, String> resultSet = new CassandraAllRowResultSet<DocInfoKey, String>(
            query);
      Map<String, Long> codes = new HashMap<String, Long>();

      int nbDocInfo = 1;
      int nbDocInfoParTrace = 10000;

      while (resultSet.hasNext()) {
         resultSet.next();

         List<String> columns = resultSet.getColumnNames();

         if (!columns.isEmpty()) {

            for (String columnKey : columns) {

               String codeOrga = resultSet.getValue(columnKey, StringSerializer
                     .get(), null);

               if (StringUtils.isNotBlank(codeOrga)) {

                  String cleMap = codeOrga + ";" + columnKey;

                  if (codes.containsKey(cleMap)) {
                     codes.put(cleMap, codes.get(cleMap) + 1);
                  } else {
                     codes.put(cleMap, 1L);
                  }

               }
            }

            // Compteurs de lignes parcourues
            if (nbDocInfo % nbDocInfoParTrace == 0) {
               LOGGER.info("Nombre de lignes de DocInfo parcourues : {}",
                     nbDocInfo);
            }
            nbDocInfo++;

         }

      }

      LOGGER.info("Nombre de lignes de DocInfo au total : {}", nbDocInfo - 1);

      return codes;
   }

}
