package sample;


/**
 * Created by John on 15/4/11.
 */
public class CarMes {
    private String id;
    private Type[] type;

    public String getId() {
        return id;
    }

    public Type[] getType() {
        return type;
    }
}

class Type{
    private String name;
    private Option[] option;

    public Option[] getOption() {
        return option;
    }

    public String getName() {
        return name;

    }


}

class Option{
    private String label;
    private String option1;
    private String option2;

    public String getLabel() {
        return label;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }
}
