
package fr.urssaf.image.commons.webservice.exemple.ssl.document.modele;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.commons.webservice.exemple.ssl.document.modele package. 
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

    private final static QName _Multiplie_QNAME = new QName("http://localhost/", "multiplie");
    private final static QName _BonjourResponse_QNAME = new QName("http://localhost/", "bonjourResponse");
    private final static QName _Bonjour_QNAME = new QName("http://localhost/", "bonjour");
    private final static QName _MultiplieResponse_QNAME = new QName("http://localhost/", "multiplieResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.commons.webservice.exemple.ssl.document.modele
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MultiplieResponseType }
     * 
     */
    public MultiplieResponseType createMultiplieResponseType() {
        return new MultiplieResponseType();
    }

    /**
     * Create an instance of {@link BonjourResponseType }
     * 
     */
    public BonjourResponseType createBonjourResponseType() {
        return new BonjourResponseType();
    }

    /**
     * Create an instance of {@link MultiplieRequestType }
     * 
     */
    public MultiplieRequestType createMultiplieRequestType() {
        return new MultiplieRequestType();
    }

    /**
     * Create an instance of {@link BonjourRequestType }
     * 
     */
    public BonjourRequestType createBonjourRequestType() {
        return new BonjourRequestType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiplieRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/", name = "multiplie")
    public JAXBElement<MultiplieRequestType> createMultiplie(MultiplieRequestType value) {
        return new JAXBElement<MultiplieRequestType>(_Multiplie_QNAME, MultiplieRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BonjourResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/", name = "bonjourResponse")
    public JAXBElement<BonjourResponseType> createBonjourResponse(BonjourResponseType value) {
        return new JAXBElement<BonjourResponseType>(_BonjourResponse_QNAME, BonjourResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BonjourRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/", name = "bonjour")
    public JAXBElement<BonjourRequestType> createBonjour(BonjourRequestType value) {
        return new JAXBElement<BonjourRequestType>(_Bonjour_QNAME, BonjourRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiplieResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost/", name = "multiplieResponse")
    public JAXBElement<MultiplieResponseType> createMultiplieResponse(MultiplieResponseType value) {
        return new JAXBElement<MultiplieResponseType>(_MultiplieResponse_QNAME, MultiplieResponseType.class, null, value);
    }

}
