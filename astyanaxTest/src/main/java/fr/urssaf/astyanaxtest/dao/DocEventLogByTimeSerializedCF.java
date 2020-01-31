package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class DocEventLogByTimeSerializedCF {

   public final static AnnotatedCompositeSerializer<DocEventLogByTimeSerializedCompositeColumnDefinition> columnSerializer = new AnnotatedCompositeSerializer<DocEventLogByTimeSerializedCompositeColumnDefinition>(
		   DocEventLogByTimeSerializedCompositeColumnDefinition.class);

   public final static ColumnFamily<String, DocEventLogByTimeSerializedCompositeColumnDefinition> cf = new ColumnFamily<String, DocEventLogByTimeSerializedCompositeColumnDefinition>(
         "DocEventLogByTimeSerialized", StringSerializer.get(), columnSerializer);
}
