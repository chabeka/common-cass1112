package org.springframework.batch.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@MessageEndpoint
public class SmokeTests {

  @Autowired
  private MessageChannel smokein;

  @Autowired
  private PollableChannel smokeout;

  // This has to be static because Spring Integration registers the handler
  // more than once (every time a test instance is created), but only one of
  // them will get the message.
  private volatile static int count = 0;

  @ServiceActivator(inputChannel = "smokein", outputChannel = "smokeout")
  public String process(final String message) {
    count++;
    final String result = message + ": " + count;
    return result;
  }

  @Test
  public void testDummyWithSimpleAssert() throws Exception {
    assertTrue(true);
  }

  @Test
  public void testVanillaSendAndReceive() throws Exception {
    smokein.send(new GenericMessage<>("foo"));
    @SuppressWarnings("unchecked")
    final Message<String> message = (Message<String>) smokeout.receive(100);
    final String result = message == null ? null : message.getPayload();
    assertEquals("foo: 1", result);
    assertEquals(1, count);
  }

}
