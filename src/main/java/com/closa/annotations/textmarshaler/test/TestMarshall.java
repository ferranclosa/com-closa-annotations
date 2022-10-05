package com.closa.annotations.textmarshaler.test;

import com.closa.annotations.textmarshaler.engine.TMEngine;
import com.closa.annotations.validation.engine.ValidationEngine;
import com.closa.annotations.validation.test.ValidIfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestMarshall implements CommandLineRunner {

    @Autowired
    ValidationEngine validationEngine;
    ValidIfoDTO odto = new ValidIfoDTO();
    @Override
    public void run(String... args) throws Exception {
        TextMarshallClass tmc = new TextMarshallClass();
        tmc.setName("Ferran ");
        tmc.setAddress1("6 Rue Vouillé");
        tmc.setAddress2("15ème Arrondissement" );
        tmc.setAddress3("Paris, France");
        tmc.setPostcode(75015);
        tmc.setLastname("Closa-Cervera");
        tmc.setClientCode('£');
        tmc.setMarriedStatus(null);
        tmc.setMarriedDate(LocalDate.of(2022, 4, 21));


        String text = TMEngine.marshal(tmc);

        System.out.println(text);

        TextMarshallClass newOne = (TextMarshallClass) TMEngine.unmarshal(text, new TextMarshallClass());

    }
}
