package fr.urssaf.image.sae.test.divers.pile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import me.prettyprint.hector.api.Keyspace;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.pile.travaux.service.JobQueueService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-local.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-givn-gns.xml" })
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-test-cspp-gns.xml" })
public class PileTravauxTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(PileTravauxTest.class);

   @Autowired
   private JobLectureService jobLectureService;
   
   @Autowired
   private JobQueueService jobQueueService;
   
   @Autowired
   private Keyspace keyspace;
   
   @Test
   public void getStatFailureJobs() {
      Map<String, List<JobRequest>> groupByCnp = new HashMap<String, List<JobRequest>>();
      int nbErreursTotal = 0;
      int nbErreursFiltre = 0;
      long nbDocsNonArchive = 0;
      long nbTraitementRelance = 0;
      Date dateMin = new GregorianCalendar(2015, 3, 9, 14, 0, 0).getTime();
      Date dateMax = new GregorianCalendar().getTime();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 500000);
      for (JobRequest job : jobs) {
         if (job.getState() == JobState.FAILURE && isDateBetween(job, dateMin, dateMax)) {
            // Recupere le cnp
            String cnp = null;
            String urlEcde = getUrlEcde(job);
            if (urlEcde.contains("cnp31")) {
               cnp = "cnp31";
            } else if (urlEcde.contains("cnp69")) {
               cnp = "cnp69";
            } else if (urlEcde.contains("cnp75")) {
               cnp = "cnp75";
            } else {
               // dans ce cas, on est en environnement autre que la production
               cnp = "nonprod";
            }
            List<JobRequest> subListByCnp = null;
            if (!groupByCnp.containsKey(cnp)) {
               subListByCnp = new ArrayList<JobRequest>();
            } else {
               subListByCnp = groupByCnp.get(cnp);
            }
            subListByCnp.add(job);
            groupByCnp.put(cnp, subListByCnp);
            nbErreursFiltre++;
            if (job.getDocCount() != null) {
               nbDocsNonArchive += job.getDocCount();
            } 
            if (isTraitementRelance(urlEcde, jobs)) {
               nbTraitementRelance++;
            } else {
               LOGGER.debug("Traitement non relancé : {} ou toujours en erreur ({} docs non archivés, répertoire ecde : {})", new Object[] { job.getIdJob().toString(), job.getDocCount(), urlEcde});
            }
         } 
         
         if (job.getState() == JobState.FAILURE) {
            nbErreursTotal++;
         }
      }
      LOGGER.info("{} erreurs au total sur {} jobs", new Object[] { nbErreursTotal, jobs.size()});
      LOGGER.info("{} erreurs comprise entre le {} et le {} ({} docs non archivés)", new Object[] { nbErreursFiltre, formatter.format(dateMin), formatter.format(dateMax), nbDocsNonArchive});
      Iterator<Entry<String, List<JobRequest>>> iterateur = groupByCnp.entrySet().iterator();
      while (iterateur.hasNext()) {
         Entry<String, List<JobRequest>> entry = iterateur.next();
         LOGGER.info("{} : {} traitements en erreur -> {} docs non archivés", new Object[] { entry.getKey() , entry.getValue().size(), getNbDocsParCnp(entry.getValue())});
      }
      if (nbTraitementRelance > 0) {
         LOGGER.info("{} traitements relancés avec succès: reste {} traitements en erreurs", new Object[] { nbTraitementRelance, nbErreursFiltre - nbTraitementRelance});
      }
   }

   private String getUrlEcde(JobRequest job) {
      String urlEcde = "";
      if (job.getJobParameters() != null && job.getJobParameters().get("ecdeUrl") != null) {
         urlEcde = job.getJobParameters().get("ecdeUrl");
      } else if (job.getParameters() != null) {
         urlEcde = job.getParameters();
      }
      return urlEcde;
   }
   
   private long getNbDocsParCnp(List<JobRequest> jobs) {
      long nbDocs = 0;
      for (JobRequest job : jobs) {
         if (job.getDocCount() != null) {
            nbDocs += job.getDocCount();
         }
      }
      return nbDocs;
   }
   
   private boolean isDateBetween(JobRequest job, Date dateMin, Date dateMax) {
      return job.getCreationDate().after(dateMin) && job.getCreationDate().before(dateMax);
   }
   
   private boolean isTraitementRelance(String urlEcde, List<JobRequest> jobs) {
      boolean traitementRelance = false;
      for (JobRequest job : jobs) {
         if (job.getState() == JobState.SUCCESS && getUrlEcde(job).equals(urlEcde)) {
            traitementRelance = true;
            break;
         }
      }
      return traitementRelance;
   }
   
   @Test
   @Ignore
   public void getStatutJobByUrlEcde() {
      
      JobRequest jobTrouve = null;
      String urlEcde = "ecde://cnp31ecde.cer31.recouv/attest_002/20150810/34690/sommaire.xml";
      
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 200000);
      for (JobRequest job : jobs) {
         //LOGGER.debug("Job {}", getUrlEcde(job));
         if (getUrlEcde(job).equals(urlEcde)) {
            jobTrouve = job;
            break;
         }
      }
      if (jobTrouve != null) {
         LOGGER.info("Le traitement {} ({}) est a l'etat {}", new Object[] { urlEcde, jobTrouve.getIdJob().toString(), jobTrouve.getState()});
      } else {
         LOGGER.info("Le traitement {} n'existe pas cote sae", new Object[] { urlEcde });
      }
   }
   
   @Test
   public void getStatutJobByUrlEcde2() {
      
      List<String> listUrls = new ArrayList<String>();
      
      String urlEcde = "ecde://cnp31ecde.cer31.recouv/attest_002/20160624";
      
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 200000);
      for (JobRequest job : jobs) {
         //LOGGER.debug("Job {}", getUrlEcde(job));
         //if (getUrlEcde(job).startsWith(urlEcde) && job.getState() == JobState.CREATED) {
         if (getUrlEcde(job).startsWith(urlEcde) && job.getState() == JobState.CREATED && job.getDocCount().intValue() == 1) {
            if (!listUrls.contains(getUrlEcde(job))) {
               LOGGER.info("{} - {}", getUrlEcde(job), job.getVi().getPagms());
               listUrls.add(getUrlEcde(job));
               //jobQueueService.deleteJob(job.getIdJob());
            }
         }
      }
      /*if (jobTrouve != null) {
         LOGGER.info("Le traitement {} ({}) est a l'etat {}", new Object[] { urlEcde, jobTrouve.getIdJob().toString(), jobTrouve.getState()});
      } else {
         LOGGER.info("Le traitement {} n'existe pas cote sae", new Object[] { urlEcde });
      }*/
   }
   
   @Test
   //@Ignore
   public void getStatutListJobsByUrlEcde() {
      
      /*String[] numJobs = new String[] {
            "1117",
            "1118",
            "1108",
            "1109",
            "1115",
            "1113",
            "1119",
            "1104",
            "1107",
            "1114",
            "1120",
            "1103",
            "1101",
            "1105",
            "1111",
            "1116",
            "1112"
      };*/
      
      String[] numJobs = new String[] {
            "320602",
            "319475",
            "320082",
            "320691",
            "320710",
            "319495",
            "319902",
            "320654",
            "320089",
            "320085",
            "319894",
            "320573",
            "320692",
            "320665",
            "320655",
            "320714",
            "319473",
            "320615",
            "319906",
            "319899",
            "320575",
            "320653",
            "319477",
            "319895",
            "320574",
            "319482",
            "320103",
            "319481",
            "320099",
            "319914",
            "320084",
            "320651",
            "320682",
            "319476",
            "320646",
            "320647",
            "320245",
            "319006",
            "320650",
            "320698",
            "319487",
            "320663",
            "320112",
            "320096",
            "320087",
            "320648",
            "320101",
            "320090",
            "320613",
            "320645",
            "320616",
            "319480",
            "320097",
            "320265",
            "320686",
            "320110",
            "320094",
            "320098",
            "320652",
            "320701",
            "320105",
            "319479",
            "319009",
            "320086",
            "320088",
            "320689",
            "319905",
            "319913",
            "320095",
            "320660",
            "319911",
            "319904",
            "320666",
            "320108",
      };
      
      List<JobRequest> jobsTrouve = null;
      String urlEcde = "ecde://cnp31ecde.cer31.recouv/attest_002/20160607/";
      
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 170000);
      for (String numJob : numJobs) {
         jobsTrouve = new ArrayList<JobRequest>();
         String urlComplete = urlEcde + numJob + "/sommaire.xml";
         for (JobRequest job : jobs) {
            if (getUrlEcde(job).equals(urlComplete)) {
               jobsTrouve.add(job);
            }
         }
         if (jobsTrouve != null && !jobsTrouve.isEmpty()) {
            LOGGER.info("Le traitement {} ({}) est a l'etat {}", new Object[] { urlComplete, getIdsJob(jobsTrouve), getStates(jobsTrouve)});
         } else {
            LOGGER.info("Le traitement {} n'existe pas cote sae", new Object[] { urlComplete });
         }
         //Assert.assertTrue(jobTrouve != null);
         //Assert.assertEquals(jobTrouve.getState(), JobState.SUCCESS);
      }
   }
   
   private String getIdsJob(List<JobRequest> jobs) {

      StringBuffer buffer = new StringBuffer();
      
      // tri la liste par date de demarrage
      Collections.sort(jobs, new Comparator<JobRequest>() {
         @Override
         public int compare(JobRequest o1, JobRequest o2) {
            Date dateJob1;
            Date dateJob2;
            if (o1.getEndingDate() != null) {
               dateJob1 = o1.getEndingDate();
            } else if (o1.getStartingDate() != null) {
               dateJob1 = o1.getStartingDate();
            } else if (o1.getReservationDate() != null) {
               dateJob1 = o1.getReservationDate();
            } else if (o1.getCreationDate() != null) {
               dateJob1 = o1.getCreationDate();
            } else {
               dateJob1 = null;
            }
            if (o2.getEndingDate() != null) {
               dateJob2 = o2.getEndingDate();
            } else if (o2.getStartingDate() != null) {
               dateJob2 = o2.getStartingDate();
            } else if (o2.getReservationDate() != null) {
               dateJob2 = o2.getReservationDate();
            } else if (o1.getCreationDate() != null) {
               dateJob2 = o2.getCreationDate();
            } else {
               dateJob2 = null;
            }
            return dateJob1.compareTo(dateJob2);
         }
      });
      
      boolean firstJob = true;
      for (JobRequest job : jobs) {
         if (!firstJob) {
            buffer.append(" -> ");
         }
         buffer.append(job.getIdJob().toString());
         firstJob = false;
      }
      return buffer.toString();
   }
   
   private String getStates(List<JobRequest> jobs) {

      StringBuffer buffer = new StringBuffer();
      
      // tri la liste par date de demarrage
      Collections.sort(jobs, new Comparator<JobRequest>() {
         @Override
         public int compare(JobRequest o1, JobRequest o2) {
            Date dateJob1;
            Date dateJob2;
            if (o1.getEndingDate() != null) {
               dateJob1 = o1.getEndingDate();
            } else if (o1.getStartingDate() != null) {
               dateJob1 = o1.getStartingDate();
            } else if (o1.getReservationDate() != null) {
               dateJob1 = o1.getReservationDate();
            } else if (o1.getCreationDate() != null) {
               dateJob1 = o1.getCreationDate();
            } else {
               dateJob1 = null;
            }
            if (o2.getEndingDate() != null) {
               dateJob2 = o2.getEndingDate();
            } else if (o2.getStartingDate() != null) {
               dateJob2 = o2.getStartingDate();
            } else if (o2.getReservationDate() != null) {
               dateJob2 = o2.getReservationDate();
            } else if (o1.getCreationDate() != null) {
               dateJob2 = o2.getCreationDate();
            } else {
               dateJob2 = null;
            }
            return dateJob1.compareTo(dateJob2);
         }
      });
      
      boolean firstJob = true;
      for (JobRequest job : jobs) {
         if (!firstJob) {
            buffer.append(" -> ");
         }
         buffer.append(job.getState().toString());
         firstJob = false;
      }
      return buffer.toString();
   }
   
   @Test
   //@Ignore
   public void getDoubleArchivage() throws IOException {
      Map<String, List<JobRequest>> groupByUrlEcde = new TreeMap<String, List<JobRequest>>();
      Date dateMin = new DateTime().withDate(2016, 10, 12).withTime(0, 0, 0, 0).toDate();
      Date dateMax = new DateTime().withDate(2016, 10, 12).withTime(23, 59, 59, 0).toDate();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      Date dateMinRecuperee = null;
      Date dateMaxRecuperee = null;
      
      LOGGER.info("Récupération des jobs dans la pile des travaux");
      // recupere l'ensemble des jobs
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 100000);
      LOGGER.info("{} jobs récupérés", jobs.size());
      for (JobRequest job : jobs) {
         // test si les jobs sont dans la plage de date
         if (isDateBetween(job, dateMin, dateMax)) {
            List<JobRequest> subListByUrlEcde = null;
            String urlEcde = getUrlEcde(job);
            if (!groupByUrlEcde.containsKey(urlEcde)) {
               subListByUrlEcde = new ArrayList<JobRequest>();
            } else {
               subListByUrlEcde = groupByUrlEcde.get(urlEcde);
            }
            subListByUrlEcde.add(job);
            groupByUrlEcde.put(urlEcde, subListByUrlEcde);
         }
         if (dateMinRecuperee == null || dateMinRecuperee.after(job.getCreationDate())) {
            dateMinRecuperee = job.getCreationDate();
         }
         if (dateMaxRecuperee == null || dateMaxRecuperee.before(job.getCreationDate())) {
            dateMaxRecuperee = job.getCreationDate();
         }
      }
      LOGGER.info("jobs récupérés de {} à {}", new Object[] { formatter.format(dateMinRecuperee), formatter.format(dateMaxRecuperee)});
      
      FileOutputStream stream = new FileOutputStream("c:\\divers\\jobsDoublon.txt");
      
      Iterator<Entry<String, List<JobRequest>>> iterateur = groupByUrlEcde.entrySet().iterator();
      int compteur = 0;
      int compteurTraitementASupprimer = 0;
      int compteurDocASupprimer = 0;
      while (iterateur.hasNext()) {
         Entry<String, List<JobRequest>> entry = iterateur.next();
         if (entry.getValue().size() > 1 && isJobAlreadySuccessfull(entry.getValue())) {
            LOGGER.info("{} : traitements lancés {} fois", new Object[] { entry.getKey() , entry.getValue().size()});
            StringBuffer buffer = new StringBuffer();
            buffer.append(entry.getKey());
            buffer.append(" : \n");
            stream.write(buffer.toString().getBytes());
            
            // Trie de la liste
            Collections.sort(entry.getValue(), new Comparator<JobRequest>() {
               @Override
               public int compare(JobRequest o1, JobRequest o2) {
                  return o1.getCreationDate().compareTo(o2.getCreationDate());
               }
            });
            
            boolean oneSuccess = false;
            
            for (JobRequest job : entry.getValue()) {
               //LOGGER.info("    {} -> {} -> {} docs (lancé à {})", new Object[] { job.getIdJob(), job.getState(), job.getDocCount(), formatter.format(job.getStartingDate()) });
               StringBuffer bufferJob = new StringBuffer();
               bufferJob.append(job.getIdJob());
               bufferJob.append(";");
               bufferJob.append(job.getState());
               bufferJob.append(";");
               bufferJob.append(job.getDocCount());
               bufferJob.append(";");
               bufferJob.append(formatter.format(job.getCreationDate()));
               bufferJob.append(";");
               bufferJob.append(formatter.format(job.getStartingDate()));
               bufferJob.append("\n");
               stream.write(bufferJob.toString().getBytes());
               if (job.getState() == JobState.SUCCESS && oneSuccess == false) {
                  oneSuccess = true;
               } else if (job.getState() == JobState.SUCCESS && oneSuccess == true) {
                  compteurTraitementASupprimer++;
                  compteurDocASupprimer += job.getDocCount();
               }
            }
            compteur++;
         }
      }
      stream.close();
      LOGGER.info("Soit {} traitements plusieurs fois du {} au {}", new Object[] { compteur, formatter.format(dateMin), formatter.format(dateMax)});
      LOGGER.info("Soit {} traitements a supprimer correspondant à {} documents", new Object[] { compteurTraitementASupprimer, compteurDocASupprimer });
   }
   
   @Test
   public void getCountSmallJobMasse() {
      
      final long PARAM_NB_DOCS_MAX = 5;
      Date dateMin = new DateTime().withDate(2016, 10, 11).withTime(18, 0, 0, 0).toDate();
      Date dateMax = new DateTime().withDate(2016, 10, 12).withTime(8, 0, 0, 0).toDate();
      String cnpRecherche = "cnp69";
      
      long compteurJob = 0;
      long compteurJobCnp = 0;
      long compteurJobSmall = 0;
      Map<Integer, Long> countByDocCount = new HashMap<Integer, Long>();
      Map<String, Long> countByProduit = new HashMap<String, Long>();
      
      LOGGER.info("Récupération des jobs dans la pile des travaux");
      // recupere l'ensemble des jobs
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 100000);
      
      // boucle sur les jobs
      LOGGER.info("{} jobs récupérés", jobs.size());
      for (JobRequest job : jobs) {
         // test si les jobs sont dans la plage de date
         if (isDateBetween(job, dateMin, dateMax)) {
            if (getUrlEcde(job).contains(cnpRecherche)) {
               // on est dans le bon cnp
               compteurJobCnp++;
               
               if (job.getDocCount() <= PARAM_NB_DOCS_MAX) {
                  compteurJobSmall++;
                  if (!countByDocCount.containsKey(job.getDocCount())) {
                     countByDocCount.put(job.getDocCount(), Long.valueOf(1));
                  } else {
                     countByDocCount.put(job.getDocCount(), countByDocCount.get(job.getDocCount()) + 1);
                  }
                  
                  if (!countByProduit.containsKey(job.getVi().getPagms().get(0))) {
                     countByProduit.put(job.getVi().getPagms().get(0), Long.valueOf(1));
                  } else {
                     countByProduit.put(job.getVi().getPagms().get(0), countByProduit.get(job.getVi().getPagms().get(0)) + 1);
                  }
               }
            }
            compteurJob++;
         }
      }
      
      LOGGER.info("{} jobs pour le CNP {} sur {} jobs au total", new Object[] { compteurJobCnp, cnpRecherche, compteurJob});
      LOGGER.info("{} petits jobs petit sur ce cnp", new Object[] { compteurJobSmall });
      for (Integer docCount : countByDocCount.keySet()) {
         LOGGER.info("{} jobs de {} documents", new Object[] { countByDocCount.get(docCount), docCount });
      }
      for (String pagm : countByProduit.keySet()) {
         LOGGER.info("{} jobs pour le PAGM {}", new Object[] { countByProduit.get(pagm), pagm });
      }
   }
   
   private boolean isJobAlreadySuccessfull(List<JobRequest> jobs) {
      int nbSuccess = 0;
      for (JobRequest job : jobs) {
         if (job.getState() == JobState.SUCCESS) {
            nbSuccess++;
         }
      }
      return nbSuccess > 1 ? true : false;
   }
   
   @Test
   //@Ignore
   public void getRunningJobs() {
      String[] hostnames = { "hwi31saeappli1", "hwi31saeappli2", "hwi31saeappli3",
            "hwi69saeappli1", "hwi69saeappli2", "hwi69saeappli3",
            "hwi75saeappli1", "hwi75saeappli2", "hwi75saeappli3"
      };
      
      for (String hostname : hostnames) {
         List<JobRequest> jobs = jobLectureService
            .getNonTerminatedJobs(hostname);
         
         if (jobs.isEmpty()) {
            LOGGER.info("Aucun job en cours sur le serveur {}", new Object[] { hostname });
         } else {
            for (JobRequest job : jobs) {
               if (job.getState() == JobState.RESERVED || job.getState() == JobState.STARTING) {
                  LOGGER.info("Le traitement {} ({}) est a l'etat {} sur le serveur {}", new Object[] { getUrlEcde(job), job.getIdJob().toString(), job.getState(), job.getReservedBy()});
               } 
            }
         }
      }
   }
   
   @Test
   @Ignore
   public void unblocOrdonnanceur()  {
      UUID idTraitement = UUID.fromString("c4461af0-9d88-11e5-a338-005056bf2e59");
      
      LOGGER.info("Recuperation du job {}", new String[] { idTraitement.toString() });
      JobRequest job = jobLectureService.getJobRequest(idTraitement);
      if (job != null) {
         LOGGER.info("Job trouve a l'etat {}", new String[] { job.getState().toString() });
         if (jobLectureService.isJobRemovable(job)) {
            LOGGER.info("Job supprimable");
            jobQueueService.deleteJob(idTraitement);
            LOGGER.info("Job supprime");
         } else {
            LOGGER.info("Job non supprimable");
         }
      } else {
         LOGGER.info("Job non trouve");
      }
   }
   
   @Test
   public void getBlockingJobs() {
      String[] hostnames = { "hwi31saeappli1", "hwi31saeappli2", "hwi31saeappli3",
            "hwi69saeappli1", "hwi69saeappli2", "hwi69saeappli3",
            "hwi75saeappli1", "hwi75saeappli2", "hwi75saeappli3"
      };
      
      for (String hostname : hostnames) {
         List<JobQueue> jobs = jobLectureService
            .getNonTerminatedSimpleJobs(hostname);
         
         if (jobs.isEmpty()) {
            LOGGER.info("Aucun job en cours sur le serveur {}", new Object[] { hostname });
         } else {
            for (JobQueue job : jobs) {
               JobRequest request = jobLectureService.getJobRequest(job.getIdJob());
               if (request.getState() == JobState.FAILURE || request.getState() == JobState.SUCCESS) { 
                  LOGGER.info("Le traitement {} ({}) est a l'etat {} sur le serveur {} mais est toujours dans la queue", new Object[] { getUrlEcde(request), request.getIdJob().toString(), request.getState(), request.getReservedBy()});
               } else if (request.getState() == JobState.STARTING || request.getState() == JobState.RESERVED) {
                  LOGGER.info("Le traitement {} ({}) est en cours ({}) sur le serveur {}", new Object[] { getUrlEcde(request), request.getIdJob().toString(), request.getState(), request.getReservedBy()});
               }
            }
         }
      }
   }
   
   @Test
   public void deleteOldJobs() {
      
      boolean dryRun = true;
      
      Date dateMax = new DateTime().withTimeAtStartOfDay().minusDays(31).toDate();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      
      if (dryRun) {
         LOGGER.info("Mode dryRun actif, pas de suppression");
      }
      LOGGER.info("date de fin de purge : {}", formatter.format(dateMax));
      
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 200000);
      
      int nbJobSupprimes = 0;
      int nbJobTotal = 0;
      for (JobRequest jobRequest : jobs) {
         Date dateCreation = jobRequest.getCreationDate();
         if (dateCreation.before(dateMax)
               || DateUtils.isSameDay(dateCreation, dateMax)) {
            if (!dryRun) {
               jobQueueService.deleteJob(jobRequest.getIdJob());
            }
            nbJobSupprimes++;
            if (nbJobSupprimes % 1000 == 0) {
               LOGGER.info("En cours, {} jobs", nbJobSupprimes);
            }
         }
         nbJobTotal++;
      }
      if (dryRun) {
         LOGGER.info("Nombre de travaux à supprimer : {} - Nb jobs total : {}", nbJobSupprimes, nbJobTotal);
      } else {
         LOGGER.info("Nombre de travaux supprimés : {}", nbJobSupprimes);
      }
   }
}
