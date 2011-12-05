package fr.urssaf.image.sae.webservices.skeleton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationResponseType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ConsultationTest {

   @Autowired
   private SaeServiceSkeleton skeleton;

   private Consultation createConsultationResponseType(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return Consultation.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   private static void assertMetadata(MetadonneeType metadata,
         Map<String, Object> expectedMetadatas) {

      Assert.assertTrue("la metadonnée '"
            + metadata.getCode().getMetadonneeCodeType() + "' est inattendue",
            expectedMetadatas.containsKey(metadata.getCode()
                  .getMetadonneeCodeType()));

      Assert.assertEquals("la valeur de la metadonnée est inattendue",
            expectedMetadatas.get(metadata.getCode().getMetadonneeCodeType()),
            metadata.getValeur().getMetadonneeValeurType());

      expectedMetadatas.remove(metadata.getCode().getMetadonneeCodeType());
   }

   @Autowired
   private SAEDocumentService documentService;

   @After
   public void after() {
      EasyMock.reset(documentService);
   }

   @Test
   public void consultation_success() throws IOException,
         SAEConsultationServiceException {

      UntypedDocument document = new UntypedDocument();

      File expectedContent = new File(
            "src/test/resources/storage/attestation.pdf");

      document.setContent(FileUtils.readFileToByteArray(expectedContent));
      List<UntypedMetadata> untypedMetadatas = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode("CodeActivite");
      metadata1.setValue("2");
      untypedMetadatas.add(metadata1);

      UntypedMetadata metadata2 = new UntypedMetadata();
      metadata2.setLongCode("ContratDeService");
      metadata2.setValue("CS1");
      untypedMetadatas.add(metadata2);

      document.setUMetadatas(untypedMetadatas);

      EasyMock.expect(
            documentService.consultation(UUID
                  .fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf")))
            .andReturn(document);

      EasyMock.replay(documentService);

      Consultation request = createConsultationResponseType("src/test/resources/request/consultation_success.xml");

      ConsultationResponseType response = skeleton.consultationSecure(request)
            .getConsultationResponse();

      MetadonneeType[] metadatas = response.getMetadonnees().getMetadonnee();

      Assert.assertNotNull("la liste des metadonnées doit être renseignée",
            metadatas);
      Assert.assertEquals("nombre de metadatas inattendu", 2, metadatas.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("CodeActivite", "2");
      expectedMetadatas.put("ContratDeService", "CS1");

      assertMetadata(metadatas[0], expectedMetadatas);
      assertMetadata(metadatas[1], expectedMetadatas);

      Assert.assertTrue("Des métadonnées sont attendues", expectedMetadatas
            .isEmpty());

      DataHandler actualContent = response.getObjetNumerique()
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();

      Assert.assertEquals("le contenu n'est pas attendu en base64", Base64Utils
            .encode(FileUtils.readFileToByteArray(expectedContent)),
            Base64Utils.encode(actualContent));

      Assert.assertTrue("le contenu n'est pas attendu", IOUtils.contentEquals(
            FileUtils.openInputStream(expectedContent), actualContent
                  .getInputStream()));

      Assert
            .assertNull(
                  "Test de l'archivage unitaire : doit avoir aucune url directe de consultation",
                  response.getObjetNumerique()
                        .getObjetNumeriqueConsultationTypeChoice_type0()
                        .getUrl());
   }


}
