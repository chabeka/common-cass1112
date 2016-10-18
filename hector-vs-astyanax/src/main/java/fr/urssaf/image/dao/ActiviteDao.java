package fr.urssaf.image.dao;

import java.util.List;

import fr.urssaf.image.model.Activite;

public interface ActiviteDao {

	List<Activite> findLimitedByClient(Long id);
	
	List<Activite> findByClient(Long id);
	
	void insert(Long id, Activite activite);
}
