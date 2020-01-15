/**
 *   (AC75095351)
 */
package fr.urssaf.image.sae.rnd;

import java.util.Date;
import java.util.List;

import org.javers.core.diff.Diff;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.cql.RndCqlSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351) Classe de test migration des referentielFormat
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationRndTest {

  @Autowired
  private RndCqlSupport supportCql;

  @Autowired
  private RndSupport supportThrift;

  @Autowired
  MigrationRnd migrationRnd;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationRndTest.class);



  String[] listCode = {"1.2.1.1.3", "9.2.1.1.4", "8.1.2.1.5", "3.2.4.4.1", "4.2.3.1.1", "7.4.1.2.4"};

  Integer[] listCodeFonction = {1, 9, 8, 3, 4, 7};

  Integer[] listCodeActivite = {2, 2, 1, 2, 2, 4};

  String[] listLibelle = {"LIASSE AFFILIATION ACT UR COMPETENTE",
                          "DECISION SUITE A APPEL DE CANDIDATURES",
                          "JUSTIFICATIF SOCIAL ET FISCAL",
                          "SAISINE DES JURIDICTIONS EUROPEENNES",
                          "DEMANDE D'ACTION DE PREVENTION AU COTISANT",
  "TABLE DOMAINE NAME SERVEUR"};


  Integer[] listDureeConservation = {2555, 1095, 10950, 10950, 2555, 1095};

  Boolean[] listCloture = {false, false, false, false, false, true};

  TypeCode[] listType = {TypeCode.ARCHIVABLE_AED, TypeCode.ARCHIVABLE_AED, TypeCode.ARCHIVABLE_AED, TypeCode.ARCHIVABLE_AED, TypeCode.ARCHIVABLE_AED,
                         TypeCode.ARCHIVABLE_AED};


  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données Rnd vers referentielFormatcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<TypeDocument> listThrift = supportThrift.findAll();
      migrationRnd.migrationFromThriftToCql();
      final List<TypeDocument> listCql = supportCql.findAll();
      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());

      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {
    for (int i = 0; i < listCode.length; i++) {
      supportThrift.ajouterRnd(createTypeDocument(i), new Date().getTime());
    }
  }

  /**
   * Migration des données droitreferentielFormatcql vers DroitRnd
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationRnd.migrationFromCqlTothrift();
    final List<TypeDocument> listThrift = supportThrift.findAll();
    final List<TypeDocument> listCql = supportCql.findAll();

    Assert.assertTrue(!listThrift.isEmpty());
    Assert.assertTrue(!listCql.isEmpty());
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitreferentielFormatcql
   */
  private void populateTableCql() {
    for (int i = 0; i < listCode.length; i++) {
      supportCql.ajouterRnd(createTypeDocument(i));
    }
  }

  /**
   * Création de l'entité TypeDocument lié à l'indice i
   * 
   * @param i
   */
  private TypeDocument createTypeDocument(final int i) {
    final TypeDocument typeDocument = new TypeDocument();
    try {
      typeDocument.setCode(listCode[i]);
      typeDocument.setCodeFonction(String.valueOf(listCodeFonction[i]));
      typeDocument.setCodeActivite(String.valueOf(listCodeActivite[i]));
      typeDocument.setLibelle(listLibelle[i]);
      typeDocument.setDureeConservation(listDureeConservation[i]);
      typeDocument.setCloture(listCloture[i]);
      typeDocument.setType(listType[i]);
    }
    catch (final Exception e) {
      System.out.println("Exception: i=" + i + " " + e.getMessage());
    }

    return typeDocument;
  }
  
  @Test
  public void diffAddTest() {

    populateTableThrift();
    migrationRnd.migrationFromThriftToCql();

    final List<TypeDocument> listThrift = supportThrift.findAll();

    final TypeDocument typeDocument = new TypeDocument();
    typeDocument.setCode("CODEADD");
    typeDocument.setLibelle("LIBADD");
    typeDocument.setCodeFonction("LIBADD");
    supportCql.ajouterRnd(typeDocument);
    final List<TypeDocument> listCql = supportCql.findAll();
    final Diff diff = migrationRnd.compareRnds(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: TypeDocument/CODEADD }"));

  }

  @Test
  public void diffDescTest() {

    populateTableThrift();
    migrationRnd.migrationFromThriftToCql();

    final List<TypeDocument> listThrift = supportThrift.findAll();
    final List<TypeDocument> listCql = supportCql.findAll();
    listCql.get(0).setLibelle("LIBDIFF");
    final Diff diff = migrationRnd.compareRnds(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("ValueChange{ 'description' value changed from 'Ajout de notes' to 'DESCDIFF' }"));

  }
}
