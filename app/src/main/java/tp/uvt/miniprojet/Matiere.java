
package tp.uvt.miniprojet;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Matiere {

    @SerializedName("id_matiere")
    @Expose
    private String idMatiere;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("tuteur")
    @Expose
    private String tuteur;
    @SerializedName("examens")
    @Expose
    private List<Examen> examens = null;
    @SerializedName("moyenne")
    @Expose
    private String moyenne;

    public String getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(String idMatiere) {
        this.idMatiere = idMatiere;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTuteur() {
        return tuteur;
    }

    public void setTuteur(String tuteur) {
        this.tuteur = tuteur;
    }

    public List<Examen> getExamens() {
        return examens;
    }

    public void setExamens(List<Examen> examens) {
        this.examens = examens;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

}
