
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent.AdrIndiceRepetitionType;


/**
 * Une adresse peut etre trouvee a partir de differents criteres : code postal et code commune sont des criteres principaux (obligatroires) et les autres champs de l'adresse sont des criteres secondaires
 * 
 * <p>Classe Java pour CriteresRechercheAdresse_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="CriteresRechercheAdresse_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeTypeVoie" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Adr_TypeVoie_Type" minOccurs="0"/&gt;
 *         &lt;element name="numVoie" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_NumVoie_Type" minOccurs="0"/&gt;
 *         &lt;element name="libelleVoie" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_VoieLieuDit_Type" minOccurs="0"/&gt;
 *         &lt;element name="codePostal" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_CodePostal_Type"/&gt;
 *         &lt;element name="pays" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_Pays_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeCommuneINSEE" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}REICodeGeoINSEE_Type"/&gt;
 *         &lt;element name="indiceRepetition" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_IndiceRepetition_Type" minOccurs="0"/&gt;
 *         &lt;element name="libelleCommuneEtranger" type="{http://cfe.recouv/2008-11/TypeRegent}Commune_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CriteresRechercheAdresse_Type", propOrder = {
    "codeTypeVoie",
    "numVoie",
    "libelleVoie",
    "codePostal",
    "pays",
    "codeCommuneINSEE",
    "indiceRepetition",
    "libelleCommuneEtranger"
})
public class CriteresRechercheAdresseType {

    @XmlSchemaType(name = "string")
    protected AdrTypeVoieType codeTypeVoie;
    protected String numVoie;
    protected String libelleVoie;
    @XmlElement(required = true)
    protected String codePostal;
    protected String pays;
    @XmlElement(required = true)
    protected REICodeGeoINSEEType codeCommuneINSEE;
    @XmlSchemaType(name = "string")
    protected AdrIndiceRepetitionType indiceRepetition;
    protected String libelleCommuneEtranger;

    /**
     * Obtient la valeur de la propriété codeTypeVoie.
     * 
     * @return
     *     possible object is
     *     {@link AdrTypeVoieType }
     *     
     */
    public AdrTypeVoieType getCodeTypeVoie() {
        return codeTypeVoie;
    }

    /**
     * Définit la valeur de la propriété codeTypeVoie.
     * 
     * @param value
     *     allowed object is
     *     {@link AdrTypeVoieType }
     *     
     */
    public void setCodeTypeVoie(AdrTypeVoieType value) {
        this.codeTypeVoie = value;
    }

    /**
     * Obtient la valeur de la propriété numVoie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumVoie() {
        return numVoie;
    }

    /**
     * Définit la valeur de la propriété numVoie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumVoie(String value) {
        this.numVoie = value;
    }

    /**
     * Obtient la valeur de la propriété libelleVoie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleVoie() {
        return libelleVoie;
    }

    /**
     * Définit la valeur de la propriété libelleVoie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleVoie(String value) {
        this.libelleVoie = value;
    }

    /**
     * Obtient la valeur de la propriété codePostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * Définit la valeur de la propriété codePostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodePostal(String value) {
        this.codePostal = value;
    }

    /**
     * Obtient la valeur de la propriété pays.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPays() {
        return pays;
    }

    /**
     * Définit la valeur de la propriété pays.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPays(String value) {
        this.pays = value;
    }

    /**
     * Obtient la valeur de la propriété codeCommuneINSEE.
     * 
     * @return
     *     possible object is
     *     {@link REICodeGeoINSEEType }
     *     
     */
    public REICodeGeoINSEEType getCodeCommuneINSEE() {
        return codeCommuneINSEE;
    }

    /**
     * Définit la valeur de la propriété codeCommuneINSEE.
     * 
     * @param value
     *     allowed object is
     *     {@link REICodeGeoINSEEType }
     *     
     */
    public void setCodeCommuneINSEE(REICodeGeoINSEEType value) {
        this.codeCommuneINSEE = value;
    }

    /**
     * Obtient la valeur de la propriété indiceRepetition.
     * 
     * @return
     *     possible object is
     *     {@link AdrIndiceRepetitionType }
     *     
     */
    public AdrIndiceRepetitionType getIndiceRepetition() {
        return indiceRepetition;
    }

    /**
     * Définit la valeur de la propriété indiceRepetition.
     * 
     * @param value
     *     allowed object is
     *     {@link AdrIndiceRepetitionType }
     *     
     */
    public void setIndiceRepetition(AdrIndiceRepetitionType value) {
        this.indiceRepetition = value;
    }

    /**
     * Obtient la valeur de la propriété libelleCommuneEtranger.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleCommuneEtranger() {
        return libelleCommuneEtranger;
    }

    /**
     * Définit la valeur de la propriété libelleCommuneEtranger.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleCommuneEtranger(String value) {
        this.libelleCommuneEtranger = value;
    }

}
