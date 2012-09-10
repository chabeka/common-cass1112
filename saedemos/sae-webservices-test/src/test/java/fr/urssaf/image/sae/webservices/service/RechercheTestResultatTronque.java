package fr.urssaf.image.sae.webservices.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

import org.apache.commons.lang.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class RechercheTestResultatTronque {

   @Autowired
   private RechercheService service;

   @Autowired
   private ArchivageMasseService masseService;

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }

   @Before
   public final void init() throws URISyntaxException, RemoteException {
      URI urlSommaireEcde = new URI(
            "ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20120828/casdetest/sommaire.xml");
      masseService.archivageMasse(urlSommaireEcde);

   }

   @Test
   @Ignore("Resultat non prédictible")
   public void recherche_success_resultat_tronque() throws RemoteException {

      AuthenticateUtils.authenticate("ROLE_TOUS;FULL");

      String lucene = "Denomination:\"TEST_10\"";
      String[] codes = new String[] { "Titre", "Hash" };

      RechercheResponseType response = service.recherche(lucene, codes);

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertEquals("on s'attend à avoir 249 documents", 249,
            resultats.length);

      Assert.assertTrue("le resultat doit etre tronqué", response
            .getResultatTronque());

   }

}
