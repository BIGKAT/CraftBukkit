package net.minecraft.src;

public class ExceptionInvalidNumber extends CommandException {

    public ExceptionInvalidNumber() {
        this("commands.generic.num.invalid", new Object[0]);
    }

    public ExceptionInvalidNumber(String s, Object... aobject) {
        super(s, aobject);
    }
}
