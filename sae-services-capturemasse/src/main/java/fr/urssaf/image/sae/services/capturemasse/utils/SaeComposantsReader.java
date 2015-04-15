/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.utils;

import java.io.InputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxUtils;
import org.springframework.batch.item.xml.stax.DefaultFragmentEventReader;
import org.springframework.batch.item.xml.stax.FragmentEventReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import fr.urssaf.image.sae.services.capturemasse.model.SaeComposantVirtuelType;
import fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat.ComposantDocumentVirtuelType;

/**
 * Classe permettant de réaliser la lecture du fichier sommaire.xml dans le cas
 * où il s'agit de lire les documents virtuels
 * 
 */
public class SaeComposantsReader
      extends
      AbstractItemCountingItemStreamItemReader<JAXBElement<SaeComposantVirtuelType>>
      implements
      ResourceAwareItemReaderItemStream<JAXBElement<SaeComposantVirtuelType>>,
      InitializingBean {

   private static final Log LOGGER = LogFactory
         .getLog(SaeComposantsReader.class);

   private FragmentEventReader fragmentReader;

   private XMLEventReader eventReader;

   private Unmarshaller unmarshaller;

   private Resource resource;

   private InputStream inputStream;

   private String fragmentRootElementName;

   private boolean noInput;

   private boolean strict = true;

   private String fragmentRootElementNameSpace;

   private String indexElementName;

   private int indexCount = -1;

   /**
    * Constructeur
    */
   public SaeComposantsReader() {
      setName(ClassUtils.getShortName(StaxEventItemReader.class));
   }

   /**
    * In strict mode the reader will throw an exception on
    * {@link #open(org.springframework.batch.item.ExecutionContext)} if the
    * input resource does not exist.
    * 
    * @param strict
    *           false by default
    */
   public final void setStrict(boolean strict) {
      this.strict = strict;
   }

   /**
    * @param resource
    *           la resource à lire
    */
   public final void setResource(Resource resource) {
      this.resource = resource;
   }

   /**
    * @param unmarshaller
    *           maps xml fragments corresponding to records to objects
    */
   public final void setUnmarshaller(Unmarshaller unmarshaller) {
      this.unmarshaller = unmarshaller;
   }

   /**
    * @param fragmentRootElementName
    *           name of the root element of the fragment
    */
   public final void setFragmentRootElementName(String fragmentRootElementName) {
      this.fragmentRootElementName = fragmentRootElementName;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void afterPropertiesSet() throws Exception {
      Assert.notNull(unmarshaller, "The Unmarshaller must not be null.");
      Assert.hasLength(fragmentRootElementName,
            "The FragmentRootElementName must not be null");
      if (fragmentRootElementName.contains("{")) {
         fragmentRootElementNameSpace = fragmentRootElementName.replaceAll(
               "\\{(.*)\\}.*", "$1");
         fragmentRootElementName = fragmentRootElementName.replaceAll(
               "\\{.*\\}(.*)", "$1");
      }
   }

   /**
    * Responsible for moving the cursor before the StartElement of the fragment
    * root.
    * 
    * This implementation simply looks for the next corresponding element, it
    * does not care about element nesting. You will need to override this method
    * to correctly handle composite fragments.
    * 
    * @return <code>true</code> if next fragment was found, <code>false</code>
    *         otherwise.
    * 
    * @throws NonTransientResourceException
    *            if the cursor could not be moved. This will be treated as fatal
    *            and subsequent calls to read will return null.
    */
   protected final boolean moveCursorToNextFragment(XMLEventReader reader) {
      try {
         while (true) {
            while (reader.peek() != null && !reader.peek().isStartElement()) {
               reader.nextEvent();
            }
            if (reader.peek() == null) {
               return false;
            }
            QName startElementName = ((StartElement) reader.peek()).getName();

            if (startElementName.getLocalPart().equals(indexElementName)) {
               indexCount++;
            }

            if (startElementName.getLocalPart().equals(fragmentRootElementName)) {
               if (fragmentRootElementNameSpace == null
                     || startElementName.getNamespaceURI().equals(
                           fragmentRootElementNameSpace)) {
                  return true;
               }
            }
            reader.nextEvent();

         }
      } catch (XMLStreamException e) {
         throw new NonTransientResourceException(
               "Error while reading from event reader", e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doClose() throws Exception {
      try {
         if (fragmentReader != null) {
            fragmentReader.close();
         }
         if (inputStream != null) {
            inputStream.close();
         }
      } finally {
         fragmentReader = null;
         inputStream = null;
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doOpen() throws Exception {
      Assert.notNull(resource, "The Resource must not be null.");

      noInput = true;
      if (!resource.exists()) {
         if (strict) {
            throw new IllegalStateException(
                  "Input resource must exist (reader is in 'strict' mode)");
         }
         LOGGER.warn("Input resource does not exist "
               + resource.getDescription());
         return;
      }
      if (!resource.isReadable()) {
         if (strict) {
            throw new IllegalStateException(
                  "Input resource must be readable (reader is in 'strict' mode)");
         }
         LOGGER.warn("Input resource is not readable "
               + resource.getDescription());
         return;
      }

      inputStream = resource.getInputStream();
      eventReader = XMLInputFactory.newInstance().createXMLEventReader(
            inputStream);
      fragmentReader = new DefaultFragmentEventReader(eventReader);
      noInput = false;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final JAXBElement<SaeComposantVirtuelType> doRead()
         throws Exception {

      if (noInput) {
         return null;
      }

      SaeComposantVirtuelType item = null;
      JAXBElement<SaeComposantVirtuelType> jaxbItem = null;
      boolean success = false;
      try {
         success = moveCursorToNextFragment(fragmentReader);
      } catch (NonTransientResourceException e) {
         // Prevent caller from retrying indefinitely since this is fatal
         noInput = true;
         throw e;
      }
      if (success) {
         fragmentReader.markStartFragment();

         try {
            @SuppressWarnings("unchecked")
            JAXBElement<ComposantDocumentVirtuelType> mappedFragment = (JAXBElement<ComposantDocumentVirtuelType>) unmarshaller
                  .unmarshal(StaxUtils.getSource(fragmentReader));
            item = new SaeComposantVirtuelType();
            item.setMetadonnees(mappedFragment.getValue().getMetadonnees());
            item.setNombreDePages(mappedFragment.getValue().getNombreDePages());
            item.setNumeroPageDebut(mappedFragment.getValue()
                  .getNumeroPageDebut());
            item.setIndex(indexCount);

            jaxbItem = new JAXBElement<SaeComposantVirtuelType>(QName
                  .valueOf("somres"), SaeComposantVirtuelType.class, item);

         } finally {
            fragmentReader.markFragmentProcessed();
         }
      }

      return jaxbItem;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void jumpToItem(int itemIndex) throws Exception {
      for (int i = 0; i < itemIndex; i++) {
         readToStartFragment();
         readToEndFragment();
      }
   }

   /*
    * Read until the first StartElement tag that matches the provided
    * fragmentRootElementName. Because there may be any number of tags in
    * between where the reader is now and the fragment start, this is done in a
    * loop until the element type and name match.
    */
   private void readToStartFragment() throws XMLStreamException {
      while (true) {
         XMLEvent nextEvent = eventReader.nextEvent();
         if (nextEvent.isStartElement()
               && ((StartElement) nextEvent).getName().getLocalPart().equals(
                     fragmentRootElementName)) {
            return;
         }
      }
   }

   /*
    * Read until the first EndElement tag that matches the provided
    * fragmentRootElementName. Because there may be any number of tags in
    * between where the reader is now and the fragment end tag, this is done in
    * a loop until the element type and name match
    */
   private void readToEndFragment() throws XMLStreamException {
      while (true) {
         XMLEvent nextEvent = eventReader.nextEvent();
         if (nextEvent.isEndElement()
               && ((EndElement) nextEvent).getName().getLocalPart().equals(
                     fragmentRootElementName)) {
            return;
         }
      }
   }

   /**
    * @return l'index de l'élément dans lequel se trouve l'objet actuel
    */
   public final int getIndexCount() {
      return indexCount;
   }

   /**
    * @param indexElementName
    *           the indexElementName to set
    */
   public final void setIndexElementName(String indexElementName) {
      this.indexElementName = indexElementName;
   }
}
