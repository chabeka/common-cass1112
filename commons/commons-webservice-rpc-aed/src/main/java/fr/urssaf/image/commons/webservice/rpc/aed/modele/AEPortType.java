/**
 * AEPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;

public interface AEPortType extends java.rmi.Remote {

    /**
     * Permet de tester la connexion aux services WEB.
     */
    public java.lang.String ping() throws java.rmi.RemoteException;

    /**
     * Initialisation du lot et recuperation du mode de transfert.
     */
    public void initLot(java.lang.String idTransfert, java.lang.String idLot, java.lang.String nomLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.PreDocument[] listeDocuments, java.lang.String nombreDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, javax.xml.rpc.holders.BooleanHolder isLotCree, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocuments2, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException;

    /**
     * Déclaration d'une nouvelle copie de document opérée dans une
     * application cliente SAEL.
     */
    public boolean declareNouvellesCopies(fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[] listeDocuments, java.lang.String progTraitement, java.lang.String operation, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException;

    /**
     * Traitement des documents provenant de la GED (GED deportee)
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur gedDeportee(java.lang.String idOrganisme, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGed[] listeDocumentGed, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException;

    /**
     * Recuperation du parametrage du transfert
     */
    public void getParametrageTransfert(java.lang.String applicationSource, java.lang.String IP, javax.xml.rpc.holders.StringHolder typeTransfert, javax.xml.rpc.holders.StringHolder urlFTP, javax.xml.rpc.holders.StringHolder login, javax.xml.rpc.holders.StringHolder password, javax.xml.rpc.holders.StringHolder chemin, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur, javax.xml.rpc.holders.StringHolder idTransfert) throws java.rmi.RemoteException;

    /**
     * Methode de dispatch et synchronisation des referentiels chaine
     * d'acquisition - AE
     */
    public void dispatchLot(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException;

    /**
     * Methode de synchronisation des referentiels chaine d'acquisition
     * - AE
     */
    public void synchroniseLot(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException;

    /**
     * Methode de synchronisation des lots GED a l archivage
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur synchroniseGed(fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGedAED[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException;

    /**
     * Methode de synchronisation des referentiels chaine d'acquisition
     * - AE
     */
    public void synchroniseLotLIIni(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, java.lang.String idLotFils, java.lang.String typeDocument, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException;

    /**
     * Methode de synchronisation des referentiels chaine d'acquisition
     * - AE
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur correspondancesID(java.lang.String[] listeID, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException;

    /**
     * Methode de synchronisation des referentiels chaine d'acquisition
     * - AE
     */
    public void synchroniseLotLIFin(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, java.lang.String idLotFils, java.lang.String typeDocument, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException;

    /**
     * Permet de rejeter des documents
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur rejetDocuments(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentRejete[] listeDocumentsRejetes) throws java.rmi.RemoteException;

    /**
     * Methode de declaration de la suppression d'une affaire WATT
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur suppressionDocuments(fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSupprime[] listeDocumentsSupprimes, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException;

    /**
     * Archivage d'un lot de GED
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED archivageLot(java.lang.String ip, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED lot) throws java.rmi.RemoteException;

    /**
     * Synchronisation d'un lot
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED synchronisationLot(java.lang.String ip, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED lot) throws java.rmi.RemoteException;
}
