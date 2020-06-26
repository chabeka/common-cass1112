package fr.urssaf.image.sae.lotinstallmaj.cql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

public class UtilsColunmFalmilly {

	
	// Methodes utilitaires
	
	public static List<String> getTablesNames(Session session, String keySpaceName){
	   Metadata metadata =session.getCluster().getMetadata();
	   List<String> tablesNames = new ArrayList<>();
	   
	   Collection<TableMetadata> tablesMetadata= metadata.getKeyspace(keySpaceName).getTables();
	    for(TableMetadata tm:tablesMetadata){
	    	tablesNames.add(tm.getName());
	    }
	    return tablesNames;
	}
	
	public static List<String> getTableColunmNames(Session session, String keySpaceName, String name){
	   Metadata metadata =session.getCluster().getMetadata();
	   List<String> colunmNames = new ArrayList<>();
	   
	   Collection<TableMetadata> tablesMetadata= metadata.getKeyspace(keySpaceName).getTables();
	    for(TableMetadata tm:tablesMetadata){
	        Collection<ColumnMetadata> columnsMetadata=tm.getColumns(); 
	        if(tm.getName().equals(name)) {
		        for(ColumnMetadata cm:columnsMetadata){
		            String columnName=cm.getName();
		            colunmNames.add(columnName);
		        }
	        }
	    }
	    return colunmNames;
	}
}
