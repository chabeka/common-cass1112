//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.22 at 10:23:26 AM CET 
//


package fr.gouv.ae.archive.draft.standard_echange_v0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesCodeType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesIDType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.CodeDescriptionLevelType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.CodeLanguageType;
import un.unece.uncefact.data.standard.unqualifieddatatype._6.TextType;


/**
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UniqueID xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;AAES000001&lt;/ccts:UniqueID&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Acronym xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;ABIE&lt;/ccts:Acronym&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:DictionaryEntryName xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Archive&lt;/ccts:DictionaryEntryName&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Version xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;1.0&lt;/ccts:Version&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Definition xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Ensemble constitué d'un contenu d'information et de son information de pérennisation.&lt;/ccts:Definition&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:ObjectClassTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Archive&lt;/ccts:ObjectClassTerm&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:QualifierTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Archive&lt;/ccts:QualifierTerm&gt;
 * </pre>
 * 
 *                         
 * 
 * <p>Java class for ArchiveType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArchiveType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ArchivalAgencyArchiveIdentifier" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesIDType" minOccurs="0"/>
 *         &lt;element name="ArchivalAgreement" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesIDType" minOccurs="0"/>
 *         &lt;element name="ArchivalProfile" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesIDType" minOccurs="0"/>
 *         &lt;element name="DescriptionLanguage" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}CodeLanguageType" maxOccurs="unbounded"/>
 *         &lt;element name="DescriptionLevel" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}CodeDescriptionLevelType"/>
 *         &lt;element name="Name" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType"/>
 *         &lt;element name="ServiceLevel" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesCodeType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TransferringAgencyArchiveIdentifier" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesIDType" minOccurs="0"/>
 *         &lt;element name="ContentDescription" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}ContentDescriptionType"/>
 *         &lt;element name="Appraisal" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}AppraisalRulesType" minOccurs="0"/>
 *         &lt;element name="AccessRestriction" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}AccessRestrictionRulesType" minOccurs="0"/>
 *         &lt;element name="Document" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}DocumentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Contains" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}ArchiveObjectType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArchiveType", propOrder = {
    "archivalAgencyArchiveIdentifier",
    "archivalAgreement",
    "archivalProfile",
    "descriptionLanguage",
    "descriptionLevel",
    "name",
    "serviceLevel",
    "transferringAgencyArchiveIdentifier",
    "contentDescription",
    "appraisal",
    "accessRestriction",
    "document",
    "contains"
})
public class ArchiveType {

    @XmlElement(name = "ArchivalAgencyArchiveIdentifier")
    protected ArchivesIDType archivalAgencyArchiveIdentifier;
    @XmlElement(name = "ArchivalAgreement")
    protected ArchivesIDType archivalAgreement;
    @XmlElement(name = "ArchivalProfile")
    protected ArchivesIDType archivalProfile;
    @XmlElement(name = "DescriptionLanguage", required = true)
    protected List<CodeLanguageType> descriptionLanguage;
    @XmlElement(name = "DescriptionLevel", required = true)
    protected CodeDescriptionLevelType descriptionLevel;
    @XmlElement(name = "Name", required = true)
    protected TextType name;
    @XmlElement(name = "ServiceLevel")
    protected List<ArchivesCodeType> serviceLevel;
    @XmlElement(name = "TransferringAgencyArchiveIdentifier")
    protected ArchivesIDType transferringAgencyArchiveIdentifier;
    @XmlElement(name = "ContentDescription", required = true)
    protected ContentDescriptionType contentDescription;
    @XmlElement(name = "Appraisal")
    protected AppraisalRulesType appraisal;
    @XmlElement(name = "AccessRestriction")
    protected AccessRestrictionRulesType accessRestriction;
    @XmlElement(name = "Document")
    protected List<DocumentType> document;
    @XmlElement(name = "Contains")
    protected List<ArchiveObjectType> contains;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the archivalAgencyArchiveIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesIDType }
     *     
     */
    public ArchivesIDType getArchivalAgencyArchiveIdentifier() {
        return archivalAgencyArchiveIdentifier;
    }

    /**
     * Sets the value of the archivalAgencyArchiveIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesIDType }
     *     
     */
    public void setArchivalAgencyArchiveIdentifier(ArchivesIDType value) {
        this.archivalAgencyArchiveIdentifier = value;
    }

    /**
     * Gets the value of the archivalAgreement property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesIDType }
     *     
     */
    public ArchivesIDType getArchivalAgreement() {
        return archivalAgreement;
    }

    /**
     * Sets the value of the archivalAgreement property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesIDType }
     *     
     */
    public void setArchivalAgreement(ArchivesIDType value) {
        this.archivalAgreement = value;
    }

    /**
     * Gets the value of the archivalProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesIDType }
     *     
     */
    public ArchivesIDType getArchivalProfile() {
        return archivalProfile;
    }

    /**
     * Sets the value of the archivalProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesIDType }
     *     
     */
    public void setArchivalProfile(ArchivesIDType value) {
        this.archivalProfile = value;
    }

    /**
     * Gets the value of the descriptionLanguage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descriptionLanguage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescriptionLanguage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeLanguageType }
     * 
     * 
     */
    public List<CodeLanguageType> getDescriptionLanguage() {
        if (descriptionLanguage == null) {
            descriptionLanguage = new ArrayList<CodeLanguageType>();
        }
        return this.descriptionLanguage;
    }

    /**
     * Gets the value of the descriptionLevel property.
     * 
     * @return
     *     possible object is
     *     {@link CodeDescriptionLevelType }
     *     
     */
    public CodeDescriptionLevelType getDescriptionLevel() {
        return descriptionLevel;
    }

    /**
     * Sets the value of the descriptionLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeDescriptionLevelType }
     *     
     */
    public void setDescriptionLevel(CodeDescriptionLevelType value) {
        this.descriptionLevel = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setName(TextType value) {
        this.name = value;
    }

    /**
     * Gets the value of the serviceLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArchivesCodeType }
     * 
     * 
     */
    public List<ArchivesCodeType> getServiceLevel() {
        if (serviceLevel == null) {
            serviceLevel = new ArrayList<ArchivesCodeType>();
        }
        return this.serviceLevel;
    }

    /**
     * Gets the value of the transferringAgencyArchiveIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesIDType }
     *     
     */
    public ArchivesIDType getTransferringAgencyArchiveIdentifier() {
        return transferringAgencyArchiveIdentifier;
    }

    /**
     * Sets the value of the transferringAgencyArchiveIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesIDType }
     *     
     */
    public void setTransferringAgencyArchiveIdentifier(ArchivesIDType value) {
        this.transferringAgencyArchiveIdentifier = value;
    }

    /**
     * Gets the value of the contentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link ContentDescriptionType }
     *     
     */
    public ContentDescriptionType getContentDescription() {
        return contentDescription;
    }

    /**
     * Sets the value of the contentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentDescriptionType }
     *     
     */
    public void setContentDescription(ContentDescriptionType value) {
        this.contentDescription = value;
    }

    /**
     * Gets the value of the appraisal property.
     * 
     * @return
     *     possible object is
     *     {@link AppraisalRulesType }
     *     
     */
    public AppraisalRulesType getAppraisal() {
        return appraisal;
    }

    /**
     * Sets the value of the appraisal property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppraisalRulesType }
     *     
     */
    public void setAppraisal(AppraisalRulesType value) {
        this.appraisal = value;
    }

    /**
     * Gets the value of the accessRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link AccessRestrictionRulesType }
     *     
     */
    public AccessRestrictionRulesType getAccessRestriction() {
        return accessRestriction;
    }

    /**
     * Sets the value of the accessRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessRestrictionRulesType }
     *     
     */
    public void setAccessRestriction(AccessRestrictionRulesType value) {
        this.accessRestriction = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the document property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentType }
     * 
     * 
     */
    public List<DocumentType> getDocument() {
        if (document == null) {
            document = new ArrayList<DocumentType>();
        }
        return this.document;
    }

    /**
     * Gets the value of the contains property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contains property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContains().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArchiveObjectType }
     * 
     * 
     */
    public List<ArchiveObjectType> getContains() {
        if (contains == null) {
            contains = new ArrayList<ArchiveObjectType>();
        }
        return this.contains;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
