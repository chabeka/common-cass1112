package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;
import fr.urssaf.image.sae.ecde.service.EcdeFileServiceWrapper;

/**
 * Classe d'implémentation du service EcdeFileServiceWrapper
 * 
 */
@Service
@Qualifier("ecdeFileSW")
public class EcdeFileServiceWrapperImpl implements EcdeFileServiceWrapper {

   @Autowired
   private EcdeFileService ecdeFileService;
   
   @Autowired
   private EcdeSources ecdeSources;
   
   /**
    * {@inheritDoc}}
    */
   @Override
   public final URI convertFileToURI(File ecdeFile) throws EcdeBadFileException {
      EcdeSource[] sources = ecdeSources.getSources();
      return ecdeFileService.convertFileToURI(ecdeFile, sources);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final File convertSommaireToFile(URI sommaireURL) throws EcdeBadURLException, EcdeBadURLFormatException {
      EcdeSource[] sources = ecdeSources.getSources();
      return ecdeFileService.convertSommaireToFile(sommaireURL, sources);
   }

   /**
    * {@inheritDoc}}
    */
   @Override
   public final File convertURIToFile(URI ecdeURL) throws EcdeBadURLException, EcdeBadURLFormatException {
      EcdeSource[] sources = ecdeSources.getSources();
      return ecdeFileService.convertURIToFile(ecdeURL, sources);
   }

}
