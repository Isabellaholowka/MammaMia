package model;

import jakarta.persistence.*;

@Entity
@Table(name = "Avaliacoes")

public class Avaliacoes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private int valorTotalDeVendas;

    @Column(nullable = false)
    private String pratosMaisPedidos;

    @Column(nullable = false)
    private int vendasTotais;

    @Column(nullable = false)
    private int desempenho;


    public Avaliacoes() { }

    public Avaliacoes(String cidade, int valorTotalDeVendas, String pratosMaisPedidos, int vendasTotais,
                  int desempenho) {
        this.cidade = cidade;
        this.valorTotalDeVendas = valorTotalDeVendas;
        this.pratosMaisPedidos = pratosMaisPedidos;
        this.vendasTotais = vendasTotais;
        this.desempenho = desempenho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getValorTotalDeVendas() {
        return valorTotalDeVendas;
    }

    public void setValorTotalDeVendas(int valorTotalDeVendas) {
        this.valorTotalDeVendas = valorTotalDeVendas;
    }

    public String getPratosMaisPedidos() {
        return pratosMaisPedidos;
    }

    public void setPratosMaisPedidos(String pratosMaisPedidos) {
        this.pratosMaisPedidos = pratosMaisPedidos;
    }

    public int getVendasTotais() {
        return vendasTotais;
    }

    public void setVendasTotais(int vendasTotais) {
        this.vendasTotais = vendasTotais;
    }

    public int getDesempenho() {
        return desempenho;
    }

    public void setDesempenho(int desempenho) {
        this.desempenho = desempenho;
    }

    @Override
    public String toString() {
        return "Garcon [id=" + id + ", cidade=" + cidade + ", valorTotalDeVendas=" + valorTotalDeVendas +
               ", pratosMaisPedidos=" + pratosMaisPedidos + ", vendasTotais=" + vendasTotais + ", desempenho=" + desempenho +
               "]";
    }
    
}
