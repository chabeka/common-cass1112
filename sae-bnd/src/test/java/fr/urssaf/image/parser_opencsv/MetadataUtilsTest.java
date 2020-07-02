package fr.urssaf.image.parser_opencsv;


import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import fr.urssaf.image.parser_opencsv.application.exception.MetaFormatCSVException;
import fr.urssaf.image.parser_opencsv.jaxb.model.ListeMetadonneeType;
import fr.urssaf.image.parser_opencsv.jaxb.model.MetadonneeType;
import fr.urssaf.image.parser_opencsv.utils.MetadataUtils;

public class MetadataUtilsTest {

   private static ListeMetadonneeType listeMetadonnees;

   @Rule
   public final ExpectedException thrown = ExpectedException.none();

   private static final String META_MESSAGE_NOT_NULL = "format de la Meta dans le CSV";

   @BeforeClass
   public static void setup() {
      // Initialisation du Jeu de données
      listeMetadonnees = new ListeMetadonneeType();
      final MetadonneeType meta1 = new MetadonneeType();
      meta1.setCode("Titre");
      meta1.setValeur("Attestation de vigilance 2");

      final MetadonneeType meta2 = new MetadonneeType();
      meta2.setCode("DateCreation");
      meta2.setValeur("2011-02-08");

      final MetadonneeType meta3 = new MetadonneeType();
      meta3.setCode("ApplicationProductrice");
      meta3.setValeur("");

      final MetadonneeType meta4 = new MetadonneeType();
      meta4.setCode("CodeOrganismeProprietaire");
      meta4.setValeur("UR750");

      final MetadonneeType meta5 = new MetadonneeType();
      meta5.setCode("CodeRND");
      meta5.setValeur("2.3.1.1.12");

      final MetadonneeType meta6 = new MetadonneeType();
      meta6.setCode("Hash");
      meta6.setValeur("a2f93f1f121ebba0faef2c0596f2f126eacae77b");

      final MetadonneeType meta7 = new MetadonneeType();
      meta7.setCode("CodeOrganismeGestionnaire");
      meta7.setValeur("CER69");

      listeMetadonnees.getMetadonnee().add(meta1);
      listeMetadonnees.getMetadonnee().add(meta2);
      listeMetadonnees.getMetadonnee().add(meta3);
      listeMetadonnees.getMetadonnee().add(meta4);
      listeMetadonnees.getMetadonnee().add(meta5);
      listeMetadonnees.getMetadonnee().add(meta6);
      listeMetadonnees.getMetadonnee().add(meta7);
   }

   @Test
   public void testOneMetaWithNullCode() throws MetaFormatCSVException {

      thrown.expect(MetaFormatCSVException.class);
      thrown.expectMessage(containsString(META_MESSAGE_NOT_NULL));

      final String metaKey = "";
      final String metaValue = "ADELAIDE";

      final String metaString = metaKey + ":" + metaValue;
      MetadataUtils.convertMetaFromString(metaString);
   }

   @Test
   public void testOneMetaWithNullValue() throws MetaFormatCSVException {
      final String metaKey = "ApplicationProductrice";
      final String metaValue = "";

      final String metaString = metaKey + ":" + metaValue;
      final MetadonneeType meta = new MetadonneeType();
      meta.setCode(metaKey);
      meta.setValeur(metaValue);
      final MetadonneeType metaObject = MetadataUtils.convertMetaFromString(metaString);
      System.out.println(metaObject);
      assertEquals(meta, metaObject);
   }

   @Test
   public void testOneMetaWithEmptyString() throws MetaFormatCSVException {

      thrown.expect(MetaFormatCSVException.class);
      thrown.expectMessage(containsString(META_MESSAGE_NOT_NULL));

      final String metaString = "";
      MetadataUtils.convertMetaFromString(metaString);
   }

   @Test
   public void testOneMetaWithNull() throws MetaFormatCSVException {

      thrown.expect(MetaFormatCSVException.class);
      thrown.expectMessage(containsString(META_MESSAGE_NOT_NULL));

      final String metaString = null;
      MetadataUtils.convertMetaFromString(metaString);
   }

   @Test
   public void testOneMetaWithNotNullCode() throws MetaFormatCSVException {
      final String metaKey = "ApplicationProductrice";
      final String metaValue = "ADELAIDE";

      final String metaString = metaKey + ":" + metaValue;
      final MetadonneeType meta = new MetadonneeType();
      meta.setCode(metaKey);
      meta.setValeur(metaValue);
      final MetadonneeType metaObject = MetadataUtils.convertMetaFromString(metaString);
      System.out.println(metaObject);
      assertEquals(meta, metaObject);
   }

   @Test
   public void testListMeta() throws MetaFormatCSVException {
      final String metaListString = "Titre:Attestation de vigilance 2,"
            + "DateCreation:2011-02-08,"
            + "ApplicationProductrice:,"
            + "CodeOrganismeProprietaire:UR750,"
            + "CodeOrganismeGestionnaire:CER69,"
            + "CodeRND:2.3.1.1.12,"
            + "Hash:a2f93f1f121ebba0faef2c0596f2f126eacae77b";

      final ListeMetadonneeType metadonnees = MetadataUtils.convertListMetasFromString(metaListString);
      final boolean equal = listeMetadonnees.compareWith(metadonnees);

      assertTrue(equal);
   }

}
