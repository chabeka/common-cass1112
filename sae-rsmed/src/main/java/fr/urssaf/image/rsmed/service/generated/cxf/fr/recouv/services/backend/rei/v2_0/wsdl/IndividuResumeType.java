
package fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.services.backend.rei.v2_0.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CiviliteType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.CodeGeoINSEECertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.DateCertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.EntiteCotisanteType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.NIRNumeroCertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.NoRIBACertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.NomCertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.PrenomCertificationType;
import fr.urssaf.image.rsmed.service.generated.cxf.fr.recouv.datamodel.rei.v2_0.xsd.PrenomsPatronymiquesCertificationType;


/**
 * IndividuResume contient une sous partie des
 * 						informations propres a un individu
 * 
 * <p>Classe Java pour IndividuResume_Type complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="IndividuResume_Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}EntiteCotisante_Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nir" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NIRNumeroCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="civilite" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}Civilite_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomPatronymique" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NomCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomMarital" type="{http://cfe.recouv/2008-11/TypeRegent}Nom_Type" minOccurs="0"/&gt;
 *         &lt;element name="nomUsage" type="{http://cfe.recouv/2008-11/TypeRegent}Nom_Type" minOccurs="0"/&gt;
 *         &lt;element name="prenomsPatronymiques" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}PrenomsPatronymiquesCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="prenomUsuel" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}PrenomCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="dateNaissance" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}DateCertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codeCommuneInseeNaissance" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeGeoINSEECertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="codePaysNaissance" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}CodeGeoINSEECertification_Type" minOccurs="0"/&gt;
 *         &lt;element name="noRiba" type="{http://www.recouv.fr/datamodel/REI/v2.0/xsd}NoRIBACertification_Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividuResume_Type", propOrder = {
    "nir",
    "civilite",
    "nomPatronymique",
    "nomMarital",
    "nomUsage",
    "prenomsPatronymiques",
    "prenomUsuel",
    "dateNaissance",
    "codeCommuneInseeNaissance",
    "codePaysNaissance",
    "noRiba"
})
public class IndividuResumeType
    extends EntiteCotisanteType
{

    protected NIRNumeroCertificationType nir;
    protected CiviliteType civilite;
    protected NomCertificationType nomPatronymique;
    protected String nomMarital;
    protected String nomUsage;
    protected PrenomsPatronymiquesCertificationType prenomsPatronymiques;
    protected PrenomCertificationType prenomUsuel;
    protected DateCertificationType dateNaissance;
    protected CodeGeoINSEECertificationType codeCommuneInseeNaissance;
    protected CodeGeoINSEECertificationType codePaysNaissance;
    protected NoRIBACertificationType noRiba;

    /**
     * Obtient la valeur de la propriété nir.
     * 
     * @return
     *     possible object is
     *     {@link NIRNumeroCertificationType }
     *     
     */
    public NIRNumeroCertificationType getNir() {
        return nir;
    }

    /**
     * Définit la valeur de la propriété nir.
     * 
     * @param value
     *     allowed object is
     *     {@link NIRNumeroCertificationType }
     *     
     */
    public void setNir(NIRNumeroCertificationType value) {
        this.nir = value;
    }

    /**
     * Obtient la valeur de la propriété civilite.
     * 
     * @return
     *     possible object is
     *     {@link CiviliteType }
     *     
     */
    public CiviliteType getCivilite() {
        return civilite;
    }

    /**
     * Définit la valeur de la propriété civilite.
     * 
     * @param value
     *     allowed object is
     *     {@link CiviliteType }
     *     
     */
    public void setCivilite(CiviliteType value) {
        this.civilite = value;
    }

    /**
     * Obtient la valeur de la propriété nomPatronymique.
     * 
     * @return
     *     possible object is
     *     {@link NomCertificationType }
     *     
     */
    public NomCertificationType getNomPatronymique() {
        return nomPatronymique;
    }

    /**
     * Définit la valeur de la propriété nomPatronymique.
     * 
     * @param value
     *     allowed object is
     *     {@link NomCertificationType }
     *     
     */
    public void setNomPatronymique(NomCertificationType value) {
        this.nomPatronymique = value;
    }

    /**
     * Obtient la valeur de la propriété nomMarital.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomMarital() {
        return nomMarital;
    }

    /**
     * Définit la valeur de la propriété nomMarital.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomMarital(String value) {
        this.nomMarital = value;
    }

    /**
     * Obtient la valeur de la propriété nomUsage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomUsage() {
        return nomUsage;
    }

    /**
     * Définit la valeur de la propriété nomUsage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomUsage(String value) {
        this.nomUsage = value;
    }

    /**
     * Obtient la valeur de la propriété prenomsPatronymiques.
     * 
     * @return
     *     possible object is
     *     {@link PrenomsPatronymiquesCertificationType }
     *     
     */
    public PrenomsPatronymiquesCertificationType getPrenomsPatronymiques() {
        return prenomsPatronymiques;
    }

    /**
     * Définit la valeur de la propriété prenomsPatronymiques.
     * 
     * @param value
     *     allowed object is
     *     {@link PrenomsPatronymiquesCertificationType }
     *     
     */
    public void setPrenomsPatronymiques(PrenomsPatronymiquesCertificationType value) {
        this.prenomsPatronymiques = value;
    }

    /**
     * Obtient la valeur de la propriété prenomUsuel.
     * 
     * @return
     *     possible object is
     *     {@link PrenomCertificationType }
     *     
     */
    public PrenomCertificationType getPrenomUsuel() {
        return prenomUsuel;
    }

    /**
     * Définit la valeur de la propriété prenomUsuel.
     * 
     * @param value
     *     allowed object is
     *     {@link PrenomCertificationType }
     *     
     */
    public void setPrenomUsuel(PrenomCertificationType value) {
        this.prenomUsuel = value;
    }

    /**
     * Obtient la valeur de la propriété dateNaissance.
     * 
     * @return
     *     possible object is
     *     {@link DateCertificationType }
     *     
     */
    public DateCertificationType getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Définit la valeur de la propriété dateNaissance.
     * 
     * @param value
     *     allowed object is
     *     {@link DateCertificationType }
     *     
     */
    public void setDateNaissance(DateCertificationType value) {
        this.dateNaissance = value;
    }

    /**
     * Obtient la valeur de la propriété codeCommuneInseeNaissance.
     * 
     * @return
     *     possible object is
     *     {@link CodeGeoINSEECertificationType }
     *     
     */
    public CodeGeoINSEECertificationType getCodeCommuneInseeNaissance() {
        return codeCommuneInseeNaissance;
    }

    /**
     * Définit la valeur de la propriété codeCommuneInseeNaissance.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeGeoINSEECertificationType }
     *     
     */
    public void setCodeCommuneInseeNaissance(CodeGeoINSEECertificationType value) {
        this.codeCommuneInseeNaissance = value;
    }

    /**
     * Obtient la valeur de la propriété codePaysNaissance.
     * 
     * @return
     *     possible object is
     *     {@link CodeGeoINSEECertificationType }
     *     
     */
    public CodeGeoINSEECertificationType getCodePaysNaissance() {
        return codePaysNaissance;
    }

    /**
     * Définit la valeur de la propriété codePaysNaissance.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeGeoINSEECertificationType }
     *     
     */
    public void setCodePaysNaissance(CodeGeoINSEECertificationType value) {
        this.codePaysNaissance = value;
    }

    /**
     * Obtient la valeur de la propriété noRiba.
     * 
     * @return
     *     possible object is
     *     {@link NoRIBACertificationType }
     *     
     */
    public NoRIBACertificationType getNoRiba() {
        return noRiba;
    }

    /**
     * Définit la valeur de la propriété noRiba.
     * 
     * @param value
     *     allowed object is
     *     {@link NoRIBACertificationType }
     *     
     */
    public void setNoRiba(NoRIBACertificationType value) {
        this.noRiba = value;
    }

}
