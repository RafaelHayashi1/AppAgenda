package com.example.agendaatividadesv2.modelos;

public class Local {
    private int id;
    private String nome;
    private String endereco;
    private String descricao;

    public Local() {}

    public Local(String nome, String endereco, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome;
    }
} 