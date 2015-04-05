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


public class Controller implements Initializable{
    public TextField idTextField;
    public AnchorPane vedioPanel;
    public AnchorPane wordPanel;
    public TextArea wordViewer;
    public MediaView mediaView;
    private String localPath;



    public void searchClick(ActionEvent actionEvent) throws IOException {
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


                fis2003=new FileInputStream(new File(localPath+"/temp/test.doc"));
                doc2003 = new HWPFDocument(fis2003);
                word2003 = new WordExtractor(doc2003);

                String text2003 = word2003.getText();
                wordViewer.setText(text2003);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            localPath = System.getProperty("user.dir").replaceAll("\\\\", "/");
            System.out.println(localPath);

        }

        public void testDBConnect(ActionEvent actionEvent) {

            DBConnector db =new DBConnector();
            db.testDB();
        }
}
