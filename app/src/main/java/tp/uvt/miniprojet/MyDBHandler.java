package tp.uvt.miniprojet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    // Database Info
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mini_projet.db";
    private static final String TABLE_MATIERES = "matieres";
    private static final String TABLE_EXAMENS = "examens";
    private static final String TABLE_ETUDIENT = "etudient";
    private static final String TABLE_CLASSE = "classe";
    private static final String TABLE_EXAMENS_MATIERE = "examens_matiere";


    // Table Matiéres
    private static final String CLASSE_ID = "_id_ma";
    private static final String CLASSE_TITRE = "_titre";
    // Table Matiéres
    private static final String MATIERES_ID = "_id_ma";
    private static final String MATIERES_TITRE = "_titre";
    private static final String MATIERES_DESCRIPTION = "_desc";
    private static final String MATIERES_MOYENNE = "_moy";
    // Table Examens
    private static final String EXAMENS_ID = "_id_ex";
    private static final String EXAMENS_TITRE = "_titre";
    private static final String EXAMENS_DATE_OPERATION = "_date_op";
    private static final String EXAMENS_DATE_UPDATE = "_date_up";
    private static final String EXAMENS_NOTE = "_note";
    private static final String EXAMENS_COEF = "_coef";
    // Table ETUDIENT
    private static final String ETUDIENT_ID = "_id_etudient";
    private static final String ETUDIENT_NOM = "_nom";
    private static final String ETUDIENT_PRENOM = "_prenom";
    private static final String ETUDIENT_EMAIL = "_eamil";
    private static final String ETUDIENT_PASSWORD = "_password";
    private static final String ETUDIENT_DERNIERSYNC = "_dernier_sync";
    private static final String ETUDIENT_IDCLASSE = "_id_classe";
    private static final String ETUDIENT_LEBELLECLASSE = "_libelle_classe";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TACHES;

        CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_CLASSE + "(" + CLASSE_ID + " INTEGER PRIMARY KEY," + CLASSE_TITRE + " TEXT)";
        db.execSQL(CREATE_TABLE_TACHES);

        CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_MATIERES + "(" + MATIERES_ID + " INTEGER PRIMARY KEY," + MATIERES_TITRE + " TEXT," + MATIERES_DESCRIPTION + " TEXT," + MATIERES_MOYENNE + " TEXT)";
        db.execSQL(CREATE_TABLE_TACHES);

        CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_EXAMENS + "(" + EXAMENS_ID + " INTEGER PRIMARY KEY," + EXAMENS_TITRE + " TEXT)";
        db.execSQL(CREATE_TABLE_TACHES);

        CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_EXAMENS_MATIERE + "(" + EXAMENS_ID + " INTEGER NOT NULL," + MATIERES_ID + " INTEGER NOT NULL," + EXAMENS_DATE_OPERATION + " TEXT," + EXAMENS_DATE_UPDATE + " TEXT," + EXAMENS_NOTE + " TEXT," + EXAMENS_COEF + " TEXT )";
        db.execSQL(CREATE_TABLE_TACHES);

        CREATE_TABLE_TACHES = "CREATE TABLE " + TABLE_ETUDIENT + "(" + ETUDIENT_ID + " INTEGER NOT NULL PRIMARY KEY," + ETUDIENT_NOM + "  TEXT," + ETUDIENT_PRENOM + " TEXT," + ETUDIENT_EMAIL + " TEXT," + ETUDIENT_PASSWORD + " TEXT," + ETUDIENT_DERNIERSYNC + " TEXT," + ETUDIENT_IDCLASSE + " TEXT);";
        db.execSQL(CREATE_TABLE_TACHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ETUDIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATIERES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMENS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMENS_MATIERE);
        onCreate(db);
    }
    public void addClasse(Classe classe)
    {
        ContentValues values = new ContentValues();
        values.put(CLASSE_ID,Integer.parseInt(classe.getIdClasse()));
        values.put(CLASSE_TITRE, classe.getLibelle());
        List<Matiere> matierelist = classe.getMatieres();
        Iterator item = matierelist.iterator();
        while(item.hasNext()){
            Matiere o = (Matiere) item.next();
            Matiere mat = new Matiere();
            mat.setIdMatiere(o.getIdMatiere());
            mat.setLibelle(o.getLibelle());
            mat.setTuteur(o.getTuteur());
            mat.setMoyenne(o.getMoyenne());
            mat.setExamens(o.getExamens());
            addMatiere(mat);
        };
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CLASSE, null, values);
        db.close();
    }
    public void addMatiere(Matiere matiere)
    {
        ContentValues values = new ContentValues();
        values.put(MATIERES_ID, Integer.parseInt(matiere.getIdMatiere()));
        values.put(MATIERES_TITRE, matiere.getLibelle());
        values.put(MATIERES_DESCRIPTION, matiere.getTuteur());
        values.put(MATIERES_MOYENNE, matiere.getMoyenne());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_MATIERES, null, values);
        db.close();

        List<Examen> examenlist = matiere.getExamens();
        Iterator item = examenlist.iterator();
        while(item.hasNext()){
            Examen o = (Examen) item.next();
            Examen examen = new Examen();
            examen.setIdExamen(o.getIdExamen());
            examen.setLibelle(o.getLibelle());
            examen.setDateOperation(o.getDateOperation());
            examen.setDateUpdate(o.getDateUpdate());
            examen.setNote(o.getNote());
            examen.setCoeff(o.getCoeff());
            addExamen(examen);
            addExamenMatiere(Integer.parseInt(matiere.getIdMatiere()) ,Integer.parseInt(o.getIdExamen()), examen);
        };
    }
    public void addExamen(Examen examen)
    {
        if(!IsExamens(Integer.parseInt(examen.getIdExamen())))
        {
            ContentValues values = new ContentValues();
            values.put(EXAMENS_ID, Integer.parseInt(examen.getIdExamen()));
            values.put(EXAMENS_TITRE, examen.getLibelle());
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_EXAMENS, null, values);
            db.close();
        }
    }
    public void addExamenMatiere(int id_matiere, int id_examen, Examen examen)
    {
        ContentValues values = new ContentValues();
        values.put(EXAMENS_ID, id_examen);
        values.put(MATIERES_ID, id_matiere);
        values.put(EXAMENS_DATE_OPERATION, examen.getDateOperation());
        values.put(EXAMENS_DATE_UPDATE, examen.getDateUpdate());
        values.put(EXAMENS_NOTE, examen.getNote());
        values.put(EXAMENS_COEF, examen.getCoeff());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EXAMENS_MATIERE, null, values);
        db.close();
    }
    public ArrayList<Matiere> findMatieres()
    {
        ArrayList<Matiere> Matieres = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MATIERES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query , null);
        if(cursor.moveToFirst())
        {
            do {
                Matiere t = new Matiere();
                t.setIdMatiere(cursor.getString(0));
                t.setLibelle(cursor.getString(1));
                t.setTuteur(cursor.getString(2));
                if(cursor.getString(3).toString()!= "-")
                {
                    t.setMoyenne(cursor.getString(3));
                }
                else
                {
                    t.setMoyenne("-");
                }
                //t.setMoyenne(cursor.getString(3));
                Matieres.add(t);
            } while(cursor.moveToNext());
        }
        db.close();
        return Matieres;
    }
    public ArrayList<Examen> findExamens(int id_matiere)
    {
        ArrayList<Examen> Examens = new ArrayList<>();
        Examen t;

        String query1 = "SELECT * FROM " + TABLE_EXAMENS_MATIERE + " WHERE " + MATIERES_ID + " = " + id_matiere;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(query1 , null);
        if(cursor1.moveToFirst())
        {
            do {
                String query = "SELECT * FROM " + TABLE_EXAMENS + " WHERE " + EXAMENS_ID + " = " + cursor1.getInt(0);
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(query , null);
                if(cursor.moveToFirst())
                {
                    do {
                        t = new Examen();
                        t.setIdExamen(cursor.getString(0));
                        t.setLibelle(cursor.getString(1));
                        t.setDateOperation(cursor1.getString(2));
                        t.setDateUpdate(cursor1.getString(3));
                        if(cursor1.getString(4).toString() != "-")
                        {
                            t.setNote(cursor1.getString(4));
                        }
                        else
                        {
                            t.setNote("-");
                        }
                        //t.setNote(cursor1.getString(4));
                        t.setCoeff(cursor1.getString(5));
                        Examens.add(t);
                    } while(cursor.moveToNext());
                }
                db.close();
            } while(cursor1.moveToNext());
        }
        db1.close();
        return Examens;
    }
    public boolean IsExamens(int id_examen)
    {
        ArrayList<Examen> Examens = new ArrayList<>();
        Examen t;
        boolean existe = false;
        String query1 = "SELECT * FROM " + TABLE_EXAMENS + " WHERE " + EXAMENS_ID + " = " + id_examen;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(query1 , null);
        if(cursor1.moveToFirst())
        {
            existe = true;
        }
        db1.close();
        return existe;
    }
    public Etudient findStudent()
    {
        Etudient student = null;
        String query1 = "SELECT * FROM " + TABLE_ETUDIENT ;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(query1 , null);
        if(cursor1.moveToFirst())
        {
            student = new Etudient();
            student.setIdEtudient(cursor1.getString(0));
            student.setNom(cursor1.getString(1));
            student.setPrenom(cursor1.getString(2));
            student.setEmail(cursor1.getString(3));
            student.setPassword(cursor1.getString(4));
            student.setDernierSync(cursor1.getString(5));
            student.setIdClasse(cursor1.getString(6));
        }
        db1.close();
        return student;
    }
    public Classe findClasse(int id_classe)
    {
        Classe classe = null;
        String query1 = "SELECT * FROM " + TABLE_CLASSE + " WHERE " + CLASSE_ID + " = " + id_classe;
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(query1 , null);
        if(cursor1.moveToFirst())
        {
            classe = new Classe();
            classe.setIdClasse(cursor1.getString(0));
            classe.setLibelle(cursor1.getString(1));
        }
        db1.close();
        return classe;
    }
    public void addStudent(Etudient student)
    {
        clearTable(TABLE_ETUDIENT);
        ContentValues values = new ContentValues();
        values.put(ETUDIENT_ID, student.getIdEtudient());
        values.put(ETUDIENT_NOM, student.getNom());
        values.put(ETUDIENT_PRENOM, student.getPrenom());
        values.put(ETUDIENT_EMAIL, student.getEmail());
        values.put(ETUDIENT_PASSWORD, student.getPassword());
        values.put(ETUDIENT_DERNIERSYNC, student.getDernierSync());
        values.put(ETUDIENT_IDCLASSE, student.getIdClasse());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ETUDIENT, null, values);
        db.close();
    }
    public void editStudentPassword(int id, String password)
    {
        ContentValues values = new ContentValues();
        values.put(ETUDIENT_PASSWORD, password);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_ETUDIENT, values, ETUDIENT_ID + "=" + id, null);
        db.close();
    }
    public void editStudentSync(int id, String date_update)
    {
        ContentValues values = new ContentValues();
        values.put(ETUDIENT_DERNIERSYNC, date_update);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_ETUDIENT, values, ETUDIENT_ID + "=" + id, null);
        db.close();
    }
    public int clearTable(String TABLE)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, null, null);
    }



}
