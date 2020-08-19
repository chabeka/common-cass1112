package fr.urssaf.image.parser_opencsv.application.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.parser_opencsv.application.constantes.FileConst;
import fr.urssaf.image.parser_opencsv.application.constantes.Metadata;
import fr.urssaf.image.parser_opencsv.application.dao.ICorrespondanceTableSSTIGedDao;
import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceException;
import fr.urssaf.image.parser_opencsv.application.exception.CorrespondanceFormatException;
import fr.urssaf.image.parser_opencsv.application.exception.CountNbrePageFileException;
import fr.urssaf.image.parser_opencsv.application.model.CorrespondanceMetaObject;
import fr.urssaf.image.parser_opencsv.application.service.ICorrespondanceService;
import fr.urssaf.image.parser_opencsv.jaxb.model.DocumentType;
import fr.urssaf.image.parser_opencsv.jaxb.model.FichierType;
import fr.urssaf.image.parser_opencsv.jaxb.model.MetadonneeType;
import fr.urssaf.image.parser_opencsv.utils.FileUtils;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;

/**
 * Implémentation du service permettant de réaliser les correspondances entre
 * les méta SSTI et GED
 */
@Service
public class CorrespondanceServiceImpl implements ICorrespondanceService {

   private final ReferentielFormatService referentielFormatService;

   private final ICorrespondanceTableSSTIGedDao matcher;

   private final Map<String, FormatFichier> mapFormats;
   
   private final Map<String, String> mapMimeExtension;

   Map<String, CorrespondanceMetaObject> mapsRnd;

   private final Map<String, CorrespondanceMetaObject> mapCorrespondancesCaisses;

   private  List<FormatFichier> formats = new ArrayList<>();

   @Autowired
   public CorrespondanceServiceImpl(final ICorrespondanceTableSSTIGedDao matcher,
                                    final ReferentielFormatService referentielFormatService) {
      super();
      this.referentielFormatService = referentielFormatService;
      this.matcher = matcher;
      mapFormats = getMapFormats();
      mapMimeExtension = getMapMimeExtension();
      mapsRnd = matcher.getAllRNDCorresp();
      mapCorrespondancesCaisses = matcher.getAllCaisseCorresp();
   }

   private static final Logger LOGGER = LoggerFactory.getLogger(CorrespondanceServiceImpl.class);

   private String transformCodeCaisseToCodeOrgaGest(final String codeCaisse) {
      final String[] splitArray = codeCaisse.split("-");
      String numeroCaisse = splitArray[splitArray.length - 1];
      if (numeroCaisse.length() == 1) {
         numeroCaisse = "0" + numeroCaisse;
      }

      return "RSI" + numeroCaisse;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws CorrespondanceException
    * @throws CorrespondanceFormatException
    */
   @Override
   public DocumentType applyCorrespondance(final DocumentType documentType) throws CorrespondanceException, CorrespondanceFormatException {
      final Map<String, MetadonneeType> mapMetas = documentType.getMetadonnees().convertToMap();

      // Modifier le codeOrganismeGestionnaire
      documentType.getMetadonnees()
      .updateMeta(Metadata.CODE_ORGA_GESTIONNAIRE,
                  transformCodeCaisseToCodeOrgaGest(
                                                    mapMetas.get(Metadata.CODE_ORGA_GESTIONNAIRE).getValeur()));

      applyCorrespondanceFormat(documentType);

      applyCorrespondanceRnd(documentType);

      renameDocumentExtension(documentType);

      applyCorrespondanceCodeTiCodeOrgaPro(documentType);

      return documentType;
   }

   /**
    * Calculer le nbre de page pour les fichiers PDF et TIFF
    * 
    * @param fileAbsolutePath
    * @param documentType
    * @throws CountNbrePageFileException
    * @throws IOException
    */
   @Override
   public void calculateNbPages(final String fileAbsolutePath, final DocumentType documentType, final boolean activateRTF)
         throws CountNbrePageFileException, IOException {
      int nbPages = 0;
      final String formatFichier = documentType.getMetadonnees().getMetaValue(Metadata.FORM_FICHIER);
      if (formatFichier.equals(FileConst.Mime.PDF_ID)) {
         nbPages = FileUtils.countNbPagesPDF(fileAbsolutePath);
      } else if (formatFichier.equals(FileConst.Mime.TIFF_ID)) {
         nbPages = FileUtils.countNbPagesTIFF(fileAbsolutePath);
      } else if (formatFichier.equals(FileConst.Mime.DOC_ID)) {
         nbPages = FileUtils.countNbPagesDoc(fileAbsolutePath);
      } else if (formatFichier.equals(FileConst.Mime.DOCX_ID)) {
         nbPages = FileUtils.countNbPagesDocx(fileAbsolutePath);
      } else if (formatFichier.equals(FileConst.Mime.RTF_ID)) {
         if (activateRTF) {
            nbPages = FileUtils.countNbPagesRTF(fileAbsolutePath);
         } else {
            nbPages = 1;
         }
      }
      // Update la meta nbPages to simulate hash processing
      documentType.getMetadonnees().updateMeta("NbPages", String.valueOf(nbPages));
      documentType.setNombreDePages(nbPages);
   }

   /**
    * Applique les correspondances sur le format d'un document à archiver
    * 
    * @param documentType
    * @return
    * @throws CorrespondanceException
    * @throws CorrespondanceFormatException
    */
   private void applyCorrespondanceFormat(final DocumentType documentType) throws CorrespondanceException, CorrespondanceFormatException {

      // On recupère le Mimetype du fichier
      final String formatFichier = documentType.getMetadonnees().getMetaValue(Metadata.FORM_FICHIER);

      // Si le MimeType du document n'est pas géré par le GED ou n'est pas autorisé
      if (mapFormats.containsKey(formatFichier.toLowerCase())) {

         // Verifier la conformité entre l'extension et le mimetype du binaire
         final FormatFichier formatFichier2 = mapFormats.get(formatFichier);
         final String extensionListString = formatFichier2.getExtension();
         final String nomFichier = documentType.getObjetNumerique().getCheminEtNomDuFichier();

         final String[] splitArray = nomFichier.split("\\.");
         final String extension = splitArray[splitArray.length - 1];

         if (!extensionListString.contains(extension)) {
            throw new CorrespondanceFormatException(String.format("Le mimetype [%s] du document ne correspond pas à l'extension [%s]",
                                                                  formatFichier2.getTypeMime(),
                                                                  extension));
         }

         final String updateFormat = formatFichier2.getIdFormat();
         // Remplissage du format du fichier
         documentType.getMetadonnees()
         .updateMeta(Metadata.FORM_FICHIER,
                     updateFormat);
      } else {
         throw new CorrespondanceException("Aucune Correspondance ou format non autorisé en GED pour : ", formatFichier);
      }

   }

   private void applyCorrespondanceCodeTiCodeOrgaPro(final DocumentType document) throws CorrespondanceException {
      // correpondance Code Caisse & Code Organisme Proprietaire
      final String key = document.getMetadonnees().getMetaValue(Metadata.CODE_ORGA_PROPRIETAIRE);
      // Recuperation du numero de la caisse
      final int keyInt = Integer.parseInt(key.split("-")[1]);

      if (mapCorrespondancesCaisses.containsKey(String.valueOf(keyInt))) {
         final String reelCodeOrgaPro = mapCorrespondancesCaisses.get(String.valueOf(keyInt)).getValue();
         document.getMetadonnees().updateMeta(Metadata.CODE_ORGA_PROPRIETAIRE, reelCodeOrgaPro);
      } else {
         throw new CorrespondanceException("Aucune Correspondance CodeOrgaProp trouvée pour le Code", key);
      }
   }

   private void applyCorrespondanceRnd(final DocumentType documentType) throws CorrespondanceException {
      final String key = documentType.getMetadonnees().getMetaValue("CodeRND");
      final CorrespondanceMetaObject metaRnd = mapsRnd.get(key);

      if (metaRnd != null) {
         final String rndNewValue = metaRnd.getValue();
         documentType.getMetadonnees().updateMeta("CodeRND", rndNewValue);
      } else {
         throw new CorrespondanceException(key);
      }
   }

   /**
    * Renomme l'extension du fichier si besoin au format approprié
    * 
    * @param documentType
    */
   private void renameDocumentExtension(final DocumentType documentType) {

      if (documentType.getMetadonnees().getMetaValue(Metadata.FORM_FICHIER).equals(FileConst.Mime.PDF_ID)) {
         // renommer l'extension du fichier dans le path de l'objet numérique en .pdf
         final String cheminFichier = documentType.getObjetNumerique().getPath();

         String nomFichier = documentType.getObjetNumerique().getCheminEtNomDuFichier();
         nomFichier = nomFichier.replaceFirst(FileConst.Extension.BIN, FileConst.Extension.PDF);

         final FichierType fichier = new FichierType();
         fichier.setCheminEtNomDuFichier(nomFichier);
         fichier.setPath(cheminFichier);

         documentType.setObjetNumerique(fichier);
      }
   }

   /**
    * Recupère la liste des formats de fichier en base dans une map
    * dont la clé est l'identifiant du format la valeur est le format
    * 
    * @return
    */
   private Map<String, FormatFichier> getMapFormats() {

      formats = referentielFormatService.getAllFormat();

      Map<String, FormatFichier> formatsMap = formats.stream()
            .map(format -> {
              String mimeTOLowerCase = format.getTypeMime().toLowerCase();
              format.setTypeMime(mimeTOLowerCase);
              return format;
            })
            .filter(format -> !format.getIdFormat().equals("pdf"))
            .collect(
                     Collectors.toMap(FormatFichier::getTypeMime,
                                      format -> format));

      formatsMap = formatsMap.entrySet().stream()
                           .collect(Collectors.toMap(entry -> entry.getKey().toLowerCase(), entry -> entry.getValue()));

      formatsMap.forEach((k, v) -> LOGGER.info("Key {} ==> value : {}, id : {}, autorisé : {}",
                                               k,
                                               v.getExtension(),
                                               v.getIdFormat(),
                                               v.isAutoriseGED()));

      return formatsMap;
   }
   
   /**
    * Recupère une {@link Map} contenant Le Mime type ==> extension des fichiers
    * 
    * @return
    */
   private Map<String, String> getMapMimeExtension(){
     
     Map<String, String> mapMimeExtension = formats.stream()
         .map(format -> {
           String mimeTOLowerCase = format.getTypeMime().toLowerCase();
           format.setTypeMime(mimeTOLowerCase);
           return format;
         })
         .filter(format -> !format.getIdFormat().equals("pdf"))
         .collect(
                  Collectors.toMap(FormatFichier::getTypeMime,
                                   format -> format.getExtension()));
     return mapMimeExtension;
   }

   public Map<String, CorrespondanceMetaObject> getAllMatcherCodeTiToCodeOrgaPro() {
      final Map<String, CorrespondanceMetaObject> maps = matcher.getAllCaisseCorresp();
      maps.forEach((k, v) -> {
         LOGGER.info("Key {} ==> value : {}", k, v.getValue());
      });
      return maps;
   }

   @Override
  public String getExtensionFromMimeType(final String mimeType) {
      return mapMimeExtension.get(mimeType);
   }

}
