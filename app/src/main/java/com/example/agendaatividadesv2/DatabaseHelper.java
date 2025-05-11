package com.example.agendaatividadesv2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda_atividades.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de Categorias
    public static final String TABLE_CATEGORIAS = "categorias";
    public static final String COLUMN_CATEGORIA_ID = "id";
    public static final String COLUMN_CATEGORIA_NOME = "nome";
    public static final String COLUMN_CATEGORIA_DESCRICAO = "descricao";

    // Tabela de Locais
    public static final String TABLE_LOCAIS = "locais";
    public static final String COLUMN_LOCAL_ID = "id";
    public static final String COLUMN_LOCAL_NOME = "nome";
    public static final String COLUMN_LOCAL_ENDERECO = "endereco";
    public static final String COLUMN_LOCAL_DESCRICAO = "descricao";

    // Tabela de Participantes
    public static final String TABLE_PARTICIPANTES = "participantes";
    public static final String COLUMN_PARTICIPANTE_ID = "id";
    public static final String COLUMN_PARTICIPANTE_NOME = "nome";
    public static final String COLUMN_PARTICIPANTE_EMAIL = "email";
    public static final String COLUMN_PARTICIPANTE_TELEFONE = "telefone";

    // Tabela de Atividades
    public static final String TABLE_ATIVIDADES = "atividades";
    public static final String COLUMN_ATIVIDADE_ID = "id";
    public static final String COLUMN_ATIVIDADE_TITULO = "titulo";
    public static final String COLUMN_ATIVIDADE_DESCRICAO = "descricao";
    public static final String COLUMN_ATIVIDADE_DATA = "data";
    public static final String COLUMN_ATIVIDADE_HORA = "hora";
    public static final String COLUMN_ATIVIDADE_CATEGORIA_ID = "categoria_id";
    public static final String COLUMN_ATIVIDADE_LOCAL_ID = "local_id";

    // Tabela de Relacionamento Atividade-Participante
    public static final String TABLE_ATIVIDADE_PARTICIPANTE = "atividade_participante";
    public static final String COLUMN_ATIVIDADE_PARTICIPANTE_ATIVIDADE_ID = "atividade_id";
    public static final String COLUMN_ATIVIDADE_PARTICIPANTE_PARTICIPANTE_ID = "participante_id";

    // Criação da tabela Categorias
    private static final String CREATE_TABLE_CATEGORIAS = 
        "CREATE TABLE " + TABLE_CATEGORIAS + " (" +
        COLUMN_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_CATEGORIA_NOME + " TEXT NOT NULL, " +
        COLUMN_CATEGORIA_DESCRICAO + " TEXT" +
        ")";

    // Criação da tabela Locais
    private static final String CREATE_TABLE_LOCAIS = 
        "CREATE TABLE " + TABLE_LOCAIS + " (" +
        COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_LOCAL_NOME + " TEXT NOT NULL, " +
        COLUMN_LOCAL_ENDERECO + " TEXT, " +
        COLUMN_LOCAL_DESCRICAO + " TEXT" +
        ")";

    // Criação da tabela Participantes
    private static final String CREATE_TABLE_PARTICIPANTES = 
        "CREATE TABLE " + TABLE_PARTICIPANTES + " (" +
        COLUMN_PARTICIPANTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_PARTICIPANTE_NOME + " TEXT NOT NULL, " +
        COLUMN_PARTICIPANTE_EMAIL + " TEXT, " +
        COLUMN_PARTICIPANTE_TELEFONE + " TEXT" +
        ")";

    // Criação da tabela Atividades
    private static final String CREATE_TABLE_ATIVIDADES = 
        "CREATE TABLE " + TABLE_ATIVIDADES + " (" +
        COLUMN_ATIVIDADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_ATIVIDADE_TITULO + " TEXT NOT NULL, " +
        COLUMN_ATIVIDADE_DESCRICAO + " TEXT, " +
        COLUMN_ATIVIDADE_DATA + " TEXT NOT NULL, " +
        COLUMN_ATIVIDADE_HORA + " TEXT NOT NULL, " +
        COLUMN_ATIVIDADE_CATEGORIA_ID + " INTEGER, " +
        COLUMN_ATIVIDADE_LOCAL_ID + " INTEGER, " +
        "FOREIGN KEY(" + COLUMN_ATIVIDADE_CATEGORIA_ID + ") REFERENCES " + 
            TABLE_CATEGORIAS + "(" + COLUMN_CATEGORIA_ID + "), " +
        "FOREIGN KEY(" + COLUMN_ATIVIDADE_LOCAL_ID + ") REFERENCES " + 
            TABLE_LOCAIS + "(" + COLUMN_LOCAL_ID + ")" +
        ")";

    // Criação da tabela de relacionamento Atividade-Participante
    private static final String CREATE_TABLE_ATIVIDADE_PARTICIPANTE = 
        "CREATE TABLE " + TABLE_ATIVIDADE_PARTICIPANTE + " (" +
        COLUMN_ATIVIDADE_PARTICIPANTE_ATIVIDADE_ID + " INTEGER, " +
        COLUMN_ATIVIDADE_PARTICIPANTE_PARTICIPANTE_ID + " INTEGER, " +
        "PRIMARY KEY(" + COLUMN_ATIVIDADE_PARTICIPANTE_ATIVIDADE_ID + ", " + 
            COLUMN_ATIVIDADE_PARTICIPANTE_PARTICIPANTE_ID + "), " +
        "FOREIGN KEY(" + COLUMN_ATIVIDADE_PARTICIPANTE_ATIVIDADE_ID + ") REFERENCES " + 
            TABLE_ATIVIDADES + "(" + COLUMN_ATIVIDADE_ID + "), " +
        "FOREIGN KEY(" + COLUMN_ATIVIDADE_PARTICIPANTE_PARTICIPANTE_ID + ") REFERENCES " + 
            TABLE_PARTICIPANTES + "(" + COLUMN_PARTICIPANTE_ID + ")" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORIAS);
        db.execSQL(CREATE_TABLE_LOCAIS);
        db.execSQL(CREATE_TABLE_PARTICIPANTES);
        db.execSQL(CREATE_TABLE_ATIVIDADES);
        db.execSQL(CREATE_TABLE_ATIVIDADE_PARTICIPANTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui você pode implementar a lógica de atualização do banco de dados
        // Por exemplo, adicionar novas colunas ou tabelas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATIVIDADE_PARTICIPANTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATIVIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAIS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIAS);
        onCreate(db);
    }
} 