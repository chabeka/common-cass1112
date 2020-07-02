package fr.urssaf.image.parser_opencsv.application.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "job_instances")
public class JobEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer id;

   @Column(name = "job_uuid")
   private String jobUUid;

   @Column(name = "id_traitement")
   private String idTraitementMasse;

   @Column(name = "csv_file_name")
   private String csvFile;

   @Column(name = "ecde_url")
   private String ecdeUrl;

   @Column(name = "nbre_documents_initial")
   private int nbreDocumentsInitial;

   @Column(name = "nbre_documents_in_sommaire")
   private int nbreAjouteSommaire;

   @Column(name = "nombre_documents_in_ged")
   private int nombreDocumentsInGED;

   @Transient
   private String sourcePath;

   @Transient
   private String targetPath;

   public JobEntity(final String csvFile) {
      this();
      this.csvFile = csvFile;
   }

   public JobEntity(final String jobUUID, final String csvFile) {
      this(csvFile);
      jobUUid = jobUUID;
   }

   /**
    * 
    */
   public JobEntity() {
      super();
   }

   /**
    * @return the id
    */
   public Integer getId() {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId(final Integer id) {
      this.id = id;
   }

   /**
    * @return the idTraitementMasse
    */
   public String getIdTraitementMasse() {
      return idTraitementMasse;
   }

   /**
    * @param idTraitementMasse
    *           the idTraitementMasse to set
    */
   public void setIdTraitementMasse(final String idTraitementMasse) {
      this.idTraitementMasse = idTraitementMasse;
   }

   /**
    * @return the nbreDocumentsInitial
    */
   public int getNbreDocumentsInitial() {
      return nbreDocumentsInitial;
   }

   /**
    * @param nbreDocumentsInitial
    *           the nbreDocumentsInitial to set
    */
   public void setNbreDocumentsInitial(final int nbreDocumentsInitial) {
      this.nbreDocumentsInitial = nbreDocumentsInitial;
   }

   /**
    * @return the nombreDocumentsInGED
    */
   public int getNombreDocumentsInGED() {
      return nombreDocumentsInGED;
   }

   /**
    * @param nombreDocumentsInGED
    *           the nombreDocumentsInGED to set
    */
   public void setNombreDocumentsInGED(final int nombreDocumentsInGED) {
      this.nombreDocumentsInGED = nombreDocumentsInGED;
   }

   /**
    * @return the csvFile
    */
   public String getCsvFile() {
      return csvFile;
   }

   /**
    * @return the ecdeUrl
    */
   public String getEcdeUrl() {
      return ecdeUrl;
   }

   /**
    * @param ecdeUrl
    *           the ecdeUrl to set
    */
   public void setEcdeUrl(final String ecdeUrl) {
      this.ecdeUrl = ecdeUrl;
   }

   /**
    * @return the sourcePath
    */
   public String getSourcePath() {
      return sourcePath;
   }

   /**
    * @param sourcePath
    *           the sourcePath to set
    */
   public void setSourcePath(final String sourcePath) {
      this.sourcePath = sourcePath;
   }

   /**
    * @return the targetPath
    */
   public String getTargetPath() {
      return targetPath;
   }

   /**
    * @param targetPath
    *           the targetPath to set
    */
   public void setTargetPath(final String targetPath) {
      this.targetPath = targetPath;
   }

   /**
    * @return the nbreAjouteSommaire
    */
   public int getNbreAjouteSommaire() {
      return nbreAjouteSommaire;
   }

   /**
    * @param nbreAjouteSommaire
    *           the nbreAjouteSommaire to set
    */
   public void setNbreAjouteSommaire(final int nbreAjouteSommaire) {
      this.nbreAjouteSommaire = nbreAjouteSommaire;
   }

   /**
    * @return the jobUUid
    */
   public String getJobUUid() {
      return jobUUid;
   }

   /**
    * @param jobUUid
    *           the jobUUid to set
    */
   public void setJobUUid(final String jobUUid) {
      this.jobUUid = jobUUid;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "JobEntity [id=" + id + ", idTraitementMasse=" + idTraitementMasse + ", csvFile=" + csvFile + ", ecdeUrl=" + ecdeUrl + "]";
   }

}
