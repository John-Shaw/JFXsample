package sample;


import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable{
    public TextField idTextField;
    public AnchorPane vedioPanel;
    public AnchorPane wordPanel;
    public TextArea wordViewer;
    public MediaView mediaView;
    public ComboBox selectBox;
    private String localPath;
    private DBConnector db;

    private ObservableList<String> options =
            FXCollections.observableArrayList(
                    "蓄电池",
                    "废液",
                    "车门",
                    "舱盖",
                    "车轮",
                    "挡风玻璃",
                    "车顶",
                    "立柱",
                    "方向盘",
                    "座椅",
                    "仪表盘",
                    "发动机",
                    "变速器",
                    "悬挂",
                    "传动轴",
                    "油箱"
            );




    public void searchClick(ActionEvent actionEvent) throws IOException, Exception {

        FileInputStream fis2003;
        HWPFDocument doc2003;
        WordExtractor word2003;



        if (this.idTextField.getText().isEmpty()){
            Alert mesBox = new Alert(Alert.AlertType.ERROR);
            mesBox.setTitle("ERROR!");
            mesBox.setContentText("please input ID number");
            mesBox.showAndWait();
        } else {
            //todo

            System.out.println("file://" + localPath + "/temp/videoviewdemo.mp4");
            Media media = new Media("file://"+ localPath +"/temp/videoviewdemo.mp4");

            final MediaPlayer player = new MediaPlayer(media);
            mediaView.setMediaPlayer(player);
            player.play();
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    int w = player.getMedia().getWidth();
                    int h = player.getMedia().getHeight();

                    if(w>vedioPanel.getWidth())
                        w = (int)vedioPanel.getWidth();
                    if (h>vedioPanel.getHeight())
                        h = (int)vedioPanel.getHeight();

                    mediaView.setFitWidth(w);
                    mediaView.setFitHeight(h);

                }
            });

            try {

                //todo bug问题
                //由于poi库太老，现在的问题是读取word中表格会出错，然后我使用另一种读取段落的方法，读取后的表格数据不换行
                //手动加了“\n”后，正常文本每段会多一个换行
                //已解决


                fis2003=new FileInputStream(new File(localPath+"/temp/lizhu.doc"));
                doc2003 = new HWPFDocument(fis2003);
                word2003 = new WordExtractor(doc2003);

//                String text2003 = word2003.getText();
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

            } catch (Exception e) {
               e.printStackTrace();
            }

        }
    }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            localPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
            System.out.println(localPath);
            idTextField.requestFocus();

            selectBox.setItems(options);


        }

        public String pathFix(String path){
            return path.replaceAll("\\\\", "/");
        }

        public void testDBConnect(ActionEvent actionEvent) {

            db =new DBConnector();
            db.testDB();
//            db.testDownloadFiel("32");
//            String path = localPath + "/temp/samples.json";
//            readDataFromJson(path);
//
//            System.out.println(carMes.getType()[10].getOption()[0].getLabel());

        }

    private CarMes carMes;

    public void selectItem(ActionEvent actionEvent) {
    }

    public void readDataFromJson(String path){
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

        Gson gson = new Gson();

        carMes=gson.fromJson(json, CarMes.class); //String转化成JavaBean



    }
}
