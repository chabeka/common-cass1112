package fr.urssaf.image.sae.pile.travaux.ihmweb.modele;

/**
 * Enumération des différents états d'un traitement. <br>
 * <ul>
 * <li>{@link #CREATED} : en attente de prise en charge</li>
 * <li>{@link #RESERVED} : réservé</li>
 * <li>{@link #STARTING} : en cours d'exécution</li>
 * <li>{@link #SUCCESS} : terminé avec succès</li>
 * <li>{@link #FAILURE} : terminé avec échec</li>
 * </ul>
 * 
 * 
 */
public enum JobState {

   CREATED, RESERVED, STARTING, SUCCESS, FAILURE;

}
