package fr.urssaf.image.commons.junit.spring.service;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.junit.spring.exception.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class Service1Test {

   @Autowired
   private Service1 service1;

   @Autowired
   private Service2 service2;

   @After
   public void after() {
      EasyMock.reset(service2);
   }

   @Test
   public void service_success() throws ServiceException {

      EasyMock.expect(service2.service("requête success")).andReturn(
            "réponse attendue");

      EasyMock.replay(service2);

      assertEquals("réponse non attendue", "réponse attendue", service1
            .service("requête success"));
   }

   @Test(expected = ServiceException.class)
   public void service_failure() throws ServiceException {

      EasyMock.expect(service2.service("requête failure")).andThrow(
            new ServiceException());

      EasyMock.replay(service2);

      service1.service("requête failure");
   }
}
