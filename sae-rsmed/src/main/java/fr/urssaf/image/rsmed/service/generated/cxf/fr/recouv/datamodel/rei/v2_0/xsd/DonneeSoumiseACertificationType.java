
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Pour indiquer le niveau de certification d'une donnee soumise a certification
 * 
 * <p>Classe Java pour DonneeSoumiseACertification_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="DonneeSoumiseACertification_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codeStatut" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeStatut_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DonneeSoumiseACertification_Type", propOrder = {
    "codeStatut"
})
@XmlSeeAlso({
    CodeGreffeImmatRCSCertificationType.class,
    CodeDptImmatRMCertificationType.class,
    NIRNumeroCertificationType.class,
    NIRCleCertificationType.class,
    PrenomsPatronymiquesCertificationType.class,
    NomCertificationType.class,
    PrenomCertificationType.class,
    DateCertificationType.class,
    CodeNationaliteCertificationType.class,
    CodeGeoINSEECertificationType.class,
    CertificatDecesCertificationType.class,
    NoRIBACertificationType.class,
    SIRENCertificationType.class,
    SIRENAlphanumeriqueCertificationType.class,
    CommuneCertificationType.class,
    GroupeProfessionelCertificationType.class,
    SIRETCertificationType.class,
    CodeNAFCertificationType.class,
    CodeCJCertificationType.class,
    NoRCSCertificationType.class,
    NoRMCertificationType.class,
    NoEIRLCertificationType.class,
    CPAMRattachementCertificationType.class,
    NoInscriptionPAMCertificationType.class,
    CodeSpecialiteCertificationType.class,
    RegimeConventionnelCertificationType.class
})
public class DonneeSoumiseACertificationType {

    protected Integer codeStatut;

    /**
     * Obtient la valeur de la propriété codeStatut.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodeStatut() {
        return codeStatut;
    }

    /**
     * Définit la valeur de la propriété codeStatut.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodeStatut(Integer value) {
        this.codeStatut = value;
    }

}
