/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

/**
 * Interface centralisant les traitements à effectuer
 * 
 */
public interface TraitementService {

   /**
    * récupère la liste des codes organismes présents dans CASSANDRA et les
    * liste dans un fichier
    * 
    * @param filePath
    *           chemin du fichier à créer
    */
   void writeCodesOrganismes(String filePath);

}
