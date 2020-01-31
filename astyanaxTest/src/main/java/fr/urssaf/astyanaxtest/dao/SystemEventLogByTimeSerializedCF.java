package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class SystemEventLogByTimeSerializedCF {

   public final static AnnotatedCompositeSerializer<SystemEventLogByTimeSerializedCompositeColumnDefinition> columnSerializer = new AnnotatedCompositeSerializer<SystemEventLogByTimeSerializedCompositeColumnDefinition>(
		   SystemEventLogByTimeSerializedCompositeColumnDefinition.class);

   public final static ColumnFamily<String, SystemEventLogByTimeSerializedCompositeColumnDefinition> cf = new ColumnFamily<String, SystemEventLogByTimeSerializedCompositeColumnDefinition>(
         "SystemEventLogByTimeSerialized", StringSerializer.get(), columnSerializer);
}
