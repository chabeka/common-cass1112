
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * CompteEligibleDSI contient des informations du
 * 				compte eligible a la dsi dans le cadre de la Reprise DSI
 * 			
 * 
 * <p>Classe Java pour CompteDSI_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CompteDSI_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeUR" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="groupePro" type="{http://pivot.datamodel.esb.cirso.fr/1.0}GRP-PRO" minOccurs="0"/&gt;
 *         &lt;element name="noCompteExterne" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-EXT-CPT" minOccurs="0"/&gt;
 *         &lt;element name="noRiba" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRIBA_Type" minOccurs="0"/&gt;
 *         &lt;element name="siret" type="{http://pivot.datamodel.esb.cirso.fr/1.0}NO-ETAB" minOccurs="0"/&gt;
 *         &lt;element name="statut" type="{http://pivot.datamodel.esb.cirso.fr/1.0}LIB-CD-ETA-TI" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompteDSI_Type", propOrder = {
    "codeUR",
    "groupePro",
    "noCompteExterne",
    "noRiba",
    "siret",
    "statut"
})
public class CompteDSIType {

    protected String codeUR;
    protected String groupePro;
    protected String noCompteExterne;
    protected String noRiba;
    protected String siret;
    protected String statut;

    /**
     * Obtient la valeur de la propriété codeUR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeUR() {
        return codeUR;
    }

    /**
     * Définit la valeur de la propriété codeUR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeUR(String value) {
        this.codeUR = value;
    }

    /**
     * Obtient la valeur de la propriété groupePro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupePro() {
        return groupePro;
    }

    /**
     * Définit la valeur de la propriété groupePro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupePro(String value) {
        this.groupePro = value;
    }

    /**
     * Obtient la valeur de la propriété noCompteExterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCompteExterne() {
        return noCompteExterne;
    }

    /**
     * Définit la valeur de la propriété noCompteExterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCompteExterne(String value) {
        this.noCompteExterne = value;
    }

    /**
     * Obtient la valeur de la propriété noRiba.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRiba() {
        return noRiba;
    }

    /**
     * Définit la valeur de la propriété noRiba.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRiba(String value) {
        this.noRiba = value;
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
     * Obtient la valeur de la propriété statut.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit la valeur de la propriété statut.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatut(String value) {
        this.statut = value;
    }

}
