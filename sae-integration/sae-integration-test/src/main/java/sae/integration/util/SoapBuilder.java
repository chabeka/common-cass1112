/**
 *  TODO (ac75007394) Description du fichier
 */
package sae.integration.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;
import sae.integration.webservice.modele.RangeMetadonneeType;

/**
 * Classe facilitant la création des objets composant les requêtes SOAP du SAE
 */
public class SoapBuilder {

   private SoapBuilder() {
      // Classe statique
   }

   public static MetadonneeType buildMetadata(final String code, final String value) {
      final MetadonneeType meta = new MetadonneeType();
      meta.setCode(code);
      meta.setValeur(value);
      return meta;
   }

   public static String getInstantAsString(final Instant instant) {
      final DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);
      return dateFormater.format(instant);
   }

   public static RangeMetadonneeType buildRangeDateTimeMetadata(final String metaCode, final Instant minValue, final Instant maxValue) {
      return buildRangeMetadata(metaCode, getInstantAsString(minValue), getInstantAsString(maxValue));
   }

   public static RangeMetadonneeType buildRangeMetadata(final String metaCode, final String minValue, final String maxValue) {
      final RangeMetadonneeType range = new RangeMetadonneeType();
      range.setCode(metaCode);
      range.setValeurMin(minValue);
      range.setValeurMax(maxValue);
      return range;
   }

   public static void setMetaValue(final ListeMetadonneeType metaList, final String metaCode, final String metaValue) {
      // On regarde si la méta existe déjà dans la liste
      for (final MetadonneeType element : metaList.getMetadonnee()) {
         if (element.getCode().equals(metaCode)) {
            element.setValeur(metaValue);
            return;
         }
      }
      // la méta n'existe pas. On l'ajoute
      addMeta(metaList, metaCode, metaValue);
   }

   public static void addMeta(final ListeMetadonneeType metaList, final String metaCode, final String metaValue) {
      metaList.getMetadonnee().add(buildMetadata(metaCode, metaValue));
   }

   /**
    * @param metadonnees
    * @return
    */
   public static ListeMetadonneeCodeType extractMetaCodeList(final ListeMetadonneeType metadonnees) {
      final ListeMetadonneeCodeType result = new ListeMetadonneeCodeType();
      for (final MetadonneeType meta : metadonnees.getMetadonnee()) {
         result.getMetadonneeCode().add(meta.getCode());
      }
      return result;
   }
}
