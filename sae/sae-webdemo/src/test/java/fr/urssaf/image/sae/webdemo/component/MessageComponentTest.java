package fr.urssaf.image.sae.webdemo.component;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
@SuppressWarnings("PMD")
public class MessageComponentTest {

   private MockHttpServletRequest request;

   private static final String MSG_TEST_CODE = "msg.test";

   private static final String MSG_TEST = "l'abîme regarde en toi";

   private static final String MSG_FAILURE_CODE = "msg.notexist";
   
   private static final String MSG_DEFAULT = "default";

   @Before
   public void before() {

      request = new MockHttpServletRequest();
   }

   @Test(expected = IllegalArgumentException.class)
   public void componentFailure() {

      MessageComponent component = new MessageComponent();
      component.setMessageSource(null);

   }

   @Test
   public void getMessageSuccess() {

      assertEquals(MSG_TEST, MessageComponent
            .getMessage(MSG_TEST_CODE, request));
   }

   @Test
   public void getMessageFailure() {

      assertEquals(MSG_FAILURE_CODE, MessageComponent.getMessage(
            MSG_FAILURE_CODE, request));
   }

   @Test
   public void getMessageDefaultSuccess() {

      assertEquals(MSG_TEST, MessageComponent.getMessage(MSG_TEST_CODE,
            MSG_DEFAULT, request));
   }

   @Test
   public void getMessageDefaultFailure() {

      assertEquals(MSG_DEFAULT, MessageComponent.getMessage(MSG_FAILURE_CODE,
            MSG_DEFAULT, request));
   }
}
