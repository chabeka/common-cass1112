/**
 * RNDTypeDocument.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDTypeDocument  implements java.io.Serializable {
    private int _idVersion;

    private java.lang.String _reference;

    private java.lang.String _label;

    private java.lang.String _refProcessus;

    private java.lang.String _refSecteurActivite;

    private java.lang.String _refActivite;

    private java.lang.String _refFonction;

    private int _dureeArchivage;

    private boolean _destructionAutorisee;

    private boolean _etat;

    private java.lang.String _commentaire;

    private boolean _archivable;

    private boolean _archiveHisto;

    private boolean _pj;

    private java.lang.String _type;

    private java.lang.String _dlc;

    private java.lang.String _responsabilite;

    private boolean _reserveCNM;

    private java.lang.String _typePJ;

    public RNDTypeDocument() {
    }

    public RNDTypeDocument(
           int _idVersion,
           java.lang.String _reference,
           java.lang.String _label,
           java.lang.String _refProcessus,
           java.lang.String _refSecteurActivite,
           java.lang.String _refActivite,
           java.lang.String _refFonction,
           int _dureeArchivage,
           boolean _destructionAutorisee,
           boolean _etat,
           java.lang.String _commentaire,
           boolean _archivable,
           boolean _archiveHisto,
           boolean _pj,
           java.lang.String _type,
           java.lang.String _dlc,
           java.lang.String _responsabilite,
           boolean _reserveCNM,
           java.lang.String _typePJ) {
           this._idVersion = _idVersion;
           this._reference = _reference;
           this._label = _label;
           this._refProcessus = _refProcessus;
           this._refSecteurActivite = _refSecteurActivite;
           this._refActivite = _refActivite;
           this._refFonction = _refFonction;
           this._dureeArchivage = _dureeArchivage;
           this._destructionAutorisee = _destructionAutorisee;
           this._etat = _etat;
           this._commentaire = _commentaire;
           this._archivable = _archivable;
           this._archiveHisto = _archiveHisto;
           this._pj = _pj;
           this._type = _type;
           this._dlc = _dlc;
           this._responsabilite = _responsabilite;
           this._reserveCNM = _reserveCNM;
           this._typePJ = _typePJ;
    }


    /**
     * Gets the _idVersion value for this RNDTypeDocument.
     * 
     * @return _idVersion
     */
    public int get_idVersion() {
        return _idVersion;
    }


    /**
     * Sets the _idVersion value for this RNDTypeDocument.
     * 
     * @param _idVersion
     */
    public void set_idVersion(int _idVersion) {
        this._idVersion = _idVersion;
    }


    /**
     * Gets the _reference value for this RNDTypeDocument.
     * 
     * @return _reference
     */
    public java.lang.String get_reference() {
        return _reference;
    }


    /**
     * Sets the _reference value for this RNDTypeDocument.
     * 
     * @param _reference
     */
    public void set_reference(java.lang.String _reference) {
        this._reference = _reference;
    }


    /**
     * Gets the _label value for this RNDTypeDocument.
     * 
     * @return _label
     */
    public java.lang.String get_label() {
        return _label;
    }


    /**
     * Sets the _label value for this RNDTypeDocument.
     * 
     * @param _label
     */
    public void set_label(java.lang.String _label) {
        this._label = _label;
    }


    /**
     * Gets the _refProcessus value for this RNDTypeDocument.
     * 
     * @return _refProcessus
     */
    public java.lang.String get_refProcessus() {
        return _refProcessus;
    }


    /**
     * Sets the _refProcessus value for this RNDTypeDocument.
     * 
     * @param _refProcessus
     */
    public void set_refProcessus(java.lang.String _refProcessus) {
        this._refProcessus = _refProcessus;
    }


    /**
     * Gets the _refSecteurActivite value for this RNDTypeDocument.
     * 
     * @return _refSecteurActivite
     */
    public java.lang.String get_refSecteurActivite() {
        return _refSecteurActivite;
    }


    /**
     * Sets the _refSecteurActivite value for this RNDTypeDocument.
     * 
     * @param _refSecteurActivite
     */
    public void set_refSecteurActivite(java.lang.String _refSecteurActivite) {
        this._refSecteurActivite = _refSecteurActivite;
    }


    /**
     * Gets the _refActivite value for this RNDTypeDocument.
     * 
     * @return _refActivite
     */
    public java.lang.String get_refActivite() {
        return _refActivite;
    }


    /**
     * Sets the _refActivite value for this RNDTypeDocument.
     * 
     * @param _refActivite
     */
    public void set_refActivite(java.lang.String _refActivite) {
        this._refActivite = _refActivite;
    }


    /**
     * Gets the _refFonction value for this RNDTypeDocument.
     * 
     * @return _refFonction
     */
    public java.lang.String get_refFonction() {
        return _refFonction;
    }


    /**
     * Sets the _refFonction value for this RNDTypeDocument.
     * 
     * @param _refFonction
     */
    public void set_refFonction(java.lang.String _refFonction) {
        this._refFonction = _refFonction;
    }


    /**
     * Gets the _dureeArchivage value for this RNDTypeDocument.
     * 
     * @return _dureeArchivage
     */
    public int get_dureeArchivage() {
        return _dureeArchivage;
    }


    /**
     * Sets the _dureeArchivage value for this RNDTypeDocument.
     * 
     * @param _dureeArchivage
     */
    public void set_dureeArchivage(int _dureeArchivage) {
        this._dureeArchivage = _dureeArchivage;
    }


    /**
     * Gets the _destructionAutorisee value for this RNDTypeDocument.
     * 
     * @return _destructionAutorisee
     */
    public boolean is_destructionAutorisee() {
        return _destructionAutorisee;
    }


    /**
     * Sets the _destructionAutorisee value for this RNDTypeDocument.
     * 
     * @param _destructionAutorisee
     */
    public void set_destructionAutorisee(boolean _destructionAutorisee) {
        this._destructionAutorisee = _destructionAutorisee;
    }


    /**
     * Gets the _etat value for this RNDTypeDocument.
     * 
     * @return _etat
     */
    public boolean is_etat() {
        return _etat;
    }


    /**
     * Sets the _etat value for this RNDTypeDocument.
     * 
     * @param _etat
     */
    public void set_etat(boolean _etat) {
        this._etat = _etat;
    }


    /**
     * Gets the _commentaire value for this RNDTypeDocument.
     * 
     * @return _commentaire
     */
    public java.lang.String get_commentaire() {
        return _commentaire;
    }


    /**
     * Sets the _commentaire value for this RNDTypeDocument.
     * 
     * @param _commentaire
     */
    public void set_commentaire(java.lang.String _commentaire) {
        this._commentaire = _commentaire;
    }


    /**
     * Gets the _archivable value for this RNDTypeDocument.
     * 
     * @return _archivable
     */
    public boolean is_archivable() {
        return _archivable;
    }


    /**
     * Sets the _archivable value for this RNDTypeDocument.
     * 
     * @param _archivable
     */
    public void set_archivable(boolean _archivable) {
        this._archivable = _archivable;
    }


    /**
     * Gets the _archiveHisto value for this RNDTypeDocument.
     * 
     * @return _archiveHisto
     */
    public boolean is_archiveHisto() {
        return _archiveHisto;
    }


    /**
     * Sets the _archiveHisto value for this RNDTypeDocument.
     * 
     * @param _archiveHisto
     */
    public void set_archiveHisto(boolean _archiveHisto) {
        this._archiveHisto = _archiveHisto;
    }


    /**
     * Gets the _pj value for this RNDTypeDocument.
     * 
     * @return _pj
     */
    public boolean is_pj() {
        return _pj;
    }


    /**
     * Sets the _pj value for this RNDTypeDocument.
     * 
     * @param _pj
     */
    public void set_pj(boolean _pj) {
        this._pj = _pj;
    }


    /**
     * Gets the _type value for this RNDTypeDocument.
     * 
     * @return _type
     */
    public java.lang.String get_type() {
        return _type;
    }


    /**
     * Sets the _type value for this RNDTypeDocument.
     * 
     * @param _type
     */
    public void set_type(java.lang.String _type) {
        this._type = _type;
    }


    /**
     * Gets the _dlc value for this RNDTypeDocument.
     * 
     * @return _dlc
     */
    public java.lang.String get_dlc() {
        return _dlc;
    }


    /**
     * Sets the _dlc value for this RNDTypeDocument.
     * 
     * @param _dlc
     */
    public void set_dlc(java.lang.String _dlc) {
        this._dlc = _dlc;
    }


    /**
     * Gets the _responsabilite value for this RNDTypeDocument.
     * 
     * @return _responsabilite
     */
    public java.lang.String get_responsabilite() {
        return _responsabilite;
    }


    /**
     * Sets the _responsabilite value for this RNDTypeDocument.
     * 
     * @param _responsabilite
     */
    public void set_responsabilite(java.lang.String _responsabilite) {
        this._responsabilite = _responsabilite;
    }


    /**
     * Gets the _reserveCNM value for this RNDTypeDocument.
     * 
     * @return _reserveCNM
     */
    public boolean is_reserveCNM() {
        return _reserveCNM;
    }


    /**
     * Sets the _reserveCNM value for this RNDTypeDocument.
     * 
     * @param _reserveCNM
     */
    public void set_reserveCNM(boolean _reserveCNM) {
        this._reserveCNM = _reserveCNM;
    }


    /**
     * Gets the _typePJ value for this RNDTypeDocument.
     * 
     * @return _typePJ
     */
    public java.lang.String get_typePJ() {
        return _typePJ;
    }


    /**
     * Sets the _typePJ value for this RNDTypeDocument.
     * 
     * @param _typePJ
     */
    public void set_typePJ(java.lang.String _typePJ) {
        this._typePJ = _typePJ;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDTypeDocument)) return false;
        RNDTypeDocument other = (RNDTypeDocument) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this._idVersion == other.get_idVersion() &&
            ((this._reference==null && other.get_reference()==null) || 
             (this._reference!=null &&
              this._reference.equals(other.get_reference()))) &&
            ((this._label==null && other.get_label()==null) || 
             (this._label!=null &&
              this._label.equals(other.get_label()))) &&
            ((this._refProcessus==null && other.get_refProcessus()==null) || 
             (this._refProcessus!=null &&
              this._refProcessus.equals(other.get_refProcessus()))) &&
            ((this._refSecteurActivite==null && other.get_refSecteurActivite()==null) || 
             (this._refSecteurActivite!=null &&
              this._refSecteurActivite.equals(other.get_refSecteurActivite()))) &&
            ((this._refActivite==null && other.get_refActivite()==null) || 
             (this._refActivite!=null &&
              this._refActivite.equals(other.get_refActivite()))) &&
            ((this._refFonction==null && other.get_refFonction()==null) || 
             (this._refFonction!=null &&
              this._refFonction.equals(other.get_refFonction()))) &&
            this._dureeArchivage == other.get_dureeArchivage() &&
            this._destructionAutorisee == other.is_destructionAutorisee() &&
            this._etat == other.is_etat() &&
            ((this._commentaire==null && other.get_commentaire()==null) || 
             (this._commentaire!=null &&
              this._commentaire.equals(other.get_commentaire()))) &&
            this._archivable == other.is_archivable() &&
            this._archiveHisto == other.is_archiveHisto() &&
            this._pj == other.is_pj() &&
            ((this._type==null && other.get_type()==null) || 
             (this._type!=null &&
              this._type.equals(other.get_type()))) &&
            ((this._dlc==null && other.get_dlc()==null) || 
             (this._dlc!=null &&
              this._dlc.equals(other.get_dlc()))) &&
            ((this._responsabilite==null && other.get_responsabilite()==null) || 
             (this._responsabilite!=null &&
              this._responsabilite.equals(other.get_responsabilite()))) &&
            this._reserveCNM == other.is_reserveCNM() &&
            ((this._typePJ==null && other.get_typePJ()==null) || 
             (this._typePJ!=null &&
              this._typePJ.equals(other.get_typePJ())));
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
        _hashCode += get_idVersion();
        if (get_reference() != null) {
            _hashCode += get_reference().hashCode();
        }
        if (get_label() != null) {
            _hashCode += get_label().hashCode();
        }
        if (get_refProcessus() != null) {
            _hashCode += get_refProcessus().hashCode();
        }
        if (get_refSecteurActivite() != null) {
            _hashCode += get_refSecteurActivite().hashCode();
        }
        if (get_refActivite() != null) {
            _hashCode += get_refActivite().hashCode();
        }
        if (get_refFonction() != null) {
            _hashCode += get_refFonction().hashCode();
        }
        _hashCode += get_dureeArchivage();
        _hashCode += (is_destructionAutorisee() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (is_etat() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (get_commentaire() != null) {
            _hashCode += get_commentaire().hashCode();
        }
        _hashCode += (is_archivable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (is_archiveHisto() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (is_pj() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (get_type() != null) {
            _hashCode += get_type().hashCode();
        }
        if (get_dlc() != null) {
            _hashCode += get_dlc().hashCode();
        }
        if (get_responsabilite() != null) {
            _hashCode += get_responsabilite().hashCode();
        }
        _hashCode += (is_reserveCNM() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (get_typePJ() != null) {
            _hashCode += get_typePJ().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RNDTypeDocument.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTypeDocument"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_idVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_idVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("_refProcessus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_refProcessus"));
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
        elemField.setFieldName("_refActivite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_refActivite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_refFonction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_refFonction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_dureeArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_dureeArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_destructionAutorisee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_destructionAutorisee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_archivable");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_archivable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_archiveHisto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_archiveHisto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_pj");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_pj"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_dlc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_dlc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_responsabilite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_responsabilite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_reserveCNM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_reserveCNM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_typePJ");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_typePJ"));
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
