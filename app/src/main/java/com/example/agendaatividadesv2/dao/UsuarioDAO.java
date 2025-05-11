package com.example.agendaatividadesv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaatividadesv2.db.DBHelper;

public class UsuarioDAO {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public UsuarioDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long inserirUsuario(String nome, String email, String senha) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome_User", nome);
        values.put("email_User", email);
        values.put("senha_User", senha);
        long id = db.insert("Usuarios", null, values);
        db.close();
        return id;
    }

    public boolean verificarEmailExistente(String email) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Usuarios", new String[]{"email_User"}, "email_User = ?", new String[]{email}, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }

    public int verificarLogin(String email, String senha) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
            "Usuarios",
            new String[]{"id"},
            "email_User = ? AND senha_User = ?",
            new String[]{email, senha},
            null,
            null,
            null
        );

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return userId;
    }

    public String getNomeUsuario(int userId) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
            "Usuarios",
            new String[]{"nome_User"},
            "id = ?",
            new String[]{String.valueOf(userId)},
            null,
            null,
            null
        );

        String nome = "";
        if (cursor.moveToFirst()) {
            nome = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return nome;
    }
} 