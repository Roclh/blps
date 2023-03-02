//package com.roclh.blps.TestData;
//
//import com.roclh.blps.database.CategoryDatabase;
//import com.roclh.blps.database.StudopediaDatabase;
//import com.roclh.blps.entities.Category;
//import com.roclh.blps.entities.StudopediaArticle;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@NoArgsConstructor
//@Configuration
//public class StudopediaArticleConfig {
//
//
//    @Bean
//    CommandLineRunner commandLineRunner(StudopediaDatabase repository, CategoryDatabase categoryRepository){
//        return args -> {
//            Category category1 = new Category("Math");
//            Category physics = new Category("Physics");
//            Category programming = new Category("Programming");
//            Category shibal = new Category("Shibal");
//            Category russian = new Category("Russian Language");
//            Category english = new Category("English Language");
//            Category lawAndOrder = new Category("Law and Order");
//
//            categoryRepository.saveAll(List.of(category1, programming, physics, shibal, russian, english, lawAndOrder));
//
//            StudopediaArticle article1 = new StudopediaArticle(
//                    "Why we find the X?",
//                    "We find x because we want to",
//                    category1
//            );
//            StudopediaArticle article2 = new StudopediaArticle(
//                    "Newton. Why he was virgin?",
//                    "Because he became insane after an apple fell down on his head",
//                    physics
//            );
//            StudopediaArticle article3 = new StudopediaArticle(
//                    "Языки программирования.",
//                    "ЭВМ может выполнить программу, представленную только на машинном языке.",
//                    programming
//            );
//            StudopediaArticle article4 = new StudopediaArticle(
//                    "Это, опять же, «перекус» предел — 200 ккал.",
//                    "Лучше всего - фрукты, домашние сухарики, нежирный сыр, чай.",
//                    shibal
//            );
//            StudopediaArticle article5 = new StudopediaArticle(
//                    "ПОЯСНИТЕЛЬНАЯ ЗАПИСКА",
//                    "Учебное пособие предназначено в определенной мере, заполнить существующий пробел и помочь студентам в изучении философии.",
//                    russian
//            );
//            StudopediaArticle article6 = new StudopediaArticle(
//                    "РЕФЕРУВАННЯ ПУБЛІЦИСТИЧНОГО ТЕКСТУ",
//                    "Welche Informationen enthalten die Überschriften?",
//                    english
//            );
//            StudopediaArticle article7 = new StudopediaArticle(
//                    "Что не должен делать следователь при допросе потерпевшей?",
//                    "Какие СД следуют после допроса потерпевшей (а иногда и до)?",
//                    lawAndOrder
//            );
//
//            repository.saveAll(List.of(article6,article2, article3, article4, article5, article7, article1));
//        };
//    }
//}
