/**
 * DocumentGedAED.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Element contenant les informations caracterisant un document de
 * la GED version AED
 */
public class DocumentGedAED  implements java.io.Serializable {
    private java.lang.String idArchivage;

    private java.lang.String user;

    private java.lang.String nbr_copies;

    private java.lang.String nbr_archives;

    private java.lang.String supprime;

    private java.lang.String xml;

    private java.lang.String table;

    public DocumentGedAED() {
    }

    public DocumentGedAED(
           java.lang.String idArchivage,
           java.lang.String user,
           java.lang.String nbr_copies,
           java.lang.String nbr_archives,
           java.lang.String supprime,
           java.lang.String xml,
           java.lang.String table) {
           this.idArchivage = idArchivage;
           this.user = user;
           this.nbr_copies = nbr_copies;
           this.nbr_archives = nbr_archives;
           this.supprime = supprime;
           this.xml = xml;
           this.table = table;
    }


    /**
     * Gets the idArchivage value for this DocumentGedAED.
     * 
     * @return idArchivage
     */
    public java.lang.String getIdArchivage() {
        return idArchivage;
    }


    /**
     * Sets the idArchivage value for this DocumentGedAED.
     * 
     * @param idArchivage
     */
    public void setIdArchivage(java.lang.String idArchivage) {
        this.idArchivage = idArchivage;
    }


    /**
     * Gets the user value for this DocumentGedAED.
     * 
     * @return user
     */
    public java.lang.String getUser() {
        return user;
    }


    /**
     * Sets the user value for this DocumentGedAED.
     * 
     * @param user
     */
    public void setUser(java.lang.String user) {
        this.user = user;
    }


    /**
     * Gets the nbr_copies value for this DocumentGedAED.
     * 
     * @return nbr_copies
     */
    public java.lang.String getNbr_copies() {
        return nbr_copies;
    }


    /**
     * Sets the nbr_copies value for this DocumentGedAED.
     * 
     * @param nbr_copies
     */
    public void setNbr_copies(java.lang.String nbr_copies) {
        this.nbr_copies = nbr_copies;
    }


    /**
     * Gets the nbr_archives value for this DocumentGedAED.
     * 
     * @return nbr_archives
     */
    public java.lang.String getNbr_archives() {
        return nbr_archives;
    }


    /**
     * Sets the nbr_archives value for this DocumentGedAED.
     * 
     * @param nbr_archives
     */
    public void setNbr_archives(java.lang.String nbr_archives) {
        this.nbr_archives = nbr_archives;
    }


    /**
     * Gets the supprime value for this DocumentGedAED.
     * 
     * @return supprime
     */
    public java.lang.String getSupprime() {
        return supprime;
    }


    /**
     * Sets the supprime value for this DocumentGedAED.
     * 
     * @param supprime
     */
    public void setSupprime(java.lang.String supprime) {
        this.supprime = supprime;
    }


    /**
     * Gets the xml value for this DocumentGedAED.
     * 
     * @return xml
     */
    public java.lang.String getXml() {
        return xml;
    }


    /**
     * Sets the xml value for this DocumentGedAED.
     * 
     * @param xml
     */
    public void setXml(java.lang.String xml) {
        this.xml = xml;
    }


    /**
     * Gets the table value for this DocumentGedAED.
     * 
     * @return table
     */
    public java.lang.String getTable() {
        return table;
    }


    /**
     * Sets the table value for this DocumentGedAED.
     * 
     * @param table
     */
    public void setTable(java.lang.String table) {
        this.table = table;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentGedAED)) return false;
        DocumentGedAED other = (DocumentGedAED) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idArchivage==null && other.getIdArchivage()==null) || 
             (this.idArchivage!=null &&
              this.idArchivage.equals(other.getIdArchivage()))) &&
            ((this.user==null && other.getUser()==null) || 
             (this.user!=null &&
              this.user.equals(other.getUser()))) &&
            ((this.nbr_copies==null && other.getNbr_copies()==null) || 
             (this.nbr_copies!=null &&
              this.nbr_copies.equals(other.getNbr_copies()))) &&
            ((this.nbr_archives==null && other.getNbr_archives()==null) || 
             (this.nbr_archives!=null &&
              this.nbr_archives.equals(other.getNbr_archives()))) &&
            ((this.supprime==null && other.getSupprime()==null) || 
             (this.supprime!=null &&
              this.supprime.equals(other.getSupprime()))) &&
            ((this.xml==null && other.getXml()==null) || 
             (this.xml!=null &&
              this.xml.equals(other.getXml()))) &&
            ((this.table==null && other.getTable()==null) || 
             (this.table!=null &&
              this.table.equals(other.getTable())));
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
        if (getIdArchivage() != null) {
            _hashCode += getIdArchivage().hashCode();
        }
        if (getUser() != null) {
            _hashCode += getUser().hashCode();
        }
        if (getNbr_copies() != null) {
            _hashCode += getNbr_copies().hashCode();
        }
        if (getNbr_archives() != null) {
            _hashCode += getNbr_archives().hashCode();
        }
        if (getSupprime() != null) {
            _hashCode += getSupprime().hashCode();
        }
        if (getXml() != null) {
            _hashCode += getXml().hashCode();
        }
        if (getTable() != null) {
            _hashCode += getTable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentGedAED.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGedAED"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nbr_copies");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nbr_copies"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nbr_archives");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nbr_archives"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supprime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "supprime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xml");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xml"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("table");
        elemField.setXmlName(new javax.xml.namespace.QName("", "table"));
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
