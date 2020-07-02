package fr.urssaf.image.parser_opencsv.application.service;

import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;

public interface IValidatorService {

   boolean validateRequireMetadatas(DocumentType documentType);

}
