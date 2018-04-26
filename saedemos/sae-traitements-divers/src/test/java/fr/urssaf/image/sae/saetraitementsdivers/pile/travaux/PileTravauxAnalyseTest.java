package fr.urssaf.image.sae.saetraitementsdivers.pile.travaux;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-saeTraitementsDivers-jobs-test.xml" })
public class PileTravauxAnalyseTest {

   private static final String DATE_PATTERN_TIMESTAMP = "dd/MM/yyyy HH:mm:ss";

   private static final DateFormat DATE_FORMAT_TIMESTAMP = new SimpleDateFormat(
         DATE_PATTERN_TIMESTAMP);

   @Autowired
   private JobLectureService jobLectureService;

   @Autowired
   private Keyspace keyspace;

   @Test
   public void liste_jobs_between_dates() {

      Date dateMin = new GregorianCalendar(2014, 4, 9, 20, 0, 0).getTime();
      Date dateMax = new GregorianCalendar(2014, 4, 10, 12, 00, 00).getTime();

      // récupère la liste des jobs
      List<JobRequest> jobs = jobLectureService.getAllJobs(keyspace, 500000);
      // System.out.println(String.format("%s jobs récupérés au total",
      // jobs.size()));

      // tri de la liste par date de creation descendant
      Collections.sort(jobs, new Comparator<JobRequest>() {
         @Override
         public int compare(JobRequest job1, JobRequest job2) {
            return job2.getCreationDate().compareTo(job1.getCreationDate());
         }
      });

      int nbJobs = 0;
      // boucle sur la pile de travaux
      for (JobRequest currentJob : jobs) {
         // filtre sur les dates
         if (dateMin.before(currentJob.getCreationDate())
               && (dateMax.after(currentJob.getCreationDate()))) {
            System.out.println(String
                  .format("%s - %s - reservé par %s à %s - %d documents archivés en %s",
                        new Object[] {
                              currentJob.getIdJob().toString(),
                              currentJob.getState(),
                              currentJob.getReservedBy(),
                              DATE_FORMAT_TIMESTAMP.format(currentJob
                                    .getReservationDate()),
                              currentJob.getDocCount(),
                              getDuree(currentJob)}));
            nbJobs++;
         }
      }
      System.out.println(String.format("%s jobs sur la période du %s au %s",
            new Object[] { nbJobs, DATE_FORMAT_TIMESTAMP.format(dateMin),
                  DATE_FORMAT_TIMESTAMP.format(dateMax) }));
   }
   
   private String getDuree(JobRequest job) {
      StringBuffer duree = new StringBuffer();
      long difference = (job.getEndingDate().getTime() - job.getStartingDate().getTime()) / 1000;
      long nbSecondes = difference % 60;
      long nbMinutes = difference / 60;
      long nbHours = difference / 3600;
      if (nbHours > 0) {
         duree.append(nbHours);
         duree.append(" h ");
      }
      if (nbMinutes > 0) {
         duree.append(nbMinutes);
         duree.append(" min ");
      }
      if (nbSecondes > 0) {
         duree.append(nbSecondes);
         duree.append("s");
      }
      return duree.toString();
   }
}
