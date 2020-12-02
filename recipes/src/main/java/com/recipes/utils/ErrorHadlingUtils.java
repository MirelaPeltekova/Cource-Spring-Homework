package com.recipes.utils;

import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorHadlingUtils {

    public static List<String> getViolationsAsStringLists(Errors errors) {
        //glabal errors+ field
        List<String> globalErrors= errors.getGlobalErrors().stream()
                .map(err -> err.getDefaultMessage())
                .collect(Collectors.toList());
        List<String> fieldErrors= errors.getFieldErrors().stream()
                .map(err -> err.getField() + " = " + err.getRejectedValue() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        List<String>allErrors=new ArrayList<>(globalErrors);
        allErrors.addAll(fieldErrors);
        return allErrors;
    }
}
