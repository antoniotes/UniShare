package it.antoniotes.unishare.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.R;
import it.antoniotes.unishare.data.Insegnamento;
import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.db.DBHelper;

public class InsegnamentiAdapter extends RecyclerView.Adapter<InsegnamentiAdapter.Holder> {
    private Context context;
    private List<Insegnamento> listaInsegnamenti;

    DBHelper db;
    private List<Integer> favoriteList;


    public InsegnamentiAdapter(Context context, List<Insegnamento> listaInsegnamenti) {
        this.context = context;
        this.listaInsegnamenti = listaInsegnamenti;
    }


    public void setListaInsegnamenti(List<Insegnamento> listaInsegnamenti) {
        this.listaInsegnamenti = listaInsegnamenti;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InsegnamentiAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.layout_item_insegnamento, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InsegnamentiAdapter.Holder holder, int position) {
        UserSession session = UserSession.getInstance();
        db = new DBHelper(context);
        int idUtente = db.getIDFromUsername(session.getLoggedInUsername());
        favoriteList = db.getPreferitiByUserId(idUtente);
        //logIntList(favoriteList, "Preferiti");
        listaInsegnamenti = getAllInsegnamenti();
        Insegnamento insegnamento = listaInsegnamenti.get(position);
        holder.titoloInsegnamento.setText(insegnamento.getNomeInsegnamento());
        holder.nomeProf.setText(insegnamento.getNomeProfessore());
        int idInsegnamento = db.getInsegnamentoID(holder.titoloInsegnamento.getText().toString(), holder.nomeProf.getText().toString());
        if(favoriteList.isEmpty()!=true) {
            for (int i : favoriteList) {
                if (i == idInsegnamento) {
                    cambiaSorgente(holder.like, R.drawable.cuorerosso);
                    holder.like.setTag("liked");
                }
            }
        }

        //verifica preferiti
        holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Apro cartella "+ holder.titoloInsegnamento.getText(), Toast.LENGTH_SHORT).show();

                }
            });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implementare verifiche di DB
                Log.d("antonio", "idUtente ="+ idUtente);
                Log.d("antonio", "titolo insegnamento ="+ holder.titoloInsegnamento.getText().toString());
                Log.d("antonio", "nome prof ="+ holder.nomeProf.getText().toString());
                Log.d("antonio", "idInsegnamento ="+ idInsegnamento);

                if(holder.like.getTag().equals("unliked")) {
                    boolean added = addFavorite(idUtente, idInsegnamento);
                    if (added == true) {
                        Log.d("antonio", "Aggiungo insegnamento ="+ idInsegnamento);
                        Toast.makeText(context, "Aggiunto ai preferiti " + holder.titoloInsegnamento.getText(), Toast.LENGTH_SHORT).show();
                        cambiaSorgente(holder.like, R.drawable.cuorerosso);
                        holder.like.setTag("liked");
                        Log.d("antonio", "Aggiungo Tag ="+ holder.like.getTag());
                    }

                } else if (holder.like.getTag().equals("liked")) {
                    Boolean removed = db.deleteFavorite(idUtente, idInsegnamento);
                    if (removed == true){
                        Log.d("antonio", "Cancello insegnamento ="+ idInsegnamento);
                        Toast.makeText(context, "Rimosso dai preferiti " + holder.titoloInsegnamento.getText(), Toast.LENGTH_SHORT).show();
                        cambiaSorgente(holder.like, R.drawable.cuore);
                        holder.like.setTag("unliked");
                        Log.d("antonio", "Aggiungo Tag ="+ holder.like.getTag());
                    }

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return listaInsegnamenti != null ? listaInsegnamenti.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView titoloInsegnamento;
        private TextView nomeProf;
        private ImageView like;

        private RelativeLayout card;




        public Holder(@NonNull View itemView) {
            super(itemView);
            titoloInsegnamento = itemView.findViewById(R.id.titoloInsegnamento);
            nomeProf = itemView.findViewById(R.id.profInsegnamento);
            like = itemView.findViewById(R.id.like);
            like.setTag("unliked");
            card = itemView.findViewById(R.id.root_layout);
        }
    }

    public void cambiaSorgente(ImageView imageView, int nuovaSrc) {
        imageView.setImageResource(nuovaSrc);
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


    public Boolean addFavorite(int idUser, int idMateria){
        return db.insertFavorite(idUser, idMateria);
    }

    public void logIntList(List<Integer> intList, String tag) {
        for (int i : intList) {
            Log.d(tag, String.valueOf(i));
        }
    }


}
