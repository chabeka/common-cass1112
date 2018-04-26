package fr.urssaf.image.commons.spring.batch.service;

import fr.urssaf.image.commons.spring.batch.model.flat.Livre;

public interface ReaderService {

   void traitement(Livre livre);
}
