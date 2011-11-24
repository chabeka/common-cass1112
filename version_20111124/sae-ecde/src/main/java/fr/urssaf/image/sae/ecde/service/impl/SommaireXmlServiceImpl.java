package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType;
import fr.urssaf.image.sae.ecde.service.SommaireXmlService;
import fr.urssaf.image.sae.ecde.util.JAXBUtils;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;

/**
 * Service permettant la lecture des fichiers sommaire.xml <br>
 * <br>
 * Cette fonctionnalité permet la lecture des fichiers sommaires.xml. Un fichier
 * sommaire.xml<br>
 * doit être representé via le modèle objet par un objet de type <br>
 * {@link fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType}
 * 
 */
@Service
@Qualifier("somXmlService")
public class SommaireXmlServiceImpl implements SommaireXmlService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SommaireXmlServiceImpl.class);
   @Autowired
   private ApplicationContext context;

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   /**
    * @param context
    *           . Le context Spring.
    */
   public final void setContext(ApplicationContext context) {
      this.context = context;
   }

   /**
    * Methode permettant la lecture du fichier sommaire.xml <br>
    * avec en entree un flux
    * 
    * @param input
    *           de type InputStream
    * @return SommaireType objet representant le contenu d'un fichier
    *         sommaire.xml
    * 
    * @throws EcdeXsdException
    *            une erreur de structure a été detectée sur le sommaire.xml
    */
   @Override
   public final SommaireType readSommaireXml(InputStream input)
         throws EcdeXsdException {
      String prefixeTrc = "readSommaireXml()";
      LOGGER.debug("{} - Début", prefixeTrc);
      try {
         LOGGER
               .debug(
                     "{} - Début du chargement du fichier sommaire.xml dans les objets Jaxb",
                     prefixeTrc);
         final Resource classPath = getContext().getResource(
               "classpath:xsd_som_res/sommaire.xsd");
         // ClassPathResource classPath = new
         // ClassPathResource("xsd_som_res/sommaire.xsd");
         URL xsdSchema = classPath.getURL();
         SommaireType sommaireType = JAXBUtils.unmarshal(input, xsdSchema);
         LOGGER
         .debug(
               "{} - Fin du chargement du fichier sommaire.xml dans les objets Jaxb",
               prefixeTrc);
         return sommaireType;
      } catch (JAXBException e) {
         // Pour la gestion de l'erreur BATCH MODE
         if (e.getLinkedException().getMessage().contains(
               "[TOUT_OU_RIEN, PARTIEL]")) {
            LOGGER.debug("{} - {}", prefixeTrc, MessageRessourcesUtils
                  .recupererMessage("invalid.batchmode.error", null));
            throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage(
                  "invalid.batchmode.error", null), e);
         } else {
            LOGGER.debug("{} - {}", prefixeTrc, MessageRessourcesUtils.recupererMessage(
                  "sommaireresultatsexception.message", "sommaire.xml"));
            throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage(
                  "sommaireresultatsexception.message", "sommaire.xml"), e);
         }
      } catch (SAXException e) {
         LOGGER.debug("{} - {}", prefixeTrc, MessageRessourcesUtils.recupererMessage(
               "sommaireresultatsexception.message", "sommaire.xml"));
         throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage(
               "sommaireresultatsexception.message", "sommaire.xml"), e);
      } catch (IOException e) {
         LOGGER.debug("{} - {}", prefixeTrc, MessageRessourcesUtils
               .recupererMessage("sommairelectureexception.message", null));
         throw new EcdeRuntimeException(MessageRessourcesUtils
               .recupererMessage("sommairelectureexception.message", null), e);
      }
   }

   /**
    * Methode permettant la lecture du fichier sommaire.xml <br>
    * avec en entree un flux
    * 
    * @param input
    *           de type File
    * @return SommaireType objet representant le contenu d'un fichier
    *         sommaire.xml
    * 
    * @throws EcdeXsdException
    *            une erreur de structure a été detectée sur le sommaire.xml
    */
   @Override
   public final SommaireType readSommaireXml(File input)
         throws EcdeXsdException {
      try {
         return readSommaireXml(convertFileToInputStream(input));
      } catch (IOException e) {
         throw new IllegalArgumentException("Erreur d'argument en entrée.", e);
      }
   }

   /*
    * Methode permettant de convertir un objet de type File en InputStream
    */
   private InputStream convertFileToInputStream(File file) throws IOException {

      return FileUtils.openInputStream(file);
   }
}
