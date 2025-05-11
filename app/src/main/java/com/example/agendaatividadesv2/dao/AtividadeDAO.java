package com.example.agendaatividadesv2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.agendaatividadesv2.db.DBHelper;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.example.agendaatividadesv2.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private Context context;
    private SessionManager sessionManager;

    public AtividadeDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        sessionManager = new SessionManager(context);
    }

    public long inserirAtividade(Atividade atividade) {
        try {
            db = dbHelper.getWritableDatabase();
            android.util.Log.d("AtividadeDAO", "Inserindo atividade: " +
                "\nTítulo: " + atividade.getTitulo() +
                "\nDescrição: " + atividade.getDescricao() +
                "\nData: " + atividade.getData() +
                "\nHora: " + atividade.getHora() +
                "\nLocal: " + atividade.getLocal() +
                "\nCategoria: " + atividade.getCategoria() +
                "\nParticipantes: " + atividade.getParticipantes());

            ContentValues values = new ContentValues();
            values.put("titulo_Ativ", atividade.getTitulo());
            values.put("datalnicial_Ativ", atividade.getData());
            values.put("hora", atividade.getHora());
            values.put("local_Ativ", atividade.getLocal());
            values.put("desc_Ativ", atividade.getDescricao());
            values.put("categoria", atividade.getCategoria());
            values.put("participantes", atividade.getParticipantes());
            values.put("id_usuario", sessionManager.getUserId());

            long id = db.insert("Atividades", null, values);
            android.util.Log.d("AtividadeDAO", "Atividade inserida com ID: " + id);
            return id;
        } catch (Exception e) {
            android.util.Log.e("AtividadeDAO", "Erro ao inserir atividade: " + e.getMessage());
            return -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<Atividade> listarAtividades() {
        List<Atividade> atividades = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "titulo_Ativ", "datalnicial_Ativ", "hora", "local_Ativ", "desc_Ativ", "categoria", "participantes"};
        Cursor cursor = db.query("Atividades", columns, null, null, null, null, "datalnicial_Ativ ASC, hora ASC");

        while (cursor.moveToNext()) {
            Atividade atividade = new Atividade();
            atividade.setId(cursor.getInt(cursor.getColumnIndex("id")));
            atividade.setTitulo(cursor.getString(cursor.getColumnIndex("titulo_Ativ")));
            atividade.setData(cursor.getString(cursor.getColumnIndex("datalnicial_Ativ")));
            atividade.setHora(cursor.getString(cursor.getColumnIndex("hora")));
            atividade.setLocal(cursor.getString(cursor.getColumnIndex("local_Ativ")));
            atividade.setDescricao(cursor.getString(cursor.getColumnIndex("desc_Ativ")));
            atividade.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
            atividade.setParticipantes(cursor.getString(cursor.getColumnIndex("participantes")));
            atividades.add(atividade);
        }
        cursor.close();
        db.close();
        return atividades;
    }

    public Atividade buscarAtividade(long id) {
        db = dbHelper.getReadableDatabase();
        String[] columns = {"id", "titulo_Ativ", "datalnicial_Ativ", "hora", "local_Ativ", "desc_Ativ", "categoria", "participantes"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query("Atividades", columns, selection, selectionArgs, null, null, null);

        Atividade atividade = null;
        if (cursor.moveToFirst()) {
            atividade = new Atividade();
            atividade.setId(cursor.getInt(cursor.getColumnIndex("id")));
            atividade.setTitulo(cursor.getString(cursor.getColumnIndex("titulo_Ativ")));
            atividade.setData(cursor.getString(cursor.getColumnIndex("datalnicial_Ativ")));
            atividade.setHora(cursor.getString(cursor.getColumnIndex("hora")));
            atividade.setLocal(cursor.getString(cursor.getColumnIndex("local_Ativ")));
            atividade.setDescricao(cursor.getString(cursor.getColumnIndex("desc_Ativ")));
            atividade.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
            atividade.setParticipantes(cursor.getString(cursor.getColumnIndex("participantes")));
        }
        cursor.close();
        db.close();
        return atividade;
    }

    public boolean excluirAtividade(long id) {
        db = dbHelper.getWritableDatabase();
        String where = "id = ? AND id_usuario = ?";
        String[] whereArgs = {String.valueOf(id), String.valueOf(sessionManager.getUserId())};
        return db.delete("Atividades", where, whereArgs) > 0;
    }

    public boolean atualizarAtividade(Atividade atividade) {
        try {
            db = dbHelper.getWritableDatabase();
            android.util.Log.d("AtividadeDAO", "Atualizando atividade ID: " + atividade.getId());
            ContentValues values = new ContentValues();
            values.put("titulo_Ativ", atividade.getTitulo());
            values.put("datalnicial_Ativ", atividade.getData());
            values.put("hora", atividade.getHora());
            values.put("local_Ativ", atividade.getLocal());
            values.put("desc_Ativ", atividade.getDescricao());
            values.put("categoria", atividade.getCategoria());
            values.put("participantes", atividade.getParticipantes());

            String whereClause = "id = ? AND id_usuario = ?";
            String[] whereArgs = {String.valueOf(atividade.getId()), String.valueOf(sessionManager.getUserId())};
            int rowsAffected = db.update("Atividades", values, whereClause, whereArgs);
            
            android.util.Log.d("AtividadeDAO", "Linhas afetadas: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            android.util.Log.e("AtividadeDAO", "Erro ao atualizar atividade: " + e.getMessage());
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<String> listarParticipantesUnicos() {
        List<String> participantes = new ArrayList<>();
        try {
            db = dbHelper.getReadableDatabase();
            
            Cursor cursor = db.query(
                "Atividades",
                new String[]{"participantes"},
                null,
                null,
                "participantes",
                null,
                null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String participantesStr = cursor.getString(cursor.getColumnIndexOrThrow("participantes"));
                    if (participantesStr != null && !participantesStr.isEmpty()) {
                        String[] listaParticipantes = participantesStr.split(",");
                        for (String participante : listaParticipantes) {
                            String participanteTrim = participante.trim();
                            if (!participanteTrim.isEmpty() && !participantes.contains(participanteTrim)) {
                                participantes.add(participanteTrim);
                            }
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            android.util.Log.e("AtividadeDAO", "Erro ao listar participantes: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return participantes;
    }

    public List<Atividade> listarAtividadesFiltradas(String titulo, String dataInicial, String dataFinal, String participante, String categoria) {
        List<Atividade> atividades = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();

        // Se nenhum filtro foi preenchido, retorna lista vazia
        if (titulo.isEmpty() && dataInicial.isEmpty() && dataFinal.isEmpty() && 
            participante.isEmpty() && categoria.isEmpty()) {
            return atividades;
        }

        if (!titulo.isEmpty()) {
            selection.append("titulo_Ativ LIKE ?");
            selectionArgs.add("%" + titulo + "%");
        }

        if (!dataInicial.isEmpty()) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("datalnicial_Ativ >= ?");
            selectionArgs.add(dataInicial);
        }

        if (!dataFinal.isEmpty()) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("datalnicial_Ativ <= ?");
            selectionArgs.add(dataFinal);
        }

        if (!participante.isEmpty()) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("participantes LIKE ?");
            selectionArgs.add("%" + participante + "%");
        }

        if (!categoria.isEmpty()) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("categoria = ?");
            selectionArgs.add(categoria);
        }

        // Adiciona filtro para mostrar apenas atividades do usuário atual
        if (selection.length() > 0) {
            selection.append(" AND ");
        }
        selection.append("id_usuario = ?");
        selectionArgs.add(String.valueOf(sessionManager.getUserId()));

        android.util.Log.d("AtividadeDAO", "Query de filtro: " + selection.toString());
        android.util.Log.d("AtividadeDAO", "Args: " + selectionArgs.toString());

        Cursor cursor = db.query(
            "Atividades",
            null,
            selection.toString(),
            selectionArgs.toArray(new String[0]),
            null,
            null,
            "datalnicial_Ativ ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Atividade atividade = new Atividade();
                atividade.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                atividade.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo_Ativ")));
                atividade.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("desc_Ativ")));
                atividade.setData(cursor.getString(cursor.getColumnIndexOrThrow("datalnicial_Ativ")));
                atividade.setHora(cursor.getString(cursor.getColumnIndexOrThrow("hora")));
                atividade.setLocal(cursor.getString(cursor.getColumnIndexOrThrow("local_Ativ")));
                atividade.setParticipantes(cursor.getString(cursor.getColumnIndexOrThrow("participantes")));
                atividade.setCategoria(cursor.getString(cursor.getColumnIndexOrThrow("categoria")));
                atividades.add(atividade);
            } while (cursor.moveToNext());
            cursor.close();
        }

        android.util.Log.d("AtividadeDAO", "Atividades encontradas: " + atividades.size());
        return atividades;
    }
} 