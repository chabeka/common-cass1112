package fr.urssaf.image.parser_opencsv.application.reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import fr.urssaf.image.parser_opencsv.application.model.Statistic;

public class ResultatsXMLReader {

   private XMLEventReader eventReader;

   public Statistic getResultats(final String path) throws IOException, XMLStreamException {

      final File file = new File(path + "resultats.xml");
      final XMLInputFactory factory = XMLInputFactory.newInstance();
      eventReader = factory.createXMLEventReader(new FileReader(file));

      final Statistic statistic = new Statistic();
      int i = 0;
      while (eventReader.hasNext()) {
         final XMLEvent xmlEvent = eventReader.nextEvent();
         if (xmlEvent.isStartElement()) {
            final StartElement startElement = xmlEvent.asStartElement();

            switch (startElement.getName().getLocalPart()) {
            case "initialDocumentsCount":
               final Characters initialCount = (Characters) eventReader.nextEvent();
               statistic.setInitialDocumentsCount(Integer.parseInt(initialCount.getData()));
               i++;
               break;

            case "integratedDocumentsCount":
               final Characters integrated = (Characters) eventReader.nextEvent();
               statistic.setAddedDocumentsCount(Integer.parseInt(integrated.getData()));
               i++;
               break;

            case "nonIntegratedDocumentsCount":
               final Characters nonIntegrated = (Characters) eventReader.nextEvent();
               statistic.setNonAddedDocumentsCount(Integer.parseInt(nonIntegrated.getData()));
               i++;
               break;
            default:
               break;
            }

            if (i == 3) {
               // Sortir de la boucle les stats on été récupérées
               break;
            }
         }
      }

      return statistic;
   }

   public void closeResultatStream() throws XMLStreamException {
      if (eventReader != null) {
         eventReader.close();
      }
   }

}
