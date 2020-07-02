
package fr.urssaf.image.parser_opencsv.webservice.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.parser_opencsv.webservice.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ArchivageUnitaire_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageUnitaire");
    private final static QName _ArchivageUnitaireResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageUnitaireResponse");
    private final static QName _ArchivageUnitairePJ_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageUnitairePJ");
    private final static QName _ArchivageUnitairePJResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageUnitairePJResponse");
    private final static QName _StockageUnitaire_QNAME = new QName("http://www.cirtil.fr/saeService", "stockageUnitaire");
    private final static QName _StockageUnitaireResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "stockageUnitaireResponse");
    private final static QName _GetDocFormatOrigine_QNAME = new QName("http://www.cirtil.fr/saeService", "getDocFormatOrigine");
    private final static QName _GetDocFormatOrigineResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "getDocFormatOrigineResponse");
    private final static QName _SuppressionMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "suppressionMasse");
    private final static QName _RestoreMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "restoreMasse");
    private final static QName _EtatTraitementsMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "etatTraitementsMasse");
    private final static QName _ArchivageMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageMasse");
    private final static QName _ArchivageMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageMasseResponse");
    private final static QName _ArchivageMasseAvecHash_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageMasseAvecHash");
    private final static QName _ArchivageMasseAvecHashResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "archivageMasseAvecHashResponse");
    private final static QName _Recherche_QNAME = new QName("http://www.cirtil.fr/saeService", "recherche");
    private final static QName _RechercheResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "rechercheResponse");
    private final static QName _Consultation_QNAME = new QName("http://www.cirtil.fr/saeService", "consultation");
    private final static QName _ConsultationResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationResponse");
    private final static QName _ConsultationMTOM_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationMTOM");
    private final static QName _ConsultationMTOMResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationMTOMResponse");
    private final static QName _Modification_QNAME = new QName("http://www.cirtil.fr/saeService", "modification");
    private final static QName _ModificationResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "modificationResponse");
    private final static QName _Suppression_QNAME = new QName("http://www.cirtil.fr/saeService", "suppression");
    private final static QName _SuppressionResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "suppressionResponse");
    private final static QName _RecuperationMetadonneesResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "recuperationMetadonneesResponse");
    private final static QName _Transfert_QNAME = new QName("http://www.cirtil.fr/saeService", "transfert");
    private final static QName _TransfertResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "transfertResponse");
    private final static QName _ConsultationAffichable_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationAffichable");
    private final static QName _ConsultationAffichableResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationAffichableResponse");
    private final static QName _RechercheNbRes_QNAME = new QName("http://www.cirtil.fr/saeService", "rechercheNbRes");
    private final static QName _RechercheNbResResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "rechercheNbResResponse");
    private final static QName _RechercheParIterateur_QNAME = new QName("http://www.cirtil.fr/saeService", "rechercheParIterateur");
    private final static QName _RechercheParIterateurResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "rechercheParIterateurResponse");
    private final static QName _AjoutNote_QNAME = new QName("http://www.cirtil.fr/saeService", "ajoutNote");
    private final static QName _AjoutNoteResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "ajoutNoteResponse");
    private final static QName _SuppressionMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "suppressionMasseResponse");
    private final static QName _RestoreMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "restoreMasseResponse");
    private final static QName _EtatTraitementsMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "etatTraitementsMasseResponse");
    private final static QName _Copie_QNAME = new QName("http://www.cirtil.fr/saeService", "copie");
    private final static QName _CopieResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "copieResponse");
    private final static QName _DocumentExistant_QNAME = new QName("http://www.cirtil.fr/saeService", "documentExistant");
    private final static QName _DocumentExistantResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "documentExistantResponse");
    private final static QName _ConsultationGNTGNS_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationGNTGNS");
    private final static QName _ConsultationGNTGNSResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "consultationGNTGNSResponse");
    private final static QName _ModificationMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "modificationMasse");
    private final static QName _ModificationMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "modificationMasseResponse");
    private final static QName _TransfertMasse_QNAME = new QName("http://www.cirtil.fr/saeService", "transfertMasse");
    private final static QName _TransfertMasseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "transfertMasseResponse");
    private final static QName _Deblocage_QNAME = new QName("http://www.cirtil.fr/saeService", "deblocage");
    private final static QName _DeblocageResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "deblocageResponse");
    private final static QName _Reprise_QNAME = new QName("http://www.cirtil.fr/saeService", "reprise");
    private final static QName _RepriseResponse_QNAME = new QName("http://www.cirtil.fr/saeService", "repriseResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.parser_opencsv.webservice.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PingRequest }
     * 
     */
    public PingRequest createPingRequest() {
        return new PingRequest();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link PingSecureRequest }
     * 
     */
    public PingSecureRequest createPingSecureRequest() {
        return new PingSecureRequest();
    }

    /**
     * Create an instance of {@link PingSecureResponse }
     * 
     */
    public PingSecureResponse createPingSecureResponse() {
        return new PingSecureResponse();
    }

    /**
     * Create an instance of {@link RecuperationMetadonnees }
     * 
     */
    public RecuperationMetadonnees createRecuperationMetadonnees() {
        return new RecuperationMetadonnees();
    }

    /**
     * Create an instance of {@link ArchivageUnitaireRequestType }
     * 
     */
    public ArchivageUnitaireRequestType createArchivageUnitaireRequestType() {
        return new ArchivageUnitaireRequestType();
    }

    /**
     * Create an instance of {@link ArchivageUnitaireResponseType }
     * 
     */
    public ArchivageUnitaireResponseType createArchivageUnitaireResponseType() {
        return new ArchivageUnitaireResponseType();
    }

    /**
     * Create an instance of {@link ArchivageUnitairePJRequestType }
     * 
     */
    public ArchivageUnitairePJRequestType createArchivageUnitairePJRequestType() {
        return new ArchivageUnitairePJRequestType();
    }

    /**
     * Create an instance of {@link ArchivageUnitairePJResponseType }
     * 
     */
    public ArchivageUnitairePJResponseType createArchivageUnitairePJResponseType() {
        return new ArchivageUnitairePJResponseType();
    }

    /**
     * Create an instance of {@link StockageUnitaireRequestType }
     * 
     */
    public StockageUnitaireRequestType createStockageUnitaireRequestType() {
        return new StockageUnitaireRequestType();
    }

    /**
     * Create an instance of {@link StockageUnitaireResponseType }
     * 
     */
    public StockageUnitaireResponseType createStockageUnitaireResponseType() {
        return new StockageUnitaireResponseType();
    }

    /**
     * Create an instance of {@link GetDocFormatOrigineRequestType }
     * 
     */
    public GetDocFormatOrigineRequestType createGetDocFormatOrigineRequestType() {
        return new GetDocFormatOrigineRequestType();
    }

    /**
     * Create an instance of {@link GetDocFormatOrigineResponseType }
     * 
     */
    public GetDocFormatOrigineResponseType createGetDocFormatOrigineResponseType() {
        return new GetDocFormatOrigineResponseType();
    }

    /**
     * Create an instance of {@link SuppressionMasseRequestType }
     * 
     */
    public SuppressionMasseRequestType createSuppressionMasseRequestType() {
        return new SuppressionMasseRequestType();
    }

    /**
     * Create an instance of {@link RestoreMasseRequestType }
     * 
     */
    public RestoreMasseRequestType createRestoreMasseRequestType() {
        return new RestoreMasseRequestType();
    }

    /**
     * Create an instance of {@link EtatTraitementsMasseRequestType }
     * 
     */
    public EtatTraitementsMasseRequestType createEtatTraitementsMasseRequestType() {
        return new EtatTraitementsMasseRequestType();
    }

    /**
     * Create an instance of {@link ArchivageMasseRequestType }
     * 
     */
    public ArchivageMasseRequestType createArchivageMasseRequestType() {
        return new ArchivageMasseRequestType();
    }

    /**
     * Create an instance of {@link ArchivageMasseResponseType }
     * 
     */
    public ArchivageMasseResponseType createArchivageMasseResponseType() {
        return new ArchivageMasseResponseType();
    }

    /**
     * Create an instance of {@link ArchivageMasseAvecHashRequestType }
     * 
     */
    public ArchivageMasseAvecHashRequestType createArchivageMasseAvecHashRequestType() {
        return new ArchivageMasseAvecHashRequestType();
    }

    /**
     * Create an instance of {@link ArchivageMasseAvecHashResponseType }
     * 
     */
    public ArchivageMasseAvecHashResponseType createArchivageMasseAvecHashResponseType() {
        return new ArchivageMasseAvecHashResponseType();
    }

    /**
     * Create an instance of {@link RechercheRequestType }
     * 
     */
    public RechercheRequestType createRechercheRequestType() {
        return new RechercheRequestType();
    }

    /**
     * Create an instance of {@link RechercheResponseType }
     * 
     */
    public RechercheResponseType createRechercheResponseType() {
        return new RechercheResponseType();
    }

    /**
     * Create an instance of {@link ConsultationRequestType }
     * 
     */
    public ConsultationRequestType createConsultationRequestType() {
        return new ConsultationRequestType();
    }

    /**
     * Create an instance of {@link ConsultationResponseType }
     * 
     */
    public ConsultationResponseType createConsultationResponseType() {
        return new ConsultationResponseType();
    }

    /**
     * Create an instance of {@link ConsultationMTOMRequestType }
     * 
     */
    public ConsultationMTOMRequestType createConsultationMTOMRequestType() {
        return new ConsultationMTOMRequestType();
    }

    /**
     * Create an instance of {@link ConsultationMTOMResponseType }
     * 
     */
    public ConsultationMTOMResponseType createConsultationMTOMResponseType() {
        return new ConsultationMTOMResponseType();
    }

    /**
     * Create an instance of {@link ModificationRequestType }
     * 
     */
    public ModificationRequestType createModificationRequestType() {
        return new ModificationRequestType();
    }

    /**
     * Create an instance of {@link ModificationResponseType }
     * 
     */
    public ModificationResponseType createModificationResponseType() {
        return new ModificationResponseType();
    }

    /**
     * Create an instance of {@link SuppressionRequestType }
     * 
     */
    public SuppressionRequestType createSuppressionRequestType() {
        return new SuppressionRequestType();
    }

    /**
     * Create an instance of {@link SuppressionResponseType }
     * 
     */
    public SuppressionResponseType createSuppressionResponseType() {
        return new SuppressionResponseType();
    }

    /**
     * Create an instance of {@link RecuperationMetadonneesResponseType }
     * 
     */
    public RecuperationMetadonneesResponseType createRecuperationMetadonneesResponseType() {
        return new RecuperationMetadonneesResponseType();
    }

    /**
     * Create an instance of {@link TransfertRequestType }
     * 
     */
    public TransfertRequestType createTransfertRequestType() {
        return new TransfertRequestType();
    }

    /**
     * Create an instance of {@link TransfertResponseType }
     * 
     */
    public TransfertResponseType createTransfertResponseType() {
        return new TransfertResponseType();
    }

    /**
     * Create an instance of {@link ConsultationAffichableRequestType }
     * 
     */
    public ConsultationAffichableRequestType createConsultationAffichableRequestType() {
        return new ConsultationAffichableRequestType();
    }

    /**
     * Create an instance of {@link ConsultationAffichableResponseType }
     * 
     */
    public ConsultationAffichableResponseType createConsultationAffichableResponseType() {
        return new ConsultationAffichableResponseType();
    }

    /**
     * Create an instance of {@link RechercheNbResRequestType }
     * 
     */
    public RechercheNbResRequestType createRechercheNbResRequestType() {
        return new RechercheNbResRequestType();
    }

    /**
     * Create an instance of {@link RechercheNbResResponseType }
     * 
     */
    public RechercheNbResResponseType createRechercheNbResResponseType() {
        return new RechercheNbResResponseType();
    }

    /**
     * Create an instance of {@link RechercheParIterateurRequestType }
     * 
     */
    public RechercheParIterateurRequestType createRechercheParIterateurRequestType() {
        return new RechercheParIterateurRequestType();
    }

    /**
     * Create an instance of {@link RechercheParIterateurResponseType }
     * 
     */
    public RechercheParIterateurResponseType createRechercheParIterateurResponseType() {
        return new RechercheParIterateurResponseType();
    }

    /**
     * Create an instance of {@link AjoutNoteRequestType }
     * 
     */
    public AjoutNoteRequestType createAjoutNoteRequestType() {
        return new AjoutNoteRequestType();
    }

    /**
     * Create an instance of {@link AjoutNoteResponseType }
     * 
     */
    public AjoutNoteResponseType createAjoutNoteResponseType() {
        return new AjoutNoteResponseType();
    }

    /**
     * Create an instance of {@link SuppressionMasseResponseType }
     * 
     */
    public SuppressionMasseResponseType createSuppressionMasseResponseType() {
        return new SuppressionMasseResponseType();
    }

    /**
     * Create an instance of {@link RestoreMasseResponseType }
     * 
     */
    public RestoreMasseResponseType createRestoreMasseResponseType() {
        return new RestoreMasseResponseType();
    }

    /**
     * Create an instance of {@link EtatTraitementsMasseResponseType }
     * 
     */
    public EtatTraitementsMasseResponseType createEtatTraitementsMasseResponseType() {
        return new EtatTraitementsMasseResponseType();
    }

    /**
     * Create an instance of {@link CopieRequestType }
     * 
     */
    public CopieRequestType createCopieRequestType() {
        return new CopieRequestType();
    }

    /**
     * Create an instance of {@link CopieResponseType }
     * 
     */
    public CopieResponseType createCopieResponseType() {
        return new CopieResponseType();
    }

    /**
     * Create an instance of {@link DocumentExistantRequestType }
     * 
     */
    public DocumentExistantRequestType createDocumentExistantRequestType() {
        return new DocumentExistantRequestType();
    }

    /**
     * Create an instance of {@link DocumentExistantResponseType }
     * 
     */
    public DocumentExistantResponseType createDocumentExistantResponseType() {
        return new DocumentExistantResponseType();
    }

    /**
     * Create an instance of {@link ConsultationGNTGNSRequestType }
     * 
     */
    public ConsultationGNTGNSRequestType createConsultationGNTGNSRequestType() {
        return new ConsultationGNTGNSRequestType();
    }

    /**
     * Create an instance of {@link ConsultationGNTGNSResponseType }
     * 
     */
    public ConsultationGNTGNSResponseType createConsultationGNTGNSResponseType() {
        return new ConsultationGNTGNSResponseType();
    }

    /**
     * Create an instance of {@link ModificationMasseRequestType }
     * 
     */
    public ModificationMasseRequestType createModificationMasseRequestType() {
        return new ModificationMasseRequestType();
    }

    /**
     * Create an instance of {@link ModificationMasseResponseType }
     * 
     */
    public ModificationMasseResponseType createModificationMasseResponseType() {
        return new ModificationMasseResponseType();
    }

    /**
     * Create an instance of {@link TransfertMasseRequestType }
     * 
     */
    public TransfertMasseRequestType createTransfertMasseRequestType() {
        return new TransfertMasseRequestType();
    }

    /**
     * Create an instance of {@link TransfertMasseResponseType }
     * 
     */
    public TransfertMasseResponseType createTransfertMasseResponseType() {
        return new TransfertMasseResponseType();
    }

    /**
     * Create an instance of {@link DeblocageRequestType }
     * 
     */
    public DeblocageRequestType createDeblocageRequestType() {
        return new DeblocageRequestType();
    }

    /**
     * Create an instance of {@link DeblocageResponseType }
     * 
     */
    public DeblocageResponseType createDeblocageResponseType() {
        return new DeblocageResponseType();
    }

    /**
     * Create an instance of {@link RepriseRequestType }
     * 
     */
    public RepriseRequestType createRepriseRequestType() {
        return new RepriseRequestType();
    }

    /**
     * Create an instance of {@link RepriseResponseType }
     * 
     */
    public RepriseResponseType createRepriseResponseType() {
        return new RepriseResponseType();
    }

    /**
     * Create an instance of {@link ListeUuidType }
     * 
     */
    public ListeUuidType createListeUuidType() {
        return new ListeUuidType();
    }

    /**
     * Create an instance of {@link TraitementMasseType }
     * 
     */
    public TraitementMasseType createTraitementMasseType() {
        return new TraitementMasseType();
    }

    /**
     * Create an instance of {@link ListeTraitementsMasseType }
     * 
     */
    public ListeTraitementsMasseType createListeTraitementsMasseType() {
        return new ListeTraitementsMasseType();
    }

    /**
     * Create an instance of {@link ObjetNumeriqueType }
     * 
     */
    public ObjetNumeriqueType createObjetNumeriqueType() {
        return new ObjetNumeriqueType();
    }

    /**
     * Create an instance of {@link MetadonneeType }
     * 
     */
    public MetadonneeType createMetadonneeType() {
        return new MetadonneeType();
    }

    /**
     * Create an instance of {@link ListeMetadonneeType }
     * 
     */
    public ListeMetadonneeType createListeMetadonneeType() {
        return new ListeMetadonneeType();
    }

    /**
     * Create an instance of {@link RangeMetadonneeType }
     * 
     */
    public RangeMetadonneeType createRangeMetadonneeType() {
        return new RangeMetadonneeType();
    }

    /**
     * Create an instance of {@link ListeRangeMetadonneeType }
     * 
     */
    public ListeRangeMetadonneeType createListeRangeMetadonneeType() {
        return new ListeRangeMetadonneeType();
    }

    /**
     * Create an instance of {@link ListeMetadonneeCodeType }
     * 
     */
    public ListeMetadonneeCodeType createListeMetadonneeCodeType() {
        return new ListeMetadonneeCodeType();
    }

    /**
     * Create an instance of {@link RequetePrincipaleType }
     * 
     */
    public RequetePrincipaleType createRequetePrincipaleType() {
        return new RequetePrincipaleType();
    }

    /**
     * Create an instance of {@link FiltreType }
     * 
     */
    public FiltreType createFiltreType() {
        return new FiltreType();
    }

    /**
     * Create an instance of {@link IdentifiantPageType }
     * 
     */
    public IdentifiantPageType createIdentifiantPageType() {
        return new IdentifiantPageType();
    }

    /**
     * Create an instance of {@link ResultatRechercheType }
     * 
     */
    public ResultatRechercheType createResultatRechercheType() {
        return new ResultatRechercheType();
    }

    /**
     * Create an instance of {@link ListeResultatRechercheType }
     * 
     */
    public ListeResultatRechercheType createListeResultatRechercheType() {
        return new ListeResultatRechercheType();
    }

    /**
     * Create an instance of {@link DataFileType }
     * 
     */
    public DataFileType createDataFileType() {
        return new DataFileType();
    }

    /**
     * Create an instance of {@link ObjetNumeriqueConsultationType }
     * 
     */
    public ObjetNumeriqueConsultationType createObjetNumeriqueConsultationType() {
        return new ObjetNumeriqueConsultationType();
    }

    /**
     * Create an instance of {@link MetadonneeDispoType }
     * 
     */
    public MetadonneeDispoType createMetadonneeDispoType() {
        return new MetadonneeDispoType();
    }

    /**
     * Create an instance of {@link ListeMetadonneeDispoType }
     * 
     */
    public ListeMetadonneeDispoType createListeMetadonneeDispoType() {
        return new ListeMetadonneeDispoType();
    }

    /**
     * Create an instance of {@link ResultatRechercheNbResType }
     * 
     */
    public ResultatRechercheNbResType createResultatRechercheNbResType() {
        return new ResultatRechercheNbResType();
    }

    /**
     * Create an instance of {@link ListeResultatRechercheNbResType }
     * 
     */
    public ListeResultatRechercheNbResType createListeResultatRechercheNbResType() {
        return new ListeResultatRechercheNbResType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageUnitaireRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageUnitaireRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageUnitaire")
    public JAXBElement<ArchivageUnitaireRequestType> createArchivageUnitaire(ArchivageUnitaireRequestType value) {
        return new JAXBElement<ArchivageUnitaireRequestType>(_ArchivageUnitaire_QNAME, ArchivageUnitaireRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageUnitaireResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageUnitaireResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageUnitaireResponse")
    public JAXBElement<ArchivageUnitaireResponseType> createArchivageUnitaireResponse(ArchivageUnitaireResponseType value) {
        return new JAXBElement<ArchivageUnitaireResponseType>(_ArchivageUnitaireResponse_QNAME, ArchivageUnitaireResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageUnitairePJRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageUnitairePJRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageUnitairePJ")
    public JAXBElement<ArchivageUnitairePJRequestType> createArchivageUnitairePJ(ArchivageUnitairePJRequestType value) {
        return new JAXBElement<ArchivageUnitairePJRequestType>(_ArchivageUnitairePJ_QNAME, ArchivageUnitairePJRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageUnitairePJResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageUnitairePJResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageUnitairePJResponse")
    public JAXBElement<ArchivageUnitairePJResponseType> createArchivageUnitairePJResponse(ArchivageUnitairePJResponseType value) {
        return new JAXBElement<ArchivageUnitairePJResponseType>(_ArchivageUnitairePJResponse_QNAME, ArchivageUnitairePJResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StockageUnitaireRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StockageUnitaireRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "stockageUnitaire")
    public JAXBElement<StockageUnitaireRequestType> createStockageUnitaire(StockageUnitaireRequestType value) {
        return new JAXBElement<StockageUnitaireRequestType>(_StockageUnitaire_QNAME, StockageUnitaireRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StockageUnitaireResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link StockageUnitaireResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "stockageUnitaireResponse")
    public JAXBElement<StockageUnitaireResponseType> createStockageUnitaireResponse(StockageUnitaireResponseType value) {
        return new JAXBElement<StockageUnitaireResponseType>(_StockageUnitaireResponse_QNAME, StockageUnitaireResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDocFormatOrigineRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetDocFormatOrigineRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "getDocFormatOrigine")
    public JAXBElement<GetDocFormatOrigineRequestType> createGetDocFormatOrigine(GetDocFormatOrigineRequestType value) {
        return new JAXBElement<GetDocFormatOrigineRequestType>(_GetDocFormatOrigine_QNAME, GetDocFormatOrigineRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDocFormatOrigineResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetDocFormatOrigineResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "getDocFormatOrigineResponse")
    public JAXBElement<GetDocFormatOrigineResponseType> createGetDocFormatOrigineResponse(GetDocFormatOrigineResponseType value) {
        return new JAXBElement<GetDocFormatOrigineResponseType>(_GetDocFormatOrigineResponse_QNAME, GetDocFormatOrigineResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuppressionMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SuppressionMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "suppressionMasse")
    public JAXBElement<SuppressionMasseRequestType> createSuppressionMasse(SuppressionMasseRequestType value) {
        return new JAXBElement<SuppressionMasseRequestType>(_SuppressionMasse_QNAME, SuppressionMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestoreMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RestoreMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "restoreMasse")
    public JAXBElement<RestoreMasseRequestType> createRestoreMasse(RestoreMasseRequestType value) {
        return new JAXBElement<RestoreMasseRequestType>(_RestoreMasse_QNAME, RestoreMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EtatTraitementsMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EtatTraitementsMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "etatTraitementsMasse")
    public JAXBElement<EtatTraitementsMasseRequestType> createEtatTraitementsMasse(EtatTraitementsMasseRequestType value) {
        return new JAXBElement<EtatTraitementsMasseRequestType>(_EtatTraitementsMasse_QNAME, EtatTraitementsMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageMasse")
    public JAXBElement<ArchivageMasseRequestType> createArchivageMasse(ArchivageMasseRequestType value) {
        return new JAXBElement<ArchivageMasseRequestType>(_ArchivageMasse_QNAME, ArchivageMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageMasseResponse")
    public JAXBElement<ArchivageMasseResponseType> createArchivageMasseResponse(ArchivageMasseResponseType value) {
        return new JAXBElement<ArchivageMasseResponseType>(_ArchivageMasseResponse_QNAME, ArchivageMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageMasseAvecHashRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageMasseAvecHashRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageMasseAvecHash")
    public JAXBElement<ArchivageMasseAvecHashRequestType> createArchivageMasseAvecHash(ArchivageMasseAvecHashRequestType value) {
        return new JAXBElement<ArchivageMasseAvecHashRequestType>(_ArchivageMasseAvecHash_QNAME, ArchivageMasseAvecHashRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArchivageMasseAvecHashResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArchivageMasseAvecHashResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "archivageMasseAvecHashResponse")
    public JAXBElement<ArchivageMasseAvecHashResponseType> createArchivageMasseAvecHashResponse(ArchivageMasseAvecHashResponseType value) {
        return new JAXBElement<ArchivageMasseAvecHashResponseType>(_ArchivageMasseAvecHashResponse_QNAME, ArchivageMasseAvecHashResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "recherche")
    public JAXBElement<RechercheRequestType> createRecherche(RechercheRequestType value) {
        return new JAXBElement<RechercheRequestType>(_Recherche_QNAME, RechercheRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "rechercheResponse")
    public JAXBElement<RechercheResponseType> createRechercheResponse(RechercheResponseType value) {
        return new JAXBElement<RechercheResponseType>(_RechercheResponse_QNAME, RechercheResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultation")
    public JAXBElement<ConsultationRequestType> createConsultation(ConsultationRequestType value) {
        return new JAXBElement<ConsultationRequestType>(_Consultation_QNAME, ConsultationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationResponse")
    public JAXBElement<ConsultationResponseType> createConsultationResponse(ConsultationResponseType value) {
        return new JAXBElement<ConsultationResponseType>(_ConsultationResponse_QNAME, ConsultationResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationMTOMRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationMTOMRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationMTOM")
    public JAXBElement<ConsultationMTOMRequestType> createConsultationMTOM(ConsultationMTOMRequestType value) {
        return new JAXBElement<ConsultationMTOMRequestType>(_ConsultationMTOM_QNAME, ConsultationMTOMRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationMTOMResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationMTOMResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationMTOMResponse")
    public JAXBElement<ConsultationMTOMResponseType> createConsultationMTOMResponse(ConsultationMTOMResponseType value) {
        return new JAXBElement<ConsultationMTOMResponseType>(_ConsultationMTOMResponse_QNAME, ConsultationMTOMResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificationRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ModificationRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "modification")
    public JAXBElement<ModificationRequestType> createModification(ModificationRequestType value) {
        return new JAXBElement<ModificationRequestType>(_Modification_QNAME, ModificationRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificationResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ModificationResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "modificationResponse")
    public JAXBElement<ModificationResponseType> createModificationResponse(ModificationResponseType value) {
        return new JAXBElement<ModificationResponseType>(_ModificationResponse_QNAME, ModificationResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuppressionRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SuppressionRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "suppression")
    public JAXBElement<SuppressionRequestType> createSuppression(SuppressionRequestType value) {
        return new JAXBElement<SuppressionRequestType>(_Suppression_QNAME, SuppressionRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuppressionResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SuppressionResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "suppressionResponse")
    public JAXBElement<SuppressionResponseType> createSuppressionResponse(SuppressionResponseType value) {
        return new JAXBElement<SuppressionResponseType>(_SuppressionResponse_QNAME, SuppressionResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperationMetadonneesResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RecuperationMetadonneesResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "recuperationMetadonneesResponse")
    public JAXBElement<RecuperationMetadonneesResponseType> createRecuperationMetadonneesResponse(RecuperationMetadonneesResponseType value) {
        return new JAXBElement<RecuperationMetadonneesResponseType>(_RecuperationMetadonneesResponse_QNAME, RecuperationMetadonneesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransfertRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransfertRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "transfert")
    public JAXBElement<TransfertRequestType> createTransfert(TransfertRequestType value) {
        return new JAXBElement<TransfertRequestType>(_Transfert_QNAME, TransfertRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransfertResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransfertResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "transfertResponse")
    public JAXBElement<TransfertResponseType> createTransfertResponse(TransfertResponseType value) {
        return new JAXBElement<TransfertResponseType>(_TransfertResponse_QNAME, TransfertResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationAffichableRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationAffichableRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationAffichable")
    public JAXBElement<ConsultationAffichableRequestType> createConsultationAffichable(ConsultationAffichableRequestType value) {
        return new JAXBElement<ConsultationAffichableRequestType>(_ConsultationAffichable_QNAME, ConsultationAffichableRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationAffichableResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationAffichableResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationAffichableResponse")
    public JAXBElement<ConsultationAffichableResponseType> createConsultationAffichableResponse(ConsultationAffichableResponseType value) {
        return new JAXBElement<ConsultationAffichableResponseType>(_ConsultationAffichableResponse_QNAME, ConsultationAffichableResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheNbResRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheNbResRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "rechercheNbRes")
    public JAXBElement<RechercheNbResRequestType> createRechercheNbRes(RechercheNbResRequestType value) {
        return new JAXBElement<RechercheNbResRequestType>(_RechercheNbRes_QNAME, RechercheNbResRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheNbResResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheNbResResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "rechercheNbResResponse")
    public JAXBElement<RechercheNbResResponseType> createRechercheNbResResponse(RechercheNbResResponseType value) {
        return new JAXBElement<RechercheNbResResponseType>(_RechercheNbResResponse_QNAME, RechercheNbResResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheParIterateurRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheParIterateurRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "rechercheParIterateur")
    public JAXBElement<RechercheParIterateurRequestType> createRechercheParIterateur(RechercheParIterateurRequestType value) {
        return new JAXBElement<RechercheParIterateurRequestType>(_RechercheParIterateur_QNAME, RechercheParIterateurRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RechercheParIterateurResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RechercheParIterateurResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "rechercheParIterateurResponse")
    public JAXBElement<RechercheParIterateurResponseType> createRechercheParIterateurResponse(RechercheParIterateurResponseType value) {
        return new JAXBElement<RechercheParIterateurResponseType>(_RechercheParIterateurResponse_QNAME, RechercheParIterateurResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjoutNoteRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AjoutNoteRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "ajoutNote")
    public JAXBElement<AjoutNoteRequestType> createAjoutNote(AjoutNoteRequestType value) {
        return new JAXBElement<AjoutNoteRequestType>(_AjoutNote_QNAME, AjoutNoteRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AjoutNoteResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link AjoutNoteResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "ajoutNoteResponse")
    public JAXBElement<AjoutNoteResponseType> createAjoutNoteResponse(AjoutNoteResponseType value) {
        return new JAXBElement<AjoutNoteResponseType>(_AjoutNoteResponse_QNAME, AjoutNoteResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SuppressionMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SuppressionMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "suppressionMasseResponse")
    public JAXBElement<SuppressionMasseResponseType> createSuppressionMasseResponse(SuppressionMasseResponseType value) {
        return new JAXBElement<SuppressionMasseResponseType>(_SuppressionMasseResponse_QNAME, SuppressionMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RestoreMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RestoreMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "restoreMasseResponse")
    public JAXBElement<RestoreMasseResponseType> createRestoreMasseResponse(RestoreMasseResponseType value) {
        return new JAXBElement<RestoreMasseResponseType>(_RestoreMasseResponse_QNAME, RestoreMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EtatTraitementsMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EtatTraitementsMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "etatTraitementsMasseResponse")
    public JAXBElement<EtatTraitementsMasseResponseType> createEtatTraitementsMasseResponse(EtatTraitementsMasseResponseType value) {
        return new JAXBElement<EtatTraitementsMasseResponseType>(_EtatTraitementsMasseResponse_QNAME, EtatTraitementsMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopieRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CopieRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "copie")
    public JAXBElement<CopieRequestType> createCopie(CopieRequestType value) {
        return new JAXBElement<CopieRequestType>(_Copie_QNAME, CopieRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopieResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CopieResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "copieResponse")
    public JAXBElement<CopieResponseType> createCopieResponse(CopieResponseType value) {
        return new JAXBElement<CopieResponseType>(_CopieResponse_QNAME, CopieResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentExistantRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DocumentExistantRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "documentExistant")
    public JAXBElement<DocumentExistantRequestType> createDocumentExistant(DocumentExistantRequestType value) {
        return new JAXBElement<DocumentExistantRequestType>(_DocumentExistant_QNAME, DocumentExistantRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentExistantResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DocumentExistantResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "documentExistantResponse")
    public JAXBElement<DocumentExistantResponseType> createDocumentExistantResponse(DocumentExistantResponseType value) {
        return new JAXBElement<DocumentExistantResponseType>(_DocumentExistantResponse_QNAME, DocumentExistantResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationGNTGNSRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationGNTGNSRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationGNTGNS")
    public JAXBElement<ConsultationGNTGNSRequestType> createConsultationGNTGNS(ConsultationGNTGNSRequestType value) {
        return new JAXBElement<ConsultationGNTGNSRequestType>(_ConsultationGNTGNS_QNAME, ConsultationGNTGNSRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultationGNTGNSResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConsultationGNTGNSResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "consultationGNTGNSResponse")
    public JAXBElement<ConsultationGNTGNSResponseType> createConsultationGNTGNSResponse(ConsultationGNTGNSResponseType value) {
        return new JAXBElement<ConsultationGNTGNSResponseType>(_ConsultationGNTGNSResponse_QNAME, ConsultationGNTGNSResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificationMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ModificationMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "modificationMasse")
    public JAXBElement<ModificationMasseRequestType> createModificationMasse(ModificationMasseRequestType value) {
        return new JAXBElement<ModificationMasseRequestType>(_ModificationMasse_QNAME, ModificationMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModificationMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ModificationMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "modificationMasseResponse")
    public JAXBElement<ModificationMasseResponseType> createModificationMasseResponse(ModificationMasseResponseType value) {
        return new JAXBElement<ModificationMasseResponseType>(_ModificationMasseResponse_QNAME, ModificationMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransfertMasseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransfertMasseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "transfertMasse")
    public JAXBElement<TransfertMasseRequestType> createTransfertMasse(TransfertMasseRequestType value) {
        return new JAXBElement<TransfertMasseRequestType>(_TransfertMasse_QNAME, TransfertMasseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransfertMasseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransfertMasseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "transfertMasseResponse")
    public JAXBElement<TransfertMasseResponseType> createTransfertMasseResponse(TransfertMasseResponseType value) {
        return new JAXBElement<TransfertMasseResponseType>(_TransfertMasseResponse_QNAME, TransfertMasseResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeblocageRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeblocageRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "deblocage")
    public JAXBElement<DeblocageRequestType> createDeblocage(DeblocageRequestType value) {
        return new JAXBElement<DeblocageRequestType>(_Deblocage_QNAME, DeblocageRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeblocageResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeblocageResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "deblocageResponse")
    public JAXBElement<DeblocageResponseType> createDeblocageResponse(DeblocageResponseType value) {
        return new JAXBElement<DeblocageResponseType>(_DeblocageResponse_QNAME, DeblocageResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepriseRequestType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RepriseRequestType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "reprise")
    public JAXBElement<RepriseRequestType> createReprise(RepriseRequestType value) {
        return new JAXBElement<RepriseRequestType>(_Reprise_QNAME, RepriseRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RepriseResponseType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link RepriseResponseType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeService", name = "repriseResponse")
    public JAXBElement<RepriseResponseType> createRepriseResponse(RepriseResponseType value) {
        return new JAXBElement<RepriseResponseType>(_RepriseResponse_QNAME, RepriseResponseType.class, null, value);
    }

}
