/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.ContratServiceNotFoundException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaeDroitsEtFormat;
import fr.urssaf.image.sae.droit.model.SaePrmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-permit-all-test.xml" })
public class PrmdServicePermitAllTest {

   private static final String PRMD_1 = "PRMD_1";
   private static final String META_1 = "META_1";
   private static final String META_2 = "META_2";
   private static final String VALEUR_1 = "VALEUR_1";
   private static final String VALEUR_5 = "VALEUR_5";

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private SaeDroitService droitService;

   @Test
   public void prmdAutoriseTout() {
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();

      SaePrmd saePrmd = new SaePrmd();

      Prmd prmd = new Prmd();
      prmd.setCode(PRMD_1);

      prmd.setBean("permitAll");

      saePrmd.setPrmd(prmd);

      saePrmds.add(saePrmd);

      Map<String, String> dynamicParams = new HashMap<String, String>();
      saePrmd.setValues(dynamicParams);

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      UntypedMetadata meta = new UntypedMetadata();
      meta.setLongCode(META_1);
      meta.setValue(VALEUR_5);
      metadatas.add(meta);

      meta = new UntypedMetadata();
      meta.setLongCode(META_2);
      meta.setValue(VALEUR_1);
      metadatas.add(meta);
      boolean permitted = prmdService.isPermitted(metadatas, saePrmds);

      Assert.assertTrue("l'autorisation doit etre Acceptée", permitted);
   }

   @Test
   public void testLoadSaeDroits() throws ContratServiceNotFoundException,
         PagmfNotFoundException, FormatControlProfilNotFoundException {

      SaeDroitsEtFormat saeDroitsEtFormat = droitService.loadSaeDroits(META_1, Arrays
            .asList("TOUS"));
      Assert.assertNotNull(saeDroitsEtFormat);
      SaeDroits saeDroits = saeDroitsEtFormat.getSaeDroits();
      
//      SaeDroits saeDroits = droitService.loadSaeDroits(META_1, Arrays
//            .asList("TOUS"));

      Assert.assertEquals("4 actions unitaires doivent etre présentes", 4,
            saeDroits.size());
      List<String> actions = Arrays.asList(new String[] { "consultation",
            "recherche", "archivage_unitaire", "archivage_masse" });
      for (String key : saeDroits.keySet()) {
         Assert.assertTrue("la clé " + key
               + " doit faire partie des actions disponibles", actions
               .contains(key));
         Assert.assertEquals("1 seul PRMD rattaché à l'action " + key, 1,
               saeDroits.get(key).size());
         Assert.assertEquals("prmd_default doit être le PRMD présent",
               "prmd_default", saeDroits.get(key).get(0).getPrmd().getCode());
      }

   }
}
