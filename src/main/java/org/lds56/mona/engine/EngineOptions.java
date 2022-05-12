package org.lds56.mona.engine;

import org.lds56.mona.core.exception.OptionLoadErrorException;

import java.util.HashMap;

public class EngineOptions extends HashMap<EngineOption, Object> {

    public static final EngineOptions EMPTY = new EngineOptions();

    public EngineOptions() {
        super();
    }

    public static EngineOptions setUp() {
        return new EngineOptions();
    }

    public EngineOptions setOption(EngineOption opt, Object val) {
        this.put(opt, val);
        return this;
    }

    public short getLevel(EngineOption opt) throws OptionLoadErrorException {
        if (opt.getType() != EngineOption.Type.LEVEL) {
            throw new OptionLoadErrorException("Level option required `short` value");
        }
        return (short)this.getOrDefault(opt, opt.getDefault());
    }

    public int getSize(EngineOption opt) throws OptionLoadErrorException {
        if (opt.getType() != EngineOption.Type.SIZE) {
            throw new OptionLoadErrorException("Size option required `int` value");
        }
        return (int)this.getOrDefault(opt, opt.getDefault());
    }

    public boolean getEnabled(EngineOption opt) throws OptionLoadErrorException {
        if (opt.getType() != EngineOption.Type.SWITCH) {
            throw new OptionLoadErrorException("Switch option required `boolean` value");
        }
        return (boolean)this.getOrDefault(opt, opt.getDefault());
    }

    public EngineOption.Choice getChoice(EngineOption opt) throws OptionLoadErrorException {
        if (opt.getType() != EngineOption.Type.CHOICE) {
            throw new OptionLoadErrorException("Choice option required `choice` value");
        }
        return (EngineOption.Choice)this.getOrDefault(opt, opt.getDefault());
    }

}
