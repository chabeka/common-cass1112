package fr.urssaf.image.sae.webservices.impl.helper;

import java.util.ArrayList;
import java.util.List;

import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

/**
 * Classe utilitaire pour l'implémentation
 * {@link fr.urssaf.image.sae.webservices.impl.SaeStorageServiceImpl}
 * 
 * 
 * 
 */
public final class ObjectStorageHelper {

   private ObjectStorageHelper() {

   }

   /**
    * instanciation d'une liste de {@link MetadonneeType} à partir des
    * {@link StorageMetadata} contenu dans une instance de
    * {@link StorageDocument}
    * <ul>
    * <li>{@link MetadonneeType#setCode} : {@link StorageMetadata#getCode()}</li>
    * <li>{@link MetadonneeType#setValeur} : {@link StorageMetadata#getValue()}</li>
    * </ul>
    * 
    * @param storageDocument
    *           instance de {@link StorageDocument} doit être non null
    * @return collection d'instance de {@link MetadonneeType}
    */
   public static List<MetadonneeType> createListMetadonneeType(
         StorageDocument storageDocument) {

      List<MetadonneeType> metadonnees = new ArrayList<MetadonneeType>();

      for (StorageMetadata storageMetadata : CollectionUtils
            .loadListNotNull(storageDocument.getMetadatas())) {
         MetadonneeType metadonnee = new MetadonneeType();
         String code = storageMetadata.getShortCode();
         String valeur = storageMetadata.getValue().toString();
         metadonnee = ObjectTypeFactory.createMetadonneeType(code, valeur);
         metadonnees.add(metadonnee);
      }

      return metadonnees;
   }

}
