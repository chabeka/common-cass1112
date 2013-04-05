package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.cache.LoadingCache;
import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.support.DictionarySupport;

/**
 * Classe d'implémentation du serviceDictionaryService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 */

public class DictionaryServiceImpl implements DictionaryService {
   
   @Autowired
   private DictionarySupport dictionarySupport;
   @Autowired
   private JobClockSupport clockSupport;
   @Autowired
   private CuratorFramework curator;
   private LoadingCache<String, Dictionary> dictionaries;
   @Value("sae.metadata.cache")
   private int duration;
   
   @Override
   public void addElements(String name, List<String> values) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void deleteElement(String name, List<String> values) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public Dictionary find(String name) throws DictionaryNotFoundException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<Dictionary> findAll() {
      // TODO Auto-generated method stub
      return null;
   }

}
