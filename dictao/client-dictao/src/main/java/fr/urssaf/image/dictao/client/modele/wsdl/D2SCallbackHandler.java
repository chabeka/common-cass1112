
/**
 * D2SCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package fr.urssaf.image.dictao.client.modele.wsdl;

    /**
     *  D2SCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class D2SCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public D2SCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public D2SCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getArchiveEx method
            * override this method for handling normal response from getArchiveEx operation
            */
           public void receiveResultgetArchiveEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.GetArchiveExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getArchiveEx operation
           */
            public void receiveErrorgetArchiveEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for prepareSignatureEx method
            * override this method for handling normal response from prepareSignatureEx operation
            */
           public void receiveResultprepareSignatureEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.PrepareSignatureExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from prepareSignatureEx operation
           */
            public void receiveErrorprepareSignatureEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for signatureEx method
            * override this method for handling normal response from signatureEx operation
            */
           public void receiveResultsignatureEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.SignatureExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from signatureEx operation
           */
            public void receiveErrorsignatureEx(java.lang.Exception e) {
            }
                


    }
    