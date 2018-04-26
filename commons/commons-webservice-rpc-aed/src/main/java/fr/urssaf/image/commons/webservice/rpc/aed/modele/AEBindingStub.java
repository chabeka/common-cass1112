/**
 * AEBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;

public class AEBindingStub extends org.apache.axis.client.Stub implements fr.urssaf.image.commons.webservice.rpc.aed.modele.AEPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[15];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ping");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "reponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("initLot");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idTransfert"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nomLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListePreDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.PreDocument[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nombreDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "isLotCree"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("declareNouvellesCopies");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "progTraitement"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "isDeclarationOK"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("gedDeportee");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idOrganisme"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentGed"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentGed"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGed[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "erreur"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getParametrageTransfert");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "typeTransfert"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "urlFTP"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "login"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "password"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "chemin"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idTransfert"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("dispatchLot");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsManquants"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsInconnus"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("synchroniseLot");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsManquants"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsInconnus"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("synchroniseGed");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsGedAED"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGedAED[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "erreur"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("correspondancesID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ArrayOfstring"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "erreur"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("synchroniseLotLIIni");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLotFils"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "typeDocument"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsManquants"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsInconnus"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("synchroniseLotLIFin");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLotFils"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "typeDocument"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocuments"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsManquants"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsInconnus"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "erreur"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("rejetDocuments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "IP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idLot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsRejetes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsRejetes"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentRejete[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "erreur"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("suppressionDocuments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "listeDocumentsSupprimes"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSupprime"), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSupprime[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "modeTest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode"), fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "erreur"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("archivageLot");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "lot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "LotAED"), fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "LotAED"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "lotComplete"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("synchronisationLot");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ip"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "lot"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "LotAED"), fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "LotAED"));
        oper.setReturnClass(fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "lotComplete"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[14] = oper;

    }

    public AEBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AEBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AEBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ArrayOfstring");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Document");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Document.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGed");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGed.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGedAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGedAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentRejete");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentRejete.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentSupprime");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSupprime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentSynchro");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Erreur");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "FichierAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.FichierAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Index.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "InstanceIndexationAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentAED[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentAED");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentGed");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGed[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGed");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocuments");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Document");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsGedAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGedAED[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentGedAED");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsRejetes");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentRejete[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentRejete");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSupprime");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSupprime[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentSupprime");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeDocumentsSynchro");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentSynchro");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeIndex");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeInstanceAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.InstanceIndexationAED[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "InstanceIndexationAED");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListeOperationAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "OperationAED");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "ListePreDocuments");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.PreDocument[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "PreDocument");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "LotAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Mode");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "OperationAED");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.OperationAED.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "PreDocument");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.commons.webservice.rpc.aed.modele.PreDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public java.lang.String ping() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#ping");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "ping"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void initLot(java.lang.String idTransfert, java.lang.String idLot, java.lang.String nomLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.PreDocument[] listeDocuments, java.lang.String nombreDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, javax.xml.rpc.holders.BooleanHolder isLotCree, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocuments2, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#initLot");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "initLot"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idTransfert, idLot, nomLot, listeDocuments, nombreDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                isLotCree.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName("", "isLotCree"))).booleanValue();
            } catch (java.lang.Exception _exception) {
                isLotCree.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "isLotCree")), boolean.class)).booleanValue();
            }
            try {
                listeDocuments2.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) _output.get(new javax.xml.namespace.QName("", "listeDocuments"));
            } catch (java.lang.Exception _exception) {
                listeDocuments2.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocuments")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean declareNouvellesCopies(fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[] listeDocuments, java.lang.String progTraitement, java.lang.String operation, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#declareNouvellesCopies");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "declareNouvellesCopies"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {listeDocuments, progTraitement, operation, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur gedDeportee(java.lang.String idOrganisme, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGed[] listeDocumentGed, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#gedDeportee");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "gedDeportee"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idOrganisme, listeDocumentGed, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void getParametrageTransfert(java.lang.String applicationSource, java.lang.String IP, javax.xml.rpc.holders.StringHolder typeTransfert, javax.xml.rpc.holders.StringHolder urlFTP, javax.xml.rpc.holders.StringHolder login, javax.xml.rpc.holders.StringHolder password, javax.xml.rpc.holders.StringHolder chemin, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur, javax.xml.rpc.holders.StringHolder idTransfert) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#getParametrageTransfert");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "getParametrageTransfert"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                typeTransfert.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "typeTransfert"));
            } catch (java.lang.Exception _exception) {
                typeTransfert.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "typeTransfert")), java.lang.String.class);
            }
            try {
                urlFTP.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "urlFTP"));
            } catch (java.lang.Exception _exception) {
                urlFTP.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "urlFTP")), java.lang.String.class);
            }
            try {
                login.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "login"));
            } catch (java.lang.Exception _exception) {
                login.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "login")), java.lang.String.class);
            }
            try {
                password.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "password"));
            } catch (java.lang.Exception _exception) {
                password.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "password")), java.lang.String.class);
            }
            try {
                chemin.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "chemin"));
            } catch (java.lang.Exception _exception) {
                chemin.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "chemin")), java.lang.String.class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
            try {
                idTransfert.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "idTransfert"));
            } catch (java.lang.Exception _exception) {
                idTransfert.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "idTransfert")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void dispatchLot(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#dispatchLot");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "dispatchLot"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP, idLot, listeDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class);
            }
            try {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus")), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void synchroniseLot(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#synchroniseLot");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "synchroniseLot"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP, idLot, listeDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class);
            }
            try {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus")), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur synchroniseGed(fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentGedAED[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#synchroniseGED");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "synchroniseGed"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {listeDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur correspondancesID(java.lang.String[] listeID, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#correspondancesID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "correspondancesID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {listeID, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void synchroniseLotLIIni(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, java.lang.String idLotFils, java.lang.String typeDocument, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#synchroniseLotLIIni");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "synchroniseLotLIIni"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP, idLot, idLotFils, typeDocument, listeDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class);
            }
            try {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus")), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void synchroniseLotLIFin(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, java.lang.String idLotFils, java.lang.String typeDocument, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[] listeDocuments, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsHolder listeDocumentsManquants, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ListeDocumentsSynchroHolder listeDocumentsInconnus, fr.urssaf.image.commons.webservice.rpc.aed.modele.holders.ErreurHolder erreur) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#synchroniseLotLIFin");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "synchroniseLotLIFin"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP, idLot, idLotFils, typeDocument, listeDocuments, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsManquants.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsManquants")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Document[].class);
            }
            try {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) _output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus"));
            } catch (java.lang.Exception _exception) {
                listeDocumentsInconnus.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "listeDocumentsInconnus")), fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSynchro[].class);
            }
            try {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _output.get(new javax.xml.namespace.QName("", "erreur"));
            } catch (java.lang.Exception _exception) {
                erreur.value = (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "erreur")), fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur rejetDocuments(java.lang.String applicationSource, java.lang.String IP, java.lang.String idLot, fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentRejete[] listeDocumentsRejetes) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#rejetDocuments");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "rejetDocuments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationSource, IP, idLot, listeDocumentsRejetes});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur suppressionDocuments(fr.urssaf.image.commons.webservice.rpc.aed.modele.DocumentSupprime[] listeDocumentsSupprimes, fr.urssaf.image.commons.webservice.rpc.aed.modele.Mode modeTest) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#suppressionDocuments");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "suppressionDocuments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {listeDocumentsSupprimes, modeTest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.Erreur.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED archivageLot(java.lang.String ip, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED lot) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#archivageLot");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "archivageLot"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ip, lot});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED synchronisationLot(java.lang.String ip, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED lot) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:AE:AEPortType#synchronisationLot");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE/binding", "synchronisationLot"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ip, lot});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.commons.webservice.rpc.aed.modele.LotAED.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
