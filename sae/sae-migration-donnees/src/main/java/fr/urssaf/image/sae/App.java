package fr.urssaf.image.sae;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.sae.batch.MigrationTraceDestinataire;
import fr.urssaf.image.sae.batch.MigrationTraceJournalEvt;
import fr.urssaf.image.sae.batch.MigrationTraceRegExploitation;
import fr.urssaf.image.sae.batch.MigrationTraceRegSecurite;
import fr.urssaf.image.sae.batch.MigrationTraceRegTechnique;

/**
 * Hello world!
 */

public class App {

  public static void main(final String[] args) {

    final String[] springConfig = {"applicationContext-cassandra-poc.xml"};
    final ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    final CassandraCQLClientFactory ccf = (CassandraCQLClientFactory) context.getBean("cassandraCQLClientFactory");

    // Trace destinataire
    final MigrationTraceDestinataire mtrdesti = context.getBean(MigrationTraceDestinataire.class);
    /*
     * mtrdesti.migrationFromThriftToCql();
     * mtrdesti.migrationFromCqlTothrift();
     */

    // Trace reg exploitation
    final MigrationTraceRegExploitation mtrex = context.getBean(MigrationTraceRegExploitation.class);
    /*
     * mtrex.migrationFromThriftToCql();
     * mtrex.migrationFromCqlToThrift();
     * mtrex.migrationIndexFromCqlToThrift();
     * mtrex.migrationIndexFromThriftToCql();
     */

    // trace reg journal
    final MigrationTraceJournalEvt mtjournal = context.getBean(MigrationTraceJournalEvt.class);
    /*
     * mtjournal.migrationFromThriftToCql();
     * mtjournal.migrationFromCqlToThrift();
     * mtjournal.migrationIndexFromCqlToThrift();
     * mtjournal.migrationIndexFromThriftToCql();
     */
    try {
       mtjournal.migrationIndexDocFromThriftToCql();
    }
    catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // trace reg Technique
    final MigrationTraceRegTechnique mtrtech = context.getBean(MigrationTraceRegTechnique.class);
    /*
     * mtrtech.migrationFromThriftToCql();
     * mtrtech.migrationFromCqlToThrift();
     * mtrtech.migrationIndexFromCqlToThrift();
     * mtrtech.migrationIndexFromThriftToCql();
     */

    // trace reg Securité
    final MigrationTraceRegSecurite mtrsecu = context.getBean(MigrationTraceRegSecurite.class);
    /*
     * mtrsecu.migrationFromThriftToCql();
     * mtrsecu.migrationFromCqlToThrift();
     * mtrsecu.migrationIndexFromCqlToThrift();
     * mtrsecu.migrationIndexFromThriftToCql();
     */

  }

}
