//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.19 at 05:19:14 PM CET 
//


package un.unece.uncefact.codelist.draft.daf.appraisalcode._2009_08_18;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AppraisalCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AppraisalCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="conserver"/>
 *     &lt;enumeration value="detruire"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AppraisalCodeType", namespace = "urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18")
@XmlEnum
public enum AppraisalCodeType {


    /**
     * 
     * 					
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Conserver&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 				
     * 
     */
    @XmlEnumValue("conserver")
    CONSERVER("conserver"),

    /**
     * 
     * 					
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:accessRestrictionCode:2009-08-18" xmlns:clmDAFAppraisalCode="urn:un:unece:uncefact:codelist:draft:DAF:appraisalCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;D�truire&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 				
     * 
     */
    @XmlEnumValue("detruire")
    DETRUIRE("detruire");
    private final String value;

    AppraisalCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AppraisalCodeType fromValue(String v) {
        for (AppraisalCodeType c: AppraisalCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
