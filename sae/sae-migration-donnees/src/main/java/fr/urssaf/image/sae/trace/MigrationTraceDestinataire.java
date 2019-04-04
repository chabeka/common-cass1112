/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

   private static final Logger LOGGER = LoggerFactory.getLogger(MigrationTraceDestinataire.class);

   /**
    * Migration de la CF Thrift vers la CF cql
    */
   public void migrationFromThriftToCql() {

      LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- start ");

      final List<TraceDestinataire> traces = supportTDesti.findAll();
      if (!traces.isEmpty()) {
         destinatairedao.saveAll(traces);
      }

      LOGGER.info(" MigrationTraceDestinataire - migrationFromThriftToCql- end ");
   }

   /**
    * Migration de la CF cql vers la CF Thrift
    */
   public void migrationFromCqlTothrift() {

      LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- start ");

      final Iterator<TraceDestinataire> new_traces = destinatairedao.findAllWithMapper();
      while (new_traces.hasNext()) {
         final TraceDestinataire nextTrace = new_traces.next();
         supportTDesti.create(nextTrace, new Date().getTime());
      }

      LOGGER.info(" MigrationTraceDestinataire - migrationFromCqlTothrift- end ");
   }
}
