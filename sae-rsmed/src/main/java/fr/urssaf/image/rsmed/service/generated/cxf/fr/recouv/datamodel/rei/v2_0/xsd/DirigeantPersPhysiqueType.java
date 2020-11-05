
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Relation entre Individu et l'entreprise en qualite de dirigeant.
 * 
 * <p>Classe Java pour DirigeantPersPhysique_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DirigeantPersPhysique_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idEntreprise" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="idIndividu" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IdREI_Type"/&gt;
 *         &lt;element name="groupeProfessionel" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}GroupeProfessionelCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="typeProfessionLiberale" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeTypeProfessionLiberale_Type" minOccurs="0"/&gt;
 *         &lt;element name="estForain" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codeCommuneRattachement" type="{http://cfe.recouv/2008-11/TypeRegent}Adr_CodePostal_Type" minOccurs="0"/&gt;
 *         &lt;element name="estAutoEntrepreneur" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="qualiteDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}QualiteDirigeant_Type"/&gt;
 *         &lt;element name="siretDirigeant" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="cpamRattachement" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CPAMRattachementCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="noInscriptionPAM" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoInscriptionPAMCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeSpecialite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeSpecialiteCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="regimeConventionnel" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}RegimeConventionnelCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="noRPPS" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRPPS_Type" minOccurs="0"/&gt;
 *         &lt;element name="estGerantMajoritaire" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirigeantPersPhysique_Type", propOrder = {
    "idEntreprise",
    "idIndividu",
    "groupeProfessionel",
    "typeProfessionLiberale",
    "estForain",
    "codeCommuneRattachement",
    "estAutoEntrepreneur",
    "qualiteDirigeant",
    "siretDirigeant",
    "cpamRattachement",
    "noInscriptionPAM",
    "codeSpecialite",
    "regimeConventionnel",
    "noRPPS",
    "estGerantMajoritaire"
})
@XmlSeeAlso({
    DirigeantPersPhysiqueCompletType.class
})
public class DirigeantPersPhysiqueType {

    protected long idEntreprise;
    protected long idIndividu;
    protected GroupeProfessionelCertificationType groupeProfessionel;
    protected String typeProfessionLiberale;
    protected Boolean estForain;
    protected String codeCommuneRattachement;
    protected Boolean estAutoEntrepreneur;
    @XmlElement(required = true)
    protected QualiteDirigeantType qualiteDirigeant;
    protected SIRETCertificationType siretDirigeant;
    protected CPAMRattachementCertificationType cpamRattachement;
    protected NoInscriptionPAMCertificationType noInscriptionPAM;
    protected CodeSpecialiteCertificationType codeSpecialite;
    protected RegimeConventionnelCertificationType regimeConventionnel;
    protected String noRPPS;
    protected Boolean estGerantMajoritaire;

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
     * Obtient la valeur de la propriété idIndividu.
     * 
     */
    public long getIdIndividu() {
        return idIndividu;
    }

    /**
     * Définit la valeur de la propriété idIndividu.
     * 
     */
    public void setIdIndividu(long value) {
        this.idIndividu = value;
    }

    /**
     * Obtient la valeur de la propriété groupeProfessionel.
     * 
     * @return
     *     possible object is
     *     {@link GroupeProfessionelCertificationType }
     *     
     */
    public GroupeProfessionelCertificationType getGroupeProfessionel() {
        return groupeProfessionel;
    }

    /**
     * Définit la valeur de la propriété groupeProfessionel.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupeProfessionelCertificationType }
     *     
     */
    public void setGroupeProfessionel(GroupeProfessionelCertificationType value) {
        this.groupeProfessionel = value;
    }

    /**
     * Obtient la valeur de la propriété typeProfessionLiberale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeProfessionLiberale() {
        return typeProfessionLiberale;
    }

    /**
     * Définit la valeur de la propriété typeProfessionLiberale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeProfessionLiberale(String value) {
        this.typeProfessionLiberale = value;
    }

    /**
     * Obtient la valeur de la propriété estForain.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstForain() {
        return estForain;
    }

    /**
     * Définit la valeur de la propriété estForain.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstForain(Boolean value) {
        this.estForain = value;
    }

    /**
     * Obtient la valeur de la propriété codeCommuneRattachement.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeCommuneRattachement() {
        return codeCommuneRattachement;
    }

    /**
     * Définit la valeur de la propriété codeCommuneRattachement.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeCommuneRattachement(String value) {
        this.codeCommuneRattachement = value;
    }

    /**
     * Obtient la valeur de la propriété estAutoEntrepreneur.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstAutoEntrepreneur() {
        return estAutoEntrepreneur;
    }

    /**
     * Définit la valeur de la propriété estAutoEntrepreneur.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstAutoEntrepreneur(Boolean value) {
        this.estAutoEntrepreneur = value;
    }

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
     * Obtient la valeur de la propriété siretDirigeant.
     * 
     * @return
     *     possible object is
     *     {@link SIRETCertificationType }
     *     
     */
    public SIRETCertificationType getSiretDirigeant() {
        return siretDirigeant;
    }

    /**
     * Définit la valeur de la propriété siretDirigeant.
     * 
     * @param value
     *     allowed object is
     *     {@link SIRETCertificationType }
     *     
     */
    public void setSiretDirigeant(SIRETCertificationType value) {
        this.siretDirigeant = value;
    }

    /**
     * Obtient la valeur de la propriété cpamRattachement.
     * 
     * @return
     *     possible object is
     *     {@link CPAMRattachementCertificationType }
     *     
     */
    public CPAMRattachementCertificationType getCpamRattachement() {
        return cpamRattachement;
    }

    /**
     * Définit la valeur de la propriété cpamRattachement.
     * 
     * @param value
     *     allowed object is
     *     {@link CPAMRattachementCertificationType }
     *     
     */
    public void setCpamRattachement(CPAMRattachementCertificationType value) {
        this.cpamRattachement = value;
    }

    /**
     * Obtient la valeur de la propriété noInscriptionPAM.
     * 
     * @return
     *     possible object is
     *     {@link NoInscriptionPAMCertificationType }
     *     
     */
    public NoInscriptionPAMCertificationType getNoInscriptionPAM() {
        return noInscriptionPAM;
    }

    /**
     * Définit la valeur de la propriété noInscriptionPAM.
     * 
     * @param value
     *     allowed object is
     *     {@link NoInscriptionPAMCertificationType }
     *     
     */
    public void setNoInscriptionPAM(NoInscriptionPAMCertificationType value) {
        this.noInscriptionPAM = value;
    }

    /**
     * Obtient la valeur de la propriété codeSpecialite.
     * 
     * @return
     *     possible object is
     *     {@link CodeSpecialiteCertificationType }
     *     
     */
    public CodeSpecialiteCertificationType getCodeSpecialite() {
        return codeSpecialite;
    }

    /**
     * Définit la valeur de la propriété codeSpecialite.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeSpecialiteCertificationType }
     *     
     */
    public void setCodeSpecialite(CodeSpecialiteCertificationType value) {
        this.codeSpecialite = value;
    }

    /**
     * Obtient la valeur de la propriété regimeConventionnel.
     * 
     * @return
     *     possible object is
     *     {@link RegimeConventionnelCertificationType }
     *     
     */
    public RegimeConventionnelCertificationType getRegimeConventionnel() {
        return regimeConventionnel;
    }

    /**
     * Définit la valeur de la propriété regimeConventionnel.
     * 
     * @param value
     *     allowed object is
     *     {@link RegimeConventionnelCertificationType }
     *     
     */
    public void setRegimeConventionnel(RegimeConventionnelCertificationType value) {
        this.regimeConventionnel = value;
    }

    /**
     * Obtient la valeur de la propriété noRPPS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoRPPS() {
        return noRPPS;
    }

    /**
     * Définit la valeur de la propriété noRPPS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoRPPS(String value) {
        this.noRPPS = value;
    }

    /**
     * Obtient la valeur de la propriété estGerantMajoritaire.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstGerantMajoritaire() {
        return estGerantMajoritaire;
    }

    /**
     * Définit la valeur de la propriété estGerantMajoritaire.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstGerantMajoritaire(Boolean value) {
        this.estGerantMajoritaire = value;
    }

}
