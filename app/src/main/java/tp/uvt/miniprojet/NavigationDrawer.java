package tp.uvt.miniprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Matiere> Matieres;
    MyCustomAdapter myCustomAdapter;
    ListView list;
    //    Button bt_add;
    private static final String TABLE_CLASSE = "classe";
    private static final String TABLE_MATIERES = "matieres";
    private static final String TABLE_EXAMENS = "examens";
    private static final String TABLE_EXAMENS_MATIERE = "examens_matiere";
    private int id_etudient = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        Etudient student ;
        MyDBHandler dhHandler = new MyDBHandler(getApplication(), null, null, 1);
        student = dhHandler.findStudent();
        id_etudient = Integer.parseInt(student.getIdEtudient());


        View headerView = navigationView.getHeaderView(0);
        TextView txt_username = (TextView) headerView.findViewById(R.id.txt_username);
        txt_username.setText(student.getNom() + " " + student.getPrenom());
        TextView txt_email = (TextView) headerView.findViewById(R.id.txt_email);
        txt_email.setText(student.getEmail());
        list = (ListView)findViewById(R.id.list_matieres);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //myCustomAdapter.getItemId(position) // id matiére
                Intent myIntent = new Intent(getApplication(), ExamensActivity.class);
                Bundle b = new Bundle();
                b.putInt("id_martiere", (int)myCustomAdapter.getItemId(position));
                myIntent.putExtras(b);
                startActivity(myIntent);
            }
        });
        GetData();
    }
    //------------------------------------------------

    void GetData()
    {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            AlertDialog.Builder builder;
            AlertDialog dialog;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation!");
            builder.setMessage("Etes-vous sur de vouloir synchronisez vous-données!");
            builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getMatieres();
                }
            });
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Sync_Data();
                }
            });
            // 3. Get the AlertDialog from create()
            dialog = builder.create();
            dialog.show();
        }
        else
        {
            getMatieres();
        }
    }
    void Sync_Data()
    {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI)
        {
            //TODO: authentifaction with external data base
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://192.168.137.1:8080/miniprojet_android/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            RestInterface restInterface = retrofit.create(RestInterface.class);
            Call<Classe> call = restInterface.getClasseInfo(id_etudient);
            call.enqueue(new Callback<Classe>() {
                @Override
                public void onResponse(Call<Classe> call, Response<Classe> response) {
                    Classe repos = response.body();
                    MyDBHandler dhHandler = new MyDBHandler(getApplication(), null, null, 1);
                    Classe myClasse = new Classe();
                    if(Integer.valueOf(repos.getIdClasse()) != -1)
                    {
                        dhHandler.clearTable(TABLE_CLASSE);
                        dhHandler.clearTable(TABLE_MATIERES);
                        dhHandler.clearTable(TABLE_EXAMENS);
                        dhHandler.clearTable(TABLE_EXAMENS_MATIERE);

                        myClasse.setIdClasse(repos.getIdClasse());
                        myClasse.setLibelle(repos.getLibelle());
                        myClasse.setMatieres(repos.getMatieres());
                        dhHandler.addClasse(myClasse);
                        Retrofit.Builder builder2 = new Retrofit.Builder()
                                .baseUrl("http://192.168.137.1:8080/miniprojet_android/")
                                .addConverterFactory(GsonConverterFactory.create());
                        Retrofit retrofit2 = builder2.build();
                        RestInterface restInterface2 = retrofit2.create(RestInterface.class);
                        Call<Result> call2 = restInterface2.edit_date_sync(id_etudient);
                        call2.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                Result repos2 = response.body();
                                MyDBHandler dhHandler = new MyDBHandler(getApplication(), null, null, 1);

                                if(Integer.valueOf(repos2.getSucces()) == 0)
                                {
                                    Snackbar.make(getCurrentFocus(), "Erreur de Syncronisation!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else if(Integer.valueOf(repos2.getSucces()) == 1)
                                {
                                    dhHandler.editStudentSync(id_etudient, repos2.getDate());
                                    Snackbar.make(getCurrentFocus(), "Syncronisation avec succès", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                                else if(Integer.valueOf(repos2.getSucces()) > 1)
                                {
                                    Snackbar.make(getCurrentFocus(), "Plusieur etudiants avec le même identifiant!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                Snackbar.make(getCurrentFocus(), "Erreur d'acces au serveur :(" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        });
                        getMatieres();
                    }
                    else
                    {
                        Snackbar.make(getCurrentFocus(), "Paramètres invalide :(", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                }

                @Override
                public void onFailure(Call<Classe> call, Throwable t) {
                    Snackbar.make(getCurrentFocus(), "Erreur d'acces au serveur :(" , Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }


    public void getMatieres()
    {
        MyDBHandler dhHandler = new MyDBHandler(this, null, null, 1);
        Matieres = dhHandler.findMatieres();
        if(Matieres != null)
        {
            myCustomAdapter = new MyCustomAdapter(Matieres);
            list.setAdapter(myCustomAdapter);
        }
    }


    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<Matiere> Items = new ArrayList<Matiere>();
        MyCustomAdapter(ArrayList<Matiere> Items)
        {
            this.Items = Items;
        }
        @Override
        public int getCount(){
            return Items.size();
        }
        @Override
        public String getItem(int Position){
            return Items.get(Position).getLibelle();
        }
        @Override
        public long getItemId(int Position){ return Integer.parseInt(Items.get(Position).getIdMatiere()); }
        @Override
        public View getView(int i, View view, ViewGroup viewgroup){
            LayoutInflater linflater = getLayoutInflater();
            View view1 = linflater.inflate(R.layout.row_view, null);

            TextView txtname = (TextView)view1.findViewById(R.id.txt_name);
            TextView txtdesc= (TextView)view1.findViewById(R.id.txt_desc);
            TextView txtdate= (TextView)view1.findViewById(R.id.txt_date);
            txtname.setText(Items.get(i).getLibelle());
            txtdesc.setText(Items.get(i).getTuteur());
            txtdate.setText(Items.get(i).getMoyenne());
            return  view1;
        }

    }

    //------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(getApplication(), EditProfilActivity.class);
            Bundle b = new Bundle();
            b.putInt("id_etudient",Integer.valueOf(id_etudient));
            myIntent.putExtras(b);
            startActivity(myIntent);

        } else if (id == R.id.nav_share) {
            String url = "https://www.facebook.com/institut.superieur.informatique/";
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            String url = "http://www.isi.rnu.tn/Fr/accueil_46_4";
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
