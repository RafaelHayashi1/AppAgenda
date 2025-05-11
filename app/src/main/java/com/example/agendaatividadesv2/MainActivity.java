package com.example.agendaatividadesv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agendaatividadesv2.dao.UsuarioDAO;
import com.example.agendaatividadesv2.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText edtEmail, edtSenha;
    private Button btnEntrar;
    private TextView txtRegistrar;
    private UsuarioDAO usuarioDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        txtRegistrar = findViewById(R.id.txtRegistrar);


        usuarioDAO = new UsuarioDAO(this);
        sessionManager = new SessionManager(this);


        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
            return;
        }


        btnEntrar.setOnClickListener(v -> fazerLogin());
        txtRegistrar.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }

    private void fazerLogin() {
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = usuarioDAO.verificarLogin(email, senha);
        if (userId != -1) {
            String nome = usuarioDAO.getNomeUsuario(userId);
            sessionManager.createLoginSession(userId, nome, email);

            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
        }
    }
} 