package fr.urssaf.image.parser_opencsv.application.writer;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.parser_opencsv.application.model.Statistic;

/**
 * Writer des statistiques concernant la transformation du fichier CSV
 * en sommaire.xml
 */
public class XMLStatisticWriter {

   private static final String STAT_FILE_NAME = "statistique.xml";

   private static final Logger LOGGER = LoggerFactory.getLogger(XMLStatisticWriter.class);

   /**
    * 
    */
   public XMLStatisticWriter() {
      super();
   }

   /**
    * Ecrit les statistiques dans le fichier XML
    * 
    * @param stat
    * @param directory
    */
   public void write(final Statistic stat, final String directory) {
      final File file = new File(directory + STAT_FILE_NAME);
      try {
         final JAXBContext jaxbContext = JAXBContext.newInstance(Statistic.class);
         final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
         jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

         jaxbMarshaller.marshal(stat, file);
      }
      catch (final JAXBException e) {
         final String message = "Erreur lors de l'écriture des statistiques concernant le parsing du CSV";
         LOGGER.error(message);
         throw new RuntimeException(message, e);
      }

   }

}
