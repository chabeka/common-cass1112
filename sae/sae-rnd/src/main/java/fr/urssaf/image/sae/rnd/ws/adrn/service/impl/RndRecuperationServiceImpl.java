package fr.urssaf.image.sae.rnd.ws.adrn.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.rnd.exception.RndRecuperationException;
import fr.urssaf.image.sae.rnd.factory.ConvertFactory;
import fr.urssaf.image.sae.rnd.modele.Configuration;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationLocator;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationPort_PortType;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument;
import fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire;
import fr.urssaf.image.sae.rnd.ws.adrn.service.RndRecuperationService;

/**
 * Service de récupération du RND à partir des WS de l'ADRN
 * 
 * 
 */
@Service
public class RndRecuperationServiceImpl implements RndRecuperationService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(RndRecuperationServiceImpl.class);

   // Durée à 3 ans (3*365 jours)
   private static final int DUREE_CONSERVATION = 1095;

   @Autowired
   private Configuration config;

   @Override
   public final List<TypeDocument> getListeCodesTemporaires()
         throws RndRecuperationException {

      String trcPrefix = "getListeCodesTemporaires";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      InterfaceDuplicationPort_PortType port;
      try {
         port = getPort();

         TransCodeTemporaire tabTransCodeTempo[] = port
               .getListeCodesTemporaires();

         List<TypeDocument> listeTypeDocs = new ArrayList<TypeDocument>();
         for (TransCodeTemporaire transCodeTemporaire : tabTransCodeTempo) {
            TypeDocument typeDoc = new TypeDocument();
            typeDoc.setCloture(false);
            typeDoc.setCode(transCodeTemporaire.getReferenceCodeTemporaire());
           
            // Durée à 3 ans (3*365 jours)
            typeDoc.setDureeConservation(DUREE_CONSERVATION);
            typeDoc.setLibelle(transCodeTemporaire.getLabelCodeTemporaire());
            typeDoc.setType(TypeCode.TEMPORAIRE);

            listeTypeDocs.add(typeDoc);
         }

         LOGGER.debug(FIN_LOG, trcPrefix);
         return listeTypeDocs;

      } catch (ServiceException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      } catch (RemoteException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public final Map<String, String> getListeCorrespondances(String version)
         throws RndRecuperationException {

      String trcPrefix = "getListeCorrespondances";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.debug("{} - Version : {}", new String[] { trcPrefix, version });

      InterfaceDuplicationPort_PortType port;
      try {
         port = getPort();

         RNDCorrespondance tabRndCorresp[] = port
               .getListeCorrespondances(version);

         Map<String, String> listeCorresp = new HashMap<String, String>();
         for (RNDCorrespondance rndCorrespondance : tabRndCorresp) {
            listeCorresp.put(rndCorrespondance.get_ctReference(),
                  rndCorrespondance.get_tdReference());
         }

         LOGGER.debug(FIN_LOG, trcPrefix);
         return listeCorresp;

      } catch (ServiceException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      } catch (RemoteException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      }

   }

   @Override
   public final List<TypeDocument> getListeRnd(String version)
         throws RndRecuperationException {

      String trcPrefix = "getListeRnd";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      LOGGER.debug("{} - Version : {}", new String[] { trcPrefix, version });

      InterfaceDuplicationPort_PortType port;
      try {
         port = getPort();
         RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(version);
         ConvertFactory convertFact = new ConvertFactory();
         List<TypeDocument> listeTypeDocuments = convertFact
               .wsToDocumentsType(typesDoc);

         LOGGER.debug(FIN_LOG, trcPrefix);
         return listeTypeDocuments;

      } catch (ServiceException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      } catch (RemoteException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public final String getVersionCourante() throws RndRecuperationException {

      String trcPrefix = "getVersionCourante";
      LOGGER.debug(DEBUT_LOG, trcPrefix);

      InterfaceDuplicationPort_PortType port;
      try {
         port = getPort();
         LOGGER.debug(FIN_LOG, trcPrefix);
         return port.getLastNumVersion()[0];

      } catch (ServiceException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      } catch (RemoteException e) {
         throw new RndRecuperationException(e.getMessage(), e.getCause());
      }
   }

   private InterfaceDuplicationPort_PortType getPort() throws ServiceException {
      InterfaceDuplicationLocator locator = new InterfaceDuplicationLocator();
      locator.setInterfaceDuplicationPortEndpointAddress(config.getUrlWsAdrn());
      InterfaceDuplicationPort_PortType port = locator
            .getInterfaceDuplicationPort();
      return port;
   }

}
