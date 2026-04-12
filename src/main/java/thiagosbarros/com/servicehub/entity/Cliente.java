package thiagosbarros.com.servicehub.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "cliente",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_email_empresa", columnNames = {"empresa_id", "email"})
        }
)
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    protected Cliente() {
    }

    public Cliente(String nome, String email, LocalDate dataNascimento, Empresa empresa) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
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
        if (!(o instanceof Cliente cliente)) return false;
        return id != null && Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
