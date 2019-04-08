/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.indexcounterupdater;

import org.junit.Test;

/**
 * TODO (ac75007394) Description du type
 */
public class IndexCounterUpdaterTest {

   @Test
   /**
    * Traitement d'un range en mode simulation
    * 
    * @throws Exception
    */
   public void processRangeTest() throws Exception {
      final String cassandraServers = "cnp69saecas1.cer69.recouv";
      final String cassandraUsername = "root";
      final String cassandraPassword = "regina4932";
      final String cassandraLocalDC = "LYON_SP";
      final String dfceBaseName = "";
      final int maxExecutionTime = 60;
      final boolean simulationMode = true;
      final IndexCounterUpdater updater = new IndexCounterUpdater(cassandraServers,
                                                                  cassandraUsername,
                                                                  cassandraPassword,
                                                                  cassandraLocalDC,
                                                                  dfceBaseName,
                                                                  maxExecutionTime,
                                                                  simulationMode);
      updater.init();
      final String index = "isi";
      final int rangeId = 0;
      updater.processRange(index, rangeId);
   }

   @Test
   /**
    * Traitement complet en mode simulation
    * 
    * @throws Exception
    */
   public void processAllTest() throws Exception {
      final String cassandraServers = "cnp69gntcas1.cer69.recouv";
      final String cassandraUsername = "root";
      final String cassandraPassword = "regina4932";
      final String cassandraLocalDC = "LYON_SP";
      final String dfceBaseName = "";
      final int maxExecutionTime = 60;
      final boolean simulationMode = true;
      final IndexCounterUpdater updater = new IndexCounterUpdater(cassandraServers,
                                                                  cassandraUsername,
                                                                  cassandraPassword,
                                                                  cassandraLocalDC,
                                                                  dfceBaseName,
                                                                  maxExecutionTime,
                                                                  simulationMode);
      updater.start();
   }
}
