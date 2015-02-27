/**
 * 
 */
package fr.urssaf.image.sae.droit.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe d'un ensemble de droits du SAE. Chaque droit du SAE a une AU associée
 * à une liste de PRMD. Cela résulte de la fusion des PRMD pour les AU
 * identiques
 * 
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class SaeDroits extends HashMap<String, List<SaePrmd>> implements
      Map<String, List<SaePrmd>> {

   private static final long serialVersionUID = -5913141404840013369L;

  

}
