/**
 *
 */
package fr.urssaf.image.sae.storage.dfce.bo;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Objet représentant la liste des index composite
 */
@Component
public class IndexCompositeComponent {

  /**
   * Liste des index composite
   */
  private static Set<CompositeIndex> indexCompositeList;

  private final DFCEServices dfceServices;

  /**
   * Constructeur
   * 
   * @param dfceServices
   *          service de connexion à DFCE
   */
  @Autowired
  public IndexCompositeComponent(final DFCEServices dfceServices) {
    this.dfceServices = dfceServices;
  }

  /**
   * @return la liste des index composites
   */
  public final Set<CompositeIndex> getIndexCompositeList() {
    if (indexCompositeList == null) {
      synchronized (IndexCompositeComponent.class) {
        if (indexCompositeList == null) {
          loadIndexCompositeList();
        }
      }
    }
    return indexCompositeList;
  }

  /**
   * Charge la liste des index composite
   */
  public void loadIndexCompositeList() {
    // Récupération la liste des index composites
    indexCompositeList = dfceServices.fetchAllCompositeIndex();
  }

}
