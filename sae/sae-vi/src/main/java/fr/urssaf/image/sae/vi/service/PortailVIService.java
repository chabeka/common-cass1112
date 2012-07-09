package fr.urssaf.image.sae.vi.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.vi.configuration.VIConfiguration;
import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.VIPortailContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VIPortailCreateParams;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.ObjectFactory;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.PagmsType;
import fr.urssaf.image.sae.vi.modele.jaxb.viportail.ViType;

/**
 * Classe de lecture et d'écriture du VI pour le mode portail à portail
 */
public final class PortailVIService {

   private static final Logger LOGGER = LoggerFactory.getLogger(PortailVIService.class);
   
   private final Schema schema;
   

   /**
    * Constructeur
    */
   public PortailVIService() {

      // Construction de l'objet Schema permettant la validation XSD des VI
      // générés dans la méthode creerVIpourPortailAPortail
      SchemaFactory schemaFactory = SchemaFactory
            .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      try {
         schema = schemaFactory.newSchema(this.getClass().getResource(
               VIConfiguration.path()));
      } catch (SAXException e) {
         throw new IllegalStateException(e);
      }

   }

   /**
    * Création d'un VI de type portail à portail
    * 
    * @param viParams
    *           les informations permettant de construire le VI
    * @return le VI sous la forme d'une chaîne de caractères (XML)
    * @throws VIException
    *            si un problème survient pendant la génération du VI
    */
   public String creerVI(VIPortailCreateParams viParams) throws VIException {

      // TODO Vérification par AOP des paramètres d'entrée de la méthode

      // Utilisation de Jaxb pour produire le VI qui est au format XML

      // 1) Construction de la factory d'objet Jaxb
      // requise pour les opérations ultérieures
      ObjectFactory factory = new ObjectFactory();

      // 2) Construction de l'objet Jaxb ViType avec tout son contenu
      ViType viType = buildVi(factory, viParams);

      // 3) Sérialisation de l'objet Jaxb en XML, avec contrôle
      // du format avec le XSD
      String viXml = marshalVi(factory, viType);
      return StringUtils.trim(viXml);

   }

   private ViType buildVi(ObjectFactory factory, VIPortailCreateParams viParams) {

      ViType viType = factory.createViType();

      // audience
      viType.setAudience(viParams.getAudience());

      // issuer
      viType.setIssuer(viParams.getIssuer());

      // nameID
      viType.setNameID(viParams.getNameId());
      
      // habilitationAnais
      viType.setHabilitationAnais(viParams.getHabAnais());

      // les PAGM
      PagmsType pagmsType = factory.createPagmsType();
      viType.setPagms(pagmsType);
      List<String> viTypePagms = pagmsType.getPagm();
      for (String pagm : viParams.getPagmList()) {
         viTypePagms.add(pagm);
      }

      // fin
      return viType;

   }

   private String marshalVi(ObjectFactory factory, ViType viType)
         throws VIException {

      VIValidationEventHandler eventHandler = new VIValidationEventHandler();

      try {
         JAXBContext context = JAXBContext.newInstance(ViType.class);

         Marshaller marshaller = context.createMarshaller();
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

         marshaller.setSchema(schema);

         marshaller.setEventHandler(eventHandler);
         StringWriter writer = new StringWriter();

         marshaller.marshal(factory.createVi(viType), writer);

         eventHandler.validate();

         return writer.toString();
      } catch (JAXBException e) {
         throw new IllegalArgumentException(e);
      }

   }

   private static class VIValidationEventHandler implements
         ValidationEventHandler {

      private final List<ValidationEvent> validationEvents = new ArrayList<ValidationEvent>();

      @Override
      public boolean handleEvent(ValidationEvent event) {
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
    * Lecture d'un VI de type "portail à portail" et extraction des données
    * "intéressantes" de son contenu dans un objet du modèle facilement
    * utilisable
    * 
    * @param viXml
    *           le VI "portail à portail" au format XML (tel que généré par la
    *           méthode creerVIpourPortailAPortail
    * @return les informations extraites du VI
    * @throws VIException
    *            si une erreur se produit pendant la lecture du VI
    */
   public VIPortailContenuExtrait lireVI(String viXml) throws VIException {

      // TODO Vérification par AOP des paramètres d'entrée de la méthode

      // Utilisation de Jaxb pour désérialiser le xml
      ViType viType = unmarshalVi(viXml);

      // Mapping ViType vers VIPortailContenuExtrait
      VIPortailContenuExtrait viData = new VIPortailContenuExtrait();
      viData.setAudience(viType.getAudience());
      viData.setIssuer(viType.getIssuer());
      viData.setNameId(viType.getNameID());
      for (String pagm : viType.getPagms().getPagm()) {
         viData.getPagmList().add(pagm);
      }
      viData.setHabAnais(viType.getHabilitationAnais());

      // Renvoie du résultat de la méthode
      return viData;

   }

   private ViType unmarshalVi(String xml) throws VIException {

      VIValidationEventHandler eventHandler = new VIValidationEventHandler();
      try {

         JAXBContext context = JAXBContext.newInstance(ViType.class);
         Unmarshaller unmarshaller = context.createUnmarshaller();

         unmarshaller.setEventHandler(eventHandler);
         unmarshaller.setSchema(schema);

         InputStream input = new ByteArrayInputStream(xml.getBytes());

         try {

            ViType viType = (ViType) unmarshaller.unmarshal(input);

            eventHandler.validate();

            return viType;

         } finally {
            input.close();
         }

      } catch (IOException e) {
         throw new IllegalArgumentException(e);
      } catch (UnmarshalException e) {
         throw new VIException(e);
      } catch (JAXBException e) {
         throw new IllegalArgumentException(e);
      }

   }

}
