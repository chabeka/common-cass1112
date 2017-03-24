package fr.urssaf.image.cassandra;

import java.io.UnsupportedEncodingException;

import fr.urssaf.image.administration.modele.DataBaseModel;

public interface SAEHector {

   String createCassandraSchema(DataBaseModel dataModel)
         throws UnsupportedEncodingException;

   void deleteColumn(String keyspace, String columnFamilyName);
}
