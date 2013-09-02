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

   /*
    * La liste des métadonnées à modifier ne doit pas être nulle
    */
   @Test(expected = IllegalArgumentException.class)
   public void testDeleteMetasObligatoires() throws NotModifiableMetadataEx,
         UnknownMetadataEx {
      service.checkSaeMetadataForDelete(null);
   }

   /*
    * On ne peut pas supprimer des métas non supprimables (ie obligatoires au
    * stockage) ou qui ne sont pas modifiables
    */
   @Test(expected = NotModifiableMetadataEx.class)
   public void testDeleteMetasNonSupprimable() throws NotModifiableMetadataEx,
         UnknownMetadataEx {
      List<UntypedMetadata> list = Arrays.asList(new UntypedMetadata("Periode",
            null), new UntypedMetadata("Titre", null));
      service.checkSaeMetadataForDelete(list);
   }

   @Test
   public void testDeleteMetasSucces() {
      List<UntypedMetadata> list = Arrays.asList(new UntypedMetadata("Periode",
            null), new UntypedMetadata("Siren", null));
      try {
         service.checkSaeMetadataForDelete(list);
      } catch (NotModifiableMetadataEx exception) {
         Assert.fail("erreur non attendue");
      } catch (UnknownMetadataEx e) {
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

   /*
    * On vérifie qu'une métadonnée inexistante ne peut pas être vidée
    */
   @Test(expected = UnknownMetadataEx.class)
   public void testUpdateMetasInexistanteDelete()
         throws ReferentialRndException, UnknownCodeRndEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, NotModifiableMetadataEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "codeInexistant", null));

      service.checkSaeMetadataForDelete(metadatas);
   }

   /*
    * On vérifie qu'une métadonnée inexistante ne peut pas être modifiée
    */
   @Test(expected = UnknownMetadataEx.class)
   public void testUpdateMetasInexistanteUpdate()
         throws ReferentialRndException, UnknownCodeRndEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx,
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
            "Titre", "ceci est le titre"), new UntypedMetadata("CodeFonction",
            "12as"));

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
