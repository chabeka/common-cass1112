
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Lien entre une entreprise dirigeante et une entreprise dirigée.
 * 
 * <p>Classe Java pour DirigeantPersMorale_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DirigeantPersMorale_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="qualiteDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}QualiteDirigeant_Type"/&gt;
 *         &lt;element name="dateEffet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="entrepriseDirigee" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="entrepriseDirigeante" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirigeantPersMorale_Type", propOrder = {
    "qualiteDirigeant",
    "dateEffet",
    "entrepriseDirigee",
    "entrepriseDirigeante"
})
public class DirigeantPersMoraleType {

    @XmlElement(required = true)
    protected QualiteDirigeantType qualiteDirigeant;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffet;
    protected long entrepriseDirigee;
    protected long entrepriseDirigeante;

    /**
     * Obtient la valeur de la propriété qualiteDirigeant.
     * 
     * @return
     *     possible object is
     *     {@link QualiteDirigeantType }
     *     
     */
    public QualiteDirigeantType getQualiteDirigeant() {
        return qualiteDirigeant;
    }

    /**
     * Définit la valeur de la propriété qualiteDirigeant.
     * 
     * @param value
     *     allowed object is
     *     {@link QualiteDirigeantType }
     *     
     */
    public void setQualiteDirigeant(QualiteDirigeantType value) {
        this.qualiteDirigeant = value;
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
     * Obtient la valeur de la propriété entrepriseDirigee.
     * 
     */
    public long getEntrepriseDirigee() {
        return entrepriseDirigee;
    }

    /**
     * Définit la valeur de la propriété entrepriseDirigee.
     * 
     */
    public void setEntrepriseDirigee(long value) {
        this.entrepriseDirigee = value;
    }

    /**
     * Obtient la valeur de la propriété entrepriseDirigeante.
     * 
     */
    public long getEntrepriseDirigeante() {
        return entrepriseDirigeante;
    }

    /**
     * Définit la valeur de la propriété entrepriseDirigeante.
     * 
     */
    public void setEntrepriseDirigeante(long value) {
        this.entrepriseDirigeante = value;
    }

}
