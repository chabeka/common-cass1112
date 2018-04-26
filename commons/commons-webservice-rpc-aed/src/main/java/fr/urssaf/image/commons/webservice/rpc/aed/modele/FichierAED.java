/**
 * FichierAED.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Fichier pour entree en GED nouvelle formule
 */
public class FichierAED  implements java.io.Serializable {
    private java.lang.String nom;

    private java.lang.String type;

    private java.lang.String xsl;

    private java.lang.String chemin;

    private java.lang.String typeHash;

    private java.lang.String hash;

    private java.lang.String contenu;

    private java.lang.String typeSignature;

    private java.lang.String blocSignature;

    private java.lang.String blocHorodatage;

    public FichierAED() {
    }

    public FichierAED(
           java.lang.String nom,
           java.lang.String type,
           java.lang.String xsl,
           java.lang.String chemin,
           java.lang.String typeHash,
           java.lang.String hash,
           java.lang.String contenu,
           java.lang.String typeSignature,
           java.lang.String blocSignature,
           java.lang.String blocHorodatage) {
           this.nom = nom;
           this.type = type;
           this.xsl = xsl;
           this.chemin = chemin;
           this.typeHash = typeHash;
           this.hash = hash;
           this.contenu = contenu;
           this.typeSignature = typeSignature;
           this.blocSignature = blocSignature;
           this.blocHorodatage = blocHorodatage;
    }


    /**
     * Gets the nom value for this FichierAED.
     * 
     * @return nom
     */
    public java.lang.String getNom() {
        return nom;
    }


    /**
     * Sets the nom value for this FichierAED.
     * 
     * @param nom
     */
    public void setNom(java.lang.String nom) {
        this.nom = nom;
    }


    /**
     * Gets the type value for this FichierAED.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this FichierAED.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the xsl value for this FichierAED.
     * 
     * @return xsl
     */
    public java.lang.String getXsl() {
        return xsl;
    }


    /**
     * Sets the xsl value for this FichierAED.
     * 
     * @param xsl
     */
    public void setXsl(java.lang.String xsl) {
        this.xsl = xsl;
    }


    /**
     * Gets the chemin value for this FichierAED.
     * 
     * @return chemin
     */
    public java.lang.String getChemin() {
        return chemin;
    }


    /**
     * Sets the chemin value for this FichierAED.
     * 
     * @param chemin
     */
    public void setChemin(java.lang.String chemin) {
        this.chemin = chemin;
    }


    /**
     * Gets the typeHash value for this FichierAED.
     * 
     * @return typeHash
     */
    public java.lang.String getTypeHash() {
        return typeHash;
    }


    /**
     * Sets the typeHash value for this FichierAED.
     * 
     * @param typeHash
     */
    public void setTypeHash(java.lang.String typeHash) {
        this.typeHash = typeHash;
    }


    /**
     * Gets the hash value for this FichierAED.
     * 
     * @return hash
     */
    public java.lang.String getHash() {
        return hash;
    }


    /**
     * Sets the hash value for this FichierAED.
     * 
     * @param hash
     */
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }


    /**
     * Gets the contenu value for this FichierAED.
     * 
     * @return contenu
     */
    public java.lang.String getContenu() {
        return contenu;
    }


    /**
     * Sets the contenu value for this FichierAED.
     * 
     * @param contenu
     */
    public void setContenu(java.lang.String contenu) {
        this.contenu = contenu;
    }


    /**
     * Gets the typeSignature value for this FichierAED.
     * 
     * @return typeSignature
     */
    public java.lang.String getTypeSignature() {
        return typeSignature;
    }


    /**
     * Sets the typeSignature value for this FichierAED.
     * 
     * @param typeSignature
     */
    public void setTypeSignature(java.lang.String typeSignature) {
        this.typeSignature = typeSignature;
    }


    /**
     * Gets the blocSignature value for this FichierAED.
     * 
     * @return blocSignature
     */
    public java.lang.String getBlocSignature() {
        return blocSignature;
    }


    /**
     * Sets the blocSignature value for this FichierAED.
     * 
     * @param blocSignature
     */
    public void setBlocSignature(java.lang.String blocSignature) {
        this.blocSignature = blocSignature;
    }


    /**
     * Gets the blocHorodatage value for this FichierAED.
     * 
     * @return blocHorodatage
     */
    public java.lang.String getBlocHorodatage() {
        return blocHorodatage;
    }


    /**
     * Sets the blocHorodatage value for this FichierAED.
     * 
     * @param blocHorodatage
     */
    public void setBlocHorodatage(java.lang.String blocHorodatage) {
        this.blocHorodatage = blocHorodatage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FichierAED)) return false;
        FichierAED other = (FichierAED) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nom==null && other.getNom()==null) || 
             (this.nom!=null &&
              this.nom.equals(other.getNom()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.xsl==null && other.getXsl()==null) || 
             (this.xsl!=null &&
              this.xsl.equals(other.getXsl()))) &&
            ((this.chemin==null && other.getChemin()==null) || 
             (this.chemin!=null &&
              this.chemin.equals(other.getChemin()))) &&
            ((this.typeHash==null && other.getTypeHash()==null) || 
             (this.typeHash!=null &&
              this.typeHash.equals(other.getTypeHash()))) &&
            ((this.hash==null && other.getHash()==null) || 
             (this.hash!=null &&
              this.hash.equals(other.getHash()))) &&
            ((this.contenu==null && other.getContenu()==null) || 
             (this.contenu!=null &&
              this.contenu.equals(other.getContenu()))) &&
            ((this.typeSignature==null && other.getTypeSignature()==null) || 
             (this.typeSignature!=null &&
              this.typeSignature.equals(other.getTypeSignature()))) &&
            ((this.blocSignature==null && other.getBlocSignature()==null) || 
             (this.blocSignature!=null &&
              this.blocSignature.equals(other.getBlocSignature()))) &&
            ((this.blocHorodatage==null && other.getBlocHorodatage()==null) || 
             (this.blocHorodatage!=null &&
              this.blocHorodatage.equals(other.getBlocHorodatage())));
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
        if (getNom() != null) {
            _hashCode += getNom().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getXsl() != null) {
            _hashCode += getXsl().hashCode();
        }
        if (getChemin() != null) {
            _hashCode += getChemin().hashCode();
        }
        if (getTypeHash() != null) {
            _hashCode += getTypeHash().hashCode();
        }
        if (getHash() != null) {
            _hashCode += getHash().hashCode();
        }
        if (getContenu() != null) {
            _hashCode += getContenu().hashCode();
        }
        if (getTypeSignature() != null) {
            _hashCode += getTypeSignature().hashCode();
        }
        if (getBlocSignature() != null) {
            _hashCode += getBlocSignature().hashCode();
        }
        if (getBlocHorodatage() != null) {
            _hashCode += getBlocHorodatage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FichierAED.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "FichierAED"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xsl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xsl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chemin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "chemin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
        elemField.setFieldName("contenu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contenu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeSignature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeSignature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blocSignature");
        elemField.setXmlName(new javax.xml.namespace.QName("", "blocSignature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blocHorodatage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "blocHorodatage"));
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
