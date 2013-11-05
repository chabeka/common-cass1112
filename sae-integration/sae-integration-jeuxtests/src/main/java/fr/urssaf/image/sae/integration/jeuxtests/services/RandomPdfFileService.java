package fr.urssaf.image.sae.integration.jeuxtests.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.integration.jeuxtests.modele.NomFichierEtSha1;


public class RandomPdfFileService {

   
   private static final Logger LOG = LoggerFactory.getLogger(RandomPdfFileService.class);
   
   public void genererDocumentsContenuAleatoire(
         int tailleFichierEnKo,
         int nbFichiers,
         String repertoireFichiers) throws IOException {
      
      int nbLeadingsZerosPourNomFichier = String.valueOf(nbFichiers).length();
      
      String nomFichier = "txt_%0" + nbLeadingsZerosPourNomFichier + "d.pdf";
      
      File file;
      FileInputStream fis;
      List<NomFichierEtSha1> listeSha1 = new ArrayList<NomFichierEtSha1>();
      
      for (int i=1;i<=nbFichiers;i++) {
         
         if ((i==1) || ((i%100)==0) || (i==nbFichiers)) {
            LOG.debug(i + "/" + nbFichiers);
         }
         
         // Emplacement et nom du fichier
         file = new File(repertoireFichiers,String.format(nomFichier, i));
         
         // Génère le fichier
         genererFichierAleatoire(file,tailleFichierEnKo);
         
         // Calcul du SHA-1
         fis = new FileInputStream(file);
         listeSha1.add(
               new NomFichierEtSha1(
                     file.getName(), 
                     DigestUtils.shaHex(fis))
               );
         
      }
      
      // Ecrit les SHA-1 dans un fichier nommé "_sha1.properties"
      // dans le même répertoire que les fichiers aléatoires générés
      LOG.debug("Ecriture du fichier contenant les SHA-1");
      ecrireListeSha1DansFichier(repertoireFichiers,listeSha1);
      
   }
   
   
   private void genererFichierAleatoire(File file, int tailleEnKo) throws IOException {
      
      // Génère un fichier au contenu aléatoire de tailleEnKo kilo-octets
      // => on génère un fichier texte contenant des UUID séparés par des CRLF
      
      int tailleEnOctets = tailleEnKo*1024;
      
      int tailleUuidEnOctets = 36+2 ; // +2 pour un CRLF
      
      int nbUuid = tailleEnOctets / tailleUuidEnOctets;
      if ((tailleEnOctets % tailleUuidEnOctets)>0) {
         nbUuid++;
      }
      
      FileWriterWithEncoding writer = new FileWriterWithEncoding(
            file,"UTF-8", false);
      try {
         
         for (int i=1;i<=nbUuid;i++) {
            writer.write(UUID.randomUUID().toString());
            writer.write("\r\n");
         }
         
      }
      finally {
         writer.close();
      }
      
   }
   
   
   private void ecrireListeSha1DansFichier(
         String repertoire,
         List<NomFichierEtSha1> listeSha1) throws IOException {
      
      File file = new File(repertoire,"_sha1.properties");
      FileWriterWithEncoding writer = new FileWriterWithEncoding(
            file,"UTF-8", false);
      try {

         for (int i=0;i<listeSha1.size();i++) {
            writer.write(listeSha1.get(i).getNomFichier());
            writer.write("=");
            writer.write(listeSha1.get(i).getSha1());
            writer.write("\r\n");
         }
         
      }
      finally {
         writer.close();
      }
      
   }
   
   
   public void genererFichierDesSha1(
         String repertoireFichiersPdf,
         String repertoireSortieFichierDesSha1) throws IOException {
      
      File repertoire = new File(repertoireFichiersPdf);
      
      Collection<File> fichiers = FileUtils.listFiles(
            repertoire, new String[] { "pdf", "PDF"}, false);
      
      FileInputStream fis;
      List<NomFichierEtSha1> listeSha1 = new ArrayList<NomFichierEtSha1>();
      for (File file : fichiers) {
         fis = new FileInputStream(file);
         listeSha1.add(
               new NomFichierEtSha1(
                     file.getName(), 
                     DigestUtils.shaHex(fis))
               );
      }
      
      ecrireListeSha1DansFichier(repertoireSortieFichierDesSha1, listeSha1);
      
   }
   
   
   
}
