package fr.urssaf.image.sae.webservices.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.util.Assert;

/**
 * Liste des droits autorisés : liste des actions unitaires et de leurs
 * périmètres de données associé<br>
 * <br>
 * La classe étend une {@link HashMap}<br>
 * Instanciation:
 * <ul>
 * <li><code>key</code>: action unitaire de type {@link String}</li>
 * <li><code>values</code>: périmètres de données associées de type {@link List}
 * de type{@link String}</li>
 * </ul>
 * key:
 * 
 * 
 */
@Deprecated
public class ActionsUnitaires extends HashMap<String, List<String>> {

   private static final long serialVersionUID = 1L;

   /**
    * Stocke un périmètre de données à une action unitaire<br>
    * Si l'action n'est pas stocké alors elle est créée.
    * 
    * @param actionUnitaire
    *           action unitaire associé au périmètre de données
    * @param perimetreDonnees
    *           périmètre de données à stocker
    */
   public final void addAction(String actionUnitaire, String perimetreDonnees) {

      if (!this.containsKey(actionUnitaire)) {
         this.put(actionUnitaire, new ArrayList<String>());
      }

      this.get(actionUnitaire).add(perimetreDonnees);

   }

   /**
    * Renvoie la liste des actions unitaires, sans les périmètres de données
    * 
    * @return Les actions unitaires
    */
   @SuppressWarnings("unchecked")
   public final List<String> getListeActionsUniquement() {

      return IteratorUtils.toList(this.keySet().iterator());
   }

   /**
    * Renvoie les périmètres de données associés à une action unitaire
    * 
    * @param actionUnitaire
    *           L'action unitaire
    * @return Les périmètres de données associés à l'action unitaire
    */
   public final List<String> getPerimetresDonnees(String actionUnitaire) {

      Assert.notNull(actionUnitaire);

      return this.get(actionUnitaire);
   }

}
