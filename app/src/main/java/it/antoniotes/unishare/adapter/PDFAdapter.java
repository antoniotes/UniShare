package it.antoniotes.unishare.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import it.antoniotes.unishare.R;
import it.antoniotes.unishare.data.PDFFile;
import it.antoniotes.unishare.data.UserSession;
import it.antoniotes.unishare.db.DBHelper;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.Holder> {

    private Context context;
    private List<PDFFile> listaPDF;

    DBHelper db;

    public PDFAdapter(Context context, List<PDFFile> listaPDF) {
        this.context = context;
        this.listaPDF = listaPDF;
    }


    @NonNull
    @Override
    public PDFAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PDFAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PDFAdapter.Holder holder, int position) {
        UserSession session = UserSession.getInstance();
        db = new DBHelper(context);
        int idUtente = db.getIDFromUsername(session.getLoggedInUsername());
        PDFFile PDFFile = listaPDF.get(position);
        String fileName = "Nome PDF";
/*        try {
            PDDocument document = PDDocument.load(PDFFile.getData());
            PDDocumentInformation info = document.getDocumentInformation();
            fileName = info.getTitle();

        } catch (IOException e) {
            // Gestisci l'eccezione IOException
            e.printStackTrace();
        }*/
        holder.titoloPDF.setText(fileName);
        holder.nomeInsegnamento.setText(PDFFile.getMateria());
        String nomeCreatore = db.getUsernameFromID(PDFFile.getIdCreatore());
        holder.nomeCreatore.setText(nomeCreatore);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //apriPDF(byte[] data);
                byte[] pdfData = PDFFile.getData();
                File temp = createTempFile(pdfData);
                Uri fileUri = FileProvider.getUriForFile(context, "it.antoniotes.unishare.provider", temp);
                openPdfFile(fileUri);
                //viewTempFile(temp, context);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaPDF != null ? listaPDF.size() : 0;    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView titoloPDF;
        private TextView nomeInsegnamento;
        private TextView nomeCreatore;

        private RelativeLayout card;




        public Holder(@NonNull View itemView) {
            super(itemView);
            titoloPDF = itemView.findViewById(R.id.titoloPDF);
            nomeInsegnamento = itemView.findViewById(R.id.insegnamentoPDF);
            nomeCreatore = itemView.findViewById(R.id.PDFowner);
            card = itemView.findViewById(R.id.root_layout);
        }
    }

    public java.io.File createTempFile(byte[] fileBytes) {
        FileOutputStream fos = null;
        java.io.File tempFile = null;
        try {
            // Creazione di un file temporaneo

            tempFile = java.io.File.createTempFile("temp", ".pdf");

            // Scrittura dei byte del file sul file temporaneo
            fos = new FileOutputStream(tempFile);
            fos.write(fileBytes);

            // Visualizzazione del file temporaneo
            // ...
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tempFile;
        }
    }

    public void viewTempFile(File file, Context context) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }

    private void openPdfFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public void updateData(List<PDFFile> pdfFiles) {
        this.listaPDF = pdfFiles;
        notifyDataSetChanged();
    }

}
