/**
 * 
 */
package fr.urssaf.image.sae.storage.dfce.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.docubase.toolkit.model.reference.LifeCycleRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Objet représentant la liste de type de documents
 * 
 */
@Component
public class DocumentsTypeList {

   /**
    * Liste des types de documents supportés
    */
   private List<String> types;

   private final DFCEServicesManager dfceServicesManager;

   /**
    * Consturcteur
    * 
    * @param dfceServicesManager
    *           service de connexion à DFCE
    */
   @Autowired
   public DocumentsTypeList(DFCEServicesManager dfceServicesManager) {
      this.dfceServicesManager = dfceServicesManager;
      this.types = null;
   }

   /**
    * @return la liste des documents supportés
    */
   public final List<String> getTypes() {

      synchronized (DocumentsTypeList.class) {

         if (types == null) {
            init();
         }

      }

      return types;

   }

   private void init() {

      boolean startActive = dfceServicesManager.isActive();
      try {

         if (!startActive) {
            dfceServicesManager.getConnection();
         }

         Set<LifeCycleRule> lifeCycleRules = dfceServicesManager
               .getDFCEService().getStorageAdministrationService()
               .getAllLifeCycleRules();

         types = new ArrayList<String>();

         for (LifeCycleRule rule : lifeCycleRules) {
            types.add(rule.getDocumentType());
         }

      } catch (ConnectionServiceEx exception) {
         throw new DocumentTypeException("impossible de se connecter à DFCE",
               exception);
      } finally {
         if (!startActive) {
            dfceServicesManager.closeConnection();
         }
      }
   }

}
