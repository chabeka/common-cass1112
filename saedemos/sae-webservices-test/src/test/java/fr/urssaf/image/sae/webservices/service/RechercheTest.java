package fr.urssaf.image.sae.webservices.service;

import java.rmi.RemoteException;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ResultatRechercheType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class RechercheTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(RechercheTest.class);

   @Autowired
   private RechercheService service;

   @Test
   @Ignore("Resultat non prédictible")
   public void recherche_success() throws RemoteException {

      String lucene = "Siren:123456789 AND CodeRND:2.3.1.1.8";
      String[] codes = new String[] { "Titre", "Hash" };

      RechercheResponseType response = service.recherche(lucene, codes);

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertFalse("on s'attend à avoir au moins un document", ArrayUtils
            .isEmpty(resultats));

      LOG.debug("la recherche renvoie " + resultats.length + " documents");

   }

}
