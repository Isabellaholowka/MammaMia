package model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "garcons")
public class Garcon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identificacao;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String rg;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate dateCadastro;

    private int numeroCTPS;

    @Column(length = 2000)
    private String observacoes;

    public Garcon() { }

    public Garcon(String identificacao, String endereco, String rg, String status,
                  LocalDate dateCadastro, int numeroCTPS, String observacoes) {
        this.identificacao = identificacao;
        this.endereco = endereco;
        this.rg = rg;
        this.status = status;
        this.dateCadastro = dateCadastro;
        this.numeroCTPS = numeroCTPS;
        this.observacoes = observacoes;
    }

    // Getters e Setters ORIGINAIS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDateCadastro() { return dateCadastro; }
    public void setDateCadastro(LocalDate dateCadastro) { this.dateCadastro = dateCadastro; }
    public int getNumeroCTPS() { return numeroCTPS; }
    public void setNumeroCTPS(int numeroCTPS) { this.numeroCTPS = numeroCTPS; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // MÉTODOS ADICIONAIS para compatibilidade com o controller (SEM ALTERAR ESTRUTURA)
    public String getNumero() { return this.identificacao; }
    public void setNumero(String numero) { this.identificacao = numero; }
    
    public String getSituacao() { return this.status; }
    public void setSituacao(String situacao) { this.status = situacao; }
    
    public LocalDate getDataCadastro() { return this.dateCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dateCadastro = dataCadastro; }

    @Override
    public String toString() {
        return "Garcon [id=" + id + ", identificacao=" + identificacao + ", endereco=" + endereco +
               ", rg=" + rg + ", status=" + status + ", dateCadastro=" + dateCadastro +
               ", numeroCTPS=" + numeroCTPS + ", observacoes=" + observacoes + "]";
    }
}