package com.example.agendaatividadesv2.modelos;

public class Atividade {
    private long id;
    private String titulo;
    private String descricao;
    private String data;
    private String hora;
    private String local;
    private String participantes;
    private String categoria;

    public Atividade() {}

    public Atividade(String titulo, String descricao, String data, String hora, String local, String participantes, String categoria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.participantes = participantes;
        this.categoria = categoria;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getParticipantes() { return participantes; }
    public void setParticipantes(String participantes) { this.participantes = participantes; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

}