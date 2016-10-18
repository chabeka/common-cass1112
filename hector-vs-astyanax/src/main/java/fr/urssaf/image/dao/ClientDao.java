package fr.urssaf.image.dao;

import java.util.List;

import fr.urssaf.image.model.Client;

/**
 * Dao pour acceder a la table des clients
 * 
 * @author Cedric
 */
public interface ClientDao {
	
	Client findById(Long id);
	
	List<Client> findAllLimited(boolean showEmptyRow);
	
	List<Client> findAll(boolean showEmptyRow);
	
	void insert(Client client);
	
	void update(Client client);
	
	void delete(Client client);
}
