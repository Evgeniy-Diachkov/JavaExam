package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Link;
import util.LocalDateTimeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис для сокращения ссылок.
 */
public class LinkShortener {
    private final Map<UUID, Link> links = new HashMap<>();
    private final Gson gson;

    private final String filePath = "links.json";

    public LinkShortener() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        loadLinks();
    }

    public Link createShortLink(String longLink, UUID userUi, int clicks, long timeSeconds) {
        UUID linkId = UUID.randomUUID();
        String shortLink = "https://short.ly/" + linkId.toString().substring(0, 8);

        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(timeSeconds);

        Link link = Link.builder()
                .longLink(longLink)
                .shortLink(shortLink)
                .userUi(userUi)
                .clicksLeft(clicks)
                .expirationDate(expirationDate)
                .id(linkId)
                .build();

        links.put(linkId, link);
        saveLinks();
        return link;
    }

    public Map<UUID, Link> getLinks() {
        return links;
    }

    public void cleanupLinks() {
        links.values().removeIf(link ->
                link.getClicksLeft() <= 0 || link.getExpirationDate().isBefore(LocalDateTime.now()));
        saveLinks();
    }

    private void saveLinks() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(links, writer);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения ссылок: " + e.getMessage());
        }
    }

    private void loadLinks() {
        File file = new File(filePath);

        // Если файл отсутствует, создаем пустое хранилище
        if (!file.exists()) {
            System.out.println("Файл links.json не найден. Создается пустое хранилище ссылок.");
            return;
        }

        // Если файл пустой, загружаем пустое хранилище
        if (file.length() == 0) {
            System.out.println("Файл links.json пуст. Загружается пустое хранилище.");
            return;
        }

        // Чтение файла, если он существует и не пуст
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, Link>>() {}.getType();
            Map<UUID, Link> loadedLinks = gson.fromJson(reader, type);
            if (loadedLinks != null) {
                links.putAll(loadedLinks);
                System.out.println("Ссылки успешно загружены из файла.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла links.json: " + e.getMessage());
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("Файл links.json поврежден или имеет некорректный формат. Загружается пустое хранилище.");
        }
    }
}
