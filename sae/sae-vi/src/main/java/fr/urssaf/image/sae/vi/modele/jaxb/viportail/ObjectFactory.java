//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.31 at 10:03:34 AM CEST 
//


package fr.urssaf.image.sae.vi.modele.jaxb.viportail;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.sae.vi.modele.jaxb.viportail package. 
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
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ObjectFactory {

    private final static QName _Vi_QNAME = new QName("http://www.cirtil.fr/sae/viPortailAportail", "vi");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.sae.vi.modele.jaxb.viportail
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PagmsType }
     * 
     */
    public PagmsType createPagmsType() {
        return new PagmsType();
    }

    /**
     * Create an instance of {@link ViType }
     * 
     */
    public ViType createViType() {
        return new ViType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/sae/viPortailAportail", name = "vi")
    public JAXBElement<ViType> createVi(ViType value) {
        return new JAXBElement<ViType>(_Vi_QNAME, ViType.class, null, value);
    }

}
