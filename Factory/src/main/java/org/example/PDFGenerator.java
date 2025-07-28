package org.example;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    /**
     * Генерирует PDF-файл с заказом и (если передано) добавляет изображение.
     *
     * @param dishes     Список блюд, которые нужно добавить в заказ.
     * @param filePath   Путь для сохранения PDF-файла.
     * @param imagePaths Пути к изображениям (может быть null).
     */
    public static void generatePDF(List<String> dishes, String filePath, List<String> imagePaths) {
        System.out.println("Начало генерации PDF...");
        System.out.println("Передано блюд: " + (dishes != null ? dishes.size() : 0));
        System.out.println("Передано изображений: " + (imagePaths != null ? imagePaths.size() : 0));
        System.out.println("Путь сохранения PDF: " + filePath);

        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            System.out.println("Файл PDF открыт: " + filePath);

            // Поддержка кириллических символов
            PdfFont font = PdfFontFactory.createFont("C:/Windows/Fonts/arial.ttf", "Cp1251", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            document.setFont(font);

            document.add(new Paragraph("Ваш заказ:").setBold().setFontSize(16));
            System.out.println("Добавлен заголовок");

            for (String dish : dishes) {
                document.add(new Paragraph("- " + dish));
                System.out.println("Добавлено блюдо: " + dish);
            }

            if (imagePaths != null && !imagePaths.isEmpty()) {
                for (String imagePath : imagePaths) {
                    System.out.println("Проверка изображения: " + imagePath);
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        try {
                            ImageData imageData = ImageDataFactory.create(imgFile.toURI().toURL());
                            Image image = new Image(imageData);
                            image.setAutoScale(true);
                            document.add(image);
                            System.out.println("Добавлено изображение: " + imagePath);
                        } catch (IOException e) {
                            System.out.println("Ошибка при загрузке изображения: " + imagePath + " - " + e.getMessage());
                        }
                    } else {
                        System.out.println("Ошибка: Файл изображения не найден - " + imagePath);
                    }
                }
            }

            System.out.println("PDF успешно создан: " + filePath);

        } catch (IOException e) {
            System.out.println("Ошибка при создании PDF: " + e.getMessage());
        }
    }
}
