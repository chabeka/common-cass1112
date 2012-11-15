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

   /**
    * Liste et écrit dans un fichier les documents dont au moins un des codes
    * organismes est à renuméroter
    * 
    * @param outputPath
    *           chemin du fichier de sortie
    * @param propertiesFilePath
    *           chemin du fichier contenant les correspondances ancien_code =>
    *           nouveau_code
    */
   void writeDocUuidsToUpdate(String outputPath, String propertiesFilePath);
}
