package ru.timeconqueror.fxmlobfuscator.mapping;

import java.io.File;

public interface IMappingsParser {
    Mappings parse(File obfuscationLogFile);
}
