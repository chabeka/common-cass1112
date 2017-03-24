package fr.urssaf.image.commons.controller.spring3.exemple.controller.form;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-servlet.xml")
public class FormControllerTest {

   @Autowired
   private FormController controller;

   @Autowired
   private ApplicationContext applicationContext;

   private MockHttpServletRequest request;

   private MockHttpServletResponse response;

   private AnnotationMethodHandlerAdapter handlerAdapter;

   @Before
   public void init() {

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();
      handlerAdapter = applicationContext
            .getBean(AnnotationMethodHandlerAdapter.class);

      request.setMethod("POST");

      request.setParameter("closeDate", "2009-12-12");
      request.setParameter("openDate", "12/12/2008");
      request.setParameter("titre", "titre");
      request.setParameter("etat", "close");
      request.setParameter("level", "1");
      request.setParameter("interneFormulaire.comment", "commentaire");

   }

   @Test
   public void save() throws Exception {

      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("redirect:/table.do", model.getViewName());

   }

   @Test
   public void saveFailure() throws Exception {

      request.setParameter("closeDate", "12/12/2009");
      request.setParameter("titre", " ");

      ModelAndView model = handlerAdapter.handle(request, response, controller);
      
      BindingResult result = BindingResultUtils.getBindingResult(model
            .getModel(), "formFormulaire");

      assertEquals(2, result.getErrorCount());
      assertEquals("NotEmpty", result.getFieldError("titre").getCode());
      assertEquals("typeMismatch", result.getFieldError("closeDate").getCode());

      assertEquals("form/formView", model.getViewName());

   }

}
