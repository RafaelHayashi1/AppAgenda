package com.example.agendaatividadesv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaatividadesv2.db.DBHelper;
import com.example.agendaatividadesv2.modelos.Local;

import java.util.ArrayList;
import java.util.List;

public class LocalDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public LocalDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long inserir(Local local) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", local.getNome());
        values.put("endereco", local.getEndereco());
        values.put("descricao", local.getDescricao());
        return db.insert("Locais", null, values);
    }

    public boolean atualizar(Local local) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", local.getNome());
        values.put("endereco", local.getEndereco());
        values.put("descricao", local.getDescricao());
        return db.update("Locais", values, "id = ?", 
            new String[]{String.valueOf(local.getId())}) > 0;
    }

    public boolean excluir(int id) {
        db = dbHelper.getWritableDatabase();
        return db.delete("Locais", "id = ?", 
            new String[]{String.valueOf(id)}) > 0;
    }

    public Local buscarPorId(int id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Locais", null, "id = ?", 
            new String[]{String.valueOf(id)}, null, null, null);
        
        Local local = null;
        if (cursor.moveToFirst()) {
            local = new Local();
            local.setId(cursor.getInt(cursor.getColumnIndex("id")));
            local.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            local.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        }
        cursor.close();
        return local;
    }

    public List<String> listarNomesLocais() {
        List<String> locais = new ArrayList<>();
        try {
            android.util.Log.d("LocalDAO", "Iniciando listagem de nomes de locais");
            db = dbHelper.getReadableDatabase();
            
            Cursor cursor = db.query("Locais", new String[]{"nome"}, null, null, null, null, "nome");
            
            if (cursor.moveToFirst()) {
                do {
                    String nome = cursor.getString(0);
                    locais.add(nome);
                    android.util.Log.d("LocalDAO", "Local encontrado: " + nome);
                } while (cursor.moveToNext());
            }
            cursor.close();
            android.util.Log.d("LocalDAO", "Total de locais encontrados: " + locais.size());
        } catch (Exception e) {
            android.util.Log.e("LocalDAO", "Erro ao listar nomes de locais: " + e.getMessage(), e);
        }
        return locais;
    }

    public List<Local> listarTodos() {
        db = dbHelper.getReadableDatabase();
        List<Local> locais = new ArrayList<>();
        Cursor cursor = db.query("Locais", null, null, null, null, null, "nome ASC");
        
        while (cursor.moveToNext()) {
            Local local = new Local();
            local.setId(cursor.getInt(cursor.getColumnIndex("id")));
            local.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            local.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            locais.add(local);
        }
        cursor.close();
        return locais;
    }
} 