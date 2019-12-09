/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
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
import fr.urssaf.image.sae.services.exception.modification.NotModifiableMetadataEx;
import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEControlesModificationServiceTest {

   @Autowired
   private SAEControlesModificationService service;

   /*
    * La liste des métadonnées à modifier ne doit pas être nulle
    */
   @Test(expected = IllegalArgumentException.class)
   public void testDeleteMetasObligatoires() throws NotModifiableMetadataEx,
         UnknownMetadataEx, RequiredArchivableMetadataEx {
      service.checkSaeMetadataForDelete(null);
   }

   /*
    * On ne peut pas supprimer des métas non supprimables (ie obligatoires au
    * stockage) ou qui ne sont pas modifiables
    */
   @Test(expected = RequiredArchivableMetadataEx.class)
   public void testDeleteMetasNonSupprimable() throws NotModifiableMetadataEx,
         UnknownMetadataEx, RequiredArchivableMetadataEx {
      List<UntypedMetadata> list = Arrays.asList(new UntypedMetadata("Periode",
            null), new UntypedMetadata("Titre", null));
      service.checkSaeMetadataForDelete(list);
   }

   @Test
   public void testDeleteMetasSucces() throws RequiredArchivableMetadataEx {
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
         UnknownHashCodeEx, NotModifiableMetadataEx,
         MetadataValueNotInDictionaryEx {
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
         UnknownHashCodeEx, NotModifiableMetadataEx,
         MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "codeInexistant", null));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   // CE TEST N'EST PLUS VALABLE CAR LE CONTROLE DE DUPPLICATION A ETE DEPLACE
   // (pour prendre en compte la modif et le suppression en même temps)
   // @Test(expected = DuplicatedMetadataEx.class)
   // public void testUpdateMetasDupliquee() throws ReferentialRndException,
   // UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
   // UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
   // RequiredArchivableMetadataEx, NotArchivableMetadataEx,
   // UnknownHashCodeEx, NotModifiableMetadataEx {
   // List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
   // "Titre", "ceci est le titre"), new UntypedMetadata(
   // "NumeroCompteInterne", "123456"), new UntypedMetadata("Titre",
   // "ceci est le titre 2"));
   //
   // service.checkSaeMetadataForUpdate(metadatas);
   // }

   @Test(expected = InvalidValueTypeAndFormatMetadataEx.class)
   public void testUpdateTypeErrone() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         NotModifiableMetadataEx, MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata("Periode",
            "12asr"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = NotModifiableMetadataEx.class)
   public void testUpdateMetasNonArchivable() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         NotModifiableMetadataEx, MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata("VersionRND",
            "12.2"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

   @Test(expected = NotModifiableMetadataEx.class)
   public void testUpdateMetasnonModifiable() throws ReferentialRndException,
         UnknownCodeRndEx, InvalidValueTypeAndFormatMetadataEx,
         UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx,
         RequiredArchivableMetadataEx, UnknownHashCodeEx,
         NotModifiableMetadataEx, MetadataValueNotInDictionaryEx {
      List<UntypedMetadata> metadatas = Arrays.asList(new UntypedMetadata(
            "Titre", "ceci est le titre"), new UntypedMetadata(
            "DateDebutConservation", "2010-04-04"));

      service.checkSaeMetadataForUpdate(metadatas);
   }

}
