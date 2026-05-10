package thiagosbarros.com.servicehub.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import thiagosbarros.com.servicehub.entity.Usuario;

import java.util.Collection;
import java.util.List;

public class ServiceHubUserDetails implements UserDetails {

    private final Usuario usuario;

    public ServiceHubUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getEmpresaId() {
        return usuario.getEmpresa().getId();
    }

    public Long getUsuarioId() {
        return usuario.getId();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }
}
