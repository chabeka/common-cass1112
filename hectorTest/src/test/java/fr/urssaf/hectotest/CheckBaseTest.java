package fr.urssaf.hectotest;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * L'idée est de vérifier la consistance des données DFCE dans cassandra
 *
 */
public class CheckBaseTest {
	Keyspace keyspace;
	Cluster cluster;
	PrintStream sysout;
	Dumper dumper;
	
	@Before  
	public void init() throws Exception {
		ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
		ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
		ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
		HashMap<String,String> credentials = new HashMap<String, String>() {
		   
         private static final long serialVersionUID = 4010531863456382257L;

      { put("username", "root");}{ put("password", "regina4932");}};
		
		cluster = HFactory.getOrCreateCluster("Docubase", new CassandraHostConfigurator("cer69imageint9.cer69.recouv:9160" ));
		//cluster = HFactory.getOrCreateCluster("Docubase", new CassandraHostConfigurator("cer69-saeint2.cer69.recouv:9160" ));
		keyspace = HFactory.createKeyspace("Docubase", cluster, ccl, FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE, credentials);
		sysout = new PrintStream(System.out, true, "UTF-8");
		dumper = new Dumper(keyspace, sysout);
    }
    
	@After
	public void close() {
		//cluster.getConnectionManager().shutdown();
	}
	
	@Test
	public void testCheckSAETest() throws Exception {
		checkBase("SAE-TEST");
	}

	/**
	 * Lance l'ensemble des vérifications sur une base DFCE
	 * @param baseName nom de la base à checker
	 * @throws Exception
	 */
	private void checkBase(String baseName) throws Exception {
		sysout.println("Checking categories...");
		checkCategories(baseName);
		sysout.println("Checking documents...");
		checkDocuments(baseName);
	}

	
	private void checkDocuments(String baseName) {
		// TODO 
	}

	/**
	 * On vérifie que les catégories de la base baseName sont bien à la fois dans
	 * les CF BasesReference et
	 * @param baseName
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
   private void checkCategories(String baseName) throws Exception {
		List<String> categories1 = getCategoriesFromBasesReference(baseName);
		List<String> categories2 = getCategoriesFromBaseCategoriesReference(baseName);
		Collection c = CollectionUtils.disjunction(categories1, categories2);
		Assert.assertTrue(c.isEmpty());
	}
	
	/**
	 * Récupère la liste des catégories associées à la base "baseName", en se basant
	 * sur la CF BasesReference et BaseCategoriesReference
	 * @param baseName
	 * @return
	 * @throws Exception
	 */
	private List<String> getCategoriesFromBasesReference(String baseName) throws Exception {
		
		List<String> categories = new ArrayList<String>();
		StringSerializer stringSerializer = StringSerializer.get();
		BytesArraySerializer  bytesSerializer = BytesArraySerializer.get();
		CqlQuery<byte[],String,byte[]> cqlQuery = new CqlQuery<byte[],String,byte[]>(keyspace, bytesSerializer, stringSerializer, bytesSerializer);
		String query = "select * from BasesReference where Key ='" +  ConvertHelper.stringToHex(baseName) + "'";
		cqlQuery.setQuery(query);
		QueryResult<CqlRows<byte[],String,byte[]>> result = cqlQuery.execute();
		CqlRows<byte[], String, byte[]> orderedRows = result.get();
		Row<byte[], String, byte[]> row = orderedRows.peekLast();
		ColumnSlice<String, byte[]> columnSlice = row.getColumnSlice();
		List<HColumn<String, byte[]>> columns = columnSlice.getColumns();
		for (HColumn<String, byte[]> column : columns) {
			String name = column.getName();
			byte[] value =  column.getValue();
			String stringValue = ConvertHelper.getReadableUTF8String(value);
			if (name.startsWith("baseCategoryReferences.")) {
				char c = name.charAt(23);
				if (c >= '0' && c <= '9') {
					if (stringValue.startsWith(baseName + "\\xef\\xbf\\xbf")) {
						String s = stringValue.substring(baseName.length() + 12);
						categories.add(s);
					}
					else {
						throw new Exception("Valeur inattendue :" + stringValue);
					}
				}
			}
		}
		return categories;
	}

	/**
	 * Récupère la liste des catégories associées à la base "baseName", en se basant
	 * sur la CF BaseCategoriesReference
	 * @param baseName
	 * @return
	 * @throws Exception
	 */
	private List<String> getCategoriesFromBaseCategoriesReference(String baseName) throws Exception {
		
		List<String> categories = new ArrayList<String>();
		StringSerializer stringSerializer = StringSerializer.get();
		BytesArraySerializer  bytesSerializer = BytesArraySerializer.get();
		CqlQuery<byte[],String,byte[]> cqlQuery = new CqlQuery<byte[],String,byte[]>(keyspace, bytesSerializer, stringSerializer, bytesSerializer);
		String query = "select * from BaseCategoriesReference where Key > '" + ConvertHelper.stringToHex(baseName) + "efbfbf'" +
		   " and KEY < '" + ConvertHelper.stringToHex(baseName) + "ffff'";
		
		cqlQuery.setQuery(query);
		QueryResult<CqlRows<byte[],String,byte[]>> result = cqlQuery.execute();
		CqlRows<byte[], String, byte[]> orderedRows = result.get();
		
		for (Row<byte[], String, byte[]> row : orderedRows) {
			String key = ConvertHelper.getReadableUTF8String(row.getKey());
			String categorie;
			if (key.startsWith(baseName + "\\xef\\xbf\\xbf")) {
				categorie = key.substring(baseName.length() + 12);
			}
			else {
				throw new Exception("Clé inattendue : " + key);
			}
			
			ColumnSlice<String, byte[]> columnSlice = row.getColumnSlice();
			List<HColumn<String, byte[]>> columns = columnSlice.getColumns();
			String baseId = getColumnValue(columns, "baseId");
			if (!baseId.equals(baseName)) throw new Exception("Base id inattendu : " + baseId);
			String categoryReference = getColumnValue(columns, "categoryReference");
			if (!categoryReference.equals(categorie)) throw new Exception("categoryReference inattendu : " + categoryReference);
			categories.add(categorie);
		}		
		return categories;
	}

	/**
	 * Récupère la valeur de la colonne dont le nom est columnName
	 * @param columns
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	private String getColumnValue(List<HColumn<String, byte[]>> columns, String columnName) throws Exception {
		for (HColumn<String, byte[]> column : columns) {
			String name = column.getName();
			if (name.equals(columnName)) {
				byte[] value =  column.getValue();
				String stringValue = ConvertHelper.getReadableUTF8String(value);
				return stringValue;
			}
		}
		return null;
	}
}
