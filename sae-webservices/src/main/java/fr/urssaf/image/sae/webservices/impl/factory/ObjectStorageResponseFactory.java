package fr.urssaf.image.sae.webservices.impl.factory;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponseType;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.ListeResultatRechercheType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RechercheResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.impl.helper.ObjectStorageHelper;

/**
 * Classe d'instanciation des réponses pour l'implémentation
 * {@link fr.urssaf.image.sae.webservices.impl.SaeStorageServiceImpl}
 * 
 * 
 */
public final class ObjectStorageResponseFactory {

   private ObjectStorageResponseFactory() {

   }

   /**
    * instanciation de {@link RechercheResponse}.<br>
    * Implementation de {@link RechercheResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="rechercheResponseType">
    *    &lt;xsd:sequence>
    *       &lt;xsd:element name="resultats" type="sae:listeResultatRechercheType">
    *       ...       
    *       &lt;/xsd:element>
    *       &lt;xsd:element name="resultatTronque" type="xsd:boolean">
    *       ...
    *       &lt;/xsd:element>
    *    &lt;/xsd:sequence>
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @param storageDocuments
    *           valeur de <code>listeResultatRechercheType</code>
    * @param resultatTronque
    *           valeur de <code>resultatTronque</code>
    * @return instance de {@link RechercheResponse}
    */
   public static RechercheResponse createRechercheResponse(
         List<StorageDocument> storageDocuments, boolean resultatTronque) {

      RechercheResponse response = new RechercheResponse();
      RechercheResponseType responseType = new RechercheResponseType();
      response.setRechercheResponse(responseType);

      ListeResultatRechercheType resultatsType = new ListeResultatRechercheType();

      if (CollectionUtils.isNotEmpty(storageDocuments)) {

         for (StorageDocument storageDocument : storageDocuments) {

            ResultatRechercheType resultatRecherche = ObjectTypeFactory
                  .createResultatRechercheType();

            resultatRecherche.setIdArchive(ObjectTypeFactory
                  .createUuidType(storageDocument.getUuid()));
            ListeMetadonneeType listeMetadonnee = ObjectTypeFactory
                  .createListeMetadonneeType();

            List<MetadonneeType> metadonnees = ObjectStorageHelper
                  .createListMetadonneeType(storageDocument);

            if (CollectionUtils.isNotEmpty(metadonnees)) {

               for (MetadonneeType metaDonnee : metadonnees) {

                  listeMetadonnee.addMetadonnee(metaDonnee);
               }

            }

            resultatRecherche.setMetadonnees(listeMetadonnee);
            resultatsType.addResultat(resultatRecherche);
         }
      }

      responseType.setResultats(resultatsType);
      responseType.setResultatTronque(resultatTronque);

      return response;

   }

   /**
    * instanciation de {@link ArchivageMasseResponse}.<br>
    * implémentation de {@link ArchivageMasseResponseType}
    * 
    * <pre>
    * &lt;xsd:complexType name="archivageMasseResponseType">
    * ...
    * &lt;/xsd:complexType>
    * </pre>
    * 
    * @return instance de {@link ArchivageMasseResponse}
    */
   public static ArchivageMasseResponse createArchivageMasseResponse() {

      ArchivageMasseResponse response = new ArchivageMasseResponse();
      ArchivageMasseResponseType responseType = new ArchivageMasseResponseType();
      response.setArchivageMasseResponse(responseType);
      return response;
   }

}
