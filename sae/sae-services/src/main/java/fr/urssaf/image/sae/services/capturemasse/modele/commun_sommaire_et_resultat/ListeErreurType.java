//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.14 at 11:29:41 AM CET 
//


package fr.urssaf.image.sae.services.capturemasse.modele.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste d'erreur
 * 
 * <p>Java class for listeErreurType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeErreurType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="erreur" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}erreurType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeErreurType", propOrder = {
    "erreur"
})
@SuppressWarnings("PMD")
public class ListeErreurType {

    protected List<ErreurType> erreur;

    /**
     * Gets the value of the erreur property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the erreur property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErreur().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErreurType }
     * 
     * 
     */
    public List<ErreurType> getErreur() {
        if (erreur == null) {
            erreur = new ArrayList<ErreurType>();
        }
        return this.erreur;
    }

}
