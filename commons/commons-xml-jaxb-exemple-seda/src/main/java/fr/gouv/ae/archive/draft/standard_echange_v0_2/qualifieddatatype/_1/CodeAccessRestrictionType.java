//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.19 at 05:19:14 PM CET 
//


package fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import un.unece.uncefact.codelist.draft.daf.accessrestrictioncode._2009_08_18.AccessRestrictionCodeType;


/**
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UniqueID xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;QDTAES000005&lt;/ccts:UniqueID&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Acronym xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;QDT&lt;/ccts:Acronym&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:DictionaryEntryName xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Code. Type&lt;/ccts:DictionaryEntryName&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Version xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;1.0&lt;/ccts:Version&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Definition xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Code correspondant au niveau de restriction d'access (source: loi archive).&lt;/ccts:Definition&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:PrimitiveType xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns:clm60133="urn:un:unece:uncefact:codelist:standard:6:0133:40106" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:clmDAFDescriptionLevelCode="urn:un:unece:uncefact:codelist:draft:DAF:descriptionLevelCode:2009-08-18" xmlns:clmDAFDocumentTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:documentTypeCode:2009-08-18" xmlns:clmDAFFileTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:fileTypeCode:2009-08-18" xmlns:clmDAFLanguageCode="urn:un:unece:uncefact:codelist:draft:DAF:languageCode:2009-08-18" xmlns:clmDAFReplyCode="urn:un:unece:uncefact:codelist:draft:DAF:replyCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:clmIANACharacterSetCode="urn:un:unece:uncefact:codelist:standard:IANA:CharacterSetCode:2007-05-14" xmlns:clmIANAMIMEMediaType="urn:un:unece:uncefact:codelist:standard:IANA:MIMEMediaType:2008-11-12" xmlns:ids5ISO316612A="urn:un:unece:uncefact:identifierlist:standard:5:ISO316612A:SecondEdition2006VI-3" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;string&lt;/ccts:PrimitiveType&gt;
 * </pre>
 * 
 * 			
 * 
 * <p>Java class for CodeAccessRestrictionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CodeAccessRestrictionType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18>AccessRestrictionCodeType">
 *       &lt;attribute name="listVersionID" use="required" type="{http://www.w3.org/2001/XMLSchema}token" fixed="edition 2009" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeAccessRestrictionType", propOrder = {
    "value"
})
public class CodeAccessRestrictionType {

    @XmlValue
    protected AccessRestrictionCodeType value;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String listVersionID;

    /**
     * 
     * 			      
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:accessRestrictionCode:2009-08-18" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Table des codes pour la restriction d'acc�s&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 			      
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Description xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:accessRestrictionCode:2009-08-18" xmlns:clmDAFAccessRestrictionCode="urn:un:unece:uncefact:codelist:draft:DAF:accessRestrictionCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Les diff�rentes valeurs de cette table sont tir�es de la loi archive.&lt;/ccts:Description&gt;
     * </pre>
     * 
     * 		      
     * 
     * @return
     *     possible object is
     *     {@link AccessRestrictionCodeType }
     *     
     */
    public AccessRestrictionCodeType getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessRestrictionCodeType }
     *     
     */
    public void setValue(AccessRestrictionCodeType value) {
        this.value = value;
    }

    /**
     * Gets the value of the listVersionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListVersionID() {
        if (listVersionID == null) {
            return "edition 2009";
        } else {
            return listVersionID;
        }
    }

    /**
     * Sets the value of the listVersionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListVersionID(String value) {
        this.listVersionID = value;
    }

}
