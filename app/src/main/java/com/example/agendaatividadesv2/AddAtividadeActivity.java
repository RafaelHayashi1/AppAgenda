package com.example.agendaatividadesv2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agendaatividadesv2.dao.AtividadeDAO;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

public class AddAtividadeActivity extends AppCompatActivity {
    private TextInputEditText etTitulo, etDescricao, etData, etHora, etLocal;
    private AutoCompleteTextView etParticipantes, spinnerCategoria;
    private MaterialButton btnSalvar, btnCancelar;
    private AtividadeDAO atividadeDAO;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;
    private ArrayAdapter<String> adapterCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atividade);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicialização dos componentes
        etTitulo = findViewById(R.id.etTitulo);
        etDescricao = findViewById(R.id.etDescricao);
        etData = findViewById(R.id.etData);
        etHora = findViewById(R.id.etHora);
        etLocal = findViewById(R.id.etLocal);
        etParticipantes = findViewById(R.id.etParticipantes);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Inicialização do DAO e formatos
        atividadeDAO = new AtividadeDAO(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Verificar se é edição
        long atividadeId = getIntent().getLongExtra("atividade_id", -1);
        android.util.Log.d("AddAtividade", "ID recebido: " + atividadeId);

        if (atividadeId != -1) {
            // É edição
            Atividade atividade = atividadeDAO.buscarAtividade((int) atividadeId);
            android.util.Log.d("AddAtividade", "Atividade encontrada: " + (atividade != null ? atividade.getTitulo() : "null"));

            if (atividade != null) {
                android.util.Log.d("AddAtividade", "Preenchendo dados: " +
                    "\nTítulo: " + atividade.getTitulo() +
                    "\nDescrição: " + atividade.getDescricao() +
                    "\nData: " + atividade.getData() +
                    "\nHora: " + atividade.getHora() +
                    "\nLocal: " + atividade.getLocal());

                etTitulo.setText(atividade.getTitulo());
                etDescricao.setText(atividade.getDescricao());
                etData.setText(atividade.getData());
                etHora.setText(atividade.getHora());
                etLocal.setText(atividade.getLocal());
                if (atividade.getCategoria() != null) {
                    spinnerCategoria.setText(atividade.getCategoria(), false);
                }
                if (atividade.getParticipantes() != null) {
                    etParticipantes.setText(atividade.getParticipantes());
                }
                btnSalvar.setText("Atualizar Atividade");
            }
        }

        // Configuração do DatePicker
        etData.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    etData.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Configuração do TimePicker
        etHora.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    etHora.setText(timeFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            );
            timePickerDialog.show();
        });

        // Configurar spinner de categorias
        configurarSpinnerCategoria();

        // Configuração dos botões
        btnSalvar.setOnClickListener(v -> salvarAtividade());
        btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void salvarAtividade() {
        String titulo = etTitulo.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String data = etData.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String participantes = etParticipantes.getText().toString().trim();
        String categoria = spinnerCategoria.getText().toString().trim();

        android.util.Log.d("AddAtividade", "Dados coletados: " +
            "\nTítulo: " + titulo +
            "\nDescrição: " + descricao +
            "\nData: " + data +
            "\nHora: " + hora +
            "\nLocal: " + local +
            "\nParticipantes: " + participantes +
            "\nCategoria: " + categoria);

        // Validação dos campos
        if (titulo.isEmpty() || data.isEmpty() || hora.isEmpty() || categoria.isEmpty()) {
            android.util.Log.e("AddAtividade", "Campos obrigatórios não preenchidos");
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto atividade
        Atividade atividade = new Atividade(titulo, descricao, data, hora, local, participantes, categoria);
        
        // Verificar se é edição
        long atividadeId = getIntent().getLongExtra("atividade_id", -1);
        boolean sucesso;
        
        if (atividadeId != -1) {
            // É edição
            android.util.Log.d("AddAtividade", "Atualizando atividade ID: " + atividadeId);
            atividade.setId((int) atividadeId);
            sucesso = atividadeDAO.atualizarAtividade(atividade);
        } else {
            // É nova atividade
            android.util.Log.d("AddAtividade", "Inserindo nova atividade");
            sucesso = atividadeDAO.inserirAtividade(atividade) != -1;
        }

        if (sucesso) {
            android.util.Log.d("AddAtividade", "Operação realizada com sucesso");
            Toast.makeText(this, atividadeId != -1 ? "Atividade atualizada com sucesso!" : "Atividade salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            android.util.Log.e("AddAtividade", "Erro ao salvar atividade");
            Toast.makeText(this, "Erro ao salvar atividade", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarDadosAtividade() {
        long atividadeId = getIntent().getLongExtra("atividade_id", -1);
        android.util.Log.d("AddAtividade", "Carregando dados da atividade ID: " + atividadeId);

        if (atividadeId != -1) {
            Atividade atividade = atividadeDAO.buscarAtividade((int) atividadeId);
            if (atividade != null) {
                android.util.Log.d("AddAtividade", "Atividade encontrada: " + atividade.getTitulo());
                etTitulo.setText(atividade.getTitulo());
                etDescricao.setText(atividade.getDescricao());
                etData.setText(atividade.getData());
                etHora.setText(atividade.getHora());
                etLocal.setText(atividade.getLocal());
                
                // Carregar categoria
                if (atividade.getCategoria() != null && !atividade.getCategoria().isEmpty()) {
                    int posicao = adapterCategorias.getPosition(atividade.getCategoria());
                    if (posicao >= 0) {
                        spinnerCategoria.setSelection(posicao);
                    }
                }
                
                // Carregar participantes
                if (atividade.getParticipantes() != null && !atividade.getParticipantes().isEmpty()) {
                    etParticipantes.setText(atividade.getParticipantes());
                }
                
                btnSalvar.setText("Atualizar Atividade");
                android.util.Log.d("AddAtividade", "Dados carregados: " +
                    "\nTítulo: " + atividade.getTitulo() +
                    "\nData: " + atividade.getData() +
                    "\nHora: " + atividade.getHora() +
                    "\nLocal: " + atividade.getLocal() +
                    "\nDescrição: " + atividade.getDescricao() +
                    "\nCategoria: " + atividade.getCategoria() +
                    "\nParticipantes: " + atividade.getParticipantes());
            } else {
                android.util.Log.e("AddAtividade", "Atividade não encontrada");
                Toast.makeText(this, "Erro ao carregar atividade", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void configurarSpinnerCategoria() {
        // Lista de categorias comuns
        List<String> categorias = Arrays.asList(
            "Reunião",
            "Evento",
            "Compromisso",
            "Tarefa",
            "Lembrete",
            "Outro"
        );

        adapterCategorias = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            categorias
        );
        spinnerCategoria.setAdapter(adapterCategorias);
    }
}