package fr.urssaf.image.sae.services.controles.traces;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.urssaf.image.sae.services.CommonsServices;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe permettant de tester le service de contrôle.
 * 
 */
public class TracesControlesSupportTest extends CommonsServices {

   @Autowired
   private TracesControlesSupport support;

   @Autowired
   private RegTechniqueService regTechniqueService;

   @Test
   public void traceErreurIdentFormatFichierWithoutAuthenticationAndIdDoc() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace
      support.traceErreurIdentFormatFichier("junit", "fmt/354", "fmt/40",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_IDENT_FICHIER);
   }

   @Test
   public void traceErreurIdentFormatFichierWithoutIdDocAndThrowException() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace (provoque une NullPointerException)
      TracesControlesSupport supportInstancie = new TracesControlesSupport();
      supportInstancie.traceErreurIdentFormatFichier("junit", "fmt/354",
            "fmt/40", "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      int sizeApres = 0;
      if (liste != null) {
         sizeApres = liste.size();
      }
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide",
            sizeApres, sizeAvant);
   }

   @Test
   public void traceErreurIdentFormatFichierWithoutIdDocWithAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout des informations d'authentification
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("CS_DEV_TOUTES_ACTIONS");
      extrait.getPagms().add("PAGM_TOUTES_ACTIONS");
      extrait.setIdUtilisateur("NON_RENSEIGNE");
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            extrait.getIdUtilisateur(), extrait,
            new String[] { "PRMD_PERMIT_ALL" });
      SecurityContextHolder.getContext().setAuthentication(token);

      // ajout de la trace
      support.traceErreurIdentFormatFichier("junit", "fmt/354", "fmt/40",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_IDENT_FICHIER);
   }

   @Test
   public void traceErreurIdentFormatFichierWithIdDocWithoutAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace
      support.traceErreurIdentFormatFichier("junit", "fmt/354", "fmt/40",
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_IDENT_FICHIER);
   }

   @Test
   public void traceErreurIdentFormatFichierWithIdDocAndThrowException() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace (provoque une NullPointerException)
      TracesControlesSupport supportInstancie = new TracesControlesSupport();
      supportInstancie.traceErreurIdentFormatFichier("junit", "fmt/354",
            "fmt/40", "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      int sizeApres = 0;
      if (liste != null) {
         sizeApres = liste.size();
      }
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide",
            sizeApres, sizeAvant);
   }

   @Test
   public void traceErreurIdentFormatFichierWithIdDocAndAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout des informations d'authentification
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("CS_DEV_TOUTES_ACTIONS");
      extrait.getPagms().add("PAGM_TOUTES_ACTIONS");
      extrait.setIdUtilisateur("NON_RENSEIGNE");
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            extrait.getIdUtilisateur(), extrait,
            new String[] { "PRMD_PERMIT_ALL" });
      SecurityContextHolder.getContext().setAuthentication(token);

      // ajout de la trace
      support.traceErreurIdentFormatFichier("junit", "fmt/354", "fmt/40",
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_IDENT_FICHIER);
   }

   @Test
   public void traceErreurValidFormatFichierWithoutAuthenticationAndIdDoc() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace
      support.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_VALID_FICHIER);
   }

   @Test
   public void traceErreurValidFormatFichierWithoutIdDocAndThrowException() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace (provoque une NullPointerException)
      TracesControlesSupport supportInstancie = new TracesControlesSupport();
      supportInstancie.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      int sizeApres = 0;
      if (liste != null) {
         sizeApres = liste.size();
      }
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide",
            sizeApres, sizeAvant);
   }

   @Test
   public void traceErreurValidFormatFichierWithoutIdDocWithAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout des informations d'authentification
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("CS_DEV_TOUTES_ACTIONS");
      extrait.getPagms().add("PAGM_TOUTES_ACTIONS");
      extrait.setIdUtilisateur("NON_RENSEIGNE");
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            extrait.getIdUtilisateur(), extrait,
            new String[] { "PRMD_PERMIT_ALL" });
      SecurityContextHolder.getContext().setAuthentication(token);

      // ajout de la trace
      support.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_VALID_FICHIER);
   }

   @Test
   public void traceErreurValidFormatFichierWithIdDocWithoutAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace
      support.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_VALID_FICHIER);
   }

   @Test
   public void traceErreurValidFormatFichierWithIdDocAndThrowException() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout de la trace (provoque une NullPointerException)
      TracesControlesSupport supportInstancie = new TracesControlesSupport();
      supportInstancie.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      int sizeApres = 0;
      if (liste != null) {
         sizeApres = liste.size();
      }
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide",
            sizeApres, sizeAvant);
   }

   @Test
   public void traceErreurValidFormatFichierWithIdDocAndAuthentication() {

      // initialise les dates de debut et de fin
      GregorianCalendar dateDebut = new GregorianCalendar();
      dateDebut.set(Calendar.HOUR, 0);
      dateDebut.set(Calendar.MINUTE, 0);
      dateDebut.set(Calendar.SECOND, 0);
      GregorianCalendar dateFin = new GregorianCalendar(dateDebut
            .get(Calendar.YEAR), dateDebut.get(Calendar.MONTH), dateDebut
            .get(Calendar.DAY_OF_MONTH), 23, 59, 59);

      // recupere la taille du registre de surveillance technique
      List<TraceRegTechniqueIndex> liste = regTechniqueService.lecture(
            dateDebut.getTime(), dateFin.getTime(), 100, true);
      int sizeAvant = 0;
      if (liste != null) {
         sizeAvant = liste.size();
      }

      // ajout des informations d'authentification
      VIContenuExtrait extrait = new VIContenuExtrait();
      extrait.setCodeAppli("CS_DEV_TOUTES_ACTIONS");
      extrait.getPagms().add("PAGM_TOUTES_ACTIONS");
      extrait.setIdUtilisateur("NON_RENSEIGNE");
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            extrait.getIdUtilisateur(), extrait,
            new String[] { "PRMD_PERMIT_ALL" });
      SecurityContextHolder.getContext().setAuthentication(token);

      // ajout de la trace
      support.traceErreurValidFormatFichier("junit", "fmt/354",
            "Le pdf n'est pas du tout valide",
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000");

      // verifie que la trace a ete ajoutee
      liste = regTechniqueService.lecture(dateDebut.getTime(), dateFin
            .getTime(), 100, true);
      Assert.assertNotNull(
            "Le registre de surveillance technique ne devrait pas être null",
            liste);
      Assert.assertEquals(
            "Le registre de surveillance technique devrait être vide", liste
                  .size(), sizeAvant + 1);
      TraceRegTechniqueIndex trace = liste.get(0);
      Assert.assertNotNull("La trace ne devrait pas être null", trace);
      Assert.assertEquals("La trace ne devrait pas être null", trace
            .getCodeEvt(),
            TracesControlesSupport.TRACE_CODE_EVT_ERREUR_VALID_FICHIER);
   }
}
