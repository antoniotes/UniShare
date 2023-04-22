package it.antoniotes.unishare.data;

import androidx.room.PrimaryKey;

public class Insegnamento {
     @PrimaryKey(autoGenerate = true)
        private Long id;
        private String nomeInsegnamento;
        private String nomeProfessore;

        private int anno;

    public Insegnamento() {
    }

    public Insegnamento(String nomeInsegnamento, String nomeProfessore, int anno) {
        this.nomeInsegnamento = nomeInsegnamento;
        this.nomeProfessore = nomeProfessore;
        this.anno = anno;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeInsegnamento() {
        return nomeInsegnamento;
    }

    public void setNomeInsegnamento(String nomeInsegnamento) {
        this.nomeInsegnamento = nomeInsegnamento;
    }

    public String getNomeProfessore() {
        return nomeProfessore;
    }

    public void setNomeProfessore(String nomeProfessore) {
        this.nomeProfessore = nomeProfessore;
    }
    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }
}
