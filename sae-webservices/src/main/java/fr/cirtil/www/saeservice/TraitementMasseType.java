/**
 * TraitementMasseType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.7  Built on : Nov 20, 2017 (11:41:50 GMT)
 */
package fr.cirtil.www.saeservice;


/**
 *  TraitementMasseType bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class TraitementMasseType implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = traitementMasseType
       Namespace URI = http://www.cirtil.fr/saeService
       Namespace Prefix = ns1
     */

    /**
     * field for IdJob
     */
    protected fr.cirtil.www.saeservice.UuidType localIdJob;

    /**
     * field for Type
     */
    protected java.lang.String localType;

    /**
     * field for DateCreation
     */
    protected java.lang.String localDateCreation;

    /**
     * field for Etat
     */
    protected java.lang.String localEtat;

    /**
     * field for NombreDocuments
     */
    protected java.lang.String localNombreDocuments;

    /**
     * field for DateReservation
     */
    protected java.lang.String localDateReservation;

    /**
     * field for DateDebut
     */
    protected java.lang.String localDateDebut;

    /**
     * field for DateFin
     */
    protected java.lang.String localDateFin;

    /**
     * field for Message
     */
    protected java.lang.String localMessage;

    /**
     * field for ToCheckFlag
     */
    protected boolean localToCheckFlag;

    /**
     * field for ToCheckFlagRaison
     */
    protected java.lang.String localToCheckFlagRaison;

    /**
     * Auto generated getter method
     * @return fr.cirtil.www.saeservice.UuidType
     */
    public fr.cirtil.www.saeservice.UuidType getIdJob() {
        return localIdJob;
    }

    /**
     * Auto generated setter method
     * @param param IdJob
     */
    public void setIdJob(fr.cirtil.www.saeservice.UuidType param) {
        this.localIdJob = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getType() {
        return localType;
    }

    /**
     * Auto generated setter method
     * @param param Type
     */
    public void setType(java.lang.String param) {
        this.localType = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDateCreation() {
        return localDateCreation;
    }

    /**
     * Auto generated setter method
     * @param param DateCreation
     */
    public void setDateCreation(java.lang.String param) {
        this.localDateCreation = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getEtat() {
        return localEtat;
    }

    /**
     * Auto generated setter method
     * @param param Etat
     */
    public void setEtat(java.lang.String param) {
        this.localEtat = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getNombreDocuments() {
        return localNombreDocuments;
    }

    /**
     * Auto generated setter method
     * @param param NombreDocuments
     */
    public void setNombreDocuments(java.lang.String param) {
        this.localNombreDocuments = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDateReservation() {
        return localDateReservation;
    }

    /**
     * Auto generated setter method
     * @param param DateReservation
     */
    public void setDateReservation(java.lang.String param) {
        this.localDateReservation = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDateDebut() {
        return localDateDebut;
    }

    /**
     * Auto generated setter method
     * @param param DateDebut
     */
    public void setDateDebut(java.lang.String param) {
        this.localDateDebut = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getDateFin() {
        return localDateFin;
    }

    /**
     * Auto generated setter method
     * @param param DateFin
     */
    public void setDateFin(java.lang.String param) {
        this.localDateFin = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getMessage() {
        return localMessage;
    }

    /**
     * Auto generated setter method
     * @param param Message
     */
    public void setMessage(java.lang.String param) {
        this.localMessage = param;
    }

    /**
     * Auto generated getter method
     * @return boolean
     */
    public boolean getToCheckFlag() {
        return localToCheckFlag;
    }

    /**
     * Auto generated setter method
     * @param param ToCheckFlag
     */
    public void setToCheckFlag(boolean param) {
        this.localToCheckFlag = param;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getToCheckFlagRaison() {
        return localToCheckFlagRaison;
    }

    /**
     * Auto generated setter method
     * @param param ToCheckFlagRaison
     */
    public void setToCheckFlagRaison(java.lang.String param) {
        this.localToCheckFlagRaison = param;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
        final javax.xml.namespace.QName parentQName,
        final org.apache.axiom.om.OMFactory factory)
        throws org.apache.axis2.databinding.ADBException {
        return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(
                this, parentQName));
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://www.cirtil.fr/saeService");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":traitementMasseType", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "traitementMasseType", xmlWriter);
            }
        }

        if (localIdJob == null) {
            throw new org.apache.axis2.databinding.ADBException(
                "idJob cannot be null!!");
        }

        localIdJob.serialize(new javax.xml.namespace.QName(
                "http://www.cirtil.fr/saeService", "idJob"), xmlWriter);

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "type", xmlWriter);

        if (localType == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "type cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localType);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "dateCreation", xmlWriter);

        if (localDateCreation == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "dateCreation cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDateCreation);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "etat", xmlWriter);

        if (localEtat == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "etat cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localEtat);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "nombreDocuments", xmlWriter);

        if (localNombreDocuments == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "nombreDocuments cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localNombreDocuments);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "dateReservation", xmlWriter);

        if (localDateReservation == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "dateReservation cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDateReservation);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "dateDebut", xmlWriter);

        if (localDateDebut == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "dateDebut cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDateDebut);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "dateFin", xmlWriter);

        if (localDateFin == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "dateFin cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localDateFin);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "message", xmlWriter);

        if (localMessage == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "message cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localMessage);
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "toCheckFlag", xmlWriter);

        if (false) {
            throw new org.apache.axis2.databinding.ADBException(
                "toCheckFlag cannot be null!!");
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    localToCheckFlag));
        }

        xmlWriter.writeEndElement();

        namespace = "http://www.cirtil.fr/saeService";
        writeStartElement(null, namespace, "toCheckFlagRaison", xmlWriter);

        if (localToCheckFlagRaison == null) {
            // write the nil attribute
            throw new org.apache.axis2.databinding.ADBException(
                "toCheckFlagRaison cannot be null!!");
        } else {
            xmlWriter.writeCharacters(localToCheckFlagRaison);
        }

        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals("http://www.cirtil.fr/saeService")) {
            return "ns1";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix,
        java.lang.String namespace, java.lang.String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix,
        java.lang.String namespace, java.lang.String attName,
        java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
        } else {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
            xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace,
        java.lang.String attName, java.lang.String attValue,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace),
                namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace,
        java.lang.String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        java.lang.String attributeValue;

        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(attributePrefix, namespace, attName,
                attributeValue);
        }
    }

    /**
     *  method to handle Qnames
     */
    private void writeQName(javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);

            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" +
                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }

                namespaceURI = qnames[i].getNamespaceURI();

                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);

                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                     .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            qnames[i]));
                }
            }

            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);

                if ((uri == null) || (uri.length() == 0)) {
                    break;
                }

                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {
        private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Factory.class);

        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static TraitementMasseType parse(
            javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
            TraitementMasseType object = new TraitementMasseType();

            int event;
            javax.xml.namespace.QName currentQName = null;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                currentQName = reader.getName();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"traitementMasseType".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (TraitementMasseType) fr.cirtil.www.saeservice.ExtensionMapper.getTypeObject(nsUri,
                                type, reader);
                        }
                    }
                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "idJob").equals(
                            reader.getName())) {
                    object.setIdJob(fr.cirtil.www.saeservice.UuidType.Factory.parse(
                            reader));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "type").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "type" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "dateCreation").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "dateCreation" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDateCreation(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "etat").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "etat" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setEtat(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "nombreDocuments").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "nombreDocuments" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setNombreDocuments(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "dateReservation").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "dateReservation" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDateReservation(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "dateDebut").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "dateDebut" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDateDebut(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "dateFin").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "dateFin" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setDateFin(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "message").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "message" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setMessage(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService", "toCheckFlag").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "toCheckFlag" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setToCheckFlag(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://www.cirtil.fr/saeService",
                            "toCheckFlagRaison").equals(reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "toCheckFlagRaison" +
                            "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setToCheckFlagRaison(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    // 1 - A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // 2 - A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
