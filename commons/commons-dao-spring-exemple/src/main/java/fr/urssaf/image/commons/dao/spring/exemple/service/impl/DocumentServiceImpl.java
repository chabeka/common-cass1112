package fr.urssaf.image.commons.dao.spring.exemple.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.exemple.dao.DocumentModifyDao;
import fr.urssaf.image.commons.dao.spring.exemple.modele.Document;
import fr.urssaf.image.commons.dao.spring.exemple.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

   @Autowired
   private DocumentModifyDao documentDao;

   @Override
   @Transactional
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public void save(List<Document> documents) {

      for (Document document : documents) {
         documentDao.save(document);
      }

   }

}
