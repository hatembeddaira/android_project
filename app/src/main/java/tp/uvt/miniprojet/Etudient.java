
package tp.uvt.miniprojet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Etudient {

    @SerializedName("id_etudient")
    @Expose
    private String idEtudient;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("prenom")
    @Expose
    private String prenom;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("dernier_sync")
    @Expose
    private String dernierSync;
    @SerializedName("id_classe")
    @Expose
    private String idClasse;
    @SerializedName("libelle_classe")
    @Expose
    private String libelleClasse;

    public String getIdEtudient() {
        return idEtudient;
    }

    public void setIdEtudient(String idEtudient) {
        this.idEtudient = idEtudient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDernierSync() {
        return dernierSync;
    }

    public void setDernierSync(String dernierSync) {
        this.dernierSync = dernierSync;
    }

    public String getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(String idClasse) {
        this.idClasse = idClasse;
    }

    public String getLibelleClasse() {
        return libelleClasse;
    }

    public void setLibelleClasse(String libelleClasse) {
        this.libelleClasse = libelleClasse;
    }

}
