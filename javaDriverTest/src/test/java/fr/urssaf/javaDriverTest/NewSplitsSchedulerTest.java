/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;

import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.dao.RangeIndexEntity;
import fr.urssaf.javaDriverTest.split.NewSplitsScheduler;

/**
 * Test de NewSplitsSchedulerTest
 */
public class NewSplitsSchedulerTest {

   CqlSession session;

   private void connectToCassandra() throws Exception {
      String servers;
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      // servers = "cnp69gntcas1,cnp69gntcas2,cnp69gntcas3";
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cnp69pregntcas1, cnp69pregntcas2";
      // servers = "cnp69givngntcas1, cnp69givngntcas2";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
      // servers = "cnp69pprodsaecas1,cnp69pprodsaecas2,cnp69pprodsaecas3"; //Préprod
      // servers = "cnp69pprodsaecas6"; //Préprod
      // servers = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv"; // Vrai préprod
      // servers = "10.213.82.56";
      servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv"; // Charge
      // servers = "cnp3gntcvecas1.cve.recouv,cnp6gntcvecas1.cve.recouv,cnp7gntcvecas1.cve.recouv"; // Charge GNT
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cer69imageint9.cer69.recouv";
      // servers = "cer69imageint10.cer69.recouv";
      // servers = "10.207.81.29";
      // servers = "hwi69givnsaecas1.cer69.recouv,hwi69givnsaecas2.cer69.recouv";
      // servers = "hwi69devsaecas1.cer69.recouv,hwi69devsaecas2.cer69.recouv";
      // servers = "hwi69ginsaecas2.cer69.recouv";
      // servers = "cer69-saeint3";
      // servers = "cnp69devgntcas1.gidn.recouv, cnp69devgntcas2.gidn.recouv";
      // servers = "cnp69dev2gntcas1.gidn.recouv, cnp69dev2gntcas2.gidn.recouv";
      // servers = "cnp69miggntcas1.gidn.recouv,cnp69miggntcas2.gidn.recouv"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv";
      // servers = "cnp69devgntcas1.gidn.recouv,cnp69devgntcas2.gidn.recouv";
      // servers = "hwi69intgnscas1.gidn.recouv,hwi69intgnscas2.gidn.recouv";

      final String cassandraLocalDC = "DC6";
      session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

   }

   private void closeCassandra() throws Exception {
      session.close();
   }

   @Test
   public void scheduleTest() throws Exception {
      connectToCassandra();
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(session, "SM_MODIFICATION_DATE");
      System.out.println("newSplits=" + newSplits);
      closeCassandra();
   }

   @Test
   public void schedule2Test() throws Exception {
      final List<RangeIndexEntity> entities = new ArrayList<>();
      entities.add(new RangeIndexEntity(0, "min_lower_bound", "max_upper_bound", 500, "NOMINAL"));
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      final LocalDateTime dateTime = LocalDateTime.parse("2018-01-01 11:11:11", formatter);
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(dateTime, entities);
      System.out.println("newSplits=" + newSplits);
      // Par défaut, sans autre information, on fait des plages de 7 jours
      final String expected = "[min_lower_bound TO max_upper_bound]|[min_lower_bound TO 20180108111111000[#[20180108111111000 TO 20180115111111000[#[20180115111111000 TO 20180122111111000[#[20180122111111000 TO 20180129111111000[#[20180129111111000 TO 20180205111111000[#[20180205111111000 TO 20180212111111000[#[20180212111111000 TO 20180219111111000[#[20180219111111000 TO 20180226111111000[#[20180226111111000 TO max_upper_bound]";
      assertEquals(expected, newSplits);
   }

   @Test
   public void schedule3Test() throws Exception {
      final List<RangeIndexEntity> entities = new ArrayList<>();
      entities.add(new RangeIndexEntity(0, "min_lower_bound", "max_upper_bound", 500, "SPLITTING"));
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      final LocalDateTime dateTime = LocalDateTime.parse("2018-01-01 11:11:11", formatter);
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(dateTime, entities);
      System.out.println("newSplits=" + newSplits);
      // Range non nominal => pas de split
      final String expected = "";
      assertEquals(expected, newSplits);
   }

   @Test
   public void schedule4Test() throws Exception {
      final List<RangeIndexEntity> entities = new ArrayList<>();
      entities.add(new RangeIndexEntity(0, "min_lower_bound", "20180101000000000", 500, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180101000000000", "20180111000000000", 500, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180111000000000", "max_upper_bound", 500, "NOMINAL"));
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      final LocalDateTime dateTime = LocalDateTime.parse("2018-01-01 11:11:11", formatter);
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(dateTime, entities);
      System.out.println("newSplits=" + newSplits);
      // Le range du milieu n'est pas terminé, donc on ne peut pas se baser dessus. On fait donc des plages de 7 jours
      final String expected = "[20180111000000000 TO max_upper_bound]|[20180111000000000 TO 20180118000000000[#[20180118000000000 TO 20180125000000000[#[20180125000000000 TO 20180201000000000[#[20180201000000000 TO 20180208000000000[#[20180208000000000 TO 20180215000000000[#[20180215000000000 TO 20180222000000000[#[20180222000000000 TO 20180301000000000[#[20180301000000000 TO max_upper_bound]";
      assertEquals(expected, newSplits);
   }

   @Test
   public void schedule5Test() throws Exception {
      final List<RangeIndexEntity> entities = new ArrayList<>();
      entities.add(new RangeIndexEntity(0, "min_lower_bound", "20180101000000000", 1000000, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180101000000000", "20180111000000000", 1000000, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180111000000000", "max_upper_bound", 1000000, "NOMINAL"));
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      final LocalDateTime dateTime = LocalDateTime.parse("2018-01-21 11:11:11", formatter);
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(dateTime, entities);
      System.out.println("newSplits=" + newSplits);
      // On a détecté un range de 10 jours avec 1 millions d'éléments. On crée donc des ranges de 10 jours.
      final String expected = "[20180111000000000 TO max_upper_bound]|[20180111000000000 TO 20180121000000000[#[20180121000000000 TO 20180131000000000[#[20180131000000000 TO 20180210000000000[#[20180210000000000 TO 20180220000000000[#[20180220000000000 TO 20180302000000000[#[20180302000000000 TO 20180312000000000[#[20180312000000000 TO 20180322000000000[#[20180322000000000 TO max_upper_bound]";
      assertEquals(expected, newSplits);
   }

   @Test
   public void schedule6Test() throws Exception {
      final List<RangeIndexEntity> entities = new ArrayList<>();
      entities.add(new RangeIndexEntity(0, "min_lower_bound", "20180101000000000", 1000000, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180101000000000", "20180111000000000", 1000000, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180111000000000", "20180121000000000", 1200000, "NOMINAL"));
      entities.add(new RangeIndexEntity(1, "20180121000000000", "max_upper_bound", 2000, "NOMINAL"));
      final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      final LocalDateTime dateTime = LocalDateTime.parse("2018-01-28 11:11:11", formatter);
      final String newSplits = NewSplitsScheduler.scheduleNewSplits(dateTime, entities);
      System.out.println("newSplits=" + newSplits);
      // On a détecté des ranges de 10 jours avec 1 à 1.2 millions d'éléments. On crée donc des ranges de 9 jours environ
      final String expected = "[20180121000000000 TO max_upper_bound]|[20180121000000000 TO 20180130021054000[#[20180130021054000 TO 20180208042148000[#[20180208042148000 TO 20180217063242000[#[20180217063242000 TO 20180226084336000[#[20180226084336000 TO 20180307105430000[#[20180307105430000 TO 20180316130524000[#[20180316130524000 TO 20180325151618000[#[20180325151618000 TO max_upper_bound]";
      assertEquals(expected, newSplits);
   }

}
