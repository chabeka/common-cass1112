
/**
 * FiltreType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:23:23 CEST)
 */

            
                package fr.cirtil.www.saeservice;
            

            /**
            *  FiltreType bean class
            */
// CHECKSTYLE:OFF
@SuppressWarnings("all")
        
        public  class FiltreType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = filtreType
                Namespace URI = http://www.cirtil.fr/saeService
                Namespace Prefix = ns1
                */
            

                        /**
                        * field for EqualFilter
                        */

                        
                                    protected fr.cirtil.www.saeservice.ListeMetadonneeType localEqualFilter ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localEqualFilterTracker = false ;

                           public boolean isEqualFilterSpecified(){
                               return localEqualFilterTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.ListeMetadonneeType
                           */
                           public  fr.cirtil.www.saeservice.ListeMetadonneeType getEqualFilter(){
                               return localEqualFilter;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param EqualFilter
                               */
                               public void setEqualFilter(fr.cirtil.www.saeservice.ListeMetadonneeType param){
                            localEqualFilterTracker = param != null;
                                   
                                            this.localEqualFilter=param;
                                    

                               }
                            

                        /**
                        * field for NotEqualFilter
                        */

                        
                                    protected fr.cirtil.www.saeservice.ListeMetadonneeType localNotEqualFilter ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localNotEqualFilterTracker = false ;

                           public boolean isNotEqualFilterSpecified(){
                               return localNotEqualFilterTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.ListeMetadonneeType
                           */
                           public  fr.cirtil.www.saeservice.ListeMetadonneeType getNotEqualFilter(){
                               return localNotEqualFilter;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param NotEqualFilter
                               */
                               public void setNotEqualFilter(fr.cirtil.www.saeservice.ListeMetadonneeType param){
                            localNotEqualFilterTracker = param != null;
                                   
                                            this.localNotEqualFilter=param;
                                    

                               }
                            

                        /**
                        * field for RangeFilter
                        */

                        
                                    protected fr.cirtil.www.saeservice.ListeRangeMetadonneeType localRangeFilter ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localRangeFilterTracker = false ;

                           public boolean isRangeFilterSpecified(){
                               return localRangeFilterTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.ListeRangeMetadonneeType
                           */
                           public  fr.cirtil.www.saeservice.ListeRangeMetadonneeType getRangeFilter(){
                               return localRangeFilter;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param RangeFilter
                               */
                               public void setRangeFilter(fr.cirtil.www.saeservice.ListeRangeMetadonneeType param){
                            localRangeFilterTracker = param != null;
                                   
                                            this.localRangeFilter=param;
                                    

                               }
                            

                        /**
                        * field for NotInRangeFilter
                        */

                        
                                    protected fr.cirtil.www.saeservice.ListeRangeMetadonneeType localNotInRangeFilter ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localNotInRangeFilterTracker = false ;

                           public boolean isNotInRangeFilterSpecified(){
                               return localNotInRangeFilterTracker;
                           }

                           

                           /**
                           * Auto generated getter method
                           * @return fr.cirtil.www.saeservice.ListeRangeMetadonneeType
                           */
                           public  fr.cirtil.www.saeservice.ListeRangeMetadonneeType getNotInRangeFilter(){
                               return localNotInRangeFilter;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param NotInRangeFilter
                               */
                               public void setNotInRangeFilter(fr.cirtil.www.saeservice.ListeRangeMetadonneeType param){
                            localNotInRangeFilterTracker = param != null;
                                   
                                            this.localNotInRangeFilter=param;
                                    

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
                           namespacePrefix+":filtreType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "filtreType",
                           xmlWriter);
                   }

               
                   }
                if (localEqualFilterTracker){
                                            if (localEqualFilter==null){
                                                 throw new org.apache.axis2.databinding.ADBException("equalFilter cannot be null!!");
                                            }
                                           localEqualFilter.serialize(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","equalFilter"),
                                               xmlWriter);
                                        } if (localNotEqualFilterTracker){
                                            if (localNotEqualFilter==null){
                                                 throw new org.apache.axis2.databinding.ADBException("notEqualFilter cannot be null!!");
                                            }
                                           localNotEqualFilter.serialize(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","notEqualFilter"),
                                               xmlWriter);
                                        } if (localRangeFilterTracker){
                                            if (localRangeFilter==null){
                                                 throw new org.apache.axis2.databinding.ADBException("rangeFilter cannot be null!!");
                                            }
                                           localRangeFilter.serialize(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","rangeFilter"),
                                               xmlWriter);
                                        } if (localNotInRangeFilterTracker){
                                            if (localNotInRangeFilter==null){
                                                 throw new org.apache.axis2.databinding.ADBException("notInRangeFilter cannot be null!!");
                                            }
                                           localNotInRangeFilter.serialize(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","notInRangeFilter"),
                                               xmlWriter);
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

                 if (localEqualFilterTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "equalFilter"));
                            
                            
                                    if (localEqualFilter==null){
                                         throw new org.apache.axis2.databinding.ADBException("equalFilter cannot be null!!");
                                    }
                                    elementList.add(localEqualFilter);
                                } if (localNotEqualFilterTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "notEqualFilter"));
                            
                            
                                    if (localNotEqualFilter==null){
                                         throw new org.apache.axis2.databinding.ADBException("notEqualFilter cannot be null!!");
                                    }
                                    elementList.add(localNotEqualFilter);
                                } if (localRangeFilterTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "rangeFilter"));
                            
                            
                                    if (localRangeFilter==null){
                                         throw new org.apache.axis2.databinding.ADBException("rangeFilter cannot be null!!");
                                    }
                                    elementList.add(localRangeFilter);
                                } if (localNotInRangeFilterTracker){
                            elementList.add(new javax.xml.namespace.QName("http://www.cirtil.fr/saeService",
                                                                      "notInRangeFilter"));
                            
                            
                                    if (localNotInRangeFilter==null){
                                         throw new org.apache.axis2.databinding.ADBException("notInRangeFilter cannot be null!!");
                                    }
                                    elementList.add(localNotInRangeFilter);
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
        public static FiltreType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            FiltreType object =
                new FiltreType();

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
                    
                            if (!"filtreType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (FiltreType)fr.cirtil.www.saeservice.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                
                    
                    reader.next();
                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","equalFilter").equals(reader.getName())){
                                
                                                object.setEqualFilter(fr.cirtil.www.saeservice.ListeMetadonneeType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","notEqualFilter").equals(reader.getName())){
                                
                                                object.setNotEqualFilter(fr.cirtil.www.saeservice.ListeMetadonneeType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","rangeFilter").equals(reader.getName())){
                                
                                                object.setRangeFilter(fr.cirtil.www.saeservice.ListeRangeMetadonneeType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://www.cirtil.fr/saeService","notInRangeFilter").equals(reader.getName())){
                                
                                                object.setNotInRangeFilter(fr.cirtil.www.saeservice.ListeRangeMetadonneeType.Factory.parse(reader));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                    }
                                  
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
           
    