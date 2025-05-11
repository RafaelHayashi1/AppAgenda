package com.example.agendaatividadesv2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agendaatividadesv2.dao.AtividadeDAO;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class DetalheAtividadeActivity extends AppCompatActivity {

    private MaterialTextView tvTitulo, tvData, tvHorario, tvLocal, tvDescricao, tvParticipantes, tvCategoria;
    private MaterialButton btnEditar, btnExcluir;
    private AtividadeDAO atividadeDAO;
    private Atividade atividade;
    private ActivityResultLauncher<Intent> editarAtividadeLauncher;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_atividade);

        // Configurar ActivityResultLauncher
        editarAtividadeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    carregarAtividade();
                }
            }
        );

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalhes da Atividade");

        // Inicializar views
        tvTitulo = findViewById(R.id.tvTitulo);
        tvData = findViewById(R.id.tvData);
        tvHorario = findViewById(R.id.tvHorario);
        tvLocal = findViewById(R.id.tvLocal);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvParticipantes = findViewById(R.id.tvParticipantes);
        tvCategoria = findViewById(R.id.tvCategoria);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);

        // Inicializar DAO
        atividadeDAO = new AtividadeDAO(this);

        // Receber ID da atividade
        carregarAtividade();

        // Configurar botões
        btnEditar.setOnClickListener(v -> editarAtividade());
        btnExcluir.setOnClickListener(v -> confirmarExclusao());
    }

    private void carregarAtividade() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            android.util.Log.e("DetalheAtividade", "Intent extras é null");
            Toast.makeText(this, "Erro: Dados da atividade não encontrados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        long atividadeId = extras.getLong("atividade_id", -1);
        android.util.Log.d("DetalheAtividade", "Tentando carregar atividade ID: " + atividadeId);

        if (atividadeId == -1) {
            android.util.Log.e("DetalheAtividade", "ID da atividade não encontrado no Intent");
            Toast.makeText(this, "Erro: ID da atividade não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        atividade = atividadeDAO.buscarAtividade((int) atividadeId);

        if (atividade != null) {
            android.util.Log.d("DetalheAtividade", "Atividade encontrada: " + atividade.getTitulo());
            tvTitulo.setText(atividade.getTitulo());
            tvData.setText(atividade.getData());
            tvHorario.setText(atividade.getHora());
            tvLocal.setText(atividade.getLocal());
            tvDescricao.setText(atividade.getDescricao());
            
            // Exibir participantes
            String participantes = atividade.getParticipantes();
            if (participantes != null && !participantes.isEmpty()) {
                tvParticipantes.setText(participantes);
            } else {
                tvParticipantes.setText("Nenhum participante cadastrado");
            }
            
            // Exibir categoria
            String categoria = atividade.getCategoria();
            if (categoria != null && !categoria.isEmpty()) {
                tvCategoria.setText(categoria);
            } else {
                tvCategoria.setText("Categoria não definida");
            }
            
            android.util.Log.d("DetalheAtividade", "Dados exibidos: " +
                "\nTítulo: " + atividade.getTitulo() +
                "\nData: " + atividade.getData() +
                "\nHora: " + atividade.getHora() +
                "\nLocal: " + atividade.getLocal() +
                "\nDescrição: " + atividade.getDescricao() +
                "\nParticipantes: " + atividade.getParticipantes() +
                "\nCategoria: " + atividade.getCategoria());
        } else {
            android.util.Log.e("DetalheAtividade", "Atividade não encontrada para o ID: " + atividadeId);
            Toast.makeText(this, "Erro ao carregar atividade", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void editarAtividade() {
        if (atividade == null) {
            android.util.Log.e("DetalheAtividade", "Atividade é null");
            Toast.makeText(this, "Erro: Dados da atividade não encontrados", Toast.LENGTH_SHORT).show();
            return;
        }

        android.util.Log.d("DetalheAtividade", "Iniciando edição da atividade ID: " + atividade.getId());
        
        try {
            Intent intent = new Intent(this, EditarAtividadeActivity.class);
            intent.putExtra("atividade_id", atividade.getId());
            editarAtividadeLauncher.launch(intent);
        } catch (Exception e) {
            android.util.Log.e("DetalheAtividade", "Erro ao abrir tela de edição: " + e.getMessage());
            Toast.makeText(this, "Erro ao abrir tela de edição", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar os dados da atividade quando voltar da tela de edição
        carregarAtividade();
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Atividade")
                .setMessage("Tem certeza que deseja excluir esta atividade?")
                .setPositiveButton("Sim", (dialog, which) -> excluirAtividade())
                .setNegativeButton("Não", null)
                .show();
    }

    private void excluirAtividade() {
        if (atividadeDAO.excluirAtividade(atividade.getId())) {
            Toast.makeText(this, "Atividade excluída com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao excluir atividade", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 