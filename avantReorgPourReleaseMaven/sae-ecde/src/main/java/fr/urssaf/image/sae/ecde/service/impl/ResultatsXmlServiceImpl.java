package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.ObjectFactory;
import fr.urssaf.image.sae.ecde.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.ecde.service.ResultatsXmlService;
import fr.urssaf.image.sae.ecde.util.JAXBUtils;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;
/**
 * Implementation de la methode writeResultatsXml pour l'ecriture du fichier <br>
 * resultats.xml.
 * 
 */
@Service
public class ResultatsXmlServiceImpl implements ResultatsXmlService {
   private static final Logger LOGGER = LoggerFactory
   .getLogger(ResultatsXmlServiceImpl.class);
   // pour la creation d'un object JAXBElement resultatsType
   private static ObjectFactory object = new ObjectFactory();
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
    * Methode permettant l'ecriture du fichier resultats.xml
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output flux dans lequel écrire resultats.xml
    * @throws EcdeXsdException erreur de structure a été détectée sur le resultats.xml
    */
   @Override
   public final void writeResultatsXml(ResultatsType resultatsXml, OutputStream output) throws EcdeXsdException {
      try {
         String prefixeTrc = "writeResultatsXml()";
         LOGGER.debug("{} - Début", prefixeTrc);
         LOGGER
         .debug(
               "{} - Début de la génération du fichier resultats.xml",
               prefixeTrc);
         final Resource classPath = getContext().getResource("classpath:xsd_som_res/resultats.xsd");
         URL xsdSchema = classPath.getURL();
         JAXBElement<ResultatsType> resultats = object.createResultats(resultatsXml);
         
         JAXBUtils.marshal(resultats, output, xsdSchema);
         //ici on force la libération des buffers
         output.flush();
         //ici on ferme
         output.close();
         LOGGER
         .debug(
               "{} - Fin de la génération du fichier resultats.xml",
               prefixeTrc);
         
      } catch (SAXException e) {
         throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage("sommaireresultatsexception.message", "resultats.xml"), e);
      } catch (JAXBException e) {
         throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage("sommaireresultatsexception.message", "resultats.xml"), e);
      } catch (IOException e) {
         throw new EcdeRuntimeException(MessageRessourcesUtils.recupererMessage("resultatsecritureexception.message", null), e);
      }
  }
  /**
    * Methode permettant l'ecriture du fichier resultats.xml
    * 
    * @param resultatsXml objet representant le contenu du resultats.xml
    * @param output fichier dans lequel écrire resultats.xml
    * @throws EcdeXsdException erreur de structure a été détectée sur le resultats.xml
    */
   @Override
   public final void writeResultatsXml(ResultatsType resultatsXml, File output) throws EcdeXsdException {
      try {
         writeResultatsXml(resultatsXml, convertFileToOutputStream(output));
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }
   }
   /*
    * Methode permettant de convertir un objet de type File en OutputStream
    */
   private OutputStream convertFileToOutputStream(File file) throws IOException {
      
      return FileUtils.openOutputStream(file);
   }

}