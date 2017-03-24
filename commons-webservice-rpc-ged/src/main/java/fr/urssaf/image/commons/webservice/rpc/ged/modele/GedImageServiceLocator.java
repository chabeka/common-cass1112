/**
 * GedImageServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;

@SuppressWarnings("all")
public class GedImageServiceLocator extends org.apache.axis.client.Service implements fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageService {

    public GedImageServiceLocator() {
    }


    public GedImageServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GedImageServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GedImagePort
    private java.lang.String GedImagePort_address = "http://cer69imageint4.cer69.recouv:9021/services/gedimageserveur.php?WSDL";

    public java.lang.String getGedImagePortAddress() {
        return GedImagePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GedImagePortWSDDServiceName = "GedImagePort";

    public java.lang.String getGedImagePortWSDDServiceName() {
        return GedImagePortWSDDServiceName;
    }

    public void setGedImagePortWSDDServiceName(java.lang.String name) {
        GedImagePortWSDDServiceName = name;
    }

    public fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImagePortType getGedImagePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GedImagePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGedImagePort(endpoint);
    }

    public fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImagePortType getGedImagePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageBindingStub _stub = new fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageBindingStub(portAddress, this);
            _stub.setPortName(getGedImagePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGedImagePortEndpointAddress(java.lang.String address) {
        GedImagePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImagePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageBindingStub _stub = new fr.urssaf.image.commons.webservice.rpc.ged.modele.GedImageBindingStub(new java.net.URL(GedImagePort_address), this);
                _stub.setPortName(getGedImagePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("GedImagePort".equals(inputPortName)) {
            return getGedImagePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.wsdl", "GedImageService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.wsdl", "GedImagePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GedImagePort".equals(portName)) {
            setGedImagePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
