
package tp.uvt.miniprojet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Examen {

    @SerializedName("id_examen")
    @Expose
    private String idExamen;
    @SerializedName("libelle")
    @Expose
    private String libelle;
    @SerializedName("date_operation")
    @Expose
    private String dateOperation;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("date_update")
    @Expose
    private String dateUpdate;
    @SerializedName("coeff")
    @Expose
    private String coeff;

    public String getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(String idExamen) {
        this.idExamen = idExamen;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(String dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getCoeff() {
        return coeff;
    }

    public void setCoeff(String coeff) {
        this.coeff = coeff;
    }

}
