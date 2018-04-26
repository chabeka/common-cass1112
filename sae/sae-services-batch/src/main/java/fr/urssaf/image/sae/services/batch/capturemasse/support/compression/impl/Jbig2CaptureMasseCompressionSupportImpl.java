package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.commons.itext.exception.ExtractionException;
import fr.urssaf.image.commons.itext.service.TraitementImageService;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.service.SaeDroitService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.exception.CompressionException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.util.UntypedMetadataFinderUtils;
import fr.urssaf.image.sae.storage.dfce.utils.HashUtils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Implémentation du support {@link CaptureMasseCompressionSupport}
 * 
 */
@Component
public class Jbig2CaptureMasseCompressionSupportImpl implements
      CaptureMasseCompressionSupport {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(Jbig2CaptureMasseCompressionSupportImpl.class);
   
   /**
    * Service des droits.
    */
   @Autowired
   private SaeDroitService saeDroitService;
   
   @Autowired
   private TraitementImageService traitementImageService;
   
   @Value("${sae.compression.pdf.executable}")
   private String executable;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isDocumentToBeCompress(final UntypedDocument document,
         final File ecdeDirectory) {
      String trcPrefix = "isDocumentToBeCompress()";
      LOGGER.debug("{} - début", trcPrefix);
      
      // recupere le contexte de securite
      AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
            .getContext().getAuthentication();
      
      // recupere le vecteur d'identification
      VIContenuExtrait extrait = (VIContenuExtrait) token.getPrincipal();
      List<SaePagm> pagms = getListePagmFromVi(extrait);
      
      boolean compressionActive = true;
      Integer seuilCompression = null;
      // on va tester qu'on a une liste de de pagm non vide
      if (pagms.size() > 0) {
         // on recupere le premier elements
         SaePagm curPagm = pagms.get(0);
         // on compare les valeurs avec chacun des autres elements
         for(int index = 1; index < pagms.size(); index++) {
            SaePagm pagm = pagms.get(index);
            if (!verifFlagCompression(curPagm, pagm)) {
               LOGGER.warn("Multiple Pagm ({}, {}) avec option de compression différente", curPagm.getCode(), pagm.getCode());
               compressionActive = false;
               break;
            }
            if (!verifSeuilCompression(curPagm, pagm)) {
               LOGGER.warn("Multiple Pagm ({}, {}) avec seuil de compression différent", curPagm.getCode(), pagm.getCode());
               compressionActive = false;
               break;
            }
         }
         
         // si a l'issue de la boucle, la compression est active, c'est qu'on a 
         // des pagms avec meme option de compression (flag et seuil)
         if (compressionActive) {
            // dans ce cas, on va recuperer ces informations sur le premier
            compressionActive = curPagm.getCompressionPdfActive() == null ? false
                  : curPagm.getCompressionPdfActive().booleanValue();
            seuilCompression = curPagm.getSeuilCompressionPdf();
         }
         
      } else {
         // pas de pagm d'archivage de masse
         // donc pas de compression
         compressionActive = false;
      }
      
      // on va verifier que la compression est active et on verifie que l'os est bien le bon
      if (compressionActive) {
         if (!isUnixSystem()) {
            LOGGER.debug("La compression a ete desactivee pour l'os {}", System.getProperty("os.name"));
            compressionActive = false;
         }
      }
      
      // on va verifier que la compression est active et on verifie la taille du fichier
      if (compressionActive) {
         // on gere le seuil par defaut
         if (seuilCompression == null) {
            seuilCompression = Constantes.SEUIL_COMPRESSION_DEFAUT;
         }
         if (!isPoidsSuperieur(document, ecdeDirectory, seuilCompression)) {
            LOGGER.debug("La compression a ete desactivee car le document a un poids trop petit {}", document.getFilePath());
            compressionActive = false;
         }
      }
      
      // on va verifier que la compression est active et on verifie que toutes les pages comportent des images plein écran
      if (compressionActive) {
         if (!isFullImagePdf(document, ecdeDirectory)) {
            LOGGER.debug("La compression a ete desactivee car le document ne comporte pas que des images plein-ecran {}", document.getFilePath());
            compressionActive = false;
         }
      }
      
      LOGGER.debug("{} - fin", trcPrefix);
      
      return compressionActive;
   }

   /**
    * Methode permettant de recuperer la liste des pagms d'archivage de masse
    * contenue dans le vi a partir du contrat de service.
    * 
    * @param extrait
    *           extrait contenant le vi
    * @return List<SaePagm>
    */
   private List<SaePagm> getListePagmFromVi(final VIContenuExtrait extrait) {
      List<SaePagm> retour = new ArrayList<SaePagm>();
      // recupere la liste des pagms du contrat de service
      // et ne renvoyer que la liste des pagms qui ont des droits d'archivage de
      // masse
      // et qui sont dans le vi (vecteur d'identification)
      List<SaePagm> pagms = saeDroitService.getListeSaePagm(extrait
            .getCodeAppli());
      for (SaePagm pagm : pagms) {
         // verifie si le pagm est contenu dans le vi actuel
         if (extrait.getPagms().contains(pagm.getCode())) {
            // le pagm est utilise dans le vi
            // donc on doit verifier que le pagm contient des droits d'archivage
            // de masse
            if (pagm.getPagma().getActionUnitaires()
                  .contains("archivage_masse")) {
               // le pagm a bien l'action d'archivage de masse
               retour.add(pagm);
            }
         }
      }
      return retour;
   }
   
   /**
    * Methode permettant de comparer le flag de compression entre 2 pagms.
    * 
    * @param pagm1
    *           pagm 1
    * @param pagm2
    *           pagm 2
    * @return boolean (vrai si la valeur est identique)
    */
   private boolean verifFlagCompression(final SaePagm pagm1, final SaePagm pagm2) {
      boolean isIdentique = false;
      if ((pagm1.getCompressionPdfActive() == null && pagm2
            .getCompressionPdfActive() == null)
            || (pagm1.getCompressionPdfActive() == null && !pagm2
                  .getCompressionPdfActive().booleanValue())
            || (!pagm1.getCompressionPdfActive().booleanValue() && pagm2
                  .getCompressionPdfActive() == null)
            || (pagm1.getCompressionPdfActive().booleanValue() ==
                  pagm2.getCompressionPdfActive().booleanValue())) {
         isIdentique = true;
      }
      return isIdentique;
   }
   
   /**
    * Methode permettant de comparer le seuil de compression entre 2 pagms.
    * 
    * @param pagm1
    *           pagm 1
    * @param pagm2
    *           pagm 2
    * @return boolean (vrai si la valeur est identique)
    */
   private boolean verifSeuilCompression(final SaePagm pagm1,
         final SaePagm pagm2) {
      boolean isIdentique = false;
      if ((pagm1.getSeuilCompressionPdf() == null && pagm2
            .getSeuilCompressionPdf() == null)
            || (pagm1.getSeuilCompressionPdf() == null && pagm2
                  .getSeuilCompressionPdf().intValue() == Constantes.SEUIL_COMPRESSION_DEFAUT)
            || (pagm1.getSeuilCompressionPdf().intValue() == Constantes.SEUIL_COMPRESSION_DEFAUT && pagm2
                  .getSeuilCompressionPdf() == null)
            || (pagm1.getSeuilCompressionPdf().intValue() == pagm2
                  .getSeuilCompressionPdf().intValue())) {
         isIdentique = true;
      }
      return isIdentique;
   }
   
   /**
    * Methode permettant de verifier que c'est un system de type unix.
    * 
    * @return boolean
    */
   private boolean isUnixSystem() {
      String os = System.getProperty("os.name").toLowerCase();
      return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os
            .indexOf("aix") >= 0);
   }
   
   /**
    * Methode permettant de calculer le chemin du document.
    * 
    * @param document
    *           document
    * @param ecdeDirectory
    *           chemin de l'ecde
    * @return
    */
   private String getPath(final UntypedDocument document,
         final File ecdeDirectory) {
      final String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + document.getFilePath();
      return path;
   }
   
   /**
    * Methode permettant de savoir si le poids du document est superieur au
    * seuil.
    * 
    * @param document
    *           document
    * @param ecdeDirectory
    *           repertoire ecde
    * @param seuil
    *           seuil
    * @return boolean
    */
   private boolean isPoidsSuperieur(final UntypedDocument document,
         final File ecdeDirectory, final Integer seuil) {
      final String path = getPath(document, ecdeDirectory);
      final File documentFile = new File(path);
      return (documentFile.length() > Long.valueOf(seuil.intValue()));
   }
   
   /**
    * Methode permettant de verifier que le fichier pdf ne comporte que des
    * images plein ecran
    * 
    * @param document
    *           document
    * @param ecdeDirectory
    *           repertoire ecde
    * @return boolean
    */
   private boolean isFullImagePdf(final UntypedDocument document,
         final File ecdeDirectory) {
      final String path = getPath(document, ecdeDirectory);
      final File documentFile = new File(path);
      return traitementImageService.isFullImagePdf(documentFile);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public CompressedDocument compresserDocument(final UntypedDocument document,
         final File ecdeDirectory) throws CompressionException {
      String trcPrefix = "compresserDocument()";
      LOGGER.debug("{} - début", trcPrefix);
      CompressedDocument compressedDocument = null;

      final String path = getPath(document, ecdeDirectory);
      final File documentFile = new File(path);
      if (documentFile.exists()) {
         final String fileName = documentFile.getName().split("\\.")[0];
         final String extension = documentFile.getName().split("\\.")[1];
         // a partir du nom du fichier sans l'extension, on va creer le
         // repertoire d'extraction
         final String repertoireName = fileName;
         final String repExtraction = ecdeDirectory.getAbsolutePath()
               + File.separator + "documents" + File.separator + repertoireName;
         final File fileRepExtraction = new File(repExtraction);
         if (!fileRepExtraction.exists()) {
            fileRepExtraction.mkdirs();
         }

         try {
            // on extrait les images dans le repertoire d'extraction
            traitementImageService.extractImages(documentFile, repExtraction);

            // calcul le nom du fichier compresse
            final String nomFichierCompresse = repertoireName + "-compressed."
                  + extension;
            final String pathCompressedFile = ecdeDirectory.getAbsolutePath()
                  + File.separator + "documents" + File.separator
                  + nomFichierCompresse;

            // on lance la regeneration du pdf a l'aide de jbig2 et du script
            // python
            if (isUnixSystem()) {

               // remplacement de _PATH_IMAGES_TO_REPLACE
               String command = StringUtils.replace(this.executable, "_PATH_IMAGES_TO_REPLACE",
                     repExtraction);

               // remplacement de _PATH_OUTPUT_FILE_TO_REPLACE
               command = StringUtils.replace(command, "_PATH_OUTPUT_FILE_TO_REPLACE", pathCompressedFile);
               
               // lance la commande
               try {
                  LOGGER.debug("{} - execution de la commande : {}", trcPrefix, command);
                  Process p = executeCommand(command);
                  int codeRetour = 0;
                  try {
                     codeRetour = p.waitFor();
                  } catch (InterruptedException e) {
                     LOGGER.error(e.getMessage());
                  }
                  
                  if (codeRetour != 0) {
                     InputStream stream = p.getErrorStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                     String ligne = br.readLine();
                     StringBuffer buffer = new StringBuffer();
                     while (ligne != null) {
                        buffer.append(ligne);
                        buffer.append("\n");
                        ligne = br.readLine();
                     }
                     LOGGER.error(buffer.toString());
                     
                  }
               } catch (IOException e) {
                  LOGGER.error(e.getMessage());
               }

            } else {
               // sous les autres systemes, on ne devrait pas passer par la
               // (sauf en junit)
               // on va quand meme, copier le fichier d'origine
               LOGGER.warn("Pas de compression sur un OS non supporte par jbig2.");
               copyDocument(documentFile, pathCompressedFile);
            }

            compressedDocument = new CompressedDocument();
            // stocke le nom et le chemin du fichier compressé
            compressedDocument.setFileName(nomFichierCompresse);
            compressedDocument.setFilePath(pathCompressedFile);
            // stocke le nom du fichier d'origine
            compressedDocument.setOriginalName(documentFile.getName());

            // on supprime le repertoire d'extraction
            try {
               FileUtils.deleteDirectory(fileRepExtraction);
            } catch (IOException e) {
               LOGGER.error(e.getMessage());
            }

            // recupere l'algo de hash
            final String algoHashCode = UntypedMetadataFinderUtils
                  .valueMetadataFinder(document.getUMetadatas(),
                        SAEArchivalMetadatas.TYPE_HASH.getLongCode());
            
            // on calcule la hash du nouveau document pdf
            FileInputStream inCompressedFile = null;
            try {
               inCompressedFile = new FileInputStream(pathCompressedFile);
               final String hashCalculated = HashUtils.hashHex(
                     inCompressedFile, algoHashCode);

               LOGGER.debug("{} - hash du document compresse : {}", trcPrefix, hashCalculated);
               // stocke le nouveau hash
               compressedDocument.setHash(hashCalculated);

            } catch (FileNotFoundException e) {
               throw new CompressionException(e);
            } catch (NoSuchAlgorithmException e) {
               throw new CompressionException(e);
            } catch (IOException e) {
               throw new CompressionException(e);
            } finally {
               if (inCompressedFile != null) {
                  try {
                     inCompressedFile.close();
                  } catch (IOException e) {
                     // rien a faire
                  }
               }
            }
         } catch (ExtractionException e) {
            throw new CompressionException(e);
         }

      } else {
         LOGGER.error("Le document {} n'existe pas.", documentFile.getName());
      }

      LOGGER.debug("{} - fin", trcPrefix);
      return compressedDocument;
   }

   /**
    * Methode permettant de copier le document d'origine.
    * 
    * @param documentFile
    *           document d'origine.
    * @param pathCompressedFile
    *           document de sortie
    */
   private void copyDocument(final File documentFile,
         final String pathCompressedFile) {
      FileInputStream inputStream = null;
      FileOutputStream outputStream = null;
      try {
         inputStream = new FileInputStream(documentFile);
         outputStream = new FileOutputStream(pathCompressedFile);
         IOUtils.copy(inputStream, outputStream);
         
      } catch (FileNotFoundException e) {
         LOGGER.error(e.getMessage());
      } catch (IOException e) {
         LOGGER.error(e.getMessage());
      } finally {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (IOException e) {
               // rien a faire
            }
         }
         if (outputStream != null) {
            try {
               outputStream.close();
            } catch (IOException e) {
               // rien a faire
            }
         }
      }
   }
   
   /**
    * Methode permettant d'executer la commande de compression.
    * @param commande commande de compression
    * @return Process
    * @throws IOException Exception levée lorsqu'il y a une erreur d'entree/sortie
    */
   private Process executeCommand(final String commande) throws IOException {
      Runtime runtime = Runtime.getRuntime();

      Process process = runtime.exec(commande);
      
      return process;
   }
}
