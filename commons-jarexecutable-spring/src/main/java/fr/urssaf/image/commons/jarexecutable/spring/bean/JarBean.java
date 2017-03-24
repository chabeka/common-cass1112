package fr.urssaf.image.commons.jarexecutable.spring.bean;


/**
 * Bean pour un simple jar executable.
 * 
 */
public class JarBean {
   
   private String hello;
   
   /**
    * @return the hello
    */
   public final String getHello() {
      hello = "Hello Jar Spring";
      return hello;
   }

   /**
    * @param hello the hello to set
    */
   public final void setHello(String hello) {
      this.hello = hello;
   }

   /**
    * methode d'addition de deux entiers
    * 
    * @param nb1 a additionner
    * @param nb2 a additionner
    * @return somme des deux entiers
    */
   public final int add(int nb1, int nb2) {
      return nb1 + nb2;
   }
   
   /**
    * String a afficher
    * 
    * @param messageAAfficher message a a afficher
    */
   public final void stringAAfficher(String messageAAfficher) {
      System.out.println(messageAAfficher);
   }
   
}
