/**
 * 
 */
package fr.urssaf.image.sae.igc.modele;

import java.net.URL;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 
 */
@XStreamAlias("URLTelechargementCRL")
public class URLList {

   @XStreamImplicit(itemFieldName = "url")
   private List<URL> urls;

   /**
    * @return the urls
    */
   public final List<URL> getUrls() {
      return urls;
   }

   /**
    * @param urls
    *           the urls to set
    */
   public final void setUrls(List<URL> urls) {
      this.urls = urls;
   }

}
