package fr.urssaf.image.sae.webservices.skeleton;

import java.util.List;
import java.util.UUID;

import org.apache.axis2.AxisFault;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.ListeMetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.MetadonneeValeurType;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationRequestType;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.modification.ModificationException;
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import fr.urssaf.image.sae.services.modification.SAEModificationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class ModificationTest {

   @Autowired
   private SaeServiceSkeletonInterface skeleton;

   @Autowired
   private SAEModificationService modificationService;

   @After
   public void after() {
      EasyMock.reset(modificationService);
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testModification() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx, ModificationException,
         AxisFault, ArchiveInexistanteEx, MetadataValueNotInDictionaryEx {

      Modification request = new Modification();
      ModificationRequestType type = new ModificationRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(UUID.randomUUID().toString());
      type.setUuid(uuidType);

      ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();
      MetadonneeType metadonneeType = new MetadonneeType();
      MetadonneeCodeType codeType = new MetadonneeCodeType();
      codeType.setMetadonneeCodeType("apr");
      MetadonneeValeurType valeurType = new MetadonneeValeurType();
      valeurType.setMetadonneeValeurType(null);
      metadonneeType.setCode(codeType);
      metadonneeType.setValeur(valeurType);
      listeMetadonneeType
            .setMetadonnee(new MetadonneeType[] { metadonneeType });
      type.setMetadonnees(listeMetadonneeType);

      request.setModification(type);

      modificationService.modification(EasyMock.anyObject(UUID.class), EasyMock
            .anyObject(List.class));
      EasyMock.expectLastCall().once();

      EasyMock.replay(modificationService);

      ModificationResponse modificationSecure = skeleton
            .modificationSecure(request);

      EasyMock.verify(modificationService);

      Assert.assertNotNull("la réponse doit etre non nulle", modificationSecure
            .getModificationResponse());
   }

}
