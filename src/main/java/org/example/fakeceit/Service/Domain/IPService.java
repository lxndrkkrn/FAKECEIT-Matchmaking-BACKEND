package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.IP.CreateIP_Request_DTO;
import org.example.fakeceit.DTOs.Request.Domain.IP.DeleteIP_Request_DTO;
import org.example.fakeceit.DTOs.Response.Domain.IP.CreateIP_Response_DTO;
import org.example.fakeceit.Entity.IP;
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

    public CreateIP_Response_DTO createIP(CreateIP_Request_DTO createIP_request_dto) {
        log.info("Попытка создания сервера");

        IP ip = new IP();

        ip.setIp(createIP_request_dto.ip());
        ip.setPort(createIP_request_dto.port());
        ip.setStatus(createIP_request_dto.status());
        ip.setRegion(createIP_request_dto.region());
        ip.setRcon(createIP_request_dto.rcon());

        return new CreateIP_Response_DTO(
                ip.getId(),
                ip.getIp(),
                ip.getPort(),
                ip.getStatus(),
                ip.getRegion()
        );
    }

    public void deleteIP(DeleteIP_Request_DTO deleteIPRequestDto) {
        log.info("Попытка удаления сервера");

        IP ip = ipRepository.findById(deleteIPRequestDto.id()).orElseThrow(() -> new NotFound404("Сервер не найден"));

        ipRepository.delete(ip);
    }

    //CMD logic() {}
}
