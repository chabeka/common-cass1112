/**
 * DocumentGed.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Element contenant les informations caracterisant un document de
 * la GED deportee
 */
public class DocumentGed  implements java.io.Serializable {
    private java.lang.String idArchivage;

    private java.lang.String typeBase;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] indexes;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] motClefs;

    private java.lang.String orgMutualise;

    private java.lang.String codeUrssaf;

    private java.lang.String dateReception;

    private java.lang.String hash;

    public DocumentGed() {
    }

    public DocumentGed(
           java.lang.String idArchivage,
           java.lang.String typeBase,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] indexes,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] motClefs,
           java.lang.String orgMutualise,
           java.lang.String codeUrssaf,
           java.lang.String dateReception,
           java.lang.String hash) {
           this.idArchivage = idArchivage;
           this.typeBase = typeBase;
           this.indexes = indexes;
           this.motClefs = motClefs;
           this.orgMutualise = orgMutualise;
           this.codeUrssaf = codeUrssaf;
           this.dateReception = dateReception;
           this.hash = hash;
    }


    /**
     * Gets the idArchivage value for this DocumentGed.
     * 
     * @return idArchivage
     */
    public java.lang.String getIdArchivage() {
        return idArchivage;
    }


    /**
     * Sets the idArchivage value for this DocumentGed.
     * 
     * @param idArchivage
     */
    public void setIdArchivage(java.lang.String idArchivage) {
        this.idArchivage = idArchivage;
    }


    /**
     * Gets the typeBase value for this DocumentGed.
     * 
     * @return typeBase
     */
    public java.lang.String getTypeBase() {
        return typeBase;
    }


    /**
     * Sets the typeBase value for this DocumentGed.
     * 
     * @param typeBase
     */
    public void setTypeBase(java.lang.String typeBase) {
        this.typeBase = typeBase;
    }


    /**
     * Gets the indexes value for this DocumentGed.
     * 
     * @return indexes
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] getIndexes() {
        return indexes;
    }


    /**
     * Sets the indexes value for this DocumentGed.
     * 
     * @param indexes
     */
    public void setIndexes(fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] indexes) {
        this.indexes = indexes;
    }


    /**
     * Gets the motClefs value for this DocumentGed.
     * 
     * @return motClefs
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] getMotClefs() {
        return motClefs;
    }


    /**
     * Sets the motClefs value for this DocumentGed.
     * 
     * @param motClefs
     */
    public void setMotClefs(fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] motClefs) {
        this.motClefs = motClefs;
    }


    /**
     * Gets the orgMutualise value for this DocumentGed.
     * 
     * @return orgMutualise
     */
    public java.lang.String getOrgMutualise() {
        return orgMutualise;
    }


    /**
     * Sets the orgMutualise value for this DocumentGed.
     * 
     * @param orgMutualise
     */
    public void setOrgMutualise(java.lang.String orgMutualise) {
        this.orgMutualise = orgMutualise;
    }


    /**
     * Gets the codeUrssaf value for this DocumentGed.
     * 
     * @return codeUrssaf
     */
    public java.lang.String getCodeUrssaf() {
        return codeUrssaf;
    }


    /**
     * Sets the codeUrssaf value for this DocumentGed.
     * 
     * @param codeUrssaf
     */
    public void setCodeUrssaf(java.lang.String codeUrssaf) {
        this.codeUrssaf = codeUrssaf;
    }


    /**
     * Gets the dateReception value for this DocumentGed.
     * 
     * @return dateReception
     */
    public java.lang.String getDateReception() {
        return dateReception;
    }


    /**
     * Sets the dateReception value for this DocumentGed.
     * 
     * @param dateReception
     */
    public void setDateReception(java.lang.String dateReception) {
        this.dateReception = dateReception;
    }


    /**
     * Gets the hash value for this DocumentGed.
     * 
     * @return hash
     */
    public java.lang.String getHash() {
        return hash;
    }


    /**
     * Sets the hash value for this DocumentGed.
     * 
     * @param hash
     */
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentGed)) return false;
        DocumentGed other = (DocumentGed) obj;
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
            ((this.typeBase==null && other.getTypeBase()==null) || 
             (this.typeBase!=null &&
              this.typeBase.equals(other.getTypeBase()))) &&
            ((this.indexes==null && other.getIndexes()==null) || 
             (this.indexes!=null &&
              java.util.Arrays.equals(this.indexes, other.getIndexes()))) &&
            ((this.motClefs==null && other.getMotClefs()==null) || 
             (this.motClefs!=null &&
              java.util.Arrays.equals(this.motClefs, other.getMotClefs()))) &&
            ((this.orgMutualise==null && other.getOrgMutualise()==null) || 
             (this.orgMutualise!=null &&
              this.orgMutualise.equals(other.getOrgMutualise()))) &&
            ((this.codeUrssaf==null && other.getCodeUrssaf()==null) || 
             (this.codeUrssaf!=null &&
              this.codeUrssaf.equals(other.getCodeUrssaf()))) &&
            ((this.dateReception==null && other.getDateReception()==null) || 
             (this.dateReception!=null &&
              this.dateReception.equals(other.getDateReception()))) &&
            ((this.hash==null && other.getHash()==null) || 
             (this.hash!=null &&
              this.hash.equals(other.getHash())));
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
        if (getTypeBase() != null) {
            _hashCode += getTypeBase().hashCode();
        }
        if (getIndexes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndexes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndexes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMotClefs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMotClefs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMotClefs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOrgMutualise() != null) {
            _hashCode += getOrgMutualise().hashCode();
        }
        if (getCodeUrssaf() != null) {
            _hashCode += getCodeUrssaf().hashCode();
        }
        if (getDateReception() != null) {
            _hashCode += getDateReception().hashCode();
        }
        if (getHash() != null) {
            _hashCode += getHash().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentGed.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGed"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeBase");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeBase"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indexes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indexes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motClefs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motClefs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orgMutualise");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orgMutualise"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeUrssaf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeUrssaf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateReception");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateReception"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hash");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hash"));
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
