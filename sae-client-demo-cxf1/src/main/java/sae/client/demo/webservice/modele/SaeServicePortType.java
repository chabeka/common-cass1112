package sae.client.demo.webservice.modele;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.1
 * 2019-02-01T17:32:14.120+01:00
 * Generated source version: 2.6.1
 * 
 */
@WebService(targetNamespace = "http://www.cirtil.fr/saeService", name = "SaeServicePortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SaeServicePortType {

    /**
     * OpÃ©ration d'archivage d'un document
     *             unique
     */
    @WebMethod(action = "archivageUnitairePJ")
    @WebResult(name = "archivageUnitairePJResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ArchivageUnitairePJResponseType archivageUnitairePJ(
        @WebParam(partName = "input", name = "archivageUnitairePJ", targetNamespace = "http://www.cirtil.fr/saeService")
        ArchivageUnitairePJRequestType input
    );

    /**
     * OpÃ©ration de rÃ©cuperation de la liste
     *             des
     *             mÃ©tadonnÃ©es disponible pour le client
     */
    @WebMethod(action = "recuperationMetadonnees")
    @WebResult(name = "recuperationMetadonneesResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RecuperationMetadonneesResponseType recuperationMetadonnees(
        @WebParam(partName = "input", name = "recuperationMetadonnees", targetNamespace = "http://www.cirtil.fr/saeService")
        RecuperationMetadonnees input
    );

    /**
     * OpÃ©ration de modification de masse de 
     *          documents
     */
    @WebMethod(action = "modificationMasse")
    @WebResult(name = "modificationMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ModificationMasseResponseType modificationMasse(
        @WebParam(partName = "input", name = "modificationMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        ModificationMasseRequestType input
    );

    /**
     * OpÃ©ration d'archivage d'un document
     *             unique
     */
    @WebMethod(action = "archivageUnitaire")
    @WebResult(name = "archivageUnitaireResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ArchivageUnitaireResponseType archivageUnitaire(
        @WebParam(partName = "input", name = "archivageUnitaire", targetNamespace = "http://www.cirtil.fr/saeService")
        ArchivageUnitaireRequestType input
    );

    /**
     * OpÃ©ration de rÃ©cupÃ©ration d'un document au
     *             format d'origine
     */
    @WebMethod(action = "getDocFormatOrigine")
    @WebResult(name = "getDocFormatOrigineResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public GetDocFormatOrigineResponseType getDocFormatOrigine(
        @WebParam(partName = "input", name = "getDocFormatOrigine", targetNamespace = "http://www.cirtil.fr/saeService")
        GetDocFormatOrigineRequestType input
    );

    /**
     * OpÃ©ration de rÃ©cupÃ©ration des Ã©tats des
     *             traitements de masse
     */
    @WebMethod(action = "etatTraitementsMasse")
    @WebResult(name = "etatTraitementsMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public EtatTraitementsMasseResponseType etatTraitementsMasse(
        @WebParam(partName = "input", name = "etatTraitementsMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        EtatTraitementsMasseRequestType input
    );

    /**
     * OpÃ©ration d'archivage de documents
     *             multiples
     */
    @WebMethod(action = "archivageMasse")
    @WebResult(name = "archivageMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ArchivageMasseResponseType archivageMasse(
        @WebParam(partName = "input", name = "archivageMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        ArchivageMasseRequestType input
    );

    /**
     * OpÃ©ration de transfert d'un document
     *             de
     *             la
     *             GNT vers la GNS
     */
    @WebMethod(action = "transfert")
    @WebResult(name = "transfertResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public TransfertResponseType transfert(
        @WebParam(partName = "input", name = "transfert", targetNamespace = "http://www.cirtil.fr/saeService")
        TransfertRequestType input
    );

    /**
     * OpÃ©ration de suppression d'un
     *             document
     *             unique
     */
    @WebMethod(action = "suppression")
    @WebResult(name = "suppressionResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public SuppressionResponseType suppression(
        @WebParam(partName = "input", name = "suppression", targetNamespace = "http://www.cirtil.fr/saeService")
        SuppressionRequestType input
    );

    /**
     * OpÃ©ration de reprise de job
     */
    @WebMethod(action = "reprise")
    @WebResult(name = "repriseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RepriseResponseType reprise(
        @WebParam(partName = "input", name = "reprise", targetNamespace = "http://www.cirtil.fr/saeService")
        RepriseRequestType input
    );

    /**
     * OpÃ©ration de restauration en masse de
     *             documents supprimÃ©s
     */
    @WebMethod(action = "restoreMasse")
    @WebResult(name = "restoreMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RestoreMasseResponseType restoreMasse(
        @WebParam(partName = "input", name = "restoreMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        RestoreMasseRequestType input
    );

    /**
     * OpÃ©ration de stockage unitaire d'un
     *             document
     */
    @WebMethod(action = "stockageUnitaire")
    @WebResult(name = "stockageUnitaireResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public StockageUnitaireResponseType stockageUnitaire(
        @WebParam(partName = "input", name = "stockageUnitaire", targetNamespace = "http://www.cirtil.fr/saeService")
        StockageUnitaireRequestType input
    );

    /**
     * OpÃ©ration de vÃ©rification d'un
     *             document
     */
    @WebMethod(action = "documentExistant")
    @WebResult(name = "documentExistantResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public DocumentExistantResponseType documentExistant(
        @WebParam(partName = "input", name = "documentExistant", targetNamespace = "http://www.cirtil.fr/saeService")
        DocumentExistantRequestType input
    );

    /**
     * OpÃ©ration de recherche documentaire
     *             sur
     *             le
     *             SAE (par iterateur)
     */
    @WebMethod(action = "rechercheParIterateur")
    @WebResult(name = "rechercheParIterateurResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RechercheParIterateurResponseType rechercheParIterateur(
        @WebParam(partName = "input", name = "rechercheParIterateur", targetNamespace = "http://www.cirtil.fr/saeService")
        RechercheParIterateurRequestType input
    );

    /**
     * OpÃ©ration de copie d'un
     *             document
     */
    @WebMethod(action = "copie")
    @WebResult(name = "copieResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public CopieResponseType copie(
        @WebParam(partName = "input", name = "copie", targetNamespace = "http://www.cirtil.fr/saeService")
        CopieRequestType input
    );

    /**
     * OpÃ©ration de dÃ©blocage de job
     */
    @WebMethod(action = "deblocage")
    @WebResult(name = "deblocageResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public DeblocageResponseType deblocage(
        @WebParam(partName = "input", name = "deblocage", targetNamespace = "http://www.cirtil.fr/saeService")
        DeblocageRequestType input
    );

    /**
     * OpÃ©ration de consultationAffichable
     *             d'un
     *             document
     *             sur le SAE
     */
    @WebMethod(action = "consultationAffichable")
    @WebResult(name = "consultationAffichableResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ConsultationAffichableResponseType consultationAffichable(
        @WebParam(partName = "input", name = "consultationAffichable", targetNamespace = "http://www.cirtil.fr/saeService")
        ConsultationAffichableRequestType input
    );

    /**
     * OpÃ©ration de suppression de masse de
     *             documents
     */
    @WebMethod(action = "suppressionMasse")
    @WebResult(name = "suppressionMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public SuppressionMasseResponseType suppressionMasse(
        @WebParam(partName = "input", name = "suppressionMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        SuppressionMasseRequestType input
    );

    /**
     * OpÃ©ration de consultation GNT vers GNS d'un
     *             document
     */
    @WebMethod(action = "consultationGNTGNS")
    @WebResult(name = "consultationGNTGNSResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ConsultationGNTGNSResponseType consultationGNTGNS(
        @WebParam(partName = "input", name = "consultationGNTGNS", targetNamespace = "http://www.cirtil.fr/saeService")
        ConsultationGNTGNSRequestType input
    );

    /**
     * OpÃ©ration de test de la disponibilitÃ©
     *             du
     *             serveur
     */
    @WebMethod(operationName = "Ping", action = "Ping")
    @WebResult(name = "PingResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public PingResponse ping(
        @WebParam(partName = "input", name = "PingRequest", targetNamespace = "http://www.cirtil.fr/saeService")
        PingRequest input
    );

    /**
     * OpÃ©ration d'ajout d'une note Ã  un
     *             document
     */
    @WebMethod(action = "ajoutNote")
    @WebResult(name = "ajoutNoteResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public AjoutNoteResponseType ajoutNote(
        @WebParam(partName = "input", name = "ajoutNote", targetNamespace = "http://www.cirtil.fr/saeService")
        AjoutNoteRequestType input
    );

    /**
     * OpÃ©ration de consultation
     *             documentaire
     *             sur
     *             le
     *             SAE
     */
    @WebMethod(action = "consultation")
    @WebResult(name = "consultationResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ConsultationResponseType consultation(
        @WebParam(partName = "input", name = "consultation", targetNamespace = "http://www.cirtil.fr/saeService")
        ConsultationRequestType input
    );

    /**
     * OpÃ©ration de transfert de documents
     *             multiples avec un hash pour le sommaire.xml
     */
    @WebMethod(action = "transfertMasse")
    @WebResult(name = "transfertMasseResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public TransfertMasseResponseType transfertMasse(
        @WebParam(partName = "input", name = "transfertMasse", targetNamespace = "http://www.cirtil.fr/saeService")
        TransfertMasseRequestType input
    );

    /**
     * OpÃ©ration de test de la disponibilitÃ©
     *             du
     *             serveur en mode sÃ©curisÃ©
     */
    @WebMethod(operationName = "PingSecure", action = "PingSecure")
    @WebResult(name = "PingSecureResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public PingSecureResponse pingSecure(
        @WebParam(partName = "input", name = "PingSecureRequest", targetNamespace = "http://www.cirtil.fr/saeService")
        PingSecureRequest input
    );

    /**
     * OpÃ©ration de recherche documentaire
     *             sur
     *             le
     *             SAE
     *             (avec retour du nombre de rÃ©sultats)
     */
    @WebMethod(action = "rechercheNbRes")
    @WebResult(name = "rechercheNbResResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RechercheNbResResponseType rechercheNbRes(
        @WebParam(partName = "input", name = "rechercheNbRes", targetNamespace = "http://www.cirtil.fr/saeService")
        RechercheNbResRequestType input
    );

    /**
     * OpÃ©ration d'archivage de documents
     *             multiples avec un hash pour le sommaire.xml
     */
    @WebMethod(action = "archivageMasseAvecHash")
    @WebResult(name = "archivageMasseAvecHashResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ArchivageMasseAvecHashResponseType archivageMasseAvecHash(
        @WebParam(partName = "input", name = "archivageMasseAvecHash", targetNamespace = "http://www.cirtil.fr/saeService")
        ArchivageMasseAvecHashRequestType input
    );

    /**
     * OpÃ©ration de consultationMTOM
     *             documentaire
     *             sur le SAE
     */
    @WebMethod(action = "consultationMTOM")
    @WebResult(name = "consultationMTOMResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ConsultationMTOMResponseType consultationMTOM(
        @WebParam(partName = "input", name = "consultationMTOM", targetNamespace = "http://www.cirtil.fr/saeService")
        ConsultationMTOMRequestType input
    );

    /**
     * OpÃ©ration de modification d'un
     *             document
     *             unique
     */
    @WebMethod(action = "modification")
    @WebResult(name = "modificationResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public ModificationResponseType modification(
        @WebParam(partName = "input", name = "modification", targetNamespace = "http://www.cirtil.fr/saeService")
        ModificationRequestType input
    );

    /**
     * OpÃ©ration de recherche documentaire
     *             sur le
     *             SAE
     */
    @WebMethod(action = "recherche")
    @WebResult(name = "rechercheResponse", targetNamespace = "http://www.cirtil.fr/saeService", partName = "output")
    public RechercheResponseType recherche(
        @WebParam(partName = "input", name = "recherche", targetNamespace = "http://www.cirtil.fr/saeService")
        RechercheRequestType input
    );
}
