/**
 * RNDProcessus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDProcessus  implements java.io.Serializable {
    private java.lang.String _reference;

    private java.lang.String _label;

    private java.lang.String _refSecteurActivite;

    private int _idVersion;

    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypesDocuments;

    private boolean _etat;

    private java.lang.String _commentaire;

    public RNDProcessus() {
    }

    public RNDProcessus(
           java.lang.String _reference,
           java.lang.String _label,
           java.lang.String _refSecteurActivite,
           int _idVersion,
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypesDocuments,
           boolean _etat,
           java.lang.String _commentaire) {
           this._reference = _reference;
           this._label = _label;
           this._refSecteurActivite = _refSecteurActivite;
           this._idVersion = _idVersion;
           this._listeTypesDocuments = _listeTypesDocuments;
           this._etat = _etat;
           this._commentaire = _commentaire;
    }


    /**
     * Gets the _reference value for this RNDProcessus.
     * 
     * @return _reference
     */
    public java.lang.String get_reference() {
        return _reference;
    }


    /**
     * Sets the _reference value for this RNDProcessus.
     * 
     * @param _reference
     */
    public void set_reference(java.lang.String _reference) {
        this._reference = _reference;
    }


    /**
     * Gets the _label value for this RNDProcessus.
     * 
     * @return _label
     */
    public java.lang.String get_label() {
        return _label;
    }


    /**
     * Sets the _label value for this RNDProcessus.
     * 
     * @param _label
     */
    public void set_label(java.lang.String _label) {
        this._label = _label;
    }


    /**
     * Gets the _refSecteurActivite value for this RNDProcessus.
     * 
     * @return _refSecteurActivite
     */
    public java.lang.String get_refSecteurActivite() {
        return _refSecteurActivite;
    }


    /**
     * Sets the _refSecteurActivite value for this RNDProcessus.
     * 
     * @param _refSecteurActivite
     */
    public void set_refSecteurActivite(java.lang.String _refSecteurActivite) {
        this._refSecteurActivite = _refSecteurActivite;
    }


    /**
     * Gets the _idVersion value for this RNDProcessus.
     * 
     * @return _idVersion
     */
    public int get_idVersion() {
        return _idVersion;
    }


    /**
     * Sets the _idVersion value for this RNDProcessus.
     * 
     * @param _idVersion
     */
    public void set_idVersion(int _idVersion) {
        this._idVersion = _idVersion;
    }


    /**
     * Gets the _listeTypesDocuments value for this RNDProcessus.
     * 
     * @return _listeTypesDocuments
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] get_listeTypesDocuments() {
        return _listeTypesDocuments;
    }


    /**
     * Sets the _listeTypesDocuments value for this RNDProcessus.
     * 
     * @param _listeTypesDocuments
     */
    public void set_listeTypesDocuments(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypesDocuments) {
        this._listeTypesDocuments = _listeTypesDocuments;
    }


    /**
     * Gets the _etat value for this RNDProcessus.
     * 
     * @return _etat
     */
    public boolean is_etat() {
        return _etat;
    }


    /**
     * Sets the _etat value for this RNDProcessus.
     * 
     * @param _etat
     */
    public void set_etat(boolean _etat) {
        this._etat = _etat;
    }


    /**
     * Gets the _commentaire value for this RNDProcessus.
     * 
     * @return _commentaire
     */
    public java.lang.String get_commentaire() {
        return _commentaire;
    }


    /**
     * Sets the _commentaire value for this RNDProcessus.
     * 
     * @param _commentaire
     */
    public void set_commentaire(java.lang.String _commentaire) {
        this._commentaire = _commentaire;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDProcessus)) return false;
        RNDProcessus other = (RNDProcessus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._reference==null && other.get_reference()==null) || 
             (this._reference!=null &&
              this._reference.equals(other.get_reference()))) &&
            ((this._label==null && other.get_label()==null) || 
             (this._label!=null &&
              this._label.equals(other.get_label()))) &&
            ((this._refSecteurActivite==null && other.get_refSecteurActivite()==null) || 
             (this._refSecteurActivite!=null &&
              this._refSecteurActivite.equals(other.get_refSecteurActivite()))) &&
            this._idVersion == other.get_idVersion() &&
            ((this._listeTypesDocuments==null && other.get_listeTypesDocuments()==null) || 
             (this._listeTypesDocuments!=null &&
              java.util.Arrays.equals(this._listeTypesDocuments, other.get_listeTypesDocuments()))) &&
            this._etat == other.is_etat() &&
            ((this._commentaire==null && other.get_commentaire()==null) || 
             (this._commentaire!=null &&
              this._commentaire.equals(other.get_commentaire())));
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
        if (get_reference() != null) {
            _hashCode += get_reference().hashCode();
        }
        if (get_label() != null) {
            _hashCode += get_label().hashCode();
        }
        if (get_refSecteurActivite() != null) {
            _hashCode += get_refSecteurActivite().hashCode();
        }
        _hashCode += get_idVersion();
        if (get_listeTypesDocuments() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_listeTypesDocuments());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_listeTypesDocuments(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (is_etat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (get_commentaire() != null) {
            _hashCode += get_commentaire().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RNDProcessus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDProcessus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_reference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_refSecteurActivite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_refSecteurActivite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_idVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_idVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_listeTypesDocuments");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_listeTypesDocuments"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTypeDocument"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_etat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_etat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_commentaire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_commentaire"));
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
