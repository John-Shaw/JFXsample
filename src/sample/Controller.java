package sample;

import com.google.gson.Gson;
import javafx.event.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable{
    public TextField idTextField;
    public AnchorPane vedioPanel;
    public AnchorPane wordPanel;
    public TextArea wordViewer;
    public MediaView mediaView;

    public ImageView imageView;
    public Button searchBtn;
    public HBox choosenBtnHbox;
    public Label workNumberLabel;
    private String localPath;

    private Configure conf;
    private CarParts parts;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
        idTextField.requestFocus();

//        selectBox.setItems(options);

        Gson gson = new Gson();
        String configPath = localPath + "/configuration/Configure.json";
        conf = gson.fromJson(readDataFromJson(configPath), Configure.class);
        String partsPath = localPath + "/configuration/CarParts.json";
        parts = gson.fromJson(readDataFromJson(partsPath), CarParts.class);


        workNumberLabel.setText("工位号：" + conf.getWork_number());

        for (Part part:conf.getParts()){
            String btnTitle = part.getName_cn();
            idTextField.setText(btnTitle);
//            System.out.println(btnTitle);
            Button btn = new Button(btnTitle);
            btn.setId(part.getId());
            btn.setMinWidth(100);

            CarPart[] carParts = parts.getCarParts();
            for (CarPart carPart:carParts){

                if (part.getId().equals(carPart.getId())){


                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
//                            setDisplayView(carPart.getMediaName());
                            setDisplayView(carPart.getImageName());
                            try {
                                setTextView(carPart.getDocName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    btn.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode().equals(KeyCode.ENTER)) {
                                btn.fire();
                            }
                        }
                    });
                    choosenBtnHbox.getChildren().addAll(btn);

                    break;
                }
            }
        }


    }



    private void setDisplayView(String partName){
        File mediaFile = new File(localPath +"/temp/"+partName);
        if (mediaFile.exists() && partName.endsWith(".mp4")){
            Media media = new Media(mediaFile.toURI().toString());
            final MediaPlayer player = new MediaPlayer(media);
            mediaView.setMediaPlayer(player);
            player.play();
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    int w = player.getMedia().getWidth();
                    int h = player.getMedia().getHeight();

                    if (w > vedioPanel.getWidth())
                        w = (int) vedioPanel.getWidth();
                    if (h > vedioPanel.getHeight())
                        h = (int) vedioPanel.getHeight();

                    mediaView.setFitWidth(w);
                    mediaView.setFitHeight(h);

                }
            });
            return;
        }


        if (mediaFile.exists()){
            Image img = new Image(mediaFile.toURI().toString());
            double aspect = img.getWidth()/img.getHeight();
            imageView.setImage(img);
            imageView.fitWidthProperty().bind(vedioPanel.widthProperty());
            imageView.setFitHeight(vedioPanel.getWidth()/aspect);
            return;
        }

        Alert mesBox = new Alert(Alert.AlertType.ERROR);
        mesBox.setTitle("ERROR!");
        mesBox.setHeaderText("发生了一个错误。");
        mesBox.setContentText("未找到任何文件，请尝试更新数据库。");
        mesBox.showAndWait();

    }

    private void setTextView(String partName) throws IOException {

        File docFile = new File(localPath+"/temp/"+ partName);
        if (docFile.exists()){
            FileInputStream fis2003 = new FileInputStream(docFile);
            HWPFDocument doc2003 = new HWPFDocument(fis2003);
            WordExtractor word2003 = new WordExtractor(doc2003);

            String text = "";
            String[] fileData = word2003.getParagraphText();
            for (int i=0; i<fileData.length; i++){
                if (fileData[i] != null){
                    if(fileData[i].endsWith("\n")){
                        text += fileData[i];
                    }
                    else{
                        text += fileData[i] + "\n";}
                }
            }
            wordViewer.setText(text);
        } else {
            Alert mesBox = new Alert(Alert.AlertType.ERROR);
            mesBox.setTitle("ERROR!");
            mesBox.setHeaderText("发生了一个错误。");
            mesBox.setContentText("未找到任何文件，请尝试更新数据库。");
            mesBox.showAndWait();
        }

    }

    public void searchClick(ActionEvent actionEvent) throws IOException {

        if (this.idTextField.getText().isEmpty()){
            Alert mesBox = new Alert(Alert.AlertType.ERROR);
            mesBox.setTitle("ERROR!");
            mesBox.setHeaderText("发生了一个错误。");
            mesBox.setContentText("请输入ID。");
            mesBox.showAndWait();
        } else {
            Button btn = (Button)choosenBtnHbox.getChildren().get(4);
            btn.fire();
            btn.requestFocus();
        }

    }

        public String pathFix(String path){
            return path.replaceAll("\\\\", "/");
        }

        public void testDBConnect(ActionEvent actionEvent) {
//            System.out.println(parts.getCarParts()[1].getImageName());
//            db =new DBConnector();
//            db.testDB();
//            System.out.println(db.httpDownload("","temp/lizhu.doc"));
//            db.testDownloadFiel("32");
//            String path = localPath + "/temp/samples.json";
//            readDataFromJson(path);
//
//            System.out.println(carMes.getType()[10].getOption()[0].getLabel());

//            String path = localPath + "/temp/Configure.json";
//            readDataFromJson(path);
//            System.out.println(conf.getParts().length);
            Alert mesBox = new Alert(Alert.AlertType.INFORMATION);
            mesBox.setTitle("更新!");
            mesBox.setHeaderText(null);
            mesBox.setContentText("您的数据已为最新");
            mesBox.showAndWait();

        }



    public String readDataFromJson(String path) {
        //读取json文件，保存到String json中
        String fileName=path;
        File file=new File(fileName);
        StringBuffer sb = new StringBuffer() ;
        String line;
        BufferedReader br=null;

        try {
            //修复逗比windows下的奇葩乱码问题
            br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while((line=br.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json=sb.toString();

        return json;

    }
    public void onEnter(ActionEvent actionEvent) {
        if (this.idTextField.getText().isEmpty()) {
            Alert mesBox = new Alert(Alert.AlertType.ERROR);
            mesBox.setTitle("ERROR!");
            mesBox.setHeaderText("发生了一个错误。");
            mesBox.setContentText("未找到任何文件，请尝试更新数据库。");
            mesBox.showAndWait();
        } else {
            searchBtn.requestFocus();
        }
    }

    public void onSearchEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            searchBtn.fire();
            choosenBtnHbox.getChildren().get(4).requestFocus();
        }
    }

    public void onRightPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            searchBtn.requestFocus();
        }
    }
}
