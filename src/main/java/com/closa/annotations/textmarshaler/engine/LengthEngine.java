package com.closa.annotations.textmarshaler.engine;

import com.closa.annotations.textmarshaler.interfaces.*;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTMarshall;
import com.closa.annotations.textmarshaler.model.Types;
import com.closa.annotations.textmarshaler.model.WorkingObject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LengthEngine {


    Logger logger = LoggerFactory.getLogger(this.getClass());
    public LengthEngine() {
    }

    String result;
    Map<Field, WorkingObject> shouMap = new HashMap<>();

    public final String marshal(Object obj) {

        logger.info("Entered the marshal for " + obj.getClass().getSimpleName());

        if (!obj.getClass().isAnnotationPresent(TXTMarshall.class)) {
            logger.error("Missing @TXTMarshallc annotation for object" + obj.getClass().getSimpleName());
            throw new RuntimeException("Error to marshal. @TXTMarshall annotation is missing");
        }

        String result = "";
        Annotation annot = null;
        /** Deal with the FIELD level
         *
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for (Field current : fieldList) {
            buildTheMap(current, obj);
        }
        /**
         * We should have the full class map.
         * We need to sort it by order
         * and then process each item and build the String
         */
        result = "";
        Comparator<WorkingObject> byOrder = (WorkingObject obj1, WorkingObject obj2) -> obj1.getOrder().compareTo(obj2.getOrder());
        LinkedHashMap<Field, WorkingObject> sortedMap = shouMap.entrySet().stream()
                .sorted(Map.Entry.<Field, WorkingObject>comparingByValue(byOrder))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        sortedMap.entrySet().stream().forEach(each -> {
            WorkingObject wo = each.getValue();
            buildTheLength(wo);

            });
            return this.result;
        }

    private void buildTheLength(WorkingObject wo) {
    }


    @SneakyThrows
    private void buildTheMap(Field current, Object obj  ) {
        boolean validCompatibility = false;
        Annotation annot = null;
        AnnotationUtils.findAnnotation(TXTUnmarshallString.class, null );
        boolean x = current.isAnnotationPresent(TXTUnmarshallString.class) ;
        if (current.isAnnotationPresent(TXTUnmarshallString.class)) {
            annot = current.getAnnotation(TXTUnmarshallString.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTUnmarshallString txtUnmarshallString = (TXTUnmarshallString) annot;
            WorkingObject wo = new WorkingObject(txtUnmarshallString.order(),
                    txtUnmarshallString.length(),
                    txtUnmarshallString.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.STRING);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallChar.class)) {
            annot = current.getAnnotation(TXTMarshallChar.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallChar txtMarshallChar = (TXTMarshallChar) annot;
            WorkingObject wo =new WorkingObject(txtMarshallChar.order(),
                    txtMarshallChar.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.CHARACTER);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallBoolean.class)) {
            annot = current.getAnnotation(TXTMarshallBoolean.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallBoolean txtMarshallBoolean = (TXTMarshallBoolean) annot;
            WorkingObject wo =new WorkingObject(txtMarshallBoolean.order(),
                    txtMarshallBoolean.pattern(),
                    txtMarshallBoolean.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.BOOLEAN);
            shouMap.put(current, wo);
            return;
        }
        if (current.isAnnotationPresent(TXTMarshallNumber.class)) {
            annot = current.getAnnotation(TXTMarshallNumber.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallNumber txtMarshallNumber = (TXTMarshallNumber) annot;
            WorkingObject wo =new WorkingObject(txtMarshallNumber.order()
                    , txtMarshallNumber.length(),
                    txtMarshallNumber.paddingChar(), txtMarshallNumber.padPosition(),
                    txtMarshallNumber.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.NUMBER);
            shouMap.put(current, wo);
            return;
        }
        if (current.isAnnotationPresent(TXTMarshallDate.class)) {
            annot = current.getAnnotation(TXTMarshallDate.class);
            validCompatibility = checkTypeCompatibility(current, annot);
            TXTMarshallDate txtMarshallDate = (TXTMarshallDate) annot;
            WorkingObject wo =new WorkingObject(txtMarshallDate.order(),
                    txtMarshallDate.datePattern(),
                    txtMarshallDate.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.LOCALDATE);
            shouMap.put(current, wo);
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
