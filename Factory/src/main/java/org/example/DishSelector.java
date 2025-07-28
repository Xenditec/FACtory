package org.example;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DishSelector {
    private static final Set<String> ingredients = new HashSet<>();

    public static void selectDishes(Scanner scanner) {
        System.out.println("\nВЫБЕРИТЕ ИНГРЕДИЕНТЫ:");
        System.out.println("---------------------");
        System.out.println("Доступные ингредиенты: strawberry, kiwi, banana, laim");
        System.out.println("1 - Подтвердить выбор");
        System.out.println("0 - Отменить выбор");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("1")) {
                generateDishes();
                break;
            } else if (input.equals("0")) {
                System.out.println("Выбор отменен.");
                break;
            } else if (ingredients.contains(input)) {
                System.out.println("Ошибка: ингредиент уже выбран!");
                SoundPlayer.playErrorSound();
            } else if (input.equals("strawberry") || input.equals("kiwi") || input.equals("banana") || input.equals("laim")) {
                ingredients.add(input);
                System.out.println("Ингредиент добавлен: " + input);
            } else {
                System.out.println("Неизвестный ингредиент. Попробуйте снова.");
            }
        }
    }

    private static void generateDishes() {
        if (ingredients.isEmpty()) {
            System.out.println("Вы не выбрали ингредиенты.");
            return;
        }

        System.out.println("\nВаши блюда:");
        if (ingredients.contains("strawberry") && ingredients.contains("banana")) {
            System.out.println("- клубнично банановый смузи");
        }
        if (ingredients.contains("kiwi") && ingredients.contains("laim")) {
            System.out.println("- киви лаймовый сок");
        }
        for (String ingredient : ingredients) {
            switch (ingredient) {
                case "strawberry": System.out.println("- клубника"); break;
                case "kiwi": System.out.println("- киви"); break;
                case "banana": System.out.println("- банан"); break;
                case "laim": System.out.println("- лайм"); break;
            }
        }
    }
}