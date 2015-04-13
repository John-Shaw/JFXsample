package sample;

/**
 * Created by John on 15/4/13.
 */
public class CarParts{
    private CarPart[] carParts;

    public CarPart[] getCarParts() {
        return carParts;
    }
}

class CarPart {
    private String part;
    private String name_cn;
    private String imageName;
    private String mediaName;
    private String docName;
    private String id;

    public String getPart() {
        return part;
    }

    public String getName_cn() {
        return name_cn;
    }

    public String getImageName() {
        return imageName;
    }

    public String getMediaName() {
        return mediaName;
    }

    public String getDocName() {
        return docName;
    }

    public String getId() {
        return id;
    }
}
