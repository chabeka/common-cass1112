
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * AdresseDSI contient des informations de l'adresse
 * 				de l'individu eligible a la dsi dans le cadre de la Reprise DSI
 * 			
 * 
 * <p>Classe Java pour AdresseDSI_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="AdresseDSI_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adresse1" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN1" minOccurs="0"/&gt;
 *         &lt;element name="adresse2" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN2" minOccurs="0"/&gt;
 *         &lt;element name="adresse3" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN3" minOccurs="0"/&gt;
 *         &lt;element name="adresse4" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN4" minOccurs="0"/&gt;
 *         &lt;element name="adresse5" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN5" minOccurs="0"/&gt;
 *         &lt;element name="adresse6" type="{http://pivot.datamodel.esb.cirso.fr/1.0}ADR-COR-LGN6" minOccurs="0"/&gt;
 *         &lt;element name="codeInsee" type="{http://cfe.recouv/2008-11/TypeRegent}CodeGeoINSEE_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdresseDSI_Type", propOrder = {
    "adresse1",
    "adresse2",
    "adresse3",
    "adresse4",
    "adresse5",
    "adresse6",
    "codeInsee"
})
public class AdresseDSIType {

    protected String adresse1;
    protected String adresse2;
    protected String adresse3;
    protected String adresse4;
    protected String adresse5;
    protected String adresse6;
    protected String codeInsee;

    /**
     * Obtient la valeur de la propriété adresse1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse1() {
        return adresse1;
    }

    /**
     * Définit la valeur de la propriété adresse1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse1(String value) {
        this.adresse1 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse2() {
        return adresse2;
    }

    /**
     * Définit la valeur de la propriété adresse2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse2(String value) {
        this.adresse2 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse3() {
        return adresse3;
    }

    /**
     * Définit la valeur de la propriété adresse3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse3(String value) {
        this.adresse3 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse4() {
        return adresse4;
    }

    /**
     * Définit la valeur de la propriété adresse4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse4(String value) {
        this.adresse4 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse5() {
        return adresse5;
    }

    /**
     * Définit la valeur de la propriété adresse5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse5(String value) {
        this.adresse5 = value;
    }

    /**
     * Obtient la valeur de la propriété adresse6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresse6() {
        return adresse6;
    }

    /**
     * Définit la valeur de la propriété adresse6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresse6(String value) {
        this.adresse6 = value;
    }

    /**
     * Obtient la valeur de la propriété codeInsee.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeInsee() {
        return codeInsee;
    }

    /**
     * Définit la valeur de la propriété codeInsee.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeInsee(String value) {
        this.codeInsee = value;
    }

}
