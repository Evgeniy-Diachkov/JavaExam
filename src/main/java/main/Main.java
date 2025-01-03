package main;

import console.ConsoleInterface;

public class Main {
    public static void main(String[] args) {
        ConsoleInterface console = new ConsoleInterface();

        // Очистка ссылок при завершении программы
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            console.getLinkShortener().cleanupLinks();
            System.out.println("Ссылки успешно сохранены и очищены.");
        }));

        console.getLinkShortener().cleanupLinks(); // Очистка протухших ссылок
        console.start();
    }
}
