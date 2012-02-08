package fr.urssaf.image.sae.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.model.SAEQueryData;
import fr.urssaf.image.sae.services.QueriesReferenceDAO;
import fr.urssaf.image.sae.services.XmlQueryDataService;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;

/**
 * Classe qui implémente l'interface {@link QueriesReferenceDAO}
 * MetadataReferenceService}
 * 
 * @author rhofir
 * 
 */
@Service
@Qualifier("queriesReferenceDAO")
@SuppressWarnings( { "PMD.LongVariable" })
public class QueriesReferenceDAOImpl implements QueriesReferenceDAO {
   // CHECKSTYLE:OFF
   @Autowired
   @Qualifier("xmlQueryDataService")
   private XmlQueryDataService xmlQueryDataService; // NOPMD

   @Autowired
   private ApplicationContext context;

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   /**
    * @param context
    *           : le context
    */
   public final void setContext(final ApplicationContext context) {
      this.context = context;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws SAESearchServiceEx
    *            Exception lever lorsque la récupération des requêtes ne sont
    *            pas disponibles.
    */
   public final Map<String, SAEQueryData> getAllQueries()
         throws SAESearchServiceEx {

      final Resource resource = context
            .getResource("classpath:saeTestqueries.xml");

      try {

         return xmlQueryDataService.saeQueriesReader(resource.getInputStream());

      } catch (IOException e) {
         throw new SAESearchServiceEx(MetadataMessageHandler.getMessage(
               "Le fichier contenant les requetes n''est pas disponible",
               resource.getFilename()), e);
      }

   }

   /**
    * {@inheritDoc}
    */
   public final SAEQueryData getQueryByType(String queryType)
         throws SAESearchServiceEx {
      SAEQueryData reqMetaDatas = null;
      final Map<String, SAEQueryData> referentiel = getAllQueries();
      for (Map.Entry<String, SAEQueryData> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().getQueryType().equals(queryType)) {
            reqMetaDatas = metaData.getValue();
         }
      }
      return reqMetaDatas;
   }
   // CHECKSTYLE:ON
}
