package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

/**
 * Classe des serializer de définition de la famille de colonne
 * TermInfoRangeString
 * 
 * 
 */
public final class DocInfoCF {

   private DocInfoCF() {
   }

   public static final AnnotatedCompositeSerializer<DocInfoKey> KEY_SERIALIZER = new AnnotatedCompositeSerializer<DocInfoKey>(
         DocInfoKey.class);

   public static final StringSerializer COLUMN_SERIALIZER = StringSerializer
         .get();

   public static final ColumnFamily<DocInfoKey, String> CF_DOC_INFO = new ColumnFamily<DocInfoKey, String>(
         "DocInfo", KEY_SERIALIZER, COLUMN_SERIALIZER);
}
