/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaePrmd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class PrmdServiceDatasTest {

  private static final String PRMD_2 = "PRMD_2";
  private static final String PRMD_1 = "PRMD_1";
  private static final String META_1 = "META_1";
  private static final String META_2 = "META_2";
  private static final String META_3 = "META_3";
  private static final String VALEUR_1 = "VALEUR_1";
  private static final String VALEUR_2 = "VALEUR_2";
  private static final String VALEUR_3 = "VALEUR_3";
  private static final String VALEUR_4 = "VALEUR_4";
  private static final String VALEUR_5 = "VALEUR_5";

  private static final String BEAN_NAME = "prmdControleTest";
  private static final String BEAN_NAME_INEXISTANT = "prmdControleTestinexistant";

  @Autowired
  private PrmdService prmdService;

  @Autowired
  ModeApiCqlSupport modeApiCqlSupport;

  @Before
  public void setup() throws Exception {
    modeApiCqlSupport.initTables(MODE_API.HECTOR);
  }

  @Test
  public void prmdInexistantDansStatiquesEtDynamiques() {
    final List<SaePrmd> saePrmds = new ArrayList<>();

    SaePrmd saePrmd = new SaePrmd();

    Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);

    Map<String, List<String>> metadata = new HashMap<>();
    List<String> values = new ArrayList<>();
    values.add(VALEUR_1);
    values.add(VALEUR_2);
    values.add(VALEUR_3);
    metadata.put(META_1, values);

    metadata = new HashMap<>();
    values = new ArrayList<>();
    values.add(VALEUR_3);
    values.add(VALEUR_4);
    values.add(VALEUR_5);
    metadata.put(META_2, values);
    prmd.setMetadata(metadata);

    saePrmd.setPrmd(prmd);

    saePrmds.add(saePrmd);

    saePrmd = new SaePrmd();

    prmd = new Prmd();
    prmd.setCode(PRMD_2);

    metadata = new HashMap<>();
    values = new ArrayList<>();
    values.add(VALEUR_3);
    values.add(VALEUR_4);
    metadata.put(META_1, values);
    prmd.setMetadata(metadata);

    saePrmd.setPrmd(prmd);

    saePrmds.add(saePrmd);

    final Map<String, String> dynamicParams = new HashMap<>();
    dynamicParams.put(META_1, VALEUR_1);
    dynamicParams.put(META_2, VALEUR_2);
    saePrmd.setValues(dynamicParams);

    final List<UntypedMetadata> metadatas = new ArrayList<>();
    UntypedMetadata meta = new UntypedMetadata();
    meta.setLongCode(META_1);
    meta.setValue(VALEUR_5);
    metadatas.add(meta);

    meta = new UntypedMetadata();
    meta.setLongCode(META_2);
    meta.setValue(VALEUR_1);
    metadatas.add(meta);
    final boolean permitted = prmdService.isPermitted(metadatas, saePrmds);

    Assert.assertFalse("l'autorisation doit etre refusée", permitted);
  }

  @Test
  public void prmdExistantDansStatiquesEtDynamiques() {
    final List<SaePrmd> saePrmds = new ArrayList<>();

    final SaePrmd saePrmd = new SaePrmd();

    final Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);

    final Map<String, List<String>> metadata = new HashMap<>();
    final List<String> values = new ArrayList<>();
    values.add(VALEUR_1);
    values.add(VALEUR_2);
    values.add(VALEUR_5);
    metadata.put(META_1, values);
    prmd.setMetadata(metadata);

    saePrmd.setPrmd(prmd);

    saePrmds.add(saePrmd);

    final Map<String, String> dynamicParams = new HashMap<>();
    dynamicParams.put(META_2, VALEUR_1);
    saePrmd.setValues(dynamicParams);

    final List<UntypedMetadata> metadatas = new ArrayList<>();
    UntypedMetadata meta = new UntypedMetadata();
    meta.setLongCode(META_1);
    meta.setValue(VALEUR_5);
    metadatas.add(meta);

    meta = new UntypedMetadata();
    meta.setLongCode(META_2);
    meta.setValue(VALEUR_1);
    metadatas.add(meta);

    final boolean permitted = prmdService.isPermitted(metadatas, saePrmds);

    Assert.assertTrue("l'autorisation doit etre acceptée", permitted);
  }

  @Test
  public void prmdInexistantBean() {
    final List<SaePrmd> saePrmds = new ArrayList<>();

    final SaePrmd saePrmd = new SaePrmd();

    final Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);

    prmd.setBean(BEAN_NAME_INEXISTANT);
    saePrmd.setPrmd(prmd);
    saePrmds.add(saePrmd);

    final List<UntypedMetadata> metadatas = new ArrayList<>();
    final UntypedMetadata meta = new UntypedMetadata();
    meta.setLongCode(META_1);
    meta.setValue(VALEUR_5);
    metadatas.add(meta);

    final boolean permitted = prmdService.isPermitted(metadatas, saePrmds);

    Assert.assertFalse("l'autorisation doit etre refusée", permitted);
  }

  @Test
  public void prmdExistantBean() {
    final List<SaePrmd> saePrmds = new ArrayList<>();

    final SaePrmd saePrmd = new SaePrmd();

    final Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);

    prmd.setBean(BEAN_NAME);
    saePrmd.setPrmd(prmd);
    saePrmds.add(saePrmd);

    final Map<String, String> dynamicParams = new HashMap<>();
    dynamicParams.put(META_2, VALEUR_1);
    saePrmd.setValues(dynamicParams);

    final List<UntypedMetadata> metadatas = new ArrayList<>();
    final UntypedMetadata meta = new UntypedMetadata();
    meta.setLongCode(META_1);
    meta.setValue(VALEUR_5);
    metadatas.add(meta);

    final boolean permitted = prmdService.isPermitted(metadatas, saePrmds);

    Assert.assertTrue("l'autorisation doit etre acceptée", permitted);
  }

  @Test
  public void luceneFromPrmd() {
    final List<SaePrmd> prmds = new ArrayList<>();

    SaePrmd saePrmd = new SaePrmd();
    Map<String, String> values = new HashMap<>();
    values.put(META_1, VALEUR_1);
    values.put(META_2, VALEUR_2);
    saePrmd.setValues(values);

    Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);
    prmd.setLucene(META_1 + ":<%" + META_1 + "%> AND " + META_2 + ":<%"
        + META_2 + "%>");
    saePrmd.setPrmd(prmd);

    prmds.add(saePrmd);

    saePrmd = new SaePrmd();
    values = new HashMap<>();
    values.put(META_1, VALEUR_3);
    values.put(META_2, VALEUR_4);
    values.put(META_3, VALEUR_5);
    saePrmd.setValues(values);

    prmd = new Prmd();
    prmd.setCode(PRMD_2);
    prmd.setLucene(META_1 + ":<%" + META_1 + "%> AND " + META_2 + ":<%"
        + META_2 + "%> AND " + META_3 + ":<%" + META_3 + "%>");
    saePrmd.setPrmd(prmd);

    prmds.add(saePrmd);

    final String requete = prmdService.createLucene("meta:valeur", prmds);
    final String attendue = "(meta:valeur) AND ((META_1:VALEUR_1 AND META_2:VALEUR_2)"
        + " OR (META_1:VALEUR_3 AND META_2:VALEUR_4 AND META_3:VALEUR_5))";
    Assert.assertEquals("la requete fournie doit etre correcte", attendue,
                        requete);

  }


  @Test
  public void luceneFromPrmdUnSeulPrmd() {

    // Cas particulier d'1 seul PRMD :
    // Le comportement de la construction de la requête LUCENE
    //  change légèrement, afin de ne pas ajouter de parenthèses
    //  inutiles autour de la sous-requête du PRMD.

    final List<SaePrmd> prmds = new ArrayList<>();

    final SaePrmd saePrmd = new SaePrmd();
    final Map<String, String> values = new HashMap<>();
    values.put(META_1, VALEUR_1);
    values.put(META_2, VALEUR_2);
    saePrmd.setValues(values);

    final Prmd prmd = new Prmd();
    prmd.setCode("LE_PRMD");
    prmd.setLucene("MetaPrmd:ValeurMetaPrmd");
    saePrmd.setPrmd(prmd);

    prmds.add(saePrmd);

    final String requete = prmdService.createLucene("meta:valeur", prmds);
    final String attendue = "(meta:valeur) AND (MetaPrmd:ValeurMetaPrmd)";
    Assert.assertEquals("la requete fournie doit etre correcte", attendue,
                        requete);

  }

  @Test
  public void luceneFromBean() {
    final List<SaePrmd> prmds = new ArrayList<>();

    final SaePrmd saePrmd = new SaePrmd();
    final Map<String, String> values = new HashMap<>();
    values.put(META_1, VALEUR_1);
    saePrmd.setValues(values);

    final Prmd prmd = new Prmd();
    prmd.setCode(PRMD_1);
    prmd.setBean(BEAN_NAME);
    saePrmd.setPrmd(prmd);

    prmds.add(saePrmd);

    final String requete = prmdService.createLucene("meta:valeur", prmds);
    final String attendue = "(meta:valeur) AND (prmd1:valeur1)";

    Assert.assertEquals("la requete fournie doit etre correcte", attendue,
                        requete);
  }
}
