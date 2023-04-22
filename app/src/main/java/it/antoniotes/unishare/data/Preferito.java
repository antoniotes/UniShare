package it.antoniotes.unishare.data;

public class Preferito {

    private int id;
    private int idInsegnamento;
    private int idUser;

    public Preferito() {
    }

    public Preferito(int idInsegnamento, int idUser) {
        this.idInsegnamento = idInsegnamento;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInsegnamento() {
        return idInsegnamento;
    }

    public void setIdInsegnamento(int idInsegnamento) {
        this.idInsegnamento = idInsegnamento;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


}
