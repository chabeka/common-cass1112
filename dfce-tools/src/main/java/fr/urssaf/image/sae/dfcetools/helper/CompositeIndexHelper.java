package fr.urssaf.image.sae.dfcetools.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Utilitaires liés aux index composites
 */
public class CompositeIndexHelper {
   private static final Logger LOGGER = LoggerFactory.getLogger(CompositeIndexHelper.class);

   public static String normalizeMetaValue(final String metaValue) {
      return StringUtils.stripAccents(metaValue.toLowerCase());
   }

   public static String getMetaValue(final CompositeIndex compositeIndex, final Document doc) {
      final List<Category> categories = compositeIndex.getCategories();
      final StringBuilder result = new StringBuilder();
      for (final Category category : categories) {
         final String metaName = category.getName();
         final String criterionValue = getMetaValue(doc, metaName);
         if (criterionValue.isEmpty()) {
            return "";
         }
         result.append(normalizeMetaValue(criterionValue));
         result.append("\0");
      }
      return result.toString();
   }

   public static String getIndexName(final CompositeIndex compositeIndex) {
      final List<Category> categories = compositeIndex.getCategories();
      final StringBuilder result = new StringBuilder();
      for (final Category category : categories) {
         final String metaName = category.getName();
         result.append(metaName);
         result.append("&");
      }
      return result.toString();
   }

   public static String getMetaValue(final Document doc, final String metaName) {
      final List<Criterion> criterions = doc.getCriterions(metaName);
      switch (metaName) {
      case "SM_ARCHIVAGE_DATE":
         return getDateMetaValue(doc.getArchivageDate());
      case "SM_CREATION_DATE":
         return getDateMetaValue(doc.getCreationDate());
      case "SM_DOCUMENT_TYPE":
         return doc.getType();
      case "SM_UUID":
         return doc.getUuid().toString().toLowerCase();
      case "SM_MODIFICATION_DATE":
         return getDateMetaValue(doc.getModificationDate());
      case "SM_FINAL_DATE":
         return getDateMetaValue(doc.getFinalDate());
      case "SM_LIFE_CYCLE_REFERENCE_DATE":
         return getDateMetaValue(doc.getLifeCycleReferenceDate());
      }
      if (criterions.isEmpty()) {
         return "";
      }
      final String criterionValue = criterions.get(0).getWordValue();
      return criterionValue;
   }

   private static String getDateMetaValue(final Date date) {
      if (date == null) {
         return "";
      }
      final String pattern = "yyyyMMddHHmmssSSS";
      final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      return sdf.format(date);
   }

   public static String getCompositeIndexValue(final String[] values) {
      final StringBuilder builder = new StringBuilder();
      for (final String value : values) {
         builder.append(normalizeMetaValue(value));
         builder.append("\0");
      }
      return builder.toString();
   }

}
