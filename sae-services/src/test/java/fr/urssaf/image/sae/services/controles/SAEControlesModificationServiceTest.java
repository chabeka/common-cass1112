/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.modification.exception.NotModifiableMetadataEx;

public class SAEControlesModificationServiceTest extends CommonsServices {

   @Autowired
   private SAEControlesModificationService service;

   @Test(expected = IllegalArgumentException.class)
   public void testDeleteMetasObligatoires() throws NotModifiableMetadataEx {
      service.checkSaeMetadataForDelete(null);
   }

   @Test(expected = NotModifiableMetadataEx.class)
   public void testDeleteMetasNonSupprimable() throws NotModifiableMetadataEx {
      List<UntypedMetadata> list = Arrays.asList(new UntypedMetadata("Periode",
            null), new UntypedMetadata("Titre", null));
      service.checkSaeMetadataForDelete(list);
   }

   @Test
   public void testDeleteMetasSucces() {
      List<UntypedMetadata> list = Arrays.asList(new UntypedMetadata("Periode",
            null), new UntypedMetadata("CodeFonction", null));
      try {
         service.checkSaeMetadataForDelete(list);
      } catch (NotModifiableMetadataEx exception) {
         Assert.fail("erreur non attendue");
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testUpdateMetasObligatoires() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      service.checkSaeMetadataForUpdate(null);
   }

   @Test(expected = UnknownMetadataEx.class)
   public void testUpdateMetasInexistante() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "codeInexistant", null));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = DuplicatedMetadataEx.class)
   public void testUpdateMetasDupliquee() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "NumeroCompteInterne", "123456"), new UntypedMetadata("Titre",
            "ceci est le titre 2"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = InvalidValueTypeAndFormatMetadataEx.class)
   public void testUpdateTypeErrone() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "CodeFonction", "12as"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = NotArchivableMetadataEx.class)
   public void testUpdateMetasNonArchivable() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata("VersionRND",
            "12.2"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = NotModifiableMetadataEx.class)
   public void testUpdateMetasnonModifiable() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "DateDebutConservation", "2010-04-04"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

}
