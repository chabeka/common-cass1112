package fr.urssaf.image.commons.webservice.rpc.aed.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.apache.axis.AxisProperties;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.commons.webservice.rpc.aed.context.AEDJSSESocketFactory;
import fr.urssaf.image.commons.webservice.rpc.aed.service.modele.ParametrageTransfertModele;

@SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
public class AEServiceTest {

   protected static final Logger LOG = Logger.getLogger(AEServiceTest.class);

   private final AEDService service;

   public AEServiceTest() {

      service = new AEDServiceImpl();
      AxisProperties.setProperty("axis.socketSecureFactory",
            AEDJSSESocketFactory.class.getCanonicalName());

   }

   @Test
   @Ignore("L'hébergement du service web n'est pas garanti, ce dernier peut très bien être indisponible (sans parler des problèmes de certificats ...)")
   public void ping() throws RemoteException {

      LOG.debug(service.ping());
      assertEquals("Les fonctions SAEL organisme sont en ligne.", service
            .ping());

   }

   @Test
   @SuppressWarnings("PMD.ShortVariable")
   @Ignore("L'hébergement du service web n'est pas garanti, ce dernier peut très bien être indisponible (sans parler des problèmes de certificats ...)")
   public void parametrageTransfert() throws RemoteException,
         UnknownHostException {

      String applicationSource = "SAEL";

      String IP = InetAddress.getLocalHost().getHostAddress();

      ParametrageTransfertModele modele = service.getParametrageTransfert(
            applicationSource, IP);

      LOG.debug(modele.getUrlFTP().value);
      LOG.debug(modele.getTypeTransfert().value);
      LOG.debug(modele.getErreur().value);
      LOG.debug(modele.getChemin().value);
      LOG.debug(modele.getLogin().value);
      LOG.debug(modele.getPassword().value);
      LOG.debug(modele.getIdTransfert().value);

      // assertEquals("cer69imageint4.cer69.recouv", modele.getUrlFTP().value);
      assertNotNull(modele.getUrlFTP().value);
      assertEquals("LOCAL", modele.getTypeTransfert().value);
      assertNotNull(modele.getErreur().value);
      assertNotNull(modele.getChemin().value);
      // assertEquals("/SAEL/"+modele.getIdTransfert().value,
      // modele.getChemin().value);
      // assertEquals("ftpaed", modele.getLogin().value);
      // assertEquals("ftpaed", modele.getPassword().value);
      assertNotNull(modele.getLogin().value);
      assertNotNull(modele.getPassword().value);
      // assertTrue(!"".equals(modele.getIdTransfert().value));

   }
}
