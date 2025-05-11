package com.example.agendaatividadesv2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaatividadesv2.adapters.AtividadeAdapter;
import com.example.agendaatividadesv2.dao.AtividadeDAO;
import com.example.agendaatividadesv2.modelos.Atividade;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FiltroAtividadesActivity extends AppCompatActivity {
    private TextInputEditText etFiltroTitulo, etDataInicial, etDataFinal;
    private AutoCompleteTextView etFiltroParticipantes, etFiltroCategoria;
    private MaterialButton btnFiltrar, btnLimpar;
    private AtividadeDAO atividadeDAO;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private ArrayAdapter<String> adapterParticipantes, adapterCategorias;
    private RecyclerView rvResultados;
    private TextView tvResultados;
    private AtividadeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            android.util.Log.d("FiltroAtividades", "Iniciando onCreate");
            setContentView(R.layout.activity_filtro_atividades);

            // Inicialização do DAO e formatos
            android.util.Log.d("FiltroAtividades", "Inicializando DAO e formatos");
            atividadeDAO = new AtividadeDAO(this);
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // Configurar Toolbar
            android.util.Log.d("FiltroAtividades", "Configurando Toolbar");
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar == null) {
                throw new IllegalStateException("Toolbar não encontrada");
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(v -> {
                setResult(RESULT_CANCELED);
                finish();
            });

            // Inicialização dos componentes
            android.util.Log.d("FiltroAtividades", "Inicializando componentes");
            etFiltroTitulo = findViewById(R.id.etFiltroTitulo);
            etDataInicial = findViewById(R.id.etDataInicial);
            etDataFinal = findViewById(R.id.etDataFinal);
            etFiltroParticipantes = findViewById(R.id.etFiltroParticipantes);
            etFiltroCategoria = findViewById(R.id.etFiltroCategoria);
            btnFiltrar = findViewById(R.id.btnFiltrar);
            btnLimpar = findViewById(R.id.btnLimpar);
            rvResultados = findViewById(R.id.rvResultados);
            tvResultados = findViewById(R.id.tvResultados);

            if (etFiltroTitulo == null || etDataInicial == null || etDataFinal == null || 
                etFiltroParticipantes == null || etFiltroCategoria == null || 
                btnFiltrar == null || btnLimpar == null || rvResultados == null || tvResultados == null) {
                throw new IllegalStateException("Componentes não encontrados");
            }

            // Configurar RecyclerView
            rvResultados.setLayoutManager(new LinearLayoutManager(this));
            adapter = new AtividadeAdapter(this, new ArrayList<>());
            rvResultados.setAdapter(adapter);

            // Configurar DatePickers
            android.util.Log.d("FiltroAtividades", "Configurando DatePickers");
            configurarDatePicker(etDataInicial);
            configurarDatePicker(etDataFinal);

            // Carregar lista de participantes e categorias
            android.util.Log.d("FiltroAtividades", "Carregando participantes e categorias");
            carregarParticipantes();
            carregarCategorias();

            // Configurar botões
            android.util.Log.d("FiltroAtividades", "Configurando botões");
            btnFiltrar.setOnClickListener(v -> aplicarFiltro());
            btnLimpar.setOnClickListener(v -> limparFiltros());

            android.util.Log.d("FiltroAtividades", "onCreate concluído com sucesso");
        } catch (Exception e) {
            android.util.Log.e("FiltroAtividades", "Erro ao inicializar activity: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao inicializar tela de filtro: " + e.getMessage(), Toast.LENGTH_LONG).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void configurarDatePicker(TextInputEditText editText) {
        if (editText == null) {
            android.util.Log.e("FiltroAtividades", "EditText é null ao configurar DatePicker");
            return;
        }
        
        editText.setOnClickListener(v -> {
            try {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        editText.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            } catch (Exception e) {
                android.util.Log.e("FiltroAtividades", "Erro ao mostrar DatePicker: " + e.getMessage(), e);
                Toast.makeText(this, "Erro ao selecionar data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarParticipantes() {
        try {
            android.util.Log.d("FiltroAtividades", "Iniciando carregamento de participantes");
            List<String> participantes = atividadeDAO.listarParticipantesUnicos();
            if (participantes == null) {
                android.util.Log.w("FiltroAtividades", "Lista de participantes é null");
                participantes = new ArrayList<>();
            }
            
            android.util.Log.d("FiltroAtividades", "Criando adapter com " + participantes.size() + " participantes");
            adapterParticipantes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                participantes
            );
            etFiltroParticipantes.setAdapter(adapterParticipantes);
            android.util.Log.d("FiltroAtividades", "Participantes carregados com sucesso");
        } catch (Exception e) {
            android.util.Log.e("FiltroAtividades", "Erro ao carregar participantes: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao carregar participantes", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarCategorias() {
        try {
            android.util.Log.d("FiltroAtividades", "Iniciando carregamento de categorias");
            List<String> categorias = Arrays.asList(
                "Reunião", "Evento", "Compromisso", "Tarefa", "Lembrete", "Outro"
            );
            
            android.util.Log.d("FiltroAtividades", "Criando adapter com " + categorias.size() + " categorias");
            adapterCategorias = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categorias
            );
            etFiltroCategoria.setAdapter(adapterCategorias);
            android.util.Log.d("FiltroAtividades", "Categorias carregadas com sucesso");
        } catch (Exception e) {
            android.util.Log.e("FiltroAtividades", "Erro ao carregar categorias: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show();
        }
    }

    private void aplicarFiltro() {
        try {
            String titulo = etFiltroTitulo != null ? etFiltroTitulo.getText().toString().trim() : "";
            String dataInicial = etDataInicial != null ? etDataInicial.getText().toString().trim() : "";
            String dataFinal = etDataFinal != null ? etDataFinal.getText().toString().trim() : "";
            String participante = etFiltroParticipantes != null ? etFiltroParticipantes.getText().toString().trim() : "";
            String categoria = etFiltroCategoria != null ? etFiltroCategoria.getText().toString().trim() : "";

            // Validar datas
            if (!dataInicial.isEmpty() && !dataFinal.isEmpty()) {
                try {
                    Date inicio = dateFormat.parse(dataInicial);
                    Date fim = dateFormat.parse(dataFinal);
                    if (inicio.after(fim)) {
                        Toast.makeText(this, "Data inicial deve ser anterior à data final", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    Toast.makeText(this, "Formato de data inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Aplicar filtro e mostrar resultados
            List<Atividade> atividadesFiltradas = atividadeDAO.listarAtividadesFiltradas(
                titulo, dataInicial, dataFinal, participante, categoria
            );

            if (atividadesFiltradas.isEmpty()) {
                Toast.makeText(this, "Nenhuma atividade encontrada com os filtros selecionados", Toast.LENGTH_SHORT).show();
                tvResultados.setVisibility(View.GONE);
                rvResultados.setVisibility(View.GONE);
            } else {
                tvResultados.setVisibility(View.VISIBLE);
                rvResultados.setVisibility(View.VISIBLE);
                adapter.setAtividades(atividadesFiltradas);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            android.util.Log.e("FiltroAtividades", "Erro ao aplicar filtro: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao aplicar filtro", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparFiltros() {
        try {
            if (etFiltroTitulo != null) etFiltroTitulo.setText("");
            if (etDataInicial != null) etDataInicial.setText("");
            if (etDataFinal != null) etDataFinal.setText("");
            if (etFiltroParticipantes != null) etFiltroParticipantes.setText("");
            if (etFiltroCategoria != null) etFiltroCategoria.setText("");
            
            // Limpar resultados
            tvResultados.setVisibility(View.GONE);
            rvResultados.setVisibility(View.GONE);
            adapter.setAtividades(new ArrayList<>());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            android.util.Log.e("FiltroAtividades", "Erro ao limpar filtros: " + e.getMessage(), e);
            Toast.makeText(this, "Erro ao limpar filtros", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
} 