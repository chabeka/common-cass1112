package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;


/**
 * Formatage d'un objet de type PagmList pour le passage d'une classe
 * de formulaire à un contrôleur.
 */
public class PagmListEditor extends PropertyEditorSupport {

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      PagmList pagmList = new PagmList();
      
      if (StringUtils.isNotBlank(text)) {

         String[] pagms = PropertyEditorUtils.eclateSurRetourCharriot(text);
         if (ArrayUtils.isNotEmpty(pagms)) {
            for(String pagm: pagms) {
               pagmList.add(pagm);
            }
         }
         
      }
      
      setValue(pagmList);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      PagmList pagmList = (PagmList)getValue();
      
      StringBuilder result = new StringBuilder();
      
      if (CollectionUtils.isNotEmpty(pagmList)) {
         for (String code:pagmList) {
            result.append(code);
            result.append("\r");
         }
      }
      
      return result.toString();
      
   }

   
}
