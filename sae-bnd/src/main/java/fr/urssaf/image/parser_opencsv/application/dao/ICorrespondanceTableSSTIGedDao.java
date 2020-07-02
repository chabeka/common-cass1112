package fr.urssaf.image.parser_opencsv.application.dao;

import java.util.Map;

import fr.urssaf.image.parser_opencsv.application.model.CorrespondanceMetaObject;

/**
 * Interface dao pour la correspondance entre le CodeCaisse SSTI et
 * le CodeOrganismeProprietaire de la GED
 */
public interface ICorrespondanceTableSSTIGedDao {

   /**
    * Recupère tous les mapping entre Code Caisse SSTI et Code Organisme Proprietaire
    * 
    * @return
    */
   Map<String, CorrespondanceMetaObject> getAllCaisseCorresp();

   /**
    * Recupère tous les mapping entre Code SSTI et le CodeRND Ged
    * 
    * @return
    */
   Map<String, CorrespondanceMetaObject> getAllRNDCorresp();
}
