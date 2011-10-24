package fr.urssaf.image.sae.ecde.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ComposantDocumentVirtuelType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentVirtuelType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentVirtuelType.Composants;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ErreurType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.FichierType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeDocumentsVirtuelsType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeErreurType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeMetadonneeType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.ListeNonIntegratedDocumentsType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.MetadonneeType;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.NonIntegratedDocumentType;
import fr.urssaf.image.sae.ecde.modele.resultats.ResultatsType;
import fr.urssaf.image.sae.ecde.service.ResultatsXmlService;
/**
 * Classe permettant de tester l'implémentation
 * des méthodes de la classe ResultatsXmlServiceImpl
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.MethodNamingConventions","PMD.NcssMethodCount","PMD.ExcessiveMethodLength", "PMD.ExcessiveImports", "PMD.AvoidDuplicateLiterals"})
public class ResultatsXMLServiceImplTest {

   @Autowired
   private ResultatsXmlService service;
   
   private static ResultatsType resultats, resultats2;
      
   private static final File REPERTOIRE = new File (SystemUtils.getJavaIoTmpDir(), "resultatsEcde"); 
   
   @BeforeClass
   public static void init() throws IOException {
      resultats = new ResultatsType();
      resultats2 = new ResultatsType();
      initialiseResultats();
      initialiseResultats2();
      File rep = REPERTOIRE; 
      // creation d'un repertoire dans le rep temp de l'os
      FileUtils.forceMkdir(rep);
      //nettoyage du repertoire present dans le rep temp de l'os
      FileUtils.cleanDirectory(rep);
   }
   
   private static void initialiseResultats() {
      resultats.setBatchMode(BatchModeType.fromValue("PARTIEL"));
      resultats.setInitialDocumentsCount(4);
      resultats.setIntegratedDocumentsCount(2);
      resultats.setNonIntegratedDocumentsCount(2);
      resultats.setInitialVirtualDocumentsCount(5);
      resultats.setIntegratedVirtualDocumentsCount(4);
      resultats.setNonIntegratedVirtualDocumentsCount(1);
      
      // Non integratedDocuments -- document qui n'ont pas été archivés
      NonIntegratedDocumentType doc1 = new NonIntegratedDocumentType();
      // objet num
      FichierType objetNum = new FichierType();
      objetNum.setCheminEtNomDuFichier("repertoire/fichier1.pdf");
      doc1.setObjetNumerique(objetNum);
      
      // erreurs
      ErreurType erreur = new ErreurType();
      erreur.setCode("wsse:SecurityTokenUnavailable");
      erreur.setLibelle(" La référence au jeton de sécurité est introuvable");
      
      ErreurType erreur2 = new ErreurType();
      erreur2.setCode("vi:InvalidVI");
      erreur2.setLibelle(" Le VI est invalide");
      
      ListeErreurType listeErreur = new ListeErreurType();
      listeErreur.getErreur().add(erreur);
      listeErreur.getErreur().add(erreur2);
      
      doc1.setErreurs(listeErreur);
      
      //---------------------------------------      
      NonIntegratedDocumentType doc2 = new NonIntegratedDocumentType();
      // objet num
      FichierType objetNum2 = new FichierType();
      objetNum2.setCheminEtNomDuFichier("repertoire/fichier2.pdf");
      doc2.setObjetNumerique(objetNum2);
      
      // erreurs
      ErreurType erreur3 = new ErreurType();
      erreur3.setCode("vi:InvalidService");
      erreur3.setLibelle("Le service visé par le VI n’existe pas ou est invalide");
      
      ErreurType erreur4 = new ErreurType();
      erreur4.setCode("vi:InvalidPagm");
      erreur4.setLibelle("Le ou les PAGM présents dans le VI sont invalides");
      
      ListeErreurType listeErreur2 = new ListeErreurType();
      listeErreur2.getErreur().add(erreur3);
      listeErreur2.getErreur().add(erreur4);
      
      doc2.setErreurs(listeErreur2);
      
      ListeNonIntegratedDocumentsType listeN = new ListeNonIntegratedDocumentsType();
      listeN.getNonIntegratedDocument().add(doc1);
      listeN.getNonIntegratedDocument().add(doc2);
      
      resultats.setNonIntegratedDocuments(listeN);
      
      // NonIntegratedVirtualDocument -- document virtuel qui n'ont pas été archivés
      DocumentVirtuelType docVirtuel = new DocumentVirtuelType();
      FichierType objNumVir = new FichierType();
      objNumVir.setCheminEtNomDuFichier("repertoire/fichier3.pdf");
      docVirtuel.setObjetNumerique(objNumVir);
      
      ComposantDocumentVirtuelType composant = new ComposantDocumentVirtuelType();
      MetadonneeType meta5 = new MetadonneeType();
      meta5.setCode("CODE_METADONNEE_1");
      meta5.setValeur("La valeur");
      MetadonneeType meta6 = new MetadonneeType();
      meta6.setCode("CODE_METADONNEE_2");
      meta6.setValeur("La valeur");
      composant.setNumeroPageDebut(1);
      composant.setNombreDePages(2);
      ListeMetadonneeType listeM3 = new ListeMetadonneeType();
      listeM3.getMetadonnee().add(meta5);
      listeM3.getMetadonnee().add(meta6);
      
      composant.setMetadonnees(listeM3);
            
      ComposantDocumentVirtuelType composant2 = new ComposantDocumentVirtuelType();
      MetadonneeType meta7 = new MetadonneeType();
      meta7.setCode("CODE_METADONNEE_1");
      meta7.setValeur("La valeur");
      MetadonneeType meta8 = new MetadonneeType();
      meta8.setCode("CODE_METADONNEE_2");
      meta8.setValeur("La valeur");
      composant2.setNumeroPageDebut(2);
      composant2.setNombreDePages(2);
      ListeMetadonneeType listeM4 = new ListeMetadonneeType();
      listeM4.getMetadonnee().add(meta7);
      listeM4.getMetadonnee().add(meta8);
      
      composant2.setMetadonnees(listeM4);
      
      Composants composants = new Composants();
      composants.getComposant().add(composant);
      composants.getComposant().add(composant2);
      docVirtuel.setComposants(composants);
      //-- un deuxieme document virtuel qui n'as pas pu être archivé
      DocumentVirtuelType docVirtuel2 = new DocumentVirtuelType();
      FichierType objNumVir2 = new FichierType();
      objNumVir2.setCheminEtNomDuFichier("repertoire/fichier4.pdf");
      docVirtuel2.setObjetNumerique(objNumVir2);
      
      ComposantDocumentVirtuelType composant3 = new ComposantDocumentVirtuelType();
      MetadonneeType meta9 = new MetadonneeType();
      meta9.setCode("CODE_METADONNEE_1");
      meta9.setValeur("La valeur");
      MetadonneeType meta10 = new MetadonneeType();
      meta10.setCode("CODE_METADONNEE_2");
      meta10.setValeur("La valeur");
      composant3.setNumeroPageDebut(1);
      composant3.setNombreDePages(2);
      ListeMetadonneeType listeM5 = new ListeMetadonneeType();
      listeM5.getMetadonnee().add(meta9);
      listeM5.getMetadonnee().add(meta10);
      
      composant3.setMetadonnees(listeM5);
            
      ComposantDocumentVirtuelType composant4 = new ComposantDocumentVirtuelType();
      MetadonneeType meta11 = new MetadonneeType();
      meta11.setCode("CODE_METADONNEE_1");
      meta11.setValeur("La valeur");
      MetadonneeType meta12 = new MetadonneeType();
      meta12.setCode("CODE_METADONNEE_2");
      meta12.setValeur("La valeur");
      composant4.setNumeroPageDebut(2);
      composant4.setNombreDePages(2);
      ListeMetadonneeType listeM6 = new ListeMetadonneeType();
      listeM6.getMetadonnee().add(meta11);
      listeM6.getMetadonnee().add(meta12);
      
      composant4.setMetadonnees(listeM6);
      
      Composants composants2 = new Composants();
      composants2.getComposant().add(composant3);
      composants2.getComposant().add(composant4);
      docVirtuel2.setComposants(composants2);
      
      //--------------------
      ListeDocumentsVirtuelsType listeDV = new ListeDocumentsVirtuelsType();
      listeDV.getDocumentVirtuel().add(docVirtuel);
      listeDV.getDocumentVirtuel().add(docVirtuel2);
      resultats.setNonIntegratedVirtualDocuments(listeDV);
   }

   private static void initialiseResultats2() {
      
   // Non integratedDocuments -- document qui n'ont pas été archivés
      NonIntegratedDocumentType doc1 = new NonIntegratedDocumentType();
      // objet num
      FichierType objetNum = new FichierType();
      objetNum.setCheminEtNomDuFichier("repertoire/fichier1.pdf");
      doc1.setObjetNumerique(objetNum);
      
      // erreurs
      ErreurType erreur = new ErreurType();
      erreur.setCode("wsse:SecurityTokenUnavailable");
      erreur.setLibelle(" La référence au jeton de sécurité est introuvable");
      
      ErreurType erreur2 = new ErreurType();
      erreur2.setCode("vi:InvalidVI");
      erreur2.setLibelle(" Le VI est invalide");
      
      ListeErreurType listeErreur = new ListeErreurType();
      listeErreur.getErreur().add(erreur);
      listeErreur.getErreur().add(erreur2);
      
      doc1.setErreurs(listeErreur);
      
      ListeNonIntegratedDocumentsType listeN = new ListeNonIntegratedDocumentsType();
      listeN.getNonIntegratedDocument().add(doc1);
      
      resultats2.setNonIntegratedDocuments(listeN);
      
      // NonIntegratedVirtualDocument -- document virtuel qui n'ont pas été archivés
      DocumentVirtuelType docVirtuel = new DocumentVirtuelType();
      FichierType objNumVir = new FichierType();
      objNumVir.setCheminEtNomDuFichier("repertoire/fichier6.pdf");
      docVirtuel.setObjetNumerique(objNumVir);
      
      ComposantDocumentVirtuelType composant = new ComposantDocumentVirtuelType();
      MetadonneeType meta5 = new MetadonneeType();
      meta5.setCode("CODE_METADONNEE_1");
      meta5.setValeur("La valeur");
      MetadonneeType meta6 = new MetadonneeType();
      meta6.setCode("CODE_METADONNEE_2");
      meta6.setValeur("La valeur");
      composant.setNumeroPageDebut(1);
      composant.setNombreDePages(2);
      ListeMetadonneeType listeM3 = new ListeMetadonneeType();
      listeM3.getMetadonnee().add(meta5);
      listeM3.getMetadonnee().add(meta6);
      
      composant.setMetadonnees(listeM3);
            
      ComposantDocumentVirtuelType composant2 = new ComposantDocumentVirtuelType();
      MetadonneeType meta7 = new MetadonneeType();
      meta7.setCode("CODE_METADONNEE_1");
      meta7.setValeur("La valeur");
      composant2.setNumeroPageDebut(2);
      composant2.setNombreDePages(2);
      ListeMetadonneeType listeM4 = new ListeMetadonneeType();
      listeM4.getMetadonnee().add(meta7);
      
      composant2.setMetadonnees(listeM4);
      
      Composants composants = new Composants();
      composants.getComposant().add(composant);
      composants.getComposant().add(composant2);
      docVirtuel.setComposants(composants);
      
      //--------------------
      ListeDocumentsVirtuelsType listeDV = new ListeDocumentsVirtuelsType();
      listeDV.getDocumentVirtuel().add(docVirtuel);
      resultats2.setNonIntegratedVirtualDocuments(listeDV);
   }

   // Test avec un fichier en deuxieme argument
   @Test
   @Ignore("A revoir")
   public void writeResutatsXml_file_success() throws EcdeXsdException, IOException {
      File outputFile = new File(REPERTOIRE,"resultats_success_file.xml");
      service.writeResultatsXml(resultats, outputFile);
      boolean exist = false;
      if(outputFile.exists()) {
         exist = true;
      }
      assertEquals("fichier non existant", true, exist);
      // Sha-1 du fichier resultats-test001.xml (c'est le fichier attendu), Sha-1 calculé via un logiciel externe
      String fsumAttendu = "24f9c9ae38b4059a9a4f29b43f28875fbd51654c";
      String checksumObtenu = sha(new File(REPERTOIRE,"resultats_success_file.xml"));
      
      assertEquals("Sha-1 calculé est incorrect!", fsumAttendu, checksumObtenu);
   }
   // Test simple avec un outputStream
   @Test
   @Ignore("A revoir")
   public void writeResultatsXml_outputstream_success() throws EcdeXsdException, JAXBException, IOException {
      File outputFile = new File(REPERTOIRE,"resultats_success.xml");
      OutputStream output = new FileOutputStream(outputFile.getPath());
      service.writeResultatsXml(resultats, output);
      
      // Sha-1 du fichier resultats-test001.xml (c'est le fichier attendu), Sha-1 calculé via un logiciel externe
      String fsumAttendu = "24f9c9ae38b4059a9a4f29b43f28875fbd51654c";
      String checksumObtenu = sha(new File(REPERTOIRE,"resultats_success.xml"));
      
      assertEquals("Sha-1 calculé est incorrect!", fsumAttendu, checksumObtenu);
   }
   private static String sha(File file) throws IOException{
      InputStream data = new FileInputStream(file.getAbsolutePath());
      return DigestUtils.shaHex(data);
   }
   //--------------------------------------------
   // Test failure avec resultats sans batchMode
   @Test
   public void writeResultatsXml_outputstream_failure() throws IOException {
      try{  
         File outputFile = new File(REPERTOIRE,"resultats_success.xml");
         OutputStream output = new FileOutputStream(outputFile.getPath());
         service.writeResultatsXml(resultats2, output);
         fail("Une exception était attendue! " + EcdeXsdException.class);
      }catch (EcdeXsdException e) {
         assertEquals("Le message d'erreur n'est pas correct!","Une erreur de structure a été détectée sur le 'resultats.xml'.",e.getMessage());
      }
   }
   // Test failure avec resultats sans batchMode
   @Test
   public void writeResultatsXml_file_failure() throws IOException {
      try{  
         File outputFile = new File(REPERTOIRE,"resultats_success_file.xml");
         service.writeResultatsXml(resultats2, outputFile);
         fail("Une exception était attendue! " + EcdeXsdException.class);
      }catch (EcdeXsdException e) {
         assertEquals("Le message d'erreur n'est pas correct!","Une erreur de structure a été détectée sur le 'resultats.xml'.",e.getMessage());
      }
   }
}