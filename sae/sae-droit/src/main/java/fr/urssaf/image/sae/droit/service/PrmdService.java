/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.List;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;

/**
 * Service pour l'utilisation des PRMD
 * 
 */
public interface PrmdService {

   /**
    * Vérifie que les métadonnées appartiennent bien à l'un des PRMD
    * 
    * @param metadatas
    *           Liste des métadonnées
    * @param prmds
    *           Liste des prmd
    * @return Renvoie vrai si les métadonnées appartiennent bien à l'un des PRMD
    */
   boolean isPermitted(List<UntypedMetadata> metadatas, List<SaePrmd> prmds);

   
   /**
    * Construit une requête Lucène en prenant en compte une liste de PRMD
    * 
    * @param lucene
    *           requête LUCENE d'origine
    * @param prmds
    *           Liste des PRMD
    * @return Nouvelle requête Lucene
    */
   String createLucene(String lucene, List<SaePrmd> prmds);

}
