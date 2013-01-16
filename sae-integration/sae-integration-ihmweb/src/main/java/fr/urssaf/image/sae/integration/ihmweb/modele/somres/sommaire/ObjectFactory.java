//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 09:24:43 AM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.sommaire;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.sae.integration.ihmweb.modele.somres.sommaire package. 
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

    private final static QName _Sommaire_QNAME = new QName("http://www.cirtil.fr/sae/sommaireXml", "sommaire");
    private final static QName _SommaireTypeRestitutionUuids_QNAME = new QName("http://www.cirtil.fr/sae/sommaireXml", "restitutionUuids");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.sae.integration.ihmweb.modele.somres.sommaire
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SommaireType }
     * 
     */
    public SommaireType createSommaireType() {
        return new SommaireType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SommaireType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/sae/sommaireXml", name = "sommaire")
    public JAXBElement<SommaireType> createSommaire(SommaireType value) {
        return new JAXBElement<SommaireType>(_Sommaire_QNAME, SommaireType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/sae/sommaireXml", name = "restitutionUuids", scope = SommaireType.class)
    public JAXBElement<Boolean> createSommaireTypeRestitutionUuids(Boolean value) {
        return new JAXBElement<Boolean>(_SommaireTypeRestitutionUuids_QNAME, Boolean.class, SommaireType.class, value);
    }

}
