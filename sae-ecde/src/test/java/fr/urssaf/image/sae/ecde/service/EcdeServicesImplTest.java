package fr.urssaf.image.sae.ecde.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Classe permettant de tester que les liens se font bien avec
 * <br>
 * les differents services.
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings("PMD.MethodNamingConventions")
public class EcdeServicesImplTest {

   private static final String MESSAGE_INNATENDU = "message inattendu";
   private static final String ECDE = "ecde";
   private static final String ATTESTATION = "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
   private static final String SOMMAIRE = "/DCL001/19991231/3/sommaire.xml";

   private static final File ATTESTATION_FILE = new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");
   private static final String SEPARATOR = "://";
   
    
   @Autowired
   private EcdeServices ecdeServices;
   @Autowired
   public EcdeSources ecdeSources;
   
   
   @Test
   public void convertFileToURITest() throws EcdeBadFileException {
      URI uri = ecdeServices.convertFileToURI(ATTESTATION_FILE);
      String resultatAttendu = "ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
      String resultatObtenu = uri.getScheme() + SEPARATOR + uri.getAuthority() + uri.getPath();
      
      assertEquals(MESSAGE_INNATENDU, resultatAttendu, resultatObtenu);
   }

   @Test
   public final void convertSommaireToFileTest() throws EcdeBadURLException, EcdeBadURLFormatException, URISyntaxException {
      
      URI uri = new URI(ECDE, "ecde.cer69.recouv", SOMMAIRE, null);
      File messageObtenu = ecdeServices.convertSommaireToFile(uri);
      File messageAttendu = new File("/ecde/ecde_lyon/DCL001/19991231/3/sommaire.xml");
      
      assertEquals(MESSAGE_INNATENDU, messageObtenu, messageAttendu);
   }

   @Test
   public void convertURIToFileTest() throws EcdeBadURLException, EcdeBadURLFormatException, URISyntaxException {
      
      URI uri = new URI(ECDE, "ecde.cer69.recouv", ATTESTATION, null);
      File messageObtenu = ecdeServices.convertURIToFile(uri);
      File messageAttendu = new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");
      
      assertEquals(MESSAGE_INNATENDU, messageObtenu, messageAttendu);
   }

   
}
