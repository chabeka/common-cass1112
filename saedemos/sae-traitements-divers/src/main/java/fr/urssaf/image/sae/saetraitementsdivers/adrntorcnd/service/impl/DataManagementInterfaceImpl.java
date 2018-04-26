/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.exception.AdrnToRcndException;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanConfig;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.service.DataManagementInterface;

/**
 * 
 * Stockage des données
 */
@Service
public class DataManagementInterfaceImpl implements DataManagementInterface {

   @Autowired
   private BeanConfig beanConfig;

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.EmptyCatchBlock")
   public void saveDocuments(List<BeanRNDTypeDocument> listDoc, String version)
         throws AdrnToRcndException {

      FileWriter fileWriter = null;
      StreamResult streamResult = null;

      try {
         File file = new File(beanConfig.getSavedFilePath());
         fileWriter = new FileWriter(file);

         streamResult = new StreamResult(fileWriter);
         SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
               .newInstance();

         // SAX2.0 ContentHandler.
         TransformerHandler handler = factory.newTransformerHandler();
         Transformer serializer = handler.getTransformer();
         serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
         handler.setResult(streamResult);
         handler.startDocument();
         AttributesImpl atts = new AttributesImpl();
         // USERS tag.
         handler.startElement(StringUtils.EMPTY, StringUtils.EMPTY, "RCND",
               atts);

         for (BeanRNDTypeDocument beanDoc : listDoc) {
            addTypeDocument(handler, beanDoc, version, atts);
         }

         handler.endElement(StringUtils.EMPTY, StringUtils.EMPTY, "RCND");
         handler.endDocument();

      } catch (TransformerConfigurationException e) {
         throw new AdrnToRcndException(e);
      } catch (IllegalArgumentException e) {
         throw new AdrnToRcndException(e);
      } catch (IOException e) {
         throw new AdrnToRcndException(e);
      } catch (TransformerFactoryConfigurationError e) {
         throw new AdrnToRcndException(e);
      } catch (SAXException e) {
         throw new AdrnToRcndException(e);

      } finally {

         try {
            if (fileWriter != null) {
               fileWriter.close();
            }
         } catch (IOException exception) {
            // nothing to do
         }

      }

   }
   
   /** 
    * {@inheritDoc}
    */
   @Override
   public void saveLiveCycle(List<BeanRNDTypeDocument> listDoc, String version)
         throws AdrnToRcndException {
      
      FileWriter fileWriter = null;
      StreamResult streamResult = null;
      
      try {
         File file = new File(beanConfig.getLifeCycleFilePath());
         fileWriter = new FileWriter(file);

         streamResult = new StreamResult(fileWriter);
         SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
               .newInstance();

         // SAX2.0 ContentHandler.
         TransformerHandler handler = factory.newTransformerHandler();
         Transformer serializer = handler.getTransformer();
         serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
         handler.setResult(streamResult);
         handler.startDocument();
         AttributesImpl atts = new AttributesImpl();
         // USERS tag.
         handler.startElement(StringUtils.EMPTY, StringUtils.EMPTY, "LifeCycleRule",
               atts);

         for (BeanRNDTypeDocument beanDoc : listDoc) {
            addLifeCycle(handler, beanDoc, version, atts);
         }

         handler.endElement(StringUtils.EMPTY, StringUtils.EMPTY, "LifeCycleRule");
         handler.endDocument();

      } catch (TransformerConfigurationException e) {
         throw new AdrnToRcndException(e);
      } catch (IllegalArgumentException e) {
         throw new AdrnToRcndException(e);
      } catch (IOException e) {
         throw new AdrnToRcndException(e);
      } catch (TransformerFactoryConfigurationError e) {
         throw new AdrnToRcndException(e);
      } catch (SAXException e) {
         throw new AdrnToRcndException(e);

      } finally {

         try {
            if (fileWriter != null) {
               fileWriter.close();
            }
         } catch (IOException exception) {
            // nothing to do
         }

      }
   }

   /**
    * @param handler
    * @param beanDoc
    * @param atts
    * @throws SAXException
    */
   private void addTypeDocument(TransformerHandler handler,
         BeanRNDTypeDocument beanDoc, String version,
         AttributesImpl atts) throws SAXException {

      atts.clear();

      handler.startElement(StringUtils.EMPTY, StringUtils.EMPTY,
            "TypeDocument", atts);

      addElement(handler, "CodeRND", beanDoc.getCodeRND(), atts);

      addElement(handler, "CodeFonction", beanDoc.getCodeFonction(),
            atts);

      addElement(handler, "CodeActivite", beanDoc.getCodeActivite(),
            atts);
      addElement(handler, "LibelleRND", beanDoc.getCodeLibelle(), atts);
      addElement(handler, "DureeConservation", beanDoc
            .getDureeConservation(), atts);
      addElement(handler, "VersionRND", beanDoc.getVersionRND(), atts);

      handler.endElement(StringUtils.EMPTY, StringUtils.EMPTY, "TypeDocument");
   }
   
   /**
    * @param handler
    * @param beanDoc
    * @param atts
    * @throws SAXException
    */
   private void addLifeCycle(TransformerHandler handler,
         BeanRNDTypeDocument beanDoc, String version,
         AttributesImpl atts) throws SAXException {

      atts.clear();

      handler.startElement(StringUtils.EMPTY, StringUtils.EMPTY,
            "Rule", atts);

      addElement(handler, "CodeRND", beanDoc.getCodeRND(), atts);

      addElement(handler, "DureeConservation", beanDoc
            .getDureeConservation(), atts);

      handler.endElement(StringUtils.EMPTY, StringUtils.EMPTY, "Rule");
   }

   private void addElement(TransformerHandler handler, String name,
         String value, AttributesImpl atts) throws SAXException {
      handler.startElement(StringUtils.EMPTY, StringUtils.EMPTY, name, atts);

      if (StringUtils.isNotBlank(value)) {
         char[] tabValue = value.toCharArray();
         handler.characters(tabValue, 0, tabValue.length);
      }
      handler.endElement(StringUtils.EMPTY, StringUtils.EMPTY, name);
   }

}
