package it.antoniotes.unishare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.adapter.InsegnamentiAdapter;
import it.antoniotes.unishare.data.Insegnamento;
import it.antoniotes.unishare.db.DBHelper;

public class InsegnamentiActivity extends AppCompatActivity {
    private Context context;

    private RecyclerView rv_insegnamenti;
    private InsegnamentiAdapter insegnamentiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insegnamenti);
        context = InsegnamentiActivity.this;
        rv_insegnamenti = (RecyclerView) findViewById(R.id.favView);

        List<Insegnamento> insegnamenti = getAllInsegnamenti();

        insegnamentiAdapter = new InsegnamentiAdapter(context,insegnamenti);

        rv_insegnamenti.setLayoutManager(new LinearLayoutManager(this));
        rv_insegnamenti.setAdapter(insegnamentiAdapter);


    }

    public List<Insegnamento> getAllInsegnamenti() {
        List<Insegnamento> insegnamentiList = new ArrayList<>();

        // Creare un'istanza di SQLiteOpenHelper
        DBHelper dbHelper = new DBHelper(context);

        // Ottieni un'istanza di SQLiteDatabase
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query per tutti gli elementi della tabella "Insegnamenti"
        Cursor cursor = db.rawQuery("SELECT * FROM Insegnamenti WHERE id >= 0 AND nomeInsegnamento IS NOT NULL AND nomeDocente IS NOT NULL AND anno >= 0", null);

        // Scansiona il Cursor e crea un oggetto Insegnamento per ogni riga
        while (cursor.moveToNext()) {
            String nomeInsegnamento = cursor.getString(cursor.getColumnIndexOrThrow("nomeInsegnamento"));
            String nomeProf = cursor.getString(cursor.getColumnIndexOrThrow("nomeDocente"));
            int anno = cursor.getInt(cursor.getColumnIndexOrThrow("anno"));
            if (nomeInsegnamento != null && nomeProf != null && anno != -1) {
                Insegnamento insegnamento = new Insegnamento(nomeInsegnamento, nomeProf, anno);
                insegnamentiList.add(insegnamento);
            }
        }

        // Chiudi il Cursor e il database
        cursor.close();
        db.close();

        return insegnamentiList;
    }
}