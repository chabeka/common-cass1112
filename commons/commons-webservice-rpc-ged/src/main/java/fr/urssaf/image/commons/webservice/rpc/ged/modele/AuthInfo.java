/**
 * AuthInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;


/**
 * Infos d'autentification
 */
@SuppressWarnings("all")
public class AuthInfo  implements java.io.Serializable {
    private java.lang.String login;

    private java.lang.String password;

    private java.lang.String appli;

    private java.lang.String codeOrga;

    public AuthInfo() {
    }

    public AuthInfo(
           java.lang.String login,
           java.lang.String password,
           java.lang.String appli,
           java.lang.String codeOrga) {
           this.login = login;
           this.password = password;
           this.appli = appli;
           this.codeOrga = codeOrga;
    }


    /**
     * Gets the login value for this AuthInfo.
     * 
     * @return login
     */
    public java.lang.String getLogin() {
        return login;
    }


    /**
     * Sets the login value for this AuthInfo.
     * 
     * @param login
     */
    public void setLogin(java.lang.String login) {
        this.login = login;
    }


    /**
     * Gets the password value for this AuthInfo.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this AuthInfo.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the appli value for this AuthInfo.
     * 
     * @return appli
     */
    public java.lang.String getAppli() {
        return appli;
    }


    /**
     * Sets the appli value for this AuthInfo.
     * 
     * @param appli
     */
    public void setAppli(java.lang.String appli) {
        this.appli = appli;
    }


    /**
     * Gets the codeOrga value for this AuthInfo.
     * 
     * @return codeOrga
     */
    public java.lang.String getCodeOrga() {
        return codeOrga;
    }


    /**
     * Sets the codeOrga value for this AuthInfo.
     * 
     * @param codeOrga
     */
    public void setCodeOrga(java.lang.String codeOrga) {
        this.codeOrga = codeOrga;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AuthInfo)) return false;
        AuthInfo other = (AuthInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.login==null && other.getLogin()==null) || 
             (this.login!=null &&
              this.login.equals(other.getLogin()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.appli==null && other.getAppli()==null) || 
             (this.appli!=null &&
              this.appli.equals(other.getAppli()))) &&
            ((this.codeOrga==null && other.getCodeOrga()==null) || 
             (this.codeOrga!=null &&
              this.codeOrga.equals(other.getCodeOrga())));
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
        if (getLogin() != null) {
            _hashCode += getLogin().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getAppli() != null) {
            _hashCode += getAppli().hashCode();
        }
        if (getCodeOrga() != null) {
            _hashCode += getCodeOrga().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AuthInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.xsd1", "AuthInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("login");
        elemField.setXmlName(new javax.xml.namespace.QName("", "login"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("appli");
        elemField.setXmlName(new javax.xml.namespace.QName("", "appli"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeOrga");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeOrga"));
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
