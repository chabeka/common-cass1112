package fr.urssaf.image.sae.services.document.commons.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.utils.MockFactoryBean;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/***
 * Classe de test pour le service commun de capture en masse et capture
 * unitaire.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAECommonCaptureServiceImplTest {
   @Autowired
   @Qualifier("saeCommonCaptureService")
   SAECommonCaptureService saeCommonCaptureService;

   @Autowired
   private CassandraServerBean server;
   @Autowired
   private ParametersService parametersService;
   @Autowired
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   /**
    * @return Le service saeCommonCaptureService
    */
   public final SAECommonCaptureService getSaeCommonCaptureService() {
      return saeCommonCaptureService;
   }

   /**
    * @param saeCommonCaptureService
    *           : Le service saeCommonCaptureService.
    */
   public final void setSaeCommonCaptureService(
         SAECommonCaptureService saeCommonCaptureService) {
      this.saeCommonCaptureService = saeCommonCaptureService;
   }

   @Before
   public void init() {
      // initialisation du contexte de sécurité
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
      String[] roles = new String[] { "archivage_unitaire" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.4");

      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("2.3.1.1.12");
      typeDocCree.setCodeActivite("3");
      typeDocCree.setCodeFonction("2");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("Libellé 2.3.1.1.12");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);

      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

   }

   @After
   public void end() throws Exception {
      AuthenticationContext.setAuthenticationToken(null);
      server.resetData();
   }

   /**
    * Test de la méthode
    * {@link fr.urssaf.image.sae.services.document.commons.SAECommonCaptureService#buildStorageDocumentForCapture(UntypedDocument)}
    * .
    * @throws InvalidPagmsCombinaisonException 
    * @throws UnexpectedDomainException 
    */
   @Test
   public final void buildStorageDocumentForCapture()
         throws SAECaptureServiceEx, IOException, ParseException,
         SAEEnrichmentEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotArchivableMetadataEx,
         NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, MappingFromReferentialException,
         InvalidSAETypeException, UnknownHashCodeEx, ReferentialRndException,
         UnknownCodeRndEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile, 
         UnexpectedDomainException, InvalidPagmsCombinaisonException {

      UntypedDocument untypedDocument = MockFactoryBean
            .getUntypedDocumentMockData();
      CaptureResult captureResult = new CaptureResult();
      saeCommonCaptureService.buildStorageDocumentForCapture(untypedDocument,
            captureResult);
   }

}
