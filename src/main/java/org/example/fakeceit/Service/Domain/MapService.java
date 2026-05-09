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

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional

public class MapService {

    private final MapRepository mapRepository;

    public GameMap createMap(String name, String iconImg, String backgroundImg, String bannerImg) {
        log.info("Попытка создания карты");

        if (mapRepository.existByName(name)) {
            throw new IncorrectName("Это имя уже занято");
        }

        GameMap gameMap = new GameMap();

        gameMap.setName(name);
        gameMap.setIconImg(iconImg);
        gameMap.setBackgroundImg(backgroundImg);
        gameMap.setBannerImg(bannerImg);

        mapRepository.save(gameMap);

        return gameMap;
    }

    public void deleteMap(Long id) {
        log.info("Попытка удаления карты");

        GameMap gameMap = mapRepository.findById(id).orElseThrow(() -> new NotFound404("Карта не найдена"));

        mapRepository.delete(gameMap);
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
