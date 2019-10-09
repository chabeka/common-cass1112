import org.junit.Test;

import fr.urssaf.image.sae.zookeepercleaner.Main;

public class MainTest {

   @Test
   public void countTest() throws Exception {
      final String[] zookeeperPaths = new String[] {"Transfert", "JobRequest"};
      Main.countNodes("hwi31picgntboappli1.gidn.recouv", zookeeperPaths);
   }

   @Test
   public void reaperTest() throws Exception {
      final String[] zookeeperPaths = new String[] {"Transfert", "JobRequest"};
      final long maxExecutionTime = 30;
      Main.startCleaning("hwi31picgntboappli1.gidn.recouv", maxExecutionTime, zookeeperPaths);
   }

}
