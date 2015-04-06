package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class Controller implements Initializable{
    public TextField idTextField;
    public AnchorPane vedioPanel;
    public AnchorPane wordPanel;
    public TextArea wordViewer;
    public MediaView mediaView;
    private String localPath;
    private DBConnector db;



    public void searchClick(ActionEvent actionEvent) throws IOException, Exception {

        FileInputStream fis2003;
        HWPFDocument doc2003;
        WordExtractor word2003;



        if (this.idTextField.getText().isEmpty()){
            JOptionPane.showConfirmDialog(null,"please input ID number");
        }else {
            //todo

            System.out.println("file://"+ localPath +"/temp/videoviewdemo.mp4");
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

        }

        public void testDBConnect(ActionEvent actionEvent) {

            db =new DBConnector();
            db.testDownloadFiel("32");

        }
}
