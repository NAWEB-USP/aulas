package com.aulas.business.questoes.javatester.util;

import java.util.ArrayList;

public class Modifier extends java.lang.reflect.Modifier {
    
    public static final int values[] = {
        ABSTRACT, INTERFACE, NATIVE, PRIVATE, PROTECTED, PUBLIC, 
        STATIC, FINAL, STRICT, SYNCHRONIZED, TRANSIENT, VOLATILE};
    
	public static String getName(int modifier) {
		switch (modifier) {
		case ABSTRACT:		return "abstract";
		case FINAL:			return "final";
		case INTERFACE:		return "interface";
		case NATIVE:		return "native";
		case PRIVATE:		return "private";
		case PROTECTED:		return "protected";
		case PUBLIC:		return "public";
		case STATIC:		return "static";
		case STRICT:		return "strict";
		case SYNCHRONIZED:	return "synchronized";
		case TRANSIENT:		return "transient";
		case VOLATILE:		return "volatile";
		default: 			return "unknown";
		}
	}
	
	public static boolean containsModifier(int modifiers, int modifier) {
		switch (modifier) {
		case ABSTRACT:		return isAbstract(modifiers);
		case FINAL:			return isFinal(modifiers);
		case INTERFACE:		return isInterface(modifiers);
		case NATIVE:		return isNative(modifiers);
		case PRIVATE:		return isPrivate(modifiers);
		case PROTECTED:		return isProtected(modifiers);
		case PUBLIC:		return isPublic(modifiers);
		case STATIC:		return isStatic(modifiers);
		case STRICT:		return isStrict(modifiers);
		case SYNCHRONIZED:	return isSynchronized(modifiers);
		case TRANSIENT:		return isTransient(modifiers);
		case VOLATILE:		return isVolatile(modifiers);
		default: 			return false;
		}
	}
	
	public static boolean containsModifier(int modifiers, String modifierName) {
	    return containsModifier(modifiers, toValue(modifierName));
	}
	
	public static int toValue(String modifierName) {
		if (modifierName.equals("abstract")) {
			return ABSTRACT;
		} else if (modifierName.equals("final")) {
			return FINAL;
		} else if (modifierName.equals("interface")) {
			return INTERFACE;
		} else if (modifierName.equals("native")) {
			return NATIVE;
		} else if (modifierName.equals("private")) {
			return PRIVATE;
		} else if (modifierName.equals("protected")) {
			return PROTECTED;
		} else if (modifierName.equals("public")) {
			return PUBLIC;
		} else if (modifierName.equals("static")) {
			return STATIC;
		} else if (modifierName.equals("strict")) {
			return STRICT;
		} else if (modifierName.equals("synchronized")) {
			return SYNCHRONIZED;
		} else if (modifierName.equals("transient")) {
			return TRANSIENT;
		} else if (modifierName.equals("volatile")) {
			return VOLATILE;
		}
		
		return 0;
	}
	
	public static String[] getModifiers(int modifiers) {
	    ArrayList<String> modifiersList = new ArrayList<String>();
	    
	    for (int modifier : values) {
	        if (containsModifier(modifiers, modifier)) {
	            modifiersList.add(getName(modifier));
	        }
	    }
	    
	    return (String[]) modifiersList.toArray(new String[modifiersList.size()]);
	}
}

