package fr.urssaf.image.commons.jmx.exemple.server.mbeans;

public class ExempleStandard implements ExempleStandardMBean {

   private int valeur = 1;
   
   @Override
   public String getNom() {
      return "Mon nom est en lecture seule";
   }

   @Override
   public int getValeur() {
      return valeur;
   }
   
   @Override
   public synchronized void setValeur(int valeur) {
      this.valeur = valeur;
   }
   
   @Override
   public int add(int a, int b) {
      return a + b;
   }
   
   @Override
   public void rafraichir() {
      System.out.println("Rafraichir les donnees");
   }

}
