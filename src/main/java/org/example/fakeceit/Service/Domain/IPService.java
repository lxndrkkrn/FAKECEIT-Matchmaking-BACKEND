package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.IP;
import org.example.fakeceit.Enum.ServerRegion;
import org.example.fakeceit.Enum.ServerStatus;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.IPRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class IPService {

    private final IPRepository ipRepository;

    public IP createIP(String ip, Integer port, ServerStatus status, ServerRegion region, String rcon) {
        log.info("Попытка создания сервера");

        IP ipAddress = new IP();

        ipAddress.setIp(ip);
        ipAddress.setPort(port);
        ipAddress.setStatus(status);
        ipAddress.setRegion(region);
        ipAddress.setRcon(rcon);

        return ipAddress;
    }

    public void deleteIP(Long id) {
        log.info("Попытка удаления сервера");

        IP ip = ipRepository.findById(id).orElseThrow(() -> new NotFound404("Сервер не найден"));

        ipRepository.delete(ip);
    }

    //CMD logic() {}
}
