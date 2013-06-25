package com.aulas.business.questoes.javatester.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestManager {
    Class<?> theClass;
    Class<?> candidateClass;

    public TestManager(Class<?> theClass) {
        this.theClass = theClass;
    }

    public TestManager(Class<?> theClass, Class<?> candidateClass) {
        this.theClass = theClass;
        this.candidateClass = candidateClass;
    }

    public void testClassDeclaration() throws TestException {
        ClassTest annotation = theClass.getAnnotation(ClassTest.class);
        
        /* Configuring variables */
        String[] modifiers = (annotation.modifiers().length == 0)?
                (Modifier.getModifiers(theClass.getModifiers())):(annotation.modifiers());

        if (annotation != null) {
            for (String correctModifier : modifiers) {
                if (!Modifier.containsModifier(
                        candidateClass.getModifiers(), correctModifier)) {
                    throw new TestException("Falha na Execução. "
                            + "A classe \"" + candidateClass.getName()
                            + "\" não possui o modificador \""
                            + correctModifier + "\" conforme sugerido na questão.");
                }
            }
        }
    }
    
    private String getMethodString(String[] modifiers, Class<?> returnType, 
            String name, Class<?> ... params) {
        
        StringBuffer mStr = new StringBuffer();
        
        for (String str : modifiers) {
            mStr.append(str + " ");
        }
        
        mStr.append(returnType + " " + name + "(");
        
        if (params.length > 0) {
            mStr.append(params[0]);
            for (int i = 1; i < params.length; i++) {
                mStr.append(","+params[i]);
            }
        }
        
        mStr.append(")");
        
        return mStr.toString();
    }
    
    private void testGetterFieldDeclaration(Field field) throws TestException {
        String fieldName = field.getName();
        String methodName = "get" 
                + Character.toUpperCase(fieldName.charAt(0));
        
        if (fieldName.length() > 1) {
                methodName = methodName + fieldName.subSequence(1, fieldName.length()-1);
        }
        
        String[] modifiers = {"public"};
        Class<?> returnType = (Class<?>)field.getType();
        
        Method method = null;
        try {
            method = candidateClass.getDeclaredMethod(methodName);
        } catch (Exception e) {
            throw new TestException("Falha na Execução. " 
                    + "Esperava-se o getter \"" 
                    + getMethodString(modifiers,returnType, methodName) 
                    + "\" para o atributo \"" + field.getName() 
                    + "\" na classe \"" + candidateClass.getName() + "\".");
        }        
        
        /* Verifying return type */
        testMethodReturnType(method, returnType);
        
        /* Verifying modifiers */
        testMethodModifiers(method, modifiers); 
    }

    private void testSetterFieldDeclaration(Field field) throws TestException {
        String fieldName = field.getName();
        String methodName = "set" 
                + Character.toUpperCase(fieldName.charAt(0));
        
        if (fieldName.length() > 1) {
                methodName = methodName + fieldName.subSequence(1, fieldName.length()-1);
        }
        
        String[] modifiers = {"public"};
        Class<?> returnType = void.class;
        Class<?>[] params = {(Class<?>)field.getType()};
        
        Method method = null;
        try {
            method = candidateClass.getDeclaredMethod(methodName, params);
        } catch (Exception e) {
            throw new TestException("Falha na Execução. " 
                    + "Esperava-se o setter \"" 
                    + getMethodString(modifiers,returnType, methodName, params) 
                    + "\" para o atributo \"" + field.getName() 
                    + "\" na classe \"" + candidateClass.getName() + "\".");
        }        
        
        /* Verifying return type */
        testMethodReturnType(method, returnType);
        
        /* Verifying modifiers */
        testMethodModifiers(method, modifiers); 
    }
    
    private void testFieldDeclaration(Field expectedField) throws TestException {
        FieldTest annotation = expectedField.getAnnotation(FieldTest.class);
        
        if (annotation != null) {
            /* Configuring variables */
            String name = (annotation.name().equals(""))?
                    (expectedField.getName()):(annotation.name());
            Class<?> type = 
                    (annotation.type().equals(FieldTest.class))?
                        ((Class<?>)expectedField.getGenericType()):(annotation.type());
            String[] modifiers = (annotation.modifiers().length == 0)?
                    (Modifier.getModifiers(expectedField.getModifiers())):(annotation.modifiers());
            
            /* Verifying field existence */
            Field field;
            try {
                field = candidateClass.getDeclaredField(name);
            } catch (Exception e) {
                throw new TestException("Falha na Execução. "
                        + "Não foi encontrado o atributo \"" + name
                        + "\" na classe \"" + candidateClass.getName()
                        + "\" conforme sugerido na questão.");
            }
            
            /* Verifying field type */
            if (!field.getGenericType().equals(type)) {
                throw new TestException("Falha na Execução. "
                        + "O atributo \"" + field.getName()
                        + "\" da classe \"" + candidateClass.getName() 
                        + "\" é do tipo \"" + field.getGenericType()
                        + "\", mas a questão sugere ser do tipo \"" + type + "\".");
            }
            
            /* Verifying field modifiers */
            for (String correctModifier : modifiers) {
                if (!Modifier.containsModifier(
                        field.getModifiers(), correctModifier)) {
                    throw new TestException("Falha na Execução. "
                            + "O atributo \"" + field.getName()
                            + "\" da classe \"" + candidateClass.getName() 
                            + "\" não possui o modificador \""
                            + correctModifier + "\" conforme sugerido na questão.");
                }
            }
            
            /* Verifying getter existence */
            if (annotation.withGetter()) {
                testGetterFieldDeclaration(field);
            }
            
            /* Verifying setter existence */
            if (annotation.withSetter()) {
                testSetterFieldDeclaration(field);
            }
        }
    }

    public void testFieldsDeclaration() throws TestException {
        for (Field field : theClass.getDeclaredFields()) {
            testFieldDeclaration(field);
        }
    }

    private void testMethodReturnType(Method method, Class<?> returnType) throws TestException {
        if (!method.getGenericReturnType().equals(returnType)) {
            throw new TestException("Falha na Execução. "
                    + "O método \"" + method.getName()
                    + "\" da classe \"" + candidateClass.getName() 
                    + "\" tem retorno do tipo \"" + method.getGenericReturnType()
                    + "\", mas a questão sugere ser do tipo \"" 
                    + returnType + "\".");
        }
    }
    
    private void testMethodModifiers(Method method, String[] modifiers) throws TestException {
        for (String correctModifier : modifiers) {
            if (!Modifier.containsModifier(
                    method.getModifiers(), correctModifier)) {
                throw new TestException("Falha na Execução. "
                        + "O método \"" + method.getName()
                        + "\" da classe \"" + candidateClass.getName() 
                        + "\" não possui o modificador \""
                        + correctModifier + "\" conforme sugerido na questão.");
            }
        }   
    }
    
    private void testMethodDeclaration(Method expectedMethod) throws TestException {
        MethodTest annotation = expectedMethod.getAnnotation(MethodTest.class);
        
        if (annotation != null) {
            /* Configuring variables */
            String name = (annotation.name().equals(""))?
                    (expectedMethod.getName()):(annotation.name());
            Class<?> returnType = 
                    (annotation.returnType().equals(MethodTest.class))?
                        (expectedMethod.getReturnType()):(annotation.returnType());
            Class<?>[] parameters = (annotation.parameters().length == 0)?
                    (expectedMethod.getParameterTypes()):(annotation.parameters());
            String[] modifiers = (annotation.modifiers().length == 0)?
                    (Modifier.getModifiers(expectedMethod.getModifiers())):(annotation.modifiers());
            
            /* Verifying method existence (polymorphism allowed) */
            Method method;
            try {
                method = candidateClass.getDeclaredMethod(name, parameters);
            } catch (Exception e) {
                throw new TestException("Falha na Execução. "
                        + "Não foi encontrado o método \""
                        + getMethodString(modifiers, returnType, name, parameters)
                        + "\" na classe \"" + candidateClass.getName()
                        + "\" conforme sugerido na questão.");
            }

            /* Verifying return type */
            testMethodReturnType(method, returnType);
            
            /* Verifying modifiers */
            testMethodModifiers(method, modifiers);         
        }
    }
    
    public void testMethodsDeclaration() throws TestException {
        for (Method method : theClass.getDeclaredMethods()) {
            testMethodDeclaration(method);
        }
    }

    public static void main(String[] args) {
        TestManager t = new TestManager(Example.class, ExampleTest.class);

        try {
            t.testClassDeclaration();
            t.testFieldsDeclaration();
            t.testMethodsDeclaration();
        } catch (TestException e) {
            e.printStackTrace();
        }
        
        t = new TestManager(Example.class, Example.class);

        try {
            t.testClassDeclaration();
            t.testFieldsDeclaration();
            t.testMethodsDeclaration();
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

}
