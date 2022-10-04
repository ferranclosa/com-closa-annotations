package com.closa.annotations.translate.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class Internationalization extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${com.closa.translate.locales.default}")
    private String defaultLocale;

    @Value("#{'${com.closa.translate.locales.implemented}'.split(',')}")
    private Set<String> implemented;


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        List<Locale> LOCALES = implemented.stream().map(one -> new Locale(one.toLowerCase())).collect(Collectors.toList());
        Locale locale = Locale.getDefault();
        String headerLang = request.getHeader("Accept-Language");
        if (headerLang != null ){
            if(!LOCALES.contains(headerLang)){
                logger.warn("The Language requested ("+ headerLang + ") is not currently supported. Default will be used instead");
                return locale;
            };
        }
        return headerLang == null || headerLang.isEmpty()
                ? Locale.getDefault()
                : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
    }

}