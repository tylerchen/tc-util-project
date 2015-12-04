package org.iff.infra.util.validation;

import org.springframework.validation.Errors;

import java.util.List;

public class Validations {

  public static void required(Errors e, String field, String errorCode) {
    required(e, field, errorCode, null, null);
  }

  public static void required(Errors e, String field, String errorCode, String defaultMessage) {
    required(e, field, errorCode, null, defaultMessage);
  }

  public static void required(Errors e, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.required(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void email(Errors e, String field, String errorCode) {
    email(e, field, errorCode, null, null);
  }

  public static void email(Errors e, String field, String errorCode, String defaultMessage) {
    email(e, field, errorCode, null, defaultMessage);
  }

  public static void email(Errors e, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.email(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void creditCard(Errors e, String field, String errorCode) {
    creditCard(e, field, errorCode, null, null);
  }

  public static void creditCard(Errors e, String field, String errorCode, String defaultMessage) {
    creditCard(e, field, errorCode, null, defaultMessage);
  }

  public static void creditCard(Errors e, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.creditCard(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void url(Errors e, String field, String errorCode) {
    url(e, field, errorCode, null, null);
  }

  public static void url(Errors e, String field, String errorCode, String defaultMessage) {
    url(e, field, errorCode, null, defaultMessage);
  }

  public static void url(Errors e, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.url(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void minLength(Errors e, String field, int minLength,
                               String errorCode) {
    minLength(e, field, minLength, errorCode, null, null);
  }

  public static void minLength(Errors e, String field, int minLength,
                               String errorCode, String defaultMessage) {
    minLength(e, field, minLength, errorCode, null, defaultMessage);
  }

  public static void minLength(Errors e, String field, int minLength,
                               String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.minLength(e.getFieldValue(field), minLength))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void maxLength(Errors e, String field, int maxLength,
                               String errorCode) {
    maxLength(e, field, maxLength, errorCode, null, null);
  }

  public static void maxLength(Errors e, String field, int maxLength,
                               String errorCode, String defaultMessage) {
    maxLength(e, field, maxLength, errorCode, null, defaultMessage);
  }

  public static void maxLength(Errors e, String field, int maxLength,
                               String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.maxLength(e.getFieldValue(field), maxLength))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void rangeLength(Errors e, String field, int minLength, int maxLength,
                                 String errorCode) {
    rangeLength(e, field, minLength, maxLength, errorCode, null, null);
  }

  public static void rangeLength(Errors e, String field, int minLength, int maxLength,
                                 String errorCode, String defaultMessage) {
    rangeLength(e, field, minLength, maxLength, errorCode, null, defaultMessage);
  }

  public static void rangeLength(Errors e, String field, int minLength, int maxLength,
                                 String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.rangeLength(e.getFieldValue(field), minLength, maxLength))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void min(Errors e, String field, Number min,
                         String errorCode) {
    min(e, field, min, errorCode, null, null);
  }

  public static void min(Errors e, String field, Number min,
                         String errorCode, String defaultMessage) {
    min(e, field, min, errorCode, null, defaultMessage);
  }

  public static void min(Errors e, String field, Number min,
                         String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.min(e.getFieldValue(field), min))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void max(Errors e, String field, Number max,
                         String errorCode) {
    max(e, field, max, errorCode, null, null);
  }

  public static void max(Errors e, String field, Number max,
                         String errorCode, String defaultMessage) {
    max(e, field, max, errorCode, null, defaultMessage);
  }

  public static void max(Errors e, String field, Number max,
                         String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.max(e.getFieldValue(field), max))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void range(Errors e, String field, Number min, Number max,
                           String errorCode) {
    range(e, field, min, max, errorCode, null, null);
  }

  public static void range(Errors e, String field, Number min, Number max,
                           String errorCode, String defaultMessage) {
    range(e, field, min, max, errorCode, null, defaultMessage);
  }

  public static void range(Errors e, String field, Number min, Number max,
                           String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.range(e.getFieldValue(field), min, max))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void date(Errors e, String field, String pattern,
                          String errorCode) {
    date(e, field, pattern, errorCode, null, null);
  }

  public static void date(Errors e, String field, String pattern,
                          String errorCode, String defaultMessage) {
    date(e, field, pattern, errorCode, null, defaultMessage);
  }

  public static void date(Errors e, String field, String pattern,
                          String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.date(e.getFieldValue(field), pattern))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void zipcode(Errors e, String field, String errorCode) {
    zipcode(e, field, errorCode, null, null);
  }

  public static void zipcode(Errors e, String field,
                             String errorCode, String defaultMessage) {
    zipcode(e, field, errorCode, null, defaultMessage);
  }

  public static void zipcode(Errors e, String field,
                             String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.zipcode(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void idcard(Errors e, String field, String errorCode) {
    idcard(e, field, errorCode, null, null);
  }

  public static void idcard(Errors e, String field, String errorCode, String defaultMessage) {
    idcard(e, field, errorCode, null, defaultMessage);
  }

  public static void idcard(Errors e, String field,
                            String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.idcard(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void pattern(Errors e, String field, String regex,
                             String errorCode) {
    pattern(e, field, regex, errorCode, null, null);
  }

  public static void pattern(Errors e, String field, String regex,
                             String errorCode, String defaultMessage) {
    pattern(e, field, regex, errorCode, null, defaultMessage);
  }

  public static void pattern(Errors e, String field, String regex,
                             String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.pattern(e.getFieldValue(field), regex))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void extension(Errors e, String field, List<String> extensions,
                               String errorCode) {
    extension(e, field, extensions, errorCode, null, null);
  }

  public static void extension(Errors e, String field, List<String> extensions,
                               String errorCode, String defaultMessage) {
    extension(e, field, extensions, errorCode, null, defaultMessage);
  }

  public static void extension(Errors e, String field, List<String> extensions,
                               String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.extension(e.getFieldValue(field), extensions))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void chinese(Errors e, String field, String errorCode) {
    chinese(e, field, errorCode, null, null);
  }

  public static void chinese(Errors e, String field, String errorCode, String defaultMessage) {
    chinese(e, field, errorCode, null, defaultMessage);
  }

  public static void chinese(Errors e, String field,
                             String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.chinese(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void mobile(Errors e, String field, String errorCode) {
    mobile(e, field, errorCode, null, null);
  }

  public static void mobile(Errors e, String field, String errorCode, String defaultMessage) {
    mobile(e, field, errorCode, null, defaultMessage);
  }

  public static void mobile(Errors e, String field,
                            String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.mobile(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void tel(Errors e, String field, String errorCode) {
    mobile(e, field, errorCode, null, null);
  }

  public static void tel(Errors e, String field, String errorCode, String defaultMessage) {
    mobile(e, field, errorCode, null, defaultMessage);
  }

  public static void tel(Errors e, String field,
                         String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.tel(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void lettersOnly(Errors e, String field, String errorCode) {
    lettersOnly(e, field, errorCode, null, null);
  }

  public static void lettersOnly(Errors e, String field, String errorCode, String defaultMessage) {
    lettersOnly(e, field, errorCode, null, defaultMessage);
  }

  public static void lettersOnly(Errors e, String field,
                                 String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.lettersOnly(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void alphaNumeric(Errors e, String field, String errorCode) {
    alphaNumeric(e, field, errorCode, null, null);
  }

  public static void alphaNumeric(Errors e, String field, String errorCode, String defaultMessage) {
    alphaNumeric(e, field, errorCode, null, defaultMessage);
  }

  public static void alphaNumeric(Errors e, String field,
                                  String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.alphaNumeric(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void time(Errors e, String field, String errorCode) {
    time(e, field, errorCode, null, null);
  }

  public static void time(Errors e, String field, String errorCode, String defaultMessage) {
    time(e, field, errorCode, null, defaultMessage);
  }

  public static void time(Errors e, String field,
                          String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.time(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void equalTo(Errors e, String field, String otherField,
                             String errorCode) {
    equalTo(e, field, otherField, errorCode, null, null);
  }

  public static void equalTo(Errors e, String field, String otherField,
                             String errorCode, String defaultMessage) {
    equalTo(e, field, otherField, errorCode, null, defaultMessage);
  }

  public static void equalTo(Errors e, String field, String otherField,
                             String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.equalTo(e.getFieldValue(field), e.getFieldValue(otherField)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void digits(Errors e, String field, String errorCode) {
    digits(e, field, errorCode, null, null);
  }

  public static void digits(Errors e, String field, String errorCode, String defaultMessage) {
    digits(e, field, errorCode, null, defaultMessage);
  }

  public static void digits(Errors e, String field,
                            String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.digits(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

  public static void number(Errors e, String field, String errorCode) {
    number(e, field, errorCode, null, null);
  }

  public static void number(Errors e, String field, String errorCode, String defaultMessage) {
    number(e, field, errorCode, null, defaultMessage);
  }

  public static void number(Errors e, String field,
                            String errorCode, Object[] errorArgs, String defaultMessage) {
    if (!(ValidationMethods.number(e.getFieldValue(field)))) {
      e.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }
  }

}
