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
    private String pdf;
    private Integer nbPage;
    private String titre;
    private String codeRND;


    //
    ListeMetadonneeType listeMetadonneeType = new ListeMetadonneeType();

    public void reset() {
        this.idV2 = null;
        this.dateSaisie = null;
        this.pdf = null;
        this.nbPage = null;
        this.listeMetadonneeType = null;
        this.titre = null;
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

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCodeRND() {
        return codeRND;
    }

    public void setCodeRND(String codeRND) {
        this.codeRND = codeRND;
    }
}
