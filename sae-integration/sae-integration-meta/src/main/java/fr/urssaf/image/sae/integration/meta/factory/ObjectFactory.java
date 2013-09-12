package fr.urssaf.image.sae.integration.meta.factory;

import fr.urssaf.image.sae.integration.meta.modele.xml.MetadonneeType;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Factory d'objets pour passer de la structure XML JaxB à la structure d'objets
 * de modèle
 */
public final class ObjectFactory {

   private ObjectFactory() {
      // constructeur non accessible
   }

   public static MetadataReference create(MetadonneeType metaSource) {

      MetadataReference metaDest = new MetadataReference();

      metaDest.setArchivable(metaSource.isSpecifiableArchivage());
      metaDest.setConsultable(metaSource.isConsultable());
      metaDest.setDefaultConsultable(metaSource.isConsulteeParDefaut());
      metaDest.setDescription(metaSource.getDescription());
      metaDest.setInternal(metaSource.isGereeParDfce());
      metaDest.setLabel(metaSource.getLibelle());
      metaDest.setLength(metaSource.getTailleMax().intValue());
      metaDest.setLongCode(metaSource.getCodeLong());
      metaDest.setPattern(metaSource.getFormatage());
      metaDest.setRequiredForArchival(metaSource.isObligatoireArchivage());
      metaDest.setRequiredForStorage(metaSource.isObligatoireStockage());
      metaDest.setSearchable(metaSource.isRecherchable());
      metaDest.setShortCode(metaSource.getCodeCourt());
      metaDest.setType(metaSource.getTypeDfce());
      metaDest.setHasDictionary(metaSource.isAUnDico());
      metaDest.setDictionaryName(metaSource.getNomDico());
      metaDest.setIsIndexed(metaSource.isEstIndexee());
      metaDest.setModifiable(metaSource.isModifiable());

      return metaDest;

   }

}
