package fr.urssaf.image.parser_opencsv.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.parser_opencsv.application.service.ICaptureMasseService;
import fr.urssaf.image.parser_opencsv.webservice.model.ArchivageMasseAvecHashRequestType;
import fr.urssaf.image.parser_opencsv.webservice.model.ArchivageMasseAvecHashResponseType;
import fr.urssaf.image.parser_opencsv.webservice.model.ArchivageMasseRequestType;
import fr.urssaf.image.parser_opencsv.webservice.model.SaeServicePortType;
import fr.urssaf.image.parser_opencsv.webservice.model.SuppressionMasseRequestType;
import fr.urssaf.image.parser_opencsv.webservice.model.SuppressionMasseResponseType;

/**
 * Implémentation du Service de lancement de la capture de masse
 */
@Service
public class CaptureMasseServiceImpl implements ICaptureMasseService {

   private static final String TYPE_HASH = "SHA-1";

   @Autowired
   private SaeServicePortType port;

   // @Autowired
   // private JobQueueService jobQueueService;

   @Override
   public void lancerCaptureMasseSansHash(final String urlEcde) {
      final ArchivageMasseRequestType archivageMasseRequestType = new ArchivageMasseRequestType();
      archivageMasseRequestType.setUrlSommaire(urlEcde);

      port.archivageMasse(archivageMasseRequestType);
   }

   /**
    * Creation d'une capture de masse dans la pile des travaux
    * 
    * @param urlEcde
    * @param hash
    * @return
    */
   @Override
   public String lancerCaptureMasseAvecHash(final String urlEcde, final String hash) {
      final ArchivageMasseAvecHashRequestType archivageMasseRequestType = new ArchivageMasseAvecHashRequestType();

      archivageMasseRequestType.setUrlSommaire(urlEcde);
      archivageMasseRequestType.setTypeHash(TYPE_HASH);
      archivageMasseRequestType.setHash(hash);
      final ArchivageMasseAvecHashResponseType archivageMasseAvecHashResponseType = port.archivageMasseAvecHash(archivageMasseRequestType);

      return archivageMasseAvecHashResponseType.getUuid();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String lancerSuppressionMasse(final String luceneRequest) {
      final SuppressionMasseRequestType suppressionMasseRequestType = new SuppressionMasseRequestType();
      suppressionMasseRequestType.setRequete(luceneRequest);

      final SuppressionMasseResponseType suppressionMasseResponseType = port.suppressionMasse(suppressionMasseRequestType);

      return suppressionMasseResponseType.getUuid();
   }

}
