package br.edu.ifto.clinica.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifto.clinica.model.Usuario;
import br.edu.ifto.clinica.repository.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado"));

        String[] perfis = usuario.getPerfis()
                .stream()
                .map(perfil -> "ROLE_" + perfil.getNome())
                .toArray(String[]::new);

        return User.withUsername(usuario.getUsuario())
                .password(usuario.getSenha())
                .authorities(perfis)
                .build();
    }
}