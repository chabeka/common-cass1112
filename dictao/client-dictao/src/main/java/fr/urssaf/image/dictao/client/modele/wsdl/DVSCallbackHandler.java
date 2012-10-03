
/**
 * DVSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package fr.urssaf.image.dictao.client.modele.wsdl;

    /**
     *  DVSCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class DVSCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public DVSCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public DVSCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for verifyAuthenticationEx method
            * override this method for handling normal response from verifyAuthenticationEx operation
            */
           public void receiveResultverifyAuthenticationEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifyAuthenticationExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from verifyAuthenticationEx operation
           */
            public void receiveErrorverifyAuthenticationEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for verifySignatureEx method
            * override this method for handling normal response from verifySignatureEx operation
            */
           public void receiveResultverifySignatureEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifySignatureExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from verifySignatureEx operation
           */
            public void receiveErrorverifySignatureEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getArchiveEx method
            * override this method for handling normal response from getArchiveEx operation
            */
           public void receiveResultgetArchiveEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.GetArchiveExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getArchiveEx operation
           */
            public void receiveErrorgetArchiveEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getAuthenticationChallengeEx method
            * override this method for handling normal response from getAuthenticationChallengeEx operation
            */
           public void receiveResultgetAuthenticationChallengeEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.GetAuthenticationChallengeExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getAuthenticationChallengeEx operation
           */
            public void receiveErrorgetAuthenticationChallengeEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for verifyCertificateEx method
            * override this method for handling normal response from verifyCertificateEx operation
            */
           public void receiveResultverifyCertificateEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifyCertificateExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from verifyCertificateEx operation
           */
            public void receiveErrorverifyCertificateEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for prepareAuthenticationRequestEx method
            * override this method for handling normal response from prepareAuthenticationRequestEx operation
            */
           public void receiveResultprepareAuthenticationRequestEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.PrepareAuthenticationRequestExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from prepareAuthenticationRequestEx operation
           */
            public void receiveErrorprepareAuthenticationRequestEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for decryptEx method
            * override this method for handling normal response from decryptEx operation
            */
           public void receiveResultdecryptEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DecryptExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from decryptEx operation
           */
            public void receiveErrordecryptEx(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for customizeTokenEx method
            * override this method for handling normal response from customizeTokenEx operation
            */
           public void receiveResultcustomizeTokenEx(
                    fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.CustomizeTokenExResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from customizeTokenEx operation
           */
            public void receiveErrorcustomizeTokenEx(java.lang.Exception e) {
            }
                


    }
    