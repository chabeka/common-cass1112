package fr.urssaf.image.commons.cassandra.spring.batch.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

/**
 * Classe fournissant des méthodes utilisées dans les différente classe DAO de
 * cassandra-spring-batch
 *
 */
public final class CassandraJobHelper {

   /**
    * Classe statique
    */
   private CassandraJobHelper() {
   }
   
   /**
    * Crée une "clé" permettant de résumer un job et ses paramètres.
    * Implémentation inspirée de celle de JdbcJobInstanceDao, sauf que cette
    * dernière est bugguée puisqu'elle ne tient pas compte de jobName.
    * 
    * @param jobName          Le nom du job
    * @param jobParameters    Les paramètres du job
    * @return la "clé" (correspond à un MD5)
    */
   public static byte[] createJobKey(String jobName, JobParameters jobParameters) {

      Map<String, JobParameter> props = jobParameters.getParameters();
      props.put("__jobName", new JobParameter(jobName));
      StringBuffer stringBuffer = new StringBuffer();
      List<String> keys = new ArrayList<String>(props.keySet());
      Collections.sort(keys);
      for (String key : keys) {
         JobParameter jobParameter = props.get(key);
         String value = jobParameter.getValue() == null ? "" : jobParameter
               .toString();
         stringBuffer.append(key + "=" + value + ";");
      }

      MessageDigest digest;
      try {
         digest = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalStateException(
               "MD5 algorithm not available.  Fatal (should be in the JDK).");
      }
      try {
         byte[] bytes = digest
               .digest(stringBuffer.toString().getBytes("UTF-8"));
         return bytes;
      } catch (UnsupportedEncodingException e) {
         throw new IllegalStateException(
               "UTF-8 encoding not available.  Fatal (should be in the JDK).");
      }
   }

   /**
    * Renvoie vrai si valeur "value" respecte le pattern "pattern"
    * @param pattern  pattern ou "*" représente n'importe quelle séquence de caractères
    * @param value    valeur à tester
    * @return  vrai si la valeur respecte le pattern
    */
   public static boolean checkPattern(String pattern, String value) {
      String patString = pattern.replaceAll(
            "(\\[|\\]|\\(|\\)|\\^|\\$|\\{|\\}|\\.|\\?|\\+|\\*|\\\\)", "\\\\$1");
      patString = StringUtils.replace(patString, "\\*", ".*");
      Pattern patt = Pattern.compile(patString);
      Matcher matcher = patt.matcher(value);
      return matcher.matches();
   }
   

}
