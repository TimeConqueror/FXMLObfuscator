package ru.timeconqueror.fxmlobfuscator.mapping;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import ru.timeconqueror.fxmlobfuscator.mapping.mapped.MappedClass;
import ru.timeconqueror.fxmlobfuscator.mapping.mapped.MappedMethodDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProGuardMappingsParser implements IMappingsParser {
    private Pattern CLASS_PATTERN = Pattern.compile("^(.+?) -> (.+?):$");
    private Pattern FIELD_PATTERN = Pattern.compile("^ +([^:]+?) ([^ ]+?) -> ([A-Za-z0-9_$]+)$");
    private Pattern METHOD_PATTERN = Pattern.compile("^ *(?:\\d+:\\d+:)?(.+?) (.+?)\\((.+?)?\\) -> ([a-zA-Z0-9_]+|<init>|<clinit>|lambda\\$[A-Za-z0-9_]+\\$\\d+|access\\$\\d+)$");
    // \((.+)\)(\[*(?:B|C|D|F|I|J|L[^;]+;|S|Z))


    @Override
    public Mappings parse(File obfuscationLogFile) {
        Mappings mappings = new Mappings();
        AtomicReference<MappedClass> currentClass = new AtomicReference<>();
        try(LineIterator iter = FileUtils.lineIterator(obfuscationLogFile)) {
            while (iter.hasNext()) {
                String line = iter.next();

                if (!line.trim().startsWith("#") && !tryParseAsClass(mappings, currentClass, line) && !tryParseAsMethod(currentClass.get(), line) && !tryParseAsField(currentClass.get(), line)) {
                    System.err.println("ERROR:");
                    System.err.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return mappings;
    }

    private boolean  tryParseAsClass(Mappings mappings, AtomicReference<MappedClass> currentClass, String line) {
        Matcher matcher = CLASS_PATTERN.matcher(line);

        if(matcher.matches()){
            String name = matcher.group(1);
            String obfName = matcher.group(2);
//            System.out.println(String.format("  CLASS || %s ->> %s", name, obfName));
            MappedClass clazz = new MappedClass(name, obfName);
            mappings.addClass(clazz);
            currentClass.set(clazz);
            return true;
        } return false;

    }

    private boolean tryParseAsMethod(MappedClass currentClass, String line) {
        Matcher matcher = METHOD_PATTERN.matcher(line);

        if(matcher.matches()){
            String returnType = matcher.group(1);
            String name = matcher.group(2);
            String descriptor = matcher.group(3);
            String obfName = matcher.group(4);
//            System.out.println(String.format("\t METHOD || %s %s(%s) ->> %s", returnType, name, descriptor, obfName));

            currentClass.appendMethod(name, obfName, MappedMethodDescriptor.parseProGuardDescriptor(returnType, descriptor));
            return true;
        } return false;
    }

    private boolean tryParseAsField(MappedClass currentClass, String line){
        Matcher matcher = FIELD_PATTERN.matcher(line);

        if(matcher.matches()){
            String type = matcher.group(1);
            String name = matcher.group(2);
            String obfName = matcher.group(3);
//            System.out.println(String.format("\t  FIELD || %s %s ->> %s", type, name, obfName));

            currentClass.appendField(name, obfName);
            return true;
        } return false;
    }
}
