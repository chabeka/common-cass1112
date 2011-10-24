package fr.urssaf.image.sae.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import fr.urssaf.image.sae.model.SAEQueryData;

/**
 * Fournit des méthodes de désérialisation
 * 
 * @author rhofir
 */
public interface XmlQueryDataService {
   /**
    * @param xmlInputStream
    *           : Le flux xml contenant les données
    * @return L'ensemble des requêtes de test unitaire.
    * @throws FileNotFoundException
    *            Lorsque le fichier n'est pas présent
    */
   Map<String, SAEQueryData> saeQueriesReader(final InputStream xmlInputStream)
         throws FileNotFoundException;

}
