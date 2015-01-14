package com.luxoft.bankapp.unitTestHelper.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSets {

	String setUpDataSet() default "/DBUnit/empty.xml";

	String assertDataSet() default "";
}