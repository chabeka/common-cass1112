package factory;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.factory.ConvertFactory;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class ConvertFactoryTest {

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CorrespondancesRndSupport correspondanceRndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Ignore
   @Test
   public void testWsToDocumentsType() {

      RNDTypeDocument rndTypDoc1 = new RNDTypeDocument();
      
      rndTypDoc1.set_dureeArchivage(300);
      rndTypDoc1.set_etat(true);
      rndTypDoc1.set_refActivite("1");
      rndTypDoc1.set_refFonction("2");
      rndTypDoc1.set_label("label 1");
      rndTypDoc1.set_reference("1.1.1.1.1");

      RNDTypeDocument rndTypDoc2 = new RNDTypeDocument();

      rndTypDoc2.set_dureeArchivage(500);
      rndTypDoc2.set_etat(false);
      rndTypDoc2.set_refActivite("3");
      rndTypDoc2.set_refFonction("4");
      rndTypDoc2.set_label("label 2");
      rndTypDoc2.set_reference("2.2.2.2.2");

      RNDTypeDocument[] listeRndTypeDocs = { rndTypDoc1, rndTypDoc2 };

      ConvertFactory cf = new ConvertFactory();

      List<TypeDocument> listeTypeDocs = cf.wsToDocumentsType(listeRndTypeDocs);

      assertNotNull("La liste ne doit pas être nulle", listeTypeDocs);

      Assert.assertEquals("Le code RND doit être identique", listeTypeDocs.get(
            0).getCode(), "1.1.1.1.1");

   }

}
