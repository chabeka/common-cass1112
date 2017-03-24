package fr.urssaf.image.commons.dao.spring.exemple.service;

import java.util.List;

import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;


public interface DocumentService {

	void save(List<Document> documents);
	
}
