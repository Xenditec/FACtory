package org.example;

import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;


public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<String> orderHistory = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            showMainMenu();
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    menu();
                    break;
                case "2":
                    DishSelector.selectDishes(scanner);
                    break;
                case "3":
                    recommendDishes();
                    break;
                case "4":
                    showOrderHistory();
                    break;
                case "5":
                    clearOrderHistory();
                    break;
                case "0":
                    System.out.println("Спасибо за использование программы!");
                    return;
                default:
                    System.out.println("Некорректный ввод. Попробуйте снова.");
            }

        }
    }



    // Главное меню
    private static void showMainMenu() {
        System.out.println("\nГлавное Меню:");
        System.out.println("1) Позиции в меню:");
        System.out.println("2) Подобрать блюдо");
        System.out.println("3) Акционные блюда");
        System.out.println("4) История заказов");
        System.out.println("5) Очистить историю заказов");
        System.out.println("---------------------");
        System.out.println("0) Выход");
    }

    private static void menu(){
        List<Dishes_item> order = new ArrayList<>();
        while (true) {
            System.out.println("\nВЫБЕРИТЕ ПОЗИЦИИ МЕНЮ:");
            System.out.println("--------------");
            for (DishesType type : DishesType.values()) {
                Dishes_item dish = DishesFactory.createDish(type);
                System.out.println((type.ordinal() + 1) + ") " + dish.getName());
            }
            System.out.println("--------------");
            System.out.println("9) Подтвердить заказ");
            System.out.println("0) Отменить заказ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("9")) {
                if (!order.isEmpty()) {
                    finishOrder(order);
                    break;
                } else {
                    System.out.println("Ваш заказ пуст. Добавьте блюда.");
                }
            } else if (choice.equals("0")) {
                break;
            } else {
                try {
                    int dishIndex = Integer.parseInt(choice) - 1;
                    if (dishIndex >= 0 && dishIndex < DishesType.values().length) {
                        Dishes_item dish = DishesFactory.createDish(DishesType.values()[dishIndex]);
                        order.add(dish);
                        System.out.println(dish.getName() + " добавлен в заказ.");
                    } else {
                        System.out.println("Некорректный выбор. Попробуйте снова.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Некорректный ввод. Попробуйте снова.");
                }
            }
        }
    }





    //2 Функция:
    private static void selectOfIngredients(){
        List<Dishes_item> ingredients = new ArrayList<>();
        while(true) {
            System.out.println("Выберите позиции в меню:");
            System.out.println("------------------------");
            System.out.println("strawberry - Клубничный смузи");
            System.out.println("banana - Банановый коктейль");
            System.out.println("kiwi - Смузи из сочной киви");
            System.out.println("laim - Свежий сок лайма");
            System.out.println("-------------------------");
            System.out.println("1 - Подтвердить заказ");
            System.out.println("0 - Отменить заказ");
            for (DishesType type : DishesType.values()){

            }



            String input = scanner.nextLine().trim();
            if (input.equals("1")) {
                if (!ingredients.isEmpty()) {
                    // Функция создающая заказ из указанных ингредиентов
                    //completeOrder(ingredients);
                }
                // breake;
            } else if (input.equals("0")) {
                //break;
            }
        }

    }

    private static void finishOrder(List<Dishes_item> order) {
        System.out.println("\nВаш заказ ГОТОВ и состоит из:");
        System.out.println("——————————-");

        List<String> dishNames = new ArrayList<>();  // Список названий блюд
        List<String> imagePaths = new ArrayList<>(); // Список путей к изображениям

        for (Dishes_item dish : order) {  // Перебираем блюда
            System.out.println("- " + dish.toString());
            dishNames.add(dish.getName());  // Добавляем название блюда

            // Генерируем изображение для каждого блюда и сохраняем путь
            String imagePath = AiImageGenerator.generateDrinkImage(dish.getName());
            if (imagePath != null && !imagePath.isEmpty()) {
                imagePaths.add(imagePath);  // Добавляем путь к изображению
            }
        }

        // Сохраняем заказ в историю
        String orderDescription = String.join(", ", dishNames);
        orderHistory.add(orderDescription);

        System.out.println("Передан список блюд: " + order);

        // Генерируем PDF-чек с изображениями
        String fileName = "order_receipt.pdf";
        System.out.println("Список блюд перед созданием PDF: " + dishNames);
        PDFGenerator.generatePDF(dishNames, fileName, imagePaths);  //

        // Воспроизводим звуковой сигнал
        SoundPlayer.playBellSound();

        System.out.println("Заказ добавлен в историю. Чек сохранён в файл: " + fileName);
    }


    private static void showOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("\nИстория заказов пуста.");
        } else {
            System.out.println("\nИСТОРИЯ ЗАКАЗОВ:");
            System.out.println("---------------------");
            for (int i = 0; i < orderHistory.size(); i++) {
                System.out.println((i + 1) + ") " + orderHistory.get(i));
            }
        }
    }

    private static void clearOrderHistory() {
        orderHistory.clear();
        System.out.println("\nИстория заказов очищена.");
    }

    private static void recommendDishes() {
        System.out.println("\nРЕКОМЕНДУЕМЫЕ БЛЮДА:");
        System.out.println("---------------------");
        org.example.DishesType randomDish = org.example.DishesType.values()[(int) (Math.random() * org.example.DishesType.values().length)];
        Dishes_item dish = DishesFactory.createDish(randomDish);
        System.out.println("Мы рекомендуем: " + dish);
    }

}

