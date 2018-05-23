/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;

/**
 * TODO (AC75095028) Description du type
 */
@Service
@Qualifier("cqlServiceImpl")
public class TraceDestinataireCqlServiceImpl implements TraceDestinaireService {

  private static final String FIN_LOG = "{} - fin";

  private static final String DEBUT_LOG = "{} - début";

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(TraceDestinataireCqlServiceImpl.class);

  @Autowired
  ITraceDestinataireCqlDao destinatairecqldao;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCodeEvenementByTypeTrace(final String typeTrace) {

    final String prefix = "getCodeEvenementByTypeTrace()";
    LOGGER.debug(DEBUT_LOG, prefix);

    final Iterator<TraceDestinataire> listeTraceDestinataire = destinatairecqldao.findAllWithMapper();
    final List<String> listeCodeEvenement = new ArrayList<String>();

    while (listeTraceDestinataire.hasNext()) {
      final TraceDestinataire traceDestinataire = listeTraceDestinataire.next();
      if (traceDestinataire.getDestinataires().containsKey(typeTrace)) {
        listeCodeEvenement.add(traceDestinataire.getCodeEvt());
      }
    }

    LOGGER.debug(FIN_LOG, prefix);

    return listeCodeEvenement;
  }

}
