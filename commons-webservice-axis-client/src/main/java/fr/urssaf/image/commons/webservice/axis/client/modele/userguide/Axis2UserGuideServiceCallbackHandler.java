
/**
 * Axis2UserGuideServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package fr.urssaf.image.commons.webservice.axis.client.modele.userguide;

    /**
     *  Axis2UserGuideServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class Axis2UserGuideServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public Axis2UserGuideServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public Axis2UserGuideServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for twoWayOneParameterEcho method
            * override this method for handling normal response from twoWayOneParameterEcho operation
            */
           public void receiveResulttwoWayOneParameterEcho(
                    fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.TwoWayOneParameterEchoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from twoWayOneParameterEcho operation
           */
            public void receiveErrortwoWayOneParameterEcho(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for noParameters method
            * override this method for handling normal response from noParameters operation
            */
           public void receiveResultnoParameters(
                    fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.NoParametersResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from noParameters operation
           */
            public void receiveErrornoParameters(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for multipleParametersAddItem method
            * override this method for handling normal response from multipleParametersAddItem operation
            */
           public void receiveResultmultipleParametersAddItem(
                    fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.MultipleParametersAddItemResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from multipleParametersAddItem operation
           */
            public void receiveErrormultipleParametersAddItem(java.lang.Exception e) {
            }
                


    }
    