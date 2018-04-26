/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.service.impl;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.exception.AdrnToRcndException;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.factory.ConvertFactory;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanConfig;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationLocator;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationPort_PortType;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.RNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.service.DuplicationInterface;

/**
 * Appels Webservice correspondant à duplication
 * 
 */
@Service
public class DuplicationInterfaceImpl implements DuplicationInterface {

   @Autowired
   BeanConfig beanConfig;

   /**
    * {@inheritDoc}
    */
   @Override
   public List<BeanRNDTypeDocument> getDocumentTypesFromWS()
         throws AdrnToRcndException {

      String version = getVersionFromWS();

      if (version == null || version.isEmpty()) {
         throw new AdrnToRcndException(
               "le dernier numéro de version récupéré est null.\n "
                     + "Impossible de continuer les traitements");
      }

      return getDocuments(version);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<BeanRNDTypeDocument> getDocumentTypesFromConfigFile()
         throws AdrnToRcndException {

      return getDocuments(beanConfig.getVersion());

   }

   /**
    * Appel du WS pour une version donnée
    * 
    * @param version
    *           version désirée
    * @return la liste des documents
    * @throws AdrnToRcndException
    */
   private List<BeanRNDTypeDocument> getDocuments(String version)
         throws AdrnToRcndException {

      List<BeanRNDTypeDocument> listDoc = null;

      try {
         InterfaceDuplicationPort_PortType port = getPort();
         RNDTypeDocument[] typeDoc = port.getListeTypesDocuments(version);

         listDoc = ConvertFactory.WSToListRNDTypeDocument(typeDoc, version);

      } catch (RemoteException e) {
         throw new AdrnToRcndException(e);

      } catch (ServiceException e) {
         throw new AdrnToRcndException(e);
      }

      return listDoc;
   }

   /**
    * Retourne le port, objet permettant de réaliser les appels des méthodes
    * 
    * @return le port
    * @throws ServiceException
    *            exception d'adressage du WS
    */
   private InterfaceDuplicationPort_PortType getPort() throws ServiceException {
      InterfaceDuplicationLocator locator = new InterfaceDuplicationLocator();
      locator.setInterfaceDuplicationPortEndpointAddress(beanConfig.getUrl());
      InterfaceDuplicationPort_PortType port = locator
            .getInterfaceDuplicationPort();
      return port;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getVersionFromConfigFile() {
      return beanConfig.getVersion();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getVersionFromWS() throws AdrnToRcndException {

      String[] listVersions = null;

      try {
         InterfaceDuplicationPort_PortType port = getPort();
         listVersions = port.getLastNumVersion();

      } catch (RemoteException e) {
         throw new AdrnToRcndException(e);

      } catch (ServiceException e) {
         throw new AdrnToRcndException(e);
      }

      return listVersions[0];
   }

}
