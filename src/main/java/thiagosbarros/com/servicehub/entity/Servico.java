package thiagosbarros.com.servicehub.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "servico",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_servico_nome_empresa", columnNames = {"empresa_id", "nome"})
        }
)
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    protected Servico() {
    }

    public Servico(String nome, Integer duracaoMinutos, BigDecimal preco, Empresa empresa) {
        this.nome = nome;
        this.duracaoMinutos = duracaoMinutos;
        this.preco = preco;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servico servico)) return false;
        return id != null && Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
