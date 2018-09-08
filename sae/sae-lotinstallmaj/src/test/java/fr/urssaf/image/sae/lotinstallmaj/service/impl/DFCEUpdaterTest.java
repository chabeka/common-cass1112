package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import fr.urssaf.image.sae.lotinstallmaj.modele.CassandraConfig;

@RunWith(BlockJUnit4ClassRunner.class)
public class DFCEUpdaterTest {

   @Test
   @Ignore
   public void disableIndex() {
      
      // creation de l'index a supprimer
      String[] index = new String[] { "dte", "cog", "SM_ARCHIVAGE_DATE" };
      Map<String[], String> indexASupprimer = new HashMap<String[], String>();
      indexASupprimer.put(index, "oui");
      
      // config de l'environnement
      CassandraConfig config = new CassandraConfig();
      config.setHosts("cer69imageint10.cer69.recouv:9160");
      config.setLogin("root");
      config.setPassword("regina4932");
      DFCEUpdater dfceUpdater = new DFCEUpdater(config);
      
      // lancement de la suppression
      dfceUpdater.disableCompositeIndex(indexASupprimer);
   }
   
}
