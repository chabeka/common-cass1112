/**
 * GedImagePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;

public interface GedImagePortType extends java.rmi.Remote {

    /**
     * Mise à jour des index d'un document.
     */
    public boolean updateIndexDocumentGED(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base, java.lang.String idDocument, fr.urssaf.image.commons.webservice.rpc.ged.modele.IndexValeur[] listeIndexValeur) throws java.rmi.RemoteException;

    /**
     * Mise à jour des index d'un document.
     */
    public java.lang.String moveDocumentGED(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String baseOrig, java.lang.String idDocument, java.lang.String baseDest, fr.urssaf.image.commons.webservice.rpc.ged.modele.IndexValeur[] listeIndexValeur) throws java.rmi.RemoteException;

    /**
     * Insertion d'un document.
     */
    public java.lang.String insertDocumentGED(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base, fr.urssaf.image.commons.webservice.rpc.ged.modele.IndexValeur[] listeIndexValeur, fr.urssaf.image.commons.webservice.rpc.ged.modele.FichierWS file) throws java.rmi.RemoteException;

    /**
     * Supprime un document
     */
    public boolean deleteDocumentGED(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base, java.lang.String idDocument) throws java.rmi.RemoteException;

    /**
     * Liste des type zone d'une base.
     */
    public fr.urssaf.image.commons.webservice.rpc.ged.modele.TypeZoneWS[] getTypesZonesBase(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base) throws java.rmi.RemoteException;

    /**
     * Liste des types zones et des catégories pour une base
     */
    public fr.urssaf.image.commons.webservice.rpc.ged.modele.CategorieTypeZoneWS[] getCategorieTypesZonesBase(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base) throws java.rmi.RemoteException;

    /**
     * Pong
     */
    public java.lang.String ping() throws java.rmi.RemoteException;

    /**
     * Liste des des dictionnaires pour une base et un type zone
     */
    public java.lang.String[] getDictionnaire(fr.urssaf.image.commons.webservice.rpc.ged.modele.AuthInfo login, java.lang.String base, java.lang.String typezone, java.lang.String masque) throws java.rmi.RemoteException;
}
