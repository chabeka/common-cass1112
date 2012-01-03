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

import org.xml.sax.SAXException;

import fr.urssaf.image.sae.vi.configuration.VIConfiguration;
import fr.urssaf.image.sae.vi.exception.VIException;
import fr.urssaf.image.sae.vi.modele.DroitApplicatif;
import fr.urssaf.image.sae.vi.schema.DroitType;
import fr.urssaf.image.sae.vi.schema.IdentiteUtilisateurType;
import fr.urssaf.image.sae.vi.schema.ObjectFactory;
import fr.urssaf.image.sae.vi.schema.PerimetreType;
import fr.urssaf.image.sae.vi.schema.SaeJetonAuthentificationType;

/**
 * Classe de lecture et d'écriture du VI<br>
 * Le VI est un chaine de caractère identique au contenu d'un fichier XML<br>
 * <br>
 * le schéma XSD du VI se situe dans {@link VIConfiguration#path()} <br>
 * La manipulation du XML s'appuie sur le Framework <a
 * href='https://jaxb.dev.java.net//'>JAXB2</a><br>
 * <br>
 * Il est nécessaire de générer les classes du schéma du VI en configurant le
 * pom.xml du projet puis en lançant la commande
 * <code>mvn generate-sources -PgenerateJaxb</code><br>
 * 
 * 
 */
// Bad practice - Usage of GetResource may be unsafe if class is extended :
// L'utilisation de GetResource dans new
// fr.urssaf.image.sae.vi.service.VIService() peut-être instable si la classe
// est étendue
public final class VIService {

   private final Schema schema;

   /**
    * instanciation du {@link Schema} correspondant à
    * {@link VIConfiguration#path()}<br>
    * <br> {@link Schema} permet de valider le VI lors de sa lecture ou de son
    * écriture
    * 
    * @throws IllegalStateException
    */
   public VIService() {

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
    * Méthode de création du VI<br>
    * <br>
    * Un objet {@link Marshaller} est instancié.<br>
    * La création se fait en appelant la méthode
    * {@link Marshaller#marshal(Object, java.io.OutputStream)} <br>
    * 
    * @param lastname
    *           nom de l'utilisateur
    * @param firstname
    *           prénom de l'utilisateur
    * @param droits
    *           liste des drois applicatifs de l'utilisateur pour une
    *           application donnée
    * @return contenu d'un schéma XML
    * @throws VIException
    *            le VI créé est non conforme au XSD du VI
    */
   public String createVI(String lastname, String firstname,
         List<DroitApplicatif> droits) throws VIException {

      // utilisation de jaxb2 pour créer un fichier xml
      ObjectFactory factory = new ObjectFactory();

      SaeJetonAuthentificationType jeton = factory
            .createSaeJetonAuthentificationType();

      IdentiteUtilisateurType idUser = factory.createIdentiteUtilisateurType();
      idUser.setNom(lastname);
      idUser.setPrenom(firstname);
      jeton.setIdentiteUtilisateur(idUser);

      if (!droits.isEmpty()) {
         jeton.setDroits(factory.createDroitsType());
         for (DroitApplicatif droit : droits) {

            DroitType droitType = factory.createDroitType();
            droitType.setPerimetre(factory.createPerimetreType());
            droitType.setCode(droit.getCode());

            PerimetreType perimetre = droitType.getPerimetre();
            perimetre.setValeur(droit.getPerimetreValue());
            perimetre.setCodeType(droit.getPerimetreType());

            jeton.getDroits().getDroit().add(droitType);
         }
      }

      VIValidationEventHandler eventHandler = new VIValidationEventHandler();

      try {
         JAXBContext context = JAXBContext
               .newInstance(SaeJetonAuthentificationType.class);

         Marshaller marshaller = context.createMarshaller();
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

         marshaller.setSchema(schema);

         marshaller.setEventHandler(eventHandler);
         StringWriter writer = new StringWriter();
         marshaller.marshal(factory.createSaeJetonAuthentification(jeton),
               writer);

         eventHandler.validate();

         return writer.toString();
      } catch (JAXBException e) {
         throw new IllegalArgumentException(e);
      }

   }

   /**
    * Méthode de lecture du VI<br>
    * <br>
    * Un objet {@link Unmarshaller} est instancié.<br>
    * La création se fait en appelant la méthode
    * {@link Unmarshaller#unmarshal(InputStream)} <br>
    * 
    * @param xml
    *           VI du jeton d'authentification
    * @return représentation objet du xml
    * @throws VIException
    *            le VI lu est non conforme au XSD du VI
    */
   public SaeJetonAuthentificationType readVI(String xml)
         throws VIException {

      VIValidationEventHandler eventHandler = new VIValidationEventHandler();
      try {

         JAXBContext context = JAXBContext
               .newInstance(SaeJetonAuthentificationType.class);
         Unmarshaller unmarshaller = context.createUnmarshaller();

         unmarshaller.setEventHandler(eventHandler);
         unmarshaller.setSchema(schema);

         InputStream input = new ByteArrayInputStream(xml.getBytes());

         try {
            SaeJetonAuthentificationType jeton = (SaeJetonAuthentificationType) unmarshaller
                  .unmarshal(input);
            eventHandler.validate();

            return jeton;
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
            throw new VIException(validationEvents);
         }

      }

   }
}
