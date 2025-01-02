package model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс Link представляет сокращенную ссылку.
 * Используем Lombok для генерации геттеров, сеттеров и других методов.
 */
@Data // Автоматическая генерация методов equals, hashCode, toString, get, set.
@Builder // Упрощенная генерация объектов через паттерн Builder.
public class Link {
    private String longLink; // Оригинальная длинная ссылка.
    private String shortLink; // Сгенерированная короткая ссылка.
    private UUID userUi; // Уникальный идентификатор пользователя.
    private int clicksLeft; // Количество оставшихся доступных переходов.
    private LocalDateTime expirationDate; // Срок действия ссылки.
    private UUID id; // Уникальный идентификатор ссылки.
}
