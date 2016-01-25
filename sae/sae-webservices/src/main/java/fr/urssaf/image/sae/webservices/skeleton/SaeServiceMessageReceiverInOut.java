/**
 * SaeServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1  Built on : Aug 31, 2011 (12:22:40 CEST)
 */
package fr.urssaf.image.sae.webservices.skeleton;

import org.apache.axis2.context.MessageContext;

/**
 * SaeServiceMessageReceiverInOut message receiver
 */

// CHECKSTYLE:OFF
@SuppressWarnings("all")
public class SaeServiceMessageReceiverInOut extends
      org.apache.axis2.receivers.AbstractInOutMessageReceiver {

   public void invokeBusinessLogic(
         org.apache.axis2.context.MessageContext msgContext,
         org.apache.axis2.context.MessageContext newMsgContext)
         throws org.apache.axis2.AxisFault {

      try {

         // get the implementation class for the Web Service
         Object obj = getTheImplementationObject(msgContext);

         SaeServiceSkeletonInterface skel = (SaeServiceSkeletonInterface) obj;
         // Out Envelop
         org.apache.axiom.soap.SOAPEnvelope envelope = null;
         // Find the axisOperation that has been set by the Dispatch phase.
         org.apache.axis2.description.AxisOperation op = msgContext
               .getOperationContext().getAxisOperation();
         if (op == null) {
            throw new org.apache.axis2.AxisFault(
                  "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
         }

         java.lang.String methodName;
         if ((op.getName() != null)
               && ((methodName = org.apache.axis2.util.JavaUtils
                     .xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)) {

            if ("archivageUnitairePJ".equals(methodName)) {

               fr.cirtil.www.saeservice.ArchivageUnitairePJResponse archivageUnitairePJResponse17 = null;
               fr.cirtil.www.saeservice.ArchivageUnitairePJ wrappedParam = (fr.cirtil.www.saeservice.ArchivageUnitairePJ) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ArchivageUnitairePJ.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               archivageUnitairePJResponse17 =

               skel.archivageUnitairePJSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     archivageUnitairePJResponse17, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "archivageUnitairePJ"));
            } else

            if ("recherche".equals(methodName)) {

               fr.cirtil.www.saeservice.RechercheResponse rechercheResponse19 = null;
               fr.cirtil.www.saeservice.Recherche wrappedParam = (fr.cirtil.www.saeservice.Recherche) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.Recherche.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               rechercheResponse19 =

               skel.rechercheSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     rechercheResponse19, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "recherche"));
            } else

            if ("ping".equals(methodName)) {

               fr.cirtil.www.saeservice.PingResponse pingResponse21 = null;
               fr.cirtil.www.saeservice.PingRequest wrappedParam = (fr.cirtil.www.saeservice.PingRequest) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.PingRequest.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               pingResponse21 =

               skel.ping(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     pingResponse21, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "ping"));
            } else

            if ("archivageUnitaire".equals(methodName)) {

               fr.cirtil.www.saeservice.ArchivageUnitaireResponse archivageUnitaireResponse23 = null;
               fr.cirtil.www.saeservice.ArchivageUnitaire wrappedParam = (fr.cirtil.www.saeservice.ArchivageUnitaire) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ArchivageUnitaire.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               archivageUnitaireResponse23 =

               skel.archivageUnitaireSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     archivageUnitaireResponse23, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "archivageUnitaire"));
            } else

            if ("pingSecure".equals(methodName)) {

               fr.cirtil.www.saeservice.PingSecureResponse pingSecureResponse25 = null;
               fr.cirtil.www.saeservice.PingSecureRequest wrappedParam = (fr.cirtil.www.saeservice.PingSecureRequest) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.PingSecureRequest.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               pingSecureResponse25 =

               skel.pingSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     pingSecureResponse25, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "pingSecure"));
            } else

            if ("consultation".equals(methodName)) {

               fr.cirtil.www.saeservice.ConsultationResponse consultationResponse27 = null;
               fr.cirtil.www.saeservice.Consultation wrappedParam = (fr.cirtil.www.saeservice.Consultation) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.Consultation.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               consultationResponse27 =

               skel.consultationSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     consultationResponse27, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "consultation"));
            } else

            if ("consultationMTOM".equals(methodName)) {

               fr.cirtil.www.saeservice.ConsultationMTOMResponse consultationMTOMResponse29 = null;
               fr.cirtil.www.saeservice.ConsultationMTOM wrappedParam = (fr.cirtil.www.saeservice.ConsultationMTOM) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ConsultationMTOM.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               consultationMTOMResponse29 =

               skel.consultationMTOMSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     consultationMTOMResponse29, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "consultationMTOM"));
            } else

            if ("archivageMasse".equals(methodName)) {

               fr.cirtil.www.saeservice.ArchivageMasseResponse archivageMasseResponse31 = null;
               fr.cirtil.www.saeservice.ArchivageMasse wrappedParam = (fr.cirtil.www.saeservice.ArchivageMasse) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ArchivageMasse.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               String callerIP = (String) msgContext
                     .getProperty(MessageContext.REMOTE_ADDR);

               archivageMasseResponse31 =

               skel.archivageMasseSecure(wrappedParam, callerIP);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     archivageMasseResponse31, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "archivageMasse"));

            } else

            if ("archivageMasseAvecHash".equals(methodName)) {

               fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse archivageMasseAvecHashResponse31 = null;
               fr.cirtil.www.saeservice.ArchivageMasseAvecHash wrappedParam = (fr.cirtil.www.saeservice.ArchivageMasseAvecHash) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ArchivageMasseAvecHash.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               String callerIP = (String) msgContext
                     .getProperty(MessageContext.REMOTE_ADDR);

               archivageMasseAvecHashResponse31 =

               skel.archivageMasseAvecHashSecure(wrappedParam, callerIP);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     archivageMasseAvecHashResponse31, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "archivageMasseAvecHash"));

            } else

            if ("modification".equals(methodName)) {

               fr.cirtil.www.saeservice.ModificationResponse modificationResponse31 = null;
               fr.cirtil.www.saeservice.Modification wrappedParam = (fr.cirtil.www.saeservice.Modification) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.Modification.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               modificationResponse31 =

               skel.modificationSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     modificationResponse31, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "modification"));

            } else

            if ("suppression".equals(methodName)) {

               fr.cirtil.www.saeservice.SuppressionResponse suppressionResponse31 = null;
               fr.cirtil.www.saeservice.Suppression wrappedParam = (fr.cirtil.www.saeservice.Suppression) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.Suppression.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               suppressionResponse31 =

               skel.suppressionSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     suppressionResponse31, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "suppression"));

            } else

            if ("recuperationMetadonnees".equals(methodName)) {

               fr.cirtil.www.saeservice.RecuperationMetadonneesResponse recuperationMetadonneesResponse31 = null;
               fr.cirtil.www.saeservice.RecuperationMetadonnees wrappedParam = (fr.cirtil.www.saeservice.RecuperationMetadonnees) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.RecuperationMetadonnees.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               recuperationMetadonneesResponse31 =

               skel.recuperationMetadonneesSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     recuperationMetadonneesResponse31, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "recuperationMetadonnees"));

            } else

            if ("transfert".equals(methodName)) {

               fr.cirtil.www.saeservice.TransfertResponse transfertResponse32 = null;
               fr.cirtil.www.saeservice.Transfert wrappedParam = (fr.cirtil.www.saeservice.Transfert) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.Transfert.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               transfertResponse32 =

               skel.transfertSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     transfertResponse32, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "transfert"));

            } else

            if ("consultationAffichable".equals(methodName)) {

               fr.cirtil.www.saeservice.ConsultationAffichableResponse consultationAffichableResponse32 = null;
               fr.cirtil.www.saeservice.ConsultationAffichable wrappedParam = (fr.cirtil.www.saeservice.ConsultationAffichable) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.ConsultationAffichable.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               consultationAffichableResponse32 =

               skel.consultationAffichableSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     consultationAffichableResponse32, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "consultationAffichable"));

            } else

            if ("rechercheNbRes".equals(methodName)) {

               fr.cirtil.www.saeservice.RechercheNbResResponse rechercheResponse33 = null;
               fr.cirtil.www.saeservice.RechercheNbRes wrappedParam = (fr.cirtil.www.saeservice.RechercheNbRes) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.RechercheNbRes.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               rechercheResponse33 = skel.rechercheNbResSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     rechercheResponse33, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService", "rechercheNbRes"));
            } else

            if ("rechercheParIterateur".equals(methodName)) {

               fr.cirtil.www.saeservice.RechercheParIterateurResponse rechercheResponse34 = null;
               fr.cirtil.www.saeservice.RechercheParIterateur wrappedParam = (fr.cirtil.www.saeservice.RechercheParIterateur) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.RechercheParIterateur.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               rechercheResponse34 = skel
                     .rechercheParIterateurSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     rechercheResponse34, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "rechercheResponse34"));
            } else

            if ("ajoutNote".equals(methodName)) {

               fr.cirtil.www.saeservice.AjoutNoteResponse ajoutNoteResponse35 = null;
               fr.cirtil.www.saeservice.AjoutNote wrappedParam = (fr.cirtil.www.saeservice.AjoutNote) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.AjoutNote.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               ajoutNoteResponse35 = skel.ajoutNoteSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     ajoutNoteResponse35, false, new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "ajoutNoteResponse35"));

            } else

            if ("stockageUnitaire".equals(methodName)) {

               fr.cirtil.www.saeservice.StockageUnitaireResponse stockageUnitaireResponse36 = null;
               fr.cirtil.www.saeservice.StockageUnitaire wrappedParam = (fr.cirtil.www.saeservice.StockageUnitaire) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.StockageUnitaire.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               stockageUnitaireResponse36 = skel
                     .stockageUnitaireSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     stockageUnitaireResponse36, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "stockageUnitaireResponse36"));

            } else 

            if ("getDocFormatOrigine".equals(methodName)) {

               fr.cirtil.www.saeservice.GetDocFormatOrigineResponse getDocFormatOrigineResponse37 = null;
               fr.cirtil.www.saeservice.GetDocFormatOrigine wrappedParam = (fr.cirtil.www.saeservice.GetDocFormatOrigine) fromOM(
                     msgContext.getEnvelope().getBody().getFirstElement(),
                     fr.cirtil.www.saeservice.GetDocFormatOrigine.class,
                     getEnvelopeNamespaces(msgContext.getEnvelope()));

               getDocFormatOrigineResponse37 = skel
                     .getDocFormatOrigineSecure(wrappedParam);

               envelope = toEnvelope(getSOAPFactory(msgContext),
                     getDocFormatOrigineResponse37, false,
                     new javax.xml.namespace.QName(
                           "http://www.cirtil.fr/saeService",
                           "getDocFormatOrigineResponse37"));

            } else {
               throw new java.lang.RuntimeException("method not found");
            }

            newMsgContext.setEnvelope(envelope);
         }
      } catch (java.lang.Exception e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   //
   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageUnitairePJ param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageUnitairePJ.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageUnitairePJResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageUnitairePJResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.Recherche param, boolean optimizeContent)
         throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(fr.cirtil.www.saeservice.Recherche.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.RechercheResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.RechercheResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.PingRequest param, boolean optimizeContent)
         throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.PingRequest.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.PingResponse param, boolean optimizeContent)
         throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.PingResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageUnitaire param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageUnitaire.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageUnitaireResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageUnitaireResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.PingSecureRequest param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.PingSecureRequest.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.PingSecureResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.PingSecureResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.Consultation param, boolean optimizeContent)
         throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.Consultation.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ConsultationResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ConsultationResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ConsultationMTOM param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ConsultationMTOM.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ConsultationMTOMResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ConsultationMTOMResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageMasse param, boolean optimizeContent)
         throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageMasse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.om.OMElement toOM(
         fr.cirtil.www.saeservice.ArchivageMasseResponse param,
         boolean optimizeContent) throws org.apache.axis2.AxisFault {

      try {
         return param.getOMElement(
               fr.cirtil.www.saeservice.ArchivageMasseResponse.MY_QNAME,
               org.apache.axiom.om.OMAbstractFactory.getOMFactory());
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }

   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ArchivageUnitairePJResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.ArchivageUnitairePJResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ArchivageUnitairePJResponse wraparchivageUnitairePJ() {
      fr.cirtil.www.saeservice.ArchivageUnitairePJResponse wrappedElement = new fr.cirtil.www.saeservice.ArchivageUnitairePJResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.RechercheResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.RechercheResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.RechercheResponse wraprecherche() {
      fr.cirtil.www.saeservice.RechercheResponse wrappedElement = new fr.cirtil.www.saeservice.RechercheResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.PingResponse param, boolean optimizeContent,
         javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.PingResponse.MY_QNAME, factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.PingResponse wrapPing() {
      fr.cirtil.www.saeservice.PingResponse wrappedElement = new fr.cirtil.www.saeservice.PingResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ArchivageUnitaireResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.ArchivageUnitaireResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ArchivageUnitaireResponse wraparchivageUnitaire() {
      fr.cirtil.www.saeservice.ArchivageUnitaireResponse wrappedElement = new fr.cirtil.www.saeservice.ArchivageUnitaireResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.PingSecureResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.PingSecureResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.PingSecureResponse wrapPingSecure() {
      fr.cirtil.www.saeservice.PingSecureResponse wrappedElement = new fr.cirtil.www.saeservice.PingSecureResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ConsultationResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.ConsultationResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ConsultationResponse wrapconsultation() {
      fr.cirtil.www.saeservice.ConsultationResponse wrappedElement = new fr.cirtil.www.saeservice.ConsultationResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ConsultationMTOMResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.ConsultationMTOMResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ConsultationMTOMResponse wrapconsultationMTOM() {
      fr.cirtil.www.saeservice.ConsultationMTOMResponse wrappedElement = new fr.cirtil.www.saeservice.ConsultationMTOMResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ArchivageMasseResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.ArchivageMasseResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ArchivageMasseResponse wraparchivageMasse() {
      fr.cirtil.www.saeservice.ArchivageMasseResponse wrappedElement = new fr.cirtil.www.saeservice.ArchivageMasseResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse wraparchivageMasseAvecHash() {
      fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse wrappedElement = new fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ModificationResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.ModificationResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ModificationResponse wrapModification() {
      fr.cirtil.www.saeservice.ModificationResponse wrappedElement = new fr.cirtil.www.saeservice.ModificationResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.SuppressionResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.SuppressionResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.SuppressionResponse wrapSuppression() {
      fr.cirtil.www.saeservice.SuppressionResponse wrappedElement = new fr.cirtil.www.saeservice.SuppressionResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.RecuperationMetadonneesResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.RecuperationMetadonneesResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.RecuperationMetadonneesResponse wrapRecuperationMetadonnees() {
      fr.cirtil.www.saeservice.RecuperationMetadonneesResponse wrappedElement = new fr.cirtil.www.saeservice.RecuperationMetadonneesResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.TransfertResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.TransfertResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.TransfertResponse wrapTransfert() {
      fr.cirtil.www.saeservice.TransfertResponse wrappedElement = new fr.cirtil.www.saeservice.TransfertResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.RechercheNbResResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.RechercheNbResResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.RechercheNbResResponse wrapRechercheNbRes() {
      fr.cirtil.www.saeservice.RechercheNbResResponse wrappedElement = new fr.cirtil.www.saeservice.RechercheNbResResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.RechercheParIterateurResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.RechercheParIterateurResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.RechercheParIterateurResponse wrapRechercheParIterateur() {
      fr.cirtil.www.saeservice.RechercheParIterateurResponse wrappedElement = new fr.cirtil.www.saeservice.RechercheParIterateurResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.ConsultationAffichableResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.ConsultationAffichableResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.ConsultationAffichableResponse wrapConsultationAffichable() {
      fr.cirtil.www.saeservice.ConsultationAffichableResponse wrappedElement = new fr.cirtil.www.saeservice.ConsultationAffichableResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.AjoutNoteResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope.getBody().addChild(
               param.getOMElement(
                     fr.cirtil.www.saeservice.AjoutNoteResponse.MY_QNAME,
                     factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.AjoutNoteResponse wrapAjoutNote() {
      fr.cirtil.www.saeservice.AjoutNoteResponse wrappedElement = new fr.cirtil.www.saeservice.AjoutNoteResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.StockageUnitaireResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.StockageUnitaireResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.StockageUnitaireResponse wrapStockageUnitaire() {
      fr.cirtil.www.saeservice.StockageUnitaireResponse wrappedElement = new fr.cirtil.www.saeservice.StockageUnitaireResponse();
      return wrappedElement;
   }

   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory,
         fr.cirtil.www.saeservice.GetDocFormatOrigineResponse param,
         boolean optimizeContent, javax.xml.namespace.QName methodQName)
         throws org.apache.axis2.AxisFault {
      try {
         org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
               .getDefaultEnvelope();

         emptyEnvelope
               .getBody()
               .addChild(
                     param.getOMElement(
                           fr.cirtil.www.saeservice.GetDocFormatOrigineResponse.MY_QNAME,
                           factory));

         return emptyEnvelope;
      } catch (org.apache.axis2.databinding.ADBException e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
   }

   private fr.cirtil.www.saeservice.GetDocFormatOrigineResponse wrapGetDocFormatOrigine() {
      fr.cirtil.www.saeservice.GetDocFormatOrigineResponse wrappedElement = new fr.cirtil.www.saeservice.GetDocFormatOrigineResponse();
      return wrappedElement;
   }

   /**
    * get the default envelope
    */
   private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
         org.apache.axiom.soap.SOAPFactory factory) {
      return factory.getDefaultEnvelope();
   }

   private java.lang.Object fromOM(org.apache.axiom.om.OMElement param,
         java.lang.Class type, java.util.Map extraNamespaces)
         throws org.apache.axis2.AxisFault {

      try {

         if (fr.cirtil.www.saeservice.ArchivageUnitairePJ.class.equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageUnitairePJ.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ArchivageUnitairePJResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageUnitairePJResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.Recherche.class.equals(type)) {

            return fr.cirtil.www.saeservice.Recherche.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.RechercheResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.RechercheResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.PingRequest.class.equals(type)) {

            return fr.cirtil.www.saeservice.PingRequest.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.PingResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.PingResponse.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ArchivageUnitaire.class.equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageUnitaire.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ArchivageUnitaireResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageUnitaireResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.PingSecureRequest.class.equals(type)) {

            return fr.cirtil.www.saeservice.PingSecureRequest.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.PingSecureResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.PingSecureResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.Consultation.class.equals(type)) {

            return fr.cirtil.www.saeservice.Consultation.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ConsultationResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.ConsultationResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ConsultationMTOM.class.equals(type)) {

            return fr.cirtil.www.saeservice.ConsultationMTOM.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ConsultationMTOMResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.ConsultationMTOMResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ArchivageMasse.class.equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageMasse.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }

         if (fr.cirtil.www.saeservice.ArchivageMasseResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageMasseResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.ArchivageMasseAvecHash.class.equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageMasseAvecHash.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.Modification.class.equals(type)) {

            return fr.cirtil.www.saeservice.Modification.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.ModificationResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.ModificationResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.Suppression.class.equals(type)) {

            return fr.cirtil.www.saeservice.Suppression.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.SuppressionResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.SuppressionResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RecuperationMetadonnees.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.RecuperationMetadonnees.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RecuperationMetadonneesResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.RecuperationMetadonneesResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.Transfert.class.equals(type)) {

            return fr.cirtil.www.saeservice.Transfert.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.TransfertResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.TransfertResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.ConsultationAffichable.class.equals(type)) {

            return fr.cirtil.www.saeservice.ConsultationAffichable.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.ConsultationAffichableResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.ConsultationAffichableResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RechercheNbRes.class.equals(type)) {

            return fr.cirtil.www.saeservice.RechercheNbRes.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RechercheNbResResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.RechercheNbResResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RechercheParIterateur.class.equals(type)) {

            return fr.cirtil.www.saeservice.RechercheParIterateur.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());

         }
         if (fr.cirtil.www.saeservice.RechercheParIterateurResponse.class
               .equals(type)) {

            return fr.cirtil.www.saeservice.RechercheParIterateurResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());
         }

         if (fr.cirtil.www.saeservice.AjoutNote.class.equals(type)) {

            return fr.cirtil.www.saeservice.AjoutNote.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());
         }

         if (fr.cirtil.www.saeservice.AjoutNoteResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.AjoutNoteResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());
         }
         
         if (fr.cirtil.www.saeservice.StockageUnitaire.class.equals(type)) {

            return fr.cirtil.www.saeservice.StockageUnitaire.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());
         }

         if (fr.cirtil.www.saeservice.StockageUnitaireResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.StockageUnitaireResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());
         }
         
         if (fr.cirtil.www.saeservice.GetDocFormatOrigine.class.equals(type)) {

            return fr.cirtil.www.saeservice.GetDocFormatOrigine.Factory.parse(param
                  .getXMLStreamReaderWithoutCaching());
         }

         if (fr.cirtil.www.saeservice.GetDocFormatOrigineResponse.class.equals(type)) {

            return fr.cirtil.www.saeservice.GetDocFormatOrigineResponse.Factory
                  .parse(param.getXMLStreamReaderWithoutCaching());
         }
         

      } catch (java.lang.Exception e) {
         throw org.apache.axis2.AxisFault.makeFault(e);
      }
      return null;
   }

   /**
    * A utility method that copies the namepaces from the SOAPEnvelope
    */
   private java.util.Map getEnvelopeNamespaces(
         org.apache.axiom.soap.SOAPEnvelope env) {
      java.util.Map returnMap = new java.util.HashMap();
      java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
      while (namespaceIterator.hasNext()) {
         org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
               .next();
         returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
      }
      return returnMap;
   }

   private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
      org.apache.axis2.AxisFault f;
      Throwable cause = e.getCause();
      if (cause != null) {
         f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
      } else {
         f = new org.apache.axis2.AxisFault(e.getMessage());
      }

      return f;
   }

}// end of class
