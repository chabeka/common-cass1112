package fr.urssaf.image.commons.webservice.spring.ws;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import fr.urssaf.image.commons.webservice.spring.ws.schema.HolidayRequest;
import fr.urssaf.image.commons.webservice.spring.ws.schema.HolidayResponse;
import fr.urssaf.image.commons.webservice.spring.ws.schema.ObjectFactory;
import fr.urssaf.image.commons.webservice.spring.ws.service.HumanResourceService;

@Endpoint
public class HolidayEndpoint {

   private static final String NAMESPACE_URI = "http://mycompany.com/hr/schemas";

   private final HumanResourceService hrService;

   private final ObjectFactory factory;

   @Autowired
   public HolidayEndpoint(HumanResourceService hrService) {
      this.hrService = hrService;
      factory = new ObjectFactory();
   }

   @PayloadRoot(namespace = NAMESPACE_URI, localPart = "HolidayRequest")
   @ResponsePayload
   public HolidayResponse handleHolidayRequest(
         @RequestPayload HolidayRequest holiday) {

      Date startDate = holiday.getHoliday().getStartDate();
      Date endDate = holiday.getHoliday().getEndDate();
      String name = holiday.getEmployee().getFirstName() + " "
            + holiday.getEmployee().getLastName();
      hrService.bookHoliday(startDate, endDate, name);

      HolidayResponse response = factory.createHolidayResponse();
      response.setEcho("demande enregistrée");

      return response;
   }
}
