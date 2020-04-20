package fr.urssaf.image.sae.services.capture.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureBadEcdeUrlEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureEcdeUrlFileNotFoundEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
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
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAECaptureServiceValidationTest {

  private SAECaptureService service;

  private static List<UntypedMetadata> metadatas;

  private static URI ecdeURL;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  @BeforeClass
  public static void beforeClass() {

    ecdeURL = URI
        .create("ecde://cer69-ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");

    metadatas = new ArrayList<>();
    metadatas.add(new UntypedMetadata("test", "test"));

  }

  @Before
  public void before() {
    modeApiSupport.initTables(ModeGestionAPI.MODE_API.DATASTAX);
    service = new SAECaptureService() {

      @Override
      public CaptureResult capture(final List<UntypedMetadata> metadatas,
                                   final URI ecdeURL) {

        return null;
      }

      @Override
      public CaptureResult captureBinaire(final List<UntypedMetadata> metadatas,
                                          final DataHandler content, final String fileName) {

        return null;
      }

      @Override
      public CaptureResult captureFichier(final List<UntypedMetadata> metadatas,
                                          final String path) {
        return null;
      }
    };
  }

  @Test
  public void capture_success() throws SAECaptureServiceEx,
  RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
  UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
  EmptyDocumentEx, RequiredArchivableMetadataEx,
  NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
  UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
  MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
  UnknownFormatException, UnexpectedDomainException, 
  InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

    try {
      service.capture(metadatas, ecdeURL);

    } catch (final IllegalArgumentException e) {
      fail("les arguments en entrée doivent être valides");
    }

  }

  @Test
  public void capture_failure_metadatas_null() throws SAECaptureServiceEx,
  RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
  UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
  EmptyDocumentEx, RequiredArchivableMetadataEx,
  NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
  UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
  MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
  UnknownFormatException, UnexpectedDomainException, 
  InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

    assertCapture_failure_metadatas(service, null);
    assertCapture_failure_metadatas(service, new ArrayList<UntypedMetadata>());

  }

  private static void assertCapture_failure_metadatas(
                                                      final SAECaptureService service, final List<UntypedMetadata> metadatas)
                                                          throws SAECaptureServiceEx, RequiredStorageMetadataEx,
                                                          InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
                                                          DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
                                                          RequiredArchivableMetadataEx, NotArchivableMetadataEx,
                                                          ReferentialRndException, UnknownCodeRndEx, UnknownHashCodeEx,
                                                          CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
                                                          MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
                                                          UnknownFormatException, UnexpectedDomainException, 
                                                          InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

    try {

      service.capture(metadatas, ecdeURL);

      fail("l'argument metadatas ne doit pas être renseigné");
    } catch (final IllegalArgumentException e) {
      assertEquals("message d'exception non attendu",
                   "L'argument 'metadatas' doit être renseigné ou être non null.",
                   e.getMessage());
    }

  }

  @Test
  public void capture_failure_ecdeUrl_null() throws SAECaptureServiceEx,
  RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
  UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
  EmptyDocumentEx, RequiredArchivableMetadataEx,
  NotArchivableMetadataEx, ReferentialRndException, UnknownCodeRndEx,
  UnknownHashCodeEx, CaptureBadEcdeUrlEx, CaptureEcdeUrlFileNotFoundEx,
  MetadataValueNotInDictionaryEx, ValidationExceptionInvalidFile,
  UnknownFormatException, UnexpectedDomainException, 
  InvalidPagmsCombinaisonException, CaptureExistingUuuidException {

    try {

      service.capture(metadatas, null);

      fail("l'argument ecdeURL ne doit pas être renseigné");
    } catch (final IllegalArgumentException e) {
      assertEquals("message d'exception non attendu",
                   "L'argument 'ecdeURL' doit être renseigné ou être non null.", e
                   .getMessage());
    }

  }

}
