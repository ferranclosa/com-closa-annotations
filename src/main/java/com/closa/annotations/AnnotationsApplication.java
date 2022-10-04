package com.closa.annotations;

import com.closa.annotations.translate.engine.TranslationEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AnnotationsApplication {

    static TranslationEngine translationEngine;

    public AnnotationsApplication(TranslationEngine translationEngine) {
        this.translationEngine = translationEngine;
    }

    public static void main(String[] args) {

        SpringApplication.run(AnnotationsApplication.class, args);

    }


}
