/**
 * InterfaceDuplicationBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class InterfaceDuplicationBindingStub extends org.apache.axis.client.Stub implements fr.urssaf.image.sae.rnd.ws.adrn.modele.InterfaceDuplicationPort_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[8];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getLastNumVersion");
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfstring"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getLastNumVersionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getListeNumVersion");
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfNumVersionDate"));
        oper.setReturnClass(fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getListeNumVersionResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ping");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "pingResponse"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nomVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTransVersion"));
        oper.setReturnClass(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("reportInstallationVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codeServeur"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getListeCodesTemporaires");
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfTransCodeTemporaire"));
        oper.setReturnClass(fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ListeCodesTemporaires"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getListeTypesDocuments");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nomVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDTypeDocument"));
        oper.setReturnClass(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "listeDocs"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getListeCorrespondances");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nomVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDCorrespondance"));
        oper.setReturnClass(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ListeCorrespondances"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

    }

    public InterfaceDuplicationBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public InterfaceDuplicationBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public InterfaceDuplicationBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfNumVersionDate");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "NumVersionDate");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDActivite");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDActivite");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDCorrespondance");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDCorrespondance");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDFonction");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDFonction");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDProcessus");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDProcessus[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDProcessus");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDSecteurActivite");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDSecteurActivite[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDSecteurActivite");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfRNDTypeDocument");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTypeDocument");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfstring");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "ArrayOfTransCodeTemporaire");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "TransCodeTemporaire");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "NumVersionDate");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDActivite");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDActivite.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDCorrespondance");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDFonction");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDFonction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDProcessus");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDProcessus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDSecteurActivite");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDSecteurActivite.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTransVersion");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTypeDocument");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDVersion");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:InterfaceDuplication", "TransCodeTemporaire");
            cachedSerQNames.add(qName);
            cls = fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire.class;
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

    public java.lang.String[] getLastNumVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:InterfaceDuplicationAction");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:http://cirtil.fr/RND", "getLastNumVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[] getListeNumVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:InterfaceDuplicationAction");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:http://cirtil.fr/RND", "getListeNumVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[]) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.sae.rnd.ws.adrn.modele.NumVersionDate[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String ping() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#ping");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:http://cirtil.fr/RND", "ping"));

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

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion getVersion(java.lang.String nomVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#getVersion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:InterfaceDuplication/binding", "getVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nomVersion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTransVersion.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void reportInstallationVersion(java.lang.String codeServeur, java.lang.String numVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#reportInstallationVersion");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:InterfaceDuplication/binding", "reportInstallationVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codeServeur, numVersion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[] getListeCodesTemporaires() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#getListeCodesTemporaires");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:InterfaceDuplication/binding", "getListeCodesTemporaires"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[]) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.sae.rnd.ws.adrn.modele.TransCodeTemporaire[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[] getListeTypesDocuments(java.lang.String nomVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#getListeTypesDocuments");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:InterfaceDuplication/binding", "getListeTypesDocuments"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nomVersion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[]) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDTypeDocument[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] getListeCorrespondances(java.lang.String nomVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("capeconnect:InterfaceDuplication:InterfaceDuplicationPort#getListeCorrespondances");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:InterfaceDuplication/binding", "getListeCorrespondances"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nomVersion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[]) org.apache.axis.utils.JavaUtils.convert(_resp, fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
