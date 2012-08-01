/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.controle.PrmdControle;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;

/**
 * Classe d'implémentation du service {@link PrmdService}. Cette classe est un
 * singleton est peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Component
public class PrmdServiceImpl implements PrmdService {

   @Autowired
   private ApplicationContext context;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(PrmdServiceImpl.class);

   private static final String TRC_CHECK = "checkBean()";
   private static final String TRC_LUCENE = "createLucene()";

   /**
    * {@inheritDoc}
    */
   @Override
   public final String createLucene(String lucene, List<SaePrmd> prmds) {

      LOGGER.debug("{} - Debut de la creation de la requete", TRC_LUCENE);

      String currentRequete;
      Prmd prmd;
      SaePrmd saePrmd;

      List<String> sousRequetes = new ArrayList<String>();

      for (int index = 0; index < prmds.size(); index++) {
         saePrmd = prmds.get(index);
         currentRequete = StringUtils.EMPTY;
         prmd = saePrmd.getPrmd();

         if (StringUtils.isNotEmpty(prmd.getLucene())) {
            LOGGER.debug("{} - Concaténation avec la requete lucène du PRMD",
                  TRC_LUCENE);
            currentRequete = createLucene(prmd, saePrmd.getValues());

         } else if (StringUtils.isNotEmpty(prmd.getBean())) {
            LOGGER.debug("{} - Concaténation avec la requête du bean",
                  TRC_LUCENE);
            currentRequete = createBean(prmd, saePrmd.getValues());

         } else {
            LOGGER.info("pas de définition de requête pour le PRMD "
                  + prmd.getCode());
         }

         if (StringUtils.isNotEmpty(currentRequete)) {
            sousRequetes.add(currentRequete);
         }

      }

      LOGGER.debug("{} - Assemblage de la sous requête", TRC_LUCENE);
      String sousRequete = createSousRequete(sousRequetes);

      String requete = lucene;
      if (StringUtils.isNotEmpty(sousRequete)) {
         LOGGER.debug("{} - Assemblage de la requête définitive", TRC_LUCENE);
         requete = "(" + requete + ")AND(" + sousRequete + ")";
      }

      return requete;
   }

   /**
    * @param currentRequete
    * @return
    */
   private String createSousRequete(List<String> sousRequetes) {

      StringBuffer buffer = new StringBuffer();
      for (int index = 0; index < sousRequetes.size(); index++) {
         if (index != 0) {
            buffer.append("OR");
         }
         buffer.append("(" + sousRequetes.get(index) + ")");
      }

      return buffer.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean isPermitted(List<UntypedMetadata> metadatas,
         List<SaePrmd> prmds) {

      boolean match = false;
      int index = 0;
      SaePrmd saePrmd;
      Prmd prmd;

      Map<String, String> metaValues = getMapFromMeta(metadatas);

      while (!match && index < prmds.size()) {

         saePrmd = prmds.get(index);
         prmd = saePrmd.getPrmd();

         if (MapUtils.isNotEmpty(prmd.getMetadata())) {

            match = checkPrmd(prmd, saePrmd.getValues(), metaValues);

         } else if (StringUtils.isNotEmpty(prmd.getBean())) {

            match = checkBean(prmd, metadatas, saePrmd.getValues());

         } else {
            LOGGER.debug("pas de périmètre défini");
         }

         index++;

      }

      return match;
   }

   /**
    * @param bean
    * @param metadatas
    * @param values
    * @return
    */
   private boolean checkBean(Prmd prmd, List<UntypedMetadata> metadatas,
         Map<String, String> values) {

      boolean match = false;

      PrmdControle controle;
      try {
         controle = context.getBean(prmd.getBean(), PrmdControle.class);

         if (values == null) {
            values = new HashMap<String, String>();
         }

         match = controle.isPermitted(metadatas, values);

      } catch (BeansException e) {
         LOGGER.warn("{} - Aucune fonction {} n'existe pour le Prmd {}",
               new String[] { TRC_CHECK, prmd.getCode(), prmd.getBean() });
      }

      return match;
   }

   /**
    * @param metadatas
    * @return
    */
   private Map<String, String> getMapFromMeta(List<UntypedMetadata> metadatas) {

      Map<String, String> map = new HashMap<String, String>();
      for (UntypedMetadata untypedMetadata : metadatas) {
         map.put(untypedMetadata.getLongCode().toUpperCase(), untypedMetadata
               .getValue().toUpperCase());
      }

      return map;
   }

   /**
    * Vérifie que le périmètre est bon
    * 
    * @param prmd
    * @param values
    * @param metaValues
    * @return
    */
   private boolean checkPrmd(Prmd prmd, Map<String, String> dynamicValues,
         Map<String, String> metaValues) {
      
      if (metaValues == null) {
         metaValues = new HashMap<String, String>();
      }
      
      boolean match = true;
      Map<String, List<String>> parametres = prmd.getMetadata();

      Map<String, String> dynamicParam;
      if (dynamicValues == null) {
         dynamicParam = new HashMap<String, String>();
      } else {
         dynamicParam = dynamicValues;
      }

      if (parametres == null) {
         parametres = new HashMap<String, List<String>>();
      }

      Iterator<String> keyIterator = parametres.keySet().iterator();
      String key;
      while (keyIterator.hasNext() && match) {

         key = keyIterator.next();

         boolean metaStatic = containsIgnoreCase(metaValues.keySet(), key)
               && containsIgnoreCase(parametres.get(key), metaValues.get(key
                     .toUpperCase()));

         boolean metaDynamic = containsIgnoreCase(dynamicParam.keySet(), key)
               && containsIgnoreCase(parametres.get(key), dynamicParam.get(key));

         if (!metaStatic && !metaDynamic) {
            match = false;
         }

      }

      return match;
   }

   private boolean containsIgnoreCase(Collection<String> collection,
         String value) {
      boolean found = false;

      Iterator<String> iterator = collection.iterator();
      String currentValue;

      while (iterator.hasNext() && !found) {
         currentValue = iterator.next();
         if (currentValue.equalsIgnoreCase(value)) {
            found = true;
         }
      }

      return found;
   }

   private String createBean(Prmd prmd, Map<String, String> parametres) {
      String requete;

      try {
         PrmdControle controle = context.getBean(prmd.getBean(),
               PrmdControle.class);

         if (parametres == null) {
            parametres = new HashMap<String, String>();
         }
         requete = controle.createLucene(parametres);

      } catch (BeansException e) {
         requete = null;
      }

      return requete;
   }

   private String createLucene(Prmd prmd, Map<String, String> values) {

      String requete = prmd.getLucene();
      if (MapUtils.isNotEmpty(values)) {

         for (String key : values.keySet()) {
            requete = requete.replace("<%" + key + "%>", values.get(key));
         }
      }

      return requete;
   }
}
