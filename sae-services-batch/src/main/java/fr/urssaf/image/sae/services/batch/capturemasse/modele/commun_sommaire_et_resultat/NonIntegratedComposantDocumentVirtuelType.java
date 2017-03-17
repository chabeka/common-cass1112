//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.27 at 03:38:22 PM CET 
//


package fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Composant d'un document virtuel (indexation)
 * 
 * <p>Java class for nonIntegratedComposantDocumentVirtuelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nonIntegratedComposantDocumentVirtuelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnees" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeMetadonneeType" minOccurs="0"/>
 *         &lt;element name="erreurs" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}listeErreurType"/>
 *         &lt;element name="numeroPageDebut" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nombreDePages" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nonIntegratedComposantDocumentVirtuelType", propOrder = {
    "metadonnees",
    "erreurs",
    "numeroPageDebut",
    "nombreDePages"
})
public class NonIntegratedComposantDocumentVirtuelType {

    protected ListeMetadonneeType metadonnees;
    @XmlElement(required = true)
    protected ListeErreurType erreurs;
    protected int numeroPageDebut;
    protected int nombreDePages;

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
     * Gets the value of the erreurs property.
     * 
     * @return
     *     possible object is
     *     {@link ListeErreurType }
     *     
     */
    public ListeErreurType getErreurs() {
        return erreurs;
    }

    /**
     * Sets the value of the erreurs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListeErreurType }
     *     
     */
    public void setErreurs(ListeErreurType value) {
        this.erreurs = value;
    }

    /**
     * Gets the value of the numeroPageDebut property.
     * 
     */
    public int getNumeroPageDebut() {
        return numeroPageDebut;
    }

    /**
     * Sets the value of the numeroPageDebut property.
     * 
     */
    public void setNumeroPageDebut(int value) {
        this.numeroPageDebut = value;
    }

    /**
     * Gets the value of the nombreDePages property.
     * 
     */
    public int getNombreDePages() {
        return nombreDePages;
    }

    /**
     * Sets the value of the nombreDePages property.
     * 
     */
    public void setNombreDePages(int value) {
        this.nombreDePages = value;
    }

}
