package fr.urssaf.image.sae.ecde.service;

import java.io.IOException;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;

/**
 * Service permettant de persister les objets Resultats dans un fichier resultats.xml
 * 
 * A partir d'un objet Resultat, le service génére le fichier resultats.xml que le client<br>
 * du SAE récupérera pour savoir s'il y a ey des documents en erreur.
 * <br>Pour avertir le client que le fichier resultats.xml est prêt, le service génére
 * un fichier témoin fin_traitement.flag.
 * 
 */
public interface ResultatService {

   /**
    * Methodes permettant de persister les objets Resultats dans un fichier resultats.xml
    * 
    * @param resultats : objet a persister
    * @throws IOException erreur lors de la creation du fichier flag
    * @throws EcdeXsdException non respect du format XSD
    */
   void persistResultat(Resultats resultats) throws EcdeXsdException, IOException;
   
}
