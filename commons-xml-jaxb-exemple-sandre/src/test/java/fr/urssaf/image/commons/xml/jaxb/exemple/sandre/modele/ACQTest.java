package fr.urssaf.image.commons.xml.jaxb.exemple.sandre.modele;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

@SuppressWarnings("PMD")
public class ACQTest {

   private static final String REPERTORY;

   static {
      REPERTORY = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
   }

   @Test
   public void marshall() throws JAXBException {

      ObjectFactory factory = new ObjectFactory();

      ACQ acq = factory.createACQ();

      // ACCUSE RECEPTION
      acq.accuseReception = factory.createACQAccuseReception();

      // ACCUSE RECEPTION:ACCEPTATION
      acq.accuseReception.acceptation = factory.createAcceptation();
      acq.accuseReception.acceptation.setValue("acceptation");

      // ACCUSE RECEPTION:CODE SCENARIO
      acq.accuseReception.codeScenario = factory.createCodeScenario();
      acq.accuseReception.codeScenario.setValue("code du scenario");

      // ACCUSE RECEPTION:DATE CREATION DU FICHIER
      acq.accuseReception.dateCreationFichier = factory
            .createDateCreationFichier();
      acq.accuseReception.dateCreationFichier
            .setValue("date creation du fichier");

      // ACCUSE RECEPTION:NOM SCENARIO
      acq.accuseReception.nomScenario = factory.createNomScenario();
      acq.accuseReception.nomScenario.setLanguage("FR");
      acq.accuseReception.nomScenario.setValue("nom du scenario");

      // SCENARIO
      acq.scenario = factory.createACQScenario();
      acq.scenario.codeScenario = factory.createCodeScenario();
      acq.scenario.codeScenario.setValue("code scenario");

      JAXBContext context = JAXBContext.newInstance(ACQ.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      marshaller.marshal(acq, System.out);

      File output = new File(FilenameUtils.concat(REPERTORY, "acq.xml"));
      marshaller.marshal(acq, output);

   }

   @Test
   public void unmarshall() throws JAXBException {

      JAXBContext context = JAXBContext.newInstance(ACQ.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      File input = new File("src/test/resources/acq.xml");

      ACQ acq = (ACQ) unmarshaller.unmarshal(input);

      assertEquals("acceptation", acq.accuseReception.acceptation.getValue());
      assertEquals("code du scenario", acq.accuseReception.codeScenario
            .getValue());
      assertEquals("date creation du fichier",
            acq.accuseReception.dateCreationFichier.getValue());
      assertEquals("nom du scenario", acq.accuseReception.nomScenario
            .getValue());
      assertEquals("FR", acq.accuseReception.nomScenario.getLanguage());
      assertEquals("code scenario", acq.scenario.codeScenario.getValue());

   }
}
