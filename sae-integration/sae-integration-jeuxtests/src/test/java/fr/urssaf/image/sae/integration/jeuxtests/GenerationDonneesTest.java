package fr.urssaf.image.sae.integration.jeuxtests;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.integration.jeuxtests.services.RandomPdfFileService;
import fr.urssaf.image.sae.integration.jeuxtests.services.SommaireService;

/**
 * Ceci n'est pas une classe de TU<br>
 * <br>
 * Elle permet de lancer des générations de jeux de données au "coup par coup"
 */
public class GenerationDonneesTest {

   
   @Test
   @Ignore
   public void sommaire_500000() throws IOException {
      
      SommaireService service = new SommaireService();
      
      int nbDocs = 500000;
      String cheminEcritureFichierSommaire = "c:/divers/sommaire.xml";
      String objNumCheminEtNomFichier = "doc1.PDF";
      String hash = "a2f93f1f121ebba0faef2c0596f2f126eacae77b";
      String denomination = "Test 500000";
      String siren = null;
      boolean sirenAleatoire = true;
      String applicationTraitement = null;
      boolean avecNumeroRecours = true;
      String dateCreation = "2011-09-08";
      boolean avecRestitutionId = false;
      
      service.genereSommaireMonoPdf(
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
   
   
   @Test
   @Ignore
   public void sommaire_3000_pour_modif_option_cassandra_sstables() throws IOException {
      
      SommaireService service = new SommaireService();
      
      int nbDocs = 3000;
      String cheminEcritureFichierSommaire = "c:/divers/sommaire.xml";
      String objNumCheminEtNomFichier = "doc1.PDF";
      String hash = "a2f93f1f121ebba0faef2c0596f2f126eacae77b";
      String denomination = "Test_Cass_SSTables";
      String siren = null;
      boolean sirenAleatoire = true;
      String applicationTraitement = null;
      boolean avecNumeroRecours = true;
      String dateCreation = "2012-05-09";
      boolean avecRestitutionId = false;
      
      service.genereSommaireMonoPdf(
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
   
   
   @Test
   @Ignore
   public void sommaire_50000_avec_restitution_uuid() throws IOException {
      
      SommaireService service = new SommaireService();
      
      int nbDocs = 50000;
      String cheminEcritureFichierSommaire = "c:/divers/sommaire.xml";
      String objNumCheminEtNomFichier = "doc1.PDF";
      String hash = "a2f93f1f121ebba0faef2c0596f2f126eacae77b";
      String denomination = "Test 221-CaptureMasseID-OK-Tor-50000";
      String siren = null;
      boolean sirenAleatoire = false;
      String applicationTraitement = null;
      boolean avecNumeroRecours = true;
      String dateCreation = "2013-01-14";
      boolean avecRestitutionId = true;
      
      service.genereSommaireMonoPdf(
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
   
   
   @Test
   public void genererFichierDesSha1() throws IOException {
      
      RandomPdfFileService service = new RandomPdfFileService();
      
      service.genererFichierDesSha1(
            "C:/Divers/doc_virtuel/", 
            "C:/divers");
      
   }
   
   
   @Test
   @Ignore
   public void sommaire_document_virtuel() throws IOException {
      
      SommaireService service = new SommaireService();
      
      int nbIndexationsParFichier = 43;
      String cheminEcritureFichierSommaire = "C:/divers/sommaire.xml";
      String cheminFichierDesSha1 = "C:/divers/_sha1.properties";
      String denomination = "Test XXX doc virt";
      String siren = null;
      boolean sirenAleatoire = true;
      String applicationTraitement = "ATTESTATIONS";
      boolean avecNumeroRecours = true; 
      String dateCreation = "2013-11-05";
      boolean avecRestitutionId = false;
      int nombreDePagesParIndexation = 1;
      boolean avecIndexationSurToutesLesPages = true;
      int nbPagesTotalParFichier = 43;
      
      service.genereSommaireDocumentVirtuel(
            nbIndexationsParFichier,
            cheminEcritureFichierSommaire,
            cheminFichierDesSha1,
            denomination,
            siren,
            sirenAleatoire,
            applicationTraitement,
            avecNumeroRecours,
            dateCreation,
            avecRestitutionId,
            nombreDePagesParIndexation,
            avecIndexationSurToutesLesPages,
            nbPagesTotalParFichier);
      
   }
   
}
