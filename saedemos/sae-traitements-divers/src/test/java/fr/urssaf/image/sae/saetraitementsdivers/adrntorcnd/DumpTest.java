package fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationLocator;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.InterfaceDuplicationPort_PortType;
import fr.urssaf.image.sae.saetraitementsdivers.adrntorcnd.webservices.modele.RNDTypeDocument;

/**
 * Ceci n'est pas une classe de TU<br>
 * <br>
 * Il s'agit juste d'afficher des infos dans la console<br>
 * <br>
 * On utilise l'opération getListeTypesDocuments du service ARDN
 */
public class DumpTest {

   private final static String URL_WS = "http://cer69adrn.cer69.recouv:9007/services/duplication.php?WSDL";
   private final static String VERSION = "11.2";

   private InterfaceDuplicationPort_PortType getPort() throws ServiceException {
      InterfaceDuplicationLocator locator = new InterfaceDuplicationLocator();
      locator.setInterfaceDuplicationPortEndpointAddress(URL_WS);
      InterfaceDuplicationPort_PortType port = locator
            .getInterfaceDuplicationPort();
      return port;
   }

   private class ComptageInt {

      public Integer valeur;
      public Integer comptage;

      public ComptageInt(Integer valeur, Integer comptage) {
         this.valeur = valeur;
         this.comptage = comptage;
      }

   }

   private class CodeRndComparator implements Comparator<String> {

      @Override
      public int compare(String s1, String s2) {

         String[] codeRnd1 = StringUtils.split(s1, ".");
         String[] codeRnd2 = StringUtils.split(s2, ".");

         int result = compareDigit(codeRnd1[0], codeRnd2[0]);
         if (result == 0) {
            result = compareDigit(codeRnd1[1], codeRnd2[1]);
         }
         if (result == 0) {
            result = compareDigit(codeRnd1[2], codeRnd2[2]);
         }
         if (result == 0) {
            result = compareDigit(codeRnd1[3], codeRnd2[3]);
         }
         if (result == 0) {
            result = compareDigit(codeRnd1[4], codeRnd2[4]);
         }
         if (result == 0) {
            result = compareDigit(codeRnd1[5], codeRnd2[5]);
         }

         return result;

      }

      private int compareDigit(String digit1, String digit2) {

         int result;

         if (StringUtils.isNumeric(digit1) && StringUtils.isNumeric(digit2)) {
            Integer iDigit1 = Integer.parseInt(digit1);
            Integer iDigit2 = Integer.parseInt(digit2);
            result = iDigit1.compareTo(iDigit2);
         } else if (StringUtils.isNumeric(digit1)
               && !StringUtils.isNumeric(digit2)) {
            result = -1;
         } else if (!StringUtils.isNumeric(digit1)
               && StringUtils.isNumeric(digit2)) {
            result = 1;
         } else {
            result = digit1.compareTo(digit2);
         }

         return result;

      }

   }

   private int sommeDesValeurs(Map<Integer, Integer> map) {

      int result = 0;

      for (Map.Entry<Integer, Integer> e : map.entrySet()) {
         result += e.getValue();
      }

      return result;

   }

   private List<ComptageInt> triComptage(Map<Integer, Integer> map) {

      SortedSet<Integer> sortedset = new TreeSet<Integer>(map.keySet());

      Iterator<Integer> it = sortedset.iterator();

      List<ComptageInt> comptagesTries = new ArrayList<ComptageInt>();

      Integer cle;
      Integer valeur;
      while (it.hasNext()) {

         cle = it.next();

         valeur = map.get(cle);

         comptagesTries.add(new ComptageInt(cle, valeur));

      }

      return comptagesTries;

   }

   @Test
   public void dumpDureeArchivage() throws ServiceException, RemoteException {

      // Variable : Version du RND
      String versionRND = VERSION;

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      Map<Integer, Integer> comptages = new HashMap<Integer, Integer>();

      // String codeRnd;
      int dureeArchivage;
      Integer compteur;
      for (RNDTypeDocument typeDoc : typesDoc) {

         // codeRnd = typeDoc.get_reference();
         dureeArchivage = typeDoc.get_dureeArchivage();

         compteur = comptages.get(dureeArchivage);
         if (compteur == null) {
            comptages.put(dureeArchivage, 1);
         } else {
            comptages.put(dureeArchivage, compteur + 1);
         }

      }

      // Vérif
      int laSomme = sommeDesValeurs(comptages);
      assertEquals(typesDoc.length, laSomme);

      // Dump
      System.out.println("Version du RND utilisée : " + versionRND);
      List<ComptageInt> comptagesTries = triComptage(comptages);
      int total = 0;
      for (ComptageInt comptage : comptagesTries) {
         System.out.println(String.format(
               "Durée %1$4s : %2$4s type(s) de documents", comptage.valeur,
               comptage.comptage));
         total += comptage.comptage;
      }
      System.out.println("Total types de documents : " + total);

   }

   @Test
   public void dumpUnCodeRND() throws ServiceException, RemoteException {

      // Variables : Version du RND et Code RND recherché
      String versionRND = VERSION;
      String codeRndRecherche = "2.3.1.1.12";

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      System.out.println("Version du RND utilisée : " + versionRND);
      System.out.println("Code RND : " + codeRndRecherche);

      String codeRnd;
      boolean trouve = false;
      for (RNDTypeDocument typeDoc : typesDoc) {

         codeRnd = typeDoc.get_reference();

         if (codeRnd.equals(codeRndRecherche)) {

            trouve = true;

            System.out.println(String.format("commentaire : %s", typeDoc
                  .get_commentaire()));
            System.out.println(String.format("dlc : %s", typeDoc.get_dlc()));
            System.out.println(String.format("dureeArchivage : %s", typeDoc
                  .get_dureeArchivage()));
            System.out.println(String.format("idVersion : %s", typeDoc
                  .get_idVersion()));
            System.out
                  .println(String.format("label : %s", typeDoc.get_label()));
            System.out.println(String.format("refActivite : %s", typeDoc
                  .get_refActivite()));
            System.out.println(String.format("reference : %s", typeDoc
                  .get_reference()));
            System.out.println(String.format("refFonction : %s", typeDoc
                  .get_refFonction()));
            System.out.println(String.format("refProcessus : %s", typeDoc
                  .get_refProcessus()));
            System.out.println(String.format("refSecteurActivite : %s", typeDoc
                  .get_refSecteurActivite()));
            System.out.println(String.format("responsabilite : %s", typeDoc
                  .get_responsabilite()));
            System.out.println(String.format("type : %s", typeDoc.get_type()));
            System.out.println(String.format("typePJ : %s", typeDoc
                  .get_typePJ()));
            System.out.println(String.format("is_archivable : %s", typeDoc
                  .is_archivable()));
            System.out.println(String.format("is_archiveHisto : %s", typeDoc
                  .is_archiveHisto()));
            System.out.println(String.format("is_destructionAutorisee : %s",
                  typeDoc.is_destructionAutorisee()));
            System.out
                  .println(String.format("is_etat : %s", typeDoc.is_etat()));
            System.out.println(String.format("is_pj : %s", typeDoc.is_pj()));
            System.out.println(String.format("is_reserveCNM : %s", typeDoc
                  .is_reserveCNM()));

            break;

         }

      }

      if (!trouve) {
         System.out.println("Code RND non trouvé");
      }

   }

   @Test
   public void dumpTypesDocSansCodeFonction() throws ServiceException,
         RemoteException {

      // Variable : Version du RND
      String versionRND = VERSION;

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      System.out.println("Version du RND utilisée : " + versionRND);

      List<String> codesRnd = new ArrayList<String>();
      for (RNDTypeDocument typeDoc : typesDoc) {

         if (StringUtils.isBlank(typeDoc.get_refFonction())) {
            codesRnd.add(typeDoc.get_reference());
         }

      }

      // Dump
      Collections.sort(codesRnd, new CodeRndComparator());
      System.out.print(String.format(
            "%s type(s) de document sans code fonction", codesRnd.size()));
      System.out.println(String.format(" sur %s types de document",
            typesDoc.length));
      System.out.println(StringUtils.EMPTY);
      for (String codeRnd : codesRnd) {
         System.out.println(codeRnd);
      }

   }

   @Test
   public void dumpTypesDocSansCodeActivite() throws ServiceException,
         RemoteException {

      // Variable : Version du RND
      String versionRND = VERSION;

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      System.out.println("Version du RND utilisée : " + versionRND);

      List<String> codesRnd = new ArrayList<String>();
      for (RNDTypeDocument typeDoc : typesDoc) {

         if (StringUtils.isBlank(typeDoc.get_refActivite())) {
            codesRnd.add(typeDoc.get_reference());
         }

      }

      // Dump
      Collections.sort(codesRnd, new CodeRndComparator());
      System.out.print(String.format(
            "%s type(s) de document sans code activité", codesRnd.size()));
      System.out.println(String.format(" sur %s types de document",
            typesDoc.length));
      System.out.println(StringUtils.EMPTY);
      for (String codeRnd : codesRnd) {
         System.out.println(codeRnd);
      }

   }

   @Test
   public void dumpTypesDocSansLibelle() throws ServiceException,
         RemoteException {

      // Variable : Version du RND
      String versionRND = VERSION;

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      System.out.println("Version du RND utilisée : " + versionRND);

      List<String> codesRnd = new ArrayList<String>();
      for (RNDTypeDocument typeDoc : typesDoc) {

         if (StringUtils.isBlank(typeDoc.get_label())) {
            codesRnd.add(typeDoc.get_reference());
         }

      }

      // Dump
      Collections.sort(codesRnd, new CodeRndComparator());
      System.out.print(String.format("%s type(s) de document sans libellé",
            codesRnd.size()));
      System.out.println(String.format(" sur %s types de document",
            typesDoc.length));
      System.out.println(StringUtils.EMPTY);
      for (String codeRnd : codesRnd) {
         System.out.println(codeRnd);
      }

   }

   @Test
   public void dumpIsEtatFalse() throws ServiceException, RemoteException {

      // Variable : Version du RND
      String versionRND = VERSION;

      InterfaceDuplicationPort_PortType port = getPort();

      RNDTypeDocument typesDoc[] = port.getListeTypesDocuments(versionRND);

      int countFalse = 0;
      for (RNDTypeDocument rndTypeDocument : typesDoc) {

         if (!rndTypeDocument.is_etat()) {
            countFalse++;
         }
      }

      System.out.println("Nombre de type de documents à etat=false : "
            + countFalse + "/" + typesDoc.length);
      System.out.println("Nombre de type de documents à etat=true : "
            + (typesDoc.length - countFalse) + "/" + typesDoc.length);
   }

}
