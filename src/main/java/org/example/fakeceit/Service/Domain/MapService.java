package org.example.fakeceit.Service.Domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fakeceit.Entity.GameMap;
import org.example.fakeceit.Exception.Client.IncorrectName;
import org.example.fakeceit.Exception.ClientHTTP.NotFound404;
import org.example.fakeceit.Repositories.MapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class MapService {

    private final MapRepository mapRepository;

    public GameMap createMap(String name, String iconImg, String backgroundImg, String bannerImg, Boolean state) {
        log.info("Попытка создания карты");

        if (mapRepository.existsByName(name)) {
            throw new IncorrectName("Это имя уже занято");
        }

        GameMap gameMap = new GameMap();

        gameMap.setName(name);
        gameMap.setIconImg(iconImg);
        gameMap.setBackgroundImg(backgroundImg);
        gameMap.setBannerImg(bannerImg);
        gameMap.setIsActive(state);

        mapRepository.save(gameMap);

        return gameMap;
    }

    public void deleteMap(Long id) {
        log.info("Попытка удаления карты");

        GameMap gameMap = mapRepository.findById(id).orElseThrow(() -> new NotFound404("Карта не найдена"));

        mapRepository.delete(gameMap);
    }

    public void setIsActive(Long id, Boolean state) {
        log.info("Попытка изменения статуса карты");

        GameMap gameMap = mapRepository.findById(id).orElseThrow(() -> new NotFound404("Карта не найдена"));

        gameMap.setIsActive(state);
    }

    public List<GameMap> getActiveMaps(Boolean state) {
        return mapRepository.findByIsActive(state);
    }

    @Transactional(readOnly = true)
    public GameMap getMapById(Long id) {
        log.info("Попытка получения информации о карте по ID");

        return mapRepository.findById(id).orElseThrow(() -> new NotFound404("Карта не найдена"));
    }

    @Transactional(readOnly = true)
    public GameMap getMapByName(String name) {
        log.info("Попытка получения информации о карте по названию");

        return mapRepository.findByName(name).orElseThrow(() -> new NotFound404("Карта не найдена"));
    }

}
