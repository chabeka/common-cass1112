package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;


/**
 * Formatage d'un objet de type ResultatTestLog pour le passage d'une classe
 * de formulaire à un contrôleur.
 */
public class ResultatTestLogEditor extends PropertyEditorSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      ResultatTestLog log = new ResultatTestLog();
      
      if (StringUtils.isNotBlank(text)) {
         String[] lignes = PropertyEditorUtils.eclateSurRetourCharriot(text);
         log.appendArrayLn(lignes);
      }
      
      setValue(log);

   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      ResultatTestLog log = (ResultatTestLog)getValue();
      
      String result = StringUtils.EMPTY;
      
      if (log!=null) {
         
         StringBuffer stringBuffer = new StringBuffer();
         
         for(String item: log.getLog()) {
            stringBuffer.append(item);
         }
         result = stringBuffer.toString();
         
      }
      
      return result;
      
   }

   
}
