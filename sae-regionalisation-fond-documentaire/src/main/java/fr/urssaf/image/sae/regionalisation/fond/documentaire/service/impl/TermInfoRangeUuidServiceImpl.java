/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.ObjectSerializer;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.TermInfoRangeUuidDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidColumn;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.resultset.CassandraRowResultSet;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TermInfoRangeUuidService;

/**
 * Classe d'implémentation de l'interface {@link TermInfoRangeUuidService}
 * 
 */
@Component
public class TermInfoRangeUuidServiceImpl implements TermInfoRangeUuidService {

   @Autowired
   private TermInfoRangeUuidDao dao;

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<Map<String, String>> getInfosDoc() {

      try {
         RowQuery<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> query = dao
               .getAllUuidColumns();
         CassandraRowResultSet<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> resultSet = new CassandraRowResultSet<TermInfoRangeUuidKey, TermInfoRangeUuidColumn>(
               query);
         List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
         Map<String, String> map;

         while (resultSet.hasNext()) {
            resultSet.next();

            map = new HashMap<String, String>();
            UUID uuid = resultSet.getName().getDocumentUUID();
            map.put(Constants.UUID, uuid.toString());

            Object colValue = resultSet.getValue(ObjectSerializer.get());
            @SuppressWarnings("unchecked")
            Map<String, List<String>> colMap = (Map<String, List<String>>) colValue;

            map.put(Constants.CODE_ORG_GEST, getValue(colMap,
                  Constants.CODE_ORG_GEST));
            map.put(Constants.CODE_ORG_PROP, getValue(colMap,
                  Constants.CODE_ORG_PROP));

            datas.add(map);

         }

         return datas;

      } catch (CassandraException exception) {
         throw new ErreurTechniqueException(exception);
      }
   }

   private String getValue(Map<String, List<String>> map, String key) {
      String value = null;

      if (MapUtils.isNotEmpty(map) && CollectionUtils.isNotEmpty(map.get(key))) {
         value = map.get(key).get(0);
      }

      return value;
   }

}
