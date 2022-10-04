package com.closa.annotations.translate.engine;

import com.closa.annotations.translate.helpers.CheckBundle;
import com.closa.annotations.translate.interfaces.Translate;
import com.closa.annotations.translate.interfaces.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import java.util.stream.Collectors;

@Service
public class TranslationEngine {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    ResourceBundleMessageSource messageSource;

    @Autowired
    CheckBundle checkBundle;

    public TranslationEngine(@Qualifier(value = "messageSource") ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Object translate(Object obj) {
        Locale locale = LocaleContextHolder.getLocale();
        logger.info("Locale being used is " + locale);

        if(!obj.getClass().isAnnotationPresent(Translation.class)){
            logger.error("Missing Translation.class for object" + obj.getClass().getSimpleName());
            logger.error("The use of @Translate at field level, implies the existence of the @Translation annotation at class level. Add a @Translation annotation to class " +obj.getClass().getSimpleName());
            throw new RuntimeException("Error to execute the @Translate command. @Translation annotation is missing");
        }
        Annotation annot = obj.getClass().getAnnotation(Translation.class) ;
        Object temp = ((Translation) annot).resourceBundle();
        if( temp instanceof String) {
            ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
            rs.setBasename((String) temp);
            rs.setDefaultEncoding("UTF-8");
            rs.setUseCodeAsDefaultMessage(true);
            this.messageSource = rs;
        }
        if(!checkBundle.checkBundleExists(this.messageSource)){
            logger.error("Resource Bundle Missing [" + temp.toString() + "]");
            logger.error("Resource Bundle in the  'resourceBundle' property of the @Translation annotation does not exist on the system");
            throw new RuntimeException("Error to execute the @Translate command. Resource Bundle is missing");
        }

        /** Deal with the FIELD level
         *
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList =  Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for(Field current : fieldList){
            if(current.isAnnotationPresent(Translate.class)){
                if(!current.getType().getSimpleName().equalsIgnoreCase("STRING")){
                    logger.error("@Translate will only translate Strings! Field [" + current.getName() + "] is " + current.getType().getSimpleName() );
                    logger.error("Either remove the @Translate annotation from [" + current.getName() + "] in class [" + obj.getClass().getSimpleName() + "] or change [" + current.getName() + "] to String.");
                    continue;
                }

                annot = current.getAnnotation(Translate.class) ;
                Translate trs = (Translate) annot;
                current.setAccessible(true);
                try {
                    current.set(obj, innerTranslate(trs.key(), locale)) ;
                } catch (IllegalAccessException e) {
                    logger.error("IllegalAccessException was thrown. This should never happen, as I have setAccessible to true");
                }
            }
        }
        return obj;
    }
    private  String innerTranslate(String value, Locale locale){
       return messageSource.getMessage(value, null, locale);
    }

}
