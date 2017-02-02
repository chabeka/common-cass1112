package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;


/**
 * Formatage d'un objet de type UUIDList pour le passage d'une classe
 * de formulaire à un contrôleur.
 */
public class UUIDListEditor extends PropertyEditorSupport {

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      UUIDList uuidList = new UUIDList();
      
      if (StringUtils.isNotBlank(text)) {

         String[] uuids = PropertyEditorUtils.eclateSurRetourCharriot(text);
         if (ArrayUtils.isNotEmpty(uuids)) {
            for(String uuid: uuids) {
               uuidList.add(uuid);
            }
         }
         
      }
      
      setValue(uuidList);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      UUIDList uuidList = (UUIDList)getValue();
      
      StringBuilder result = new StringBuilder();
      
      if (CollectionUtils.isNotEmpty(uuidList)) {
         for (String code:uuidList) {
            result.append(code);
            result.append("\r");
         }
      }
      
      return result.toString();
      
   }

   
}
