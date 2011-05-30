
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:19:26 CET)
 */

        
            package fr.cirtil.www.saeservice;
        
            /**
            *  ExtensionMapper class
            */
        
// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "archivageMasseRequestType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ArchivageMasseRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "ecdeUrlType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.EcdeUrlType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "objetNumeriqueType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ObjetNumeriqueType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "uuidType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.UuidType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "listeMetadonneeCodeType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ListeMetadonneeCodeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "requeteRechercheType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.RequeteRechercheType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "consultationResponseType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ConsultationResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "rechercheRequestType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.RechercheRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "resultatRechercheType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ResultatRechercheType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "listeMetadonneeType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ListeMetadonneeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "listeResultatRechercheType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ListeResultatRechercheType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "archivageMasseResponseType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ArchivageMasseResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "rechercheResponseType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.RechercheResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "archivageUnitaireRequestType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ArchivageUnitaireRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "archivageUnitaireResponseType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ArchivageUnitaireResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "metadonneeCodeType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.MetadonneeCodeType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "metadonneeValeurType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.MetadonneeValeurType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "consultationRequestType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.ConsultationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.cirtil.fr/saeService".equals(namespaceURI) &&
                  "metadonneeType".equals(typeName)){
                   
                            return  fr.cirtil.www.saeservice.MetadonneeType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    