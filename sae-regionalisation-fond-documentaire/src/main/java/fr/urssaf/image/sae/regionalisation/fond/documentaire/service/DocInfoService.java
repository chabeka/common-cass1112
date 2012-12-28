/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.List;
import java.util.Map;

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
    * @return la clé contient un couple (code organisme; type métadonnée parmi
    *         cop ou cog), et comme valeur le nombre d'occurences. Exemple :
    *         clé=CER69;cop et valeur=150
    * @throws CassandraException
    *            exception levée en cas d'erreur d'accès aux données
    */
   Map<String, Long> getCodesOrganismes() throws CassandraException;

   /**
    * Retourne les informations rattachées aux documents existants
    * 
    * @return la liste des uuids des documents ainsi que de leurs informations
    *         rattachées exception levée en cas d'erreur d'accès aux données
    */
   List<Map<String, String>> getInfosDoc() throws CassandraException;

   /**
    * Retourne les informations rattachées aux documents existants
    * 
    * @param infos
    *           champs à retourner
    * 
    * @return la liste des informations demandées des documents
    */
   List<Map<String, String>> getInfosDoc(String... infos)
         throws CassandraException;
}
