package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-test.xml" })
public class RefMetaInitialisationServiceTest {

   @Autowired
   private RefMetaInitialisationService refMetaService;

   @Test
   public void chargeFichierMeta_test() throws JAXBException, SAXException, IOException {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      Assert.assertEquals("Le nombre de métadonnées attendu est incorrect", 129,
            metadonnees.size());
   }

   @Test
   public void genereFichierXmlAncienneVersion_test() throws JAXBException, SAXException, IOException {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      List<String> lignes = refMetaService
            .genereFichierXmlAncienneVersionRefMeta(metadonnees);
       
//       try {
//          //-- Ecriture dans un fichier temporaire, pour mieux visualiser
//          File fileTemp = new File("c:/divers/refmeta_verif1.xml");
//          FileUtils.writeLines(fileTemp, lignes);
//       } catch (IOException e) {
//          throw new MajLotRuntimeException(e);
//       }

      Assert.assertEquals("Le nombre de lignes attendu est incorrect", 2067, lignes.size());
   }

   @Test
   public void verification1_test() throws JAXBException, SAXException, IOException {
      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();
      refMetaService.verification1(metadonnees);
   }

   @Test
   public void genereFichierXmlAncienneVersionBaseDfce_test() throws JAXBException, SAXException, IOException {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      List<String> lignes = refMetaService
            .genereFichierXmlAncienneVersionBaseDfce(metadonnees);
       
//       try {
//          //-- Ecriture dans un fichier temporaire, pour mieux visualiser
//          File fileTemp = new File("c:/divers/refmeta_verif2.xml");
//          FileUtils.writeLines(fileTemp, lignes);
//       } catch (IOException e) {
//          throw new MajLotRuntimeException(e);
//       }

      Assert.assertEquals("Le nombre de lignes attendu est incorrect", 1107, lignes.size());

   }

   @Test
   public void verification2_test() throws JAXBException, SAXException, IOException {
      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();
      refMetaService.verification2(metadonnees);
   }

   /**
    * Ceci n'est pas un vrai TU<br>
    * <br>
    * Il s'agit de générer le dataset du Cassandra local à partir du fichier des
    * métadonnées utilisé par RefMetaInitialisationService. Et ceci afin
    * d'éviter les erreurs de saisie !
    * @throws IOException 
    * @throws SAXException 
    * @throws JAXBException 
    */
   @Test
   //@Ignore("Ceci n'est pas un vrai TU")
   public void genereDatasetCassandraLocal() throws JAXBException, SAXException, IOException {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();
      List<String> dataSet = new ArrayList<String>();

      for (MetadataReference metadonnee : metadonnees) {
         dataSet.add("         <row>");
         dataSet.add(String.format("            <key>%s</key>", metadonnee
               .getLongCode()));
         dataSet.add("            <column>");
         dataSet.add("               <name>sCode</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getShortCode()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>type</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getType()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>reqArch</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isRequiredForArchival())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>reqStor</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isRequiredForStorage())));
         dataSet.add("            </column>");
         if (metadonnee.getLength() > 0) {
            dataSet.add("            <column>");
            dataSet.add("               <name>length</name>");
            dataSet.add(String.format("               <value>%s</value>",
                  intToStringForDataset(metadonnee.getLength())));
            dataSet.add("            </column>");
         }
         dataSet.add("            <column>");
         dataSet.add("               <name>cons</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isConsultable())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>defCons</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isDefaultConsultable())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>search</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isSearchable())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>int</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isInternal())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>arch</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isArchivable())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>label</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getLabel()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>descr</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getDescription()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>pattern</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getPattern()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>hasDict</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.getHasDictionary())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>dictName</name>");
         dataSet.add(String.format("               <value>%s</value>",
               metadonnee.getDictionaryName()));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>index</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.getIsIndexed())));
         dataSet.add("            </column>");
         dataSet.add("            <column>");
         dataSet.add("               <name>update</name>");
         dataSet.add(String.format("               <value>%s</value>",
               boolToStringForDataset(metadonnee.isModifiable())));
         dataSet.add("            </column>");
         dataSet.add("         </row>");
         dataSet.add("");
      }

//      // Ecriture dans un fichier temporaire, pour mieux visualiser
//      try {
//         File fileTemp = new File("c:/divers/bout-de-dataset-metadata.xml");
//         FileUtils.writeLines(fileTemp, dataSet);
//      } catch (IOException e) {
//         throw new MajLotRuntimeException(e);
//      }

   }
   
   @Test
   public void chargerFichierIdxCompositesTest() throws IOException, JAXBException, SAXException{
      String message = "";
      
      List<String[]> indexes = refMetaService.chargerFichierIdxComposites(true);  
      
      //-- Test version du fichiers des indexes composites
      message = "Le nombre d'indexes attendu (fichier v3.3) est incorrect";
      Assert.assertEquals(message, 20, indexes.size());
   }
   
   @Test
   public void chargerFichierIdxCompositesASupprimerTest() throws IOException, JAXBException, SAXException{
      String message = "";
      
      List<String[]> indexes = refMetaService.chargerFichierIdxComposites(false);  
      
      //-- Test version du fichiers des indexes composites
      message = "Le nombre d'indexes attendu (fichier v3.3) est incorrect";
      Assert.assertEquals(message, 4, indexes.size());
   }

   private String boolToStringForDataset(boolean value) {
      if (value) {
         return "bytes(01)";
      } else {
         return "bytes(00)";
      }
   }

   private String intToStringForDataset(int value) {
      return "integer(" + value + ")";
   }
}
