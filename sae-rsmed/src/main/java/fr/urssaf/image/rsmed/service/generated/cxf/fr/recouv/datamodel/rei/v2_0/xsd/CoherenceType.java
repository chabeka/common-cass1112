
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * coherence entre V2 et REI qui permet de maintenir le lien entre l'entite cotisante (etablissement)  et la personne V2. ou l'entite cotisante (entreprise) et la personne V2.
 * 
 * <p>Classe Java pour Coherence_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Coherence_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idREI" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type" minOccurs="0"/&gt;
 *         &lt;element name="idV2" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdV2_Type" minOccurs="0"/&gt;
 *         &lt;element name="noUrGestionnaire" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME"/&gt;
 *         &lt;element name="noLiasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Coherence_Type", propOrder = {
    "idREI",
    "idV2",
    "noUrGestionnaire",
    "noLiasse"
})
public class CoherenceType {

    protected Long idREI;
    protected String idV2;
    @XmlElement(required = true)
    protected String noUrGestionnaire;
    protected String noLiasse;

    /**
     * Obtient la valeur de la propriété idREI.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdREI() {
        return idREI;
    }

    /**
     * Définit la valeur de la propriété idREI.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdREI(Long value) {
        this.idREI = value;
    }

    /**
     * Obtient la valeur de la propriété idV2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdV2() {
        return idV2;
    }

    /**
     * Définit la valeur de la propriété idV2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdV2(String value) {
        this.idV2 = value;
    }

    /**
     * Obtient la valeur de la propriété noUrGestionnaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoUrGestionnaire() {
        return noUrGestionnaire;
    }

    /**
     * Définit la valeur de la propriété noUrGestionnaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoUrGestionnaire(String value) {
        this.noUrGestionnaire = value;
    }

    /**
     * Obtient la valeur de la propriété noLiasse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoLiasse() {
        return noLiasse;
    }

    /**
     * Définit la valeur de la propriété noLiasse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoLiasse(String value) {
        this.noLiasse = value;
    }

}
