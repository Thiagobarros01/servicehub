package thiagosbarros.com.servicehub.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import thiagosbarros.com.servicehub.entity.Usuario;
import thiagosbarros.com.servicehub.repository.UsuarioRepository;

@Service
public class ServiceHubUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServiceHubUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] parts = username.split("\\|", 2);
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Identificador de login invalido.");
        }

        Long empresaId = Long.parseLong(parts[0]);
        String email = parts[1];

        Usuario usuario = usuarioRepository.findByEmpresaIdAndEmail(empresaId, email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));

        return new ServiceHubUserDetails(usuario);
    }
}
