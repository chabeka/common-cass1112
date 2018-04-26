/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.junit.Test;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationLocator;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationPort_PortType;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.NumVersionDate;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.RNDCorrespondance;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.RNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.TransCodeTemporaire;

/**
 * 
 * 
 */
public class TestAppelsWebService {

   private final static String URL_WS = "http://cer69adrn.cer69.recouv:9007/services/duplication.php?WSDL";
   private final static String VERSION = "11.2";

   private InterfaceDuplicationPort_PortType getPort() throws ServiceException {
      InterfaceDuplicationLocator locator = new InterfaceDuplicationLocator();
      locator.setInterfaceDuplicationPortEndpointAddress(URL_WS);
      InterfaceDuplicationPort_PortType port = locator
            .getInterfaceDuplicationPort();
      return port;
   }

   /**
    * Test l'appel du service PING
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public final void testPing() throws ServiceException, RemoteException {

      InterfaceDuplicationPort_PortType port = getPort();
      String value = port.ping();

      assertNotNull("valeur de retour de la méthode ping non null attendue",
            value);
   }

   /**
    * Test de la récupération du dernier numéro de version
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public final void testNumVersion() throws ServiceException, RemoteException {
      InterfaceDuplicationPort_PortType port = getPort();
      String[] listVersions = port.getLastNumVersion();

      assertNotNull("dernier numéro de version attendu non null", listVersions);
      assertEquals("2 versions attendue", 2, listVersions.length);
   }

   /**
    * Test de la récupération des types des document
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public final void testTypesDocuments() throws ServiceException,
         RemoteException {
      InterfaceDuplicationPort_PortType port = getPort();
      RNDTypeDocument types[] = port.getListeTypesDocuments(VERSION);

      assertNotNull("liste de documents non null", types);
      assertTrue("Au moins un document supporté", types.length > 0);

   }

   /**
    * Test que la liste des correspondances est non nulle et non vide
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public void testListeCorrespondances() throws ServiceException,
         RemoteException {
      InterfaceDuplicationPort_PortType port = getPort();
      RNDCorrespondance correspondances[] = port
            .getListeCorrespondances(VERSION);

      assertNotNull("liste de correspondances non null", correspondances);

   }

   /**
    * Test que la liste des codes temporaires est non nulle et non vide
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public void testCodeTemporaire() throws ServiceException, RemoteException {
      InterfaceDuplicationPort_PortType port = getPort();
      TransCodeTemporaire temps[] = port.getListeCodesTemporaires();

      assertNotNull("liste des codes temporaires non null", temps);

   }

   /**
    * Test que les versions par date retourne un tableau non nul et non vide
    * 
    * @throws ServiceException
    * @throws RemoteException
    */
   @Test
   public void test() throws ServiceException, RemoteException {
      InterfaceDuplicationPort_PortType port = getPort();
      NumVersionDate versionDate[] = port.getListeNumVersion();

      assertNotNull("liste des codes versions par date non null", versionDate);
      assertTrue("Au moins une version", versionDate.length > 0);
   }
}
