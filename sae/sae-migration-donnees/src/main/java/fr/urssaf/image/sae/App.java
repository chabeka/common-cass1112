package fr.urssaf.image.sae;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.batch.MigrationTraceDestinataire;
import fr.urssaf.image.sae.batch.MigrationTraceJournalEvt;
import fr.urssaf.image.sae.batch.MigrationTraceRegExploitation;

/**
 * Hello world!
 */

public class App {

  public static void main(final String[] args) {

    System.out.println("start");

    final String[] springConfig = {"applicationContext-cassandra-poc.xml"};
    final ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    final CassandraCQLClientFactory ccf = (CassandraCQLClientFactory) context.getBean("cassandraCQLClientFactory");
    // final MigrationTraceDestinataire mtd = context.getBean(MigrationTraceDestinataire.class);
    // mtd.migration_of_trace_destinataire_thrift_to_cql();

    final MigrationTraceRegExploitation mtre = context.getBean(MigrationTraceRegExploitation.class);
    // mtre.migrationFromThriftToCql();

    final MigrationTraceJournalEvt mtj = context.getBean(MigrationTraceJournalEvt.class);
    // mtj.migrationFromThriftToCql();

    final MigrationTraceDestinataire mtrd = context.getBean(MigrationTraceDestinataire.class);
    // mtrd.migrationFromThriftToCql();

    System.out.println(" end ");
  }

}
