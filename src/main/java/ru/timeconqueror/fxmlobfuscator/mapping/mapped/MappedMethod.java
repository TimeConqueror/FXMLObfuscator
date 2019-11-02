package ru.timeconqueror.fxmlobfuscator.mapping.mapped;

public class MappedMethod {
    private String name;
    private String obfName;
    private MappedMethodDescriptor descriptor;

    public MappedMethod(String name, String obfName, MappedMethodDescriptor descriptor) {
        this.name = name;
        this.obfName = obfName;
        this.descriptor = descriptor;
    }

    public String getName() {
        return name;
    }

    public String getObfName() {
        return obfName;
    }

    public MappedMethodDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (obj instanceof MappedMethod) {
            MappedMethod methodIn = ((MappedMethod) obj);
            return name.equals(methodIn.getName()) &&
                    obfName.equals(methodIn.getObfName()) &&
                    descriptor.equals(methodIn.descriptor);
        }

        return false;
    }
}
