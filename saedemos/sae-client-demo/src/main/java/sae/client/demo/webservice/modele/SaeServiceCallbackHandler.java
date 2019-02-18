/**
 * SaeServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:05:37 GMT)
 */
package sae.client.demo.webservice.modele;


/**
 *  SaeServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SaeServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public SaeServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SaeServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for archivageUnitairePJ method
     * override this method for handling normal response from archivageUnitairePJ operation
     */
    public void receiveResultarchivageUnitairePJ(
        sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from archivageUnitairePJ operation
     */
    public void receiveErrorarchivageUnitairePJ(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for recuperationMetadonnees method
     * override this method for handling normal response from recuperationMetadonnees operation
     */
    public void receiveResultrecuperationMetadonnees(
        sae.client.demo.webservice.modele.SaeServiceStub.RecuperationMetadonneesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from recuperationMetadonnees operation
     */
    public void receiveErrorrecuperationMetadonnees(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for modificationMasse method
     * override this method for handling normal response from modificationMasse operation
     */
    public void receiveResultmodificationMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from modificationMasse operation
     */
    public void receiveErrormodificationMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getDocFormatOrigine method
     * override this method for handling normal response from getDocFormatOrigine operation
     */
    public void receiveResultgetDocFormatOrigine(
        sae.client.demo.webservice.modele.SaeServiceStub.GetDocFormatOrigineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getDocFormatOrigine operation
     */
    public void receiveErrorgetDocFormatOrigine(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for etatTraitementsMasse method
     * override this method for handling normal response from etatTraitementsMasse operation
     */
    public void receiveResultetatTraitementsMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.EtatTraitementsMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from etatTraitementsMasse operation
     */
    public void receiveErroretatTraitementsMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for transfert method
     * override this method for handling normal response from transfert operation
     */
    public void receiveResulttransfert(
        sae.client.demo.webservice.modele.SaeServiceStub.TransfertResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from transfert operation
     */
    public void receiveErrortransfert(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for rechercheParIterateurV2 method
     * override this method for handling normal response from rechercheParIterateurV2 operation
     */
    public void receiveResultrechercheParIterateurV2(
        sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateurResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from rechercheParIterateurV2 operation
     */
    public void receiveErrorrechercheParIterateurV2(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for suppression method
     * override this method for handling normal response from suppression operation
     */
    public void receiveResultsuppression(
        sae.client.demo.webservice.modele.SaeServiceStub.SuppressionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from suppression operation
     */
    public void receiveErrorsuppression(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for reprise method
     * override this method for handling normal response from reprise operation
     */
    public void receiveResultreprise(
        sae.client.demo.webservice.modele.SaeServiceStub.RepriseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from reprise operation
     */
    public void receiveErrorreprise(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for restoreMasse method
     * override this method for handling normal response from restoreMasse operation
     */
    public void receiveResultrestoreMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.RestoreMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from restoreMasse operation
     */
    public void receiveErrorrestoreMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for stockageUnitaire method
     * override this method for handling normal response from stockageUnitaire operation
     */
    public void receiveResultstockageUnitaire(
        sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from stockageUnitaire operation
     */
    public void receiveErrorstockageUnitaire(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for rechercheParIterateur method
     * override this method for handling normal response from rechercheParIterateur operation
     */
    public void receiveResultrechercheParIterateur(
        sae.client.demo.webservice.modele.SaeServiceStub.RechercheParIterateurResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from rechercheParIterateur operation
     */
    public void receiveErrorrechercheParIterateur(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for copie method
     * override this method for handling normal response from copie operation
     */
    public void receiveResultcopie(
        sae.client.demo.webservice.modele.SaeServiceStub.CopieResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from copie operation
     */
    public void receiveErrorcopie(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for suppressionArchivageMasse method
     * override this method for handling normal response from suppressionArchivageMasse operation
     */
    public void receiveResultsuppressionArchivageMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.SuppressionArchivageMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from suppressionArchivageMasse operation
     */
    public void receiveErrorsuppressionArchivageMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for deblocage method
     * override this method for handling normal response from deblocage operation
     */
    public void receiveResultdeblocage(
        sae.client.demo.webservice.modele.SaeServiceStub.DeblocageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from deblocage operation
     */
    public void receiveErrordeblocage(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for suppressionMasseV2 method
     * override this method for handling normal response from suppressionMasseV2 operation
     */
    public void receiveResultsuppressionMasseV2(
        sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from suppressionMasseV2 operation
     */
    public void receiveErrorsuppressionMasseV2(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for consultationAffichable method
     * override this method for handling normal response from consultationAffichable operation
     */
    public void receiveResultconsultationAffichable(
        sae.client.demo.webservice.modele.SaeServiceStub.ConsultationAffichableResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from consultationAffichable operation
     */
    public void receiveErrorconsultationAffichable(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for suppressionMasse method
     * override this method for handling normal response from suppressionMasse operation
     */
    public void receiveResultsuppressionMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.SuppressionMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from suppressionMasse operation
     */
    public void receiveErrorsuppressionMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ping method
     * override this method for handling normal response from ping operation
     */
    public void receiveResultping(
        sae.client.demo.webservice.modele.SaeServiceStub.PingResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ping operation
     */
    public void receiveErrorping(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ajoutNote method
     * override this method for handling normal response from ajoutNote operation
     */
    public void receiveResultajoutNote(
        sae.client.demo.webservice.modele.SaeServiceStub.AjoutNoteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ajoutNote operation
     */
    public void receiveErrorajoutNote(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for consultation method
     * override this method for handling normal response from consultation operation
     */
    public void receiveResultconsultation(
        sae.client.demo.webservice.modele.SaeServiceStub.ConsultationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from consultation operation
     */
    public void receiveErrorconsultation(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for transfertMasse method
     * override this method for handling normal response from transfertMasse operation
     */
    public void receiveResulttransfertMasse(
        sae.client.demo.webservice.modele.SaeServiceStub.TransfertMasseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from transfertMasse operation
     */
    public void receiveErrortransfertMasse(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for pingSecure method
     * override this method for handling normal response from pingSecure operation
     */
    public void receiveResultpingSecure(
        sae.client.demo.webservice.modele.SaeServiceStub.PingSecureResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from pingSecure operation
     */
    public void receiveErrorpingSecure(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for rechercheNbRes method
     * override this method for handling normal response from rechercheNbRes operation
     */
    public void receiveResultrechercheNbRes(
        sae.client.demo.webservice.modele.SaeServiceStub.RechercheNbResResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from rechercheNbRes operation
     */
    public void receiveErrorrechercheNbRes(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for archivageMasseAvecHash method
     * override this method for handling normal response from archivageMasseAvecHash operation
     */
    public void receiveResultarchivageMasseAvecHash(
        sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseAvecHashResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from archivageMasseAvecHash operation
     */
    public void receiveErrorarchivageMasseAvecHash(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for consultationMTOM method
     * override this method for handling normal response from consultationMTOM operation
     */
    public void receiveResultconsultationMTOM(
        sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOMResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from consultationMTOM operation
     */
    public void receiveErrorconsultationMTOM(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for modification method
     * override this method for handling normal response from modification operation
     */
    public void receiveResultmodification(
        sae.client.demo.webservice.modele.SaeServiceStub.ModificationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from modification operation
     */
    public void receiveErrormodification(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for recherche method
     * override this method for handling normal response from recherche operation
     */
    public void receiveResultrecherche(
        sae.client.demo.webservice.modele.SaeServiceStub.RechercheResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from recherche operation
     */
    public void receiveErrorrecherche(java.lang.Exception e) {
    }
}
