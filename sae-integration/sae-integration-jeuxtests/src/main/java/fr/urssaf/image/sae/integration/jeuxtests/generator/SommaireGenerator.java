package fr.urssaf.image.sae.integration.jeuxtests.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.jeuxtests.utils.SommaireUtils;

public class SommaireGenerator {

   private File fileSommaire;
   private File repertoireDocuments;

   private FileWriterWithEncoding writer;

   public SommaireGenerator(File ecdeRepertoireTraitementBavaria) {

      fileSommaire = new File(ecdeRepertoireTraitementBavaria, "sommaire.xml");

      repertoireDocuments = new File(ecdeRepertoireTraitementBavaria,
            "documents");

   }

   public void genereSommaire() throws IOException {

      boolean avecRestitutionId = false;

      writer = new FileWriterWithEncoding(fileSommaire, "UTF-8", false);
      try {

         // En-tête
         SommaireUtils.ecritureSommaire_EnTete(writer, avecRestitutionId);

         // Début du noeud documents
         SommaireUtils.ecritureSommaire_NoeudDocumentsDebut(writer);

         // Traite le répertoire documents
         traiteRepertoire(repertoireDocuments);
         
         // Pied de XML
         SommaireUtils.ecritureSommaire_NoeudDocumentsFin(writer);
         SommaireUtils.ecritureSommaire_NoeudDocVirtuVide(writer);
         SommaireUtils.ecritureSommaire_Pied(writer);

      } finally {
         writer.close();
      }

   }

   private void traiteRepertoire(File repertoire) throws IOException {
      // récursif
      for (File file : repertoire.listFiles()) {
         if (file.isFile()) {
            traiteFichier(file);
         } else if (file.isDirectory()) {
            traiteRepertoire(file);
         }
      }
   }

   private void traiteFichier(File file) throws IOException {

      String objNumNomFichier = buildObjNumNomFichier(file);
      String hash = sha1(file);
      String denomination = objNumNomFichier;
      String siren = null;
      int indiceDoc = 0;
      boolean sirenAleatoire = false;
      String applicationTraitement = "SAE";
      boolean avecNumeroRecours = false;
      String dateCreation = "2014-02-14";
      
      SommaireUtils.ecritureSommaire_NoeudDocument(
            writer, 
            objNumNomFichier, 
            hash, 
            denomination,
            siren,
            sirenAleatoire,
            indiceDoc,
            applicationTraitement,
            avecNumeroRecours,
            dateCreation);

   }
   
   
   private String buildObjNumNomFichier(File file) {
      
      String cheminPdf = file.getAbsolutePath();
      String cheminRepertoireDocument = repertoireDocuments.getAbsolutePath();
      
      String objNum = cheminPdf.substring(cheminRepertoireDocument.length()+1);
      
      objNum = StringUtils.replace(objNum, "\\", "/");
      
      return objNum;
      
   }
   
   private String sha1(File file) throws IOException {
      
      FileInputStream fis = new FileInputStream(file) ;
      String hash;
      try {
         hash = DigestUtils.shaHex(fis);
      } finally {
         if (fis!=null) {
            fis.close();
         }
      }
      
      return hash;
      
   }

}
