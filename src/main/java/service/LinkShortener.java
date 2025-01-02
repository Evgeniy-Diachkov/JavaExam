package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Link;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис для сокращения ссылок.
 */
public class LinkShortener {
    private final Map<UUID, Link> links = new HashMap<>();
    private final String filePath = "links.json";
    private final Gson gson = new Gson();

    public LinkShortener() {
        loadLinks();
    }

    /**
     * Создание короткой ссылки.
     */
    public Link createShortLink(String longLink, UUID userUi, int clicks) {
        UUID linkId = UUID.randomUUID();
        String shortLink = "https://short.ly/" + linkId.toString().substring(0, 8);
        Link link = Link.builder()
                .longLink(longLink)
                .shortLink(shortLink)
                .userUi(userUi)
                .clicksLeft(clicks)
                .expirationDate(java.time.LocalDateTime.now().plusDays(1))
                .id(linkId)
                .build();
        links.put(linkId, link);
        saveLinks();
        return link;
    }

    /**
     * Получение хранилища ссылок.
     */
    public Map<UUID, Link> getLinks() {
        return links;
    }

    /**
     * Сохранение ссылок в файл.
     */
    private void saveLinks() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(links, writer);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных ссылок: " + e.getMessage());
        }
    }

    /**
     * Загрузка ссылок из файла.
     */
    private void loadLinks() {
        File file = new File(filePath);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<UUID, Link>>() {}.getType();
                Map<UUID, Link> loadedLinks = gson.fromJson(reader, type);
                if (loadedLinks != null) {
                    links.putAll(loadedLinks);
                }
            } catch (IOException e) {
                System.err.println("Ошибка загрузки данных ссылок: " + e.getMessage());
            }
        }
    }
}
