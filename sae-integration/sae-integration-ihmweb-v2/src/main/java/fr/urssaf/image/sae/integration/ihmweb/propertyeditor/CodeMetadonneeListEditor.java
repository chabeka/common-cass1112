package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;


/**
 * Formatage d'un objet de type CodeMetadonneeList pour le passage d'une classe
 * de formulaire à un contrôleur.
 */
public class CodeMetadonneeListEditor extends PropertyEditorSupport {

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      CodeMetadonneeList codesMetadonnes = new CodeMetadonneeList();
      
      if (StringUtils.isNotBlank(text)) {

         String[] codes = PropertyEditorUtils.eclateSurRetourCharriot(text);
         if (ArrayUtils.isNotEmpty(codes)) {
            for(String code: codes) {
               codesMetadonnes.add(code);
            }
         }
         
      }
      
      setValue(codesMetadonnes);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      CodeMetadonneeList codesMetadonnes = (CodeMetadonneeList)getValue();
      
      StringBuilder result = new StringBuilder();
      
      if (CollectionUtils.isNotEmpty(codesMetadonnes)) {
         for (String code:codesMetadonnes) {
            result.append(code);
            result.append("\r");
         }
      }
      
      return result.toString();
      
   }

   
}
