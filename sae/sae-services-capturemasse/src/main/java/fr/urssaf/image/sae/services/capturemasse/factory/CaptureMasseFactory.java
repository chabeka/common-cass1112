/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.factory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.DocumentType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.capturemasse.utils.SaeComposantsReader;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.VirtualStorageDocument;

/**
 * Classe de factory
 * 
 */
public class CaptureMasseFactory {

   @Autowired
   // @Qualifier("mappingService")
   private MappingDocumentService mappingService;

   @Autowired
   private Jaxb2Marshaller jaxb2UnMarshaller;

   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   @Autowired
   private ApplicationContext context;

   /**
    * création d'un itemProcessorAdapter
    * 
    * @return Objet permettant de réaliser la transformation d'un objet
    *         SAEDocument vers une objet StorageDocument
    */
   public final ItemProcessorAdapter<SAEDocument, StorageDocument> getItemProcessor() {
      final ItemProcessorAdapter<SAEDocument, StorageDocument> adapter = new ItemProcessorAdapter<SAEDocument, StorageDocument>();
      adapter.setTargetObject(mappingService);
      adapter.setTargetMethod("saeDocumentToStorageDocument");
      return adapter;
   }

   /**
    * création du reader de fichier sommaire XML
    * 
    * @param ecde
    *           URL du fichier sommaire
    * @return le Reader STAX du document sommaire XML
    * @throws EcdeBadURLException
    *            Exception si l'URL est erronée
    * @throws EcdeBadURLFormatException
    *            Exception si l'URL est mal formatée
    * @throws URISyntaxException
    *            Exception si l'URI est erronée
    * @throws CaptureMasseSommaireEcdeURLException
    *            URL sommaire erronée
    * @throws CaptureMasseSommaireFileNotFoundException
    *            le fichier sommaire n'est pas trouvé
    */
   public final StaxEventItemReader<DocumentType> getSommaireReader(
         final String ecde) throws EcdeBadURLException,
         EcdeBadURLFormatException, URISyntaxException,
         CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {
      final StaxEventItemReader<DocumentType> reader = new StaxEventItemReader<DocumentType>();
      reader.setFragmentRootElementName("document");
      reader.setUnmarshaller(jaxb2UnMarshaller);

      final URI ecdeURL = new URI(ecde);
      final File sommaire = fileSupport.convertURLtoFile(ecdeURL);

      final Resource resource = context.getResource("file:"
            + sommaire.getAbsolutePath());

      reader.setResource(resource);

      return reader;
   }

   /**
    * création du reader de fichier sommaire XML pour les documents virtuels
    * 
    * @param ecde
    *           URL du fichier sommaire
    * 
    * @return le Reader STAX du document sommaire XML
    * 
    * @throws EcdeBadURLException
    *            Exception si l'URL est erronée
    * @throws EcdeBadURLFormatException
    *            Exception si l'URL est mal formatée
    * @throws URISyntaxException
    *            Exception si l'URI est erronée
    * @throws CaptureMasseSommaireEcdeURLException
    *            URL sommaire erronée
    * @throws CaptureMasseSommaireFileNotFoundException
    *            le fichier sommaire n'est pas trouvé
    */
   public final SaeComposantsReader getComposantReader(final String ecde)
         throws EcdeBadURLException, EcdeBadURLFormatException,
         URISyntaxException, CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {

      final SaeComposantsReader reader = new SaeComposantsReader();
      reader.setFragmentRootElementName("composant");
      reader.setUnmarshaller(jaxb2UnMarshaller);
      reader.setIndexElementName("documentVirtuel");

      final URI ecdeURL = new URI(ecde);
      final File sommaire = fileSupport.convertURLtoFile(ecdeURL);

      final Resource resource = context.getResource("file:"
            + sommaire.getAbsolutePath());

      reader.setResource(resource);

      return reader;
   }

   /**
    * création du reader de fichier sommaire XML
    * 
    * @param ecde
    *           URL du fichier sommaire
    * @return le Reader STAX du document sommaire XML
    * @throws EcdeBadURLException
    *            Exception si l'URL est erronée
    * @throws EcdeBadURLFormatException
    *            Exception si l'URL est mal formatée
    * @throws URISyntaxException
    *            Exception si l'URI est erronée
    * @throws CaptureMasseSommaireEcdeURLException
    *            URL sommaire erronée
    * @throws CaptureMasseSommaireFileNotFoundException
    *            le fichier sommaire n'est pas trouvé
    */
   public final StaxEventItemReader<FichierType> getObjetNumeriqueReader(
         final String ecde) throws EcdeBadURLException,
         EcdeBadURLFormatException, URISyntaxException,
         CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException {
      final StaxEventItemReader<FichierType> reader = new StaxEventItemReader<FichierType>();
      reader.setFragmentRootElementName("objetNumerique");
      reader.setUnmarshaller(jaxb2UnMarshaller);

      final URI ecdeURL = new URI(ecde);
      final File sommaire = fileSupport.convertURLtoFile(ecdeURL);

      final Resource resource = context.getResource("file:"
            + sommaire.getAbsolutePath());

      reader.setResource(resource);

      return reader;
   }

   /**
    * création d'un itemProcessorAdapter
    * 
    * @return Objet permettant de réaliser la transformation d'un objet
    *         SAEVirtualDocument vers une objet VirtualStorageDocument
    */
   public final ItemProcessorAdapter<SAEVirtualDocument, VirtualStorageDocument> getVirtualDocumentProcessor() {
      final ItemProcessorAdapter<SAEVirtualDocument, VirtualStorageDocument> adapter = new ItemProcessorAdapter<SAEVirtualDocument, VirtualStorageDocument>();
      adapter.setTargetObject(mappingService);
      adapter.setTargetMethod("saeVirtualDocumentToVirtualStorageDocument");
      return adapter;
   }
}
