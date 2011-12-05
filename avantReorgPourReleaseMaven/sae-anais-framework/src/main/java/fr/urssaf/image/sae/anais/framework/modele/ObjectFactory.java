package fr.urssaf.image.sae.anais.framework.modele;

/**
 * Classe d'instanciation des classes modèles contenues dans le package modele<br>
 * <br>
 * Les modèles instanciés sont:<br>
 * <ul>
 * <li>{@link SaeAnaisAdresseServeur}</li>
 * <li>{@link SaeAnaisProfilCompteApplicatif}</li>
 * <li>{@link SaeAnaisProfilServeur}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectFactory {

   private ObjectFactory() {

   }

   /**
    * Méthode d'instanciation de {@link SaeAnaisAdresseServeur}
    * 
    * @return {@link SaeAnaisAdresseServeur}
    */
   public static SaeAnaisAdresseServeur createSaeAnaisAdresseServeur() {
      return new SaeAnaisAdresseServeur();
   }

   /**
    * Méthode d'instanciation de {@link SaeAnaisProfilCompteApplicatif}
    * 
    * @return {@link SaeAnaisProfilCompteApplicatif}
    */
   public static SaeAnaisProfilCompteApplicatif createSaeAnaisProfilCompteApplicatif() {
      return new SaeAnaisProfilCompteApplicatif();
   }

   /**
    * Méthode d'instanciation de {@link SaeAnaisProfilServeur}
    * 
    * @return {@link SaeAnaisProfilServeur}
    */
   public static SaeAnaisProfilServeur createSaeAnaisProfilServeur() {
      return new SaeAnaisProfilServeur();
   }
}
