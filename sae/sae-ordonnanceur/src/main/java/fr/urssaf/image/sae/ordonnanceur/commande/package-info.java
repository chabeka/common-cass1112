/**
 * ce package comporte tous les traitements qui peuvent être exécutés par l'ordonnanceur.<br>
 * Tous les traitements s'exécutent dans un {@link java.lang.Thread} propre.<br>
 * Les classes héritent de l'interface {@link java.util.concurrent.Callable} ou {@link java.lang.Runnable}.<br>
 * <br>
 * La méthode <code>run</code> ou <code>call</code> ne contient  en général que l'appel au service et non l'implémentation du service.<br>
 * Les services sont appelés à partir de l'instance {@link org.springframework.context.ApplicationContext} du contexte SPRING.
 */
package fr.urssaf.image.sae.ordonnanceur.commande;

