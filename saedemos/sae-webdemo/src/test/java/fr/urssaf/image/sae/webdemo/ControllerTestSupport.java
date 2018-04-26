package fr.urssaf.image.sae.webdemo;

import org.apache.log4j.Logger;
import org.junit.Before;
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

   protected static final Logger LOG = Logger
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

         return model;

      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

   }

   protected final String json(C controller) {

      try {
         handlerAdapter.handle(request, response, controller);

         return response.getContentAsString();

      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

   }

   public Object getAttributeSession(String name) {
      return request.getSession().getAttribute(name);
   }

   public void setURI(String requestURI) {

      this.request.setRequestURI(requestURI);
   }

   public void setSession(String name, Object value) {

      this.request.getSession().setAttribute(name, value);
   }
   
   public int getStatut(){
      return response.getStatus();
   }

}
