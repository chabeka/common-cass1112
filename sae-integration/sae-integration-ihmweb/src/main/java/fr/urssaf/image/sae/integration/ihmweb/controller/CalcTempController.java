/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CalcTempFormulaire;

/**
 * 
 * 
 */
@Controller
@RequestMapping(value = "calcTemp")
public class CalcTempController {

   private SimpleDateFormat SIMPLE_DF = new SimpleDateFormat("dd/MM/yyyy");
   
   private SimpleDateFormat SIMPLE_HF = new SimpleDateFormat("HH:mm:ss");

   /**
    * Retourne la page par défaut
    * 
    * @return la page à afficher
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView() {
      return "calcTemp";
   }

   /**
    * Renvoie la date par défaut
    * 
    * @return la date
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.GET, params = "action=getDate")
   public final HashMap<String, Object> getDefaultDate() {

      String currentDate = SIMPLE_DF.format(new Date());

      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("success", true);
      map.put("startDate", currentDate);

      return map;

   }

   /**
    * Renvoie la date calculée
    * 
    * @return la date
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.POST, params = "action=calculDateFin")
   public final HashMap<String, Object> getDefaultDate(
         CalcTempFormulaire formulaire) {

      HashMap<String, Object> map = new HashMap<String, Object>();
      String message = null;
      boolean succes = false;

      if (formulaire.getStartDate() == null) {
         message = "La date de début est vide";
      } else if (formulaire.getTime() < 1) {
         message = "La durée est vide ou inférieure ou égale à 0";
      } else {

         try {
            Date startDate = SIMPLE_DF.parse(formulaire.getStartDate());

            Date endDate = DateUtils.addDays(startDate, formulaire.getTime());
            String value = SIMPLE_DF.format(endDate);
            map.put("endDate", value);
            succes = true;
         } catch (ParseException e) {
            message = "Date invalide";
         }
      }

      map.put("message", message);
      map.put("success", succes);

      return map;

   }
   
   
   @ResponseBody
   @RequestMapping(method = RequestMethod.POST, params = "action=convertTimestampToDate")
   public final HashMap<String, Object> convertTimestampToDate(
         @RequestParam long timestamp) {

      Date laDate = new Date(timestamp) ;
      
      String date = SIMPLE_DF.format(laDate);
      String heure = SIMPLE_HF.format(laDate);
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("success", true);
      map.put("date", date);
      map.put("heure", heure);

      return map;

   }
   
   
   @ResponseBody
   @RequestMapping(method = RequestMethod.POST, params = "action=convertDateToTimestamp")
   public final HashMap<String, Object> convertDateToTimestamp(
         @RequestParam String date,
         @RequestParam String heure) throws ParseException {

      Date datePart = SIMPLE_DF.parse(date);
      Date heurePart = SIMPLE_HF.parse(heure);
      
      Calendar calendarHeure = Calendar.getInstance();
      calendarHeure.setTime(heurePart);
      int cHeure = calendarHeure.get(Calendar.HOUR_OF_DAY); 
      int cMinute = calendarHeure.get(Calendar.MINUTE);
      int cSecondes = calendarHeure.get(Calendar.SECOND);
      
      Calendar calendarDate = Calendar.getInstance();
      calendarDate.setTime(datePart);
      calendarDate.set(Calendar.HOUR_OF_DAY, cHeure);
      calendarDate.set(Calendar.MINUTE, cMinute);
      calendarDate.set(Calendar.SECOND, cSecondes);
      
      long timestamp = calendarDate.getTimeInMillis();
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("success", true);
      map.put("timestamp", timestamp);

      return map;

   }

}
