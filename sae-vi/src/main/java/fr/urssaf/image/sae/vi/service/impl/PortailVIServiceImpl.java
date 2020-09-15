package fr.urssaf.image.sae.vi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.vi.configuration.VIConfiguration;
import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.VIPortailContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VIPortailCreateParams;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.ObjectFactory;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.PagmsType;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.ViType;
import fr.urssaf.image.sae.vi.service.PortailVIService;

/**
 * interface de {@link PortailVIService}
 */
@Component
public final class PortailVIServiceImpl implements PortailVIService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(PortailVIServiceImpl.class);

  private static final Schema SCHEMA;
  static {
    // Construction de l'objet Schema permettant la validation XSD des VI
    // générés dans la méthode creerVIpourPortailAPortail
    final SchemaFactory schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try {
      SCHEMA = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(VIConfiguration.path()));
    } catch (final SAXException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String creerVI(final VIPortailCreateParams viParams) throws VIException {

    // TODO Vérification par AOP des paramètres d'entrée de la méthode

    // Utilisation de Jaxb pour produire le VI qui est au format XML

    // 1) Construction de la factory d'objet Jaxb
    // requise pour les opérations ultérieures
    final ObjectFactory factory = new ObjectFactory();

    // 2) Construction de l'objet Jaxb ViType avec tout son contenu
    final ViType viType = buildVi(factory, viParams);

    // 3) Sérialisation de l'objet Jaxb en XML, avec contrôle
    // du format avec le XSD
    final String viXml = marshalVi(factory, viType);
    return StringUtils.trim(viXml);

  }

  private ViType buildVi(final ObjectFactory factory, final VIPortailCreateParams viParams) {

    final ViType viType = factory.createViType();

    // audience
    viType.setAudience(viParams.getAudience());

    // issuer
    viType.setIssuer(viParams.getIssuer());

    // login
    viType.setLogin(viParams.getLogin());

    // nameID
    viType.setNameID(viParams.getNameId());

    // habilitationAnais
    viType.setHabilitationAnais(viParams.getHabAnais());

    // les PAGM
    final PagmsType pagmsType = factory.createPagmsType();
    viType.setPagms(pagmsType);
    final List<String> viTypePagms = pagmsType.getPagm();
    for (final String pagm : viParams.getPagmList()) {
      viTypePagms.add(pagm);
    }

    // fin
    return viType;

  }

  private String marshalVi(final ObjectFactory factory, final ViType viType)
      throws VIException {

    final VIValidationEventHandler eventHandler = new VIValidationEventHandler();

    try {
      final JAXBContext context = JAXBContext.newInstance(ViType.class);

      final Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      marshaller.setSchema(SCHEMA);

      marshaller.setEventHandler(eventHandler);
      final StringWriter writer = new StringWriter();

      marshaller.marshal(factory.createVi(viType), writer);

      eventHandler.validate();

      return writer.toString();
    } catch (final JAXBException e) {
      throw new IllegalArgumentException(e);
    }

  }

  private static class VIValidationEventHandler implements
  ValidationEventHandler {

    private final List<ValidationEvent> validationEvents = new ArrayList<>();

    @Override
    public boolean handleEvent(final ValidationEvent event) {
      validationEvents.add(event);
      return true;
    }

    private void validate() throws VIException {

      if (!validationEvents.isEmpty()) {
        LOGGER.debug("Erreur(s) de validation XSD : {}", validationEvents);
        throw new VIException(validationEvents);
      }

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VIPortailContenuExtrait lireVI(final String viXml) throws VIException {

    // TODO Vérification par AOP des paramètres d'entrée de la méthode

    // Utilisation de Jaxb pour désérialiser le xml
    final ViType viType = unmarshalVi(viXml);

    // Mapping ViType vers VIPortailContenuExtrait
    final VIPortailContenuExtrait viData = new VIPortailContenuExtrait();
    viData.setAudience(viType.getAudience());
    viData.setIssuer(viType.getIssuer());
    viData.setLogin(viType.getLogin());
    viData.setNameId(viType.getNameID());
    for (final String pagm : viType.getPagms().getPagm()) {
      viData.getPagmList().add(pagm);
    }
    viData.setHabAnais(viType.getHabilitationAnais());

    // Renvoie du résultat de la méthode
    return viData;

  }

  private ViType unmarshalVi(final String xml) throws VIException {

    final VIValidationEventHandler eventHandler = new VIValidationEventHandler();
    try {

      final JAXBContext context = JAXBContext.newInstance(ViType.class);
      final Unmarshaller unmarshaller = context.createUnmarshaller();

      unmarshaller.setEventHandler(eventHandler);
      unmarshaller.setSchema(SCHEMA);

      final InputStream input = new ByteArrayInputStream(xml.getBytes());

      final Reader reader = new InputStreamReader(input,"UTF-8");         
      final InputSource is = new InputSource(reader);
      is.setEncoding("UTF-8");

      try {

        final ViType viType = (ViType) unmarshaller.unmarshal(is);

        eventHandler.validate();

        return viType;

      } finally {
        input.close();
      }

    } catch (final IOException e) {
      throw new IllegalArgumentException(e);
    } catch (final UnmarshalException e) {
      throw new VIException(e);
    } catch (final JAXBException e) {
      throw new IllegalArgumentException(e);
    }

  }

}
