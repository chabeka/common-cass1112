/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.List;

/**
 * Interface fournissant les services concernant la famille de colonne
 * <b>DocInfo</b>
 * 
 */
public interface DocInfoService {

   /**
    * Retourne la liste dédoublonnée des codes organismes présents dans les
    * métadonnées des documents
    * 
    * @return la liste des organismes
    */
   List<String> getCodesOrganismes();

}
