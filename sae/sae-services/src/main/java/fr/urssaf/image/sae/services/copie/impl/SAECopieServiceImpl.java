package fr.urssaf.image.sae.services.copie.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.capture.SAECaptureService;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.copie.SAECopieService;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@Service
@Qualifier("saeCopieService")
public class SAECopieServiceImpl implements SAECopieService {

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService consultation;

   @Autowired
   private SAECaptureService capture;

   @Override
   public UUID copie(UUID idCopie, List<UntypedMetadata> metadata)
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx, SAECaptureServiceEx,
         ReferentialRndException, UnknownCodeRndEx, RequiredStorageMetadataEx,
         InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx,
         DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx,
         RequiredArchivableMetadataEx, NotArchivableMetadataEx,
         UnknownHashCodeEx, EmptyFileNameEx, MetadataValueNotInDictionaryEx,
         UnknownFormatException, ValidationExceptionInvalidFile,
         UnexpectedDomainException, InvalidPagmsCombinaisonException,
         CaptureExistingUuuidException, ReferentialException,
         SAECopieServiceException, ArchiveInexistanteEx {
      // TODO Auto-generated method stub

      // Ajout des droits pour la consultation et l'archivage unitaire
      AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
            .getContext().getAuthentication();
      List<SaePrmd> saePrmds = token.getSaeDroits().get("copie");

      if (!token.getSaeDroits().containsKey("archivage_unitaire")) {
         token.getSaeDroits().put("archivage_unitaire", saePrmds);
      }
      if (!token.getSaeDroits().containsKey("consultation")) {
         token.getSaeDroits().put("consultation", saePrmds);
      }

      String[] roles = new String[token.getAuthorities().size() + 2];
      int index = 0;
      for (GrantedAuthority authory : token.getAuthorities()) {
         roles[index] = authory.getAuthority();
         index++;
      }
      roles[index] = "archivage_unitaire";
      roles[++index] = "consultation";

      AuthenticationToken authentication = AuthenticationFactory
            .createAuthentication(
                  ((VIContenuExtrait) token.getPrincipal()).getIdUtilisateur(),
                  (VIContenuExtrait) token.getPrincipal(), roles);

      AuthenticationContext.setAuthenticationToken(authentication);

      // Consultation du document depuis l'ID copie
      Map<String, MetadataReference> map = referenceDAO
            .getArchivableMetadataReferences();

      // Construire List<String> comprenant toutes les keys de la maps
      List<String> list = new ArrayList<String>();
      for (Map.Entry<String, MetadataReference> e : map.entrySet()) {
         list.add(e.getValue().getLongCode());
      }

      // Verification metadatas non specifiables
      int i = 0;
      for (UntypedMetadata md : metadata) {
         for (String str : list) {
            if (md.getLongCode().equals(str))
               i = 1;
         }
         if (i == 0) {
            String message = StringUtils.replace(
                  "L'une des métadonnées passées en paramètre n'existe pas ou n'est pas spécifiable '{0}'",
                  "{0}", md.getLongCode());
            throw new SAECopieServiceException(message);
         }
         i = 0;
      }

      // Appelle du service de consultation
      ConsultParams param = new ConsultParams(idCopie, list);
      UntypedDocument untypedDocument = new UntypedDocument();

      untypedDocument = consultation.consultation(param);
      if (untypedDocument == null) {
         return null;
      }

      // Modification et remplissage des nouvelles métadonnées
      List<UntypedMetadata> fin = new ArrayList<UntypedMetadata>();
      Boolean bool = false;
      for (UntypedMetadata md : untypedDocument.getUMetadatas()) {
         for (UntypedMetadata md2 : metadata) {
            if (md.getLongCode().equals(md2.getLongCode())) {
               fin.add(md2);
               bool = true;
            }
         }
         if (bool.equals(false)) {
            fin.add(md);
         }
         bool = false;
      }

      // suppression des meta vide dans la liste
      Iterator<UntypedMetadata> it = fin.iterator();
      while (it.hasNext()) {
         UntypedMetadata name = it.next();
         // Do something
         if (StringUtils.isEmpty(name.getValue())
               || name.getLongCode().startsWith("Domaine")
               || name.getLongCode().startsWith("IdGed"))
            it.remove();
      }

      byte[] data = null;
      try {
         data = IOUtils.toByteArray(untypedDocument.getContent()
               .getInputStream());
      } catch (IOException e) {
         e.printStackTrace();
      }

      ByteArrayDataSource bads = null;
      try {
         bads = new ByteArrayDataSource(data, "typeMIME");

      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }

      DataHandler dataHandler = new DataHandler(bads);
      // Appelle au service d'archivageBinaire
      CaptureResult res = capture.captureBinaire(fin, dataHandler,
            untypedDocument.getFileName());

      // Retourne l'ID du document copié
      return res.getIdDoc();
   }

}
