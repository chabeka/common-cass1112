package fr.urssaf.image.sae.integration.jeuxtests.utils;

import java.io.IOException;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class SommaireUtils {

   public static void ecritureSommaire_EnTete(
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
   
   
   public static void ecritureSommaire_NoeudDocumentsDebut(
         FileWriterWithEncoding writer) throws IOException {
      writer.write("   <som:documents>"); writer.write("\r\n");
   }
   
   public static void ecritureSommaire_NoeudDocumentsFin(
         FileWriterWithEncoding writer) throws IOException {
      writer.write("   </som:documents>"); writer.write("\r\n");
   }
   
   
   public static void ecritureSommaire_Pied(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("</som:sommaire>"); writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudDocVirtuVide(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   <som:documentsVirtuels />"); writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudDocument(
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
   
   
   public static void ecritureSommaire_NoeudMetadonnees(
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
      writer.write("               <somres:valeur>Document de test</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>DateCreation</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>" + dateCreation + "</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>ApplicationProductrice</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>SAE</somres:valeur>"); writer.write("\r\n");
      writer.write("            </somres:metadonnee>"); writer.write("\r\n");
      
      writer.write("            <somres:metadonnee>"); writer.write("\r\n");
      writer.write("               <somres:code>CodeOrganismeProprietaire</somres:code>"); writer.write("\r\n");
      writer.write("               <somres:valeur>CER69</somres:valeur>"); writer.write("\r\n");
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
      writer.write("               <somres:valeur>1</somres:valeur>"); writer.write("\r\n");
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
   
   
   private static String buildSirenAleatoire() {
      return RandomStringUtils.randomNumeric(10);
   }
   
   
   public static void ecritureSommaire_NoeudDocumentsVirtuelsDebut(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   <som:documents />"); writer.write("\r\n"); 
      writer.write("\r\n");
      
      writer.write("   <som:documentsVirtuels>"); writer.write("\r\n"); 
      writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudDocumentsVirtuelsFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("   </som:documentsVirtuels>"); writer.write("\r\n"); 
      writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudDocumentVirtuelDebut(
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
   
   public static void ecritureSommaire_NoeudDocumentVirtuelFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("\r\n");
      writer.write("         </somres:composants>"); writer.write("\r\n");
      writer.write("      </somres:documentVirtuel>"); writer.write("\r\n");
      writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudComposantDebut(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("            <somres:composant>"); writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudComposantFin(
         FileWriterWithEncoding writer) throws IOException {
      
      writer.write("            </somres:composant>"); writer.write("\r\n");
      
   }
   
   public static void ecritureSommaire_NoeudsPages(
         FileWriterWithEncoding writer, 
         int numeroPageDebut, 
         int nombrePages) throws IOException {
      
      writer.write("               <somres:numeroPageDebut>" + numeroPageDebut + "</somres:numeroPageDebut>"); writer.write("\r\n");
      writer.write("               <somres:nombreDePages>" + nombrePages + "</somres:nombreDePages>"); writer.write("\r\n");
      
   }
   
   
}
