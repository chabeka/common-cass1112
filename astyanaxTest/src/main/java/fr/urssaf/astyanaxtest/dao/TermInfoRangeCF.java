package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.BytesArraySerializer;

public class TermInfoRangeCF {

   public final static AnnotatedCompositeSerializer<TermInfoRangeKey> keySerializer = new AnnotatedCompositeSerializer<TermInfoRangeKey>(
         TermInfoRangeKey.class);

   public final static AnnotatedCompositeSerializer<TermInfoRangeColumn> columnSerializer = new AnnotatedCompositeSerializer<TermInfoRangeColumn>(
         TermInfoRangeColumn.class);

   public final static ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> stringCf = new ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn>("TermInfoRangeString", keySerializer, columnSerializer);
   public final static ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> dateTimeCf = new ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn>("TermInfoRangeDatetime", keySerializer, columnSerializer);
   public final static ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn> uuidCf = new ColumnFamily<TermInfoRangeKey, TermInfoRangeColumn>("TermInfoRangeUUID", keySerializer, columnSerializer);
   
   public final static ColumnFamily<byte[], TermInfoRangeColumn> stringCfKeyAsBytes = new ColumnFamily<byte[], TermInfoRangeColumn>("TermInfoRangeString", BytesArraySerializer.get(), columnSerializer);
}
