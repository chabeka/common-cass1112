//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.19 at 05:19:14 PM CET 
//


package un.unece.uncefact.identifierlist.standard._5.iso316612a.secondedition2006vi_3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the un.unece.uncefact.identifierlist.standard._5.iso316612a.secondedition2006vi_3 package. 
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

    private final static QName _ISOTwoletterCountryCodeIdentifier_QNAME = new QName("urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3", "ISOTwoletterCountryCodeIdentifier");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: un.unece.uncefact.identifierlist.standard._5.iso316612a.secondedition2006vi_3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ISOTwoletterCountryCodeIdentifierContentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3", name = "ISOTwoletterCountryCodeIdentifier")
    public JAXBElement<ISOTwoletterCountryCodeIdentifierContentType> createISOTwoletterCountryCodeIdentifier(ISOTwoletterCountryCodeIdentifierContentType value) {
        return new JAXBElement<ISOTwoletterCountryCodeIdentifierContentType>(_ISOTwoletterCountryCodeIdentifier_QNAME, ISOTwoletterCountryCodeIdentifierContentType.class, null, value);
    }

}
