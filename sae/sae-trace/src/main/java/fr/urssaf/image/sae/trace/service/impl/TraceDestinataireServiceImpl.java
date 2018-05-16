/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.daocql.support.TraceDestinataireCqlSupport;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;

/**
 * Classe d'implémentation du support {@link ITraceDestinataireService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 */
@Service
@Qualifier("serviceImpl")
public class TraceDestinataireServiceImpl implements TraceDestinaireService {

  public TraceDestinataireSupport traceDestinataireSupport;

  public TraceDestinataireCqlSupport traceDestinataireCqlSupport;

  private static final String FIN_LOG = "{} - fin";

  private static final String DEBUT_LOG = "{} - début";

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(TraceDestinataireServiceImpl.class);

  public boolean flagCql;

  /**
   * Constructeur
   *
   * @param traceDestinataireSupport
   *          Support de la classe DAO TraceDestinataireDao
   * @param traceDestinataireCqlSupport
   *          Support de la classe DAO TraceDestinataireCqlDao
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

    if (flagCql) {
      listeTraceDestinataire = traceDestinataireSupport.findAll();
    } else {
      listeTraceDestinataire = traceDestinataireCqlSupport.findAll();
    }

    final List<String> listeCodeEvenement = new ArrayList<String>();

    for (final TraceDestinataire traceDestinataire : listeTraceDestinataire) {
      if (traceDestinataire.getDestinataires().containsKey(typeTrace)) {
        listeCodeEvenement.add(traceDestinataire.getCodeEvt());
      }
    }

    LOGGER.debug(FIN_LOG, prefix);

    return listeCodeEvenement;
  }

}
