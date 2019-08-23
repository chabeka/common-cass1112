/**
 *
 */
package fr.urssaf.image.sae.storage.dfce.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import net.docubase.toolkit.model.reference.LifeCycleRule;

/**
 * Objet représentant la liste de type de documents
 */
@Component
public class DocumentsTypeList {

   /**
    * Liste des types de documents supportés
    */
   private static List<String> types;

   private final DFCEServices dfceServices;

   /**
    * Constructeur
    *
    * @param dfceServices
    *           service de connexion à DFCE
    */
   @Autowired
   public DocumentsTypeList(final DFCEServices dfceServices) {
      this.dfceServices = dfceServices;
   }

   /**
    * @return la liste des documents supportés
    */
   public final List<String> getTypes() {
      if (types == null) {
         synchronized (DocumentsTypeList.class) {
            if (types == null) {
               loadDocumentTypeList();
            }
         }
      }
      return types;
   }

   /**
    * Charge la liste des types de documents supportés
    */
   private void loadDocumentTypeList() {
      final Set<LifeCycleRule> lifeCycleRules = dfceServices.getAllLifeCycleRules();

      types = new ArrayList<>();

      for (final LifeCycleRule rule : lifeCycleRules) {
         types.add(rule.getDocumentType());
      }
   }

}
