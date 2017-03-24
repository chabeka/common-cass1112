/**
 * Document.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Element contenant les informations caracterisant un document
 */
public class Document  implements java.io.Serializable {
    private java.lang.String idDocument;

    private java.lang.String nomFichier;

    private java.lang.String idArchivage;

    private java.lang.String etat;

    private java.lang.String codeOrganisme;

    public Document() {
    }

    public Document(
           java.lang.String idDocument,
           java.lang.String nomFichier,
           java.lang.String idArchivage,
           java.lang.String etat,
           java.lang.String codeOrganisme) {
           this.idDocument = idDocument;
           this.nomFichier = nomFichier;
           this.idArchivage = idArchivage;
           this.etat = etat;
           this.codeOrganisme = codeOrganisme;
    }


    /**
     * Gets the idDocument value for this Document.
     * 
     * @return idDocument
     */
    public java.lang.String getIdDocument() {
        return idDocument;
    }


    /**
     * Sets the idDocument value for this Document.
     * 
     * @param idDocument
     */
    public void setIdDocument(java.lang.String idDocument) {
        this.idDocument = idDocument;
    }


    /**
     * Gets the nomFichier value for this Document.
     * 
     * @return nomFichier
     */
    public java.lang.String getNomFichier() {
        return nomFichier;
    }


    /**
     * Sets the nomFichier value for this Document.
     * 
     * @param nomFichier
     */
    public void setNomFichier(java.lang.String nomFichier) {
        this.nomFichier = nomFichier;
    }


    /**
     * Gets the idArchivage value for this Document.
     * 
     * @return idArchivage
     */
    public java.lang.String getIdArchivage() {
        return idArchivage;
    }


    /**
     * Sets the idArchivage value for this Document.
     * 
     * @param idArchivage
     */
    public void setIdArchivage(java.lang.String idArchivage) {
        this.idArchivage = idArchivage;
    }


    /**
     * Gets the etat value for this Document.
     * 
     * @return etat
     */
    public java.lang.String getEtat() {
        return etat;
    }


    /**
     * Sets the etat value for this Document.
     * 
     * @param etat
     */
    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }


    /**
     * Gets the codeOrganisme value for this Document.
     * 
     * @return codeOrganisme
     */
    public java.lang.String getCodeOrganisme() {
        return codeOrganisme;
    }


    /**
     * Sets the codeOrganisme value for this Document.
     * 
     * @param codeOrganisme
     */
    public void setCodeOrganisme(java.lang.String codeOrganisme) {
        this.codeOrganisme = codeOrganisme;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Document)) return false;
        Document other = (Document) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idDocument==null && other.getIdDocument()==null) || 
             (this.idDocument!=null &&
              this.idDocument.equals(other.getIdDocument()))) &&
            ((this.nomFichier==null && other.getNomFichier()==null) || 
             (this.nomFichier!=null &&
              this.nomFichier.equals(other.getNomFichier()))) &&
            ((this.idArchivage==null && other.getIdArchivage()==null) || 
             (this.idArchivage!=null &&
              this.idArchivage.equals(other.getIdArchivage()))) &&
            ((this.etat==null && other.getEtat()==null) || 
             (this.etat!=null &&
              this.etat.equals(other.getEtat()))) &&
            ((this.codeOrganisme==null && other.getCodeOrganisme()==null) || 
             (this.codeOrganisme!=null &&
              this.codeOrganisme.equals(other.getCodeOrganisme())));
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
        if (getIdDocument() != null) {
            _hashCode += getIdDocument().hashCode();
        }
        if (getNomFichier() != null) {
            _hashCode += getNomFichier().hashCode();
        }
        if (getIdArchivage() != null) {
            _hashCode += getIdArchivage().hashCode();
        }
        if (getEtat() != null) {
            _hashCode += getEtat().hashCode();
        }
        if (getCodeOrganisme() != null) {
            _hashCode += getCodeOrganisme().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Document.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Document"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomFichier");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomFichier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("etat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "etat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeOrganisme");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeOrganisme"));
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
