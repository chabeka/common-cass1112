/**
 * AEService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;

public interface AEService extends javax.xml.rpc.Service {
    public java.lang.String getAEPortAddress();

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType getAEPort() throws javax.xml.rpc.ServiceException;

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType getAEPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
