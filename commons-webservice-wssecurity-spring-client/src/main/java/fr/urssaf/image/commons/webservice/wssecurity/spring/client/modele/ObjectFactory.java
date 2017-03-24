
package fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele package. 
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

    private final static QName _AllDocumentsResponse_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "allDocumentsResponse");
    private final static QName _AllDocuments_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "allDocuments");
    private final static QName _GetDocumentResponse_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "getDocumentResponse");
    private final static QName _Save_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "save");
    private final static QName _SaveResponse_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "saveResponse");
    private final static QName _GetDocument_QNAME = new QName("http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", "getDocument");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link AllDocuments }
     * 
     */
    public AllDocuments createAllDocuments() {
        return new AllDocuments();
    }

    /**
     * Create an instance of {@link GetDocumentResponse }
     * 
     */
    public GetDocumentResponse createGetDocumentResponse() {
        return new GetDocumentResponse();
    }

    /**
     * Create an instance of {@link Save }
     * 
     */
    public Save createSave() {
        return new Save();
    }

    /**
     * Create an instance of {@link GetDocument }
     * 
     */
    public GetDocument createGetDocument() {
        return new GetDocument();
    }

    /**
     * Create an instance of {@link SaveResponse }
     * 
     */
    public SaveResponse createSaveResponse() {
        return new SaveResponse();
    }

    /**
     * Create an instance of {@link AllDocumentsResponse }
     * 
     */
    public AllDocumentsResponse createAllDocumentsResponse() {
        return new AllDocumentsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AllDocumentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "allDocumentsResponse")
    public JAXBElement<AllDocumentsResponse> createAllDocumentsResponse(AllDocumentsResponse value) {
        return new JAXBElement<AllDocumentsResponse>(_AllDocumentsResponse_QNAME, AllDocumentsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AllDocuments }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "allDocuments")
    public JAXBElement<AllDocuments> createAllDocuments(AllDocuments value) {
        return new JAXBElement<AllDocuments>(_AllDocuments_QNAME, AllDocuments.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDocumentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "getDocumentResponse")
    public JAXBElement<GetDocumentResponse> createGetDocumentResponse(GetDocumentResponse value) {
        return new JAXBElement<GetDocumentResponse>(_GetDocumentResponse_QNAME, GetDocumentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Save }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "save")
    public JAXBElement<Save> createSave(Save value) {
        return new JAXBElement<Save>(_Save_QNAME, Save.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "saveResponse")
    public JAXBElement<SaveResponse> createSaveResponse(SaveResponse value) {
        return new JAXBElement<SaveResponse>(_SaveResponse_QNAME, SaveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.spring.wssecurity.webservice.commons.image.urssaf.fr/", name = "getDocument")
    public JAXBElement<GetDocument> createGetDocument(GetDocument value) {
        return new JAXBElement<GetDocument>(_GetDocument_QNAME, GetDocument.class, null, value);
    }

}
