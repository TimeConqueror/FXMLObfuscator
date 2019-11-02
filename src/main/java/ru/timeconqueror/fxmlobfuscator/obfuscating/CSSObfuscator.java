package ru.timeconqueror.fxmlobfuscator.obfuscating;

import ru.timeconqueror.fxmlobfuscator.mapping.Mappings;
import ru.timeconqueror.fxmlobfuscator.mapping.mapped.MappedClass;
import ru.timeconqueror.fxmlobfuscator.mapping.mapped.MappedField;
import ru.timeconqueror.fxmlobfuscator.util.ContentFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSSObfuscator extends Obfuscator {
    private static final Pattern CSS_FIELD_PATTERN = Pattern.compile("#-?([_a-zA-Z]+[_a-zA-Z0-9-]*)(?=[^}]*\\{)");//#...
    private static final Pattern CSS_COMMENT_PATTERN = Pattern.compile("/\\*(.+?)\\*/");//#...

    public CSSObfuscator(Mappings mappings) {
        super(mappings);
    }

    @Override
    public void obfuscate(ContentFile css) {
        Matcher m = CSS_COMMENT_PATTERN.matcher(css.getContent());

        MappedClass boundClass;
        StringBuffer sb = new StringBuffer();
        if (m.find()) {
            String clazzName = m.group(1);
            boundClass = mappings.getClazz(clazzName);
            if (boundClass != null) {
                m.appendReplacement(sb, "");
            } else {
                System.err.println("Can't find obfuscated name for class " + clazzName + " in " + css.getName());
                return;
            }
            m.appendTail(sb);
        } else {
            System.err.println("Can't find controller class in " + css.getName() +"\n" +
                    "Check if you add path to controller class in comment as here:\n" +
                    "/*com.test.program.path.Gui*/");
            return;
        }
        css.setContent(sb.toString());
        obfCSSFields(boundClass, css);
    }

    private void obfCSSFields(MappedClass controller, ContentFile css){
        Matcher m = CSS_FIELD_PATTERN.matcher(css.getContent());

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String fieldName = m.group(1);
            MappedField field = controller.getField(fieldName);
            if (field != null) {
                m.appendReplacement(sb, m.group(0).replaceFirst(fieldName, field.getObfName()));
            } else {
                System.err.println("Can't find field " + fieldName + " in " + controller.getName() + " while trying to obfuscate css. This field won't be obfuscated.");
            }
        }
        m.appendTail(sb);

        css.setContent(sb.toString());
    }
}
