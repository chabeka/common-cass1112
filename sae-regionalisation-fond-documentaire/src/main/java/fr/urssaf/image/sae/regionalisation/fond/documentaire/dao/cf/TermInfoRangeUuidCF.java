package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

/**
 * Classe des serializer de définition de la famille de colonne
 * TermInfoRangeString
 * 
 * 
 */
public final class TermInfoRangeUuidCF {

   private TermInfoRangeUuidCF() {
   }

   public static final AnnotatedCompositeSerializer<TermInfoRangeUuidKey> KEY_SERIALIZER = new AnnotatedCompositeSerializer<TermInfoRangeUuidKey>(
         TermInfoRangeUuidKey.class);

   public static final AnnotatedCompositeSerializer<TermInfoRangeUuidColumn> COLUMN_SERIALIZER = new AnnotatedCompositeSerializer<TermInfoRangeUuidColumn>(
         TermInfoRangeUuidColumn.class);

   public static final ColumnFamily<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> CF_TERM_INFO_RANGE_UUID = new ColumnFamily<TermInfoRangeUuidKey, TermInfoRangeUuidColumn>(
         "TermInfoRangeUUID", KEY_SERIALIZER, COLUMN_SERIALIZER);
}
