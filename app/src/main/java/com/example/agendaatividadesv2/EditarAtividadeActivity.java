package com.example.agendaatividadesv2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agendaatividadesv2.dao.AtividadeDAO;
import com.example.agendaatividadesv2.dao.CategoriaDAO;
import com.example.agendaatividadesv2.dao.LocalDAO;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

public class EditarAtividadeActivity extends AppCompatActivity {
    private TextInputEditText etTitulo, etDescricao, etData, etHora;
    private AutoCompleteTextView etParticipantes, spinnerCategoria, etLocal;
    private MaterialButton btnSalvar, btnCancelar;
    private AtividadeDAO atividadeDAO;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;
    private ArrayAdapter<String> adapterCategorias;
    private long atividadeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d("EditarAtividade", "onCreate iniciado");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_atividade);
        android.util.Log.d("EditarAtividade", "setContentView concluído");

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        android.util.Log.d("EditarAtividade", "Toolbar configurada");

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
        android.util.Log.d("EditarAtividade", "Componentes inicializados");

        // Inicialização do DAO e formatos
        atividadeDAO = new AtividadeDAO(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        android.util.Log.d("EditarAtividade", "DAO e formatos inicializados");

        // Carregar categorias no spinner
        configurarSpinnerCategoria();
        android.util.Log.d("EditarAtividade", "Spinner configurado");

        // Receber ID da atividade
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            android.util.Log.e("EditarAtividade", "Intent extras é null");
            Toast.makeText(this, "Erro: Dados da atividade não encontrados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        atividadeId = extras.getLong("atividade_id", -1);
        android.util.Log.d("EditarAtividade", "ID recebido: " + atividadeId);

        if (atividadeId == -1) {
            android.util.Log.e("EditarAtividade", "ID da atividade não encontrado no Intent");
            Toast.makeText(this, "Erro: ID da atividade não encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar DatePicker
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

        // Configurar TimePicker
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

        // Configurar botões
        btnSalvar.setOnClickListener(v -> salvarAtividade());
        btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        android.util.Log.d("EditarAtividade", "Botões configurados");

        // Carregar dados da atividade
        carregarDadosAtividade();
        android.util.Log.d("EditarAtividade", "onCreate concluído");
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.util.Log.d("EditarAtividade", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        android.util.Log.d("EditarAtividade", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.util.Log.d("EditarAtividade", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        android.util.Log.d("EditarAtividade", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.util.Log.d("EditarAtividade", "onDestroy");
    }

    private void carregarDadosAtividade() {
        Atividade atividade = atividadeDAO.buscarAtividade(atividadeId);
        if (atividade != null) {
            android.util.Log.d("EditarAtividade", "Carregando dados da atividade: " + atividade.getTitulo());
            
            etTitulo.setText(atividade.getTitulo());
            etDescricao.setText(atividade.getDescricao());
            etData.setText(atividade.getData());
            etHora.setText(atividade.getHora());
            etLocal.setText(atividade.getLocal());
            
            // Carregar categoria
            if (atividade.getCategoria() != null && !atividade.getCategoria().isEmpty()) {
                spinnerCategoria.setText(atividade.getCategoria(), false);
            }
            
            // Carregar participantes
            if (atividade.getParticipantes() != null && !atividade.getParticipantes().isEmpty()) {
                etParticipantes.setText(atividade.getParticipantes());
            }
            
            android.util.Log.d("EditarAtividade", "Dados carregados: " +
                "\nTítulo: " + atividade.getTitulo() +
                "\nData: " + atividade.getData() +
                "\nHora: " + atividade.getHora() +
                "\nLocal: " + atividade.getLocal() +
                "\nDescrição: " + atividade.getDescricao() +
                "\nCategoria: " + atividade.getCategoria() +
                "\nParticipantes: " + atividade.getParticipantes());
        } else {
            android.util.Log.e("EditarAtividade", "Atividade não encontrada");
            Toast.makeText(this, "Erro ao carregar atividade", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void salvarAtividade() {
        String titulo = etTitulo.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String data = etData.getText().toString().trim();
        String hora = etHora.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String participantes = etParticipantes.getText().toString().trim();
        String categoria = spinnerCategoria.getText().toString().trim();

        android.util.Log.d("EditarAtividade", "Salvando atividade: " +
            "\nTítulo: " + titulo +
            "\nDescrição: " + descricao +
            "\nData: " + data +
            "\nHora: " + hora +
            "\nLocal: " + local +
            "\nParticipantes: " + participantes +
            "\nCategoria: " + categoria);

        // Validação dos campos
        if (titulo.isEmpty() || data.isEmpty() || hora.isEmpty() || categoria.isEmpty()) {
            android.util.Log.e("EditarAtividade", "Campos obrigatórios não preenchidos");
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto atividade
        Atividade atividade = new Atividade(titulo, descricao, data, hora, local, participantes, categoria);
        atividade.setId((int) atividadeId);

        // Atualizar atividade
        if (atividadeDAO.atualizarAtividade(atividade)) {
            android.util.Log.d("EditarAtividade", "Atividade atualizada com sucesso");
            Toast.makeText(this, "Atividade atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            android.util.Log.e("EditarAtividade", "Erro ao atualizar atividade");
            Toast.makeText(this, "Erro ao atualizar atividade", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarSpinnerCategoria() {
        CategoriaDAO categoriaDAO = new CategoriaDAO(this);
        List<String> categorias = categoriaDAO.listarNomesCategorias();
        
        adapterCategorias = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            categorias
        );
        spinnerCategoria.setAdapter(adapterCategorias);
    }

    private void configurarSpinnerLocal() {
        LocalDAO localDAO = new LocalDAO(this);
        List<String> locais = localDAO.listarNomesLocais();
        
        ArrayAdapter<String> adapterLocais = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            locais
        );
        etLocal.setAdapter(adapterLocais);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
} 