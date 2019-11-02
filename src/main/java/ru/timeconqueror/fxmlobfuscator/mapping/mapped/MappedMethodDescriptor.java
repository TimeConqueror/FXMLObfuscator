package ru.timeconqueror.fxmlobfuscator.mapping.mapped;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MappedMethodDescriptor {
    private MappedType returnType;
    private List<MappedType>argumentTypes;

    public MappedMethodDescriptor(MappedType returnType, List<MappedType> argumentTypes) {
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
    }

    public static MappedMethodDescriptor parseProGuardDescriptor(String returnType, @Nullable String descriptor) {
        List<MappedType> argumentTypes = Collections.emptyList();
        if(descriptor != null){
            String[] strs = descriptor.split(",");
            if(strs.length != 0){
                argumentTypes = Arrays.stream(strs).map(MappedType::parseProGuardType).collect(Collectors.toList());
            }
        }

        return new MappedMethodDescriptor(MappedType.parseProGuardType(returnType), argumentTypes);
    }

    public List<MappedType> getArgumentTypes() {
        return argumentTypes;
    }

    public MappedType getReturnType() {
        return returnType;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;

        if(obj instanceof MappedMethodDescriptor){
            MappedMethodDescriptor descIn = ((MappedMethodDescriptor) obj);
           if(returnType.equals(descIn.getReturnType())){
               for (int i = 0; i < argumentTypes.size(); i++) {
                   if(argumentTypes.get(i).equals(descIn.getArgumentTypes().get(i))){
                       return true;
                   }
               }
           }
        }

        return false;
    }
}
