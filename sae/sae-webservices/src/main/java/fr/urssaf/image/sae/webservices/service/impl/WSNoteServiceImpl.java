package fr.urssaf.image.sae.webservices.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteResponse;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.SAEDocumentNoteException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.webservices.exception.AjoutNoteAxisFault;
import fr.urssaf.image.sae.webservices.service.WSNoteService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectNoteFactory;

/**
 * Implémentation de {@link WSNoteService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@Service
public final class WSNoteServiceImpl implements WSNoteService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSNoteServiceImpl.class);

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   @Override
   public AjoutNoteResponse ajoutNote(final AjoutNote request)
         throws AjoutNoteAxisFault {

      // Traces debug - entrée méthode
      final String prefixeTrc = "ajoutNote()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      final UUID docUuid = UUID.fromString(request.getAjoutNote().getUuid()
            .getUuidType());
      final String contenu = request.getAjoutNote().getNote().getNoteTxtType();

      // Récupération du login fourni dans l'appel du service
      String login = "";
      if (SecurityContextHolder.getContext().getAuthentication() != null) {
         final VIContenuExtrait extrait = (VIContenuExtrait) SecurityContextHolder
               .getContext().getAuthentication().getPrincipal();
         login = extrait.getIdUtilisateur();
      }

      try {
         saeService.addDocumentNote(docUuid, contenu, login);
      } catch (final SAEDocumentNoteException e) {
         throw new AjoutNoteAxisFault(e);
      } catch (final ArchiveInexistanteEx e) {
         throw new AjoutNoteAxisFault("ArchiveNonTrouvee", e.getMessage(), e);
      }

      final AjoutNoteResponse response = ObjectNoteFactory.createAjoutNoteResponse();

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);
      return response;
   }

}
