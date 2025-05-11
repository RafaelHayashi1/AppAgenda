package com.example.agendaatividadesv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaatividadesv2.dao.UsuarioDAO;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edtNome, edtEmail, edtSenha, edtConfSenha;
    private Button btnRegistrar;
    private TextView txtEntrar;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioDAO = new UsuarioDAO(this);
        inicializarComponentes();
        configurarListeners();
    }

    private void inicializarComponentes() {
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfSenha = findViewById(R.id.edtConfSenha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtEntrar = findViewById(R.id.txtEntrar);
    }

    private void configurarListeners() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarRegistro();
            }
        });

        txtEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void realizarRegistro() {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String confSenha = edtConfSenha.getText().toString().trim();


            long id = usuarioDAO.inserirUsuario(nome, email, senha);
            if (id != -1) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao realizar cadastro!", Toast.LENGTH_SHORT).show();
            }
        }
    }



