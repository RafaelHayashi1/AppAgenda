package com.example.agendaatividadesv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaatividadesv2.adapters.AtividadeAdapter;
import com.example.agendaatividadesv2.dao.AtividadeDAO;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.example.agendaatividadesv2.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private Button btnAddAtividade, btnSair;
    private RecyclerView rvAtividades;
    private AtividadeDAO atividadeDAO;
    private SessionManager sessionManager;
    private AtividadeAdapter adapter;
    private static final int REQUEST_FILTRO = 2;
    private MaterialButton btnFiltroAvancado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddAtividade = findViewById(R.id.btnAddAtividade);
        btnSair = findViewById(R.id.btnSair);
        rvAtividades = findViewById(R.id.rvAtividades);
        btnFiltroAvancado = findViewById(R.id.btnFiltroAvancado);

        // Inicialização do DAO e SessionManager
        atividadeDAO = new AtividadeDAO(this);
        sessionManager = new SessionManager(this);

        // Configurar RecyclerView
        rvAtividades.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AtividadeAdapter(this, atividadeDAO.listarAtividades());
        rvAtividades.setAdapter(adapter);

        // Configuração dos botões
        btnAddAtividade.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddAtividadeActivity.class);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            sessionManager.logout();
            finish();
        });

        btnFiltroAvancado.setOnClickListener(v -> abrirFiltroAvancado());
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarAtividades();
    }

    private void carregarAtividades() {
        List<Atividade> atividades = atividadeDAO.listarAtividades();
        adapter.setAtividades(atividades);
    }

    private void abrirFiltroAvancado() {
        Intent intent = new Intent(this, FiltroAtividadesActivity.class);
        startActivityForResult(intent, REQUEST_FILTRO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILTRO && resultCode == RESULT_OK && data != null) {
            aplicarFiltros(data.getExtras());
        }
    }

    private void aplicarFiltros(Bundle filtros) {
        String titulo = filtros.getString("titulo", "");
        String dataInicial = filtros.getString("data_inicial", "");
        String dataFinal = filtros.getString("data_final", "");
        String participante = filtros.getString("participante", "");
        String categoria = filtros.getString("categoria", "");

        List<Atividade> atividadesFiltradas = atividadeDAO.listarAtividadesFiltradas(
            titulo,
            dataInicial,
            dataFinal,
            participante,
            categoria
        );

        adapter.setAtividades(atividadesFiltradas);
        adapter.notifyDataSetChanged();
    }
}