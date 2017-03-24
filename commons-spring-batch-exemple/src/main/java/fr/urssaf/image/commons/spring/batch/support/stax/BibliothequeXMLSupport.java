package fr.urssaf.image.commons.spring.batch.support.stax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;

public class BibliothequeXMLSupport {

   private static final String B_NAMESPACE = "http://www.cirtil.fr/sae/common/bibliotheque";

   private static final String REF_NAMESPACE = "http://www.cirtil.fr/sae/common/bibliotheque_element";

   private static final String REF_PREFIX = "ref";

   private static final String B_PREFIX = "b";

   private final XMLEventFactory eventFactory;

   public BibliothequeXMLSupport() {
      eventFactory = XMLEventFactory.newInstance();
   }

   public void writer(File bibliothequeXMLPath, int nombreDeLivres) {

      OutputStream libraryXML;
      try {
         libraryXML = FileUtils.openOutputStream(bibliothequeXMLPath);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         XMLEventWriter writer = outputFactory.createXMLEventWriter(libraryXML);
         writer = new IndentingXMLEventWriter(writer);

         try {

            this.headerWriter(writer);

            for (int i = 1; i <= nombreDeLivres; i++) {

               Livre livre = new Livre();
               livre.setIdentifiant(i);
               livre.setAuteur("auteur_" + i);
               livre.setTitre("titre_" + i);

               this.itemWriter(livre, writer);

            }

            this.footerWriter(writer);

         } catch (XMLStreamException e) {
            throw new NestableRuntimeException(e);
         } finally {
            writer.close();

         }

      } catch (XMLStreamException e) {
         throw new NestableRuntimeException(e);
      }

   }

   public void writer(File output, File input) {

      OutputStream libraryXML;
      try {
         libraryXML = FileUtils.openOutputStream(output);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }

      InputStream inputStream;
      try {
         inputStream = FileUtils.openInputStream(input);
      } catch (IOException e) {
         throw new NestableRuntimeException(e);
      }

      XMLInputFactory inputFactory = XMLInputFactory.newInstance();

      XMLEventReader reader;
      try {
         reader = inputFactory.createXMLEventReader(inputStream);
      } catch (XMLStreamException e) {
         throw new NestableRuntimeException(e);
      }

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

      try {
         XMLEventWriter writer = outputFactory.createXMLEventWriter(libraryXML);

         try {

            this.headerWriter(writer);

            this.write(reader, writer);

            this.footerWriter(writer);

         } catch (XMLStreamException e) {

            throw new NestableRuntimeException(e);

         } finally {

            reader.close();
            writer.close();

         }

      } catch (XMLStreamException e) {
         throw new NestableRuntimeException(e);
      }

   }

   private void write(XMLEventReader reader, XMLEventWriter writer)
         throws XMLStreamException {

      while (reader.hasNext()) {

         XMLEvent next = reader.nextEvent();

         if (next.isStartElement()) {
            StartElement startElement = next.asStartElement();

            if ("livres".equals(startElement.getName().getLocalPart())) {

               break;
            }

         }

      }

      while (reader.hasNext()) {

         XMLEvent next = reader.nextEvent();

         writer.add(next);

         if (next.isEndElement()) {
            EndElement endElement = next.asEndElement();

            if ("livres".equals(endElement.getName().getLocalPart())) {

               break;
            }

         }

      }
   }

   private void headerWriter(XMLEventWriter writer) throws XMLStreamException {

      writer.add(eventFactory.createStartDocument("UTF-8", "1.0"));

      writer.add(eventFactory.createStartElement(B_PREFIX, B_NAMESPACE,
            "bibliotheque"));
      writer.add(eventFactory.createNamespace(B_PREFIX, B_NAMESPACE));
      writer.add(eventFactory.createNamespace(REF_PREFIX, REF_NAMESPACE));

      // balise adresse
      writer.add(eventFactory.createStartElement(B_PREFIX, B_NAMESPACE,
            "adresse"));
      writer.add(eventFactory.createCharacters("adresse de la bibliotheque"));
      writer.add(eventFactory
            .createEndElement(B_PREFIX, B_NAMESPACE, "adresse"));

      writer.add(eventFactory.createStartElement(B_PREFIX, B_NAMESPACE,
            "livres"));

   }

   private void footerWriter(XMLEventWriter writer) throws XMLStreamException {

      writer.add(eventFactory.createEndElement(B_PREFIX, B_NAMESPACE,
            "bibliotheque"));
      writer.add(eventFactory.createEndDocument());

   }

   private void itemWriter(Livre livre, XMLEventWriter writer)
         throws XMLStreamException {

      // ouverture de la balise livre
      writer.add(eventFactory.createStartElement(REF_PREFIX, REF_NAMESPACE,
            "livre"));
      writer.add(eventFactory.createAttribute("id", ObjectUtils.toString(livre
            .getIdentifiant(), "nc")));

      // balise titre
      writer.add(eventFactory.createStartElement(REF_PREFIX, REF_NAMESPACE,
            "titre"));
      writer.add(eventFactory.createCharacters(livre.getTitre()));
      writer.add(eventFactory.createEndElement(REF_PREFIX, REF_NAMESPACE,
            "titre"));

      // balise auteur
      writer.add(eventFactory.createStartElement(REF_PREFIX, REF_NAMESPACE,
            "auteur"));
      writer.add(eventFactory.createCharacters(livre.getAuteur()));
      writer.add(eventFactory.createEndElement(REF_PREFIX, REF_NAMESPACE,
            "auteur"));

      writer.add(eventFactory.createEndElement(REF_PREFIX, REF_NAMESPACE,
            "livre"));

   }
}
