package fr.urssaf.image.sae.ecde.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Classe de chargement des messages pour la couche ECDE
 */
public final class EcdeMessageSource extends ResourceBundleMessageSource {

   private final List<String> baseNames = new ArrayList<String>();

   /**
    * {@inheritDoc}
    */
   @Override
   public void setBasenames(String[] basenames) {
      for (String s : basenames) {
         baseNames.add(s);
      }
      super.setBasenames(basenames);
   }

   /**
    * Renvoie la liste des messages correspondant à la Locale passée en
    * paramètre
    * 
    * @param locale
    *           la Locale dont on veut lister les messages
    * @return la liste des messages correspondant à la Locale passée en
    *         paramètre
    */
   public Map<String, String> getMessages(Locale locale) {
      Map<String, String> map = new TreeMap<String, String>();
      for (String baseName : baseNames) {
         map.putAll(getMessages(baseName, locale));
      }
      return map;
   }

   private Map<String, String> getMessages(String basename, Locale locale) {
      ResourceBundle bundle = getResourceBundle(basename, locale);
      Map<String, String> treeMap = new TreeMap<String, String>();
      for (String key : bundle.keySet()) {
         treeMap.put(key, getMessage(key, null, locale));
      }
      return treeMap;
   }

}
