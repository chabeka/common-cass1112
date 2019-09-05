package sae.integration.data;

import java.util.Random;
import java.util.UUID;

import sae.integration.util.SoapBuilder;
import sae.integration.webservice.modele.ListeMetadonneeType;

/**
 * Classe utilitaire permettant de générer des données (en particulier métadonnées) de test
 */
public class RandomData {

   private static final String[] AppliTraitementList = new String[] {"SATURNE", "WATT", "SCRIBE"};

   private static final String[] AppliProductionceList = new String[] {"ADELAIDE", "CIME", "SCRIBE"};

   private static final String[] codeOrgaList = new String[] {"AC750", "UR117", "UR200", "UR217", "UR227", "UR237", "UR247", "UR257", "UR267", "UR317", "UR417",
         "UR427",
         "UR437", "UR527", "UR537", "UR547", "UR727", "UR737", "UR747", "UR827", "UR837", "UR917", "UR937",
         "CM430",
         "CM422", "CM330", "CM621", "UR971", "UR972", "UR973", "UR974", "UR976"};

   private static final String[] CodeRNDList = new String[] {"2.1.1.1.1", "1.2.2.4.12", "2.3.1.1.12", "1.2.5.F.X", "2.B.X.X.X", "3.D.X.X.X", "1.2.2.4.13",
         "3.3.1.1.2", "1.2.5.L.X", "3.1.2.1.2"};

   private RandomData() {
      // Classe statique
   }

   public static ListeMetadonneeType getRandomMetadatas() {
      final ListeMetadonneeType metaList = new ListeMetadonneeType();
      SoapBuilder.addMeta(metaList, "ApplicationProductrice", getApplicationProductrice());
      SoapBuilder.addMeta(metaList, "ApplicationTraitement", getApplicationTraitement());
      SoapBuilder.addMeta(metaList, "CodeOrganismeGestionnaire", getCodeOrga());
      SoapBuilder.addMeta(metaList, "CodeOrganismeProprietaire", getCodeOrga());
      SoapBuilder.addMeta(metaList, "CodeRND", getRND());
      SoapBuilder.addMeta(metaList, "DateCreation", getRandomDate());
      SoapBuilder.addMeta(metaList, "Titre", getTitre());
      SoapBuilder.addMeta(metaList, "Siren", getRandomNumString(10));
      SoapBuilder.addMeta(metaList, "Denomination", getRandomString(20));
      SoapBuilder.addMeta(metaList, "NumeroCompteExterne", getRandomNumString(18));
      SoapBuilder.addMeta(metaList, "Siret", getRandomNumString(14));
      return metaList;
   }

   public static ListeMetadonneeType getRandomMetadatasWithGedId() {
      final ListeMetadonneeType metaList = getRandomMetadatas();
      SoapBuilder.addMeta(metaList, "IdGed", UUID.randomUUID().toString());
      return metaList;
   }

   public static String getTitre() {
      return "Titre " + getRandomString(10);
   }

   public static String getApplicationProductrice() {
      return getElementInArray(AppliProductionceList);
   }

   public static String getApplicationTraitement() {
      return getElementInArray(AppliTraitementList);
   }

   public static String getCodeOrga() {
      return getElementInArray(codeOrgaList);
   }

   public static String getRND() {
      return getElementInArray(CodeRNDList);
   }

   public static String getElementInArray(final String[] elements) {
      final Random rnd = new Random();
      return elements[rnd.nextInt(elements.length)];
   }

   public static int getRandomNumber(final int min, final int max) {
      final Random rnd = new Random();
      return rnd.nextInt(max - min + 1) + min;
   }

   public static String getRandomNumString(final int size) {
      final Random rnd = new Random();
      final StringBuilder bld = new StringBuilder();
      for (int i = 0; i < size; i++) {
         bld.append(rnd.nextInt(10));
      }
      return bld.toString();
   }

   public static String getRandomDate() {
      return getRandomNumber(2010, 2019) + "-" +
            String.format("%02d", getRandomNumber(1, 12)) + "-" +
            String.format("%02d", getRandomNumber(1, 28));
   }

   public static String getRandomString(final int size) {
      final String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
      final int len = characters.length();
      final Random rnd = new Random();
      final StringBuilder bld = new StringBuilder();
      for (int i = 0; i < size; i++) {
         final int index = rnd.nextInt(len);
         bld.append(characters.charAt(index));
      }
      return bld.toString();
   }
}
