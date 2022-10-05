package com.closa.annotations.textmarshaler.engine;

import com.closa.annotations.textmarshaler.interfaces.*;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTMarshall;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTUnmarshall;
import com.closa.annotations.textmarshaler.model.BooleanPattern;
import com.closa.annotations.textmarshaler.model.Types;
import com.closa.annotations.textmarshaler.model.WorkingObject;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TMEngine {
    static Logger logger = LoggerFactory.getLogger(TMEngine.class);

    static Map<Field, WorkingObject> shouMap = new HashMap<>();

    public static final String marshal(Object obj) {

        logger.info("Entered the marshal for " + obj.getClass().getSimpleName());

        if (!obj.getClass().isAnnotationPresent(TXTMarshall.class)) {
            logger.error("Missing @TXTMarshall annotation for object" + obj.getClass().getSimpleName());
            throw new RuntimeException("Error to marshal. @TXTMarshall annotation is missing");
        }

        StringBuilder result = new StringBuilder();
        Annotation annot = null;

        /** Deal with the FIELD level
         *
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for (Field current : fieldList) {
            buildMarshallMap(current, obj);
        }
        /**
         * We should have the full class map.
         * We need to sort it by order
         * and then process each item and build the String
         */

        List<WorkingObject> sortedList = (List<WorkingObject>) shouMap.entrySet().stream()
                .map(one -> one.getValue())
                .sorted(Comparator.comparingInt(WorkingObject::getOrder))
                .collect(Collectors.toList());
        for (WorkingObject wo : sortedList) {
            result.append(buildTheText(wo));
        }

        return result.toString();
    }

    public static final Object unmarshal(String incomingTXT, Object obj) {

        logger.info("Entered the unmarshal for " + obj.getClass().getSimpleName());

        if (!obj.getClass().isAnnotationPresent(TXTUnmarshall.class)) {
            logger.error("Missing @TXTUNmarshall annotation for object" + obj.getClass().getSimpleName());
            throw new RuntimeException("Error to marshal. @TXTUnmarshall annotation is missing");
        }

        /** Deal with the FIELD level
         *
         */

        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for (Field current : fieldList) {
            buildUnmarshallMap(current, obj);
        }
        /**
         * We should have the full class map.
         * We need to sort it by order
         * and then process each item and build the String
         */

        List<WorkingObject> sortedList = (List<WorkingObject>) shouMap.entrySet().stream()
                .map(one -> one.getValue())
                .sorted(Comparator.comparingInt(WorkingObject::getOrder))
                .collect(Collectors.toList());
        Integer startPos = -1;
        Integer prevLength = -1;
        for (WorkingObject wo : sortedList) {
            if (startPos == -1) {
                startPos = wo.getLength() - 1;
                wo.setStartPos(startPos);
                prevLength = wo.getLength();
            } else {
                startPos = startPos + prevLength;
                wo.setStartPos(startPos);
                prevLength = wo.getLength();
            }
        }

        obj = rebuildObject(incomingTXT, sortedList, obj);
        System.out.println(sortedList.toString());
        return obj;
    }

    @SneakyThrows
    private static Object rebuildObject(String theText, List<WorkingObject> sortedList, Object obj) {

        Object newObj = obj;
        for (WorkingObject wo : sortedList) {
            wo.getField().setAccessible(true);
            switch (wo.getType()) {
                case STRING:
                    wo.getField().set(obj, toStringValue(theText, wo));
                    break;
                case BOOLEAN:
                    wo.getField().set(obj, toBooleanValue(theText, wo));
                    break;
                case CHARACTER:
                    wo.getField().set(obj, toCharacterValue(theText, wo));
                    break;
                case NUMBER:
                    wo.getField().set(obj, toNumberValue(theText, wo));
                default:
                    break;
            }
        }

        return newObj;
    }


    @SneakyThrows
    private static void buildMarshallMap(Field current, Object obj) {
        boolean validCompatibility = false;
        Annotation annot = null;
        if (current.isAnnotationPresent(TXTMarshallString.class)) {
            annot = current.getAnnotation(TXTMarshallString.class);
            checkTypeCompatibility(current, annot);
            TXTMarshallString txtMarshallString = (TXTMarshallString) annot;
            WorkingObject wo = new WorkingObject(txtMarshallString.order(), txtMarshallString.length(),
                    txtMarshallString.paddingChar(),
                    txtMarshallString.padPosition(),
                    txtMarshallString.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.STRING);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallChar.class)) {
            annot = current.getAnnotation(TXTMarshallChar.class);
            checkTypeCompatibility(current, annot);
            TXTMarshallChar txtMarshallChar = (TXTMarshallChar) annot;
            WorkingObject wo = new WorkingObject(txtMarshallChar.order(),
                    txtMarshallChar.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.CHARACTER);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTMarshallBoolean.class)) {
            annot = current.getAnnotation(TXTMarshallBoolean.class);
            checkTypeCompatibility(current, annot);
            TXTMarshallBoolean txtMarshallBoolean = (TXTMarshallBoolean) annot;
            WorkingObject wo = new WorkingObject(txtMarshallBoolean.order(),
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
            checkTypeCompatibility(current, annot);
            TXTMarshallNumber txtMarshallNumber = (TXTMarshallNumber) annot;
            WorkingObject wo = new WorkingObject(txtMarshallNumber.order()
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
            checkTypeCompatibility(current, annot);
            TXTMarshallDate txtMarshallDate = (TXTMarshallDate) annot;
            WorkingObject wo = new WorkingObject(txtMarshallDate.order(),
                    txtMarshallDate.datePattern(),
                    txtMarshallDate.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.LOCALDATE);
            shouMap.put(current, wo);
            return;
        }

    }


    private static String buildTheText(WorkingObject obj) {
        switch (obj.getType()) {
            case STRING:
                switch (obj.getPadPosition()) {
                    case atEnd:
                        return Strings.padEnd((String) obj.getObj(), obj.getLength(), obj.getPadChar());
                    case atStart:
                        return Strings.padStart((String) obj.getObj(), obj.getLength(), obj.getPadChar());
                    default:
                        return obj.getObj().toString();
                }

            case CHARACTER:
                Character x = (Character) obj.getObj();
                if (x == null) {
                    return obj.getNullChar().toString();
                } else
                    return x.toString();

            case NUMBER:
                Number z = (Number) obj.getObj();
                switch (obj.getPadPosition()) {
                    case atEnd:
                        return Strings.padEnd(z.toString(), obj.getLength(), obj.getPadChar());
                    case atStart:
                        return Strings.padStart(z.toString(), obj.getLength(), obj.getPadChar());
                    default:
                        return z.toString();
                }

            case LOCALDATE:
                String patt = null;

                switch (obj.getDatePattern()) {
                    case Y4MD:
                        patt = "yyyy-MM-dd";
                        break;
                    case Y2MD:
                        patt = "yy-MM-dd";
                        break;
                    case DMY2:
                        patt = "dd-MM-yy";
                        break;
                    case DMY4:
                        patt = "dd-MM-yyyy";
                        break;
                    case MDY2:
                        patt = "MM-dd-yy";
                        break;
                    case MDY4:
                        patt = "MM-dd-yyyy";
                        break;
                }
                LocalDate y = (LocalDate) obj.getObj();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(patt);
                if (y == null) {
                    return obj.getNullChar().toString();
                }
                String theDate = dtf.format(y);
                return theDate.replace("-", "");


            case BOOLEAN:
                String truth = null;
                String lie = null;
                switch (obj.getBooleanPattern()) {
                    case Binary:
                        truth = "1";
                        lie = "0";
                        break;
                    case FalseTrue:
                        truth = "FALSE";
                        lie = "TRUE ";
                        break;
                    case YesNo:
                        truth = "Y";
                        lie = "N";
                        break;
                    case OnOff:
                        truth = " ON";
                        lie = "OFF";
                        break;
                }
                Boolean w = (Boolean) obj.getObj();
                if (w == null) {
                    return obj.getNullChar().toString();
                }
                return w ? truth : lie;

            default:
                throw new RuntimeException("Type not yet supported");
        }

    }

    private static void checkTypeCompatibility(Field current, Annotation annot) {

        Object fieldType = current.getType();
        if ((annot instanceof TXTUnmarshallString) && (fieldType.toString().indexOf(".String") != 0)) {
            return;
        }
        if ((annot instanceof TXTUnmarshallChar) && ((fieldType.toString().indexOf(".Character") != 0))) {
            return;
        }
        if ((annot instanceof TXTMarshallNumber) && ((fieldType.toString().indexOf(".Number") != 0))) {
            return;
        }
        if ((annot instanceof TXTUnmarshallBoolean) && ((fieldType.toString().indexOf(".Boolean") != 0))) {
            return;
        }
        if ((annot instanceof TXTUnmarshallDate) && ((fieldType.toString().indexOf(".LocalDate") != 0))) {
            return;
        }
        if ((annot instanceof TXTMarshallString) && (fieldType.toString().indexOf(".String") != 0)) {
            return;
        }
        if ((annot instanceof TXTMarshallChar) && ((fieldType.toString().indexOf(".Character") != 0))) {
            return;
        }
        if ((annot instanceof TXTMarshallNumber) && ((fieldType.toString().indexOf(".Number") != 0))) {
            return;
        }
        if ((annot instanceof TXTMarshallBoolean) && ((fieldType.toString().indexOf(".Boolean") != 0))) {
            return;
        }
        if ((annot instanceof TXTMarshallDate) && ((fieldType.toString().indexOf(".LocalDate") != 0))) {
            return;
        }

        throw new RuntimeException("Invalid usage of the Annotation! ");
    }

    @SneakyThrows
    private static void buildUnmarshallMap(Field current, Object obj) {
        Annotation annot = null;
        AnnotationUtils.findAnnotation(TXTUnmarshallString.class, null);
        boolean x = current.isAnnotationPresent(TXTUnmarshallString.class);
        if (current.isAnnotationPresent(TXTUnmarshallString.class)) {
            annot = current.getAnnotation(TXTUnmarshallString.class);
            checkTypeCompatibility(current, annot);
            TXTUnmarshallString TXTUnmarshallString = (TXTUnmarshallString) annot;
            WorkingObject wo = new WorkingObject(TXTUnmarshallString.order(),
                    TXTUnmarshallString.length(),
                    TXTUnmarshallString.paddingChar(), TXTUnmarshallString.padPosition(), TXTUnmarshallString.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.STRING);
            wo.setField(current);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTUnmarshallChar.class)) {
            annot = current.getAnnotation(TXTUnmarshallChar.class);
            checkTypeCompatibility(current, annot);
            TXTUnmarshallChar TXTUnmarshallChar = (TXTUnmarshallChar) annot;
            WorkingObject wo = new WorkingObject(TXTUnmarshallChar.order(),
                    TXTUnmarshallChar.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.CHARACTER);
            wo.setField(current);
            shouMap.put(current, wo);
            return;
        }

        if (current.isAnnotationPresent(TXTUnmarshallBoolean.class)) {
            annot = current.getAnnotation(TXTUnmarshallBoolean.class);
            checkTypeCompatibility(current, annot);
            TXTUnmarshallBoolean TXTUnmarshallBoolean = (TXTUnmarshallBoolean) annot;
            WorkingObject wo = new WorkingObject(TXTUnmarshallBoolean.order(),
                    TXTUnmarshallBoolean.pattern(),
                    TXTUnmarshallBoolean.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.BOOLEAN);
            wo.setField(current);
            shouMap.put(current, wo);
            return;
        }
        if (current.isAnnotationPresent(TXTMarshallNumber.class)) {
            annot = current.getAnnotation(TXTMarshallNumber.class);
            checkTypeCompatibility(current, annot);
            TXTMarshallNumber txtMarshallNumber = (TXTMarshallNumber) annot;
            WorkingObject wo = new WorkingObject(txtMarshallNumber.order()
                    , txtMarshallNumber.length(),
                    txtMarshallNumber.paddingChar(), txtMarshallNumber.padPosition(),
                    txtMarshallNumber.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.NUMBER);
            wo.setField(current);
            shouMap.put(current, wo);
            return;
        }
        if (current.isAnnotationPresent(TXTUnmarshallDate.class)) {
            annot = current.getAnnotation(TXTUnmarshallDate.class);
            checkTypeCompatibility(current, annot);
            TXTUnmarshallDate TXTUnmarshallDate = (TXTUnmarshallDate) annot;
            WorkingObject wo = new WorkingObject(TXTUnmarshallDate.order(),
                    TXTUnmarshallDate.datePattern(),
                    TXTUnmarshallDate.nullChar());
            current.setAccessible(true);
            wo.setObj(current.get(obj));
            wo.setType(Types.LOCALDATE);
            wo.setField(current);
            shouMap.put(current, wo);
            return;
        }

    }

    private static Boolean isNull(String result, WorkingObject wo) {
        if (result.contains(wo.getNullChar().toString())) {
            return true;

        } else return false;
    }

    private static String toStringValue(String theText, WorkingObject wo) {
        Integer endIndex = wo.getStartPos() + wo.getLength();
        String result = theText.substring(wo.getStartPos(), endIndex);
        if (!isNull(result, wo)) {
            switch (wo.getPadPosition()) {
                case atEnd:
                    return result.replace(wo.getPadChar().toString(), " ").trim();
                case atStart:
                    return result.replace(wo.getPadChar().toString(), " ").trim();
                default:
                    return result.trim();
            }
        } else {
            return null;
        }
    }

    private static Boolean toBooleanValue(String theText, WorkingObject wo) {
        Integer endIndex = wo.getStartPos() + wo.getLength();
        String result = theText.substring(wo.getStartPos(), endIndex);
        if (!isNull(result, wo)) {
            if (result.equalsIgnoreCase("1") && wo.getBooleanPattern().equals(BooleanPattern.Binary) ||
                    result.equalsIgnoreCase("Y") && wo.getBooleanPattern().equals(BooleanPattern.YesNo) ||
                    result.trim().equalsIgnoreCase("ON") && wo.getBooleanPattern().equals(BooleanPattern.OnOff) ||
                    result.trim().equalsIgnoreCase("TRUE") && wo.getBooleanPattern().equals(BooleanPattern.FalseTrue)
            ) {
                return true;
            } else {
                return false;
            }
        } else {
            return null;
        }
    }

    private static Character toCharacterValue(String theText, WorkingObject wo) {
        Integer endIndex = wo.getStartPos() + wo.getLength();
        String result = theText.substring(wo.getStartPos(), endIndex);
        if (!isNull(result, wo)) {
            return Character.valueOf(result.charAt(0));
        } else {
            return null;
        }
    }

    private static Number toNumberValue(String theText, WorkingObject wo) {
        Integer endIndex = wo.getStartPos() + wo.getLength();
        String result = theText.substring(wo.getStartPos(), endIndex);
        if (!isNull(result, wo)) {
            switch (wo.getPadPosition()) {
                case atEnd:
                case atStart:
                    if (wo.getField().getType().toString().indexOf(".Integer") > -1) {
                        return (Number) Integer.valueOf(result.replace(wo.getPadChar().toString(), " ").trim());
                    }
                    if (wo.getField().getType().toString().indexOf(".Long") > -1) {
                        return (Number) Long.valueOf(result.replace(wo.getPadChar().toString(), " ").trim());
                    }
                default:
                    return (Number) Long.valueOf(result.trim());
            }
        } else {
            return null;
        }
    }
}

