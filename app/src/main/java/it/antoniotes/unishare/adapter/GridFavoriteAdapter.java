package it.antoniotes.unishare.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.antoniotes.unishare.LoginActivity;
import it.antoniotes.unishare.R;
import it.antoniotes.unishare.ShowFileActivity;
import it.antoniotes.unishare.data.Preferito;
import it.antoniotes.unishare.db.DBHelper;

public class GridFavoriteAdapter extends RecyclerView.Adapter<GridFavoriteAdapter.GridFavoriteViewHolder> {
    private Context mContext;
    private List<Preferito> mFavorites;


    public GridFavoriteAdapter(Context context, List<Preferito> favorites) {
        mContext = context;
        mFavorites = favorites;
    }

    @NonNull
    @Override
    public GridFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_layout, parent, false);
        return new GridFavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridFavoriteViewHolder holder, int position) {
        Preferito favorite = mFavorites.get(position);
        DBHelper db = new DBHelper(mContext);
        int folder = R.drawable.baseline_folder_24;

        // Set the data to the view elementscome

        holder.favText.setText(truncateString(db.getNomeInsegnamento(favorite.getIdInsegnamento())));
        holder.favImage.setImageResource(folder);
        holder.favImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ShowFileActivity.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public static class GridFavoriteViewHolder extends RecyclerView.ViewHolder {
        public ImageView favImage;
        public TextView favText;


        public GridFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favImage = itemView.findViewById(R.id.picFav);
            favText = itemView.findViewById(R.id.fav);
        }
    }

    private String truncateString(String text) {
        final int MAX_LENGTH = 8;
        if (text.length() > MAX_LENGTH) {
            return text.substring(0, MAX_LENGTH) + "...";
        } else {
            return text;
        }
    }
}