package fr.urssaf.image.sae.services;

import static fr.urssaf.image.sae.services.mapping.BeanTestDocumentMapper.saeMockDocumentXmlToUntypedDocument;

import java.io.IOException;
import java.text.ParseException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.model.SAEDocumentMockData;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.mapping.BeanTestDocumentMapper;

/**
 * Classe de base pour les tests unitaires.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("all")
public class CommonsServices {
   // CHECKSTYLE:OFF

   @Autowired
   @Qualifier("saeServiceProvider")
   private SAEServiceProvider sAEServiceProvider;
   @Autowired
   @Qualifier("xmlMockDataService")
   private XmlMockDataService xmlMockDataService;
   @Autowired
   private ApplicationContext context;

   /**
    * @return Le service xmlMockDataService
    */
   public final XmlMockDataService getXmlMockDataService() {
      return xmlMockDataService;
   }

   /**
    * @param xmlMockDataService
    *           : Le service xmlMockDataService.
    */
   public final void setXmlMockDataService(XmlMockDataService xmlMockDataService) {
      this.xmlMockDataService = xmlMockDataService;
   }

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   /**
    * @param context
    *           : le context
    */
   public final void setContext(final ApplicationContext context) {
      this.context = context;
   }

   /**
    * @return Le service xmlDataService
    */
   public final XmlMockDataService getXmlDataService() {
      return xmlMockDataService;
   }

   /**
    * @param xmlMockDataService
    *           Le service xmlDataService
    */
   public final void setXmlDataService(XmlMockDataService xmlMockDataService) {
      this.xmlMockDataService = xmlMockDataService;
   }

   /**
    * Initialisation des tests. <br>
    * @return SAEDocument {@link SAEDocument}
    */

   public final SAEDocument getSAEDocumentMockData() throws IOException,
         ParseException, SAECaptureServiceEx {
      // Injection de jeu de donnée.
      final Resource resource = context
            .getResource("classpath:XML/saeDocumentMockData.xml");
      try {
         final SAEDocumentMockData saeDocumentMock = getXmlDataService()
               .saeDocumentReader(resource.getInputStream());

         return BeanTestDocumentMapper
               .saeMockDocumentXmlToSAEDocument(saeDocumentMock);
      } catch (IOException e) {
         throw new SAECaptureServiceEx(
               "Le fichier contenant les documents de tests unitaire n''est pas disponible.",
               e);
      }
   }

   /**
    * Initialisation des tests. <br>
    * @return UntypedDocument {@link UntypedDocument}
    */

   public final UntypedDocument getUntypedDocumentMockData()
         throws IOException, ParseException, SAECaptureServiceEx {
      // Injection de jeu de donnée.
      final Resource resource = context
            .getResource("classpath:XML/saeDocumentMockData.xml");
      try {
         final SAEDocumentMockData saeDocumentMock = getXmlDataService()
               .saeDocumentReader(resource.getInputStream());

         return saeMockDocumentXmlToUntypedDocument(saeDocumentMock);
      } catch (IOException e) {
         throw new SAECaptureServiceEx(
               "Le fichier contenant les documents de tests unitaire n''est pas disponible.",
               e);
      }
   }

   /**
    * @return Un service.
    */
   public final SAEServiceProvider getSaeServiceProvider() {
      return sAEServiceProvider;
   }

   /**
    * @param sAEServiceProvider
    *           : Service provider.
    */
   public final void setSaeServiceProvider(SAEServiceProvider sAEServiceProvider) {
      this.sAEServiceProvider = sAEServiceProvider;
   }
   // CHECKSTYLE:ON
}
