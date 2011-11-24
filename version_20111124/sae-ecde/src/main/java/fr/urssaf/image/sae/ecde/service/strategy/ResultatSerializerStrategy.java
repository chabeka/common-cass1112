package fr.urssaf.image.sae.ecde.service.strategy;

import java.io.IOException;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;

/**
 * 
 * Strategy utilisée pour le service ResultatService
 *
 */
public interface ResultatSerializerStrategy {

   /**
    * Methode permettant de serializé les attributs d'un Bussiness Object en fichier<br>
    * BO->JAXB->XML 
    * 
    * @param resultat objet BO resultat
    * @throws EcdeXsdException exception du au non respect du fichier XSD
    * @throws IOException lors de la création du fichier flag
    * 
    */
   void serializeResultat(Resultats resultat) throws EcdeXsdException, IOException;
}
