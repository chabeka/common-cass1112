/**
 * DocumentAED.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Document pour entree en GED nouvelle formule
 */
public class DocumentAED  implements java.io.Serializable {
    private java.lang.String idDocument;

    private java.lang.String idArchivage;

    private java.lang.String etat;

    private java.lang.String motifEtat;

    private java.lang.String codeOrganisme;

    private java.lang.String rang;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED[] historique;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED[] instances;

    private java.lang.String idArchivageEnGED;

    private java.lang.String baseGED;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.FichierAED fichier;

    public DocumentAED() {
    }

    public DocumentAED(
           java.lang.String idDocument,
           java.lang.String idArchivage,
           java.lang.String etat,
           java.lang.String motifEtat,
           java.lang.String codeOrganisme,
           java.lang.String rang,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED[] historique,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED[] instances,
           java.lang.String idArchivageEnGED,
           java.lang.String baseGED,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.FichierAED fichier) {
           this.idDocument = idDocument;
           this.idArchivage = idArchivage;
           this.etat = etat;
           this.motifEtat = motifEtat;
           this.codeOrganisme = codeOrganisme;
           this.rang = rang;
           this.historique = historique;
           this.instances = instances;
           this.idArchivageEnGED = idArchivageEnGED;
           this.baseGED = baseGED;
           this.fichier = fichier;
    }


    /**
     * Gets the idDocument value for this DocumentAED.
     * 
     * @return idDocument
     */
    public java.lang.String getIdDocument() {
        return idDocument;
    }


    /**
     * Sets the idDocument value for this DocumentAED.
     * 
     * @param idDocument
     */
    public void setIdDocument(java.lang.String idDocument) {
        this.idDocument = idDocument;
    }


    /**
     * Gets the idArchivage value for this DocumentAED.
     * 
     * @return idArchivage
     */
    public java.lang.String getIdArchivage() {
        return idArchivage;
    }


    /**
     * Sets the idArchivage value for this DocumentAED.
     * 
     * @param idArchivage
     */
    public void setIdArchivage(java.lang.String idArchivage) {
        this.idArchivage = idArchivage;
    }


    /**
     * Gets the etat value for this DocumentAED.
     * 
     * @return etat
     */
    public java.lang.String getEtat() {
        return etat;
    }


    /**
     * Sets the etat value for this DocumentAED.
     * 
     * @param etat
     */
    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }


    /**
     * Gets the motifEtat value for this DocumentAED.
     * 
     * @return motifEtat
     */
    public java.lang.String getMotifEtat() {
        return motifEtat;
    }


    /**
     * Sets the motifEtat value for this DocumentAED.
     * 
     * @param motifEtat
     */
    public void setMotifEtat(java.lang.String motifEtat) {
        this.motifEtat = motifEtat;
    }


    /**
     * Gets the codeOrganisme value for this DocumentAED.
     * 
     * @return codeOrganisme
     */
    public java.lang.String getCodeOrganisme() {
        return codeOrganisme;
    }


    /**
     * Sets the codeOrganisme value for this DocumentAED.
     * 
     * @param codeOrganisme
     */
    public void setCodeOrganisme(java.lang.String codeOrganisme) {
        this.codeOrganisme = codeOrganisme;
    }


    /**
     * Gets the rang value for this DocumentAED.
     * 
     * @return rang
     */
    public java.lang.String getRang() {
        return rang;
    }


    /**
     * Sets the rang value for this DocumentAED.
     * 
     * @param rang
     */
    public void setRang(java.lang.String rang) {
        this.rang = rang;
    }


    /**
     * Gets the historique value for this DocumentAED.
     * 
     * @return historique
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED[] getHistorique() {
        return historique;
    }


    /**
     * Sets the historique value for this DocumentAED.
     * 
     * @param historique
     */
    public void setHistorique(fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED[] historique) {
        this.historique = historique;
    }


    /**
     * Gets the instances value for this DocumentAED.
     * 
     * @return instances
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED[] getInstances() {
        return instances;
    }


    /**
     * Sets the instances value for this DocumentAED.
     * 
     * @param instances
     */
    public void setInstances(fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED[] instances) {
        this.instances = instances;
    }


    /**
     * Gets the idArchivageEnGED value for this DocumentAED.
     * 
     * @return idArchivageEnGED
     */
    public java.lang.String getIdArchivageEnGED() {
        return idArchivageEnGED;
    }


    /**
     * Sets the idArchivageEnGED value for this DocumentAED.
     * 
     * @param idArchivageEnGED
     */
    public void setIdArchivageEnGED(java.lang.String idArchivageEnGED) {
        this.idArchivageEnGED = idArchivageEnGED;
    }


    /**
     * Gets the baseGED value for this DocumentAED.
     * 
     * @return baseGED
     */
    public java.lang.String getBaseGED() {
        return baseGED;
    }


    /**
     * Sets the baseGED value for this DocumentAED.
     * 
     * @param baseGED
     */
    public void setBaseGED(java.lang.String baseGED) {
        this.baseGED = baseGED;
    }


    /**
     * Gets the fichier value for this DocumentAED.
     * 
     * @return fichier
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.FichierAED getFichier() {
        return fichier;
    }


    /**
     * Sets the fichier value for this DocumentAED.
     * 
     * @param fichier
     */
    public void setFichier(fr.urssaf.image.commons.webservice.rpc.aed.modele.FichierAED fichier) {
        this.fichier = fichier;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentAED)) return false;
        DocumentAED other = (DocumentAED) obj;
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
            ((this.idArchivage==null && other.getIdArchivage()==null) || 
             (this.idArchivage!=null &&
              this.idArchivage.equals(other.getIdArchivage()))) &&
            ((this.etat==null && other.getEtat()==null) || 
             (this.etat!=null &&
              this.etat.equals(other.getEtat()))) &&
            ((this.motifEtat==null && other.getMotifEtat()==null) || 
             (this.motifEtat!=null &&
              this.motifEtat.equals(other.getMotifEtat()))) &&
            ((this.codeOrganisme==null && other.getCodeOrganisme()==null) || 
             (this.codeOrganisme!=null &&
              this.codeOrganisme.equals(other.getCodeOrganisme()))) &&
            ((this.rang==null && other.getRang()==null) || 
             (this.rang!=null &&
              this.rang.equals(other.getRang()))) &&
            ((this.historique==null && other.getHistorique()==null) || 
             (this.historique!=null &&
              java.util.Arrays.equals(this.historique, other.getHistorique()))) &&
            ((this.instances==null && other.getInstances()==null) || 
             (this.instances!=null &&
              java.util.Arrays.equals(this.instances, other.getInstances()))) &&
            ((this.idArchivageEnGED==null && other.getIdArchivageEnGED()==null) || 
             (this.idArchivageEnGED!=null &&
              this.idArchivageEnGED.equals(other.getIdArchivageEnGED()))) &&
            ((this.baseGED==null && other.getBaseGED()==null) || 
             (this.baseGED!=null &&
              this.baseGED.equals(other.getBaseGED()))) &&
            ((this.fichier==null && other.getFichier()==null) || 
             (this.fichier!=null &&
              this.fichier.equals(other.getFichier())));
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
        if (getIdArchivage() != null) {
            _hashCode += getIdArchivage().hashCode();
        }
        if (getEtat() != null) {
            _hashCode += getEtat().hashCode();
        }
        if (getMotifEtat() != null) {
            _hashCode += getMotifEtat().hashCode();
        }
        if (getCodeOrganisme() != null) {
            _hashCode += getCodeOrganisme().hashCode();
        }
        if (getRang() != null) {
            _hashCode += getRang().hashCode();
        }
        if (getHistorique() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHistorique());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHistorique(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInstances() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInstances());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInstances(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdArchivageEnGED() != null) {
            _hashCode += getIdArchivageEnGED().hashCode();
        }
        if (getBaseGED() != null) {
            _hashCode += getBaseGED().hashCode();
        }
        if (getFichier() != null) {
            _hashCode += getFichier().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentAED.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentAED"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("etat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "etat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motifEtat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motifEtat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeOrganisme");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeOrganisme"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rang");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rang"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("historique");
        elemField.setXmlName(new javax.xml.namespace.QName("", "historique"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "OperationAED"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instances");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instances"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "InstanceIndexationAED"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivageEnGED");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivageEnGED"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("baseGED");
        elemField.setXmlName(new javax.xml.namespace.QName("", "baseGED"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fichier");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fichier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "FichierAED"));
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
