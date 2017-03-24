package fr.urssaf.image.commons.controller.spring3.exemple.controller.table;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-servlet.xml")
public class TableControllerTest {

   @Autowired
   private TableController controller;
   
   @Autowired
   private ApplicationContext applicationContext;

   private MockHttpServletRequest request;

   private MockHttpServletResponse response;

   private AnnotationMethodHandlerAdapter handlerAdapter;

   @Before
   public void before() {

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();
      handlerAdapter = applicationContext
            .getBean(AnnotationMethodHandlerAdapter.class);

   }

   @Test
   public void getDefaultView() throws Exception {

      request.setMethod("GET");

      ModelAndView model = handlerAdapter.handle(request, response, controller);

      assertEquals("table/tableView", model.getViewName());

   }

}
