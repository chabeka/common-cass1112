/**
 * 
 */
package fr.urssaf.image.sae.services.batch.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.services.batch.common.utils.BatchAuthentificationUtils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

public class BatchAuthentificationUtilsTest {

   @Test
   public void testViVide() {

      JobRequest job = new JobRequest();
      AuthenticationToken token = BatchAuthentificationUtils
            .getToken(job);

      Assert.assertNotNull("le token ne doit pas etre null", token);
      List<GrantedAuthority> authorities = (List<GrantedAuthority>) token
            .getAuthorities();
      List<String> sAuthorities = Arrays.asList("recherche",
            "archivage_unitaire", "archivage_masse", "consultation");
      Assert.assertEquals("il doit y avoir le nombre correct d'autorisations",
            4, authorities.size());
      List<String> authList = new ArrayList<String>();
      for (GrantedAuthority authority : authorities) {
         authList.add(authority.getAuthority());
      }

      for (String auth : sAuthorities) {
         Assert.assertTrue("l'élement " + auth + " doit etre présent "
               + " dans la liste des éléments récupérés", authList
               .contains(auth));
      }
   }

   @Test
   public void testViElementRecherche() {

      AuthenticationToken token = getJobToken("recherche");

      Assert.assertNotNull("le token ne doit pas etre null", token);
      List<GrantedAuthority> authorities = (List<GrantedAuthority>) token
            .getAuthorities();
      List<String> sAuthorities = Arrays.asList("recherche");
      Assert.assertEquals("il doit y avoir le nombre correct d'autorisations",
            1, authorities.size());
      List<String> authList = new ArrayList<String>();
      for (GrantedAuthority authority : authorities) {
         authList.add(authority.getAuthority());
      }

      for (String auth : sAuthorities) {
         Assert.assertTrue("l'élement " + auth + " doit etre présent "
               + " dans la liste des éléments récupérés", authList
               .contains(auth));
      }
   }

   @Test
   public void testViElementCaptureMasse() {

      AuthenticationToken token = getJobToken("archivage_masse");

      Assert.assertNotNull("le token ne doit pas etre null", token);
      List<GrantedAuthority> authorities = (List<GrantedAuthority>) token
            .getAuthorities();
      List<String> sAuthorities = Arrays.asList("recherche", "archivage_masse");
      Assert.assertEquals("il doit y avoir le nombre correct d'autorisations",
            2, authorities.size());
      List<String> authList = new ArrayList<String>();
      for (GrantedAuthority authority : authorities) {
         authList.add(authority.getAuthority());
      }

      for (String auth : sAuthorities) {
         Assert.assertTrue("l'élement " + auth + " doit etre présent "
               + " dans la liste des éléments récupérés", authList
               .contains(auth));
      }
   }
   
   @Test
   public void testViElementSuppressionMasse() {

      AuthenticationToken token = getJobToken("suppression_masse");

      Assert.assertNotNull("le token ne doit pas etre null", token);
      List<GrantedAuthority> authorities = (List<GrantedAuthority>) token
            .getAuthorities();
      List<String> sAuthorities = Arrays.asList("recherche", "suppression_masse");
      Assert.assertEquals("il doit y avoir le nombre correct d'autorisations",
            2, authorities.size());
      List<String> authList = new ArrayList<String>();
      for (GrantedAuthority authority : authorities) {
         authList.add(authority.getAuthority());
      }

      for (String auth : sAuthorities) {
         Assert.assertTrue("l'élement " + auth + " doit etre présent "
               + " dans la liste des éléments récupérés", authList
               .contains(auth));
      }
   }
   
   @Test
   public void testViElementRestoreMasse() {

      AuthenticationToken token = getJobToken("restore_masse");

      Assert.assertNotNull("le token ne doit pas etre null", token);
      List<GrantedAuthority> authorities = (List<GrantedAuthority>) token
            .getAuthorities();
      List<String> sAuthorities = Arrays.asList("recherche", "restore_masse");
      Assert.assertEquals("il doit y avoir le nombre correct d'autorisations",
            2, authorities.size());
      List<String> authList = new ArrayList<String>();
      for (GrantedAuthority authority : authorities) {
         authList.add(authority.getAuthority());
      }

      for (String auth : sAuthorities) {
         Assert.assertTrue("l'élement " + auth + " doit etre présent "
               + " dans la liste des éléments récupérés", authList
               .contains(auth));
      }
   }
   
   private AuthenticationToken getJobToken(String droit) {
      JobRequest job = new JobRequest();
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("TEST");
      extrait.setIdUtilisateur("TEST_UTILISATION");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> listPrmd = new ArrayList<SaePrmd>();
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("codePrmd");
      prmd.setDescription("description");
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setPrmd(prmd);
      listPrmd.add(saePrmd);
      saeDroits.put(droit, listPrmd);
      extrait.setSaeDroits(saeDroits);

      job.setVi(extrait);

      AuthenticationToken token = BatchAuthentificationUtils
            .getToken(job);
      return token;
   }

}
