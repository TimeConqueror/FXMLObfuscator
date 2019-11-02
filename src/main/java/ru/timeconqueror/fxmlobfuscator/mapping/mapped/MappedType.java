package ru.timeconqueror.fxmlobfuscator.mapping.mapped;

import org.jetbrains.annotations.Nullable;

public class MappedType {
    private FieldType type;
    private String name;
    private int arrayDimension;

    public MappedType(FieldType type, String name) {
        this(type, name, 0);
    }

    public MappedType(FieldType type, @Nullable String name, int arrayDimension) {
        this.type = type;
        this.name = name;
        this.arrayDimension = arrayDimension;
    }

    public static MappedType parseProGuardType(String proGuardType) {
        String type = proGuardType.replaceAll("(?:\\[])+$", "");
        int arrayDim = (proGuardType.length() - type.length()) /2 ;
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType != FieldType.REFERENCE && type.equals(fieldType.literalName)) {
                return new MappedType(fieldType, null, arrayDim);
            }
        }

        return new MappedType(FieldType.REFERENCE, type, arrayDim);
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public int getArrayDimension() {
        return arrayDimension;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;

        if(obj instanceof MappedType){
            MappedType typeIn = ((MappedType) obj);
            return typeIn.arrayDimension == arrayDimension &&
                    (typeIn.name == null || typeIn.name.equals(name)) &&
                    typeIn.type == type;
        }

        return false;
    }

    public enum FieldType {
        BYTE('B', "byte"),
        CHAR('C', "char"),
        DOUBLE('D', "double"),
        FLOAT('F', "float"),
        INT('I', "int"),
        LONG('J', "long"),
        REFERENCE('L', "reference"),
        SHORT('S', "short"),
        BOOLEAN('Z', "boolean"),
        VOID('V', "void");

        private char baseType;
        private String literalName;

        FieldType(char baseType, String literalName) {
            this.baseType = baseType;
            this.literalName = literalName;
        }
    }

}