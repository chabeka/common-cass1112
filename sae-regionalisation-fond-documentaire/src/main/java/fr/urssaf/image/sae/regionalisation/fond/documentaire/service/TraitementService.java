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

   /**
    * Met à jour les documents listés dans le fichier dans l'intervalle spécifié
    * 
    * @param inputFilePath
    *           fichier contenant la liste des documents à modifier
    * @param outputFilePath
    *           fichier contenant la liste des modifications effectuées
    * @param propertiesFilePath
    *           fichier contenant les correspondances
    * @param firstRecord
    *           index du premier enregistrement à traiter
    * @param lastRecord
    *           index du dernier enregistrement à traiter
    */
   void updateDocuments(String inputFilePath, String outputFilePath,
         String propertiesFilePath, int firstRecord, int lastRecord);

   /**
    * Liste et écrit dans un fichier les documents dont le numéro de compte
    * externe commence par un code organisme listé dans le fichier de properties
    * 
    * @param outputPath
    *           fichier de sortie
    * @param propertiesFilePath
    *           fichier de propriétés contenant les correspondances
    */
   void writeDocStartingWithCodeOrga(String outputPath,
         String propertiesFilePath);
}