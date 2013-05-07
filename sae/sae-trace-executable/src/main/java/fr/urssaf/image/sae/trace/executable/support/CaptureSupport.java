package fr.urssaf.image.sae.trace.executable.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.executable.utils.SaeFileUtils;

/**
 * Classe de support permettant de réaliser des captures de fichier
 * 
 */
@Component
public class CaptureSupport {

   @Autowired
   private SAECaptureService saeCaptureService;

   @Autowired
   private ParametersService paramService;

   /**
    * @param zipPath
    *           chemin vers le fichier à archiver
    * @param date
    *           journée concernée par la journalisation
    * @return l'identifiant du document archivé
    * @throws TraceExecutableException
    *            erreur lors de la capture du fichier
    */
   public final UUID capture(String zipPath, Date date)
         throws TraceExecutableException {

      File zipFile = new File(zipPath);

      try {
         List<UntypedMetadata> metadatas = getMetadatas(date, zipFile);
         return saeCaptureService.captureFichier(metadatas, zipPath);

      } catch (Exception exception) {
         throw new TraceExecutableException(exception);

      }

   }

   private List<UntypedMetadata> getMetadatas(Date date, File zipFile)
         throws TraceExecutableException {

      List<UntypedMetadata> list = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata;

      try {

         metadata = new UntypedMetadata("Titre", paramService
               .getJournalisationEvtMetaTitre());
         list.add(metadata);

         metadata = new UntypedMetadata("ApplicationProductrice", paramService
               .getJournalisationEvtMetaApplProd());
         list.add(metadata);

         metadata = new UntypedMetadata("CodeOrganismeProprietaire",
               paramService.getJournalisationEvtMetaCodeOrga());
         list.add(metadata);

         metadata = new UntypedMetadata("CodeOrganismeGestionnaire",
               paramService.getJournalisationEvtMetaCodeOrga());
         list.add(metadata);

         metadata = new UntypedMetadata("CodeRND", paramService
               .getJournalisationEvtMetaCodeRnd());
         list.add(metadata);

         metadata = new UntypedMetadata("ApplicationTraitement", paramService
               .getJournalisationEvtMetaApplTrait());
         list.add(metadata);

      } catch (ParameterNotFoundException exception) {
         throw new TraceExecutableException(exception);
      }

      metadata = new UntypedMetadata("DateCreation",
            DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
      list.add(metadata);

      metadata = new UntypedMetadata("Hash", SaeFileUtils
            .calculateSha1(zipFile));
      list.add(metadata);

      metadata = new UntypedMetadata("TypeHash", "SHA-1");
      list.add(metadata);

      metadata = new UntypedMetadata("NbPages", "1");
      list.add(metadata);

      metadata = new UntypedMetadata("FormatFichier", "crtl/1");
      list.add(metadata);

      metadata = new UntypedMetadata("IdTraitementMasse",
            DateFormatUtils.ISO_DATE_FORMAT.format(date));
      list.add(metadata);

      return list;
   }

}
