package com.closa.annotations.textmarshaler.engine;

import com.closa.annotations.textmarshaler.interfaces.TXTMarshallString;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallChar;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallBoolean;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallDate;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallNumber;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTMarshall;
import com.closa.annotations.textmarshaler.model.WorkingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MarshalEngine {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    public MarshalEngine() {

    }

    Map<Field, WorkingObject> shouMap = new HashMap<>();

    public final String marshal(Object obj) {

        logger.info("Entered the marshal for " + obj.getClass().getSimpleName());

        if (!obj.getClass().isAnnotationPresent(TXTMarshall.class)) {
            logger.error("Missing @TXTMarshallc annotation for object" + obj.getClass().getSimpleName());
            throw new RuntimeException("Error to marshal. @TXTMarshall annotation is missing");
        }

        String result = null;
        Annotation annot = null;
        /** Deal with the FIELD level
         *
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for (Field current : fieldList) {
            buildTheMap(current);
        }
        /**
         * We should have the full class map.
         * We need to sort it by order
         * and then process each item and build the String
         */
        Comparator<WorkingObject> byOrder = (WorkingObject obj1, WorkingObject obj2) -> obj1.getOrder().compareTo(obj2.getOrder());

        LinkedHashMap<Field, WorkingObject> sortedMap = shouMap.entrySet().stream()
                .sorted(Map.Entry.<Field, WorkingObject>comparingByValue(byOrder))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        sortedMap.entrySet().stream().forEach(each -> {
            Field fld = each.getKey();
            WorkingObject wo = each.getValue();
            fld.setAccessible(true);
            try {
                Object o = fld.get(obj);
                System.out.println(o.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });



        return " ";
    }

    private void buildTheMap(Field current ) {
        boolean validCompatibility = false;
        Annotation annot = null;
        AnnotationUtils.findAnnotation(TXTMarshallString.class, null );
        boolean x = current.isAnnotationPresent(TXTMarshallString.class) ;
        if (current.isAnnotationPresent(TXTMarshallString.class)) {
            annot = current.getAnnotation(TXTMarshallString.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallString txtMarshallString = (TXTMarshallString) annot;
            shouMap.put(current,
                    new WorkingObject(txtMarshallString.order(), txtMarshallString.length(),
                            txtMarshallString.paddingChar(),
                            txtMarshallString.padPosition(),
                            txtMarshallString.nullChar()));
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallChar.class)) {
            annot = current.getAnnotation(TXTMarshallChar.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallChar txtMarshallChar = (TXTMarshallChar) annot;
            shouMap.put(current,
                    new WorkingObject(txtMarshallChar.order(),
                            txtMarshallChar.nullChar()));
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallBoolean.class)) {
            annot = current.getAnnotation(TXTMarshallBoolean.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallBoolean txtMarshallBoolean = (TXTMarshallBoolean) annot;
            shouMap.put(current,
                    new WorkingObject(txtMarshallBoolean.order(),
                            txtMarshallBoolean.pattern(),
                            txtMarshallBoolean.nullChar()));
            return;
        }
        if (current.isAnnotationPresent(TXTMarshallNumber.class)) {
            annot = current.getAnnotation(TXTMarshallNumber.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallNumber txtMarshallNumber = (TXTMarshallNumber) annot;
            shouMap.put(current,
                    new WorkingObject(txtMarshallNumber.order(), txtMarshallNumber.length(),
                           txtMarshallNumber.paddingChar(), txtMarshallNumber.padPosition(),
                            txtMarshallNumber.nullChar()));
            return;
        }
        if (current.isAnnotationPresent(TXTMarshallDate.class)) {
            annot = current.getAnnotation(TXTMarshallDate.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallDate txtMarshallDate = (TXTMarshallDate) annot;
            shouMap.put(current,
                    new WorkingObject(txtMarshallDate.order(),
                            txtMarshallDate.datePattern(),
                            txtMarshallDate.nullChar()));
            return;
        }

    }




    private Boolean checkTypeCompatibility(Field current, Annotation annot) {

        Object fieldType = current.getType();
        if ((annot instanceof TXTMarshallString) && (fieldType instanceof String)) {
            return true;
        }
        if ((annot instanceof TXTMarshallChar) && (fieldType instanceof Character)) {
            return true;
        }
        if ((annot instanceof TXTMarshallNumber) && (fieldType instanceof Number)) {
            return true;
        }
        if ((annot instanceof TXTMarshallBoolean) && (fieldType instanceof Boolean)) {
            return true;
        }
        if ((annot instanceof TXTMarshallDate) && (fieldType instanceof LocalDate)) {
            return true;
        }
        return false;

    }


}
