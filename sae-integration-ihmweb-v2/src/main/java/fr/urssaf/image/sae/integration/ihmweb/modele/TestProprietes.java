package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestProprietes {
   
   private String name;
   private String messageAttendu;
   private String[] checkboxValue;
   private Map<String, Map<String, String>> resStub;
   private Map<String, Map<String, String>> messageInOut;
   
   public TestProprietes(){
      resStub = new LinkedHashMap<String, Map<String, String>>();
   }
   
   public TestProprietes(String name){
      this.name = name;
      resStub = new LinkedHashMap<String, Map<String, String>>();
   }
   
   
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
  
   public String getMessageAttendu() {
      return messageAttendu;
   }
   public void setMessageAttendu(String messageAttendu) {
      this.messageAttendu = messageAttendu;
   }
   public Map<String, Map<String, String>> getResStub() {
      return resStub;
   }
   public void setResStub(Map<String, Map<String, String>> resStub) {
      this.resStub = resStub;
   }

   public String[] getCheckboxValue() {
      return checkboxValue;
   }

   public void setCheckboxValue(String[] checkboxValue) {
      this.checkboxValue = checkboxValue;
   }

   public Map<String, Map<String, String>> getMessageInOut() {
      return messageInOut;
   }

   public void setMessageInOut(Map<String, Map<String, String>> mesageInOut) {
      this.messageInOut = mesageInOut;
   } 
}
