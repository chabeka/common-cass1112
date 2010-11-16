//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.16 at 01:12:24 PM CET 
//


package fr.gouv.ae.archive.draft.standard_echange_v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesIDType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.CodeKeywordType;
import un.unece.uncefact.data.standard.unqualifieddatatype._6.TextType;


/**
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UniqueID xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;AAES000005&lt;/ccts:UniqueID&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Acronym xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;ABIE&lt;/ccts:Acronym&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:DictionaryEntryName xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Keyword&lt;/ccts:DictionaryEntryName&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Version xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;1.0&lt;/ccts:Version&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Definition xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Mot-cl� associ� � une archive ou un objet d'archive.&lt;/ccts:Definition&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:ObjectClassTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Keyword&lt;/ccts:ObjectClassTerm&gt;
 * </pre>
 * 
 *                                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:QualifierTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Keyword&lt;/ccts:QualifierTerm&gt;
 * </pre>
 * 
 *                         
 * 
 * <p>Java class for KeywordType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KeywordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccessRestriction" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2}AccessRestrictionRulesType" minOccurs="0"/>
 *         &lt;element name="KeywordContent" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType"/>
 *         &lt;element name="KeywordReference" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesIDType" minOccurs="0"/>
 *         &lt;element name="KeywordType" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}CodeKeywordType" minOccurs="0"/>
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
@XmlType(name = "KeywordType", propOrder = {
    "accessRestriction",
    "keywordContent",
    "keywordReference",
    "keywordType"
})
public class KeywordType {

    @XmlElement(name = "AccessRestriction")
    protected AccessRestrictionRulesType accessRestriction;
    @XmlElement(name = "KeywordContent", required = true)
    protected TextType keywordContent;
    @XmlElement(name = "KeywordReference")
    protected ArchivesIDType keywordReference;
    @XmlElement(name = "KeywordType")
    protected CodeKeywordType keywordType;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

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
     * Gets the value of the keywordContent property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getKeywordContent() {
        return keywordContent;
    }

    /**
     * Sets the value of the keywordContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setKeywordContent(TextType value) {
        this.keywordContent = value;
    }

    /**
     * Gets the value of the keywordReference property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesIDType }
     *     
     */
    public ArchivesIDType getKeywordReference() {
        return keywordReference;
    }

    /**
     * Sets the value of the keywordReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesIDType }
     *     
     */
    public void setKeywordReference(ArchivesIDType value) {
        this.keywordReference = value;
    }

    /**
     * Gets the value of the keywordType property.
     * 
     * @return
     *     possible object is
     *     {@link CodeKeywordType }
     *     
     */
    public CodeKeywordType getKeywordType() {
        return keywordType;
    }

    /**
     * Sets the value of the keywordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeKeywordType }
     *     
     */
    public void setKeywordType(CodeKeywordType value) {
        this.keywordType = value;
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
