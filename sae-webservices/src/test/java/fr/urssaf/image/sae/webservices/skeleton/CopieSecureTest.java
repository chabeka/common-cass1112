package fr.urssaf.image.sae.webservices.skeleton;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieRequestType;
import fr.cirtil.www.saeservice.CopieResponse;
import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.copie.SAECopieService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.webservices.exception.CopieAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class CopieSecureTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   @Qualifier("saeCopieService")
   private SAECopieService saeCopieService;

   @Test
   public void copie_success() throws ParseException, CopieAxisFault,
         SaeAccessDeniedAxisFault, SAEConsultationServiceException,
         SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx,
         ReferentialException, SAECopieServiceException, ArchiveInexistanteEx,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
         RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         EmptyDocumentEx, RequiredArchivableMetadataEx,
         NotArchivableMetadataEx, UnknownHashCodeEx, EmptyFileNameEx,
         MetadataValueNotInDictionaryEx, UnknownFormatException,
         ValidationExceptionInvalidFile, UnexpectedDomainException,
         InvalidPagmsCombinaisonException, CaptureExistingUuuidException {
      Copie request = new Copie();
      request.setCopie(new CopieRequestType());

      Map<String, Object> metadatas = new HashMap<String, Object>();

      String[] parsePatterns = new String[] { "yyyy-MM-dd" };

      metadatas.put("apr", "ADELAIDE");
      metadatas.put("cop", "CER69");
      metadatas.put("cog", "UR750");
      metadatas.put("vrn", "11.1");
      metadatas.put("dom", "2");
      metadatas.put("act", "3");
      metadatas.put("nbp", "8");
      metadatas.put("ffi", "fmt/354");
      metadatas.put("cse", "ATT_PROD_002");
      metadatas.put("dre", DateUtils.parseDate("1999-12-30", parsePatterns));
      metadatas.put("dfc", DateUtils.parseDate("2012-01-01", parsePatterns));
      metadatas.put("cot", Boolean.TRUE);
      metadatas.put("CodeRND", "2.3.1.1.12");

      List<UntypedMetadata> fin = new ArrayList<UntypedMetadata>();

      request.getCopie().setIdGed(new UuidType());
      request.getCopie().getIdGed()
            .setUuidType("00000000-0000-0000-0000-000000000000");
      request.getCopie().setMetadonnees(new ListeMetadonneeType());

      fin.add(new UntypedMetadata("NbPages", "4"));
      fin.add(new UntypedMetadata("ApplicationProductrice", "Ada"));
      fin.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));

      List<MetadonneeType> meta = convertListeMetasServiceToWebService(fin);
      MetadonneeType[] metaFinal = new MetadonneeType[meta.size()];
      metaFinal = meta.toArray(metaFinal);

      request.getCopie().getMetadonnees().setMetadonnee(metaFinal);

      UUID res = UUID.randomUUID();

      EasyMock.expect(
            saeCopieService.copie((UUID) EasyMock.anyObject(),
                  (List<UntypedMetadata>) EasyMock.anyObject())).andReturn(res);
      EasyMock.replay(saeCopieService);

      CopieResponse response = skeleton.copieSecure(request);
      assertNotNull("La reponse ne doit pas etre null", response);
      assertNotNull("L'ID de copie ne doit pas etre null", response
            .getCopieResponse().getIdGed());
   }

   private List<MetadonneeType> convertListeMetasServiceToWebService(
         List<UntypedMetadata> listeMetasService) {

      List<MetadonneeType> metadatas = new ArrayList<MetadonneeType>();

      for (UntypedMetadata untypedMetadata : CollectionUtils
            .loadListNotNull(listeMetasService)) {

         String code = untypedMetadata.getLongCode();
         String valeur = untypedMetadata.getValue();
         if (untypedMetadata.getValue() == null) {
            valeur = StringUtils.EMPTY;
         }
         MetadonneeType metadonnee = ObjectTypeFactory.createMetadonneeType(
               code, valeur);

         metadatas.add(metadonnee);
      }

      return metadatas;

   }

}
