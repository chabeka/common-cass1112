/**
 * TransCodeTemporaire.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class TransCodeTemporaire  implements java.io.Serializable {
    private java.lang.String referenceCodeTemporaire;

    private java.lang.String labelCodeTemporaire;

    private java.lang.String commentCodeTemporaire;

    public TransCodeTemporaire() {
    }

    public TransCodeTemporaire(
           java.lang.String referenceCodeTemporaire,
           java.lang.String labelCodeTemporaire,
           java.lang.String commentCodeTemporaire) {
           this.referenceCodeTemporaire = referenceCodeTemporaire;
           this.labelCodeTemporaire = labelCodeTemporaire;
           this.commentCodeTemporaire = commentCodeTemporaire;
    }


    /**
     * Gets the referenceCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @return referenceCodeTemporaire
     */
    public java.lang.String getReferenceCodeTemporaire() {
        return referenceCodeTemporaire;
    }


    /**
     * Sets the referenceCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @param referenceCodeTemporaire
     */
    public void setReferenceCodeTemporaire(java.lang.String referenceCodeTemporaire) {
        this.referenceCodeTemporaire = referenceCodeTemporaire;
    }


    /**
     * Gets the labelCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @return labelCodeTemporaire
     */
    public java.lang.String getLabelCodeTemporaire() {
        return labelCodeTemporaire;
    }


    /**
     * Sets the labelCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @param labelCodeTemporaire
     */
    public void setLabelCodeTemporaire(java.lang.String labelCodeTemporaire) {
        this.labelCodeTemporaire = labelCodeTemporaire;
    }


    /**
     * Gets the commentCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @return commentCodeTemporaire
     */
    public java.lang.String getCommentCodeTemporaire() {
        return commentCodeTemporaire;
    }


    /**
     * Sets the commentCodeTemporaire value for this TransCodeTemporaire.
     * 
     * @param commentCodeTemporaire
     */
    public void setCommentCodeTemporaire(java.lang.String commentCodeTemporaire) {
        this.commentCodeTemporaire = commentCodeTemporaire;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransCodeTemporaire)) return false;
        TransCodeTemporaire other = (TransCodeTemporaire) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referenceCodeTemporaire==null && other.getReferenceCodeTemporaire()==null) || 
             (this.referenceCodeTemporaire!=null &&
              this.referenceCodeTemporaire.equals(other.getReferenceCodeTemporaire()))) &&
            ((this.labelCodeTemporaire==null && other.getLabelCodeTemporaire()==null) || 
             (this.labelCodeTemporaire!=null &&
              this.labelCodeTemporaire.equals(other.getLabelCodeTemporaire()))) &&
            ((this.commentCodeTemporaire==null && other.getCommentCodeTemporaire()==null) || 
             (this.commentCodeTemporaire!=null &&
              this.commentCodeTemporaire.equals(other.getCommentCodeTemporaire())));
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
        if (getReferenceCodeTemporaire() != null) {
            _hashCode += getReferenceCodeTemporaire().hashCode();
        }
        if (getLabelCodeTemporaire() != null) {
            _hashCode += getLabelCodeTemporaire().hashCode();
        }
        if (getCommentCodeTemporaire() != null) {
            _hashCode += getCommentCodeTemporaire().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransCodeTemporaire.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "TransCodeTemporaire"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceCodeTemporaire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referenceCodeTemporaire"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("labelCodeTemporaire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "labelCodeTemporaire"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commentCodeTemporaire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "commentCodeTemporaire"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
