package fr.urssaf.image.sae.saetraitementsdivers.traces;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-saeTraitementsDivers-traces-test.xml" })
public class TraceAnalyseTest {

   private static final String DATE_PATTERN_TIMESTAMP = "dd/MM/yyyy HH:mm:ss";
   private static final String DATE_PATTERN_JOURNEE = "dd/MM/yyyy";
   
   private static final DateFormat DATE_FORMAT_TIMESTAMP = new SimpleDateFormat(DATE_PATTERN_TIMESTAMP);
   private static final DateFormat DATE_FORMAT_JOURNEE = new SimpleDateFormat(DATE_PATTERN_JOURNEE);
   
   @Autowired
   private RegTechniqueService regTechniqueService ;
   
   @Test
   public void erreurs_recherche_globale() throws IOException {
      
      // On regarde les traces sur les 10 derniers jours

      int nbJoursAnalyse = 10;
      boolean ordreInverse = false;
      int taille = 5000;
      Date dateDuJour = new Date();
      String codeEvenement = "WS_RECHERCHE|KO";
      String contratService = "CS_RECHERCHE_DOCUMENTAIRE";
      File fichierSortie = new File("C:/divers/traces_erreur_rg.csv");
      List<String> exportTraces = new ArrayList<String>();
      
      
      Date dateDebut;
      Date dateFin;
      Date dateJour;
      TraceRegTechnique trace;
      String erreur;
      String requeteRecherche;
      for (int i=nbJoursAnalyse;i>=1;i--) {
         
         // Construit la plage de date de minuit à 23h59 pour la journée à analyser
         dateJour = DateUtils.addDays(dateDuJour, -1*i);
         dateDebut = buildDateDebut(dateJour);
         dateFin = buildDateFin(dateJour);
         // System.out.print(dateDebut + "=>" + dateFin + " : ");
         System.out.println(dateDebut);
         
         // Appel du service
         List<TraceRegTechniqueIndex> traces = regTechniqueService.lecture(dateDebut,
               dateFin, taille, ordreInverse);

         // Boucle 
         if (traces != null) {
            
            // System.out.println(traces.size());
            
            for (TraceRegTechniqueIndex traceIndex : traces) {
               
               // On regarde si on se trouve sur une trace d'erreur de recherche provenant la RG
               if (
                     (StringUtils.equals(traceIndex.getCodeEvt(), codeEvenement)) && 
                     (StringUtils.equals(traceIndex.getContrat(), contratService))) {
                  
                  // On récupère le détail de la trace
                  trace = regTechniqueService.lecture(traceIndex.getIdentifiant());
                  
                  // On extrait l'erreur et la requête de recherche
                  erreur = extraitErreur(trace);
                  
                  // On extrait la requête de recherche
                  requeteRecherche = extraitRequeteRecherche(trace);
                  
                  // Compilation des éléments
                  exportTraces.add(
                        DATE_FORMAT_TIMESTAMP.format(trace.getTimestamp()) + ";" +
                        DATE_FORMAT_JOURNEE.format(trace.getTimestamp()) + ";" +
                        erreur + ";" + 
                        requeteRecherche);
                  
               }
               
               
               
            }
            
         } else {
            
            // System.out.println("0");
            
         }
         
         
         
      }
      
      // Sortie des traces dans un fichier
      FileOutputStream fos = new FileOutputStream(fichierSortie);
      try {
         IOUtils.writeLines(exportTraces, null, fos);
      } finally {
        if (fos!=null) {
           fos.close();
        }
      }
      
   }
   
   private Date buildDateDebut(Date laDate) {
      
      Date result = DateUtils.setHours(laDate, 0);
      result = DateUtils.setMinutes(result, 0);
      result = DateUtils.setSeconds(result, 0);
      result = DateUtils.setMilliseconds(result, 0);
      return result;
      
   }
   
   private Date buildDateFin(Date laDate) {
      
      Date result = DateUtils.setHours(laDate, 23);
      result = DateUtils.setMinutes(result, 59);
      result = DateUtils.setSeconds(result, 59);
      result = DateUtils.setMilliseconds(result, 999);
      return result;
      
   }
   
   private String extraitErreur(TraceRegTechnique trace) {
      
      int debut = trace.getStacktrace().indexOf(":");
      int fin = trace.getStacktrace().indexOf("\n");
      
      return trace.getStacktrace().substring(debut +1, fin).trim();
      
   }
   
   private String extraitRequeteRecherche(TraceRegTechnique trace) {
      
      String soapRequest = (String) trace.getInfos().get("soapRequest");
      
      int debut = soapRequest.indexOf("<ns1:requete>") + "<ns1:requete>".length();
      int fin = soapRequest.indexOf("</ns1:requete>");
      
      return soapRequest.substring(debut, fin).trim();
      
      
   }
   
   
}
