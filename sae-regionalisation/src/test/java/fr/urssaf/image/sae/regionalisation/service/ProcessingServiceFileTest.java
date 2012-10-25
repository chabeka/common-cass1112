/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.service;

import java.io.File;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.exception.LineFormatException;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ProcessingServiceFileTest {

   @Autowired
   private ServiceProviderSupport providerSupport;

   @Autowired
   private SaeDocumentDao saeDocumentDao;

   @Autowired
   private TraceDao traceDao;

   @Autowired
   private File repository;

   @Autowired
   private ApplicationContext context;

   @Autowired
   private ProcessingService service;

   @Test
   public void test_failure_format_fichier() throws IOException {

      try {
         // connexion à DFCE
         providerSupport.connect();
         EasyMock.expectLastCall().once();

         // déconnexion à DFCE
         providerSupport.disconnect();
         EasyMock.expectLastCall().once();

         EasyMock.replay(providerSupport);

         Resource resource = context.getResource("csv/fichier_format_errone");
         File fichier = resource.getFile();

         service.launchWithFile(false, fichier, "12", 1, 12, repository
               .getAbsolutePath());

      } catch (Exception e) {

         EasyMock.verify(providerSupport);

         Assert.assertEquals("le classe d'exception doit être "
               + LineFormatException.class.getName(),
               LineFormatException.class, e.getClass());
      }

      EasyMock.reset(providerSupport, traceDao, saeDocumentDao);
   }

}
