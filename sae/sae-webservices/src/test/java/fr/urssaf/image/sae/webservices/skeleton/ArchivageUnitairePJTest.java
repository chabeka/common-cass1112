package fr.urssaf.image.sae.webservices.skeleton;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponseType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageUnitairePJTest {

   @Autowired
   private SaeServiceSkeleton skeleton;

   @Autowired
   private SAECaptureService captureService;

   @After
   public void after() {
      EasyMock.reset(captureService);
   }

   private ArchivageUnitairePJ createArchivageUnitairePJ(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageUnitairePJ.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   @Test
   public void archivageUnitairePJ_urlEcde_success() throws AxisFault {

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(EasyMock.anyObject(UntypedMetadata.class));

      try {
         EasyMock
               .expect(
                     captureService.capture(metadatas, EasyMock
                           .anyObject(URI.class))).andReturn(
                     UUID.fromString("110E8400-E29B-11D4-A716-446655440000"));
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(captureService);

      assertArchivageUnitairePJ("src/test/resources/request/archivageUnitairePJ_urlEcde_success.xml");
   }

   @Test
   public void archivageUnitairePJ_file_success() throws AxisFault {

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      metadatas.add(EasyMock.anyObject(UntypedMetadata.class));

      DataHandler content = EasyMock.notNull();

      try {

         EasyMock.expect(
               captureService.captureBinaire(metadatas, content, EasyMock
                     .anyObject(String.class))).andReturn(
               UUID.fromString("110E8400-E29B-11D4-A716-446655440000"));
      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

      EasyMock.replay(captureService);

      assertArchivageUnitairePJ("src/test/resources/request/archivageUnitairePJ_file_success.xml");
   }

   private void assertArchivageUnitairePJ(String soap) throws AxisFault {

      ArchivageUnitairePJ request = createArchivageUnitairePJ(soap);

      ArchivageUnitairePJResponseType response = skeleton
            .archivageUnitairePJSecure(request)
            .getArchivageUnitairePJResponse();

      Assert.assertEquals("Test de l'archivage unitaire",
            "110E8400-E29B-11D4-A716-446655440000", response.getIdArchive()
                  .getUuidType());

      EasyMock.verify(captureService);
   }

}
