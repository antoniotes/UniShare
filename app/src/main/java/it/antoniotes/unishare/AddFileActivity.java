package it.antoniotes.unishare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.data.Insegnamento;
import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.db.DBHelper;

public class AddFileActivity extends AppCompatActivity {
    Button browse;
    Spinner spinnerProfessori;
    Spinner spinnerInsegnamenti;
    Button addFile;
    DBHelper db;
    private static final int PICK_PDF_FILE = 1;
    byte[] file;
    List<Insegnamento> insegnamentoList;

    String materiaScelta;
    String professoreScelto;


    int check1 = 0;
    int check2 = 0;
    int check3 = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        db = new DBHelper(this);
        getInsegnamentoList();
        disableSpinnerProfessori();
        //setSpinnerProfessori();
        setSpinnerInsegnamenti();
        setInsertButton();

        browse = findViewById(R.id.browsefile);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String mimeType = getContentResolver().getType(uri);

            if (mimeType != null && mimeType.equals("application/pdf")) {
                try {
                    ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
                    if (pfd != null) {
                        FileDescriptor fd = pfd.getFileDescriptor();
                        FileInputStream inputStream = new FileInputStream(fd);
                        long fileSize = pfd.getStatSize();
                        if (fileSize > 10240) {
                            // il file è troppo grande, gestisci l'errore di conseguenza
                        }
                        byte[] buffer = new byte[10240];
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        file = outputStream.toByteArray();
                        if (file != null) {
                            check1 = 1;
                            browse.setText("PDFFile Selezionato");
                        }
                        outputStream.close();
                        inputStream.close();
                        pfd.close();
                        openPdfFile(uri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Scegli un file PDF", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_PDF_FILE);
    }
    private void openPdfFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private File getTempFile() {
        File file = null;
        try {
            file = File.createTempFile("temp", ".pdf", getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setSpinnerInsegnamenti(){
        spinnerInsegnamenti = findViewById(R.id.spinnerInsegnamenti);
        List<String> nomiInsegnamenti = new ArrayList<>();
        for(Insegnamento insegnamento : insegnamentoList) {
            nomiInsegnamenti.add(insegnamento.getNomeInsegnamento());
        }
        ArrayAdapter<String> elementi = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.spinner_dropdown_item,
                nomiInsegnamenti
    );

        elementi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInsegnamenti.setAdapter(elementi);
        spinnerInsegnamenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Logica da implementare quando utente seleziona elemento dalla lista*/
                String insegnamentoScelto = (String) parent.getItemAtPosition(position);
                ableSpinnerProfessori();
                setSpinnerProfessori(insegnamentoScelto);
                materiaScelta = insegnamentoScelto;
                check2 = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Seleziona un elemento dallo spinner!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSpinnerProfessori(String insegnamentoScelto){
        spinnerProfessori = findViewById(R.id.spinnerProfessori);
        List<String> nomiProfessori = new ArrayList<>();
        for(Insegnamento insegnamento : insegnamentoList) {
            if (insegnamento.getNomeInsegnamento().equals(insegnamentoScelto)) {
                nomiProfessori.add(insegnamento.getNomeProfessore());
            }
        }
        ArrayAdapter<String> elementi = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.spinner_dropdown_item,
                nomiProfessori
        );
        elementi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfessori.setAdapter(elementi);
        spinnerProfessori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Logica da implementare quando utente seleziona elemento dalla lista*/
                String profScelto = (String) parent.getItemAtPosition(position);
                professoreScelto = profScelto;
                check3 = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Seleziona un elemento dallo spinner!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setInsertButton(){
        Log.d("Antonio","Check1 = " + check1);
        Log.d("Antonio","Check2 = " + check2);
        Log.d("Antonio","Check3 = " + check3);
        addFile = findViewById(R.id.insertButton);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check1 == 1 && check2 == 1 && check3 == 1){
                    Log.d("Antonio","Check1 = " + check1);
                    Log.d("Antonio","Check2 = " + check2);
                    Log.d("Antonio","Check3 = " + check3);
                    UserSession userSession = UserSession.getInstance();
                    String user = userSession.getLoggedInUsername();
                    int idUser = db.getIDFromUsername(user);

                    Boolean checkInsert = db.insertFile(file, materiaScelta, professoreScelto, 0, idUser);
                    if(checkInsert==true){
                        Toast.makeText(getApplicationContext(), "Caricamento riuscito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Caricamento non riuscito", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    private byte[] downloadPdf(Uri uri) {
        byte[] bytes = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private void openPdf(byte[] pdfBytes) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getCacheDir() + "/temp.pdf"), "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            FileOutputStream fos = new FileOutputStream(new File(getCacheDir(), "temp.pdf"));
            fos.write(pdfBytes);
            fos.close();
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disableSpinnerProfessori(){
        spinnerProfessori = findViewById(R.id.spinnerProfessori);
        spinnerProfessori.setEnabled(false);
    }

    public void ableSpinnerProfessori(){
        spinnerProfessori = findViewById(R.id.spinnerProfessori);
        spinnerProfessori.setEnabled(true);
    }

    public void getInsegnamentoList(){
        insegnamentoList = new ArrayList<>();
        insegnamentoList = db.getAllInsegnamenti();
    }



}

