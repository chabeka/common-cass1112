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
import org.codehaus.plexus.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import fr.urssaf.image.sae.storage.dfce.support.TracesDfceSupport;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@Service
@Qualifier("saeCopieService")
public class SAECopieServiceImpl implements SAECopieService {
  private static final Logger LOG = LoggerFactory
      .getLogger(SAECopieService.class);
  @Autowired
  private MetadataReferenceDAO referenceDAO;

  @Autowired
  @Qualifier("saeConsultationService")
  private SAEConsultationService consultation;

  @Autowired
  private SAECaptureService capture;

  @Autowired
  private TracesDfceSupport tracesSupport;

  @Override
  public UUID copie(final UUID idCopie, final List<UntypedMetadata> metadata)
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
    final AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
        .getContext().getAuthentication();
    final List<SaePrmd> saePrmds = token.getSaeDroits().get("copie");

    if (!token.getSaeDroits().containsKey("archivage_unitaire")) {
      token.getSaeDroits().put("archivage_unitaire", saePrmds);
    }
    if (!token.getSaeDroits().containsKey("consultation")) {
      token.getSaeDroits().put("consultation", saePrmds);
    }

    final String[] roles = new String[token.getAuthorities().size() + 2];
    int index = 0;
    for (final GrantedAuthority authory : token.getAuthorities()) {
      roles[index] = authory.getAuthority();
      index++;
    }
    roles[index] = "ROLE_archivage_unitaire";
    roles[++index] = "ROLE_consultation";

    final AuthenticationToken authentication = AuthenticationFactory
        .createAuthentication(
                              ((VIContenuExtrait) token.getPrincipal()).getIdUtilisateur(),
                              token.getPrincipal(), roles);

    AuthenticationContext.setAuthenticationToken(authentication);

    // Consultation du document depuis l'ID copie
    final Map<String, MetadataReference> map = referenceDAO
        .getArchivableMetadataReferences();

    // Construire List<String> comprenant toutes les keys de la maps
    final List<String> list = new ArrayList<>();
    for (final Map.Entry<String, MetadataReference> e : map.entrySet()) {
      list.add(e.getValue().getLongCode());
    }

    // Verification metadatas non specifiables

    if (metadata.get(0).getLongCode() != null) {
      int i = 0;
      for (final UntypedMetadata md : metadata) {
        for (final String str : list) {
          if (md.getLongCode().equals(str)) {
            i = 1;
          }
        }
        if (i == 0) {
          final String message = StringUtils
              .replace(
                       "L'une des métadonnées passées en paramètre n'existe pas ou n'est pas spécifiable '{0}'",
                       "{0}", md.getLongCode());
          throw new SAECopieServiceException(message);
        }
        i = 0;
      }
    }

    // Appelle du service de consultation
    final ConsultParams param = new ConsultParams(idCopie, list);
    UntypedDocument untypedDocument = new UntypedDocument();

    untypedDocument = consultation.consultation(param);
    if (untypedDocument == null) {
      return null;
    }

    // Modification et remplissage des nouvelles métadonnées
    final List<UntypedMetadata> fin = new ArrayList<>();
    Boolean bool = false;
    for (final UntypedMetadata md : untypedDocument.getUMetadatas()) {
      for (final UntypedMetadata md2 : metadata) {
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
    final Iterator<UntypedMetadata> it = fin.iterator();
    while (it.hasNext()) {
      final UntypedMetadata name = it.next();
      // Do something
      if (StringUtils.isEmpty(name.getValue())
          || name.getLongCode().startsWith("Domaine")
          || name.getLongCode().startsWith("IdGed")) {
        it.remove();
      }
    }

    byte[] data = null;
    try {
      data = IOUtils.toByteArray(untypedDocument.getContent()
                                 .getInputStream());
    } catch (final IOException e) {
      LOG.error(ExceptionUtils.getFullStackTrace(e));
    }

    ByteArrayDataSource bads = null;
    try {
      bads = new ByteArrayDataSource(data, "typeMIME");

    } catch (final IOException e1) {
      LOG.error(ExceptionUtils.getFullStackTrace(e1));
    }

    final DataHandler dataHandler = new DataHandler(bads);
    // Appelle au service d'archivageBinaire
    final CaptureResult res = capture.captureBinaire(fin, dataHandler,
                                                     untypedDocument.getFileName());


    tracesSupport.traceCopieDocumentDansDFCE(untypedDocument.getUuid(), res.getIdDoc());

    // Retourne l'ID du document copié
    return res.getIdDoc();
  }

}
