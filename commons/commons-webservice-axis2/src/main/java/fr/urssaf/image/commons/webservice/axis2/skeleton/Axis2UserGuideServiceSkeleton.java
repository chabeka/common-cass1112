/**
 * Axis2UserGuideServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:22:40 CEST)
 */
package fr.urssaf.image.commons.webservice.axis2.skeleton;

import org.apache.axis2.axis2userguide.DoInOnlyRequest;
import org.apache.axis2.axis2userguide.MultipleParametersAddItemRequest;
import org.apache.axis2.axis2userguide.MultipleParametersAddItemResponse;
import org.apache.axis2.axis2userguide.NoParametersRequest;
import org.apache.axis2.axis2userguide.NoParametersResponse;
import org.apache.axis2.axis2userguide.TwoWayOneParameterEchoRequest;
import org.apache.axis2.axis2userguide.TwoWayOneParameterEchoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.webservice.axis2.service.UserGuideService;

/**
 * Axis2UserGuideServiceSkeleton java skeleton for the axisService
 */
@Component
public class Axis2UserGuideServiceSkeleton implements
      Axis2UserGuideServiceSkeletonInterface {

   private final UserGuideService service;

   @Autowired
   public Axis2UserGuideServiceSkeleton(UserGuideService service) {
      this.service = service;
   }

   /**
    * Auto generated method signature
    * 
    * @param doInOnlyRequest
    */
   @Override
   public void doInOnly(DoInOnlyRequest request) {

      service.doInOnly(request);

   }

   /**
    * Auto generated method signature
    * 
    * @param twoWayOneParameterEchoRequest
    */
   @Override
   public TwoWayOneParameterEchoResponse twoWayOneParameterEcho(
         TwoWayOneParameterEchoRequest request) {

      return service.twoWayOneParameterEcho(request);
   }

   /**
    * Auto generated method signature
    * 
    * @param noParametersRequest
    */
   @Override
   public NoParametersResponse noParameters(NoParametersRequest request) {

      return service.noParameters(request);
   }

   /**
    * Auto generated method signature
    * 
    * @param multipleParametersAddItemRequest
    */
   @Override
   public MultipleParametersAddItemResponse multipleParametersAddItem(
         MultipleParametersAddItemRequest request) {

      return service.multipleParametersAddItem(request);
   }
}
