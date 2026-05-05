package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Domain.Map.CreateMapRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Map.DeleteMapRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Map.GetMapByIdRequestDTO;
import org.example.fakeceit.DTOs.Request.Domain.Map.GetMapByNameRequestDTO;
import org.example.fakeceit.DTOs.Response.Domain.Map.CreateMapResponseDTO;
import org.example.fakeceit.DTOs.Response.Domain.Map.DeleteMapResponseDTO;
import org.example.fakeceit.DTOs.Response.Domain.Map.GetMapResponseDTO;
import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Exception.Client.IncorrectName;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.MapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class MapService {

    private final MapRepository mapRepository;

    public CreateMapResponseDTO createMap(@Valid CreateMapRequestDTO createMapRequestDTO) {
        log.info("Попытка создания карты");

        if (mapRepository.existByName(createMapRequestDTO.name())) {
            throw new IncorrectName("Это имя уже занято");
        }

        GameMap gameMap = new GameMap();

        gameMap.setName(createMapRequestDTO.name());
        gameMap.setIconImg(createMapRequestDTO.iconImg());
        gameMap.setBackgroundImg(createMapRequestDTO.backgroundImg());
        gameMap.setBannerImg(createMapRequestDTO.bannerImg());

        mapRepository.save(gameMap);

        return new CreateMapResponseDTO(
                gameMap.getId(),
                gameMap.getName(),
                gameMap.getIconImg(),
                gameMap.getBackgroundImg(),
                gameMap.getBannerImg()
        );
    }

    public DeleteMapResponseDTO deleteMap(@Valid DeleteMapRequestDTO deleteMapRequestDTO) {
        log.info("Попытка удаления карты");

        GameMap gameMap = mapRepository.findById(deleteMapRequestDTO.id()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        mapRepository.delete(gameMap);

        return new DeleteMapResponseDTO(
                gameMap.getId()
        );
    }

    @Transactional(readOnly = true)
    public GetMapResponseDTO getMapById(@Valid GetMapByIdRequestDTO getMapByIdRequestDTO) {
        log.info("Попытка получения информации о карте по ID");

        GameMap gameMap = mapRepository.findById(getMapByIdRequestDTO.id()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        return new GetMapResponseDTO(
                gameMap.getId(),
                gameMap.getName(),
                gameMap.getIconImg(),
                gameMap.getBackgroundImg(),
                gameMap.getBannerImg()
        );
    }

    @Transactional(readOnly = true)
    public GetMapResponseDTO getMapByName(@Valid GetMapByNameRequestDTO getMapByNameRequestDTO) {
        log.info("Попытка получения информации о карте по названию");

        GameMap gameMap = mapRepository.findByName(getMapByNameRequestDTO.name()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        return new GetMapResponseDTO(
                gameMap.getId(),
                gameMap.getName(),
                gameMap.getIconImg(),
                gameMap.getBackgroundImg(),
                gameMap.getBannerImg()
        );
    }

}
