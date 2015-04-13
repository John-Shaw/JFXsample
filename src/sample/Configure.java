package sample;

/**
 * Created by 建勇 on 2015/4/13.
 */
public class Configure {
    private String id;
    private Part[] parts;

    public String getId() {
        return id;
    }

    public Part[] getParts() {
        return parts;
    }
}

class Part{
    private String part;

    public String getPart() {
        return part;
    }
}