package com.example.agendaatividadesv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "HomeActivity";
    private Button btnAdicionar, btnSair;
    private RecyclerView listaAtividades;
    private AtividadeDAO atividadeDAO;
    private SessionManager gerenciadorSessao;
    private AtividadeAdapter adaptador;
    private static final int CODIGO_FILTRO = 2;
    private MaterialButton btnFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "Iniciando HomeActivity");

        // Inicializar componentes
        btnAdicionar = findViewById(R.id.btnAddAtividade);
        btnSair = findViewById(R.id.btnSair);
        listaAtividades = findViewById(R.id.rvAtividades);
        btnFiltro = findViewById(R.id.btnFiltroAvancado);

        // Inicializar DAO e gerenciador de sessão
        atividadeDAO = new AtividadeDAO(this);
        gerenciadorSessao = new SessionManager(this);

        // Configurar lista de atividades
        listaAtividades.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AtividadeAdapter(this, atividadeDAO.listarAtividades());
        listaAtividades.setAdapter(adaptador);
        Log.d(TAG, "Lista de atividades configurada");

        // Configurar botões
        btnAdicionar.setOnClickListener(v -> {
            Log.d(TAG, "Abrindo tela de adicionar atividade");
            Intent telaAdicionar = new Intent(HomeActivity.this, AddAtividadeActivity.class);
            startActivity(telaAdicionar);
        });

        btnSair.setOnClickListener(v -> {
            Log.d(TAG, "Realizando logout");
            gerenciadorSessao.logout();
            Intent telaLogin = new Intent(HomeActivity.this, MainActivity.class);
            telaLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(telaLogin);
            finish();
        });

        btnFiltro.setOnClickListener(v -> abrirFiltro());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Atualizando lista de atividades");
        atualizarLista();
    }

    private void atualizarLista() {
        List<Atividade> atividades = atividadeDAO.listarAtividades();
        adaptador.setAtividades(atividades);
        Log.d(TAG, "Lista atualizada com " + atividades.size() + " atividades");
    }

    private void abrirFiltro() {
        Log.d(TAG, "Abrindo tela de filtro");
        Intent telaFiltro = new Intent(this, FiltroAtividadesActivity.class);
        startActivityForResult(telaFiltro, CODIGO_FILTRO);
    }

    @Override
    protected void onActivityResult(int codigoRequisicao, int codigoResultado, @Nullable Intent dados) {
        super.onActivityResult(codigoRequisicao, codigoResultado, dados);
        if (codigoRequisicao == CODIGO_FILTRO && codigoResultado == RESULT_OK && dados != null) {
            Log.d(TAG, "Aplicando filtros");
            aplicarFiltros(dados.getExtras());
        }
    }

    private void aplicarFiltros(Bundle filtros) {
        String titulo = filtros.getString("titulo", "");
        String dataInicial = filtros.getString("data_inicial", "");
        String dataFinal = filtros.getString("data_final", "");
        String participante = filtros.getString("participante", "");
        String categoria = filtros.getString("categoria", "");
        String local = filtros.getString("local", "");

        Log.d(TAG, "Filtros aplicados: " +
            "\nTítulo: " + titulo +
            "\nData Inicial: " + dataInicial +
            "\nData Final: " + dataFinal +
            "\nParticipante: " + participante +
            "\nCategoria: " + categoria +
            "\nLocal: " + local);

        List<Atividade> atividadesFiltradas = atividadeDAO.listarAtividadesFiltradas(
            titulo, dataInicial, dataFinal, participante, categoria, local
        );

        adaptador.setAtividades(atividadesFiltradas);
        adaptador.notifyDataSetChanged();
        Log.d(TAG, "Filtros aplicados com sucesso. " + atividadesFiltradas.size() + " atividades encontradas");
    }
}