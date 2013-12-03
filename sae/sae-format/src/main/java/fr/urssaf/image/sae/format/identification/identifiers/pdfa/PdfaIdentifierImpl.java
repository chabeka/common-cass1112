package fr.urssaf.image.sae.format.identification.identifiers.pdfa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.commons.droid.service.impl.FormatIdentificationServiceImpl;
import fr.urssaf.image.sae.format.format.compatible.model.SaeFormatCompatible;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.identifiers.Identifier;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.model.EtapeEtResultat;
import fr.urssaf.image.sae.format.utils.Constantes;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Implémentation des appels à l’outil d’identification pour les PDF/A-1b
 * 
 */
@Service
public class PdfaIdentifierImpl implements Identifier {

   public static final String PUUID = "PUUID : ";
   public static final String PUUID_EGAL_IDFORMAT = "PUUID = IDFORMAT.";

   // objet utilisé pour permettre de recupérer le PUUID correspondant à
   // l'idFormat
   @Autowired
   private FormatIdentificationServiceImpl formatIdentificationService;

   // pour charger les différents formats compatibles.
   @Autowired
   private SaeFormatCompatible saeFormatCompatible;

   /**
    * Liste de formats compatibles avec le format de fichier défini <br>
    * dans le référentiel. Par exemple il existe plusieurs formats possibles
    * pour le PDF.
    */
   private List<String> equivalentFormat;

   @Override
   public final IdentificationResult identifyFile(String idFormat, File fichier)
         throws IOException {
      try {
         EtapeEtResultat etapeEtResultat = new EtapeEtResultat();
         List<EtapeEtResultat> listeEtapeResult = new ArrayList<EtapeEtResultat>();
         IdentificationResult identificationResult;

         // PUUID correspondant au fichier - Utilisation de DROID
         String puuid = formatIdentificationService.identifie(fichier);
         String etape1 = SaeFormatMessageHandler
               .getMessage("identify.file.etape1");
         String resultat = PUUID.concat(puuid);

         etapeEtResultat.setEtape(etape1);
         etapeEtResultat.setResultat(resultat);
         listeEtapeResult.add(etapeEtResultat);

         // comparaison du PUUID à l'IdFormat

         String etape2 = SaeFormatMessageHandler
               .getMessage("identify.file.etape2");

         if (StringUtils.equalsIgnoreCase(idFormat, puuid)) {
            String resultat2 = PUUID_EGAL_IDFORMAT;
            EtapeEtResultat etapeEtResultat2 = new EtapeEtResultat();
            etapeEtResultat2.setEtape(etape2);
            etapeEtResultat2.setResultat(resultat2);
            listeEtapeResult.add(etapeEtResultat2);

            identificationResult = new IdentificationResult();
            identificationResult.setIdentified(true);
            identificationResult.setDetails(listeEtapeResult);
         }

         else {
            // savoir si PUUID fait parti de la liste des formats compatibles
            boolean compatible = false;

            // Récupération de tous les types compatibles à partir de l'idFormat
            List<String> compatibles = saeFormatCompatible.getFormatsCompatibles(Constantes.FMT_354);
            if (!CollectionUtils.isEmpty(compatibles)) {
               compatible = compatibles.contains(idFormat) ;
            }
            
            if (compatible) {
               String resultat2 = SaeFormatMessageHandler
                     .getMessage("identify.file.puuid.diff.id.format.mais.compatible");
               EtapeEtResultat etapeEtResultat2 = new EtapeEtResultat();
               etapeEtResultat2.setEtape(etape2);
               etapeEtResultat2.setResultat(resultat2);
               listeEtapeResult.add(etapeEtResultat2);

               identificationResult = new IdentificationResult();
               identificationResult.setIdentified(true);
               identificationResult.setDetails(listeEtapeResult);
            } else {
               String resultat2 = SaeFormatMessageHandler
                     .getMessage("identify.file.puuid.diff.id.format");
               EtapeEtResultat etapeEtResultat2 = new EtapeEtResultat();
               etapeEtResultat2.setEtape(etape2);
               etapeEtResultat2.setResultat(resultat2);
               listeEtapeResult.add(etapeEtResultat2);

               identificationResult = new IdentificationResult();
               identificationResult.setIdentified(false);
               identificationResult.setDetails(listeEtapeResult);
            }
         }
         return identificationResult;

      } catch (RuntimeException except) {
         throw new IdentificationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.outil.identification"), except);
      }
   }

   @Override
   public final IdentificationResult identifyStream(String idFormat,
         InputStream stream) {

      // TODO Commons-Droid devrait proposer un service d'identification par
      // Flux
      // Pour le moment solution temporaire.

      try {
         IdentificationResult identificationResult;
         File createdFile = File.createTempFile(SaeFormatMessageHandler
               .getMessage("file.generated"), SaeFormatMessageHandler
               .getMessage("extension.file"));

         FileUtils.copyInputStreamToFile(stream, createdFile);

         stream.close();

         identificationResult = identifyFile(idFormat, createdFile);

         FileUtils.forceDelete(createdFile);

         return identificationResult;

      } catch (IOException except) {
         throw new IdentificationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.outil.identification"), except);
      }
   }

   /**
    * @return the equivalentFormat
    */
   public final List<String> getEquivalentFormat() {
      return equivalentFormat;
   }

   /**
    * @param equivalentFormat
    *           the equivalentFormat to set
    */
   public final void setEquivalentFormat(List<String> equivalentFormat) {
      this.equivalentFormat = equivalentFormat;
   }

}
