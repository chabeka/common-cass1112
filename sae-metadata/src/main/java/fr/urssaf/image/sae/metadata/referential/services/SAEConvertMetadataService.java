/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services;

import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;

/**
 * Services permettant de convertir des codes de métadonnées selon le
 * référentiel des métadonnées
 * 
 */
public interface SAEConvertMetadataService {

   /**
    * Méthode permettant la conversion de code long en code court de métadonnées
    * 
    * @param listCodeMetadata
    *           liste des codes longs de métadonnées
    * @return map de paire clé / valeur avec codeCourt / codeLong
    * @throws LongCodeNotFoundException
    *            Levée lorsque le code long n'a pas été trouvé dans le
    *            référentiel des métadonnées
    */
   Map<String, String> longCodeToShortCode(List<String> listCodeMetadata)
         throws LongCodeNotFoundException;

}
