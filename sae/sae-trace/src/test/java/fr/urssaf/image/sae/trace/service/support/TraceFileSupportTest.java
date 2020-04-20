package fr.urssaf.image.sae.trace.service.support;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.UUID;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.utils.StaxUtils;

/**
 * TU de la classe TraceFileSupport
 */
public class TraceFileSupportTest {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TraceFileSupportTest.class);

  @Test
  public void ecrireTrace_ContratServiceNull_success()
      throws XMLStreamException {

    // Création de l'objet TraceToCreate sans setter le contrat de service
    final TraceToCreate traceToCreate = new TraceToCreate();
    traceToCreate.setCodeEvt("CODE_EVT");
    traceToCreate.setContexte("contexte");

    // Création de l'objet TraceJournalEvt à partir de l'objet TraceToCreate
    final TraceJournalEvt traceJournalEvt = new TraceJournalEvt(traceToCreate,
                                                                null, UUID.randomUUID(), new Date());

    // Vérif: On s'attend déjà à ce que le contrat de service soit null
    // Sinon le test n'a plus lieu d'être
    Assert.assertNull("Le contrat de service devrait être null",
                      traceJournalEvt.getContratService());

    // Ecriture de la trace. Elle ne doit provoquer d'erreur
    final TraceFileSupport traceFileSupport = new TraceFileSupport();
    final StaxUtils staxUtils = createStaxUtils();
    traceFileSupport.ecrireTrace(staxUtils, traceJournalEvt);

  }

  private StaxUtils createStaxUtils() {

    final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    LOGGER.debug("Classe du XMLEventFactory: {}", eventFactory.getClass()
                 .getName());

    final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    LOGGER.debug("Classe du XMLOutputFactory: {}", outputFactory.getClass()
                 .getName());

    final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
    XMLEventWriter writer;
    try {
      writer = outputFactory.createXMLEventWriter(baos, "UTF-8");
      LOGGER.debug("Classe du XMLEventWriter: {}", writer.getClass()
                   .getName());

    } catch (final XMLStreamException e) {
      throw new RuntimeException(e);
    }

    final StaxUtils staxUtils = new StaxUtils(eventFactory, writer);

    return staxUtils;

  }

}
