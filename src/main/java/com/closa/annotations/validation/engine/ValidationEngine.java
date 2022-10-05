package com.closa.annotations.validation.engine;

import com.closa.annotations.validation.test.ValidIfoDTO;
import com.closa.annotations.validation.interfaces.ValidIf;
import com.closa.annotations.validation.model.ValidTypes;
import com.closa.annotations.validation.model.ValidityValues;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.closa.annotations.validation.model.ValidityValues.*;

@Service
public class ValidationEngine {

    Set<String> errors = new HashSet<>();


    public ValidIfoDTO isValid(Object obj, ValidIfoDTO oDto) {
        ValidIfoDTO odto = oDto;
        Set<String> errors = this.innerValidation(obj);
        if (errors.size() > 0) {
            odto.setValid(false);
            odto.getErrors().addAll(errors);
        } else
            odto.setValid(true);
        return odto;
    }


    private Set<String> innerValidation(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();

        List<Field> fieldList = Arrays.stream(fields).filter(x -> !x.isSynthetic()).collect(Collectors.toList());
        for (Field current : fieldList) {
            if (current.isAnnotationPresent(ValidIf.class)) {
                /**
                 * All ValidIf assume that the current field is NOT null, if it is NULL, then skip Validation.
                 */
                current.setAccessible(true);
                try {
                    Object temp = current.get(obj);
                    if (temp == null) {
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    System.out.println("It should not happen as accesible is true");
                }
                /**
                 * If we are here it means that the current field is not NULL
                 */
                Annotation annot = current.getAnnotation(ValidIf.class);
                /**
                 * Get the "what" field (The Other Field)
                 */
                ValidIf vli = (ValidIf) annot;
                String whatFieldStr = vli.field();
                /**
                 * Check that it exists in the Class
                 */
                Field theOther = null;
                try {
                    theOther = obj.getClass().getDeclaredField(whatFieldStr);
                } catch (NoSuchFieldException e) {
                    errors.add("Field [" + whatFieldStr + "] does not exist in [" + obj.getClass().getSimpleName() + "]");
                    continue;
                }

                /**
                 * Get the ValidityValue
                 */
                ValidityValues checks = vli.is();


                /**
                 * Now we have the
                 *  - the Current field
                 *  - the Other field
                 *  - the Validity Check
                 */

                ValidTypes oneOf = narrowDownTypes(theOther);

                /**
                 * We need to check the consistency between
                 *  - the Other's value and
                 *  - the Validity Check
                 *
                 *  'for example if Validity check is TRUE, then
                 *  theOther's type must be compatible with Boolean,
                 *  or if POSITIVE then the type must be numeric
                 *
                 */
                if (!validateRequest(current, theOther, checks, oneOf)) {
                    continue;
                }
                /**
                 * Now we'll perform the actual validation of the combinations
                 */

                executeValidation(obj, current, theOther, checks);
            }
        }
        return errors;
    }

    private void executeValidation(Object obj, Field current, Field theOther, ValidityValues checks) {

        String strVal = null;
        Boolean boolVal = null;
        Number numVal = null;
        LocalDate dateVal = null;
        Character charVal = null ;
        /**
         * Get the value of theOther
         */
        Object valueInTheOther = null;
        try {
            theOther.setAccessible(true);
            valueInTheOther = theOther.get(obj);
            if (valueInTheOther instanceof String) {
                strVal = (String) valueInTheOther;
            }
            if (valueInTheOther instanceof Number) {
                numVal = (Number) valueInTheOther;
            }
            if (valueInTheOther instanceof Boolean) {
                boolVal = (Boolean) valueInTheOther;
            }
            if (valueInTheOther instanceof Character) {
                charVal = (Character) valueInTheOther;
            }
            if (valueInTheOther instanceof LocalDate) {
                dateVal = (LocalDate) valueInTheOther;
            }
        } catch (IllegalAccessException e) {
            System.out.println("It should not happen as accesible is true");
        }

        switch (checks) {
            case NULL:
                if ((valueInTheOther) != null) {
                    errors.add("Invalid value in [" + theOther.getName() + "] ( it should be NULL)  with regards to [" + current.getName() + "]");
                }
                break;
            case NOT_NULL:
                if (valueInTheOther == null) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] (it should not be NULL)  with regards to [" + current.getName() + "]");
                }
                break;
            case BLANK:
                if (strVal != "") {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be Blanks)  with regards to [" + current.getName() + "]");
                }
                break;
            case NOT_BLANK:
                if (strVal == "") {
                    errors.add("Invalid value in  [" + theOther.getName() + "] (it should Not be Blanks)  with regards to [" + current.getName() + "]");
                }
                break;
            case TRUE:
                if (boolVal != true) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be True)  with regards to [" + current.getName() + "]");
                }
                break;
            case FALSE:
                if (boolVal != false) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be False)  with regards to [" + current.getName() + "]");
                }
                break;
            case POSITIVE:
                if (numVal.longValue() <= 0) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be Greater than 0)  with regards to [" + current.getName() + "]");
                }
                break;
            case NEGATIVE:
                if (numVal.longValue() >= 0) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be Less than 0)  with regards to [" + current.getName() + "]");
                }
                break;
            case ZERO:
                if (numVal.longValue() != 0) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be 0)  with regards to [" + current.getName() + "]");
                }
            case PAST:
                if (!dateVal.isBefore(LocalDate.now())) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be in the Past)  with regards to [" + current.getName() + "]");
                }
            case FUTURE:
                if (!dateVal.isAfter(LocalDate.now())) {
                    errors.add("Invalid value in  [" + theOther.getName() + "] ( it should be in the Future)  with regards to [" + current.getName() + "]");
                }
                break;
        }
    }

    private boolean validateRequest(Field current, Field theOther, ValidityValues checks, ValidTypes oneOf) {

        /**
         * We need to check the consistency between
         *  - the Other's value and
         *  - the Validity Check
         *
         *  'for example if Validity check is TRUE, then
         *  theOther's type must be compatible with Boolean,
         *  or if POSITIVE then the type must be numeric
         *
         */
        switch (oneOf) {
            /**
             * Boolean supports false, true or null
             */
            case BOOLEAN_:
                if ((checks != TRUE && checks != FALSE) && (checks != NULL && checks != NOT_NULL)) {
                    errors.add("Invalid Combination of field [" + theOther.getName() + "] with data type (BOOLEAN) and Validation Check (" + checks + ")");
                    return false;
                }
                break;
            /**
             * boolean considers that null is false, which is not true
             */
            case _BOOLEAN:
                if ((checks != TRUE && checks != FALSE)) {
                    errors.add("Invalid Combination of field [" + theOther.getName() + "] with data type (primitive boolean) and Validation Check (" + checks + ")");
                    return false;
                }
                break;
            case NUMBER:
                if ((checks != ZERO && checks != POSITIVE && checks != NEGATIVE) && (checks != NULL && checks != NOT_NULL)) {
                    errors.add("Invalid Combination of field [" + theOther.getName() + "] with data type (NUMBER) and Validation Check (" + checks + ")");
                    return false;
                }
                break;
            case STRING:
                if ((checks != BLANK && checks != NOT_BLANK) && (checks != NULL && checks != NOT_NULL)) {
                    errors.add("Invalid Combination of field [" + theOther.getName() + "] with data type (STRING) and Validation Check (" + checks + ")");
                    return false;
                }
                break;
        }

        return true;
    }

    private ValidTypes narrowDownTypes(Field theOther) {
        String otherType = theOther.getType().getSimpleName();
        Boolean isPrimitive = false;
        if(otherType.equals("boolean")
                ||  otherType.equals("int")
                ||  otherType.equals("long")
                ||  otherType.equals("double")
                ||  otherType.equals("float")
                ||  otherType.equals("char")
                ){
            isPrimitive = true;
        }

        ValidTypes oneOf = null;
        if (otherType.equalsIgnoreCase(String.valueOf(ValidTypes.BOOLEAN))) {
            if(isPrimitive){
                oneOf = ValidTypes._BOOLEAN;
            } else {
                oneOf = ValidTypes.BOOLEAN_;
            }
            return oneOf;
        }
        if (otherType.equalsIgnoreCase(String.valueOf(ValidTypes.LONG))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.INT))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.INTEGER))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.DOUBLE))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.NUMBER))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.FLOAT))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.BIGDECIMAL))
        ) {
            if(isPrimitive){
                oneOf = ValidTypes._NUMBER;
            } else {
                oneOf = ValidTypes.NUMBER_;
            }
            return oneOf;
        }

        if (otherType.equalsIgnoreCase(String.valueOf(ValidTypes.STRING))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.CHARACTER))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.CHAR))
        ) {
            oneOf = ValidTypes.STRING;
            return oneOf;
        }
        if (otherType.equalsIgnoreCase(String.valueOf(ValidTypes.LOCALDATETIME))
                || otherType.equalsIgnoreCase(String.valueOf(ValidTypes.LOCALEDATE))

        ) {
            oneOf = ValidTypes.DATE;
            return oneOf;
        }
        return oneOf;
    }
}