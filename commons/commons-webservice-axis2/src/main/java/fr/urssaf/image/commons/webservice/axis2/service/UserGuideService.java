package fr.urssaf.image.commons.webservice.axis2.service;

import org.apache.axis2.axis2userguide.DoInOnlyRequest;
import org.apache.axis2.axis2userguide.MultipleParametersAddItemRequest;
import org.apache.axis2.axis2userguide.MultipleParametersAddItemResponse;
import org.apache.axis2.axis2userguide.NoParametersRequest;
import org.apache.axis2.axis2userguide.NoParametersResponse;
import org.apache.axis2.axis2userguide.TwoWayOneParameterEchoRequest;
import org.apache.axis2.axis2userguide.TwoWayOneParameterEchoResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UserGuideService {

   private static final Logger LOG = Logger.getLogger(UserGuideService.class);

   public void doInOnly(DoInOnlyRequest request) {

      LOG.debug(request.getMessageString());

   }

   public TwoWayOneParameterEchoResponse twoWayOneParameterEcho(
         TwoWayOneParameterEchoRequest request) {

      LOG.debug(request.getEchoString());

      TwoWayOneParameterEchoResponse response = new TwoWayOneParameterEchoResponse();

      response.setEchoString(request.getEchoString());

      return response;
   }

   public NoParametersResponse noParameters(
         NoParametersRequest request) {

      LOG.debug(request);

      NoParametersResponse response = new NoParametersResponse();

      return response;
   }

   public MultipleParametersAddItemResponse multipleParametersAddItem(
         MultipleParametersAddItemRequest request) {

      LOG.debug(request.getDescription());
      LOG.debug(request.getItemId());
      LOG.debug(request.getItemName());
      LOG.debug(request.getPrice());

      MultipleParametersAddItemResponse response = new MultipleParametersAddItemResponse();

      response.setItemId(request.getItemId());
      response.setSuccessfulAdd(true);

      return response;
   }

}
