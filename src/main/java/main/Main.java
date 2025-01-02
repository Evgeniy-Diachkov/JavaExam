package main;

import console.ConsoleInterface;

/**
 * Главный класс для запуска приложения.
 */
public class Main {
    public static void main(String[] args) {
        ConsoleInterface consoleInterface = new ConsoleInterface();
        consoleInterface.start();
    }
}
