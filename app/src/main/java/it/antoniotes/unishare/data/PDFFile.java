package it.antoniotes.unishare.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "files", indices = {@Index(value = {"idFile"}, unique = true)}, foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "idCreatore",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class PDFFile {

    @PrimaryKey
    private int idFile;
    private byte[] data;
    private String materia;
    private String professore;
    private float valutazione;

    private int idCreatore;
    public PDFFile(int idFile, byte[] data, String materia, String professore, float valutazione, int idCreatore) {
        this.idFile = idFile;
        this.data = data;
        this.materia = materia;
        this.professore = professore;
        this.valutazione = valutazione;
        this.idCreatore = idCreatore;
    }

    public int getIdFile() {
        return idFile;
    }

    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getProfessore() {
        return professore;
    }

    public void setProfessore(String professore) {
        this.professore = professore;
    }

    public float getValutazione() {
        return valutazione;
    }

    public void setValutazione(float valutazione) {
        this.valutazione = valutazione;
    }

    public int getIdCreatore() {
        return idCreatore;
    }

    public void setIdCreatore(int idCreatore) {
        this.idCreatore = idCreatore;
    }
}
