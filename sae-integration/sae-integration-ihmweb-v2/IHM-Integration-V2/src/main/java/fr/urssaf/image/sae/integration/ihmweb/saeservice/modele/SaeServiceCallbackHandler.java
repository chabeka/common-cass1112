
/**
 * SaeServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package fr.urssaf.image.sae.integration.ihmweb.saeservice.modele;

    /**
     *  SaeServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public abstract class SaeServiceCallbackHandler{



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
            * auto generated Axis2 call back method for ajoutNote method
            * override this method for handling normal response from ajoutNote operation
            */
           public void receiveResultajoutNote(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.AjoutNoteResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ajoutNote operation
           */
            public void receiveErrorajoutNote(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageMasseAvecHash method
            * override this method for handling normal response from archivageMasseAvecHash operation
            */
           public void receiveResultarchivageMasseAvecHash(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseAvecHashResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archivageMasseAvecHash operation
           */
            public void receiveErrorarchivageMasseAvecHash(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageUnitairePJ method
            * override this method for handling normal response from archivageUnitairePJ operation
            */
           public void receiveResultarchivageUnitairePJ(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitairePJResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archivageUnitairePJ operation
           */
            public void receiveErrorarchivageUnitairePJ(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for rechercheParIterateur method
            * override this method for handling normal response from rechercheParIterateur operation
            */
           public void receiveResultrechercheParIterateur(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheParIterateurResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rechercheParIterateur operation
           */
            public void receiveErrorrechercheParIterateur(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deblocage method
            * override this method for handling normal response from deblocage operation
            */
           public void receiveResultdeblocage(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.DeblocageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deblocage operation
           */
            public void receiveErrordeblocage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for rechercheNbRes method
            * override this method for handling normal response from rechercheNbRes operation
            */
           public void receiveResultrechercheNbRes(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheNbResResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rechercheNbRes operation
           */
            public void receiveErrorrechercheNbRes(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for recherche method
            * override this method for handling normal response from recherche operation
            */
           public void receiveResultrecherche(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from recherche operation
           */
            public void receiveErrorrecherche(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDocFormatOrigine method
            * override this method for handling normal response from getDocFormatOrigine operation
            */
           public void receiveResultgetDocFormatOrigine(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.GetDocFormatOrigineResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDocFormatOrigine operation
           */
            public void receiveErrorgetDocFormatOrigine(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transfertMasse method
            * override this method for handling normal response from transfertMasse operation
            */
           public void receiveResulttransfertMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.TransfertMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transfertMasse operation
           */
            public void receiveErrortransfertMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for suppression method
            * override this method for handling normal response from suppression operation
            */
           public void receiveResultsuppression(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from suppression operation
           */
            public void receiveErrorsuppression(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for etatTraitementsMasse method
            * override this method for handling normal response from etatTraitementsMasse operation
            */
           public void receiveResultetatTraitementsMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.EtatTraitementsMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from etatTraitementsMasse operation
           */
            public void receiveErroretatTraitementsMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ping method
            * override this method for handling normal response from ping operation
            */
           public void receiveResultping(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ping operation
           */
            public void receiveErrorping(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for stockageUnitaire method
            * override this method for handling normal response from stockageUnitaire operation
            */
           public void receiveResultstockageUnitaire(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.StockageUnitaireResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from stockageUnitaire operation
           */
            public void receiveErrorstockageUnitaire(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for documentExistant method
            * override this method for handling normal response from documentExistant operation
            */
           public void receiveResultdocumentExistant(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.DocumentExistantResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from documentExistant operation
           */
            public void receiveErrordocumentExistant(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultationAffichable method
            * override this method for handling normal response from consultationAffichable operation
            */
           public void receiveResultconsultationAffichable(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationAffichableResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultationAffichable operation
           */
            public void receiveErrorconsultationAffichable(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageUnitaire method
            * override this method for handling normal response from archivageUnitaire operation
            */
           public void receiveResultarchivageUnitaire(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageUnitaireResponse result
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
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.PingSecureResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pingSecure operation
           */
            public void receiveErrorpingSecure(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultationGNTGNS method
            * override this method for handling normal response from consultationGNTGNS operation
            */
           public void receiveResultconsultationGNTGNS(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationGNTGNSResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultationGNTGNS operation
           */
            public void receiveErrorconsultationGNTGNS(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultation method
            * override this method for handling normal response from consultation operation
            */
           public void receiveResultconsultation(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultation operation
           */
            public void receiveErrorconsultation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modificationMasse method
            * override this method for handling normal response from modificationMasse operation
            */
           public void receiveResultmodificationMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ModificationMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modificationMasse operation
           */
            public void receiveErrormodificationMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for recuperationMetadonnees method
            * override this method for handling normal response from recuperationMetadonnees operation
            */
           public void receiveResultrecuperationMetadonnees(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RecuperationMetadonneesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from recuperationMetadonnees operation
           */
            public void receiveErrorrecuperationMetadonnees(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for consultationMTOM method
            * override this method for handling normal response from consultationMTOM operation
            */
           public void receiveResultconsultationMTOM(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ConsultationMTOMResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from consultationMTOM operation
           */
            public void receiveErrorconsultationMTOM(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for restoreMasse method
            * override this method for handling normal response from restoreMasse operation
            */
           public void receiveResultrestoreMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RestoreMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from restoreMasse operation
           */
            public void receiveErrorrestoreMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for archivageMasse method
            * override this method for handling normal response from archivageMasse operation
            */
           public void receiveResultarchivageMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ArchivageMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from archivageMasse operation
           */
            public void receiveErrorarchivageMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for modification method
            * override this method for handling normal response from modification operation
            */
           public void receiveResultmodification(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ModificationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from modification operation
           */
            public void receiveErrormodification(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for suppressionMasse method
            * override this method for handling normal response from suppressionMasse operation
            */
           public void receiveResultsuppressionMasse(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.SuppressionMasseResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from suppressionMasse operation
           */
            public void receiveErrorsuppressionMasse(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for copie method
            * override this method for handling normal response from copie operation
            */
           public void receiveResultcopie(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.CopieResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from copie operation
           */
            public void receiveErrorcopie(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transfert method
            * override this method for handling normal response from transfert operation
            */
           public void receiveResulttransfert(
                    fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.TransfertResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transfert operation
           */
            public void receiveErrortransfert(java.lang.Exception e) {
            }
                


    }
    