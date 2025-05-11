package com.example.agendaatividadesv2.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda_atividades.db";
    private static final int DATABASE_VERSION = 8;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela de Usuários
        db.execSQL("CREATE TABLE IF NOT EXISTS Usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome_User TEXT NOT NULL," +
                "email_User TEXT NOT NULL UNIQUE," +
                "senha_User TEXT NOT NULL" +
                ")");

        // Tabela de Categorias (valores fixos)
        db.execSQL("CREATE TABLE IF NOT EXISTS Categorias (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL" +
                ")");

        // Tabela de Locais (valores fixos)
        db.execSQL("CREATE TABLE IF NOT EXISTS Locais (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL" +
                ")");

        // Tabela de Atividades
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

        // Inserir categorias padrão
        db.execSQL("INSERT INTO Categorias (nome) VALUES " +
                "('Reunião'), ('Evento'), ('Compromisso'), ('Tarefa'), ('Lembrete'), ('Outro')");

        // Inserir locais padrão
        db.execSQL("INSERT INTO Locais (nome) VALUES " +
                "('Sala de Reunião'), ('Auditório'), ('Escritório'), ('Casa'), ('Outro')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 8) {
            // Criar tabelas de categorias e locais
            db.execSQL("CREATE TABLE IF NOT EXISTS Categorias (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL" +
                    ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS Locais (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL" +
                    ")");

            // Inserir categorias padrão
            db.execSQL("INSERT INTO Categorias (nome) VALUES " +
                    "('Reunião'), ('Evento'), ('Compromisso'), ('Tarefa'), ('Lembrete'), ('Outro')");

            // Inserir locais padrão
            db.execSQL("INSERT INTO Locais (nome) VALUES " +
                    "('Sala de Reunião'), ('Auditório'), ('Escritório'), ('Casa'), ('Outro')");
        }
    }
}