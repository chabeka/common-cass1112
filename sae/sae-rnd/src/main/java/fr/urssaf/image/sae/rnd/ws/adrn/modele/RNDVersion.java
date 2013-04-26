/**
 * RNDVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDVersion  implements java.io.Serializable {
    private int _id;

    private java.lang.String _label;

    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction[] _listeFonctions;

    private java.lang.String _date_debut;

    private java.lang.String _date_fin;

    private boolean _etat;

    public RNDVersion() {
    }

    public RNDVersion(
           int _id,
           java.lang.String _label,
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction[] _listeFonctions,
           java.lang.String _date_debut,
           java.lang.String _date_fin,
           boolean _etat) {
           this._id = _id;
           this._label = _label;
           this._listeFonctions = _listeFonctions;
           this._date_debut = _date_debut;
           this._date_fin = _date_fin;
           this._etat = _etat;
    }


    /**
     * Gets the _id value for this RNDVersion.
     * 
     * @return _id
     */
    public int get_id() {
        return _id;
    }


    /**
     * Sets the _id value for this RNDVersion.
     * 
     * @param _id
     */
    public void set_id(int _id) {
        this._id = _id;
    }


    /**
     * Gets the _label value for this RNDVersion.
     * 
     * @return _label
     */
    public java.lang.String get_label() {
        return _label;
    }


    /**
     * Sets the _label value for this RNDVersion.
     * 
     * @param _label
     */
    public void set_label(java.lang.String _label) {
        this._label = _label;
    }


    /**
     * Gets the _listeFonctions value for this RNDVersion.
     * 
     * @return _listeFonctions
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction[] get_listeFonctions() {
        return _listeFonctions;
    }


    /**
     * Sets the _listeFonctions value for this RNDVersion.
     * 
     * @param _listeFonctions
     */
    public void set_listeFonctions(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction[] _listeFonctions) {
        this._listeFonctions = _listeFonctions;
    }


    /**
     * Gets the _date_debut value for this RNDVersion.
     * 
     * @return _date_debut
     */
    public java.lang.String get_date_debut() {
        return _date_debut;
    }


    /**
     * Sets the _date_debut value for this RNDVersion.
     * 
     * @param _date_debut
     */
    public void set_date_debut(java.lang.String _date_debut) {
        this._date_debut = _date_debut;
    }


    /**
     * Gets the _date_fin value for this RNDVersion.
     * 
     * @return _date_fin
     */
    public java.lang.String get_date_fin() {
        return _date_fin;
    }


    /**
     * Sets the _date_fin value for this RNDVersion.
     * 
     * @param _date_fin
     */
    public void set_date_fin(java.lang.String _date_fin) {
        this._date_fin = _date_fin;
    }


    /**
     * Gets the _etat value for this RNDVersion.
     * 
     * @return _etat
     */
    public boolean is_etat() {
        return _etat;
    }


    /**
     * Sets the _etat value for this RNDVersion.
     * 
     * @param _etat
     */
    public void set_etat(boolean _etat) {
        this._etat = _etat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDVersion)) return false;
        RNDVersion other = (RNDVersion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this._id == other.get_id() &&
            ((this._label==null && other.get_label()==null) || 
             (this._label!=null &&
              this._label.equals(other.get_label()))) &&
            ((this._listeFonctions==null && other.get_listeFonctions()==null) || 
             (this._listeFonctions!=null &&
              java.util.Arrays.equals(this._listeFonctions, other.get_listeFonctions()))) &&
            ((this._date_debut==null && other.get_date_debut()==null) || 
             (this._date_debut!=null &&
              this._date_debut.equals(other.get_date_debut()))) &&
            ((this._date_fin==null && other.get_date_fin()==null) || 
             (this._date_fin!=null &&
              this._date_fin.equals(other.get_date_fin()))) &&
            this._etat == other.is_etat();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += get_id();
        if (get_label() != null) {
            _hashCode += get_label().hashCode();
        }
        if (get_listeFonctions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_listeFonctions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_listeFonctions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (get_date_debut() != null) {
            _hashCode += get_date_debut().hashCode();
        }
        if (get_date_fin() != null) {
            _hashCode += get_date_fin().hashCode();
        }
        _hashCode += (is_etat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RNDVersion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDVersion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_listeFonctions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_listeFonctions"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDFonction"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_date_debut");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_date_debut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_date_fin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_date_fin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_etat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_etat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
