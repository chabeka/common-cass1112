
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * EtablissementResume contient une sous partie des informations propres a un etablissement
 * 
 * <p>Classe Java pour EtablissementResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EtablissementResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntiteCotisante_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="urGestionnaire" type="{http://pivot.datamodel.esb.cirso.fr/1.0}CODE-ORGANISME" minOccurs="0"/&gt;
 *         &lt;element name="siret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="enseigne" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="categorieEtablissement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CategorieEtablissement_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateFermeture" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EtablissementResume_Type", propOrder = {
    "urGestionnaire",
    "siret",
    "enseigne",
    "categorieEtablissement",
    "dateFermeture"
})
@XmlSeeAlso({
    EtablissementType.class
})
public class EtablissementResumeType
    extends EntiteCotisanteType
{

    protected String urGestionnaire;
    protected SIRETCertificationType siret;
    protected String enseigne;
    protected CategorieEtablissementType categorieEtablissement;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateFermeture;

    /**
     * Obtient la valeur de la propriété urGestionnaire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrGestionnaire() {
        return urGestionnaire;
    }

    /**
     * Définit la valeur de la propriété urGestionnaire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrGestionnaire(String value) {
        this.urGestionnaire = value;
    }

    /**
     * Obtient la valeur de la propriété siret.
     * 
     * @return
     *     possible object is
     *     {@link SIRETCertificationType }
     *     
     */
    public SIRETCertificationType getSiret() {
        return siret;
    }

    /**
     * Définit la valeur de la propriété siret.
     * 
     * @param value
     *     allowed object is
     *     {@link SIRETCertificationType }
     *     
     */
    public void setSiret(SIRETCertificationType value) {
        this.siret = value;
    }

    /**
     * Obtient la valeur de la propriété enseigne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnseigne() {
        return enseigne;
    }

    /**
     * Définit la valeur de la propriété enseigne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnseigne(String value) {
        this.enseigne = value;
    }

    /**
     * Obtient la valeur de la propriété categorieEtablissement.
     * 
     * @return
     *     possible object is
     *     {@link CategorieEtablissementType }
     *     
     */
    public CategorieEtablissementType getCategorieEtablissement() {
        return categorieEtablissement;
    }

    /**
     * Définit la valeur de la propriété categorieEtablissement.
     * 
     * @param value
     *     allowed object is
     *     {@link CategorieEtablissementType }
     *     
     */
    public void setCategorieEtablissement(CategorieEtablissementType value) {
        this.categorieEtablissement = value;
    }

    /**
     * Obtient la valeur de la propriété dateFermeture.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateFermeture() {
        return dateFermeture;
    }

    /**
     * Définit la valeur de la propriété dateFermeture.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateFermeture(XMLGregorianCalendar value) {
        this.dateFermeture = value;
    }

}
