package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * Classe d'implémentation du serviceDictionaryService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

   private final DictionarySupport dictionarySupport;
   private final JobClockSupport clockSupport;
   private final CuratorFramework curator;
   private LoadingCache<String, Dictionary> dictionaries;
   private int duration;
   private final String PREFIXE_DICT ="/Dictionary/";
   private static final Logger LOGGER = LoggerFactory
   .getLogger(DictionaryServiceImpl.class);
   private static final String TRC_CREATE = "addElements()";
   private static final String TRC_DELETE = "deleteElements()";

   @Autowired
   public DictionaryServiceImpl(DictionarySupport dictSupport,
         JobClockSupport jobClockSupport, CuratorFramework curatorFramework,
         @Value("${sae.metadata.cache}") int value) {
      this.dictionarySupport = dictSupport;
      this.clockSupport = jobClockSupport;
      this.curator = curatorFramework;
      this.duration = value;

      dictionaries = CacheBuilder.newBuilder().refreshAfterWrite(duration,
            TimeUnit.MINUTES).build(new CacheLoader<String, Dictionary>() {

         @Override
         public Dictionary load(String identifiant)
               throws DictionaryNotFoundException {
            return dictionarySupport.find(identifiant);
         }

      });
   }

   /**
    * Ajout d'un nouveau dictionnaire
    */
   @Override
   public void addElements(String name, List<String> values) {
      
      String resourceName = PREFIXE_DICT + name;

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator,
            resourceName);
      try {
         
         LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
         ZookeeperUtils.acquire(mutex, resourceName);
         LOGGER.debug("{} - Création du Dictionnaire", TRC_CREATE);
         for (String value : values) {
            dictionarySupport.addElement(name, value, clockSupport.currentCLock());
         }
         
         checkLock(mutex, name);

      } finally {
         mutex.release();
      }
      


   }

   /**
    * Supression d'un dictionnaire. Attention les données supprimées sont
    * toujours présentes en cache jusqu'a leur expiration
    */

   @Override
   public void deleteElements(String name, List<String> values) {
      for (String value : values) {
         dictionarySupport.deleteElement(name, value, clockSupport
               .currentCLock());
      }
   }

   /**
    * Méthode permettant de récupérer un dictionnaire donnée. Si le dictionnaire
    * n'est pas en cache on appel la méthode find sinon on renvoit la valeur
    * contenu dans le cache.
    */
   @Override
   public Dictionary find(String name) throws DictionaryNotFoundException {

      return dictionaries.getUnchecked(name);
   }

   /**
    * Méthode permettant de récupérer tous les dictionnaires sans passer par le
    * cache.
    */
   @Override
   public List<Dictionary> findAll() {
      return dictionarySupport.findAll();
   }
   
   private void checkLock(ZookeeperMutex mutex, String dictionary) {

      if (!ZookeeperUtils.isLock(mutex)) {

         Dictionary storedDict =null;
         try {
            storedDict = dictionarySupport.find(dictionary);
         } catch (DictionaryNotFoundException e) {
            throw new MetadataReferenceException("Le dictionnaire "+ dictionary
                  + " n'a pas été créé");
         }

         if (!storedDict.getId().equals(dictionary)) {
            throw new MetadataRuntimeException("Le dictionnaire "+ dictionary +" a déjà été créé");
         }

      }

   }

}
