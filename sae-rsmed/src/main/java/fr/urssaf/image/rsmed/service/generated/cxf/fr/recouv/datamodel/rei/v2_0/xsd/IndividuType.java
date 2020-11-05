
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Un individu est une personne physique redevable ou potentiellement redevable de cotisations sociales a quelque titre que ce soit professionnellement ou personnellement	
 * 
 * <p>Classe Java pour Individu_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Individu_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}IndividuResume_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pseudonyme" type="{http://cfe.recouv/2008-11/TypeRegent}Prenom_Type" minOccurs="0"/&gt;
 *         &lt;element name="cleNir" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NIRCleCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="validiteNir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificatNirRsi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAgessa" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoAGESSA_Type" minOccurs="0"/&gt;
 *         &lt;element name="idMdArtiste" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoMDArtiste_Type" minOccurs="0"/&gt;
 *         &lt;element name="pseudoSiret" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRETAlphanumerique_Type" minOccurs="0"/&gt;
 *         &lt;element name="sirenPersonnel" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}SIRENAlphanumeriqueCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="noCpam" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="libelleCommuneNaissance" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CommuneCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeSituationFamille" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeSituationMatrimoniale_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeNationalite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeNationaliteCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="nationalite" type="{http://cfe.recouv/2008-11/TypeRegent}Nationalite_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateDeces" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DateCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="certificatDeces" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CertificatDecesCertification_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Individu_Type", propOrder = {
    "pseudonyme",
    "cleNir",
    "validiteNir",
    "certificatNirRsi",
    "idAgessa",
    "idMdArtiste",
    "pseudoSiret",
    "sirenPersonnel",
    "noCpam",
    "libelleCommuneNaissance",
    "codeSituationFamille",
    "codeNationalite",
    "nationalite",
    "dateDeces",
    "certificatDeces"
})
@XmlSeeAlso({
    IndividuCompletType.class
})
public class IndividuType
    extends IndividuResumeType
{

    protected String pseudonyme;
    protected NIRCleCertificationType cleNir;
    protected String validiteNir;
    protected String certificatNirRsi;
    protected String idAgessa;
    protected String idMdArtiste;
    protected String pseudoSiret;
    protected SIRENAlphanumeriqueCertificationType sirenPersonnel;
    protected String noCpam;
    protected CommuneCertificationType libelleCommuneNaissance;
    protected String codeSituationFamille;
    protected CodeNationaliteCertificationType codeNationalite;
    protected String nationalite;
    protected DateCertificationType dateDeces;
    protected CertificatDecesCertificationType certificatDeces;

    /**
     * Obtient la valeur de la propriété pseudonyme.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseudonyme() {
        return pseudonyme;
    }

    /**
     * Définit la valeur de la propriété pseudonyme.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseudonyme(String value) {
        this.pseudonyme = value;
    }

    /**
     * Obtient la valeur de la propriété cleNir.
     * 
     * @return
     *     possible object is
     *     {@link NIRCleCertificationType }
     *     
     */
    public NIRCleCertificationType getCleNir() {
        return cleNir;
    }

    /**
     * Définit la valeur de la propriété cleNir.
     * 
     * @param value
     *     allowed object is
     *     {@link NIRCleCertificationType }
     *     
     */
    public void setCleNir(NIRCleCertificationType value) {
        this.cleNir = value;
    }

    /**
     * Obtient la valeur de la propriété validiteNir.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValiditeNir() {
        return validiteNir;
    }

    /**
     * Définit la valeur de la propriété validiteNir.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValiditeNir(String value) {
        this.validiteNir = value;
    }

    /**
     * Obtient la valeur de la propriété certificatNirRsi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificatNirRsi() {
        return certificatNirRsi;
    }

    /**
     * Définit la valeur de la propriété certificatNirRsi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificatNirRsi(String value) {
        this.certificatNirRsi = value;
    }

    /**
     * Obtient la valeur de la propriété idAgessa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAgessa() {
        return idAgessa;
    }

    /**
     * Définit la valeur de la propriété idAgessa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAgessa(String value) {
        this.idAgessa = value;
    }

    /**
     * Obtient la valeur de la propriété idMdArtiste.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMdArtiste() {
        return idMdArtiste;
    }

    /**
     * Définit la valeur de la propriété idMdArtiste.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMdArtiste(String value) {
        this.idMdArtiste = value;
    }

    /**
     * Obtient la valeur de la propriété pseudoSiret.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseudoSiret() {
        return pseudoSiret;
    }

    /**
     * Définit la valeur de la propriété pseudoSiret.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseudoSiret(String value) {
        this.pseudoSiret = value;
    }

    /**
     * Obtient la valeur de la propriété sirenPersonnel.
     * 
     * @return
     *     possible object is
     *     {@link SIRENAlphanumeriqueCertificationType }
     *     
     */
    public SIRENAlphanumeriqueCertificationType getSirenPersonnel() {
        return sirenPersonnel;
    }

    /**
     * Définit la valeur de la propriété sirenPersonnel.
     * 
     * @param value
     *     allowed object is
     *     {@link SIRENAlphanumeriqueCertificationType }
     *     
     */
    public void setSirenPersonnel(SIRENAlphanumeriqueCertificationType value) {
        this.sirenPersonnel = value;
    }

    /**
     * Obtient la valeur de la propriété noCpam.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCpam() {
        return noCpam;
    }

    /**
     * Définit la valeur de la propriété noCpam.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCpam(String value) {
        this.noCpam = value;
    }

    /**
     * Obtient la valeur de la propriété libelleCommuneNaissance.
     * 
     * @return
     *     possible object is
     *     {@link CommuneCertificationType }
     *     
     */
    public CommuneCertificationType getLibelleCommuneNaissance() {
        return libelleCommuneNaissance;
    }

    /**
     * Définit la valeur de la propriété libelleCommuneNaissance.
     * 
     * @param value
     *     allowed object is
     *     {@link CommuneCertificationType }
     *     
     */
    public void setLibelleCommuneNaissance(CommuneCertificationType value) {
        this.libelleCommuneNaissance = value;
    }

    /**
     * Obtient la valeur de la propriété codeSituationFamille.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeSituationFamille() {
        return codeSituationFamille;
    }

    /**
     * Définit la valeur de la propriété codeSituationFamille.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeSituationFamille(String value) {
        this.codeSituationFamille = value;
    }

    /**
     * Obtient la valeur de la propriété codeNationalite.
     * 
     * @return
     *     possible object is
     *     {@link CodeNationaliteCertificationType }
     *     
     */
    public CodeNationaliteCertificationType getCodeNationalite() {
        return codeNationalite;
    }

    /**
     * Définit la valeur de la propriété codeNationalite.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeNationaliteCertificationType }
     *     
     */
    public void setCodeNationalite(CodeNationaliteCertificationType value) {
        this.codeNationalite = value;
    }

    /**
     * Obtient la valeur de la propriété nationalite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * Définit la valeur de la propriété nationalite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationalite(String value) {
        this.nationalite = value;
    }

    /**
     * Obtient la valeur de la propriété dateDeces.
     * 
     * @return
     *     possible object is
     *     {@link DateCertificationType }
     *     
     */
    public DateCertificationType getDateDeces() {
        return dateDeces;
    }

    /**
     * Définit la valeur de la propriété dateDeces.
     * 
     * @param value
     *     allowed object is
     *     {@link DateCertificationType }
     *     
     */
    public void setDateDeces(DateCertificationType value) {
        this.dateDeces = value;
    }

    /**
     * Obtient la valeur de la propriété certificatDeces.
     * 
     * @return
     *     possible object is
     *     {@link CertificatDecesCertificationType }
     *     
     */
    public CertificatDecesCertificationType getCertificatDeces() {
        return certificatDeces;
    }

    /**
     * Définit la valeur de la propriété certificatDeces.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificatDecesCertificationType }
     *     
     */
    public void setCertificatDeces(CertificatDecesCertificationType value) {
        this.certificatDeces = value;
    }

}
