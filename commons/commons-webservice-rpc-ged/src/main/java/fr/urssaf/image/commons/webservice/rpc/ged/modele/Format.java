/**
 * Format.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;

@SuppressWarnings("all")
public class Format implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Format(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ALPHA = "ALPHA";
    public static final java.lang.String _NUMERIQUE = "NUMERIQUE";
    public static final java.lang.String _ALPHANUM = "ALPHANUM";
    public static final java.lang.String _DATE = "DATE";
    public static final java.lang.String _TEXTE = "TEXTE";
    public static final Format ALPHA = new Format(_ALPHA);
    public static final Format NUMERIQUE = new Format(_NUMERIQUE);
    public static final Format ALPHANUM = new Format(_ALPHANUM);
    public static final Format DATE = new Format(_DATE);
    public static final Format TEXTE = new Format(_TEXTE);
    public java.lang.String getValue() { return _value_;}
    public static Format fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        Format enumeration = (Format)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static Format fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Format.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.xsd1", "Format"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
