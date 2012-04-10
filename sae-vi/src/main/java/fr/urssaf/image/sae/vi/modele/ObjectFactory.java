package fr.urssaf.image.sae.vi.modele;

/**
 * Classe d'instanciation des classes modèles contenues dans le package modele<br>
 * <br>
 * Les modèles instanciés sont:<br>
 * <ul>
 * <li>{@link DroitApplicatif}</li>
 * </ul>
 * 
 * 
 */
public final class ObjectFactory {

   private ObjectFactory() {

   }
   
   /**
    * Méthode d'instanciation de {@link DroitApplicatif}
    * 
    * @return {@link DroitApplicatif}
    */
   public static DroitApplicatif createDroitAplicatif() {
      return new DroitApplicatif();
   }
}
