package fr.urssaf.image.sae.anais.framework.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Profil de connexion à ANAIS pour un code environnement donné
 * (Developpement/Validation/Production)<br>
 * <br>
 * L'instanciation est uniquement possible avec
 * {@link ObjectFactory#createSaeAnaisProfilServeur()}
 * 
 * @see ObjectFactory
 */
public class SaeAnaisProfilServeur {

   private SaeAnaisEnumCodesEnvironnement codeEnvironnement;

   private List<SaeAnaisAdresseServeur> serveurs = new ArrayList<SaeAnaisAdresseServeur>();

   @SuppressWarnings("PMD.UncommentedEmptyConstructor")
   protected SaeAnaisProfilServeur() {

   }

   /**
    * 
    * @return Le code de l'environnement (développement / validation /
    *         production)
    */
   public final SaeAnaisEnumCodesEnvironnement getCodeEnvironnement() {
      return codeEnvironnement;
   }

   /**
    * 
    * @param codeEnvironnement
    *           Le code de l'environnement (développement / validation /
    *           production)
    */
   public final void setCodeEnvironnement(
         SaeAnaisEnumCodesEnvironnement codeEnvironnement) {
      this.codeEnvironnement = codeEnvironnement;
   }

   /**
    * 
    * @return La liste des adresses des serveurs ANAIS
    */
   public final List<SaeAnaisAdresseServeur> getServeurs() {
      return serveurs;
   }

   /**
    * 
    * @param serveurs
    *           La liste des adresses des serveurs ANAIS
    */
   public final void setServeurs(List<SaeAnaisAdresseServeur> serveurs) {
      this.serveurs = serveurs;
   }

}
