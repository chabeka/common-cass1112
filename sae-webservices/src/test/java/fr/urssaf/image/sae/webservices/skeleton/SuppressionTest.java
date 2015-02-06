package fr.urssaf.image.sae.webservices.skeleton;

import java.util.UUID;

import org.apache.axis2.AxisFault;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionRequestType;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.suppression.SuppressionException;
import fr.urssaf.image.sae.services.suppression.SAESuppressionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class SuppressionTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private SAESuppressionService suppressionService;

   @After
   public void after() {
      EasyMock.reset(suppressionService);
   }

   @Test
   public void testSuppression() throws SuppressionException, AxisFault,
         ArchiveInexistanteEx {

      Suppression request = new Suppression();
      SuppressionRequestType type = new SuppressionRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(UUID.randomUUID().toString());
      type.setUuid(uuidType);

      request.setSuppression(type);

      suppressionService.suppression(EasyMock.anyObject(UUID.class));
      EasyMock.expectLastCall().once();

      EasyMock.replay(suppressionService);

      SuppressionResponse suppressionSecure = skeleton
            .suppressionSecure(request);

      EasyMock.verify(suppressionService);

      Assert.assertNotNull("la réponse doit etre non nulle", suppressionSecure
            .getSuppressionResponse());
   }

}
