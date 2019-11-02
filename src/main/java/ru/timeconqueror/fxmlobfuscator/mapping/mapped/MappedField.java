package ru.timeconqueror.fxmlobfuscator.mapping.mapped;

public class MappedField {
    private String name;
    private String obfName;

    public MappedField(String name, String obfName) {
        this.name = name;
        this.obfName = obfName;
    }

    public String getName() {
        return name;
    }

    public String getObfName() {
        return obfName;
    }
}
