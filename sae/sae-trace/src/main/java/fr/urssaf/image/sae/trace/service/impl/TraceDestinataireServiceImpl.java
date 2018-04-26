/**
 * 
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.TraceDestinataire;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.service.TraceDestinaireService;

/**
 * Classe d'implémentation du support {@link TraceDestinataireService}. Cette
 * classe est un singleton et peut être accessible par le mécanisme d'injection
 * IOC avec l'annotation @Autowired
 * 
 */
@Service
public class TraceDestinataireServiceImpl implements TraceDestinaireService {

   private static final String FIN_LOG = "{} - fin";
   private static final String DEBUT_LOG = "{} - début";
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraceDestinataireServiceImpl.class);

   private TraceDestinataireSupport destSupport;

   /**
    * Constructeur
    * 
    * @param destSupport
    *           Support de la classe DAO TraceRegTechniqueDao
    */
   @Autowired
   public TraceDestinataireServiceImpl(TraceDestinataireSupport destSupport) {
      super();
      this.destSupport = destSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<String> getCodeEvenementByTypeTrace(String typeTrace) {

      String prefix = "getCodeEvenementByTypeTrace()";
      LOGGER.debug(DEBUT_LOG, prefix);

      List<TraceDestinataire> listeTraceDestinataire = destSupport.findAll();
      List<String> listeCodeEvenement = new ArrayList<String>();

      for (TraceDestinataire traceDestinataire : listeTraceDestinataire) {

         if (traceDestinataire.getDestinataires().containsKey(typeTrace)) {
            listeCodeEvenement.add(traceDestinataire.getCodeEvt());
         }
      }

      LOGGER.debug(FIN_LOG, prefix);

      return listeCodeEvenement;

   }

}
