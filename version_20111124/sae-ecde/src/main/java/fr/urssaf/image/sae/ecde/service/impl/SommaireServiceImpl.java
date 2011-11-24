package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.exception.EcdeBadSummaryException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeInvalidBatchModeException;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;
import fr.urssaf.image.sae.ecde.service.SommaireService;
import fr.urssaf.image.sae.ecde.service.strategy.impl.JaxbSommaireUnserializer;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;

/**
 * 
 * Classe concréte du service sommaire.
 * {@link SommaireService}
 * 
 */
@Service
@Qualifier("sommaireService")
public class SommaireServiceImpl implements SommaireService {

   
   @Autowired
   private EcdeFileService ecdeFileService;
   
   @Autowired
   private JaxbSommaireUnserializer jaxbSomUnserial;
   
   @Autowired
   private EcdeSources ecdeSources;
   
   private static final String UNSERIALIZE_ERROR = "unserialize.error";
   
   /**
    * {@inheritDoc}}
    */
   public final Sommaire fetchSommaireByUri(URI uri) throws EcdeGeneralException {
      try {
         
         EcdeSource[] ecdeSourcesList = ecdeSources.getSources();
         File uriFile = ecdeFileService.convertSommaireToFile(uri, ecdeSourcesList);
         
         return jaxbSomUnserial.unserializeSommaire(uriFile);
         
      } catch (EcdeInvalidBatchModeException except) {    
         throw new EcdeInvalidBatchModeException(MessageRessourcesUtils.recupererMessage("invalid.batchmode.error", null));
      } catch (Exception except) {
         throw new EcdeBadSummaryException(MessageRessourcesUtils.recupererMessage(UNSERIALIZE_ERROR, null), except);
      }   
   }
   
   
   /**
    * 
    * @return le service ecdeFile du conteneur
    */
   public final EcdeFileService getEcdeFileService() {
      return ecdeFileService;
   }
   /**
    * 
    * @param ecdeFileService set le service ecdeFile du conteneur
    */
   public final void setEcdeFileService(EcdeFileService ecdeFileService) {
      this.ecdeFileService = ecdeFileService;
   }
   /**
    * 
    * @return le service de serialisation permettant la conversion en un objet sommaire
    */
   public final JaxbSommaireUnserializer getJaxbSommaireUnserializer() {
      return jaxbSomUnserial;
   }
   /**
    * 
    * @param jaxbSomUnserial set le service de serialisation permettant la conversion en un objet sommaire
    */
   public final void setJaxbSommaireUnserializer(
         JaxbSommaireUnserializer jaxbSomUnserial) {
      this.jaxbSomUnserial = jaxbSomUnserial;
   }
   
}
