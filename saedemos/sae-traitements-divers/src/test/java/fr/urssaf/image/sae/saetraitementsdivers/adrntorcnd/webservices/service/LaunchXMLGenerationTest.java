/**
 * 
 */
package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.service;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.exception.AdrnToRcndException;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.modele.BeanRNDTypeDocument;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.service.DataManagementInterface;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.service.DuplicationInterface;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.utils.PredicateEtatRNDTypeDoc;

/**
 * Attention. Ceci n'est pas une interface de test. Cette interface sert à
 * générer les fichier de compatibilité avec CNA
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-saeTraitementsDivers-test.xml" })
public class LaunchXMLGenerationTest {

   /**
    * 
    */
   private static final String PATTERN_CODE_ACTIVITE = "[a-xA-X0]";

   private static final Logger LOG = LoggerFactory
         .getLogger(LaunchXMLGenerationTest.class);

   @Autowired
   private DuplicationInterface duplicationI;

   @Autowired
   private DataManagementInterface managementI;

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void runFromConfigurationFile() throws AdrnToRcndException {

      String version = duplicationI.getVersionFromConfigFile();

      LOG.debug("numéro de version = " + version);

      List<BeanRNDTypeDocument> typeDoc = duplicationI
            .getDocumentTypesFromConfigFile();

      /* Suppression des éléments dont is_etat=true */
      PredicateEtatRNDTypeDoc predicate = new PredicateEtatRNDTypeDoc();
      CollectionUtils.filter(typeDoc, predicate);

      for (BeanRNDTypeDocument beanRNDTypeDocument : typeDoc) {
         if (StringUtils.isEmpty(beanRNDTypeDocument.getCodeActivite())
               || Pattern.matches(PATTERN_CODE_ACTIVITE, beanRNDTypeDocument
                     .getCodeActivite())) {
            beanRNDTypeDocument.setCodeActivite(null);
         }
      }

      managementI.saveDocuments(typeDoc, version);

   }

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore
   public void runFromWSCall() throws AdrnToRcndException {

      String version = duplicationI.getVersionFromWS();

      List<BeanRNDTypeDocument> typeDoc = duplicationI.getDocumentTypesFromWS();

      /* Suppression des éléments dont is_etat=true */
      PredicateEtatRNDTypeDoc predicate = new PredicateEtatRNDTypeDoc();
      CollectionUtils.filter(typeDoc, predicate);

      for (BeanRNDTypeDocument beanRNDTypeDocument : typeDoc) {
         if (StringUtils.isEmpty(beanRNDTypeDocument.getCodeActivite())
               || Pattern.matches(PATTERN_CODE_ACTIVITE, beanRNDTypeDocument
                     .getCodeActivite())) {
            beanRNDTypeDocument.setCodeActivite(null);
         }
      }

      managementI.saveDocuments(typeDoc, version);

   }

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore
   public void runLiveCycleFromWSCall() throws AdrnToRcndException {
      String version = duplicationI.getVersionFromWS();

      List<BeanRNDTypeDocument> typeDoc = duplicationI.getDocumentTypesFromWS();

      /* Suppression des éléments dont is_etat=true */
      PredicateEtatRNDTypeDoc predicate = new PredicateEtatRNDTypeDoc();
      CollectionUtils.filter(typeDoc, predicate);

      managementI.saveLiveCycle(typeDoc, version);

   }

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore
   public void runLiveCycleFromConfig() throws AdrnToRcndException {
      String version = duplicationI.getVersionFromConfigFile();

      List<BeanRNDTypeDocument> typeDoc = duplicationI
            .getDocumentTypesFromConfigFile();

      /* Suppression des éléments dont is_etat=true */
      PredicateEtatRNDTypeDoc predicate = new PredicateEtatRNDTypeDoc();
      CollectionUtils.filter(typeDoc, predicate);

      managementI.saveLiveCycle(typeDoc, version);

   }
}
