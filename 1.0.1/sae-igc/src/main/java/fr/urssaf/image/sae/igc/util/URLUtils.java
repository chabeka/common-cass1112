package fr.urssaf.image.sae.igc.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * Classe de manipulation de {@link URL}
 * 
 * 
 */
public final class URLUtils {

   private URLUtils() {

   }

   private static final Pattern HTML_TAG_PATTERN = Pattern
         .compile("<a\\b[^>]*href=\"[^>]*>(.*?)</a>");

   private static final Pattern LINK_PATTERN = Pattern
         .compile("href=\"[^>]*\">");

   /**
    * Cette méthode renvoie les urls des liens &lt;a href='[<b>url</b>]'/>
    * contenu dans une page html <i>lien</i> &lt;/a>
    * 
    * @param url
    *           lien de la page html
    * @return liste des urls contenues dans cette page
    * @throws IOException
    *            exception levé lors de la lecture de la page html
    */
   public static List<URL> findLinks(URL url) throws IOException {
      List<URL> links = new ArrayList<URL>();

      Matcher linkTaglib = HTML_TAG_PATTERN.matcher(IOUtils.toString(url
            .openStream()));
      while (linkTaglib.find()) {

         links.add(findLink(url, linkTaglib));

      }

      return links;
   }

   private static URL findLink(URL url, Matcher link)
         throws MalformedURLException {

      Matcher matcher = LINK_PATTERN.matcher(link.group());
      matcher.find();

      return new URL(url, matcher.group().replaceFirst("href=\"", "/")
            .replaceFirst("\">", ""));
   }

}
