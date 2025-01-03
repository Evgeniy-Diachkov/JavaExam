package service;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для управления конфигурацией из файла.
 */
public class ConfigManager {
    private final String configFilePath = "config.json";
    private final Gson gson = new Gson();
    private Map<String, Object> config;

    public ConfigManager() {
        loadConfig();
    }

    /**
     * Загрузка конфигурации из файла.
     */
    private void loadConfig() {
        try (FileReader reader = new FileReader(configFilePath)) {
            config = gson.fromJson(reader, HashMap.class);
        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
            config = new HashMap<>();
        }
    }

    /**
     * Получение значения максимального количества переходов.
     *
     * @return Максимальное количество переходов.
     */
    public int getMaxClicks() {
        Object value = config.get("max_clicks");
        return value != null ? ((Number) value).intValue() : 5; // Значение по умолчанию 5
    }


    /**
     * Получение времени действия из конфигурации.
     *
     * @return Время действия в секундах.
     */
    public long getMaxLinkTimeInSeconds() {
        Map<String, Double> timeConfig = (Map<String, Double>) config.get("max_link_time");
        if (timeConfig == null) {
            return 0;
        }
        long days = timeConfig.getOrDefault("days", 0.0).longValue();
        long hours = timeConfig.getOrDefault("hours", 0.0).longValue();
        long minutes = timeConfig.getOrDefault("minutes", 0.0).longValue();
        long seconds = timeConfig.getOrDefault("seconds", 0.0).longValue();
        return days * 86400 + hours * 3600 + minutes * 60 + seconds;
    }
}

