package ru.timeconqueror.fxmlobfuscator.mapping.mapped;

import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MappedClass {
    private String name;
    private String obfName;

    // Field name -> MappedField
    private HashMap<String, MappedField> fields = new HashMap<>();
    // Pair(obfuscated method name, obfuscated method descriptor) - > MappedMethod
    private ArrayList<MappedMethod> methods = new ArrayList<>();

    public MappedClass(String name, String obfName) {
        this.name = name;
        this.obfName = obfName;
    }

    public void appendField(String name, String obfName) {
        fields.put(name, new MappedField(name, obfName));
    }

    public void appendMethod(String name, String obfName, MappedMethodDescriptor descriptor) {
        methods.add(new MappedMethod(name, obfName, descriptor));
    }

    public String getObfName() {
        return obfName;
    }

    public String getName() {
        return name;
    }

    public MappedField getField(String name){
        return fields.get(name);
    }

    public MappedMethod getMethod(String nameIn, MappedMethodDescriptor descriptor){
        MappedMethod methodIn = new MappedMethod(nameIn, null, descriptor);
        for (MappedMethod method : methods) {
            if(method.getName().equals(methodIn.getName())){
                MappedMethodDescriptor descIn = methodIn.getDescriptor();
                if(method.getDescriptor().equals(descIn)){
                    return method;
                }
            }
        }

        return null;
    }
}
