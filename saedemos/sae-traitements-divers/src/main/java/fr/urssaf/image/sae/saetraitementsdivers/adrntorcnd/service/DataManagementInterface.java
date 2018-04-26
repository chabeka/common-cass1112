/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.service;

import java.util.List;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.exception.AdrnToRcndException;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;

/**
 * Stockage des données
 * 
 */
public interface DataManagementInterface {

   /**
    * Sauvegarde dans un fichier XML la liste des documents supportés
    * 
    * @param typesDocuments
    *           les documents à sauvegarder
    * @param version
    *           version de compatibilité
    * @throws AdrnToRcndException
    *            exception lors du traitement
    */
   void saveDocuments(List<BeanRNDTypeDocument> listDoc, String version)
         throws AdrnToRcndException;

   /**
    * Sauvegarde dans un fichier XML la liste des durées de conservation par
    * type de document
    * 
    * @param listDoc
    *           la liste des types de documents à traiter
    * @param version
    *           la version de compatibilité
    * @throws AdrnToRcndException
    *            exception levée lors du traitement
    */
   void saveLiveCycle(List<BeanRNDTypeDocument> listDoc, String version)
         throws AdrnToRcndException;
}
