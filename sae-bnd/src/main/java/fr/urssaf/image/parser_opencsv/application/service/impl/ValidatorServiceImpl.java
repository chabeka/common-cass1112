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
   private final List<String> requiredMetasCodeLongListe;

   @Autowired
   public ValidatorServiceImpl(final SaeMetaDataService metadataService) {
      metadatas = metadataService.findAll();

      // Récupère la liste des metadonnées requise au stockage
      final List<MetadataReference> requiredMetas = metadatas.stream()
            .filter(requiredMetaPredicate())
            .collect(Collectors.toList());

      // Récupère la Liste des codes long des entités MetadatReference
      requiredMetasCodeLongListe = requiredMetas.stream()
            .map(MetadataReference::getLongCode)
            .collect(Collectors.toList());

      // Récupère la liste des métadonnées obligatoires qui sont remplis automatiquement par la ged ou dfce
      final String[] metasAutomatiquementRenseignees = {"DocumentVirtuel", 
            "ContratDeService", 
            "DateFinConservation", 
            "DateArchivage",
            "VersionRND"
      };
      // Retirer ces meta
      requiredMetasCodeLongListe.removeAll(Arrays.asList(metasAutomatiquementRenseignees));

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> getMissingMetadatas(final DocumentType documentType) {

      final List<MetadonneeType> metadonnees = documentType.getMetadonnees().getMetadonnee();
      final List<String> metaCodes = metadonnees.stream().map(MetadonneeType::getCode).collect(Collectors.toList());

      final List<String> missingMetas = requiredMetasCodeLongListe.stream()
            .filter(code -> !metaCodes.contains(code))
            .collect(Collectors.toList());
      return missingMetas;
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
