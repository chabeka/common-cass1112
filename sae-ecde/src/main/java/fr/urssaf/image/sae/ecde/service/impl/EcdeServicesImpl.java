package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.service.EcdeFileServiceWrapper;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.ecde.service.ResultatService;
import fr.urssaf.image.sae.ecde.service.SommaireService;

/**
 *  Classe d'implémentation de la facade de l'ECDE.
 * 
 */
@Service
@Qualifier("ecdeServices")
public class EcdeServicesImpl implements EcdeServices {

   @Autowired
   private SommaireService sommaireService;
   
   @Autowired
   private ResultatService resultatService;
   
   @Autowired
   private EcdeFileServiceWrapper ecdeFileSW;
   
   
   /**
    * {@inheritDoc}}
    */
   @Override
   public final URI convertFileToURI(File ecdeFile) throws EcdeBadFileException {
      return ecdeFileSW.convertFileToURI(ecdeFile);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final File convertSommaireToFile(URI sommaireURL) throws EcdeBadURLException, EcdeBadURLFormatException {
      return ecdeFileSW.convertSommaireToFile(sommaireURL);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final File convertURIToFile(URI ecdeURL) throws EcdeBadURLException, EcdeBadURLFormatException {
      return ecdeFileSW.convertURIToFile(ecdeURL);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final Sommaire fetchSommaireByUri(URI uri) throws EcdeGeneralException {
      return sommaireService.fetchSommaireByUri(uri);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final void persistResultat(Resultats resultats) throws EcdeXsdException, IOException {
      resultatService.persistResultat(resultats);
   }

}
