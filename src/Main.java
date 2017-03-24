
import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = null;
    private static JFrame jf = null;
    static JProgressBar progressBar = null;
    static PrintWriter pw;


    public static void main(String[] args) {
        progressBar = new JProgressBar();
        progressBar.setMaximum(1000);
        jf = new JFrame();
        jf.add(progressBar);
        jf.setSize(500,80);
        jf.setVisible(true);
        try {
            pw = new PrintWriter(new File("test.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String csv = "data.csv";
        SwingWorker<Void, Void> sw = new SwingWorker<Void,Void>(){
            private String s;
            AddJson ad = new AddJson(pw);
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

                for (int i = 1; i<1000; i++){
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
                pw.close();
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
