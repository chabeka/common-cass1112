/**
 * 
 */
package fr.urssaf.image.sae.dfce.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 *Cette classe contient la liste des codes images  suite à la désérialisation du fichier xml [codefonctions.xml].
 *Elle contient :
 * <ul>
 * <li>codeImages : La liste des codes images</li>
 * </ul>
 * @author akenore
 * 
 */
@XStreamAlias("codes")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Codes {
   @XStreamImplicit(itemFieldName = "codeimage")
   private List<CodeImage> codeImages;

   /**
    * @param codeImages  : La liste des codes applicatifs.
    */
   public final void setCodeImages(final List<CodeImage> codeImages) {
      this.codeImages = codeImages;
   }

   /**
    * @return  La liste des codes applicatifs.
    */
   public final List<CodeImage> getCodeImages() {
      return codeImages;
   }



}
