package com.faculdade.reservasala.service;

import com.faculdade.reservasala.model.Reserva;
import com.faculdade.reservasala.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String SALA_SERVICE_URL = "http://sala:8082/api/salas";
    private static final String USUARIO_SERVICE_URL = "http://usuario:8080/api/usuarios";

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> findByUsuarioId(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public Reserva save(Reserva reserva) {
        // Verificar se o usuário existe e está ativo
        try {
            Map<String, Object> usuario = restTemplate.getForObject(USUARIO_SERVICE_URL + "/" + reserva.getUsuarioId(), Map.class);
            if (usuario == null) {
                throw new RuntimeException("Usuário não encontrado");
            }
            
            // Verificar se o usuário está ativo
            if (!(Boolean) usuario.get("ativo")) {
                throw new RuntimeException("Usuário não está ativo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar usuário: " + e.getMessage());
        }

        // Verificar se a sala existe e está disponível no microserviço de sala
        try {
            Map<String, Object> sala = restTemplate.getForObject(SALA_SERVICE_URL + "/" + reserva.getSalaId(), Map.class);
            if (sala == null) {
                throw new RuntimeException("Sala não encontrada");
            }
            
            // Verificar se a sala está disponível
            if (!(Boolean) sala.get("disponivel")) {
                throw new RuntimeException("Sala não está disponível");
            }

            // Verificar se já existe reserva para o mesmo horário
            List<Reserva> reservasExistentes = reservaRepository.findBySalaIdAndDataHoraInicioBetween(
                reserva.getSalaId(),
                reserva.getDataHoraInicio(),
                reserva.getDataHoraFim()
            );

            if (!reservasExistentes.isEmpty()) {
                throw new RuntimeException("Já existe uma reserva para este horário");
            }

            // Se tudo estiver ok, atualiza a disponibilidade da sala
            restTemplate.put(SALA_SERVICE_URL + "/" + reserva.getSalaId() + "/disponibilidade?disponivel=false", null);

            reserva.setStatus("PENDENTE");
            return reservaRepository.save(reserva);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar disponibilidade da sala: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            // Atualiza a disponibilidade da sala para disponível
            try {
                restTemplate.put(SALA_SERVICE_URL + "/" + reserva.get().getSalaId() + "/disponibilidade?disponivel=true", null);
            } catch (Exception e) {
                // Se não conseguir se comunicar com o serviço de sala, apenas ignora
            }
            reservaRepository.deleteById(id);
        }
    }

    public Reserva updateStatus(Long id, String status) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            
            // Se a reserva for cancelada, atualiza a disponibilidade da sala
            if (status.equals("CANCELADA")) {
                try {
                    restTemplate.put(SALA_SERVICE_URL + "/" + reserva.getSalaId() + "/disponibilidade?disponivel=true", null);
                } catch (Exception e) {
                    // Se não conseguir se comunicar com o serviço de sala, apenas ignora
                }
            }
            
            reserva.setStatus(status);
            return reservaRepository.save(reserva);
        }
        return null;
    }
} 