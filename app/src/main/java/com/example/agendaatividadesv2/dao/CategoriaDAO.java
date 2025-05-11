package com.example.agendaatividadesv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaatividadesv2.db.DBHelper;
import com.example.agendaatividadesv2.modelos.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public CategoriaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long inserir(Categoria categoria) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", categoria.getNome());
        values.put("descricao", categoria.getDescricao());
        return db.insert("Categorias", null, values);
    }

    public boolean atualizar(Categoria categoria) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", categoria.getNome());
        values.put("descricao", categoria.getDescricao());
        return db.update("Categorias", values, "id = ?", 
            new String[]{String.valueOf(categoria.getId())}) > 0;
    }

    public boolean excluir(int id) {
        db = dbHelper.getWritableDatabase();
        return db.delete("Categorias", "id = ?", 
            new String[]{String.valueOf(id)}) > 0;
    }

    public Categoria buscarPorId(int id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Categorias", null, "id = ?", 
            new String[]{String.valueOf(id)}, null, null, null);
        
        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            categoria = new Categoria();
            categoria.setId(cursor.getInt(cursor.getColumnIndex("id")));
            categoria.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            categoria.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        }
        cursor.close();
        return categoria;
    }

    public List<String> listarNomesCategorias() {
        List<String> categorias = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query("Categorias", new String[]{"nome"}, null, null, null, null, "nome");
        
        if (cursor.moveToFirst()) {
            do {
                categorias.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categorias;
    }

    public List<Categoria> listarTodos() {
        db = dbHelper.getReadableDatabase();
        List<Categoria> categorias = new ArrayList<>();
        Cursor cursor = db.query("Categorias", null, null, null, null, null, "nome ASC");
        
        while (cursor.moveToNext()) {
            Categoria categoria = new Categoria();
            categoria.setId(cursor.getInt(cursor.getColumnIndex("id")));
            categoria.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            categoria.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            categorias.add(categoria);
        }
        cursor.close();
        return categorias;
    }
} 