package org.example;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.net.URI;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AiImageGenerator {

    private static final String API_URL = "https://huggingface.co/CompVis/stable-diffusion-v1-4";
    private static final String API_KEY = "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";

    // Используем относительный путь для сохранения изображений
    private static final String IMAGES_DIR = "src/main/java/images";

    // Максимальное количество попыток и задержка между ними
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 10000; // 10 секунд

    static {
        try {
            // Создаем директорию, если она не существует
            Path imagesPath = Paths.get(IMAGES_DIR);
            if (!Files.exists(imagesPath)) {
                Files.createDirectories(imagesPath);
                System.out.println("Директория создана: " + imagesPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Не удалось создать директорию для изображений: " + e.getMessage());
        }
    }

    public static String generateDrinkImage(String drinkName) {
        try {
            System.out.println("Генерация изображения для напитка: " + drinkName);

            // Формируем промпт для API
            String prompt = "professional photo of " + drinkName + " drink, cocktail photography, white background, 4k, high quality";

            // Генерируем изображение через API
            byte[] imageData = generateImageWithRetry(prompt, MAX_RETRIES, RETRY_DELAY_MS);

            if (imageData != null && imageData.length > 0) {
                // Формируем имя файла
                String fileName = drinkName.toLowerCase().replace(" ", "_") + ".png";
                Path imagePath = Paths.get(IMAGES_DIR, fileName);

                // Сохраняем изображение
                Files.write(imagePath, imageData);

                // Проверяем, был ли файл сохранен
                if (Files.exists(imagePath)) {
                    System.out.println("Изображение успешно сохранено: " + imagePath.toAbsolutePath());
                    System.out.println("prompt: " + prompt);
                    return imagePath.toString();
                } else {
                    System.err.println("Ошибка: файл не был сохранен.");
                    return null;
                }
            } else {
                System.err.println("Получены пустые данные изображения");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Ошибка генерации изображения: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] generateImageWithRetry(String prompt, int maxRetries, long retryDelayMs) throws IOException, InterruptedException {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                return generateImage(prompt);
            } catch (IOException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e; // Превышено количество попыток
                }
                System.err.println("Ошибка при генерации изображения. Попытка " + retryCount + " из " + maxRetries);
                Thread.sleep(retryDelayMs); // Ждем перед повторной попыткой
            }
        }
        return null;
    }

    private static byte[] generateImage(String prompt) throws IOException, InterruptedException {
        // Подготовка тела запроса
        String requestBody = "{\"inputs\":\"" + prompt + "\"}";
        System.out.println("Отправка запроса к API с промптом: " + prompt);

        // Создаем HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Отправляем запрос и обрабатываем ответ
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        System.out.println("Получен ответ от API, статус: " + response.statusCode());

        if (response.statusCode() == 200) {
            byte[] body = response.body();
            System.out.println("Размер полученных данных: " + (body != null ? body.length : 0) + " байт");
            return body;
        } else if (response.statusCode() == 503) {
            String errorBody = new String(response.body());
            System.err.println("Ошибка API: 503, модель загружается. Тело ответа: " + errorBody);
            throw new IOException("Ошибка API: 503, модель загружается");
        } else {
            String errorBody = new String(response.body());
            System.err.println("Ошибка API: " + response.statusCode() + ", тело ответа: " + errorBody);
            throw new IOException("Ошибка API: " + response.statusCode() + ", " + errorBody);
        }
    }

    // Тестовый метод
    public static void main(String[] args) {
        generateDrinkImage("Strawberry smusi");
    }
}