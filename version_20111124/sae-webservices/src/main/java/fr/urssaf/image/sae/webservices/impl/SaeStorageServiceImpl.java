package fr.urssaf.image.sae.webservices.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.webservices.SaeStorageService;
import fr.urssaf.image.sae.webservices.impl.factory.ObjectStorageResponseFactory;

/**
 * Implémentation de {@link SaeStorageService}<br>
 * La classe est un singleton de type {@link Service} esta ccessible avec
 * {@link org.springframework.beans.factory.annotation.Autowired}<br>
 * Si aucun bean de type
 * {@link fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService}
 * n'est instancié alors une Exception sera levée par Spring
 * 
 * 
 */
@Service
public class SaeStorageServiceImpl implements SaeStorageService {

   /**
    * {@inheritDoc}
    */
   @Override
   public final ArchivageMasseResponse bulkCapture(ArchivageMasse request) {

      ArchivageMasseResponse response = ObjectStorageResponseFactory
            .createArchivageMasseResponse();

      // TODO implémenter l'archivage de masse

      return response;
   }

   /**
    * {@inheritDoc}
    */

   @Override
   public final RechercheResponse search(Recherche request) {

      RechercheResponse response;

      List<StorageDocument> storageDocuments = new ArrayList<StorageDocument>();

      storageDocuments.add(createStorageMetadata1());
      storageDocuments.add(createStorageMetadata2());
      storageDocuments.add(createStorageMetadata3());

      response = ObjectStorageResponseFactory.createRechercheResponse(
            storageDocuments, false);

      return response;
   }

   private static final String CODE_RND_META = "CodeRND";

   private static final String COTISANT_META = "NumeroCotisant";

   private static final String SIRET_META = "Siret";

   private static final String COMPTE_META = "DenominationCompte";

   private static final String ORGANISME_META = "CodeOrganisme";

   private StorageDocument createStorageMetadata1() {

      StorageDocument doc = new StorageDocument();
      doc.setUuid(UUID.fromString("110E8400-E29B-11D4-A716-446655440000"));

      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      metadatas.add(new StorageMetadata(CODE_RND_META, "3.1.3.1.1"));
      metadatas.add(new StorageMetadata(COTISANT_META, "704815"));
      metadatas.add(new StorageMetadata(SIRET_META, "49980055500017"));
      metadatas.add(new StorageMetadata(COMPTE_META, "SPOHN ERWAN MARIE MAX"));
      metadatas.add(new StorageMetadata(ORGANISME_META, "UR030"));

      doc.setMetadatas(metadatas);

      return doc;
   }

   private StorageDocument createStorageMetadata2() {

      StorageDocument doc = new StorageDocument();
      doc.setUuid(UUID.fromString("510E8200-E29B-18C4-A716-446677440120"));

      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      metadatas.add(new StorageMetadata(CODE_RND_META, "1.A.X.X.X"));
      metadatas.add(new StorageMetadata(COTISANT_META, "723804"));
      metadatas.add(new StorageMetadata(SIRET_META, "07413151710009"));
      metadatas.add(new StorageMetadata(COMPTE_META, "CHEVENIER ANDRE"));
      metadatas.add(new StorageMetadata(ORGANISME_META, "UR030"));

      doc.setMetadatas(metadatas);

      return doc;

   }

   private StorageDocument createStorageMetadata3() {

      StorageDocument doc = new StorageDocument();
      doc.setUuid(UUID.fromString("48758200-A29B-18C4-B616-455677840120"));

      List<StorageMetadata> metadatas = new ArrayList<StorageMetadata>();
      metadatas.add(new StorageMetadata(CODE_RND_META, "1.2.3.3.1"));
      metadatas.add(new StorageMetadata(COTISANT_META, "719900"));
      metadatas.add(new StorageMetadata(SIRET_META, "07412723410007"));
      metadatas.add(new StorageMetadata(COMPTE_META, "COUTURIER GINETTE"));
      metadatas.add(new StorageMetadata(ORGANISME_META, "UR030"));

      doc.setMetadatas(metadatas);

      return doc;

   }

}
