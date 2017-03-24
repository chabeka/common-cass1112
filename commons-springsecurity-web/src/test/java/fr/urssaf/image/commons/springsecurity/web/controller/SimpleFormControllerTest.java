package fr.urssaf.image.commons.springsecurity.web.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import fr.urssaf.image.commons.springsecurity.web.authenticate.AuthenticateComponent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/applicationContext-security.xml", "/spring-servlet-test.xml" })
public class SimpleFormControllerTest {

   @Autowired
   private ApplicationContext context;

   @Autowired
   private SimpleFormController controller;

   @Autowired
   private AuthenticateComponent auth;

   private MockHttpServletResponse response;

   private AnnotationMethodHandlerAdapter handlerAdapter;

   private MockHttpServletRequest request;

   private static final String VIEW = "simpleForm";

   @Before
   public void before() {

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();

      handlerAdapter = context.getBean(AnnotationMethodHandlerAdapter.class);

   }

   @Test
   public void saveSuccess() throws Exception {

      auth.authenticate("admin");

      request.setMethod("POST");
      request.setParameter("title", "title");
      request.setParameter("text", "text");

      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals(VIEW, model.getViewName());
   }

   @Test
   public void saveFailure() throws Exception {

      auth.authenticate("user");

      request.setMethod("POST");
      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals(VIEW, model.getViewName());
   }

   @Test(expected = AccessDeniedException.class)
   public void saveDenied() throws Exception {

      auth.authenticate("user");

      request.setMethod("POST");
      request.setParameter("title", "title");
      request.setParameter("text", "text");

      handlerAdapter.handle(request, response, controller);

   }

   @Test
   public void initSuccess() throws Exception {

      auth.authenticate("user");

      request.setMethod("GET");
      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals(VIEW, model.getViewName());
   }

   @Test(expected = AccessDeniedException.class)
   public void initDenied() throws Exception {

      auth.authenticate("auth");

      request.setMethod("GET");
      handlerAdapter.handle(request, response, controller);

   }

}
