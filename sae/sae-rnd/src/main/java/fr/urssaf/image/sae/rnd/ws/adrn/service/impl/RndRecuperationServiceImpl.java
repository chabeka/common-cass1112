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

    final String trcPrefix = "getListeCodesTemporaires";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    InterfaceDuplicationPort_PortType port;
    try {
      port = getPort();

      final TransCodeTemporaire tabTransCodeTempo[] = port
          .getListeCodesTemporaires();

      final List<TypeDocument> listeTypeDocs = new ArrayList<>();
      for (final TransCodeTemporaire transCodeTemporaire : tabTransCodeTempo) {
        final TypeDocument typeDoc = new TypeDocument();
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

    } catch (final ServiceException e) {
      throw new RndRecuperationException(e);
    } catch (final RemoteException e) {
      throw new RndRecuperationException(e);
    }
  }

  @Override
  public final Map<String, String> getListeCorrespondances(final String version)
      throws RndRecuperationException {

    final String trcPrefix = "getListeCorrespondances";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    LOGGER.debug("{} - Version : {}", new String[] { trcPrefix, version });

    InterfaceDuplicationPort_PortType port;
    try {
      port = getPort();

      final RNDCorrespondance tabRndCorresp[] = port
          .getListeCorrespondances(version);

      final Map<String, String> listeCorresp = new HashMap<>();
      for (final RNDCorrespondance rndCorrespondance : tabRndCorresp) {
        listeCorresp.put(rndCorrespondance.get_ctReference(),
                         rndCorrespondance.get_tdReference());
      }

      LOGGER.debug(FIN_LOG, trcPrefix);
      return listeCorresp;

    } catch (final ServiceException e) {
      throw new RndRecuperationException(e);
    } catch (final RemoteException e) {
      throw new RndRecuperationException(e);
    }

  }

  @Override
  public final List<TypeDocument> getListeRnd(final String version)
      throws RndRecuperationException {

    final String trcPrefix = "getListeRnd";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    LOGGER.debug("{} - Version : {}", new String[] { trcPrefix, version });

    InterfaceDuplicationPort_PortType port;
    try {
      port = getPort();
      final RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(version);
      final ConvertFactory convertFact = new ConvertFactory();
      final List<TypeDocument> listeTypeDocuments = convertFact
          .wsToDocumentsType(typesDoc);

      LOGGER.debug(FIN_LOG, trcPrefix);
      return listeTypeDocuments;

    } catch (final ServiceException e) {
      throw new RndRecuperationException(e);
    } catch (final RemoteException e) {
      throw new RndRecuperationException(e);
    }
  }

  @Override
  public final String getVersionCourante() throws RndRecuperationException {

    final String trcPrefix = "getVersionCourante";
    LOGGER.debug(DEBUT_LOG, trcPrefix);

    InterfaceDuplicationPort_PortType port;
    try {
      port = getPort();
      LOGGER.debug(FIN_LOG, trcPrefix);
      return port.getLastNumVersion()[0];

    } catch (final ServiceException e) {
      throw new RndRecuperationException(e);
    } catch (final RemoteException e) {
      throw new RndRecuperationException(e);
    }
  }

  private InterfaceDuplicationPort_PortType getPort() throws ServiceException {
    final InterfaceDuplicationLocator locator = new InterfaceDuplicationLocator();
    locator.setInterfaceDuplicationPortEndpointAddress(config.getUrlWsAdrn());
    final InterfaceDuplicationPort_PortType port = locator
        .getInterfaceDuplicationPort();
    return port;
  }

}
