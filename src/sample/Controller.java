package sample;


//import com.google.gson.Gson;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.event.Event;
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
//    public ComboBox selectBox;
    public ImageView imageView;
    public Button searchBtn;
    public HBox choosenBtnHbox;
    public Label workNumberLabel;
    private String localPath;
//    private DBConnector db;
//    private CarMes carMes;
    private Configure conf;

//    private ObservableList<String> options =
//            FXCollections.observableArrayList(
//                    "蓄电池",
//                    "废液",
//                    "车门",
//                    "舱盖",
//                    "车轮",
//                    "挡风玻璃",
//                    "车顶",
//                    "立柱",
//                    "方向盘",
//                    "座椅",
//                    "仪表盘",
//                    "发动机",
//                    "变速器",
//                    "悬挂",
//                    "传动轴",
//                    "油箱"
//            );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
        idTextField.requestFocus();

//        selectBox.setItems(options);

        Gson gson = new Gson();
        String path = localPath + "/configuration/Configure.json";
        conf=gson.fromJson(readDataFromJson(path), Configure.class);

        workNumberLabel.setText("工位号："+conf.getId());
        for (Part part:conf.getParts()){
            Button btn = new Button(part.getName_cn());
            btn.setMinWidth(100);
            choosenBtnHbox.getChildren().addAll(btn);
        }

    }


    private void setDisplayView(String partName){
        File mediaFile = new File(localPath +"/temp/"+partName+".mp4");
        if (mediaFile.exists()){
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

        File imageFile = new File(localPath +"/temp/"+partName+".jpg");
        if (imageFile.exists()){
            Image img = new Image(imageFile.toURI().toString());
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

        File docFile = new File(localPath+"/temp/"+ partName +".doc");
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

//        FileInputStream fis2003;
//        HWPFDocument doc2003;
//        WordExtractor word2003;

        if (this.idTextField.getText().isEmpty()){
            Alert mesBox = new Alert(Alert.AlertType.ERROR);
            mesBox.setTitle("ERROR!");
            mesBox.setHeaderText("发生了一个错误。");
            mesBox.setContentText("未找到任何文件，请尝试更新数据库。");
            mesBox.showAndWait();
        } else {

            setDisplayView("xudianchi");

            setTextView("xudianchi");

            //todo bug问题
            //由于poi库太老，现在的问题是读取word中表格会出错，然后我使用另一种读取段落的方法，读取后的表格数据不换行
            //手动加了“\n”后，正常文本每段会多一个换行
            //已解决
//            try {
//

//
//
//                fis2003=new FileInputStream(new File(localPath+"/temp/xudianchi.doc"));
//                doc2003 = new HWPFDocument(fis2003);
//                word2003 = new WordExtractor(doc2003);
//
////                String text2003 = word2003.getText();
//                String text = "";
//                String[] fileData = word2003.getParagraphText();
//                for (int i=0; i<fileData.length; i++){
//                    if (fileData[i] != null){
//                        if(fileData[i].endsWith("\n")){
//                            text += fileData[i];
//                        }
//                        else{
//                        text += fileData[i] + "\n";}
//                    }
//                }
//                wordViewer.setText(text);

//                    String[] s=new String[20];
//                    FileInputStream in=new FileInputStream(new File(localPath+"/temp/test.doc"));
//                    POIFSFileSystem pfs=new POIFSFileSystem(in);
//                    HWPFDocument hwpf=new HWPFDocument(pfs);
//                    Range range =hwpf.getRange();
//
//                    TableIterator it=new TableIterator(range);
//
//                    int index=0;
//                    while(it.hasNext()){
//                        Table tb=(Table)it.next();
//                        for(int i=0;i<tb.numRows();i++){
//                            //System.out.println("Numrows :"+tb.numRows());
//                            TableRow tr=tb.getRow(i);
//                            for(int j=0;j<tr.numCells();j++){
//                                //System.out.println("numCells :"+tr.numCells());
////                      System.out.println("j   :"+j);
//                                TableCell td=tr.getCell(j);
//                                for(int k=0;k<td.numParagraphs();k++){
//                                    //System.out.println("numParagraphs :"+td.numParagraphs());
//                                    Paragraph para=td.getParagraph(k);
//                                    s[index]=para.text().trim();
//                                    index++;
//                                }
//                            }
//                        }
//                    }
//                    String s1="";
//                    for(int i=0;i<s.length;i++){
//                       s1= s1+s[i]+"\n";
//                    }
//                    wordViewer.setText(s1);
//
//            } catch (Exception e) {
//               e.printStackTrace();
//            }
//
        }

        choosenBtnHbox.getChildren().get(4).requestFocus();
    }



        public String pathFix(String path){
            return path.replaceAll("\\\\", "/");
        }

        public void testDBConnect(ActionEvent actionEvent) {

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


        }


//
//    public void selectItem(ActionEvent actionEvent) {
//
//    }


    public String readDataFromJson(String path){
        //读取json文件，保存到String json中
        String fileName=path;
        File file=new File(fileName);
        StringBuffer sb = new StringBuffer() ;
        String line;
        BufferedReader br=null;
        try {
            br=new BufferedReader(new FileReader(file));
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
}
