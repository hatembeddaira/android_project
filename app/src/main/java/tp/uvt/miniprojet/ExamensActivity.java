package tp.uvt.miniprojet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExamensActivity extends AppCompatActivity {
    ArrayList<Examen> Examens;
    MyCustomAdapter myCustomAdapter;
    ListView list;
    int id_matiere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examens);

        Bundle  b = getIntent().getExtras();
        id_matiere = b.getInt("id_martiere");
        //Toast.makeText(this, "id_matiere :" + id_matiere, Toast.LENGTH_LONG).show();

//        final ArrayList<ListItem> Items = new ArrayList<ListItem>();
//        Items.add(new ListItem(5, "DS1","Passé le 12/10/2017", "12.75"));
//        Items.add(new ListItem(41, "DS2","Passé le 12/11/2017", "19.25"));
//        Items.add(new ListItem(515, "Examen principale","Passé le 12/12/2017", "-"));
//        Items.add(new ListItem(515, "Examen comntrole","Passé le 30/01/2017", "-"));
//        final MyCustomAdapter myCustomAdapter = new MyCustomAdapter(Items);



        list = (ListView)findViewById(R.id.list_examens);
        getExamens();



    }
    public void getExamens()
    {
        MyDBHandler dhHandler = new MyDBHandler(this, null, null, 1);
        Examens = dhHandler.findExamens(id_matiere);
        if(Examens != null)
        {
            myCustomAdapter = new MyCustomAdapter(Examens);
            list.setAdapter(myCustomAdapter);
        }
    }




    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<Examen> Items = new ArrayList<Examen>();
        MyCustomAdapter(ArrayList<Examen> Items)
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
        public long getItemId(int Position){
            return Integer.parseInt(Items.get(Position).getIdExamen());
        }
        @Override
        public View getView(int i, View view, ViewGroup viewgroup){
            LayoutInflater linflater = getLayoutInflater();
            View view1 = linflater.inflate(R.layout.row_view, null);

            TextView txtname = (TextView)view1.findViewById(R.id.txt_name);
            TextView txtdesc= (TextView)view1.findViewById(R.id.txt_desc);
            TextView txtdate= (TextView)view1.findViewById(R.id.txt_date);
            txtname.setText(Items.get(i).getLibelle());
            txtdesc.setText(Items.get(i).getDateOperation());
            txtdate.setText(Items.get(i).getNote());
            return  view1;
        }

    }
}
