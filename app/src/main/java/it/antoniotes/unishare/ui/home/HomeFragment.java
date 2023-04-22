package it.antoniotes.unishare.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.AddFileActivity;
import it.antoniotes.unishare.InsegnamentiActivity;
import it.antoniotes.unishare.R;
import it.antoniotes.unishare.adapter.GridFavoriteAdapter;
import it.antoniotes.unishare.data.PDFFile;
import it.antoniotes.unishare.data.Preferito;
import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.databinding.FragmentHomeBinding;
import it.antoniotes.unishare.db.DBHelper;

public class HomeFragment extends Fragment {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name;
    EditText cerca;
    Button signOutBtn, registerBtn;
    LinearLayout btnInsegnamenti, btnAddFile, btnDIEEI;
    DBHelper db;

    List<Integer> mIDMateria;

    List<Preferito> mFavorites;
    RecyclerView mRecyclerView;

    UserSession session;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private FragmentHomeBinding binding;

    GridLayoutManager gridLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Inlfate mio da qua
        mRecyclerView = root.findViewById(R.id.grid_recyclerview);
        gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //Inizializzo la lista preferiti
        setupFavoritesGrid();
        //getListaFile();


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //-----------------CODICE QUI-----------------------//
        setDisplayedUsername();
        setBtnInsegnamenti();
        setBtnAddFile();
        setBtnDIEEI();
        fillInsegnamentiDB();

        //checkStoragePermission(getActivity());
        //List<String> lista = trovaFilePDF();





        //-----------------CODICE QUI-----------------------//
    }

    @Override
    public void onResume() {
        super.onResume();
        setupFavoritesGrid();

    }

    public void setBtnInsegnamenti(){
        btnInsegnamenti = (LinearLayout) getView().findViewById(R.id.btnInsegnamenti);
        btnInsegnamenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), InsegnamentiActivity.class);
                startActivity(i);
            }
        });
    }

    public void setBtnAddFile(){
        btnAddFile = (LinearLayout) getView().findViewById(R.id.btnAddFile);
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddFileActivity.class);
                startActivity(i);
            }
        });
    }

    public void setBtnDIEEI(){
        btnDIEEI = (LinearLayout) getView().findViewById(R.id.btnDIEEI);
        btnDIEEI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("android.intent.action.VIEW", Uri.parse("https://www.dieei.unict.it/"));
                startActivity(i);
            }
        });

    }

    public void setDisplayedUsername(){
        UserSession session = UserSession.getInstance();
        name = (TextView) getView().findViewById(R.id.nomeUtente);
        name.setText(session.getLoggedInUsername());
    }

    /*

        <item></item>
     */

    public void fillInsegnamentiDB(){
        String insegnamento1 = "Analisi Matematica 1";
        String prof1 = "S.J.N. Mosconi";
        int anno1 = 1;
        setInsegnamento(insegnamento1, prof1, anno1);


        String insegnamento2 = "Fondamenti di Informatica" ;
        String prof2 = "G. Malgeri";
        int anno2 = 2;
        setInsegnamento(insegnamento2, prof2, anno2);


        String insegnamento3 = "Chimica";
        String prof3 = "A. Siracusa";
        int anno3 = 1;
        setInsegnamento(insegnamento3, prof3, anno3);


        String insegnamento4 = "Algebra Lineare e Geometria";
        String prof4 = "P. Bonacini";
        int anno4 = 1;
        setInsegnamento(insegnamento4, prof4, anno4);

        String insegnamento5 = "Fisica 1";
        String prof5 = "F.M.D. Pellegrino'";
        int anno5 = 1;
        setInsegnamento(insegnamento5, prof5, anno5);


        String insegnamento6 = "Economia Applicata all'Ingegneria";
        String prof6 = "G. Mascali";
        int anno6 = 1;
        setInsegnamento(insegnamento6, prof6, anno6);


        String insegnamento7 = "Fisica 2";
        String prof7 = "S. Mirabella";
        int anno7 = 2;
        setInsegnamento(insegnamento7, prof7, anno7);


        String insegnamento8 = "Sistemi Operativi";
        String prof8 = "S. Cavalieri";
        int anno8 = 2;
        setInsegnamento(insegnamento8, prof8, anno8);


        String insegnamento9 = "Analisi Matematica 2";
        String prof9 ="L. Fanciullo";
        int anno9 = 2;
        setInsegnamento(insegnamento9, prof9, anno9);


        String insegnamento10 = "Programmazione Orientata ad Oggetti";
        String prof10 = "G. Mangioni";
        int anno10 = 2;
        setInsegnamento(insegnamento10, prof10, anno10);

        String insegnamento11 = "Elettrotecnica";
        String prof11 = "N. Salerno";
        int anno11 = 2;
        setInsegnamento(insegnamento11, prof11, anno11);


        String insegnamento12 = "Teoria dei Segnali";
        String prof12 = "L. Galluccio";
        int anno12 = 2;
        setInsegnamento(insegnamento12, prof12, anno12);


        String insegnamento13 = "Automatica";
        String prof13 = "R. Caponnetto";
        int anno13 = 3;
        setInsegnamento(insegnamento13, prof13, anno13);


        String insegnamento14 = "Calcolatori Elettronici";
        String prof14 = "G. Ascia";
        int anno14 = 3;
        setInsegnamento(insegnamento14, prof14, anno14);


        String insegnamento15 = "Elettronica";
        String prof15 = "E. Ragonese";
        int anno15 = 3;
        setInsegnamento(insegnamento15, prof15, anno15);


        String insegnamento16 = "Comunicazioni Digitali";
        String prof16 = "S. Serrano";
        int anno16 = 3;
        setInsegnamento(insegnamento16, prof16, anno16);


        String insegnamento17 = "Database and Web Programming";
        String prof17 = "N. Spampinato";
        int anno17 = 3;
        setInsegnamento(insegnamento17, prof17, anno17);
    }

    public void setInsegnamento(String insegnamento, String prof, int anno){
        db = new DBHelper(getContext());
        Boolean checkInsegnamento = db.checkInsegnamento(insegnamento, prof);
        if(checkInsegnamento== false){
            db.insertInsegnamentiData(insegnamento, prof, anno);
        }
    }

    public void setupFavoritesGrid(){
        UserSession session = UserSession.getInstance();
        db = new DBHelper(getContext());
        mFavorites = new ArrayList<Preferito>();
        int idUtente = db.getIDFromUsername(session.getLoggedInUsername());
        mIDMateria = db.getPreferitiByUserId(idUtente);
        if(!mIDMateria.isEmpty()) {
            for (int i : mIDMateria) {
                Preferito p = new Preferito(i, idUtente);
                mFavorites.add(p);
            }
        }
        GridFavoriteAdapter adapter = new GridFavoriteAdapter(getContext(), mFavorites);
        mRecyclerView.setAdapter(adapter);

    }

/*    public List<String> trovaFilePDF() {
        String path = "D:/files";
        File directory = new File(path);
        Log.d("Antonio", "Percordo directory " + directory.getAbsolutePath());
        List<String> filePDF = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectory = directory.listFiles();
            for (File file : subdirectory) {
                Log.d("Antonio", "Percordo subdirectory " + file.getAbsolutePath());
                File[] pdfFiles = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf")); // cerca i file PDF all'interno della sottocartella
                    for (File pdfFile : pdfFiles) {
                        Log.d("Antonio", "Percordo file pdf " + pdfFile.getAbsolutePath());
                        filePDF.add(pdfFile.getAbsolutePath()); // aggiungi il percorso del file PDF all'elenco
                    }
                }
            }
        return filePDF;
    }

    private void stampaListaFile(List<String> filePDF) {
        if(!filePDF.isEmpty()) {
            for (String filePath : filePDF) {
                Log.d("Antonio", "percorso " + filePath);
            }
            Log.d("Antonio", "lista vuota ");

        }
    }

    private void getListaFile(){
        List<PDFFile> listaPDFFile;
        listaPDFFile = db.getAllDocuments();
        for(PDFFile f : listaPDFFile){
            Log.d("Antonio", "PDFFile: " + f.getIdFile());
        }
    }*/

/*    public void checkStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }*/

}
