/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.batch;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationTraceDestinataire {

  @Autowired
  private ITraceDestinataireCqlDao destinatairedao;

  @Autowired
  private TraceDestinataireSupport supportTDesti;

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    final List<TraceDestinataire> traces = supportTDesti.findAll();
    if (!traces.isEmpty()) {
      destinatairedao.saveAll(traces);
    }
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    final Iterator<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();
    if (new_traces.hasNext()) {
      supportTDesti.create(new_traces.next(), new Date().getTime());
    }
  }
}
