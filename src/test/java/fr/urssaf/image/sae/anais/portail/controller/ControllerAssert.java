package fr.urssaf.image.sae.anais.portail.controller;

import static org.junit.Assert.assertEquals;

import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Classe de tests pour un contrôleur<br>
 * 
 * 
 * @param <C>
 *           classe du contrôleur
 */
public class ControllerAssert<C> {

   private final ControllerTestSupport<C> test;
   private final C controller;

   /**
    * initialisation de la classe
    * 
    * @param test
    *           classe de test du contrôleur
    * @param controller
    *           contrôleur testé
    */
   public ControllerAssert(ControllerTestSupport<C> test, C controller) {
      this.controller = controller;
      this.test = test;
   }

   /**
    * test sur des exceptions
    * 
    * @param field
    *           champ à tester
    * @param value
    *           valeur du champ (elle doit générer une exception)
    * @param form
    *           nom du formulaire
    * @param code
    *           code de l'erreur
    * @return erreurs du formulaire
    */
   public final BindingResult assertError(String field, String value,
         String form, String code) {

      test.initParameter(field, value);
      ModelAndView model = test.handle(controller);

      BindingResult result = BindingResultUtils.getBindingResult(model
            .getModel(), form);

      assertEquals(1, result.getErrorCount());
      assertEquals(code, result.getFieldError(field).getCode());

      return result;
   }

   /**
    * test sur les exceptions<br>
    * 
    * @see #assertError(String, String, String, String)
    * 
    * @param field
    *           champ à tester
    * @param value
    *           valeur du champ (elle doit générer une exception)
    * @param form
    *           nom du formulaire
    * @param code
    *           code de l'erreur
    * @param message
    *           message de l'erreur
    */
   public final void assertError(String field, String value, String form,
         String code, String message) {

      BindingResult result = assertError(field, value, form, code);
      assertEquals(message, result.getFieldError(field).getDefaultMessage());
   }

   /**
    * Test sur la vue
    * 
    * @param view
    *           nom de la vue
    */
   public final void assertView(String view) {

      assertEquals(view, test.handle(controller).getViewName());

   }

}
