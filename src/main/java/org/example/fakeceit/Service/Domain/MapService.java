package org.example.fakeceit.Service.Domain;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.DTOs.Request.Map.CreateMapRequestDTO;
import org.example.fakeceit.DTOs.Request.Map.DeleteMapRequestDTO;
import org.example.fakeceit.DTOs.Request.Map.GetMapByIdRequestDTO;
import org.example.fakeceit.DTOs.Request.Map.GetMapByNameRequestDTO;
import org.example.fakeceit.DTOs.Response.Map.CreateMapResponseDTO;
import org.example.fakeceit.DTOs.Response.Map.DeleteMapResponseDTO;
import org.example.fakeceit.DTOs.Response.Map.GetMapResponseDTO;
import org.example.fakeceit.Entity.Map;
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

public class MapService {

    private final MapRepository mapRepository;

    @Transactional
    public CreateMapResponseDTO createMap(@Valid CreateMapRequestDTO createMapRequestDTO) {
        log.info("Попытка создания карты");

        if (mapRepository.existByName(createMapRequestDTO.name())) {
            throw new IncorrectName("Это имя уже занято");
        }

        Map map = new Map();

        map.setName(createMapRequestDTO.name());
        map.setIconImg(createMapRequestDTO.iconImg());
        map.setBackgroundImg(createMapRequestDTO.backgroundImg());
        map.setBannerImg(createMapRequestDTO.bannerImg());

        mapRepository.save(map);

        return new CreateMapResponseDTO(
                map.getId(),
                map.getName(),
                map.getIconImg(),
                map.getBackgroundImg(),
                map.getBannerImg()
        );
    }

    @Transactional
    public DeleteMapResponseDTO deleteMap(@Valid DeleteMapRequestDTO deleteMapRequestDTO) {
        log.info("Попытка удаления карты");

        Map map = mapRepository.findById(deleteMapRequestDTO.id()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        mapRepository.delete(map);

        return new DeleteMapResponseDTO(
                map.getId()
        );
    }

    @Transactional(readOnly = true)
    public GetMapResponseDTO getMapById(@Valid GetMapByIdRequestDTO getMapByIdRequestDTO) {
        log.info("Попытка получения информации о карте по ID");

        Map map = mapRepository.findById(getMapByIdRequestDTO.id()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        return new GetMapResponseDTO(
                map.getId(),
                map.getName(),
                map.getIconImg(),
                map.getBackgroundImg(),
                map.getBannerImg()
        );
    }

    @Transactional(readOnly = true)
    public GetMapResponseDTO getMapByName(@Valid GetMapByNameRequestDTO getMapByNameRequestDTO) {
        log.info("Попытка получения информации о карте по названию");

        Map map = mapRepository.findByName(getMapByNameRequestDTO.name()).orElseThrow(() -> new NotFound404("Карта не найдена"));

        return new GetMapResponseDTO(
                map.getId(),
                map.getName(),
                map.getIconImg(),
                map.getBackgroundImg(),
                map.getBannerImg()
        );
    }

}
