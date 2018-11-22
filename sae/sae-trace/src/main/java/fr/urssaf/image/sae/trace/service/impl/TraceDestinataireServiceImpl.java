/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;

/**
 * Classe d'implémentation du support {@link ITraceDestinataireService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 */
@Service
public class TraceDestinataireServiceImpl implements TraceDestinaireService {

  private final String cfName = "tracedestinataire";

  public TraceDestinataireSupport traceDestinataireSupport;

  public TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  private static final String FIN_LOG = "{} - fin";

  private static final String DEBUT_LOG = "{} - début";

  private static final Logger LOGGER = LoggerFactory
      .getLogger(TraceDestinataireServiceImpl.class);

  /**
   * Constructeur
   * 
   * @param traceDestinataireSupport
   *           Support de la classe DAO TraceDestinataireDao
   * @param traceDestinataireCqlSupport
   *           Support de la classe DAO TraceDestinataireCqlDao
   */
  @Autowired
  public TraceDestinataireServiceImpl(final TraceDestinataireSupport traceDestinataireSupport,
                                      final TraceDestinataireCqlSupport traceDestinataireCqlSupport) {
    this.traceDestinataireSupport = traceDestinataireSupport;
    this.traceDestinataireCqlSupport = traceDestinataireCqlSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCodeEvenementByTypeTrace(final String typeTrace) {

    final String prefix = "getCodeEvenementByTypeTrace()";
    LOGGER.debug(DEBUT_LOG, prefix);

    List<TraceDestinataire> listeTraceDestinataire = new ArrayList<>();
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);

    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      listeTraceDestinataire = traceDestinataireCqlSupport.findAll();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      listeTraceDestinataire = traceDestinataireSupport.findAll();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }

    final List<String> listeCodeEvenement = new ArrayList<>();

    for (final TraceDestinataire traceDestinataire : listeTraceDestinataire) {
      if (traceDestinataire.getDestinataires().containsKey(typeTrace)) {
        listeCodeEvenement.add(traceDestinataire.getCodeEvt());
      }
    }

    LOGGER.debug(FIN_LOG, prefix);

    return listeCodeEvenement;

  }

}
