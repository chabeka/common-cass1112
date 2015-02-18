package fr.urssaf.image.sae.integration.ihmweb.utils;

import org.springframework.web.bind.WebDataBinder;

import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.IdentifiantPage;
import fr.urssaf.image.sae.integration.ihmweb.modele.LienHttpList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.CodeMetadonneeListEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.IdentifiantPageEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.LienHttpListEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.MetadonneeListEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.PagmListEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.RangeMetadonneeEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.RangeMetadonneeListEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.ResultatTestLogEditor;
import fr.urssaf.image.sae.integration.ihmweb.propertyeditor.TestStatusEnumEditor;

/**
 * Méthodes utilitaires pour les contrôleurs
 */
public final class ControllerUtils {

   
   private ControllerUtils() {
      
   }
   
   
   /**
    * Ajoute tous les objets de transtypage nécessaires pour l'application
    * 
    * @param binder le binder
    */
   public static void addAllBinders(WebDataBinder binder) {
      
      binder.registerCustomEditor(
            MetadonneeValeurList.class,
            new MetadonneeListEditor());
      
      binder.registerCustomEditor(
            MetadonneeRangeValeurList.class,
            new RangeMetadonneeListEditor());
      
      binder.registerCustomEditor(
            MetadonneeRangeValeur.class,
            new RangeMetadonneeEditor());
      
      binder.registerCustomEditor(
            IdentifiantPage.class,
            new IdentifiantPageEditor());
      
      binder.registerCustomEditor(
            TestStatusEnum.class,
            new TestStatusEnumEditor());
      
      binder.registerCustomEditor(
            ResultatTestLog.class,
            new ResultatTestLogEditor());
      
      binder.registerCustomEditor(
            LienHttpList.class,
            new LienHttpListEditor());
      
      binder.registerCustomEditor(
            CodeMetadonneeList.class,
            new CodeMetadonneeListEditor());
      
      binder.registerCustomEditor(
            PagmList.class,
            new PagmListEditor());
      
   }
   
}
