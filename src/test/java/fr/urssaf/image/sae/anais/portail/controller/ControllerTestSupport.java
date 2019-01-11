package fr.urssaf.image.sae.anais.portail.controller;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * Classe mère de tous les tests unitaires sur les contrôleurs
 * 
 * 
 * @param <C>
 *           type de contrôleur
 */
public class ControllerTestSupport<C> {

   protected static final Logger LOG = LoggerFactory
         .getLogger(ControllerTestSupport.class);

   @Autowired
   private ApplicationContext context;

   private MockHttpServletResponse response;

   private AnnotationMethodHandlerAdapter handlerAdapter;

   private MockHttpServletRequest request;

   /**
    * instanciation avec chaque test
    * <ul>
    * <li>{@link MockHttpServletRequest}</li>
    * <li>{@link MockHttpServletResponse}</li>
    * <li>{@AnnotationMethodHandlerAdapter}</li>
    * </ul>
    * 
    */
   @Before
   public final void before() {

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();

      handlerAdapter = context.getBean(AnnotationMethodHandlerAdapter.class);

   }

   /**
    * <code>request.setMethod("POST");</code>
    */
   protected final void initPost() {
      request.setMethod("POST");
   }

   /**
    * <code>request.setMethod("GET");</code>
    */
   protected final void initGet() {
      request.setMethod("GET");
   }

   /**
    * <code>request.setParameter(name, value);</code>
    * 
    * @param name
    *           nom du champ
    * @param value
    *           valeur du champ
    */
   protected final void initParameter(String name, String value) {
      if (value != null) {
         request.setParameter(name, value);
      }
   }

   /**
    * appelle la méthode
    * {@link AnnotationMethodHandlerAdapter#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, Object)
    * )}
    * 
    * @param controller
    *           contrôleur à tester
    * @return vue renvoyée par l'action
    */
   protected final ModelAndView handle(C controller) {

      try {
         ModelAndView model = handlerAdapter.handle(request, response,
               controller);

         for (String key : model.getModel().keySet()) {

            LOG.debug(key + " --> " + model.getModel().get(key));
         }

         return model;

      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

   }

}
