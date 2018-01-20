package tp.uvt.miniprojet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 01/01/2018.
 */

public class Result {
    @SerializedName("succes")
    @Expose
    private String succes;
    public String getSucces() {
        return succes;
    }
    public void setSucces(String succes) {
        this.succes = succes;
    }
    @SerializedName("date")
    @Expose
    private String date;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
