package fr.urssaf.image.commons.controller.springsecurity.exemple.controller;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import fr.urssaf.image.commons.controller.springsecurity.exemple.authenticate.SecurityUser;
import fr.urssaf.image.commons.controller.springsecurity.exemple.service.AuthenticateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml",
      "/security/applicationContext-security-aspect.xml",
      "/applicationContext-security-basic.xml", "/spring-servlet-test.xml" })
public class PageFormControllerTest {

   @Autowired
   private ApplicationContext context;

   @Autowired
   private PageFormController controller;

   @Autowired
   private AuthenticateService authenticateService;

   private MockHttpServletResponse response;

   private AnnotationMethodHandlerAdapter handlerAdapter;

   private MockHttpServletRequest request;

   @Before
   public void before() {

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();

      handlerAdapter = context.getBean(AnnotationMethodHandlerAdapter.class);

      SecurityUser securityUser = authenticateService.find("user");

      Authentication authentication = new UsernamePasswordAuthenticationToken(
            securityUser, securityUser.getPassword(), securityUser
                  .getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

   }

   @After
   public void after() {

      SecurityContextHolder.clearContext();
   }

   @Test
   public void saveValid() throws Exception {

      request.setMethod("POST");
      request.setParameter("title", "title");
      request.setParameter("text", "text");

      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("page/pageForm", model.getViewName());
   }

   @Test
   public void saveInvalid() throws Exception {

      request.setMethod("POST");
      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("page/pageForm", model.getViewName());
   }

   @Test
   public void populate() throws Exception {

      request.setMethod("POST");
      request.setParameter("populate", "");
      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("page/pageForm", model.getViewName());
   }

   @Test
   public void getDefaultView() throws Exception {

      request.setMethod("GET");
      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("page/pageForm", model.getViewName());
   }

}
