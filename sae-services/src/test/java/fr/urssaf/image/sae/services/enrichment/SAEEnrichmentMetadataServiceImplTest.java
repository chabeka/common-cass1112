package fr.urssaf.image.sae.services.enrichment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.controles.SAEControlesCaptureService;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.utils.MockFactoryBean;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEEnrichmentMetadataServiceImplTest {

   @Autowired
   @Qualifier("saeEnrichmentMetadataService")
   private SAEEnrichmentMetadataService saeEnrichmentMetadataService;

   @Autowired
   @Qualifier("saeControlesCaptureService")
   private SAEControlesCaptureService controlesCaptureService;

   @Autowired
   private MappingDocumentService mappingService;

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private ParametersService parametersService;

   /**
    * @return Le service de mappingService
    */
   public final MappingDocumentService getMappingService() {
      return mappingService;
   }

   /**
    * @param mappingService
    *           : Le service de mappingService.
    */
   public final void setMappingService(MappingDocumentService mappingService) {
      this.mappingService = mappingService;
   }

   /**
    * @return Le service d'enrichment des metadonnées.
    */
   public final SAEEnrichmentMetadataService getSaeEnrichmentMetadataService() {
      return saeEnrichmentMetadataService;
   }

   /**
    * @param saeEnrichmentMetadataService
    *           the saeEnrichmentMetadataService to set
    */
   public final void setSaeEnrichmentMetadataService(
         SAEEnrichmentMetadataService saeEnrichmentMetadataService) {
      this.saeEnrichmentMetadataService = saeEnrichmentMetadataService;
   }

   /**
    * Préparation données pour le RND
    */
   @Before
   public final void preparationDonnees() {
      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.4");
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test
   public final void enrichmentMetadata() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, InvalidSAETypeException,
         MappingFromReferentialException, ReferentialRndException,
         UnknownCodeRndEx, RequiredStorageMetadataEx {

      initDroits();

      SAEDocument saeDocument = mappingService
            .untypedDocumentToSaeDocument(MockFactoryBean
                  .getUntypedDocumentMockData());
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
      Assert.assertNotNull(saeDocument);
      controlesCaptureService.checkSaeMetadataForStorage(saeDocument);
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.enrichment.impl.SAEEnrichmentMetadataServiceImpl#enrichmentMetadata(SAEDocument)}
    * .
    */
   @Test(expected = UnknownCodeRndEx.class)
   public final void enrichmentMetadataFailed() throws SAECaptureServiceEx,
         IOException, ParseException, SAEEnrichmentEx, ReferentialRndException,
         UnknownCodeRndEx {
      SAEDocument saeDocument = MockFactoryBean.getSAEDocumentMockData();
      for (SAEMetadata saeMetadata : saeDocument.getMetadatas()) {
         if (saeMetadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_RND.getLongCode())) {
            saeMetadata.setValue("121212");
            break;
         }
      }
      saeEnrichmentMetadataService.enrichmentMetadata(saeDocument);
   }

   @After
   public void end() throws Exception {
      AuthenticationContext.setAuthenticationToken(null);
      server.resetData();
   }

   private void initDroits() {
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "capture_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("capture_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
}
