package ru.timeconqueror.fxmlobfuscator.mapping;

import ru.timeconqueror.fxmlobfuscator.mapping.mapped.MappedClass;

import java.util.ArrayList;
import java.util.HashMap;

public class Mappings {

    // Obf class name -> MappedClass
    private HashMap<String, MappedClass> classes = new HashMap<>();
    // Class name -> Obfuscated Class Name
    private HashMap<String, String> reverseClasses = new HashMap<>();
    // Obfuscated class name -> obfuscated class names that are parents to first class
    private HashMap<String, ArrayList<String>> classParents = new HashMap<>();

    void addClass(MappedClass clazz){
        classes.put(clazz.getObfName(), clazz);
        reverseClasses.put(clazz.getName(), clazz.getObfName());
    }

    public MappedClass getClazz(String name){
        String obfName = reverseClasses.get(name);
        return obfName == null ? null : classes.get(obfName);
    }
}
