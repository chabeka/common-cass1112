
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * IndividuDSI contient une sous partie des
 * 				informations propres a un individu dans le cadre de la
 * 				Reprise DSI
 * 
 * <p>Classe Java pour IndividuDSI_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndividuDSI_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="civilite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cleNir" type="{http://cfe.recouv/2008-11/TypeRegent}NIRCle_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateNaissance" type="{http://cfe.recouv/2008-11/TypeRegent}Date_Type" minOccurs="0"/&gt;
 *         &lt;element name="elegibiliteDSI" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="nir" type="{http://cfe.recouv/2008-11/TypeRegent}NIRNumero_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomUsuel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="prenomUsuel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siren" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRENAlphanumerique_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividuDSI_Type", propOrder = {
    "civilite",
    "cleNir",
    "dateNaissance",
    "elegibiliteDSI",
    "nir",
    "nomUsuel",
    "prenomUsuel",
    "siren"
})
public class IndividuDSIType {

    protected String civilite;
    protected String cleNir;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateNaissance;
    protected Boolean elegibiliteDSI;
    protected String nir;
    protected String nomUsuel;
    protected String prenomUsuel;
    protected String siren;

    /**
     * Obtient la valeur de la propriété civilite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * Définit la valeur de la propriété civilite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCivilite(String value) {
        this.civilite = value;
    }

    /**
     * Obtient la valeur de la propriété cleNir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCleNir() {
        return cleNir;
    }

    /**
     * Définit la valeur de la propriété cleNir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCleNir(String value) {
        this.cleNir = value;
    }

    /**
     * Obtient la valeur de la propriété dateNaissance.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Définit la valeur de la propriété dateNaissance.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateNaissance(XMLGregorianCalendar value) {
        this.dateNaissance = value;
    }

    /**
     * Obtient la valeur de la propriété elegibiliteDSI.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isElegibiliteDSI() {
        return elegibiliteDSI;
    }

    /**
     * Définit la valeur de la propriété elegibiliteDSI.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setElegibiliteDSI(Boolean value) {
        this.elegibiliteDSI = value;
    }

    /**
     * Obtient la valeur de la propriété nir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNir() {
        return nir;
    }

    /**
     * Définit la valeur de la propriété nir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNir(String value) {
        this.nir = value;
    }

    /**
     * Obtient la valeur de la propriété nomUsuel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomUsuel() {
        return nomUsuel;
    }

    /**
     * Définit la valeur de la propriété nomUsuel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomUsuel(String value) {
        this.nomUsuel = value;
    }

    /**
     * Obtient la valeur de la propriété prenomUsuel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrenomUsuel() {
        return prenomUsuel;
    }

    /**
     * Définit la valeur de la propriété prenomUsuel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrenomUsuel(String value) {
        this.prenomUsuel = value;
    }

    /**
     * Obtient la valeur de la propriété siren.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiren() {
        return siren;
    }

    /**
     * Définit la valeur de la propriété siren.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiren(String value) {
        this.siren = value;
    }

}
