package fr.urssaf.image.sae.services.enrichment.xml.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import fr.urssaf.image.sae.services.enrichment.xml.model.TypeDocument;

/**
 *Service pour l'opération : Désérialisation du réferentiel RCND.
 * 
 * @author rhofir
 * 
 */
public interface XmlRndDataService {
   /**
    * 
    * @param xmlInputStream
    *           : Le flux xml contenant les données
    * @return Le Référentiel des codes RND. <br>
    *         <ul>
    *         <li>Clef : code RND</li><br>
    *         <li>Valeur : objet de type {@link TypeDocument}</li>
    *         <ul>
    * 
    * @throws FileNotFoundException
    *            Lorsque le fichier n'est pas présent
    */
   Map<String, TypeDocument> rndReferenceReader(final InputStream xmlInputStream)
         throws FileNotFoundException;

}
