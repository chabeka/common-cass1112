/**
 * InterfaceDuplicationPort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

//CHECKSTYLE:OFF
@SuppressWarnings("all")
public interface InterfaceDuplicationPort_PortType extends java.rmi.Remote {

    /**
     * Recupere le dernier numero de la version
     */
    public java.lang.String[] getLastNumVersion() throws java.rmi.RemoteException;

    /**
     * Recupere la liste des numeros de version
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[] getListeNumVersion() throws java.rmi.RemoteException;

    /**
     * Cette fonction retourne une boite a coucou
     */
    public java.lang.String ping() throws java.rmi.RemoteException;

    /**
     * Cette fonction retourne la version courante et les correspondance
     * avec les codes temporaires
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion getVersion(java.lang.String nomVersion) throws java.rmi.RemoteException;

    /**
     * Report de l'installation reussie d'une version par le client
     */
    public void reportInstallationVersion(java.lang.String codeServeur, java.lang.String numVersion) throws java.rmi.RemoteException;

    /**
     * Obtient la liste des codes temporaires de la version et leur
     * correspondance en type de document RND
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[] getListeCodesTemporaires() throws java.rmi.RemoteException;
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] getListeTypesDocuments(java.lang.String nomVersion) throws java.rmi.RemoteException;

    /**
     * Obtient la liste des anciens codes temporaires et leur correspondance
     * dans la version
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] getListeCorrespondances(java.lang.String nomVersion) throws java.rmi.RemoteException;
}
