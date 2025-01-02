package service;

import model.Link;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Класс для управления переходами по ссылкам.
 * Отвечает за проверку доступности ссылок и обновление их состояния.
 */
public class LinkManager {
    private final Map<UUID, Link> links;

    /**
     * Конструктор, принимающий хранилище ссылок.
     *
     * @param links Хранилище ссылок.
     */
    public LinkManager(Map<UUID, Link> links) {
        this.links = links;
    }

    /**
     * Обработка перехода по короткой ссылке.
     *
     * @param shortLink Короткая ссылка.
     * @return Длинная ссылка, если переход успешен.
     * @throws IllegalArgumentException Если ссылка недоступна.
     */
    public String processVisit(String shortLink) {
        // Находим ссылку по короткому значению.
        Link link = links.values().stream()
                .filter(l -> l.getShortLink().equals(shortLink))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена."));

        // Проверяем срок действия ссылки.
        if (link.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Срок действия ссылки истек.");
        }

        // Проверяем оставшееся количество переходов.
        if (link.getClicksLeft() <= 0) {
            throw new IllegalArgumentException("Лимит переходов исчерпан.");
        }

        // Уменьшаем счетчик оставшихся переходов.
        link.setClicksLeft(link.getClicksLeft() - 1);

        return link.getLongLink();
    }
}
