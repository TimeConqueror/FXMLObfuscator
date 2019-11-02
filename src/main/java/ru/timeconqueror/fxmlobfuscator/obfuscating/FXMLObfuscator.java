package ru.timeconqueror.fxmlobfuscator.obfuscating;

import ru.timeconqueror.fxmlobfuscator.Dictionary;
import ru.timeconqueror.fxmlobfuscator.mapping.Mappings;
import ru.timeconqueror.fxmlobfuscator.mapping.mapped.*;
import ru.timeconqueror.fxmlobfuscator.util.ContentFile;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FXMLObfuscator extends Obfuscator {
    private static final Pattern FX_CONTROLLER_PATTERN = Pattern.compile("fx:controller=\"(.+?)\"");//fx:controller="..."
    private static final Pattern FX_FIELD_PATTERN = Pattern.compile("fx:id=\"(.+?)\"");//fx:id="..."
    private static final Pattern FX_METHOD_PATTERN;//fx:onAction(Drag...)="#..."

    static {
        StringBuilder builder = new StringBuilder("on(");
        Iterator<String> iterator = Dictionary.EVENT_ARGS_DICTIONARY.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            builder.append(s);
            if (iterator.hasNext()) {
                builder.append("|");
            }
        }
        builder.append(")=\"#(.+?)\"");
        FX_METHOD_PATTERN = Pattern.compile(builder.toString());
    }

    public FXMLObfuscator(Mappings mappings) {
        super(mappings);
    }

    @Override
    public void obfuscate(ContentFile fileIn) {
        obfFXML(fileIn);
    }

    private void obfFXML(ContentFile fxml) {
        Matcher m = FX_CONTROLLER_PATTERN.matcher(fxml.getContent());
        StringBuffer sb = new StringBuffer();

        MappedClass clazz;
        if (m.find()) {
            String clazzName = m.group(1);
            clazz = mappings.getClazz(clazzName);
            if (clazz != null) {
                m.appendReplacement(sb, m.group(0).replaceFirst(clazzName, clazz.getObfName()));
            } else {
                System.err.println("Can't find obfuscated name for class " + clazzName + " in " + fxml.getName());
                return;
            }
            m.appendTail(sb);
        } else {
            System.err.println("Can't find controller class in " + fxml.getName());
            return;
        }
        fxml.setContent(sb.toString());

        obfFXMLFields(clazz, fxml);
        obfFXMLMethods(clazz, fxml);
    }

    private static void obfFXMLFields(MappedClass fxmlController, ContentFile fxml) {
        Matcher m = FX_FIELD_PATTERN.matcher(fxml.getContent());
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String fieldName = m.group(1);
            MappedField field = fxmlController.getField(fieldName);
            if (field != null) {
                m.appendReplacement(sb, m.group(0).replaceFirst(fieldName, field.getObfName()));
            } else {
                System.err.println("Can't find field " + fieldName + " in " + fxmlController.getName() + " while trying to obfuscate fxml. This field wont'be obfuscated.");
            }
        }

        m.appendTail(sb);
        fxml.setContent(sb.toString());
    }

    private static void obfFXMLMethods(MappedClass fxmlController, ContentFile fxml) {
        Matcher m = FX_METHOD_PATTERN.matcher(fxml.getContent());
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String eventName = m.group(1);
            String methodName = m.group(2);
            List<MappedType> methodArgs = Stream.of(Dictionary.EVENT_ARGS_DICTIONARY.get(eventName)).map((Function<Class<?>, MappedType>) aClass -> new MappedType(MappedType.FieldType.REFERENCE, aClass.getName())).collect(Collectors.toList());
            MappedMethod method = fxmlController.getMethod(methodName, new MappedMethodDescriptor(new MappedType(MappedType.FieldType.VOID, null), methodArgs));
            if (method != null) {
                m.appendReplacement(sb, m.group(0).replaceFirst(methodName, method.getObfName()));
            } else {
                System.err.println("Can't find method " + methodName + " in " + fxmlController.getName() + " while it is been used in on" + eventName + " event");
            }
        }

        m.appendTail(sb);
        fxml.setContent(sb.toString());
    }
}
