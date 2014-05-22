package fr.urssaf.image.sae.utils;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;

public class MockFactoryBean {

   public static final UntypedDocument getUntypedDocumentMockData() {

      UntypedDocument doc = new UntypedDocument();

      doc.setFilePath("src/test/resources/doc/doc1.PDF");

      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      doc.setUMetadatas(metadatas);
      metadatas.add(new UntypedMetadata("SiteAcquisition", "CER69"));
      metadatas.add(new UntypedMetadata("Titre",
            "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      metadatas.add(new UntypedMetadata("DateCreation", "2012-01-01"));
      metadatas.add(new UntypedMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new UntypedMetadata("Hash",
            "A2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metadatas.add(new UntypedMetadata("TypeHash", "SHA-1"));
      metadatas.add(new UntypedMetadata("TracabilitePreArchivage", "P"));
      metadatas.add(new UntypedMetadata("ApplicationProductrice", "GED"));
      metadatas.add(new UntypedMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new UntypedMetadata("NbPages", "2"));
      metadatas.add(new UntypedMetadata("CodeOrganismeProprietaire", "UR030"));
      metadatas.add(new UntypedMetadata("CodeOrganismeGestionnaire", "UR030"));

      return doc;

   }

   public static final SAEDocument getSAEDocumentMockData() {

      SAEDocument doc = new SAEDocument();

      doc.setFilePath("src/test/resources/doc/doc1.PDF");

      List<SAEMetadata> metadatas = new ArrayList<SAEMetadata>();
      doc.setMetadatas(metadatas);
      metadatas.add(new SAEMetadata("SiteAcquisition", "CER69"));
      metadatas.add(new SAEMetadata("Titre",
            "NOTIFICATIONS DE REMBOURSEMENT du 41882050200023"));
      metadatas.add(new SAEMetadata("DateCreation", "2012-01-01")); // TODO
      metadatas.add(new SAEMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(new SAEMetadata("Hash",
            "A2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      metadatas.add(new SAEMetadata("TypeHash", "SHA-1"));
      metadatas.add(new SAEMetadata("TracabilitePreArchivage", "P"));
      metadatas.add(new SAEMetadata("ApplicationProductrice", "GED"));
      metadatas.add(new SAEMetadata("FormatFichier", "fmt/354"));
      metadatas.add(new SAEMetadata("NbPages", new Integer(2)));
      metadatas.add(new SAEMetadata("CodeOrganismeProprietaire", "UR030"));
      metadatas.add(new SAEMetadata("CodeOrganismeGestionnaire", "UR030"));

      return doc;

   }

}
