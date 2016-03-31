/**
 * RNDCorrespondance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDCorrespondance  implements java.io.Serializable {
    private java.lang.String _ctReference;

    private int _id;

    private java.lang.String _tdReference;

    private int _veID;

    public RNDCorrespondance() {
    }

    public RNDCorrespondance(
           java.lang.String _ctReference,
           int _id,
           java.lang.String _tdReference,
           int _veID) {
           this._ctReference = _ctReference;
           this._id = _id;
           this._tdReference = _tdReference;
           this._veID = _veID;
    }


    /**
     * Gets the _ctReference value for this RNDCorrespondance.
     * 
     * @return _ctReference
     */
    public java.lang.String get_ctReference() {
        return _ctReference;
    }


    /**
     * Sets the _ctReference value for this RNDCorrespondance.
     * 
     * @param _ctReference
     */
    public void set_ctReference(java.lang.String _ctReference) {
        this._ctReference = _ctReference;
    }


    /**
     * Gets the _id value for this RNDCorrespondance.
     * 
     * @return _id
     */
    public int get_id() {
        return _id;
    }


    /**
     * Sets the _id value for this RNDCorrespondance.
     * 
     * @param _id
     */
    public void set_id(int _id) {
        this._id = _id;
    }


    /**
     * Gets the _tdReference value for this RNDCorrespondance.
     * 
     * @return _tdReference
     */
    public java.lang.String get_tdReference() {
        return _tdReference;
    }


    /**
     * Sets the _tdReference value for this RNDCorrespondance.
     * 
     * @param _tdReference
     */
    public void set_tdReference(java.lang.String _tdReference) {
        this._tdReference = _tdReference;
    }


    /**
     * Gets the _veID value for this RNDCorrespondance.
     * 
     * @return _veID
     */
    public int get_veID() {
        return _veID;
    }


    /**
     * Sets the _veID value for this RNDCorrespondance.
     * 
     * @param _veID
     */
    public void set_veID(int _veID) {
        this._veID = _veID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDCorrespondance)) return false;
        RNDCorrespondance other = (RNDCorrespondance) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._ctReference==null && other.get_ctReference()==null) || 
             (this._ctReference!=null &&
              this._ctReference.equals(other.get_ctReference()))) &&
            this._id == other.get_id() &&
            ((this._tdReference==null && other.get_tdReference()==null) || 
             (this._tdReference!=null &&
              this._tdReference.equals(other.get_tdReference()))) &&
            this._veID == other.get_veID();
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
        if (get_ctReference() != null) {
            _hashCode += get_ctReference().hashCode();
        }
        _hashCode += get_id();
        if (get_tdReference() != null) {
            _hashCode += get_tdReference().hashCode();
        }
        _hashCode += get_veID();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RNDCorrespondance.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDCorrespondance"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_ctReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_ctReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_tdReference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_tdReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_veID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_veID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
