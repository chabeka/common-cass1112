package fr.urssaf.image.sae.integration.jeuxtests.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.jeuxtests.modele.NomFichierEtSha1;
import fr.urssaf.image.sae.integration.jeuxtests.utils.SommaireUtils;


public final class SommaireService {
   
   
   
   public void genereSommaireMonoPdf(
         int nbDocs,
         String cheminEcritureFichierSommaire,
         String objNumCheminEtNomFichier,
         String hash,
         String denomination,
         String siren,
         boolean sirenAleatoire) throws IOException {
      
      String applicationTraitement = "ATTESTATIONS";
      boolean avecNumeroRecours = false;
      String dateCreation = "2011-10-22";
      boolean avecRestitutionId = false;
      
      genereSommaireMonoPdf(
            nbDocs,
            cheminEcritureFichierSommaire,
            objNumCheminEtNomFichier,
            hash,
            denomination,
            siren,
            sirenAleatoire,
            applicationTraitement,
            avecNumeroRecours,
            dateCreation,
            avecRestitutionId);
      
   }
   
   
   
   public void genereSommaireMonoPdf(
         int nbDocs,
         String cheminEcritureFichierSommaire,
         String objNumCheminEtNomFichier,
         String hash,
         String denomination,
         String siren,
         boolean sirenAleatoire,
         String applicationTraitement,
         boolean avecNumeroRecours,
         String dateCreation,
         boolean avecRestitutionId) throws IOException {
      
      File file = new File(cheminEcritureFichierSommaire);
      
      FileWriterWithEncoding writer = new FileWriterWithEncoding(
            file,"UTF-8", false);
      
      try {
       
         // En-tête 
         SommaireUtils.ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début du noeud documents
         SommaireUtils.ecritureSommaire_NoeudDocumentsDebut(writer);
         
         // Pour chaque document
         for (int i=1;i<=nbDocs;i++) {
            
            if ((i==1) || ((i%100)==0) || (i==nbDocs)) {
               System.out.println(i + "/" + nbDocs);
            }
            
            SommaireUtils.ecritureSommaire_NoeudDocument(
                  writer,
                  objNumCheminEtNomFichier,
                  hash,
                  denomination,
                  siren,
                  sirenAleatoire,
                  i,
                  applicationTraitement,
                  avecNumeroRecours,
                  dateCreation);
            
         }
         
         // Pied de XML
         SommaireUtils.ecritureSommaire_NoeudDocumentsFin(writer);
         SommaireUtils.ecritureSommaire_NoeudDocVirtuVide(writer);
         SommaireUtils.ecritureSommaire_Pied(writer);
         
      }
      finally {
         writer.close();
      }
      
   }
   
   
   
   
   public void genereSommairePluriPdf(
         int nbDocs,
         String cheminEcritureFichierSommaire,
         String cheminFichierDesSha1,
         String denomination,
         String siren,
         boolean sirenAleatoire) throws IOException {
      
      String applicationTraitement = "ATTESTATIONS";
      boolean avecNumeroRecours = false;
      String dateCreation = "2011-10-22";
      boolean avecRestitutionId = false;
      
      genereSommairePluriPdf(
            nbDocs,
            cheminEcritureFichierSommaire,
            cheminFichierDesSha1,
            denomination,
            siren,
            sirenAleatoire,
            applicationTraitement,
            avecNumeroRecours,
            dateCreation,
            avecRestitutionId);
      
   }
   
   
   
   public void genereSommairePluriPdf(
         int nbDocs,
         String cheminEcritureFichierSommaire,
         String cheminFichierDesSha1,
         String denomination,
         String siren,
         boolean sirenAleatoire,
         String applicationTraitement,
         boolean avecNumeroRecours,
         String dateCreation,
         boolean avecRestitutionId) throws IOException {
      
      File fileSommaire = new File(cheminEcritureFichierSommaire);
      
      // Lecture du fichier texte contenant les SHA-1
      File fileSha1 = new File(cheminFichierDesSha1);
      List<NomFichierEtSha1> listeSha1 = lireListeSha1DepuisFichier(fileSha1);
//      System.out.println(listeSha1.size());
//      System.out.println(listeSha1.get(0).nomFichier);
      int compteurFichierDansListeSha1 = 0;
      int nbFichiersDansListeSha1 = listeSha1.size();
      
      
      
      FileWriterWithEncoding writer = new FileWriterWithEncoding(
            fileSommaire,"UTF-8", false);
      try {
       
         // En-tête 
         SommaireUtils.ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début du noeud documents
         SommaireUtils.ecritureSommaire_NoeudDocumentsDebut(writer);
         
         // Pour chaque document
         for (int i=1;i<=nbDocs;i++) {
            
            if ((i==1) || ((i%100)==0) || (i==nbDocs)) {
               System.out.println(i + "/" + nbDocs);
            }
            
            
            if (compteurFichierDansListeSha1>=nbFichiersDansListeSha1) {
               compteurFichierDansListeSha1 = 0 ;
            }
            
            
            SommaireUtils.ecritureSommaire_NoeudDocument(
                  writer,
                  listeSha1.get(compteurFichierDansListeSha1).getNomFichier() ,
                  listeSha1.get(compteurFichierDansListeSha1).getSha1(),
                  denomination,
                  siren,
                  sirenAleatoire,
                  i,
                  applicationTraitement,
                  avecNumeroRecours,
                  dateCreation);
            
            
            compteurFichierDansListeSha1++;
            
         }
         
         // Pied de XML
         SommaireUtils.ecritureSommaire_NoeudDocumentsFin(writer);
         SommaireUtils.ecritureSommaire_NoeudDocVirtuVide(writer);
         SommaireUtils.ecritureSommaire_Pied(writer);
         
      }
      finally {
         writer.close();
      }
      
   }
   
   private List<NomFichierEtSha1> lireListeSha1DepuisFichier(
         File file) throws IOException {
      
      List<NomFichierEtSha1> result = new ArrayList<NomFichierEtSha1>();
      
      List<String> lignes = FileUtils.readLines(file, "UTF-8");
      
      String[] parties;
      for(String ligne: lignes) {
         
         parties = StringUtils.split(ligne, '=');
         
         result.add(
               new NomFichierEtSha1(
                     parties[0], 
                     parties[1]));

      }
      
      return result;
      
   }
   
   
   public void genereSommaireDocumentVirtuel(
         int nbIndexationsParFichier,
         String cheminEcritureFichierSommaire,
         String cheminFichierDesSha1,
         String denomination,
         String siren,
         boolean sirenAleatoire,
         String applicationTraitement,
         boolean avecNumeroRecours,
         String dateCreation,
         boolean avecRestitutionId,
         int nombreDePagesParIndexation,
         boolean avecIndexationSurToutesLesPages,
         int nbPagesTotalParFichier) throws IOException {
      
      File fileSommaire = new File(cheminEcritureFichierSommaire);
      
      // Lecture du fichier texte contenant les SHA-1
      File fileSha1 = new File(cheminFichierDesSha1);
      List<NomFichierEtSha1> listeSha1 = lireListeSha1DepuisFichier(fileSha1);

      int indiceDoc = 1;
      
      FileWriterWithEncoding writer = new FileWriterWithEncoding(
            fileSommaire,"UTF-8", false);
      try {
       
         // En-tête 
         SommaireUtils.ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début des documents virtuels
         SommaireUtils.ecritureSommaire_NoeudDocumentsVirtuelsDebut(writer);
         
         // Pour chaque document virtuel
         for (NomFichierEtSha1 nomFichierEtSha1: listeSha1) {
            
            // Initialise le numéro de la page
            int numeroPageDebut = 1;
            
            // Ecriture du début noeud documentVirtuel
            SommaireUtils.ecritureSommaire_NoeudDocumentVirtuelDebut(writer,nomFichierEtSha1.getNomFichier());
            
            // Ecriture de chaque composant
            for (int i=1;i<=nbIndexationsParFichier;i++) {
               
               // Trace écran
               if ((i==1) || ((i%100)==0) || (i==nbIndexationsParFichier)) {
                  System.out.println(i + "/" + nbIndexationsParFichier);
               }
               
               // Ecriture du début du noeud composant
               SommaireUtils.ecritureSommaire_NoeudComposantDebut(writer);
               
               // Ecriture des métadonnées
               SommaireUtils.ecritureSommaire_NoeudMetadonnees(writer, nomFichierEtSha1.getSha1(), denomination, 
                     siren, sirenAleatoire, indiceDoc, applicationTraitement, avecNumeroRecours, 
                     dateCreation);
               
               // Ecriture des numéros de page
               SommaireUtils.ecritureSommaire_NoeudsPages(writer, numeroPageDebut, nombreDePagesParIndexation);
               numeroPageDebut += nombreDePagesParIndexation;
               
               // Ecriture de la fin du noeud composant
               SommaireUtils.ecritureSommaire_NoeudComposantFin(writer);
               
               // Augmente l'indice du document pour la méta NumeroRecours
               indiceDoc++;
               
            }
            
            // Ajoute éventuellement un composant qui indexe toutes les pages
            if (avecIndexationSurToutesLesPages) {
               
               // Ecriture du début du noeud composant
               SommaireUtils.ecritureSommaire_NoeudComposantDebut(writer);
               
               // Ecriture des métadonnées
               SommaireUtils.ecritureSommaire_NoeudMetadonnees(writer, nomFichierEtSha1.getSha1(), denomination, 
                     siren, sirenAleatoire, indiceDoc, applicationTraitement, avecNumeroRecours, 
                     dateCreation);
               
               // Ecriture des numéros de page
               SommaireUtils.ecritureSommaire_NoeudsPages(writer, 1, nbPagesTotalParFichier);
               numeroPageDebut += nombreDePagesParIndexation;
               
               // Ecriture de la fin du noeud composant
               SommaireUtils.ecritureSommaire_NoeudComposantFin(writer);
               
               // Augmente l'indice du document pour la méta NumeroRecours
               indiceDoc++;
               
            }
            
            // Ecriture de la fin noeud documentVirtuel
            SommaireUtils.ecritureSommaire_NoeudDocumentVirtuelFin(writer);
            
         }
         
         // Fin des documents virtuels
         SommaireUtils.ecritureSommaire_NoeudDocumentsVirtuelsFin(writer);
         
         // Pied de XML
         SommaireUtils.ecritureSommaire_Pied(writer);
         
      }
      finally {
         writer.close();
      }
      
   }

}
