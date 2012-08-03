package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.regionalisation.service.SearchCriterionService;

@SuppressWarnings("PMD.MethodNamingConventions")
public class SearchCriterionServiceValidationTest {

   private SearchCriterionService service;

   @Before
   public void before() {

      service = new SearchCriterionService() {

         @Override
         public void enregistrerSearchCriterion(File searchCriterionCvs)
               throws IOException {

            // aucune implémentation

         }

      };
   }

   @Test
   public void enregistrerSearchCriterion_failure() throws IOException {

      try {

         service.enregistrerSearchCriterion(null);

         Assert.fail("une exception IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Le paramètre 'searchCriterionCvs' doit être renseigné.", e
                     .getMessage());

      }
   }
}
