/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator.CassandraIterator;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
public class ParcoursDocumentsTest {

   @Autowired
   private TraitementService service;

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private DocInfoDao infoDao;

   @Test
   @Ignore
   public void listeDocumentsNonMigres() throws CassandraException {

      service
            .writeDocStartingWithCodeOrga(
                  "c:/donnees.csv",
                  "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Régionalisation/"
                        + "Vague 2 (fin 2012)/Programme/correspondances_orga.properties");
   }

   @Test
   @Ignore
   public void extractMetaValues() {

      List<String> metas = Arrays.asList("nce", "npe", "nci");
      String cheminRep = "c:/donnees";

      List<String> addedMetas = Arrays.asList("SM_UUID", "cop", "cog");
      
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.addAll(addedMetas);
      reqMetas.addAll(metas);
      
      File rep = new File(cheminRep);
      if (!rep.exists()) {
         rep.mkdir();
      }
      List<Writer> writers = new ArrayList<Writer>();

      try {
         cassandraSupport.connect();

         File fichier;
         Writer writer;
         for (String meta : metas) {
            fichier = new File(rep, "donnees_" + meta + ".csv");
            writer = new FileWriter(fichier);
            writers.add(writer);
         }

         AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
               .toArray(new String[0]));
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);
         
         Map<String, String> map;
         String meta, value;
         
         int nbDocsTraites = 0;
         
         while (iterator.hasNext()) {
            map = iterator.next();
            
            for (int i = 0; i < metas.size(); i++) {
               meta = metas.get(i);
               value = map.get(meta);
               if (StringUtils.isNotBlank(value)) {
                  writer = writers.get(i);
                  for (String metaName : addedMetas) {
                     writer.write(map.get(metaName));
                     writer.write(";");
                  }
                  writer.write(value);
                  writer.write("\n");
               }
            }
            
            nbDocsTraites++;
            if ((nbDocsTraites%1000)==0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }
            
         }
         
         System.out.println("Nombre de docs traités : " + (nbDocsTraites-1));

      } catch (IOException exception) {
         System.err.println(exception);

      } finally {
         closeWriters(writers);
         cassandraSupport.disconnect();
      }

   }

   private void closeWriters(List<Writer> writers) {
      for (Writer writer : writers) {
         closeWriter(writer);
      }
   }

   private void closeWriter(Writer writer) {
      try {
         if (writer != null) {
            writer.close();
         }
      } catch (IOException exception) {
         System.err.println("impossible de fermer le flux");
      }
   }
}
