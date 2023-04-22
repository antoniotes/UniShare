package it.antoniotes.unishare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.antoniotes.unishare.data.PDFFile;
import it.antoniotes.unishare.data.Insegnamento;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "unishareDB.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 8);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, email TEXT NOT NULL UNIQUE)");


        MyDB.execSQL("create Table documenti(idFile INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "data BLOB NOT NULL, " +
                "materia TEXT NOT NULL, " +
                "professore TEXT NOT NULL, " +
                "valutazione FLOAT NOT NULL, " +
                "idCreatore INTEGER NOT NULL)");


        MyDB.execSQL("create table insegnamenti(id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, nomeInsegnamento TEXT NOT NULL, nomeDocente TEXT NOT NULL, anno INTEGER NOT NULL)");
        MyDB.execSQL("create table preferiti(id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, id_user INTEGER, id_insegnamento INTEGER, FOREIGN KEY (id_user) REFERENCES users(id), FOREIGN KEY (id_insegnamento) REFERENCES Insegnamenti(id))");



    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        if (oldVersion <= 7) {
            MyDB.execSQL("drop Table if exists documenti");
        }


    }

    public Boolean insertData(String username, String password, String mail) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", mail);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username =?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkMail(String mail) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email =?", new String[]{mail});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username =? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getID(String username, String password) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username =? and password = ?", new String[]{username, password});
        return cursor.getInt(cursor.getColumnIndexOrThrow("id"));
    }

    public int getIDFromUsername(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        String[] projection = { "id" };
        String selection = "username = ?";
        String[] selectionArgs = { username };

        Cursor cursor = MyDB.query("users", projection, selection, selectionArgs, null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        MyDB.close();

        return userId;
    }

    public String getUsernameFromEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        String username = null;
        if (cursor != null && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex("username");
            if (usernameIndex != -1) {
                username = cursor.getString(usernameIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return username;
    }

    public String getUsernameFromID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        String username = null;
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        }
        cursor.close();
        db.close();
        return username;
    }



    public Boolean insertInsegnamentiData(String nomeInsegnamento, String nomeProf, int anno) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nomeInsegnamento", nomeInsegnamento);
        contentValues.put("nomeDocente", nomeProf);
        contentValues.put("anno", anno);
        long result = MyDB.insert("insegnamenti", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkInsegnamento(String nomeInsegnamento, String nomeProf) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from insegnamenti where nomeInsegnamento =? and nomeDocente =?", new String[]{nomeInsegnamento, nomeProf});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getInsegnamentoID(String nomeInsegnamento, String nomeDocente) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        String[] columns = { "id" };
        String selection = "nomeInsegnamento = ? AND nomeDocente = ?";
        String[] selectionArgs = { nomeInsegnamento, nomeDocente };
        Cursor cursor = db.query("insegnamenti", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return id;
    }

    public String getNomeInsegnamento(int idInsegnamento) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT nomeInsegnamento FROM insegnamenti WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idInsegnamento)});
        String nomeInsegnamento = null;
        if (cursor.moveToFirst()) {
            nomeInsegnamento = cursor.getString(cursor.getColumnIndexOrThrow("nomeInsegnamento"));
        }
        cursor.close();
        return nomeInsegnamento;
    }

    public List<Insegnamento> getAllInsegnamenti() {
        List<Insegnamento> insegnamenti = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "id",
                "nomeInsegnamento",
                "nomeDocente",
                "anno"
        };

        Cursor cursor = db.query(
                "insegnamenti",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String nomeInsegnamento = cursor.getString(cursor.getColumnIndexOrThrow("nomeInsegnamento"));
            String nomeDocente = cursor.getString(cursor.getColumnIndexOrThrow("nomeDocente"));
            int anno = cursor.getInt(cursor.getColumnIndexOrThrow("anno"));

            Insegnamento insegnamento = new Insegnamento(nomeInsegnamento, nomeDocente, anno);
            insegnamenti.add(insegnamento);
        }

        cursor.close();
        db.close();

        return insegnamenti;
    }


    public Boolean insertFavorite(int idUser, int idMateria) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_user", idUser);
        contentValues.put("id_insegnamento", idMateria);
        long result = MyDB.insert("preferiti", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean deleteFavorite(int idUser, int idMateria){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("preferiti", "id_user = ? AND id_insegnamento = ?", new String[]{String.valueOf(idUser), String.valueOf(idMateria)});
        db.close();
        return (result != 0);
    }


    public List<Integer> getPreferitiByUserId(int id_user) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> preferitiList = new ArrayList<Integer>();

        String[] columns = { "id_insegnamento" };
        String selection = "id_user = ?";
        String[] selectionArgs = { String.valueOf(id_user) };

        Cursor cursor = db.query("preferiti", columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id_insegnamento = cursor.getInt(cursor.getColumnIndexOrThrow("id_insegnamento"));
                preferitiList.add(id_insegnamento);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return preferitiList;
    }



    public boolean insertFile(byte[] data, String materia, String professore, float valutazione, int idCreatore) {
        createDocumentiTableIfNeeded();
        SQLiteDatabase dbr = this.getReadableDatabase();
        String query = "Select * FROM documenti";
        Cursor cursor = dbr.rawQuery(query, null);
        Log.d("Antonio", "cursor: "+ cursor.getCount());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("data", data);
        contentValues.put("materia", materia);
        contentValues.put("professore", professore);
        contentValues.put("valutazione", valutazione);
        contentValues.put("idCreatore", idCreatore);
        long result = db.insert("documenti", null, contentValues);
        return result != -1;
    }


    public void createDocumentiTableIfNeeded() {
        SQLiteDatabase db = getWritableDatabase();

        // Query per verificare se la tabella "documenti" esiste già
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='documenti'";
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            // Se la tabella "documenti" non esiste, creala
            Log.d("Antonio", "Creazione Tabella perche non esiste cristoddio");
            db.execSQL("create Table documenti(idFile INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    "data BLOB NOT NULL, " +
                    "materia TEXT NOT NULL, " +
                    "professore TEXT NOT NULL, " +
                    "valutazione FLOAT NOT NULL, " +
                    "idCreatore INTEGER NOT NULL)");
        }

        cursor.close();
    }


    public List<PDFFile> getAllDocuments() {
        List<PDFFile> documents = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("documenti", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int idFile = cursor.getInt(cursor.getColumnIndexOrThrow("idFile"));
            byte[] data = cursor.getBlob(cursor.getColumnIndexOrThrow("data"));
            String materia = cursor.getString(cursor.getColumnIndexOrThrow("materia"));
            String professore = cursor.getString(cursor.getColumnIndexOrThrow("professore"));
            float valutazione = cursor.getFloat(cursor.getColumnIndexOrThrow("valutazione"));
            int idCreatore = cursor.getInt(cursor.getColumnIndexOrThrow("idCreatore"));

            PDFFile document = new PDFFile(idFile, data, materia, professore, valutazione, idCreatore);
            documents.add(document);
        }

        cursor.close();
        return documents;
    }

}
