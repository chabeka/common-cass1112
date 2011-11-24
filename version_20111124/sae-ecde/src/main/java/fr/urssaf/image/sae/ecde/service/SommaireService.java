package fr.urssaf.image.sae.ecde.service;

import java.net.URI;

import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;

/**
 * 
 * Service permettant de recuperer un objet Sommaire à partir d'un fichier sommaire.xml
 * 
 * A partir d'une URL ECDE d'un sommaire, le service renvoie un objet Sommaire contenant<br>
 * une collection d'objets métiers prêt à être archivés.
 *
 */
public interface SommaireService {

   /**
    * Service permettant de recuperer un objet Sommaire à partir d'un fichier sommaire.xml
    * 
    * @param uri : à fetcher
    * @return Sommaire : objet a retourner a partir du fichier 
    * @throws EcdeGeneralException erreur avec le fichier sommaire.xml
    */
   Sommaire fetchSommaireByUri(URI uri) throws EcdeGeneralException;
}
