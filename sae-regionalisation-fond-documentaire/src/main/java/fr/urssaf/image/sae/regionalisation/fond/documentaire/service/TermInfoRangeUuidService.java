/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.List;
import java.util.Map;

/**
 * Interface de service pour la famille de colonne <b>TermInfoRangeUuid</b>
 * 
 */
public interface TermInfoRangeUuidService {

   /**
    * @return la liste des uuids des documents ainsi que de leurs informations
    *         rattachées
    */
   List<Map<String, String>> getInfosDoc();

}
