//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.16 at 01:12:24 PM CET 
//


package un.unece.uncefact.codelist.draft.daf.keywordtypecode._2009_08_18;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KeywordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="KeywordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="corpname"/>
 *     &lt;enumeration value="famname"/>
 *     &lt;enumeration value="geogname"/>
 *     &lt;enumeration value="name"/>
 *     &lt;enumeration value="occupation"/>
 *     &lt;enumeration value="persname"/>
 *     &lt;enumeration value="subject"/>
 *     &lt;enumeration value="genreform"/>
 *     &lt;enumeration value="function"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "KeywordType", namespace = "urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18")
@XmlEnum
public enum KeywordType {


    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Collectivité&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("corpname")
    CORPNAME("corpname"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Nom de famille&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("famname")
    FAMNAME("famname"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Nom géographique&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("geogname")
    GEOGNAME("geogname"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Nom&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("name")
    NAME("name"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Fonction&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("occupation")
    OCCUPATION("occupation"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Nom de personne&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("persname")
    PERSNAME("persname"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Mot-matière&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("subject")
    SUBJECT("subject"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Type de document&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("genreform")
    GENREFORM("genreform"),

    /**
     * 
     * <pre>
     * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Name xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="urn:un:unece:uncefact:codelist:draft:daf:keywordTypeCode:2009-08-18" xmlns:clmDAFkeywordTypeCode="urn:un:unece:uncefact:codelist:draft:DAF:keywordTypeCode:2009-08-18" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Activité&lt;/ccts:Name&gt;
     * </pre>
     * 
     * 
     */
    @XmlEnumValue("function")
    FUNCTION("function");
    private final String value;

    KeywordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KeywordType fromValue(String v) {
        for (KeywordType c: KeywordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
