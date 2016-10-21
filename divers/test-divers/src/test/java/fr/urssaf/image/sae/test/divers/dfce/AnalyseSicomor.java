package fr.urssaf.image.sae.test.divers.dfce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

@RunWith(BlockJUnit4ClassRunner.class)
public class AnalyseSicomor {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseSicomor.class);

   @Test
   public void analyserFichiers() throws FileNotFoundException, IOException {
      String directory = "c:/divers/a_analyser";
      
      CSVReader reader = new CSVReader(new FileReader(new File(directory, "NSICO.csv")), ';');
      Map<String, String> docs = new HashMap<String, String>(); 
      List<String> docsExport = new ArrayList<String>(); 
      
      // lecture du fichier de mise en prod
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         String uuid = nextLine[1].toUpperCase();
         String site = nextLine[2];
         docs.put(uuid, site);
      }
      
      // lecture du fichier d'export
      FileReader readerExport = new FileReader(new File(directory, "__SOMMAIRE_GNT.TXT"));
      BufferedReader bufReader = new BufferedReader(readerExport);
      String line;
      while ((line = bufReader.readLine()) != null) {
         SommaireLineMapper mapper = new SommaireLineMapper(line);
         String valeur = mapper.getDocument().get("SM_UUID").toUpperCase() + ";" + mapper.getDocument().get("sco");
         docsExport.add(valeur);
      }
      
      // comparaison
      for (String valeur : docsExport) {
         String uuid = valeur.split(";")[0];
         if (!docs.containsKey(uuid)) {
            // nouveau doc 
            LOGGER.debug("nouveau doc : {} site : {}", uuid, valeur.split(";")[1]);
         }
      }
      Iterator<Entry<String, String>> iterateur  = docs.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, String> entry = iterateur.next();
         if (!docsExport.contains(entry.getKey() + ";" + entry.getValue())) {
            LOGGER.debug("document transféré ou supprimé : {} site : {}", entry.getKey(), entry.getValue());
         }
      }
   }
   
   @Test
   public void analyserFichiers2() throws FileNotFoundException, IOException {
      String directory = "c:/divers/a_analyser";
      
      CSVReader reader = new CSVReader(new FileReader(new File(directory, "NBIEN.csv")), ';');
      Map<String, String> docs = new HashMap<String, String>(); 
      List<String> docsExport = new ArrayList<String>(); 
      
      // lecture du fichier de mise en prod
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         String uuid = nextLine[1].toUpperCase();
         String site = nextLine[2];
         docs.put(uuid, site);
      }
      
      // lecture du fichier d'export
      FileReader readerExport = new FileReader(new File(directory, "__SOMMAIRE_GNS.TXT"));
      BufferedReader bufReader = new BufferedReader(readerExport);
      String line;
      while ((line = bufReader.readLine()) != null) {
         SommaireLineMapper mapper = new SommaireLineMapper(line);
         String valeur = mapper.getDocument().get("SM_UUID").toUpperCase() + ";" + mapper.getDocument().get("sco");
         docsExport.add(valeur);
      }
      
      // comparaison
      for (String valeur : docsExport) {
         String uuid = valeur.split(";")[0];
         if (!docs.containsKey(uuid)) {
            // nouveau doc 
            LOGGER.debug("nouveau doc : {} site : {}", uuid, valeur.split(";")[1]);
         }
      }
      Iterator<Entry<String, String>> iterateur  = docs.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, String> entry = iterateur.next();
         if (!docsExport.contains(entry.getKey() + ";" + entry.getValue())) {
            LOGGER.debug("document transféré ou supprimé : {} site : {}", entry.getKey(), entry.getValue());
         }
      }
   }
   
   public class SommaireLineMapper {
      
      private Map<String, String> document;
      private String docFilename;
      private String docExtension;
      
      public SommaireLineMapper(final String line) {
         document = new HashMap<String, String>();
         initDocument(line);
      }
      
      public Map<String, String> getDocument() {
         return document;
      }

      public String getDocFilename() {
         return docFilename;
      }

      public String getDocExtension() {
         return docExtension;
      }

      /**
       * Deserialise une ligne du fichier sommaire
       * Structure d'une ligne : 
       * uuid||type||title||lifecyclerefdate||creationdate||filename||extension||CRITERIONS
       * avec CRITERIONS sous la forme : key1:|:val1:|:key2:|:val2 ...
       * 
       * @param line
       * @return
       * @throws ParseException
       */
      private void initDocument(String line) {
         
         String[] data = line.split("\\|\\|");

         //-- Champs objet document
         document.put("SM_UUID", data[0]);
         document.put("SM_DOCUMENT_TYPE", data[1]);
         document.put("SM_TITLE", data[2]);
         document.put("SM_LIFE_CYCLE_REFERENCE_DATE", data[3]);
         document.put("SM_CREATION_DATE", data[4]);
         
         //-- Infos utiles à la méthode dfce d'enregistrement 
         // du documents en base
         docFilename = data[5];
         docExtension = data[6];
         
         //-- Critérions
         for (int i = 7; i < data.length; i++) {
            
            String[] item = data[i].split(":\\|:");
            String critKey = item[0];
            String critVal = item[1];
            
            document.put(critKey, critVal);
         }
      }
   }
}
