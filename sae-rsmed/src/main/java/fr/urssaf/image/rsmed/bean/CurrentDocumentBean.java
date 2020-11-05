package fr.urssaf.image.rsmed.bean;

import fr.urssaf.image.rsmed.bean.xsd.generated.ListeMetadonneeType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/***
 * Objet pour stocker le document courant du fichier Xml en entrée
 */

@Component
public class CurrentDocumentBean {
    @NonNull
    private String idV2;
    private String dateSaisie;
    private String pdfName;
    private Integer nbPage;

    //
    ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();

    public void reset() {
        this.idV2 = null;
        this.dateSaisie = null;
        this.pdfName = null;
        this.nbPage = null;
        this.listeMetadonneeType = null;
    }

    @NonNull
    public String getIdV2() {
        return idV2;
    }

    public void setIdV2(@NonNull String idV2) {
        this.idV2 = idV2;
    }

    public String getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(String dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public Integer getNbPage() {
        return nbPage;
    }

    public void setNbPage(Integer nbPage) {
        this.nbPage = nbPage;
    }

    public ListeMetadonneeType getListeMetadonneeType() {
        return listeMetadonneeType;
    }

    public void setListeMetadonneeType(ListeMetadonneeType listeMetadonneeType) {
        this.listeMetadonneeType = listeMetadonneeType;
    }

    public void setListeMetadonneeType(CurrentDocumentBean currentDocumentBean, ListeMetadonneeType listeMetadonneeType) {
        this.listeMetadonneeType = listeMetadonneeType;
    }
}
