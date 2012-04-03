/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.impl;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport;
import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;
import fr.urssaf.image.sae.services.util.FormatUtils;

/**
 * implémentation du support {@link DebutTraitementFlagSupport}
 * 
 */
@Component
public class DebutTraitementFlagSupportImpl implements
      DebutTraitementFlagSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DebutTraitementFlagSupportImpl.class);

   private static final String PREFIXE_TRC = "writeDebutTraitementFlag()";

   private static final String DEBUT_FLAG = "debut_traitement.flag";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeDebutTraitementFlag(final DebutTraitementFlag flag,
         final File ecdeDirectory) {

      try {

         LOGGER.debug("{} - Début", PREFIXE_TRC);

         // Ajout d'un fichier début traitement debut_traitement.flag
         // Ajout du host name du serveur qui a pris le traitement
         // UUID du traitement en masse
         LOGGER.debug("{} - Début de création du fichier ({})", PREFIXE_TRC,
               DEBUT_FLAG);

         final StringBuffer urlFlag = new StringBuffer(ecdeDirectory
               .getAbsolutePath());
         urlFlag.append(File.separator);
         urlFlag.append(DEBUT_FLAG);

         LOGGER.debug("{} - Chemin du du fichier ({}) : ({})", new Object[] {
               PREFIXE_TRC, DEBUT_FLAG, urlFlag.toString() });

         final File bulkStartFile = new File(urlFlag.toString());
         final Collection<String> bulkCaptureInfos = new ArrayList<String>();
         final InetAddress hostInfo = flag.getHostInfo();
         bulkCaptureInfos.add("idTraitementMasse=" + flag.getIdTraitement());
         bulkCaptureInfos.add("heureDebutTraitementEnMasse="
               + FormatUtils.dateToString(flag.getStartDate()));
         bulkCaptureInfos.add("hostnameServeurAppli=" + hostInfo.getHostName());
         bulkCaptureInfos.add("hostIP=" + hostInfo.getHostAddress());
         FileUtils.writeLines(bulkStartFile, bulkCaptureInfos, "\r");
         LOGGER.debug("{} - Fin de création du fichier ({})", PREFIXE_TRC,
               DEBUT_FLAG);

         LOGGER.debug("{} - Sortie", PREFIXE_TRC);
         // Fin des traces debug - sortie méthode

      } catch (IOException except) {
         throw new CaptureMasseRuntimeException(except);
      }

   }

}
