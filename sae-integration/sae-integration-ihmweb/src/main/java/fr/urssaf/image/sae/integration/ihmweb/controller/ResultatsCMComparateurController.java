package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ResultatsCMComparateurFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.cmcompare.EnumCmCompare;
import fr.urssaf.image.sae.integration.ihmweb.modele.cmcompare.ResultatCmCompare;

/**
 * 
 * Controller pour l'affichage des comparaisons entre des passes d'intégration
 * sur la capture de masse
 */
@Controller
@RequestMapping(value = "cmcompare")
public class ResultatsCMComparateurController {

   
   @Autowired
   private TestConfig testConfig;
   
   
   /**
    * Le GET
    * 
    * @param model
    *           le modèle
    * 
    * @return le nom de la vue
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      // Ajoute l'objet du modèle
      ResultatsCMComparateurFormulaire form = new ResultatsCMComparateurFormulaire();
      model.addAttribute("formulaire", form);
      
      // Initialisation des répertoires des résultats à comparer
      form.setRepRef(testConfig.getCmCompareRepRef());
      form.setRepPasse(testConfig.getCmCompareRepPasse());
      
      // Lance l'analyse
      List<ResultatCmCompare> listeFichiers = analyse(form);
      
      // Affecte le résultat de l'analyse au formulaire
      form.setListeFichiers(listeFichiers);
      
      // Renvoie le nom de la vue
      return "cmcompare";
      
   }
   
   
   
   private List<ResultatCmCompare> analyse(ResultatsCMComparateurFormulaire form) {
      
      List<ResultatCmCompare> resultats = new ArrayList<ResultatCmCompare>();
      
      
      String pathRef = form.getRepRef();
      String pathPasse = form.getRepPasse();
      
      
      File repRef = new File(pathRef);
      List<File> fichiersRef = listeFichiersResultats(repRef);
      
      File repPasse = new File(pathPasse);
      List<File> fichiersPasse = listeFichiersResultats(repPasse);
      
      // Union des 2 listes
      List<String> fichiersAll = unionNomsFichiers(fichiersRef, fichiersPasse);
      
      // Tri par ordre alphabétique
      Collections.sort(fichiersAll);
      
      // Et on boucle, boucle, boucle
      ResultatCmCompare resultatCm;
      File fileDansRef;
      File fileDansPasse;
      EnumCmCompare resCompare;
      for (String nomFichier: fichiersAll) {
         
         resultatCm = new ResultatCmCompare();
         
         fileDansRef = new File(pathRef, nomFichier);
         fileDansPasse = new File(pathPasse, nomFichier);
                  
         if ((fichiersRef.contains(fileDansRef)) && (fichiersPasse.contains(fileDansPasse))) {
         
            // Fichier présent dans la passe de référence et dans la passe à vérifier
            
            resultatCm.setEtatReference(EnumCmCompare.REFERENCE);
            
            resCompare = compareFichiersResultats(fileDansRef,fileDansPasse);
            resultatCm.setEtatPasse(resCompare);
                        
         } else if (fichiersRef.contains(fileDansRef)) { 
            
            // Fichier présent uniquement dans la passe de référence
            resultatCm.setEtatReference(EnumCmCompare.REFERENCE);
            resultatCm.setEtatPasse(EnumCmCompare.EN_MOINS);
            
            
         } else {
            
            // Fichier présent uniquement dans la passe à vérifier
            resultatCm.setEtatReference(EnumCmCompare.MANQUANT);
            resultatCm.setEtatPasse(EnumCmCompare.EN_PLUS);
            
         }
         
         
         resultatCm.setNomFichier(nomFichier);
         
         
         resultats.add(resultatCm);
         
      }
      
      
      return resultats;
      
   }
   
   
   
   private List<File> listeFichiersResultats(File repertoire) {
      
      List<File> filesList = new ArrayList<File>();
      
      Collection<File> filesColl = FileUtils.listFiles(repertoire, new String[] {"xml"}, false);
      
      filesList.addAll(filesColl);
      
      return filesList;
      
   }
   
   
   private List<String> unionNomsFichiers(
         List<File> liste1,
         List<File> liste2) {
      
      List<String> resultat = new ArrayList<String>();
      
      
      if (liste1!=null) {
         for(File file: liste1) {
            resultat.add(file.getName());
         }
      }
      
      if (liste2!=null) {
         for(File file: liste2) {
            
            if (resultat.indexOf(file.getName())==-1) {
               
               resultat.add(file.getName());
               
            }
            
         }
      }
      
      return resultat;
      
   }
   
   
   private EnumCmCompare compareFichiersResultats(File fileDansRef, File fileDansPasse) {
      
      String sha1FileRef = calculeSha1(fileDansRef);
      
      String sha1FileDansPasse = calculeSha1(fileDansPasse);
      
      if (StringUtils.equals(sha1FileRef, sha1FileDansPasse)) {
         return EnumCmCompare.IDENTIQUE_REFERENCE;
      } else {
         return EnumCmCompare.DIFFERENT_REFERENCE;
      }
      
   }
   
   
   private String calculeSha1(File file) {
      
      try {
         
         FileInputStream fis = new FileInputStream(file) ;
         try {
            
            return DigestUtils.shaHex(fis);
            
         } finally {
            if (fis!=null) {
               fis.close();
            }
         }
      
      
      } catch (IOException ex) {
         throw new IntegrationRuntimeException(ex);
      }
      
   }

   
   
   @RequestMapping(method = RequestMethod.POST)
   protected final String post(
         Model model,
         @ModelAttribute("formulaire") ResultatsCMComparateurFormulaire form) {

      
      // Lance l'analyse
      List<ResultatCmCompare> listeFichiers = analyse(form);
      
      // Affecte le résultat de l'analyse au formulaire
      form.setListeFichiers(listeFichiers);
      
      
      return "cmcompare";

   }
   
   
   
}
