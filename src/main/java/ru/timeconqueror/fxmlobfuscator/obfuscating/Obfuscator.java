package ru.timeconqueror.fxmlobfuscator.obfuscating;

import ru.timeconqueror.fxmlobfuscator.mapping.Mappings;
import ru.timeconqueror.fxmlobfuscator.util.ContentFile;

public abstract class Obfuscator {
    protected Mappings mappings;

    public Obfuscator(Mappings mappings) {
        this.mappings = mappings;
    }

    protected Mappings getMappings() {
        return mappings;
    }

    /**
     * Obfuscates file.
     */
    public abstract void obfuscate(ContentFile file);
}
