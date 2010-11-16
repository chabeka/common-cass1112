//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.16 at 01:12:24 PM CET 
//


package fr.gouv.ae.archive.draft.standard_echange_v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesCodeType;
import fr.gouv.ae.archive.draft.standard_echange_v0_2.qualifieddatatype._1.ArchivesCountryType;
import un.unece.uncefact.data.standard.unqualifieddatatype._6.TextType;


/**
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:UniqueID xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;OCAAES000002&lt;/ccts:UniqueID&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Acronym xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;ABIE&lt;/ccts:Acronym&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:DictionaryEntryName xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Address&lt;/ccts:DictionaryEntryName&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Version xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;1.0&lt;/ccts:Version&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:Definition xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Lieu o� une organisation ou une personne peuvent �tre jointes ou trouv�es.&lt;/ccts:Definition&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:ObjectClassTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Address&lt;/ccts:ObjectClassTerm&gt;
 * </pre>
 * 
 * 				
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;ccts:QualifierTerm xmlns:ccts="urn:un:unece:uncefact:documentation:standard:CoreComponentsTechnicalSpecification:2" xmlns="fr:gouv:ae:archive:draft:standard_echange_v0.2" xmlns:qdt="fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;Address&lt;/ccts:QualifierTerm&gt;
 * </pre>
 * 
 * 			
 * 
 * <p>Java class for AddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BlockName" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="BuildingName" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="BuildingNumber" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="CityName" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="CitySub-DivisionName" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="Country" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesCountryType" minOccurs="0"/>
 *         &lt;element name="FloorIdentification" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="Postcode" type="{fr:gouv:ae:archive:draft:standard_echange_v0.2:QualifiedDataType:1}ArchivesCodeType" minOccurs="0"/>
 *         &lt;element name="PostOfficeBox" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="RoomIdentification" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *         &lt;element name="StreetName" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6}TextType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "blockName",
    "buildingName",
    "buildingNumber",
    "cityName",
    "citySubDivisionName",
    "country",
    "floorIdentification",
    "postcode",
    "postOfficeBox",
    "roomIdentification",
    "streetName"
})
public class AddressType {

    @XmlElement(name = "BlockName")
    protected TextType blockName;
    @XmlElement(name = "BuildingName")
    protected TextType buildingName;
    @XmlElement(name = "BuildingNumber")
    protected TextType buildingNumber;
    @XmlElement(name = "CityName")
    protected TextType cityName;
    @XmlElement(name = "CitySub-DivisionName")
    protected TextType citySubDivisionName;
    @XmlElement(name = "Country")
    protected ArchivesCountryType country;
    @XmlElement(name = "FloorIdentification")
    protected TextType floorIdentification;
    @XmlElement(name = "Postcode")
    protected ArchivesCodeType postcode;
    @XmlElement(name = "PostOfficeBox")
    protected TextType postOfficeBox;
    @XmlElement(name = "RoomIdentification")
    protected TextType roomIdentification;
    @XmlElement(name = "StreetName")
    protected TextType streetName;

    /**
     * Gets the value of the blockName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getBlockName() {
        return blockName;
    }

    /**
     * Sets the value of the blockName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setBlockName(TextType value) {
        this.blockName = value;
    }

    /**
     * Gets the value of the buildingName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getBuildingName() {
        return buildingName;
    }

    /**
     * Sets the value of the buildingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setBuildingName(TextType value) {
        this.buildingName = value;
    }

    /**
     * Gets the value of the buildingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getBuildingNumber() {
        return buildingNumber;
    }

    /**
     * Sets the value of the buildingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setBuildingNumber(TextType value) {
        this.buildingNumber = value;
    }

    /**
     * Gets the value of the cityName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getCityName() {
        return cityName;
    }

    /**
     * Sets the value of the cityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setCityName(TextType value) {
        this.cityName = value;
    }

    /**
     * Gets the value of the citySubDivisionName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getCitySubDivisionName() {
        return citySubDivisionName;
    }

    /**
     * Sets the value of the citySubDivisionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setCitySubDivisionName(TextType value) {
        this.citySubDivisionName = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesCountryType }
     *     
     */
    public ArchivesCountryType getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesCountryType }
     *     
     */
    public void setCountry(ArchivesCountryType value) {
        this.country = value;
    }

    /**
     * Gets the value of the floorIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getFloorIdentification() {
        return floorIdentification;
    }

    /**
     * Sets the value of the floorIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setFloorIdentification(TextType value) {
        this.floorIdentification = value;
    }

    /**
     * Gets the value of the postcode property.
     * 
     * @return
     *     possible object is
     *     {@link ArchivesCodeType }
     *     
     */
    public ArchivesCodeType getPostcode() {
        return postcode;
    }

    /**
     * Sets the value of the postcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArchivesCodeType }
     *     
     */
    public void setPostcode(ArchivesCodeType value) {
        this.postcode = value;
    }

    /**
     * Gets the value of the postOfficeBox property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * Sets the value of the postOfficeBox property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setPostOfficeBox(TextType value) {
        this.postOfficeBox = value;
    }

    /**
     * Gets the value of the roomIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getRoomIdentification() {
        return roomIdentification;
    }

    /**
     * Sets the value of the roomIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setRoomIdentification(TextType value) {
        this.roomIdentification = value;
    }

    /**
     * Gets the value of the streetName property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getStreetName() {
        return streetName;
    }

    /**
     * Sets the value of the streetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setStreetName(TextType value) {
        this.streetName = value;
    }

}
