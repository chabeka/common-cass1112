//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.02 at 04:48:49 PM CET 
//


package fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * D�finition d'un document � archiver
 * 
 * <p>Java class for documentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="objetNumerique" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}fichierType"/>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType"/>
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentType", propOrder = {
    "objetNumerique",
    "metadonnees",
    "numeroPageDebut",
    "nombreDePages"
})
@SuppressWarnings("PMD")
public class DocumentType {

    @XmlElement(required = true)
    protected FichierType objetNumerique;
    @XmlElement(required = true)
    protected ListeMetadonneeType metadonnees;
    protected Integer numeroPageDebut;
    protected Integer nombreDePages;

    /**
     * Gets the value of the objetNumerique property.
     * 
     * @return
     *     possible object is
     *     {@link FichierType }
     *     
     */
    public FichierType getObjetNumerique() {
        return objetNumerique;
    }

    /**
     * Sets the value of the objetNumerique property.
     * 
     * @param value
     *     allowed object is
     *     {@link FichierType }
     *     
     */
    public void setObjetNumerique(FichierType value) {
        this.objetNumerique = value;
    }

    /**
     * Gets the value of the metadonnees property.
     * 
     * @return
     *     possible object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public ListeMetadonneeType getMetadonnees() {
        return metadonnees;
    }

    /**
     * Sets the value of the metadonnees property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeMetadonneeType }
     *     
     */
    public void setMetadonnees(ListeMetadonneeType value) {
        this.metadonnees = value;
    }

    /**
     * Gets the value of the numeroPageDebut property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroPageDebut() {
        return numeroPageDebut;
    }

    /**
     * Sets the value of the numeroPageDebut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroPageDebut(Integer value) {
        this.numeroPageDebut = value;
    }

    /**
     * Gets the value of the nombreDePages property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNombreDePages() {
        return nombreDePages;
    }

    /**
     * Sets the value of the nombreDePages property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNombreDePages(Integer value) {
        this.nombreDePages = value;
    }

}
