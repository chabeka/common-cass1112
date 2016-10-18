package fr.urssaf.image.dao.hector;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import fr.urssaf.image.dao.ClientDao;
import fr.urssaf.image.iterator.hector.AllRowsIterator;
import fr.urssaf.image.model.Client;

public class ClientHectorDao implements ClientDao {
	
	private Keyspace keyspace;
	
	private String CF_NAME = "Clients";
	
	public ClientHectorDao(Keyspace keyspace) {
		super();
		this.keyspace = keyspace;
	}

	public Client findById(Long id) {
		SliceQuery<Long, String, String> query = HFactory.createSliceQuery(keyspace, LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
		query.setColumnFamily(CF_NAME);
		query.setColumnNames("Nom", "Prenom", "Email", "NumeroCarteFidelite");
		query.setKey(id);
		
		Client client = null;
		QueryResult<ColumnSlice<String, String>> result = query.execute();
		if (result != null && result.get() != null 
				&& result.get().getColumns() != null 
				&& !result.get().getColumns().isEmpty()) {
			client = new Client();
			
			// recopie l'identifiant
			client.setId(id);
			
			HColumn<String, String> colonne = result.get().getColumnByName("Nom");
			client.setNom(colonne.getValue());
			
			colonne = result.get().getColumnByName("Prenom");
			client.setPrenom(colonne.getValue());
			
			colonne = result.get().getColumnByName("Email");
			client.setEmail(colonne.getValue());

			// colonne facultative, donc on teste si la colonne est null ou non
			colonne = result.get().getColumnByName("NumeroCarteFidelite");
			if (colonne != null) {
				client.setNumeroCarteFidelite(colonne.getValue());
			}
		}
		return client;
	}

	public List<Client> findAllLimited(boolean showEmptyRow) {

		RangeSlicesQuery<Long, String, String> query = HFactory.createRangeSlicesQuery(keyspace, LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
		query.setColumnFamily(CF_NAME);
		query.setColumnNames("Nom", "Prenom", "Email", "NumeroCarteFidelite");
		// ne ramene que les 1000 premieres lignes
		// si on voulait ramener plus, il faudrait soit augmenter le nombre de row count, soit faire par iteration en jouant sur le setKeys et rowCount
		query.setRowCount(1000); 
		query.setKeys(null, null);
		
		QueryResult<OrderedRows<Long, String, String>> result = query.execute();
		
		List<Client> clients = null;
		if (result != null && result.get() != null && result.get().getCount() > 0) {
			clients = new ArrayList<Client>();
			
			for (Row<Long, String, String> row : result.get().getList()) {
				
				boolean isEmptyRow = (row.getColumnSlice() != null && row.getColumnSlice().getColumns() != null
						&& row.getColumnSlice().getColumns().isEmpty());
				
				// test si on a une ligne avec tombstone ou une vrai ligne
				if (!isEmptyRow) {
					// on a une vrai ligne
					
					Client client = new Client();
					
					// recupere l'identifiant
					client.setId(row.getKey());
					
					HColumn<String, String> colonne = row.getColumnSlice().getColumnByName("Nom");
					client.setNom(colonne.getValue());
					
					colonne = row.getColumnSlice().getColumnByName("Prenom");
					client.setPrenom(colonne.getValue());
					
					colonne = row.getColumnSlice().getColumnByName("Email");
					client.setEmail(colonne.getValue());

					// colonne facultative, donc on teste si la colonne est null ou non
					colonne = row.getColumnSlice().getColumnByName("NumeroCarteFidelite");
					if (colonne != null) {
						client.setNumeroCarteFidelite(colonne.getValue());
					}
					
					clients.add(client);
				} else if (showEmptyRow) {
					// on a eu une empty row, on l'ajoute quand meme
					clients.add(null);
				}
			}
		}
		return clients;
	}
	
	public List<Client> findAll(boolean showEmptyRow) {

      RangeSlicesQuery<Long, String, String> query = HFactory.createRangeSlicesQuery(keyspace, LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
      query.setColumnFamily(CF_NAME);
      query.setColumnNames("Nom", "Prenom", "Email", "NumeroCarteFidelite");
      // pas d'iteration de 100
      query.setRowCount(100); 
      query.setKeys(null, null);
      
      AllRowsIterator<Long, String, String> iterator = new AllRowsIterator<Long, String, String>(query);
      
      List<Client> clients = new ArrayList<Client>();
      
      while (iterator.hasNext()) {
         
         Row<Long, String, String> row = iterator.next();
         
         boolean isEmptyRow = (row.getColumnSlice() != null && row.getColumnSlice().getColumns() != null
               && row.getColumnSlice().getColumns().isEmpty());
         
         // test si on a une ligne avec tombstone ou une vrai ligne
         if (!isEmptyRow) {
            // on a une vrai ligne
            
            Client client = new Client();
            
            // recupere l'identifiant
            client.setId(row.getKey());
            
            HColumn<String, String> colonne = row.getColumnSlice().getColumnByName("Nom");
            client.setNom(colonne.getValue());
            
            colonne = row.getColumnSlice().getColumnByName("Prenom");
            client.setPrenom(colonne.getValue());
            
            colonne = row.getColumnSlice().getColumnByName("Email");
            client.setEmail(colonne.getValue());

            // colonne facultative, donc on teste si la colonne est null ou non
            colonne = row.getColumnSlice().getColumnByName("NumeroCarteFidelite");
            if (colonne != null) {
               client.setNumeroCarteFidelite(colonne.getValue());
            }
            
            clients.add(client);
         } else if (showEmptyRow) {
            // on a eu une empty row, on l'ajoute quand meme
            clients.add(null);
         }
      }
      return clients;
   }

	public void insert(Client client) {
		
		// une insertion est une ecriture dans cassandra
		writeClient(client);
	}
	
	public void update(Client client) {
		
		// un update est aussi une ecriture dans cassandra
		writeClient(client);
	}

	public void delete(Client client) {
		// creation du mutator 
		Mutator<Long> mutator = HFactory.createMutator(keyspace, LongSerializer.get());
		
		// supprime la ligne entiere (techniquement, c'est une ecriture d'un tombstone)
		mutator.addDeletion(client.getId(), CF_NAME);
		
		// execute les ecritures
		mutator.execute();
	}

	private void writeClient(Client client) {
		// creation du mutator 
		Mutator<Long> mutator = HFactory.createMutator(keyspace, LongSerializer.get());
		
		// Nom (obligatoire)
		HColumn<String, String> colNom = HFactory.createColumn("Nom", client.getNom(), StringSerializer.get(), StringSerializer.get());
		mutator.addInsertion(client.getId(), CF_NAME, colNom);
		
		// Prenom (obligatoire)
		HColumn<String, String> colPrenom = HFactory.createColumn("Prenom", client.getPrenom(), StringSerializer.get(), StringSerializer.get());
		mutator.addInsertion(client.getId(), CF_NAME, colPrenom);
		
		// Email (obligatoire)
		HColumn<String, String> colEmail = HFactory.createColumn("Email", client.getEmail(), StringSerializer.get(), StringSerializer.get());
		mutator.addInsertion(client.getId(), CF_NAME, colEmail);
		
		// Numero de carte fidelite (facultative)
		if (StringUtils.isNotEmpty(client.getNumeroCarteFidelite())) {
			HColumn<String, String> colCarteFidel = HFactory.createColumn("NumeroCarteFidelite", client.getNumeroCarteFidelite(), StringSerializer.get(), StringSerializer.get());
			mutator.addInsertion(client.getId(), CF_NAME, colCarteFidel);
		}
		
		// execute les ecritures
		mutator.execute();
	}

	

}
