package com.smile.groovy

import proguard.obfuscate.MappingProcessor

class SelfMappingProcess implements MappingProcessor {

    PrintWriter printWriter

    SelfMappingProcess(File targetFile) {
        printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(targetFile, true))), false)
        printWriter.println()
        printWriter.println()
        printWriter.println('# this is mapping keeper print begin --')
        printWriter.println()
    }

    @Override
    boolean processClassMapping(String className, String newClassName) {
        printWriter.println("-keepnames class ${newClassName} {*;}")
        return false
    }

    @Override
    void processFieldMapping(String className, String fieldType, String fieldName, String newClassName, String newFieldName) {
    }

    @Override
    void processMethodMapping(String className, int firstLineNumber, int lastLineNumber, String methodReturnType, String methodName, String methodArguments, String newClassName, int newFirstLineNumber, int newLastLineNumber, String newMethodName) {

    }

    void finish() {
        printWriter.println('# this is mapping keeper print end --')
        printWriter.println()
        printWriter.flush()
        printWriter.close()
    }
}