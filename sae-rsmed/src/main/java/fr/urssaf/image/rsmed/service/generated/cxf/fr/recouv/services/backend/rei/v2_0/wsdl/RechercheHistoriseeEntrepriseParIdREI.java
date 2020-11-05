
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
 *         &lt;element name="idEntreprise" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}IdREI_Type"/&gt;
 *         &lt;element name="dateEffetRecherche" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="options" type="{http://www.recouv.fr/services/backend/REI/v2.0/wsdl}OptionsRechercheEntreprise_Type" minOccurs="0"/&gt;
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
    "idEntreprise",
    "dateEffetRecherche",
    "options"
})
@XmlRootElement(name = "RechercheHistoriseeEntrepriseParIdREI")
public class RechercheHistoriseeEntrepriseParIdREI {

    protected long idEntreprise;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffetRecherche;
    protected OptionsRechercheEntrepriseType options;

    /**
     * Obtient la valeur de la propriété idEntreprise.
     * 
     */
    public long getIdEntreprise() {
        return idEntreprise;
    }

    /**
     * Définit la valeur de la propriété idEntreprise.
     * 
     */
    public void setIdEntreprise(long value) {
        this.idEntreprise = value;
    }

    /**
     * Obtient la valeur de la propriété dateEffetRecherche.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateEffetRecherche() {
        return dateEffetRecherche;
    }

    /**
     * Définit la valeur de la propriété dateEffetRecherche.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateEffetRecherche(XMLGregorianCalendar value) {
        this.dateEffetRecherche = value;
    }

    /**
     * Obtient la valeur de la propriété options.
     * 
     * @return
     *     possible object is
     *     {@link OptionsRechercheEntrepriseType }
     *     
     */
    public OptionsRechercheEntrepriseType getOptions() {
        return options;
    }

    /**
     * Définit la valeur de la propriété options.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsRechercheEntrepriseType }
     *     
     */
    public void setOptions(OptionsRechercheEntrepriseType value) {
        this.options = value;
    }

}
