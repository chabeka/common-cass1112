/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.utils.DocInfoResultSet;
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
   public List<String> getCodesOrganismes() {
      AllRowsQuery<DocInfoKey, String> query = dao.getQuery();
      DocInfoResultSet resultSet = new DocInfoResultSet(query);

      List<String> codes = new ArrayList<String>();
      Map<String, String> values;

      while ((values = resultSet.getNextRecord()) != null) {
         for (String value : values.values()) {
            if (!codes.contains(value)) {
               codes.add(value);
            }
         }
      }

      Collections.sort(codes);

      return codes;
   }

}
