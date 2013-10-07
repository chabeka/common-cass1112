/**
 * InterfaceDuplicationLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class InterfaceDuplicationLocator extends org.apache.axis.client.Service implements fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplication {

    public InterfaceDuplicationLocator() {
    }


    public InterfaceDuplicationLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public InterfaceDuplicationLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for InterfaceDuplicationPort
    private java.lang.String InterfaceDuplicationPort_address = "http://cer69adrn.cer69.recouv:9007/services/duplication.php";

    public java.lang.String getInterfaceDuplicationPortAddress() {
        return InterfaceDuplicationPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InterfaceDuplicationPortWSDDServiceName = "InterfaceDuplicationPort";

    public java.lang.String getInterfaceDuplicationPortWSDDServiceName() {
        return InterfaceDuplicationPortWSDDServiceName;
    }

    public void setInterfaceDuplicationPortWSDDServiceName(java.lang.String name) {
        InterfaceDuplicationPortWSDDServiceName = name;
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationPort_PortType getInterfaceDuplicationPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InterfaceDuplicationPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInterfaceDuplicationPort(endpoint);
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationPort_PortType getInterfaceDuplicationPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationBindingStub _stub = new fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationBindingStub(portAddress, this);
            _stub.setPortName(getInterfaceDuplicationPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInterfaceDuplicationPortEndpointAddress(java.lang.String address) {
        InterfaceDuplicationPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationPort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationBindingStub _stub = new fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationBindingStub(new java.net.URL(InterfaceDuplicationPort_address), this);
                _stub.setPortName(getInterfaceDuplicationPortWSDDServiceName());
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
        if ("InterfaceDuplicationPort".equals(inputPortName)) {
            return getInterfaceDuplicationPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:InterfaceDuplication", "InterfaceDuplication");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:InterfaceDuplication", "InterfaceDuplicationPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("InterfaceDuplicationPort".equals(portName)) {
            setInterfaceDuplicationPortEndpointAddress(address);
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
