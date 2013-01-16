//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5-2 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2013.01.16 à 02:11:30 PM CET 
//


package fr.urssaf.image.sae.integration.droits.modele.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.urssaf.image.sae.integration.droits.modele.xml package. 
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

    private final static QName _Droit_QNAME = new QName("http://www.cirtil.fr/saeIntegration/droit", "droit");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.urssaf.image.sae.integration.droits.modele.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DroitType }
     * 
     */
    public DroitType createDroitType() {
        return new DroitType();
    }

    /**
     * Create an instance of {@link ListeCnPkiType }
     * 
     */
    public ListeCnPkiType createListeCnPkiType() {
        return new ListeCnPkiType();
    }

    /**
     * Create an instance of {@link ListeMetadonneeType }
     * 
     */
    public ListeMetadonneeType createListeMetadonneeType() {
        return new ListeMetadonneeType();
    }

    /**
     * Create an instance of {@link ListeCsType }
     * 
     */
    public ListeCsType createListeCsType() {
        return new ListeCsType();
    }

    /**
     * Create an instance of {@link MetadonneeType }
     * 
     */
    public MetadonneeType createMetadonneeType() {
        return new MetadonneeType();
    }

    /**
     * Create an instance of {@link PrmdType }
     * 
     */
    public PrmdType createPrmdType() {
        return new PrmdType();
    }

    /**
     * Create an instance of {@link ListeAjoutPagmType }
     * 
     */
    public ListeAjoutPagmType createListeAjoutPagmType() {
        return new ListeAjoutPagmType();
    }

    /**
     * Create an instance of {@link ListePrmdType }
     * 
     */
    public ListePrmdType createListePrmdType() {
        return new ListePrmdType();
    }

    /**
     * Create an instance of {@link ListeCnCertType }
     * 
     */
    public ListeCnCertType createListeCnCertType() {
        return new ListeCnCertType();
    }

    /**
     * Create an instance of {@link ParametrePagmType }
     * 
     */
    public ParametrePagmType createParametrePagmType() {
        return new ParametrePagmType();
    }

    /**
     * Create an instance of {@link PagmpType }
     * 
     */
    public PagmpType createPagmpType() {
        return new PagmpType();
    }

    /**
     * Create an instance of {@link ListePagmType }
     * 
     */
    public ListePagmType createListePagmType() {
        return new ListePagmType();
    }

    /**
     * Create an instance of {@link PagmaType }
     * 
     */
    public PagmaType createPagmaType() {
        return new PagmaType();
    }

    /**
     * Create an instance of {@link PagmType }
     * 
     */
    public PagmType createPagmType() {
        return new PagmType();
    }

    /**
     * Create an instance of {@link ListeParametresPagmType }
     * 
     */
    public ListeParametresPagmType createListeParametresPagmType() {
        return new ListeParametresPagmType();
    }

    /**
     * Create an instance of {@link AjoutPagmType }
     * 
     */
    public AjoutPagmType createAjoutPagmType() {
        return new AjoutPagmType();
    }

    /**
     * Create an instance of {@link CsType }
     * 
     */
    public CsType createCsType() {
        return new CsType();
    }

    /**
     * Create an instance of {@link ListeActionsType }
     * 
     */
    public ListeActionsType createListeActionsType() {
        return new ListeActionsType();
    }

    /**
     * Create an instance of {@link ListeValeursType }
     * 
     */
    public ListeValeursType createListeValeursType() {
        return new ListeValeursType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DroitType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.cirtil.fr/saeIntegration/droit", name = "droit")
    public JAXBElement<DroitType> createDroit(DroitType value) {
        return new JAXBElement<DroitType>(_Droit_QNAME, DroitType.class, null, value);
    }

}
