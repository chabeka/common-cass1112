package fr.urssaf.image.parser_opencsv.application.service;

import java.io.IOException;

import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceException;
import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceFormatException;
import fr.urssaf.image.parser_opencsv.application.exception.CountNbrePageFileException;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;

/**
 * Service permettant de faire des correspondances entre les méta
 * SSTI et celles d'accoss
 */
public interface ICorrespondanceService {

   DocumentType applyCorrespondance(DocumentType documentType) throws CorrespondanceException, CorrespondanceFormatException;

   void calculateNbPages(final String fileAbsolutePath, final DocumentType documentType, final boolean activateRTF)
         throws CountNbrePageFileException, IOException;

}
