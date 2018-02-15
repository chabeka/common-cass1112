/**
 * AEServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;

public class AEServiceLocator extends org.apache.axis.client.Service implements fr.urssaf.image.commons.webservice.rpc.aed.modele.AEService {

    public AEServiceLocator() {
    }


    public AEServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AEServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AEPort
    private java.lang.String AEPort_address = "https://cer69imageint4.cer69.recouv:9011/services/aeserveur.php";

    public java.lang.String getAEPortAddress() {
        return AEPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AEPortWSDDServiceName = "AEPort";

    public java.lang.String getAEPortWSDDServiceName() {
        return AEPortWSDDServiceName;
    }

    public void setAEPortWSDDServiceName(java.lang.String name) {
        AEPortWSDDServiceName = name;
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType getAEPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AEPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAEPort(endpoint);
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType getAEPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.urssaf.image.commons.webservice.rpc.aed.modele.AEBindingStub _stub = new fr.urssaf.image.commons.webservice.rpc.aed.modele.AEBindingStub(portAddress, this);
            _stub.setPortName(getAEPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAEPortEndpointAddress(java.lang.String address) {
        AEPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.urssaf.image.commons.webservice.rpc.aed.modele.AEBindingStub _stub = new fr.urssaf.image.commons.webservice.rpc.aed.modele.AEBindingStub(new java.net.URL(AEPort_address), this);
                _stub.setPortName(getAEPortWSDDServiceName());
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
        if ("AEPort".equals(inputPortName)) {
            return getAEPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.wsdl", "AEService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.wsdl", "AEPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AEPort".equals(portName)) {
            setAEPortEndpointAddress(address);
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
