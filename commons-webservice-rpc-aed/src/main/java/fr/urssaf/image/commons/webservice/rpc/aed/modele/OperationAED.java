/**
 * OperationAED.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Operation réalisées lors des traitements en GED pour entree en
 * GED nouvelle formule
 */
public class OperationAED  implements java.io.Serializable {
    private java.lang.String date;

    private java.lang.String heure;

    private java.lang.String programme;

    private java.lang.String nature;

    private java.lang.String utilisateur;

    private java.lang.String typeHash;

    private java.lang.String hash;

    private java.lang.String[] details;

    public OperationAED() {
    }

    public OperationAED(
           java.lang.String date,
           java.lang.String heure,
           java.lang.String programme,
           java.lang.String nature,
           java.lang.String utilisateur,
           java.lang.String typeHash,
           java.lang.String hash,
           java.lang.String[] details) {
           this.date = date;
           this.heure = heure;
           this.programme = programme;
           this.nature = nature;
           this.utilisateur = utilisateur;
           this.typeHash = typeHash;
           this.hash = hash;
           this.details = details;
    }


    /**
     * Gets the date value for this OperationAED.
     * 
     * @return date
     */
    public java.lang.String getDate() {
        return date;
    }


    /**
     * Sets the date value for this OperationAED.
     * 
     * @param date
     */
    public void setDate(java.lang.String date) {
        this.date = date;
    }


    /**
     * Gets the heure value for this OperationAED.
     * 
     * @return heure
     */
    public java.lang.String getHeure() {
        return heure;
    }


    /**
     * Sets the heure value for this OperationAED.
     * 
     * @param heure
     */
    public void setHeure(java.lang.String heure) {
        this.heure = heure;
    }


    /**
     * Gets the programme value for this OperationAED.
     * 
     * @return programme
     */
    public java.lang.String getProgramme() {
        return programme;
    }


    /**
     * Sets the programme value for this OperationAED.
     * 
     * @param programme
     */
    public void setProgramme(java.lang.String programme) {
        this.programme = programme;
    }


    /**
     * Gets the nature value for this OperationAED.
     * 
     * @return nature
     */
    public java.lang.String getNature() {
        return nature;
    }


    /**
     * Sets the nature value for this OperationAED.
     * 
     * @param nature
     */
    public void setNature(java.lang.String nature) {
        this.nature = nature;
    }


    /**
     * Gets the utilisateur value for this OperationAED.
     * 
     * @return utilisateur
     */
    public java.lang.String getUtilisateur() {
        return utilisateur;
    }


    /**
     * Sets the utilisateur value for this OperationAED.
     * 
     * @param utilisateur
     */
    public void setUtilisateur(java.lang.String utilisateur) {
        this.utilisateur = utilisateur;
    }


    /**
     * Gets the typeHash value for this OperationAED.
     * 
     * @return typeHash
     */
    public java.lang.String getTypeHash() {
        return typeHash;
    }


    /**
     * Sets the typeHash value for this OperationAED.
     * 
     * @param typeHash
     */
    public void setTypeHash(java.lang.String typeHash) {
        this.typeHash = typeHash;
    }


    /**
     * Gets the hash value for this OperationAED.
     * 
     * @return hash
     */
    public java.lang.String getHash() {
        return hash;
    }


    /**
     * Sets the hash value for this OperationAED.
     * 
     * @param hash
     */
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }


    /**
     * Gets the details value for this OperationAED.
     * 
     * @return details
     */
    public java.lang.String[] getDetails() {
        return details;
    }


    /**
     * Sets the details value for this OperationAED.
     * 
     * @param details
     */
    public void setDetails(java.lang.String[] details) {
        this.details = details;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OperationAED)) return false;
        OperationAED other = (OperationAED) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.heure==null && other.getHeure()==null) || 
             (this.heure!=null &&
              this.heure.equals(other.getHeure()))) &&
            ((this.programme==null && other.getProgramme()==null) || 
             (this.programme!=null &&
              this.programme.equals(other.getProgramme()))) &&
            ((this.nature==null && other.getNature()==null) || 
             (this.nature!=null &&
              this.nature.equals(other.getNature()))) &&
            ((this.utilisateur==null && other.getUtilisateur()==null) || 
             (this.utilisateur!=null &&
              this.utilisateur.equals(other.getUtilisateur()))) &&
            ((this.typeHash==null && other.getTypeHash()==null) || 
             (this.typeHash!=null &&
              this.typeHash.equals(other.getTypeHash()))) &&
            ((this.hash==null && other.getHash()==null) || 
             (this.hash!=null &&
              this.hash.equals(other.getHash()))) &&
            ((this.details==null && other.getDetails()==null) || 
             (this.details!=null &&
              java.util.Arrays.equals(this.details, other.getDetails())));
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
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getHeure() != null) {
            _hashCode += getHeure().hashCode();
        }
        if (getProgramme() != null) {
            _hashCode += getProgramme().hashCode();
        }
        if (getNature() != null) {
            _hashCode += getNature().hashCode();
        }
        if (getUtilisateur() != null) {
            _hashCode += getUtilisateur().hashCode();
        }
        if (getTypeHash() != null) {
            _hashCode += getTypeHash().hashCode();
        }
        if (getHash() != null) {
            _hashCode += getHash().hashCode();
        }
        if (getDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetails(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OperationAED.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "OperationAED"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("heure");
        elemField.setXmlName(new javax.xml.namespace.QName("", "heure"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("programme");
        elemField.setXmlName(new javax.xml.namespace.QName("", "programme"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("utilisateur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "utilisateur"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeHash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeHash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("details");
        elemField.setXmlName(new javax.xml.namespace.QName("", "details"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
