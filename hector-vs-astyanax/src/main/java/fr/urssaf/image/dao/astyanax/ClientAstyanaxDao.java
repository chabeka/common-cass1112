package fr.urssaf.image.dao.astyanax;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.recipes.reader.AllRowsReader;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

import fr.urssaf.image.dao.ClientDao;
import fr.urssaf.image.model.Client;

public class ClientAstyanaxDao implements ClientDao {
	
	private Keyspace keyspace;
	
	private final static ColumnFamily<Long, String> CF_CLIENTS = new ColumnFamily<Long, String>("Clients", LongSerializer.get(), StringSerializer.get(), StringSerializer.get());
	
	public ClientAstyanaxDao(Keyspace keyspace) {
		super();
		this.keyspace = keyspace;
	}

	public Client findById(Long id) {
		Client client = null;
		try {
			ColumnList<String> result = keyspace.prepareQuery(CF_CLIENTS)
					.getKey(id)
					.execute().getResult();

			if (!result.isEmpty()) {
				
				client = new Client();
				
				// recopie l'identifiant
				client.setId(id);
				
				client.setNom(result.getStringValue("Nom", "Nom-Obligatoire"));
				
				client.setPrenom(result.getStringValue("Prenom", "Prenom-Obligatoire"));
				
				client.setEmail(result.getStringValue("Email", "Email-Obligatoire"));

				// colonne facultative, donc on teste si la colonne est null ou non
				client.setNumeroCarteFidelite(result.getStringValue("NumeroCarteFidelite", null));
			}
			
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
		return client;
	}
	
   @Override
   public List<Client> findAllLimited(boolean showEmptyRow) {

      final List<Client> clients = new ArrayList<Client>();
      try {
         OperationResult<Rows<Long, String>> rows = keyspace
               .prepareQuery(CF_CLIENTS).getAllRows().setRowLimit(1000) // This
                                                                       // is the
                                                                       // page
                                                                       // size
               .setIncludeEmptyRows(showEmptyRow)
               .setExceptionCallback(new ExceptionCallback() {
                  @Override
                  public boolean onException(ConnectionException e) {
                     return true;
                  }
               }).execute();
         
         for (Row<Long, String> row : rows.getResult()) {
            
            boolean isEmptyRow = (row.getColumns() != null 
                  && row.getColumns().isEmpty());
            
            // test si on a une ligne avec tombstone ou une vrai ligne
            if (!isEmptyRow) {
               // on a une vrai ligne
               
               Client client = new Client();
               
               // recupere l'identifiant
               client.setId(row.getKey());
               
               client.setNom(row.getColumns().getStringValue("Nom", "Nom-Obligatoire"));
               
               client.setPrenom(row.getColumns().getStringValue("Prenom", "Prenom-Obligatoire"));
               
               client.setEmail(row.getColumns().getStringValue("Email", "Email-Obligatoire"));

               // colonne facultative, donc on teste si la colonne est null ou non
               client.setNumeroCarteFidelite(row.getColumns().getStringValue("NumeroCarteFidelite", null));
               
               clients.add(client);
            } else {
               // on a eu une empty row, on l'ajoute quand meme
               clients.add(null);
            }
            
         }
      } catch (ConnectionException e) {
         e.printStackTrace();
      }
      
      return clients;
   }

	@Override
	public List<Client> findAll(boolean showEmptyRow) {

		final List<Client> clients = new ArrayList<Client>();
		try {
			new AllRowsReader.Builder<Long, String>(keyspace,
					CF_CLIENTS).withPageSize(100) // Read 100 rows at a time
					.withConcurrencyLevel(10) // Split entire token range into 10. Default is by number of nodes.
					.withPartitioner(null) // this will use keyspace's partitioner
					.withIncludeEmptyRows(showEmptyRow)
					.forEachRow(new Function<Row<Long, String>, Boolean>() {
						@Override
						public Boolean apply(@Nullable Row<Long, String> row) {
							// Process the row here ...
							// This will be called from multiple threads so make
							// sure your code is thread safe
							
							if (!row.getColumns().isEmpty()) {
							
								Client client = new Client();
								
								// recupere l'identifiant
								client.setId(row.getKey());
								
								client.setNom(row.getColumns().getStringValue("Nom", "Nom-Obligatoire"));
								
								client.setPrenom(row.getColumns().getStringValue("Prenom", "Prenom-Obligatoire"));
								
								client.setEmail(row.getColumns().getStringValue("Email", "Email-Obligatoire"));
	
								// colonne facultative, donc on teste si la colonne est null ou non
								client.setNumeroCarteFidelite(row.getColumns().getStringValue("NumeroCarteFidelite", null));
								
								clients.add(client);
							} else {
								clients.add(null);
							}
							
							return true;
						}
					}).build().call();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return clients;
	}

	@Override
	public void insert(Client client) {
		
		// une insertion est une ecriture dans cassandra
		writeClient(client);
	}

	@Override
	public void update(Client client) {
		
		// un update est aussi une ecriture dans cassandra
		writeClient(client);
	}

	@Override
	public void delete(Client client) {
		
		MutationBatch m = keyspace.prepareMutationBatch();
		
		// supprime la ligne entiere (techniquement, c'est une ecriture d'un tombstone)
		m.withRow(CF_CLIENTS, client.getId())
			.delete();
		
		try {
		    m.execute();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	private void writeClient(Client client) {
		
		MutationBatch m = keyspace.prepareMutationBatch();

		m.withRow(CF_CLIENTS, client.getId())
		    .putColumn("Nom", client.getNom(), null)
		    .putColumn("Prenom", client.getPrenom(), null)
		    .putColumn("Email", client.getEmail(), null)
		    .putColumnIfNotNull("NumeroCarteFidelite", client.getNumeroCarteFidelite(), null);

		try {
		    m.execute();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
}
