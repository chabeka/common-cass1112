/**
 * 
 */
package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteRequestType;
import fr.cirtil.www.saeservice.NoteTxtType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.webservices.exception.AjoutNoteAxisFault;

/**
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
public class WSNoteServiceImplTest {

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   @Autowired
   private WSNoteServiceImpl noteService;
   
   @After
   public void clean() {
      EasyMock.reset(saeService);
   }

   @Test(expected = AjoutNoteAxisFault.class)
   public void ajoutNote_erreur_contenu_vide() throws SAEDocumentNoteException,
         ArchiveInexistanteEx, AjoutNoteAxisFault {

      AjoutNote request = creationRequest();

      saeService.addDocumentNote(EasyMock.anyObject(UUID.class),
            (String) EasyMock.anyObject(), (String) EasyMock.anyObject());
      EasyMock.expectLastCall().andThrow(
            new ArchiveInexistanteEx("test unitaire: ArchiveInexistanteEx"));
      EasyMock.replay(saeService);

      noteService.ajoutNote(request);

      EasyMock.reset(saeService);
   }

   @Test(expected = AjoutNoteAxisFault.class)
   public void ajoutNote_AjoutNoteException() throws SAEDocumentNoteException,
         ArchiveInexistanteEx, AjoutNoteAxisFault {

      AjoutNote request = creationRequest();

      saeService.addDocumentNote(EasyMock.anyObject(UUID.class),
            (String) EasyMock.anyObject(), (String) EasyMock.anyObject());
      EasyMock.expectLastCall().andThrow(
            new SAEDocumentNoteException(
                  "test unitaire : SAEDocumentNoteException"));
      EasyMock.replay(saeService);

      noteService.ajoutNote(request);

      EasyMock.reset(saeService);
   }

   private AjoutNote creationRequest() {
      AjoutNote request = new AjoutNote();
      request.setAjoutNote(new AjoutNoteRequestType());
      request.getAjoutNote().setNote(new NoteTxtType());
      request.getAjoutNote().getNote().setNoteTxtType("Contenu de la note");
      request.getAjoutNote().setUuid(new UuidType());
      request.getAjoutNote().getUuid().setUuidType(
            "00000000-0000-0000-0000-000000000000");
      return request;
   }
}
