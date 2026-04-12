package thiagosbarros.com.servicehub.entity;

import jakarta.persistence.*;
import thiagosbarros.com.servicehub.entity.enums.Role;

import java.util.Objects;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_email_empresa", columnNames = {"empresa_id", "email"})
        }
)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senha, Role role, Empresa empresa) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        if (!(o instanceof Usuario usuario)) return false;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
