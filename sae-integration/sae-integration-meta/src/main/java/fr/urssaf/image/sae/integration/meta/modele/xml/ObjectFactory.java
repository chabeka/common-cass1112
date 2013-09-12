//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.09.12 à 05:03:21 PM CEST 
//


package fr.urssaf.image.sae.integration.meta.modele.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.sae.integration.meta.modele.xml package. 
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

    private final static QName _Meta_QNAME = new QName("http://www.cirtil.fr/saeIntegration/meta", "meta");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.sae.integration.meta.modele.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MetaType }
     * 
     */
    public MetaType createMetaType() {
        return new MetaType();
    }

    /**
     * Create an instance of {@link ListeDictionnaireType }
     * 
     */
    public ListeDictionnaireType createListeDictionnaireType() {
        return new ListeDictionnaireType();
    }

    /**
     * Create an instance of {@link MetadonneeType }
     * 
     */
    public MetadonneeType createMetadonneeType() {
        return new MetadonneeType();
    }

    /**
     * Create an instance of {@link ListeMetadonneesType }
     * 
     */
    public ListeMetadonneesType createListeMetadonneesType() {
        return new ListeMetadonneesType();
    }

    /**
     * Create an instance of {@link ListeValeursDictionnaireType }
     * 
     */
    public ListeValeursDictionnaireType createListeValeursDictionnaireType() {
        return new ListeValeursDictionnaireType();
    }

    /**
     * Create an instance of {@link DictionnaireType }
     * 
     */
    public DictionnaireType createDictionnaireType() {
        return new DictionnaireType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeIntegration/meta", name = "meta")
    public JAXBElement<MetaType> createMeta(MetaType value) {
        return new JAXBElement<MetaType>(_Meta_QNAME, MetaType.class, null, value);
    }

}
