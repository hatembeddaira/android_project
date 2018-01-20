package tp.uvt.miniprojet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dell on 17/12/2017.
 */

public interface RestInterface{
    @GET("authentification.php")
    Call<Etudient> authentificationEtudient(@Query("email") String email , @Query("passwd") String password);
    @GET("sync_data.php")
    Call<Classe> getClasseInfo(@Query("id") int id);
    @GET("edit_password.php")
    Call<Result> edit_password(@Query("id") int id, @Query("passwd") String password);
    @GET("edit_date_sync.php")
    Call<Result> edit_date_sync(@Query("id") int id);

}
