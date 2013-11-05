package fr.urssaf.image.sae.integration.jeuxtests.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.jeuxtests.modele.NomFichierEtSha1;


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
         ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début du noeud documents
         ecritureSommaire_NoeudDocumentsDebut(writer);
         
         // Pour chaque document
         for (int i=1;i<=nbDocs;i++) {
            
            if ((i==1) || ((i%100)==0) || (i==nbDocs)) {
               System.out.println(i + "/" + nbDocs);
            }
            
            ecritureSommaire_NoeudDocument(
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
         ecritureSommaire_NoeudDocumentsFin(writer);
         ecritureSommaire_NoeudDocVirtuVide(writer);
         ecritureSommaire_Pied(writer);
         
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
         ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début du noeud documents
         ecritureSommaire_NoeudDocumentsDebut(writer);
         
         // Pour chaque document
         for (int i=1;i<=nbDocs;i++) {
            
            if ((i==1) || ((i%100)==0) || (i==nbDocs)) {
               System.out.println(i + "/" + nbDocs);
            }
            
            
            if (compteurFichierDansListeSha1>=nbFichiersDansListeSha1) {
               compteurFichierDansListeSha1 = 0 ;
            }
            
            
            ecritureSommaire_NoeudDocument(
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
         ecritureSommaire_NoeudDocumentsFin(writer);
         ecritureSommaire_NoeudDocVirtuVide(writer);
         ecritureSommaire_Pied(writer);
         
      }
      finally {
         writer.close();
      }
      
   }
   
   
   private String buildSirenAleatoire() {
      return RandomStringUtils.randomNumeric(10);
   }
   
   
   private void ecritureSommaire_EnTete(
         FileWriterWithEncoding writer,
         boolean avecRestitutionId) throws IOException {
      
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); writer.write("\r\n");
      writer.write("<som:sommaire"); writer.write("\r\n");
      writer.write("   xmlns:som=\"http://www.cirtil.fr/sae/sommaireXml\""); writer.write("\r\n");
      writer.write("   xmlns:somres=\"http://www.cirtil.fr/sae/commun_sommaire_et_resultat\""); writer.write("\r\n");
      writer.write("   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"); writer.write("\r\n");
      writer.write("   <som:batchMode>TOUT_OU_RIEN</som:batchMode>"); writer.write("\r\n");
      // writer.write("   <som:dateCreation>2011-10-15</som:dateCreation>"); writer.write("\r\n");
      // writer.write("   <som:description>La description du traitement</som:description>"); writer.write("\r\n");
      if (avecRestitutionId) {
         writer.write("   <som:restitutionUuids>true</som:restitutionUuids>"); writer.write("\r\n");
      }
      
      
   }
   
   
   private void ecritureSommaire_NoeudDocumentsDebut(
         FileWriterWithEncoding writer) throws IOException {
      writer.write("   <som:documents>"); writer.write("\r\n");
   }
   
   private void ecritureSommaire_NoeudDocumentsFin(
         FileWriterWithEncoding writer) throws IOException {
      writer.write("   </som:documents>"); writer.write("\r\n");
   }
   
   
   private void ecritureSommaire_Pied(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("</som:sommaire>"); writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudDocVirtuVide(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   <som:documentsVirtuels />"); writer.write("\r\n");
      
   }
   
   
   
   private void ecritureSommaire_NoeudDocument(
         FileWriterWithEncoding writer,
         String objNumCheminEtNomFichier,
         String hash,
         String denomination,
         String siren,
         boolean sirenAleatoire,
         int indiceDoc,
         String applicationTraitement,
         boolean avecNumeroRecours,
         String dateCreation) throws IOException {
      
      writer.write("      <somres:document>"); writer.write("\r\n");
      
      // Objet numérique
      writer.write("         <somres:objetNumerique>"); writer.write("\r\n");
      writer.write("            <somres:cheminEtNomDuFichier>" + objNumCheminEtNomFichier + "</somres:cheminEtNomDuFichier>"); writer.write("\r\n");
      writer.write("         </somres:objetNumerique>"); writer.write("\r\n");
      
      // Les métadonnées
      ecritureSommaire_NoeudMetadonnees(writer, hash, denomination, siren, sirenAleatoire,
            indiceDoc, applicationTraitement, avecNumeroRecours, dateCreation);
      
      // Fin du document
      writer.write("         <somres:numeroPageDebut>1</somres:numeroPageDebut>"); writer.write("\r\n");
      writer.write("         <somres:nombreDePages>1</somres:nombreDePages>"); writer.write("\r\n");
      writer.write("      </somres:document>"); writer.write("\r\n");
      
   }
   
   
   private void ecritureSommaire_NoeudMetadonnees(
         FileWriterWithEncoding writer,
         String hash,
         String denomination,
         String siren,
         boolean sirenAleatoire,
         int indiceDoc,
         String applicationTraitement,
         boolean avecNumeroRecours,
         String dateCreation) throws IOException {
      
      // Début métadonnées
      writer.write("         <somres:metadonnees>"); writer.write("\r\n");
      
      // Modèle
//      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
//      writer.write("               <somres:code></somres:code>"); writer.write("\r\n");
//      writer.write("               <somres:valeur></somres:valeur>"); writer.write("\r\n");
//      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>Titre</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>Attestation de vigilance</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>DateCreation</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>" + dateCreation + "</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>ApplicationProductrice</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>ADELAIDE</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>CodeOrganismeProprietaire</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>UR750</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>CodeOrganismeGestionnaire</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>CER69</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>CodeRND</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>2.3.1.1.12</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>Hash</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>" + hash + "</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>TypeHash</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>SHA-1</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>NbPages</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>2</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>FormatFichier</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>fmt/354</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      // Siren, en option
      // Peut être une valeur fixe, ou généré aléatoirement
      if (sirenAleatoire) {
         writer.write("            <somres:metadonnee>"); writer.write("\r\n");
         writer.write("               <somres:code>Siren</somres:code>"); writer.write("\r\n");
         writer.write("               <somres:valeur>" + buildSirenAleatoire() + "</somres:valeur>"); writer.write("\r\n");
         writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      } else if (StringUtils.isNotBlank(siren)) {
         writer.write("            <somres:metadonnee>"); writer.write("\r\n");
         writer.write("               <somres:code>Siren</somres:code>"); writer.write("\r\n");
         writer.write("               <somres:valeur>" + siren + "</somres:valeur>"); writer.write("\r\n");
         writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      }
      
      // ApplicationTraitement, en option
      if (StringUtils.isNotBlank(applicationTraitement)) {
         writer.write("            <somres:metadonnee>"); writer.write("\r\n");
         writer.write("               <somres:code>ApplicationTraitement</somres:code>"); writer.write("\r\n");
         writer.write("               <somres:valeur>" + applicationTraitement + "</somres:valeur>"); writer.write("\r\n");
         writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      }
      
      // Denomination, en option
      // Surtout utilisé pour les tests d'intégration interne
      if (StringUtils.isNotBlank(denomination)) {
         writer.write("            <somres:metadonnee>"); writer.write("\r\n");
         writer.write("               <somres:code>Denomination</somres:code>"); writer.write("\r\n");
         writer.write("               <somres:valeur>" + denomination + "</somres:valeur>"); writer.write("\r\n");
         writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      }
      
      // NumeroRecours, en option
      // Surtout utilisé pour les tests d'intégration interne
      if (avecNumeroRecours) {
         writer.write("            <somres:metadonnee>"); writer.write("\r\n");
         writer.write("               <somres:code>NumeroRecours</somres:code>"); writer.write("\r\n");
         writer.write("               <somres:valeur>" + indiceDoc + "</somres:valeur>"); writer.write("\r\n");
         writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      }
      
      // Fin des métadonnées
      writer.write("         </somres:metadonnees>"); writer.write("\r\n");
      
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
         ecritureSommaire_EnTete(writer, avecRestitutionId);
         
         // Début des documents virtuels
         ecritureSommaire_NoeudDocumentsVirtuelsDebut(writer);
         
         // Pour chaque document virtuel
         for (NomFichierEtSha1 nomFichierEtSha1: listeSha1) {
            
            // Initialise le numéro de la page
            int numeroPageDebut = 1;
            
            // Ecriture du début noeud documentVirtuel
            ecritureSommaire_NoeudDocumentVirtuelDebut(writer,nomFichierEtSha1.getNomFichier());
            
            // Ecriture de chaque composant
            for (int i=1;i<=nbIndexationsParFichier;i++) {
               
               // Trace écran
               if ((i==1) || ((i%100)==0) || (i==nbIndexationsParFichier)) {
                  System.out.println(i + "/" + nbIndexationsParFichier);
               }
               
               // Ecriture du début du noeud composant
               ecritureSommaire_NoeudComposantDebut(writer);
               
               // Ecriture des métadonnées
               ecritureSommaire_NoeudMetadonnees(writer, nomFichierEtSha1.getSha1(), denomination, 
                     siren, sirenAleatoire, indiceDoc, applicationTraitement, avecNumeroRecours, 
                     dateCreation);
               
               // Ecriture des numéros de page
               ecritureSommaire_NoeudsPages(writer, numeroPageDebut, nombreDePagesParIndexation);
               numeroPageDebut += nombreDePagesParIndexation;
               
               // Ecriture de la fin du noeud composant
               ecritureSommaire_NoeudComposantFin(writer);
               
               // Augmente l'indice du document pour la méta NumeroRecours
               indiceDoc++;
               
            }
            
            // Ajoute éventuellement un composant qui indexe toutes les pages
            if (avecIndexationSurToutesLesPages) {
               
               // Ecriture du début du noeud composant
               ecritureSommaire_NoeudComposantDebut(writer);
               
               // Ecriture des métadonnées
               ecritureSommaire_NoeudMetadonnees(writer, nomFichierEtSha1.getSha1(), denomination, 
                     siren, sirenAleatoire, indiceDoc, applicationTraitement, avecNumeroRecours, 
                     dateCreation);
               
               // Ecriture des numéros de page
               ecritureSommaire_NoeudsPages(writer, 1, nbPagesTotalParFichier);
               numeroPageDebut += nombreDePagesParIndexation;
               
               // Ecriture de la fin du noeud composant
               ecritureSommaire_NoeudComposantFin(writer);
               
               // Augmente l'indice du document pour la méta NumeroRecours
               indiceDoc++;
               
            }
            
            // Ecriture de la fin noeud documentVirtuel
            ecritureSommaire_NoeudDocumentVirtuelFin(writer);
            
         }
         
         // Fin des documents virtuels
         ecritureSommaire_NoeudDocumentsVirtuelsFin(writer);
         
         // Pied de XML
         ecritureSommaire_Pied(writer);
         
      }
      finally {
         writer.close();
      }
      
   }
   
   private void ecritureSommaire_NoeudDocumentsVirtuelsDebut(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   <som:documents />"); writer.write("\r\n"); 
      writer.write("\r\n");
      
      writer.write("   <som:documentsVirtuels>"); writer.write("\r\n"); 
      writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudDocumentsVirtuelsFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   </som:documentsVirtuels>"); writer.write("\r\n"); 
      writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudDocumentVirtuelDebut(
         FileWriterWithEncoding writer,
         String nomFichier) throws IOException {
      
      writer.write("      <somres:documentVirtuel>"); writer.write("\r\n");
      writer.write("         <somres:objetNumerique>"); writer.write("\r\n");
      writer.write("            <somres:cheminEtNomDuFichier>" + nomFichier + "</somres:cheminEtNomDuFichier>"); writer.write("\r\n");
      writer.write("         </somres:objetNumerique>"); writer.write("\r\n");
      writer.write("\r\n");
      writer.write("         <somres:composants>"); writer.write("\r\n");
      writer.write("\r\n");

   }
   
   private void ecritureSommaire_NoeudDocumentVirtuelFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("\r\n");
      writer.write("         </somres:composants>"); writer.write("\r\n");
      writer.write("      </somres:documentVirtuel>"); writer.write("\r\n");
      writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudComposantDebut(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("            <somres:composant>"); writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudComposantFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("            </somres:composant>"); writer.write("\r\n");
      
   }
   
   private void ecritureSommaire_NoeudsPages(
         FileWriterWithEncoding writer, 
         int numeroPageDebut, 
         int nombrePages) throws IOException {
      
      writer.write("               <somres:numeroPageDebut>" + numeroPageDebut + "</somres:numeroPageDebut>"); writer.write("\r\n");
      writer.write("               <somres:nombreDePages>" + nombrePages + "</somres:nombreDePages>"); writer.write("\r\n");
      
   }
   

}
