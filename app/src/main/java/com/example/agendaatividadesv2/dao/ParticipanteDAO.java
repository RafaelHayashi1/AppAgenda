package com.example.agendaatividadesv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaatividadesv2.DatabaseHelper;
import com.example.agendaatividadesv2.modelos.Participante;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public ParticipanteDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long inserir(Participante participante) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", participante.getNome());
        values.put("email", participante.getEmail());
        values.put("telefone", participante.getTelefone());
        return db.insert("participantes", null, values);
    }

    public boolean atualizar(Participante participante) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", participante.getNome());
        values.put("email", participante.getEmail());
        values.put("telefone", participante.getTelefone());
        return db.update("participantes", values, "id = ?", 
            new String[]{String.valueOf(participante.getId())}) > 0;
    }

    public boolean excluir(int id) {
        db = dbHelper.getWritableDatabase();
        return db.delete("participantes", "id = ?", 
            new String[]{String.valueOf(id)}) > 0;
    }

    public Participante buscarPorId(int id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("participantes", null, "id = ?", 
            new String[]{String.valueOf(id)}, null, null, null);
        
        Participante participante = null;
        if (cursor.moveToFirst()) {
            participante = new Participante();
            participante.setId(cursor.getInt(cursor.getColumnIndex("id")));
            participante.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            participante.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            participante.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
        }
        cursor.close();
        return participante;
    }

    public List<Participante> listarTodos() {
        db = dbHelper.getReadableDatabase();
        List<Participante> participantes = new ArrayList<>();
        Cursor cursor = db.query("participantes", null, null, null, null, null, "nome ASC");
        
        while (cursor.moveToNext()) {
            Participante participante = new Participante();
            participante.setId(cursor.getInt(cursor.getColumnIndex("id")));
            participante.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            participante.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            participante.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            participantes.add(participante);
        }
        cursor.close();
        return participantes;
    }
} 