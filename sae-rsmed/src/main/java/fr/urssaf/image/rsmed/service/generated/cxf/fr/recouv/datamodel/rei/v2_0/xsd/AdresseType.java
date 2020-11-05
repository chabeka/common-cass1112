
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import fr.urssaf.image.rsmed.service.generated.cxf.recouv.cfe._2008_11.typeregent.AdrIndiceRepetitionType;


/**
 * Adresse postale, geographique ou geopostale
 * 
 * <p>Classe Java pour Adresse_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Adresse_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dateEffet" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="codeOrigine" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeOrigine_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeTypeVoie" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_TypeVoie_Type" minOccurs="0"/&gt;
 *         &lt;element name="numVoie" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_NumVoie_Type" minOccurs="0"/&gt;
 *         &lt;element name="libelleVoie" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_VoieLieuDit_Type" minOccurs="0"/&gt;
 *         &lt;element name="codePostal" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_CodePostal_Type" minOccurs="0"/&gt;
 *         &lt;element name="localiteBureauDistrib" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_Commune_Type" minOccurs="0"/&gt;
 *         &lt;element name="pays" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_Pays_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeCommuneINSEE" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}REICodeGeoINSEE_Type" minOccurs="0"/&gt;
 *         &lt;element name="libelleCommuneEtranger" type="{http://cfe.recouv/2008-11/TypeRegent}Commune_Type" minOccurs="0"/&gt;
 *         &lt;element name="complementAdresse" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_Complement_Type" minOccurs="0"/&gt;
 *         &lt;element name="indiceRepetition" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_IndiceRepetition_Type" minOccurs="0"/&gt;
 *         &lt;element name="distributionSpeciale" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_DistribSpeciale_Type" minOccurs="0"/&gt;
 *         &lt;element name="validite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}ValiditeAdresse_Type" minOccurs="0"/&gt;
 *         &lt;element name="destinataire" type="{http://cfe.recouv/2008-11/TypeRegent}AlphaNum_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Adresse_Type", propOrder = {
    "dateEffet",
    "codeOrigine",
    "codeTypeVoie",
    "numVoie",
    "libelleVoie",
    "codePostal",
    "localiteBureauDistrib",
    "pays",
    "codeCommuneINSEE",
    "libelleCommuneEtranger",
    "complementAdresse",
    "indiceRepetition",
    "distributionSpeciale",
    "validite",
    "destinataire"
})
public class AdresseType {

    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateEffet;
    protected Integer codeOrigine;
    protected String codeTypeVoie;
    protected String numVoie;
    protected String libelleVoie;
    protected String codePostal;
    protected String localiteBureauDistrib;
    protected String pays;
    protected REICodeGeoINSEEType codeCommuneINSEE;
    protected String libelleCommuneEtranger;
    protected String complementAdresse;
    @XmlSchemaType(name = "string")
    protected AdrIndiceRepetitionType indiceRepetition;
    protected String distributionSpeciale;
    protected ValiditeAdresseType validite;
    protected String destinataire;

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

    /**
     * Obtient la valeur de la propriété codeTypeVoie.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeTypeVoie() {
        return codeTypeVoie;
    }

    /**
     * Définit la valeur de la propriété codeTypeVoie.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeTypeVoie(String value) {
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
     * Obtient la valeur de la propriété localiteBureauDistrib.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocaliteBureauDistrib() {
        return localiteBureauDistrib;
    }

    /**
     * Définit la valeur de la propriété localiteBureauDistrib.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocaliteBureauDistrib(String value) {
        this.localiteBureauDistrib = value;
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

    /**
     * Obtient la valeur de la propriété complementAdresse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplementAdresse() {
        return complementAdresse;
    }

    /**
     * Définit la valeur de la propriété complementAdresse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplementAdresse(String value) {
        this.complementAdresse = value;
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
     * Obtient la valeur de la propriété distributionSpeciale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributionSpeciale() {
        return distributionSpeciale;
    }

    /**
     * Définit la valeur de la propriété distributionSpeciale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributionSpeciale(String value) {
        this.distributionSpeciale = value;
    }

    /**
     * Obtient la valeur de la propriété validite.
     * 
     * @return
     *     possible object is
     *     {@link ValiditeAdresseType }
     *     
     */
    public ValiditeAdresseType getValidite() {
        return validite;
    }

    /**
     * Définit la valeur de la propriété validite.
     * 
     * @param value
     *     allowed object is
     *     {@link ValiditeAdresseType }
     *     
     */
    public void setValidite(ValiditeAdresseType value) {
        this.validite = value;
    }

    /**
     * Obtient la valeur de la propriété destinataire.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinataire() {
        return destinataire;
    }

    /**
     * Définit la valeur de la propriété destinataire.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinataire(String value) {
        this.destinataire = value;
    }

}
