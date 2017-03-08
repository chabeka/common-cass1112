
/**
 * StockageUnitaireRequestType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:23:23 CEST)
 */

            
                package fr.cirtil.www.saeservice;
            

            /**
            *  StockageUnitaireRequestType bean class
            */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
        
        public  class StockageUnitaireRequestType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = stockageUnitaireRequestType
                Namespace URI = http://www.cirtil.fr/saeService
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for Metadonnees
                        */

                        
                                    protected fr.cirtil.www.saeservice.ListeMetadonneeType localMetadonnees ;
                                

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.ListeMetadonneeType
                           */
                           public  fr.cirtil.www.saeservice.ListeMetadonneeType getMetadonnees(){
                               return localMetadonnees;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param Metadonnees
                               */
                               public void setMetadonnees(fr.cirtil.www.saeservice.ListeMetadonneeType param){
                            
                                            this.localMetadonnees=param;
                                    

                               }
                            

                        /**
                        * field for StockageUnitaireRequestTypeChoice_type0
                        */

                        
                                    protected fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type0 localStockageUnitaireRequestTypeChoice_type0 ;
                                

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type0
                           */
                           public  fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type0 getStockageUnitaireRequestTypeChoice_type0(){
                               return localStockageUnitaireRequestTypeChoice_type0;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StockageUnitaireRequestTypeChoice_type0
                               */
                               public void setStockageUnitaireRequestTypeChoice_type0(fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type0 param){
                            
                                            this.localStockageUnitaireRequestTypeChoice_type0=param;
                                    

                               }
                            

                        /**
                        * field for StockageUnitaireRequestTypeChoice_type1
                        */

                        
                                    protected fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type1 localStockageUnitaireRequestTypeChoice_type1 ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localStockageUnitaireRequestTypeChoice_type1Tracker = false ;

                           public boolean isStockageUnitaireRequestTypeChoice_type1Specified(){
                               return localStockageUnitaireRequestTypeChoice_type1Tracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type1
                           */
                           public  fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type1 getStockageUnitaireRequestTypeChoice_type1(){
                               return localStockageUnitaireRequestTypeChoice_type1;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param StockageUnitaireRequestTypeChoice_type1
                               */
                               public void setStockageUnitaireRequestTypeChoice_type1(fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type1 param){
                            localStockageUnitaireRequestTypeChoice_type1Tracker = param != null;
                                   
                                            this.localStockageUnitaireRequestTypeChoice_type1=param;
                                    

                               }
                            

     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
               return factory.createOMElement(dataSource,parentQName);
            
        }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       javax.xml.stream.XMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               javax.xml.stream.XMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();
                    writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://www.cirtil.fr/saeService");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":stockageUnitaireRequestType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "stockageUnitaireRequestType",
                           xmlWriter);
                   }

               
                   }
               
                                            if (localMetadonnees==null){
                                                 throw new org.apache.axis2.databinding.ADBException("metadonnees cannot be null!!");
                                            }
                                           localMetadonnees.serialize(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","metadonnees"),
                                               xmlWriter);
                                        
                                            if (localStockageUnitaireRequestTypeChoice_type0==null){
                                                 throw new org.apache.axis2.databinding.ADBException("stockageUnitaireRequestTypeChoice_type0 cannot be null!!");
                                            }
                                           localStockageUnitaireRequestTypeChoice_type0.serialize(null,xmlWriter);
                                         if (localStockageUnitaireRequestTypeChoice_type1Tracker){
                                            if (localStockageUnitaireRequestTypeChoice_type1==null){
                                                 throw new org.apache.axis2.databinding.ADBException("stockageUnitaireRequestTypeChoice_type1 cannot be null!!");
                                            }
                                           localStockageUnitaireRequestTypeChoice_type1.serialize(null,xmlWriter);
                                        }
                    xmlWriter.writeEndElement();
               

        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://www.cirtil.fr/saeService")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
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
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

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
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

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
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();
                while (true) {
                    java.lang.String uri = nsContext.getNamespaceURI(prefix);
                    if (uri == null || uri.length() == 0) {
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
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "metadonnees"));
                            
                            
                                    if (localMetadonnees==null){
                                         throw new org.apache.axis2.databinding.ADBException("metadonnees cannot be null!!");
                                    }
                                    elementList.add(localMetadonnees);
                                
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "stockageUnitaireRequestTypeChoice_type0"));
                            
                            
                                    if (localStockageUnitaireRequestTypeChoice_type0==null){
                                         throw new org.apache.axis2.databinding.ADBException("stockageUnitaireRequestTypeChoice_type0 cannot be null!!");
                                    }
                                    elementList.add(localStockageUnitaireRequestTypeChoice_type0);
                                 if (localStockageUnitaireRequestTypeChoice_type1Tracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "stockageUnitaireRequestTypeChoice_type1"));
                            
                            
                                    if (localStockageUnitaireRequestTypeChoice_type1==null){
                                         throw new org.apache.axis2.databinding.ADBException("stockageUnitaireRequestTypeChoice_type1 cannot be null!!");
                                    }
                                    elementList.add(localStockageUnitaireRequestTypeChoice_type1);
                                }

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static StockageUnitaireRequestType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            StockageUnitaireRequestType object =
                new StockageUnitaireRequestType();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"stockageUnitaireRequestType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (StockageUnitaireRequestType)fr.cirtil.www.saeservice.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","metadonnees").equals(reader.getName())){
                                
                                                object.setMetadonnees(fr.cirtil.www.saeservice.ListeMetadonneeType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() ){
                                
                                                object.setStockageUnitaireRequestTypeChoice_type0(fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type0.Factory.parse(reader));
                                            
                              }  // End of if for expected property start element
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                        
                                         try{
                                    
                                    if (reader.isStartElement() ){
                                
                                                object.setStockageUnitaireRequestTypeChoice_type1(fr.cirtil.www.saeservice.StockageUnitaireRequestTypeChoice_type1.Factory.parse(reader));
                                            
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                
                                 } catch (java.lang.Exception e) {}
                              
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
    