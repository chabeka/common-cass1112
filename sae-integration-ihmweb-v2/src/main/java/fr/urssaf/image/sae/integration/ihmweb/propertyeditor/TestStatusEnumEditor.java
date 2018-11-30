package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;


/**
 * Formatage d'un objet de type TestStatusEnum pour le passage d'une classe
 * de formulaire à un contrôleur.<br>
 * <br>
 * Tableau de conversion :<br>
 * <ul>
 *    <li>Succes=1</li>
 *    <li>Echec=2</li>
 *    <li>NonPasse=3</li>
 *    <li>NonLance=4</li>
 * </ul>
 */
public class TestStatusEnumEditor extends PropertyEditorSupport {

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      if (StringUtils.isNotBlank(text)) {
         setValue(getStringAsEnum(text));
      } else {
         setValue(TestStatusEnum.NonLance);
      }
      
   }

   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {
      
      TestStatusEnum status = (TestStatusEnum)getValue();
      
      return getEnumAsString(status);
      
   }
   
   
   private String getEnumAsString(TestStatusEnum status) {
      
      String result ;
      
      if (status==TestStatusEnum.Succes) {
         result = "1";
      } else if (status==TestStatusEnum.Echec) {
         result = "2";
      } else if (status==TestStatusEnum.NonPasse) {
         result = "3";
      } else if (status==TestStatusEnum.NonLance) {
         result = "4";
      } else if (status==TestStatusEnum.SansStatus) {
         result = "5";
      } else if (status==TestStatusEnum.AControler) {
         result = "6";
      } else {
         throw new IntegrationRuntimeException("La valeur de l'énumération " + status + " n'est pas transtypable pour le web.");
      }
      
      return result;
      
   }
   
   
   private TestStatusEnum getStringAsEnum(String valeur) {
      
      String valeurTrim = StringUtils.trim(valeur);
      
      TestStatusEnum result;
      
      if (StringUtils.equals(valeurTrim, "1")) {
         result = TestStatusEnum.Succes;
      } else if (StringUtils.equals(valeurTrim, "2")) {
         result = TestStatusEnum.Echec;
      } else if (StringUtils.equals(valeurTrim, "3")) {
         result = TestStatusEnum.NonPasse;
      } else if (StringUtils.equals(valeurTrim, "4")) {
         result = TestStatusEnum.NonLance;
      } else if (StringUtils.equals(valeurTrim, "5")) {
         result = TestStatusEnum.SansStatus;
      } else if (StringUtils.equals(valeurTrim, "6")) {
         result = TestStatusEnum.AControler;
      } else {
         throw new IntegrationRuntimeException("La valeur " + valeur + " n'est pas transtypable en TestStatusEnum.");
      }
      
      return result;
      
   }

   
}
