/**
 * RNDFonction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDFonction  implements java.io.Serializable {
    private java.lang.String _reference;

    private java.lang.String _label;

    private int _idVersion;

    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite[] _listeActivites;

    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypeDocument;

    private boolean _etat;

    private java.lang.String _commentaire;

    public RNDFonction() {
    }

    public RNDFonction(
           java.lang.String _reference,
           java.lang.String _label,
           int _idVersion,
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite[] _listeActivites,
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypeDocument,
           boolean _etat,
           java.lang.String _commentaire) {
           this._reference = _reference;
           this._label = _label;
           this._idVersion = _idVersion;
           this._listeActivites = _listeActivites;
           this._listeTypeDocument = _listeTypeDocument;
           this._etat = _etat;
           this._commentaire = _commentaire;
    }


    /**
     * Gets the _reference value for this RNDFonction.
     * 
     * @return _reference
     */
    public java.lang.String get_reference() {
        return _reference;
    }


    /**
     * Sets the _reference value for this RNDFonction.
     * 
     * @param _reference
     */
    public void set_reference(java.lang.String _reference) {
        this._reference = _reference;
    }


    /**
     * Gets the _label value for this RNDFonction.
     * 
     * @return _label
     */
    public java.lang.String get_label() {
        return _label;
    }


    /**
     * Sets the _label value for this RNDFonction.
     * 
     * @param _label
     */
    public void set_label(java.lang.String _label) {
        this._label = _label;
    }


    /**
     * Gets the _idVersion value for this RNDFonction.
     * 
     * @return _idVersion
     */
    public int get_idVersion() {
        return _idVersion;
    }


    /**
     * Sets the _idVersion value for this RNDFonction.
     * 
     * @param _idVersion
     */
    public void set_idVersion(int _idVersion) {
        this._idVersion = _idVersion;
    }


    /**
     * Gets the _listeActivites value for this RNDFonction.
     * 
     * @return _listeActivites
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite[] get_listeActivites() {
        return _listeActivites;
    }


    /**
     * Sets the _listeActivites value for this RNDFonction.
     * 
     * @param _listeActivites
     */
    public void set_listeActivites(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite[] _listeActivites) {
        this._listeActivites = _listeActivites;
    }


    /**
     * Gets the _listeTypeDocument value for this RNDFonction.
     * 
     * @return _listeTypeDocument
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] get_listeTypeDocument() {
        return _listeTypeDocument;
    }


    /**
     * Sets the _listeTypeDocument value for this RNDFonction.
     * 
     * @param _listeTypeDocument
     */
    public void set_listeTypeDocument(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] _listeTypeDocument) {
        this._listeTypeDocument = _listeTypeDocument;
    }


    /**
     * Gets the _etat value for this RNDFonction.
     * 
     * @return _etat
     */
    public boolean is_etat() {
        return _etat;
    }


    /**
     * Sets the _etat value for this RNDFonction.
     * 
     * @param _etat
     */
    public void set_etat(boolean _etat) {
        this._etat = _etat;
    }


    /**
     * Gets the _commentaire value for this RNDFonction.
     * 
     * @return _commentaire
     */
    public java.lang.String get_commentaire() {
        return _commentaire;
    }


    /**
     * Sets the _commentaire value for this RNDFonction.
     * 
     * @param _commentaire
     */
    public void set_commentaire(java.lang.String _commentaire) {
        this._commentaire = _commentaire;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDFonction)) return false;
        RNDFonction other = (RNDFonction) obj;
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
            this._idVersion == other.get_idVersion() &&
            ((this._listeActivites==null && other.get_listeActivites()==null) || 
             (this._listeActivites!=null &&
              java.util.Arrays.equals(this._listeActivites, other.get_listeActivites()))) &&
            ((this._listeTypeDocument==null && other.get_listeTypeDocument()==null) || 
             (this._listeTypeDocument!=null &&
              java.util.Arrays.equals(this._listeTypeDocument, other.get_listeTypeDocument()))) &&
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
        _hashCode += get_idVersion();
        if (get_listeActivites() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_listeActivites());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_listeActivites(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (get_listeTypeDocument() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_listeTypeDocument());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_listeTypeDocument(), i);
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
        new org.apache.axis.description.TypeDesc(RNDFonction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDFonction"));
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
        elemField.setFieldName("_idVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_idVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_listeActivites");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_listeActivites"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDActivite"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_listeTypeDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_listeTypeDocument"));
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
