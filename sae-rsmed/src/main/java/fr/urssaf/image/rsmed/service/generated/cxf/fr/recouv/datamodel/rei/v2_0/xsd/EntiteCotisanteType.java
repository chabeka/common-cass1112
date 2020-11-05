
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Une entite cotisante peut etre soit un individu, une entreprise ou un etablissement.
 * 
 * <p>Classe Java pour EntiteCotisante_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="EntiteCotisante_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="typeEntite" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="codeOrigine" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntiteCotisante_Type", propOrder = {
    "id",
    "typeEntite",
    "codeOrigine"
})
@XmlSeeAlso({
    EtablissementResumeType.class,
    fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.IndividuResumeType.class,
    EntrepriseResumeType.class,
    RoleResumeType.class,
    fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl.IndividuResumeType.class
})
public abstract class EntiteCotisanteType {

    protected long id;
    protected Integer typeEntite;
    protected Integer codeOrigine;

    /**
     * Obtient la valeur de la propriété id.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété typeEntite.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTypeEntite() {
        return typeEntite;
    }

    /**
     * Définit la valeur de la propriété typeEntite.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTypeEntite(Integer value) {
        this.typeEntite = value;
    }

    /**
     * Obtient la valeur de la propriété codeOrigine.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodeOrigine() {
        return codeOrigine;
    }

    /**
     * Définit la valeur de la propriété codeOrigine.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodeOrigine(Integer value) {
        this.codeOrigine = value;
    }

}
