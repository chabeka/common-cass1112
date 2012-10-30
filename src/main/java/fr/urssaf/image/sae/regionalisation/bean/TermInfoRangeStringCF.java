package fr.urssaf.image.sae.regionalisation.bean;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

/**
 * Classe des serializer de définition de la famille de colonne
 * TermInfoRangeString
 * 
 * 
 */
public final class TermInfoRangeStringCF {

   private TermInfoRangeStringCF(){
   }
   
   public static final AnnotatedCompositeSerializer<TermInfoRangeStringKey> KEY_SERIALIZER = new AnnotatedCompositeSerializer<TermInfoRangeStringKey>(
         TermInfoRangeStringKey.class);

   public static final AnnotatedCompositeSerializer<TermInfoRangeStringColumn> COLUMN_SERIALIZER = new AnnotatedCompositeSerializer<TermInfoRangeStringColumn>(
         TermInfoRangeStringColumn.class);

   public static final ColumnFamily<TermInfoRangeStringKey, TermInfoRangeStringColumn> CF_TERM_INFO_RANGE_STRING = new ColumnFamily<TermInfoRangeStringKey, TermInfoRangeStringColumn>(
         "TermInfoRangeString", KEY_SERIALIZER, COLUMN_SERIALIZER);
}
