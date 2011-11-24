package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertNotNull;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml"})
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class ArchivageMasseTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Test
   @Ignore
   public void archivageMasse_success() throws AxisFault {

      ArchivageMasse request = createArchivageMasse("src/test/resources/request/archivageMasse_success.xml");

      ArchivageMasseResponse response = skeleton.archivageMasseSecure(request);

      assertNotNull("Test de l'archivage masse", response
            .getArchivageMasseResponse());
   }

   private ArchivageMasse createArchivageMasse(String filePath) {

      try {

         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return ArchivageMasse.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

}
