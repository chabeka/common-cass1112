package fr.urssaf.image.commons.webservice.spring.ws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml"})
public class HolidayEndpointTest {

   @Autowired
   private ApplicationContext context;

   private MockWebServiceClient mockClient;

   @Before
   public void createClient() {
      mockClient = MockWebServiceClient.createClient(context);

   }

   @Test
   public void handleHolidayRequest() throws FileNotFoundException {

      FileInputStream input = new FileInputStream(
            "src/test/resources/holidayRequest.xml");
      Source requestPayload = new StreamSource(input);

      String response = "<HolidayResponse ";
      response = response.concat("xmlns='http://mycompany.com/hr/schemas'>");
      response = response.concat("<echo>demande enregistrée</echo>");
      response = response.concat("</HolidayResponse>");

      Source responsePayload = new StringSource(response);

      mockClient.sendRequest(RequestCreators.withPayload(requestPayload))
            .andExpect(ResponseMatchers.payload(responsePayload));
   }
}
