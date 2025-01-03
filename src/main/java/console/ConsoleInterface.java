package console;

import model.Link;
import service.LinkManager;
import service.LinkShortener;
import service.UserManager;

import java.util.Scanner;
import java.util.UUID;

/**
 * Консольный интерфейс для взаимодействия с пользователем.
 */
public class ConsoleInterface {
    private final LinkShortener linkShortener;
    private final LinkManager linkManager;
    private final UserManager userManager;
    private final Scanner scanner;
    private UUID currentUser;

    /**
     * Конструктор для инициализации сервисов и сканера.
     */
    public ConsoleInterface() {
        this.linkShortener = new LinkShortener();
        this.linkManager = new LinkManager(linkShortener.getLinks());
        this.userManager = new UserManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Получение экземпляра LinkShortener.
     *
     * @return Экземпляр LinkShortener.
     */
    public LinkShortener getLinkShortener() {
        return linkShortener;
    }

    /**
     * Запуск интерфейса.
     */
    public void start() {
        System.out.println("Добро пожаловать в сервис сокращения ссылок!");
        System.out.println("Вы первый раз используете сервис? (yes/no):");
        String isFirstTime = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(isFirstTime)) {
            handleFirstTime();
        } else if ("no".equals(isFirstTime)) {
            handleReturningUser();
        } else {
            System.out.println("Неверный ввод. Попробуйте снова.");
            start(); // Рекурсия для повторного выбора
            return;
        }

        while (true) {
            System.out.println("Введите команду (create, visit, stats, delete, list_users, update_clicks, exit):");
            String command = scanner.nextLine().trim();

            switch (command) {
                case "create":
                    handleCreate();
                    break;
                case "visit":
                    handleVisit();
                    break;
                case "stats":
                    handleStats();
                    break;
                case "delete":
                    handleDelete();
                    break;
                case "list_users":
                    handleListUsers();
                    break;
                case "update_clicks":
                    handleUpdateClicks();
                    break;
                case "exit":
                    System.out.println("Выход из программы.");
                    linkShortener.cleanupLinks(); // Очистка протухших ссылок перед выходом
                    return;
                default:
                    System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }
    }

    private void handleFirstTime() {
        while (true) {
            System.out.println("Введите ваше имя для регистрации:");
            String username = scanner.nextLine().trim();
            try {
                this.currentUser = userManager.createUser(username);
                System.out.println("Регистрация завершена! Ваш UUID: " + currentUser);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleReturningUser() {
        while (true) {
            System.out.println("Введите ваше имя:");
            String username = scanner.nextLine().trim();
            System.out.println("Введите ваш UUID:");
            String uuidInput = scanner.nextLine().trim();

            if (!userManager.userExists(username)) {
                System.out.println("Пользователь не найден. Попробуйте снова.");
            } else {
                UUID storedUuid = userManager.getUserId(username);
                try {
                    UUID inputUuid = UUID.fromString(uuidInput);
                    if (storedUuid.equals(inputUuid)) {
                        this.currentUser = storedUuid;
                        System.out.println("Добро пожаловать обратно! Ваш UUID подтвержден.");
                        break;
                    } else {
                        System.out.println("Ошибка: UUID не соответствует имени пользователя. Попробуйте снова.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: Неверный формат UUID. Попробуйте снова.");
                }
            }
        }
    }


    private void handleCreate() {
        System.out.println("Введите длинную ссылку:");
        String longLink = scanner.nextLine();
        System.out.println("Введите лимит переходов:");
        int clicks = Integer.parseInt(scanner.nextLine());
        System.out.println("Введите желаемое время жизни ссылки (в секундах):");
        long timeSeconds = Long.parseLong(scanner.nextLine());

        Link link = linkShortener.createShortLink(longLink, currentUser, clicks, timeSeconds);
        System.out.println("Короткая ссылка создана: " + link.getShortLink());
        System.out.println("Срок действия: до " + link.getExpirationDate());
    }

    private void handleVisit() {
        System.out.println("Введите короткую ссылку:");
        String shortLink = scanner.nextLine();

        try {
            String longLink = linkManager.processVisit(shortLink);
            System.out.println("Переход выполнен. Открывается ссылка: " + longLink);

            // Убедимся, что ссылка содержит протокол
            if (!longLink.startsWith("http://") && !longLink.startsWith("https://")) {
                longLink = "http://" + longLink; // Добавляем http, если протокол отсутствует
            }

            // Попытка открыть ссылку в браузере
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.browse(new java.net.URI(longLink));
            } else {
                System.out.println("Открытие браузера не поддерживается на этой платформе.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Не удалось открыть браузер: " + e.getMessage());
        }
    }


    private void handleStats() {
        System.out.println("Введите короткую ссылку:");
        String shortLink = scanner.nextLine();

        linkShortener.getLinks().values().stream()
                .filter(link -> link.getShortLink().equals(shortLink))
                .findFirst()
                .ifPresentOrElse(link -> {
                    System.out.println("Оригинальная ссылка: " + link.getLongLink());
                    System.out.println("Оставшиеся переходы: " + link.getClicksLeft());
                    System.out.println("Срок действия до: " + link.getExpirationDate());
                }, () -> System.out.println("Ссылка не найдена."));
    }

    private void handleDelete() {
        System.out.println("Введите короткую ссылку:");
        String shortLink = scanner.nextLine();

        linkShortener.getLinks().values().stream()
                .filter(link -> link.getShortLink().equals(shortLink))
                .findFirst()
                .ifPresentOrElse(link -> {
                    if (link.getUserUi().equals(currentUser)) {
                        linkShortener.getLinks().remove(link.getId());
                        System.out.println("Ссылка успешно удалена.");
                    } else {
                        System.out.println("Ошибка: вы не являетесь владельцем этой ссылки.");
                    }
                }, () -> System.out.println("Ссылка не найдена."));
    }


    private void handleListUsers() {
        System.out.println("Список всех пользователей:");
        userManager.listUsers();
    }

    private void handleUpdateClicks() {
        System.out.println("Введите короткую ссылку:");
        String shortLink = scanner.nextLine();

        linkShortener.getLinks().values().stream()
                .filter(link -> link.getShortLink().equals(shortLink))
                .findFirst()
                .ifPresentOrElse(link -> {
                    if (link.getUserUi().equals(currentUser)) {
                        System.out.println("Текущий лимит переходов: " + link.getClicksLeft());
                        System.out.println("Введите новый лимит переходов:");
                        int newClicks = Integer.parseInt(scanner.nextLine());
                        link.setClicksLeft(newClicks);
                        System.out.println("Лимит переходов обновлен до: " + newClicks);
                    } else {
                        System.out.println("Ошибка: вы не являетесь владельцем этой ссылки.");
                    }
                }, () -> System.out.println("Ссылка не найдена."));
    }

}
