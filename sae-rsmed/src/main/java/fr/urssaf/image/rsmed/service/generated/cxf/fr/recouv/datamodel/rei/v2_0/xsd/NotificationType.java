
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Une notification correspond a un message asynchrone transmis par la V2 pour signaler un evenement
 * 
 * <p>Classe Java pour Notification_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Notification_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="noLiasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dateEffet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="evenement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="codeOrganisme" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="noCompteV2" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT" minOccurs="0"/&gt;
 *         &lt;element name="siret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="siret1" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="siret2" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="idPersonneV2" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdV2_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Notification_Type", propOrder = {
    "noLiasse",
    "dateEffet",
    "evenement",
    "codeOrganisme",
    "noCompteV2",
    "siret",
    "siret1",
    "siret2",
    "idPersonneV2"
})
public class NotificationType {

    protected String noLiasse;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffet;
    @XmlElement(required = true)
    protected String evenement;
    protected String codeOrganisme;
    protected String noCompteV2;
    protected String siret;
    protected String siret1;
    protected String siret2;
    protected String idPersonneV2;

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

    /**
     * Obtient la valeur de la propriété dateEffet.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEffet() {
        return dateEffet;
    }

    /**
     * Définit la valeur de la propriété dateEffet.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEffet(XMLGregorianCalendar value) {
        this.dateEffet = value;
    }

    /**
     * Obtient la valeur de la propriété evenement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvenement() {
        return evenement;
    }

    /**
     * Définit la valeur de la propriété evenement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvenement(String value) {
        this.evenement = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrganisme.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeOrganisme() {
        return codeOrganisme;
    }

    /**
     * Définit la valeur de la propriété codeOrganisme.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeOrganisme(String value) {
        this.codeOrganisme = value;
    }

    /**
     * Obtient la valeur de la propriété noCompteV2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCompteV2() {
        return noCompteV2;
    }

    /**
     * Définit la valeur de la propriété noCompteV2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCompteV2(String value) {
        this.noCompteV2 = value;
    }

    /**
     * Obtient la valeur de la propriété siret.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret() {
        return siret;
    }

    /**
     * Définit la valeur de la propriété siret.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiret(String value) {
        this.siret = value;
    }

    /**
     * Obtient la valeur de la propriété siret1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret1() {
        return siret1;
    }

    /**
     * Définit la valeur de la propriété siret1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiret1(String value) {
        this.siret1 = value;
    }

    /**
     * Obtient la valeur de la propriété siret2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret2() {
        return siret2;
    }

    /**
     * Définit la valeur de la propriété siret2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiret2(String value) {
        this.siret2 = value;
    }

    /**
     * Obtient la valeur de la propriété idPersonneV2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPersonneV2() {
        return idPersonneV2;
    }

    /**
     * Définit la valeur de la propriété idPersonneV2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPersonneV2(String value) {
        this.idPersonneV2 = value;
    }

}
