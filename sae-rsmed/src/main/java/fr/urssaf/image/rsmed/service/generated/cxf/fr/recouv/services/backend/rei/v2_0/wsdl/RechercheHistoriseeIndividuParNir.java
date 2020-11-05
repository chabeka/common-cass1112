
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nir" type="{http://cfe.recouv/2008-11/TypeRegent}NIRNumero_Type"/&gt;
 *         &lt;element name="dateEffetRecheche" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nir",
    "dateEffetRecheche"
})
@XmlRootElement(name = "RechercheHistoriseeIndividuParNir")
public class RechercheHistoriseeIndividuParNir {

    @XmlElement(required = true)
    protected String nir;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffetRecheche;

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
     * Obtient la valeur de la propriété dateEffetRecheche.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEffetRecheche() {
        return dateEffetRecheche;
    }

    /**
     * Définit la valeur de la propriété dateEffetRecheche.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEffetRecheche(XMLGregorianCalendar value) {
        this.dateEffetRecheche = value;
    }

}
