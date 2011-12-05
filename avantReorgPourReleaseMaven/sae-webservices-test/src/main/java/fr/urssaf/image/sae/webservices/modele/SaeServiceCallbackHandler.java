
/**
 * SaeServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

    package fr.urssaf.image.sae.webservices.modele;

    /**
     *  SaeServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SaeServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SaeServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SaeServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for recherche method
            * override this method for handling normal response from recherche operation
            */
           public void receiveResultrecherche(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from recherche operation
           */
            public void receiveErrorrecherche(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ping method
            * override this method for handling normal response from ping operation
            */
           public void receiveResultping(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ping operation
           */
            public void receiveErrorping(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageUnitaire method
            * override this method for handling normal response from archivageUnitaire operation
            */
           public void receiveResultarchivageUnitaire(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archivageUnitaire operation
           */
            public void receiveErrorarchivageUnitaire(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pingSecure method
            * override this method for handling normal response from pingSecure operation
            */
           public void receiveResultpingSecure(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pingSecure operation
           */
            public void receiveErrorpingSecure(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultation method
            * override this method for handling normal response from consultation operation
            */
           public void receiveResultconsultation(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultation operation
           */
            public void receiveErrorconsultation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageMasse method
            * override this method for handling normal response from archivageMasse operation
            */
           public void receiveResultarchivageMasse(
                    fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archivageMasse operation
           */
            public void receiveErrorarchivageMasse(java.lang.Exception e) {
            }
                


    }
    