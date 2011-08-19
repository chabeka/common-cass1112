package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType;
import fr.urssaf.image.sae.ecde.service.SommaireXmlService;
import fr.urssaf.image.sae.ecde.util.JAXBUtils;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;

/**
 * Service permettant la lecture des fichiers sommaire.xml 
 * <br><br>
 * Cette fonctionnalité permet la lecture des fichiers sommaires.xml. Un fichier sommaire.xml<br>
 * doit être representé via le modèle objet par un objet de type
 * <br>{@link fr.urssaf.image.sae.ecde.modele.sommaire.SommaireType} 
 * 
 */
@Service
public class SommaireXmlServiceImpl implements SommaireXmlService {

   /**
    * Methode permettant la lecture du fichier sommaire.xml
    * <br>avec en entree un flux
    *
    * @param input de type InputStream
    * @return SommaireType objet representant le contenu d'un fichier sommaire.xml
    *
    * @throws EcdeXsdException une erreur de structure a été detectée sur le sommaire.xml
    */
   @Override
   public final SommaireType readSommaireXml(InputStream input) throws EcdeXsdException {
      
      try {
         Class<SommaireType> docClass = SommaireType.class;
         ClassPathResource classPath = new ClassPathResource("xsd_som_res/sommaire.xsd");
         URL xsdSchema = classPath.getURL();
         return JAXBUtils.unmarshal(docClass, input, xsdSchema);
      } catch (JAXBException e) {
         throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage("sommaireResultatsException.message", "sommaire.xml"), e);
      } catch (SAXException e) {
         throw new EcdeXsdException(MessageRessourcesUtils.recupererMessage("sommaireResultatsException.message", "sommaire.xml"), e);
      } catch (IOException e) {
         throw new EcdeRuntimeException(MessageRessourcesUtils.recupererMessage("sommaireLectureException.message", e));
      }
   }
   /**
    * Methode permettant la lecture du fichier sommaire.xml
    * <br>avec en entree un flux
    *
    * @param input de type File
    * @return SommaireType objet representant le contenu d'un fichier sommaire.xml
    *
    * @throws EcdeXsdException une erreur de structure a été detectée sur le sommaire.xml
    */
   @Override
   public final SommaireType readSommaireXml(File input) throws EcdeXsdException {
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
