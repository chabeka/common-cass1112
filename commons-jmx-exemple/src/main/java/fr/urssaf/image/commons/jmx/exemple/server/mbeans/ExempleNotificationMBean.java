package fr.urssaf.image.commons.jmx.exemple.server.mbeans;

public interface ExempleNotificationMBean {

   String getNom(); 

   int getValeur(); 
   
   void setValeur(int valeur); 
   
   int add(int a, int b);

   void rafraichir();
   
}
