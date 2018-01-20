
package tp.uvt.miniprojet;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Classe {

    @SerializedName("id_classe")
    @Expose
    private String idClasse;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("matieres")
    @Expose
    private List<Matiere> matieres = null;

    public String getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(String idClasse) {
        this.idClasse = idClasse;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Matiere> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }

}
