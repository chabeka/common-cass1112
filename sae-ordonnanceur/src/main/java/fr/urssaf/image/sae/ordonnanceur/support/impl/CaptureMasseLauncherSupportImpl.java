package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.ordonnanceur.support.TraitementLauncherSupport;
import fr.urssaf.image.sae.ordonnanceur.util.LauncherUtils;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;

/**
 * Support pour le lancement d'un traitement de capture en masse
 * 
 * 
 */
public class CaptureMasseLauncherSupportImpl implements
      TraitementLauncherSupport {

   private static final Logger LOG = LoggerFactory
         .getLogger(CaptureMasseLauncherSupportImpl.class);

   private static final String PREFIX_LOG = "ordonnanceur()";

   private final String executable;

   private final File saeConfigResource;

   private static final String XMX = "500m";

   private static final String XMS = "500m";

   /***
    * 
    * @param executable
    *           exécutable du traitement de capture en masse
    * @param saeConfigResource
    *           fichier de configuration générale du SAE
    */
   public CaptureMasseLauncherSupportImpl(String executable,
         Resource saeConfigResource) {

      Assert.notNull(executable, "'executable' is required");
      Assert.notNull(saeConfigResource, "'saeConfigResource' is required");

      this.executable = executable;
      try {
         this.saeConfigResource = saeConfigResource.getFile();
      } catch (IOException e) {

         throw new OrdonnanceurRuntimeException(
               "Erreur lors de la lecture du fichier de configuration du SAE.",
               e);
      }

   }

   @Override
   public final void lancerTraitement(JobQueue captureMasse) {

      String command = this.createCommand(captureMasse);

      LOG.info("{} - lancement du processus: {}", PREFIX_LOG, command);

      try {
         LauncherUtils.launch(command);
      } catch (IOException e) {
         throw new OrdonnanceurRuntimeException(e);
      }
   }

   protected final String createCommand(JobQueue captureMasse) {

      String idTraitement = ObjectUtils.toString(captureMasse.getIdJob());

      // remplacement de _UUID_TO_REPLACE
      String command = StringUtils.replace(this.executable, "_UUID_TO_REPLACE",
            idTraitement);

      // remplacement de _XMX_TO_REPLACE_
      command = StringUtils.replace(command, "_XMX_TO_REPLACE_", XMX);

      // remplacement de _XMS_TO_REPLACE_
      command = StringUtils.replace(command, "_XMS_TO_REPLACE_", XMS);

      // les trois arguments sont dans l'ordre
      // 1 - le nom de l'opération : traitementMasse
      // 2 - identifiant du traitement de capture en masse
      // 3 - Le chemin complet du fichier de configuration globale du SAE

      StrBuilder builder = new StrBuilder();

      builder.appendWithSeparators(new Object[] { command, "traitementMasse",
            idTraitement, saeConfigResource.getAbsolutePath() }, " ");

      return builder.toString();
   }

}
