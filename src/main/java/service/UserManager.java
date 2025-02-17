package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Класс для управления пользователями.
 */
public class UserManager {
    private final Map<String, UUID> users = new HashMap<>();
    private final String filePath = "users.json";
    private final Gson gson = new Gson();

    public UserManager() {
        loadUsers();
    }

    /**
     * Создание нового пользователя.
     *
     * @param username Имя пользователя.
     * @return UUID пользователя, если регистрация успешна.
     * @throws IllegalArgumentException Если имя уже занято.
     */
    public UUID createUser(String username) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Имя уже занято. Выберите другое имя.");
        }
        UUID userId = UUID.randomUUID();
        users.put(username, userId);
        saveUsers();
        return userId;
    }

    /**
     * Проверка существования пользователя.
     *
     * @param username Имя пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Получение UUID пользователя по имени.
     *
     * @param username Имя пользователя.
     * @return UUID пользователя.
     */
    public UUID getUserId(String username) {
        return users.get(username);
    }

    /**
     * Вывод списка всех пользователей и их UUID.
     */
    public void listUsers() {
        System.out.println("Список всех пользователей:");
        users.forEach((username, uuid) ->
                System.out.println("Имя пользователя: " + username + ", UUID: " + uuid));
    }

    /**
     * Сохранение пользователей в файл.
     */
    private void saveUsers() {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения данных пользователей: " + e.getMessage());
        }
    }

    /**
     * Загрузка пользователей из файла.
     */
    private void loadUsers() {
        File file = new File(filePath);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, UUID>>() {}.getType();
                Map<String, UUID> loadedUsers = gson.fromJson(reader, type);
                if (loadedUsers != null) {
                    users.putAll(loadedUsers);
                }
            } catch (IOException e) {
                System.err.println("Ошибка загрузки данных пользователей: " + e.getMessage());
            }
        }
    }
}
