/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.List;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;

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
    * @throws CassandraException
    *            exception levée en cas d'erreur d'accès aux données
    */
   List<String> getCodesOrganismes() throws CassandraException;

}
