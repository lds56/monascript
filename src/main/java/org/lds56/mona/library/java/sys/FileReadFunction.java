package org.lds56.mona.library.java.sys;

import org.lds56.mona.core.runtime.collections.MonaTuple;
import org.lds56.mona.core.runtime.functions.MonaFixedArgFunc;
import org.lds56.mona.core.runtime.types.MonaError;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: Rui Chen
 * @Date: 19 Jun 2022
 * @Description: This is description.
 */
public class FileReadFunction implements MonaFixedArgFunc {

    @Override
    public String getName() {
        return "fread";
    }

    @Override
    public String getPackage() {
        return "sys";
    }

    @Override
    public MonaObject call(MonaObject fileName) {
        String fname = fileName.stringValue();
        try {
            Path path = Paths.get(fname);
            String content = Files.readAllLines(path).get(0);
            return MonaTuple.newPair(MonaString.valueOf(content), MonaNull.NIL);
        } catch (IOException e) {
            return MonaTuple.newPair(MonaNull.NIL, MonaError.err(1001, "File IO Error", e));
        }
    }
}
