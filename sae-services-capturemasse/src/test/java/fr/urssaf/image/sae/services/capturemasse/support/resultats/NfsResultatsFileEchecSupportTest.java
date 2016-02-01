/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.EcdePermissionException;
import fr.urssaf.image.sae.services.capturemasse.utils.StaxUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-services-capturemasse-test.xml",
      "/applicationContext-sae-services-capturemasse-test-mock-stax.xml" })
public class NfsResultatsFileEchecSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private ResultatsFileEchecSupport support;

   private EcdeTestSommaire ecdeTestSommaire;

   @Autowired
   private StaxUtils staxUtils;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();

      initMock();
   }

   private void initMock() {

      staxUtils.initStream(EasyMock.anyObject(File.class));
      EasyMock.expectLastCall().once();
      EasyMock.expectLastCall().andThrow(
            new EcdePermissionException(new FileNotFoundException(
                  "Impossible de trouver le fichier"))).once();
      EasyMock.expectLastCall().once();

      staxUtils.startDocument();
      EasyMock.expectLastCall().andThrow(
            new EcdePermissionException(new XMLStreamException(
                  "Impossible d'écrire le fichier"))).times(2);

      staxUtils.closeAll();
      EasyMock.expectLastCall().times(3);

      EasyMock.replay(staxUtils);

   }

   @After
   public void end() {

      EasyMock.reset(staxUtils);

      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test
   public void testEcritureSommaire() throws IOException, JAXBException {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource("sommaire.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repEcde = new File(ecdeDirectory, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      List<String> codes = new ArrayList<String>();
      codes.add(Constantes.ERR_BUL002);
      List<Integer> index = new ArrayList<Integer>();
      index.add(3);
      List<Exception> exceptions = new ArrayList<Exception>();
      exceptions.add(new Exception("la valeur x est erronée"));
      List<Integer> refIndex = new ArrayList<Integer>();

      erreur.setListCodes(codes);
      erreur.setListException(exceptions);
      erreur.setListIndex(index);
      erreur.setListRefIndex(refIndex);

      try {
         support.writeResultatsFile(ecdeDirectory, sommaire, erreur, 21);

         Assert.fail("une exception doit être levée");
      } catch (Exception exception) {

         EasyMock.verify(staxUtils);

         Assert.assertEquals("le type d'exception doit être bon",
               CaptureMasseRuntimeException.class, exception.getClass());
         Assert.assertEquals("le type d'exception mère doit être bon",
               XMLStreamException.class, exception.getCause().getClass());

         File resultats = new File(ecdeDirectory, "resultats.xml");

         Assert.assertFalse("le fichier resultats.xml doit exister", resultats
               .exists());

      }
   }

   @Test
   public void testEcritureVirtuelResultats() throws IOException,
         JAXBException, ParserConfigurationException, SAXException {
      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      File sommaire = new File(ecdeDirectory, "sommaire.xml");
      ClassPathResource resSommaire = new ClassPathResource(
            "sommaire_virtuel.xml");
      FileOutputStream fos = new FileOutputStream(sommaire);

      IOUtils.copy(resSommaire.getInputStream(), fos);
      File repEcde = new File(ecdeDirectory, "documents");
      ClassPathResource resAttestation1 = new ClassPathResource("doc1.PDF");
      File fileAttestation1 = new File(repEcde, "doc1.PDF");
      fos = new FileOutputStream(fileAttestation1);
      IOUtils.copy(resAttestation1.getInputStream(), fos);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      List<String> codes = new ArrayList<String>();
      codes.add(Constantes.ERR_BUL002);
      List<Integer> index = new ArrayList<Integer>();
      index.add(1);
      List<Exception> exceptions = new ArrayList<Exception>();
      exceptions.add(new Exception("la valeur x est erronée"));
      List<Integer> refIndex = new ArrayList<Integer>();

      erreur.setListCodes(codes);
      erreur.setListException(exceptions);
      erreur.setListIndex(index);
      erreur.setListRefIndex(refIndex);

      try {
         support.writeVirtualResultatsFile(ecdeDirectory, sommaire, erreur, 3);
         Assert.fail("une exception doit être levée");
      } catch (Exception exception) {

         EasyMock.verify(staxUtils);

         Assert.assertEquals("le type d'exception doit être bon",
               CaptureMasseRuntimeException.class, exception.getClass());
         Assert.assertEquals("le type d'exception mère doit être bon",
               XMLStreamException.class, exception.getCause().getClass());

         File resultats = new File(ecdeDirectory, "resultats.xml");

         Assert.assertFalse("le fichier resultats.xml doit exister", resultats
               .exists());

      }
   }

}
