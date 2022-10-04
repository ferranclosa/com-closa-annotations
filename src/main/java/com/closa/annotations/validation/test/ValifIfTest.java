package com.closa.annotations.validation.test;

import com.closa.annotations.validation.engine.ValidationEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ValifIfTest {
    @Autowired
    ValidationEngine validationEngine;


    /**
     * To test the TRanslation Annotation. Cat class that has two annotations: 
     * @Translation, where we specify the reousrce bundle and it also servs as a validator for the FIELD
     * @Translate. With @Translate we need to indicate on the value the key of the bundle file
     *
     * The API will return the Cat values translated to fr, sp, ca or en;
     *
     * The above TranslationEngine will look for any annotated fields and will return the value
     * on the Bundle file with the key specified on the @Translate annotation value. It will do that
     * for any Object that we pass to it.
     *
     * It uses the COntext Locale, which can be changed by setting the Accept-Language Header
     * on the call (postman or others)
     * @return
     *
     * It will return the translated text or string assuming that the Locale passed it's supported
     * on the bundle files, if not, it will return the English version, which is the default.
     *
     */
    @CrossOrigin
    @PostMapping(value = "/validIf", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ValidIfoDTO>  getTheCat (@RequestBody DependsDTO idto){

        ValidIfoDTO oDto = new ValidIfoDTO();

        oDto  = validationEngine.isValid(idto, oDto);

        return new ResponseEntity<>(oDto, HttpStatus.OK);

    }
}
