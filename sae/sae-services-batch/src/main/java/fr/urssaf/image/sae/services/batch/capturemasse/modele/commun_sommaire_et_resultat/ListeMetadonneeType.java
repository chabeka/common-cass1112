//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.27 at 10:07:17 AM CEST 
//


package fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de métadonnées
 * 
 * <p>Java class for listeMetadonneeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeMetadonneeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadonnee" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}metadonneeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeMetadonneeType", propOrder = {
    "metadonnee"
})
public class ListeMetadonneeType {

    protected List<MetadonneeType> metadonnee;

    /**
     * Gets the value of the metadonnee property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadonnee property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadonnee().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MetadonneeType }
     * 
     * 
     */
    public List<MetadonneeType> getMetadonnee() {
        if (metadonnee == null) {
            metadonnee = new ArrayList<MetadonneeType>();
        }
        return this.metadonnee;
    }

}
