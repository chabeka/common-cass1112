package fr.urssaf.image.parser_opencsv.application.service.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.javers.common.collections.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.parser_opencsv.application.service.IValidatorService;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;
import fr.urssaf.image.parser_opencsv.jaxb.model.MetadonneeType;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;

/**
 * Implementation du service de validation des méta extrait du CSV
 */
@Service
public class ValidatorServiceImpl implements IValidatorService {

   private final List<MetadataReference> metadatas;

   @Autowired
   public ValidatorServiceImpl(final SaeMetaDataService metadataService) {
      metadatas = metadataService.findAll();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean validateRequireMetadatas(final DocumentType documentType) {

      // Récupère la liste des metadonnées requise au stockage
      final List<MetadataReference> requiredMetas = metadatas.stream()
            .filter(requiredMetaPredicate())
            .collect(Collectors.toList());

      // Récupère la Liste des codes long des entités MetadatReference
      final List<String> requiredMetasCodeLongListe = requiredMetas.stream()
            .map(MetadataReference::getLongCode)
            .collect(Collectors.toList());

      // Récupère la liste des métadonnées obligatoires qui sont remplis automatiquementla ged ou dfce
      final String[] metasAutomatiquementRenseignees = {"DocumentVirtuel", 
                                                        "ContratDeService", 
                                                        "DateFinConservation", 
                                                        "DateArchivage",
                                                        "VersionRND"
      };
      // Retirer ces meta
      requiredMetasCodeLongListe.removeAll(Arrays.asList(metasAutomatiquementRenseignees));

      final List<MetadonneeType> metadonnees = documentType.getMetadonnees().getMetadonnee();
      final List<String> documentMetasCodeLongListe = metadonnees.stream()
            .map(MetadonneeType::getCode)
            .collect(Collectors.toList());

      return documentMetasCodeLongListe.containsAll(requiredMetasCodeLongListe);
   }

   /**
    * Condition Predicat pur les métadonnées requises au stockage
    * 
    * @return
    */
   private Predicate<MetadataReference> requiredMetaPredicate() {
      return MetadataReference::isRequiredForStorage;
   }

}
