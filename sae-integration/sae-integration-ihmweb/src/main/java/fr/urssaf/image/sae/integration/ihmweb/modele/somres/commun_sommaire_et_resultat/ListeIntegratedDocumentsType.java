//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.14 at 03:44:42 PM CET 
//


package fr.urssaf.image.sae.integration.ihmweb.modele.somres.commun_sommaire_et_resultat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * Une liste de documents archiv�s
 * 
 * <p>Java class for listeIntegratedDocumentsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listeIntegratedDocumentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="integratedDocument" type="{http://www.cirtil.fr/sae/commun_sommaire_et_resultat}integratedDocumentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listeIntegratedDocumentsType", propOrder = {
    "integratedDocument"
})
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ListeIntegratedDocumentsType {

    protected List<IntegratedDocumentType> integratedDocument;

    /**
     * Gets the value of the integratedDocument property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the integratedDocument property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntegratedDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IntegratedDocumentType }
     * 
     * 
     */
    public List<IntegratedDocumentType> getIntegratedDocument() {
        if (integratedDocument == null) {
            integratedDocument = new ArrayList<IntegratedDocumentType>();
        }
        return this.integratedDocument;
    }

}
