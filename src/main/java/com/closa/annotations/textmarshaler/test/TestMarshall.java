package com.closa.annotations.textmarshaler.test;

import com.closa.annotations.textmarshaler.engine.MarshalEngine;
import org.junit.runners.model.TestClass;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestMarshall implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        TextMarshallClass tmc = new TextMarshallClass();
        tmc.setName("Ferran ");
        tmc.setAddress1("6 Rue Vouillé");
        tmc.setAddress2("15ème Arrondissement" );
        tmc.setAddress3("Paris, France");
        tmc.setPostcode(75015);
        tmc.setMarriedStatus(true);
        tmc.setMarriedDate(LocalDate.of(2022, 4, 21));

        MarshalEngine me = new MarshalEngine();
        me.marshal(tmc);


    }
}
