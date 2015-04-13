package sample;

/**
 * Created by 建勇 on 2015/4/13.
 */
public class Configure {
    private String work_number;
    private Part[] parts;

    public String getWork_number() {
        return work_number;
    }

    public Part[] getParts() {
        return parts;
    }
}

class Part{
    private String part;
    private String name_cn;
    private String id;

    public String getId() {
        return id;
    }

    public String getName_cn() {
        return name_cn;
    }



    public String getPart() {
        return part;
    }
}