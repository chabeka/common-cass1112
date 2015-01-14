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
import java.util.Map.Entry;

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
   
   private final static String DOMAINE_RH = "DomaineRH";
   private final static String DOMAINE_COTISANT = "DomaineCotisant";
   private final static String DOMAINE_COMPTABLE = "DomaineComptable";
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String createLucene(String lucene, List<SaePrmd> prmds) {

      LOGGER.debug("{} - Debut de la creation de la requete", TRC_LUCENE);

      LOGGER.debug("{} - Requête LUCENE de départ : {}", new String[] {
            TRC_LUCENE, lucene });

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

         // Suite à la découverte d'un problème dans l'analyseur de
         // requête DFCE en 1.1.0 (JIRA CRTL-95), on gère le cas particulier
         // d'1 seul PRMD (1 seule sous-requête)
         // En effet, la requête suivante ne fonctionne pas dans DFCE 1.1.0 :
         // (Meta1:Valeur1) AND ((Meta2:Valeur2))
         // Alors que celle-ci fonctionne :
         // (Meta1:Valeur1) AND (Meta2:Valeur2)
         if (sousRequetes.size() == 1) {

            // Cas particulier d'1 seul PRMD : pas besoin d'ajouter de
            // parenthèse
            // supplémentaire autour de la sous-requête. Les parenthèses sont
            // déjà
            // ajoutées lors de la construction de cette sous-requête.
            requete = "(" + requete + ") AND " + sousRequete;

         } else {

            requete = "(" + requete + ") AND (" + sousRequete + ")";
         }

      }

      LOGGER.debug("{} - Requête LUCENE travaillée : {}", new String[] {
            TRC_LUCENE, requete });
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
            buffer.append(" OR ");
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

         Map<String, String> valeurs;
         if (values == null) {
            valeurs = new HashMap<String, String>();
         } else {
            valeurs = values;
         }

         match = controle.isPermitted(metadatas, valeurs);

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

         if (StringUtils.isNotBlank(untypedMetadata.getLongCode())
               && StringUtils.isNotBlank(untypedMetadata.getValue())) {
            map.put(untypedMetadata.getLongCode().toUpperCase(),
                  untypedMetadata.getValue().toUpperCase());
         }
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

      Iterator<Entry<String, List<String>>> iterator = parametres.entrySet()
            .iterator();
      // Iterator<String> keyIterator = parametres.keySet().iterator();
      String key;
      while (iterator.hasNext() && match) {

         key = iterator.next().getKey();

         boolean metaStatic = containsIgnoreCase(metaValues.keySet(), key)
               && containsIgnoreCase(parametres.get(key), metaValues.get(key
                     .toUpperCase()));

         boolean metaDynamic = containsIgnoreCase(dynamicParam.keySet(), key)
               && metaValues.get(key.toUpperCase()).equalsIgnoreCase(
                     dynamicParam.get(key));

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

         for (Entry<String, String> entry : values.entrySet()) {
            requete = requete.replace("<%" + entry.getKey() + "%>", values
                  .get(entry.getKey()));
         }
      }

      return requete;
   }

   /**
    * Teste si une clé de mata correspond à l'un des trois domaines :
    * {DomaineRH, DOMAIN_COTISANT,DOMAIN_COMPTABLE}
    * 
    * @param value : la valeur à tester
    * @return
    */
   private boolean isDomaineRhCotiOrCompt(String value){
      if(value.equals(DOMAINE_RH)|| value.equals(DOMAINE_COTISANT)
            || value.equals(DOMAINE_COMPTABLE)){
         return true;
      }
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addDomaine(List<UntypedMetadata> metadatas, List<SaePrmd> prmds) {
      
      //-- On vérifie qu'aucune métadonnée « Domaine* » n’est présente
      boolean isDomaineFound = false;
      for (UntypedMetadata meta : metadatas) {
         if(isDomaineRhCotiOrCompt(meta.getLongCode())){
            isDomaineFound = true;
            break;
         }
      }
      
      //-- Aucun domaine présent, 
      if(!isDomaineFound){
         
         int addCount = 0;
         
         //-- On boucle sur la liste des prmds passée en paramètre
         for (SaePrmd saePrmd : prmds) {
            
            PrmdControle controle;
            Prmd prmd = saePrmd.getPrmd();
            String prmdName = prmd.getBean();
            Map<String, String> prmdValues = saePrmd.getValues();
            Map<String, List<String>> prmdMetas = prmd.getMetadata();
            
            //-- Cas d'un prdm de type bean
            if(!StringUtils.isEmpty(prmdName)){
               try {
                  controle = context.getBean(prmdName, PrmdControle.class);
                  controle.addDomaine(metadatas, prmdValues);
               } catch (BeansException e) {
                  LOGGER.warn("{} - Aucune fonction {} n'existe pour le Prmd {}",
                        new String[] { TRC_CHECK, prmd.getCode(), prmd.getBean() });
               }
            }
            //-- Prmd dynamique
            else if(!MapUtils.isEmpty(prmdValues)){
               for (Map.Entry<String, List<String>> entry : prmdMetas.entrySet()) {
                  if (isDomaineRhCotiOrCompt(entry.getKey()) && prmdValues.containsKey(entry.getKey())) {
                     String valeur = prmdValues.get(entry.getKey());     
                     metadatas.add(new UntypedMetadata(entry.getKey(), valeur));
                     addCount++;
                     break;
                  }
               }
            }
            //-- Prmd classique
            else {
               for (Map.Entry<String, List<String>> entry : prmdMetas.entrySet()) {
                  if(isDomaineRhCotiOrCompt(entry.getKey())){
                     String valeur = saePrmd.getValues().get(entry.getKey());
                     metadatas.add(new UntypedMetadata(entry.getKey(), valeur));
                     break;
                  }
               }
            }
         }
         
         //-- Aucun domaine n’a été ajouté à la liste des métadonnées
         if(addCount == 0){
            metadatas.add(new UntypedMetadata(DOMAINE_COTISANT, "1"));
         }
      }
   }
}
