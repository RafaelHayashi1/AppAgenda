package com.example.agendaatividadesv2.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda_atividades.db";
    private static final int DATABASE_VERSION = 6;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_User TEXT NOT NULL," +
                "email_User TEXT NOT NULL UNIQUE," +
                "senha_User TEXT NOT NULL" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS Atividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo_Ativ TEXT NOT NULL," +
                "datalnicial_Ativ TEXT NOT NULL," +
                "hora TEXT NOT NULL," +
                "local_Ativ TEXT," +
                "desc_Ativ TEXT," +
                "categoria TEXT," +
                "participantes TEXT," +
                "id_usuario INTEGER NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES Usuarios(id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 6) {
            // Criar tabela temporária
            db.execSQL("CREATE TABLE IF NOT EXISTS Atividades_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo_Ativ TEXT NOT NULL," +
                    "datalnicial_Ativ TEXT NOT NULL," +
                    "hora TEXT NOT NULL," +
                    "local_Ativ TEXT," +
                    "desc_Ativ TEXT," +
                    "categoria TEXT," +
                    "participantes TEXT," +
                    "id_usuario INTEGER NOT NULL," +
                    "FOREIGN KEY (id_usuario) REFERENCES Usuarios(id)" +
                    ")");

            // Copiar dados existentes
            db.execSQL("INSERT INTO Atividades_temp (id, titulo_Ativ, datalnicial_Ativ, hora, local_Ativ, desc_Ativ, categoria, participantes, id_usuario) " +
                    "SELECT id, titulo_Ativ, data_Ativ, hora, local_Ativ, desc_Ativ, categoria, participantes, id_usuario FROM Atividades");

            // Remover tabela antiga
            db.execSQL("DROP TABLE IF EXISTS Atividades");

            // Renomear tabela temporária
            db.execSQL("ALTER TABLE Atividades_temp RENAME TO Atividades");

            // Remover tabelas antigas que não são mais usadas
            db.execSQL("DROP TABLE IF EXISTS Categorias");
            db.execSQL("DROP TABLE IF EXISTS Participantes");
            db.execSQL("DROP TABLE IF EXISTS Atividades_Categorias");
        }
    }
}