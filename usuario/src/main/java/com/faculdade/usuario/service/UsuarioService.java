package com.faculdade.usuario.service;

import com.faculdade.usuario.model.Usuario;
import com.faculdade.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String RESERVA_SERVICE_URL = "http://reserva-sala:8081/api/reservas";

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findByTipo(String tipo) {
        return usuarioRepository.findByTipo(tipo);
    }

    public List<Usuario> findByAtivo(Boolean ativo) {
        return usuarioRepository.findByAtivo(ativo);
    }

    public Usuario save(Usuario usuario) {
        // Verificar se já existe um usuário com o mesmo email
        Optional<Usuario> existingUser = usuarioRepository.findByEmail(usuario.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Já existe um usuário com este email");
        }
        
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        // Verificar se o usuário tem reservas ativas
        try {
            List<?> reservas = restTemplate.getForObject(RESERVA_SERVICE_URL + "/usuario/" + id, List.class);
            if (reservas != null && !reservas.isEmpty()) {
                throw new RuntimeException("Não é possível excluir o usuário pois ele possui reservas");
            }
        } catch (Exception e) {
            // Se não conseguir se comunicar com o serviço de reservas, apenas ignora
        }
        
        usuarioRepository.deleteById(id);
    }

    public Usuario update(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            
            // Verificar se o novo email já está em uso por outro usuário
            Optional<Usuario> existingUser = usuarioRepository.findByEmail(usuario.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("Já existe um usuário com este email");
            }
            
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public Usuario updateAtivo(Long id, Boolean ativo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setAtivo(ativo);
            return usuarioRepository.save(usuario);
        }
        return null;
    }
} 