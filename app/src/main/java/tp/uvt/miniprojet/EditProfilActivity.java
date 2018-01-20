package tp.uvt.miniprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfilActivity extends AppCompatActivity {
String old_passwd = null;
    Etudient etudient = null;
    TextView txt_password = null;
    TextView txt_password2 = null;
    TextView txt_password3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        TextView txt_nom = (TextView)findViewById(R.id.tv_nom) ;
        TextView txt_prenom = (TextView)findViewById(R.id.tv_prenom) ;
        TextView txt_email = (TextView)findViewById(R.id.tv_email) ;
        TextView txt_date = (TextView)findViewById(R.id.tv_date) ;
        TextView txt_classe = (TextView)findViewById(R.id.tv_classe) ;
        txt_password = (TextView)findViewById(R.id.password) ;
        txt_password2 = (TextView)findViewById(R.id.password2) ;
        txt_password3 = (TextView)findViewById(R.id.password3) ;

        MyDBHandler dhHandler = new MyDBHandler(this, null, null, 1);
        etudient = dhHandler.findStudent();
        if(etudient != null)
        {
            Classe classe = dhHandler.findClasse(Integer.parseInt(etudient.getIdClasse()));
            txt_nom.setText(etudient.getNom());
            txt_prenom.setText(etudient.getPrenom());
            txt_email.setText(etudient.getEmail());
            txt_classe.setText(classe.getLibelle());
            txt_date.setText(etudient.getDernierSync());
            old_passwd = etudient.getPassword();
            //Toast.makeText(getApplication(), old_passwd , Toast.LENGTH_SHORT).show();
        }
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMan.getActiveNetworkInfo();
                if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                {
                    AlertDialog.Builder builder;
                    AlertDialog dialog;
                    builder = new AlertDialog.Builder(EditProfilActivity.this);
                    builder.setTitle("Confirmation!");
                    builder.setMessage("Etes-vous sur de vouloir modifier le mode passe!");
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            txt_password.setText("");
                            txt_password2.setText("");
                            txt_password3.setText("");
                        }
                    });
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean bool = true;
                            //Toast.makeText(getApplication(), txt_password.getText().toString() , Toast.LENGTH_SHORT).show();
                            if(!old_passwd.equals(txt_password.getText().toString())){bool = false; Snackbar.make(getCurrentFocus(), "Mot de passe actuel est incorrect", Snackbar.LENGTH_SHORT).setAction("Action", null).show(); return;}
                            if(!txt_password2.getText().toString().equals(txt_password3.getText().toString())){bool = false; Snackbar.make(getCurrentFocus(), "Nouveau mot de passe n'est pas identique", Snackbar.LENGTH_SHORT).setAction("Action", null).show(); return;}
                            if(old_passwd.equals(txt_password2.getText().toString())){bool = false; Snackbar.make(getCurrentFocus(), "Nouveau mot de passe est le même que l'ancienne", Snackbar.LENGTH_SHORT).setAction("Action", null).show(); return;}
                            if(bool == true)
                            {
                                Retrofit.Builder builder = new Retrofit.Builder()
                                        .baseUrl("http://192.168.137.1:8080/miniprojet_android/")
                                        .addConverterFactory(GsonConverterFactory.create());
                                Retrofit retrofit = builder.build();
                                RestInterface restInterface = retrofit.create(RestInterface.class);
                                Call<Result> call = restInterface.edit_password(Integer.parseInt(etudient.getIdEtudient().toString()), txt_password2.getText().toString());
                                call.enqueue(new Callback<Result>() {
                                    @Override
                                    public void onResponse(Call<Result> call, Response<Result> response) {

                                        Result repos = response.body();
                                        MyDBHandler dhHandler = new MyDBHandler(getApplication(), null, null, 1);
                                        Result res = new Result();
                                        if(Integer.valueOf(repos.getSucces()) == 0)
                                        {
                                            Snackbar.make(view, "Erreur de modification!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        else if(Integer.valueOf(repos.getSucces()) == 1)
                                        {
                                            dhHandler.editStudentPassword(Integer.parseInt(etudient.getIdEtudient()), txt_password2.getText().toString());
                                            txt_password.setText("");
                                            txt_password2.setText("");
                                            txt_password3.setText("");
                                            Snackbar.make(view, "Modification avec succès", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                        else if(Integer.valueOf(repos.getSucces()) > 1)
                                        {
                                            Snackbar.make(view, "Plusieur etudiants avec le même identifiant!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result> call, Throwable t) {
                                        Snackbar.make(view, "Impossible d'accéder au serveur!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                });
                            }
                        }
                    });
                    // 3. Get the AlertDialog from create()
                    dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    Snackbar.make(view, "Il faut connecter à un réseau WIFI pour modifier votre mot de passe", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });
    }
}
