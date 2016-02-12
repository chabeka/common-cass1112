//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.02.09 à 11:29:35 AM CET 
//


package fr.urssaf.image.sae.lotinstallmaj.modele.metadata;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.sae.lotinstallmaj.modele.metadata package. 
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

    private final static QName _IndexesComposites_QNAME = new QName("http://www.cirtil.fr/lotinstallmaj/metadata", "indexesComposites");
    private final static QName _Referentiel_QNAME = new QName("http://www.cirtil.fr/lotinstallmaj/metadata", "referentiel");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.sae.lotinstallmaj.modele.metadata
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IndexesComposites }
     * 
     */
    public IndexesComposites createIndexesComposites() {
        return new IndexesComposites();
    }

    /**
     * Create an instance of {@link ReferentielMeta }
     * 
     */
    public ReferentielMeta createReferentielMeta() {
        return new ReferentielMeta();
    }

    /**
     * Create an instance of {@link MetaReference }
     * 
     */
    public MetaReference createMetaReference() {
        return new MetaReference();
    }

    /**
     * Create an instance of {@link IndexReference }
     * 
     */
    public IndexReference createIndexReference() {
        return new IndexReference();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IndexesComposites }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/lotinstallmaj/metadata", name = "indexesComposites")
    public JAXBElement<IndexesComposites> createIndexesComposites(IndexesComposites value) {
        return new JAXBElement<IndexesComposites>(_IndexesComposites_QNAME, IndexesComposites.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferentielMeta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/lotinstallmaj/metadata", name = "referentiel")
    public JAXBElement<ReferentielMeta> createReferentiel(ReferentielMeta value) {
        return new JAXBElement<ReferentielMeta>(_Referentiel_QNAME, ReferentielMeta.class, null, value);
    }

}
