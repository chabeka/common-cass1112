package fr.urssaf.image.commons.web.validator;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD")
public class NotEmptyTest {

   private Form form;
   
   private Validator validator;

   @Before
   public void before() {

      form = new Form();
      
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

      validator = factory.getValidator();
   }

   @Test
   public void success() {

      form.setNotEmpty("no empty");
     
      Set<ConstraintViolation<Form>> constraintViolations = validator.validate(form);
      assertEquals(0, constraintViolations.size());

   }

   @Test
   public void failure() {

      this.assertFailure(" ");
      this.assertFailure("");
      this.assertFailure(null);
      
   }
   
   private void assertFailure(String value){
      
      form.setNotEmpty(value);
      
      Set<ConstraintViolation<Form>> constraintViolations = validator.validate(form);
      assertEquals(1, constraintViolations.size());
      assertEquals("ne peut pas être vide", constraintViolations.iterator().next().getMessage());
   }

   public static class Form {

      private String notEmpty;

      public void setNotEmpty(String notEmpty) {
         this.notEmpty = notEmpty;
      }

      @NotEmpty
      public String getNotEmpty() {
         return notEmpty;
      }

   }
}
