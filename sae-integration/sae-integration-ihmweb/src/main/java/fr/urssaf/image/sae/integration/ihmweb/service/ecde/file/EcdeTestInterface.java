/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.service.ecde.file;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;

/**
 * Chargement et écriture du fichier contenant les cas de test
 * 
 */
public interface EcdeTestInterface {

   /**
    * Chargement du fichier de cas de tests défini par jndi
    * 
    * @return la liste des cas de test
    */
   EcdeTests load() throws Exception;

   /**
    * génération du fichier de cas de tests dans le fichier défini par jndi
    * 
    * @param ecdeTests
    *           liste des cas de test
    */
   void generateFile(EcdeTests ecdeTests);
}
