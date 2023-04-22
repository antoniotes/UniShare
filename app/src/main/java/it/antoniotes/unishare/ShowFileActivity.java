package it.antoniotes.unishare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.adapter.PDFAdapter;
import it.antoniotes.unishare.data.PDFFile;
import it.antoniotes.unishare.db.DBHelper;

public class ShowFileActivity extends AppCompatActivity {
    private Context context;

    private RecyclerView rv_file;
    private PDFAdapter pdfAdapter;

    private List<PDFFile> PDFFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);
        context = ShowFileActivity.this;
        rv_file = (RecyclerView) findViewById(R.id.PDFView);

        PDFFiles = getAllFile();

        pdfAdapter = new PDFAdapter(context, PDFFiles);

        rv_file.setLayoutManager(new LinearLayoutManager(this));
        rv_file.setAdapter(pdfAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        PDFFiles = getAllFile();
        pdfAdapter.updateData(PDFFiles);
    }

    public List<PDFFile> getAllFile() {
        List<PDFFile> PDFFileList = new ArrayList<>();

        // Creare un'istanza di SQLiteOpenHelper
        DBHelper dbHelper = new DBHelper(context);

        // Ottieni un'istanza di SQLiteDatabase
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query per tutti gli elementi della tabella "Insegnamenti"
        PDFFileList = dbHelper.getAllDocuments();

        return PDFFileList;
    }
}