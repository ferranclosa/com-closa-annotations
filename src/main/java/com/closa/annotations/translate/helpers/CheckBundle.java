package com.closa.annotations.translate.helpers;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Component
public class CheckBundle extends ResourceBundle {

    protected Object handleGetObject(String key) {
        return null;
    }


    public Enumeration<String> getKeys() {
        return null;
    }

    public Boolean checkBundleExists(ResourceBundleMessageSource messageSource){
        String[] baseName  = messageSource.getBasenameSet().toArray(new String[messageSource.getBasenameSet().size()]);
        Locale locale = LocaleContextHolder.getLocale();
        try {
            ResourceBundle rb = this.getBundle(baseName[0], locale);
        } catch (MissingResourceException e) {
            return false;
        }
            return true;
    }
}
