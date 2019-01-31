package org.springframework.batch.admin.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class LocalFileServiceIntegrationTests {

  private static Log logger = LogFactory.getLog(LocalFileServiceIntegrationTests.class);

  @Autowired
  private LocalFileService service;

  @Autowired
  @Qualifier("input-files")
  private SubscribableChannel files;

  private static File trigger;

  private static MessageHandler handler = new MessageHandler() {
    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
      logger.debug("Handled " + message);
      trigger = (File) message.getPayload();
    }
  };

  @Before
  public void setUp() throws Exception {
    FileUtils.deleteDirectory(service.getUploadDirectory());
    files.unsubscribe(handler);
    files.subscribe(handler);
  }

  @Test
  public void testUpload() throws Exception {
    final FileInfo info = service.createFile("spam/bucket");
    final Resource file = service.getResource(info.getPath());
    assertTrue(file.exists());
    assertTrue(file.getFile().getParentFile().exists());
  }

  @Test
  public void testTrigger() throws Exception {
    final FileInfo info = service.createFile("spam/bucket/crap");
    final Resource file = service.getResource(info.getPath());
    assertTrue(file.exists());
    service.publish(info);
    assertNotNull(trigger);
  }

}
