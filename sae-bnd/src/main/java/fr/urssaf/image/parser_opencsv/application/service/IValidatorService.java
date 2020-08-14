package fr.urssaf.image.parser_opencsv.application.service;

import java.util.List;

import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;

public interface IValidatorService {

   /**
    * Renvoie la listes des code des métadonnées manquantes
    * 
    * @param documentType
    * @return
    */
   List<String> getMissingMetadatas(DocumentType documentType);

}
