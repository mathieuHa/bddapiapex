import com.opencsv.CSVWriter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = null;
    private static JFrame jf = null;
    static JProgressBar progressBar = null;

    public static void main(String[] args) {
        progressBar = new JProgressBar();
        progressBar.setMaximum(50);
        jf = new JFrame();
        jf.add(progressBar);
        jf.setSize(500,80);
        jf.setVisible(true);
        String csv = "data.csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("file.csv"), '\t');
            // feed in your array (or convert your data to an array)
            String[] entries = "first#second#third".split("#");
            writer.writeNext(entries);
            writer.close();

            //close the writer
        } catch (IOException e) {
            e.printStackTrace();
        }
        SwingWorker<Void, Void> sw = new SwingWorker<Void,Void>(){
            private String s;
            AddJson ad = new AddJson();
            @Override
            protected Void doInBackground() throws Exception {
                // TODO Auto-generated method stub
                File f = new File("dico.dico");
                FileReader fr;
                try {
                    fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);
                    scanner = new Scanner(br);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                for (int i = 1; i<10; i++){
                    s = scanner.nextLine();
                    //ad.addVideo(i);
                    //ad.addBook(s);
                    ad.addMusic(s);
                    setProgress(i);
                }

                return null;
            }
            @Override
            protected void done() {
                jf.dispose();
            }

        };
        sw.addPropertyChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                if("progress".equals(arg0.getPropertyName())){
                    if(SwingUtilities.isEventDispatchThread())
                        progressBar.setValue(progressBar.getValue()+1);
                }
            }
        });
        sw.execute();

    }

    public void LectureFichier (){

    }




}
