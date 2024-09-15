package Main;

import java.io.*;

public class Config {
    GamePanel gp;
    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig() throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter("config"));

        if(gp.fullScreenOn){
            bw.write("On");

    }
        if(!gp.fullScreenOn){
            bw.write("Off");
        }
        bw.newLine();

        bw.write(String.valueOf(gp.music.volumeScale));
        bw.newLine();

        bw.write(String.valueOf(gp.se.volumeScale));
        bw.newLine();

        bw.close();}
    public void loadConfig() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("config"));
         String a = br.readLine();

         if(a.equals("On")){
             gp.fullScreenOn = true;
         }
            if(a.equals("Off")){
                gp.fullScreenOn = false;
            }

            a = br.readLine();
            gp.music.volumeScale = (int) Float.parseFloat(a);

            a = br.readLine();
            gp.se.volumeScale = (int) Float.parseFloat(a);

            br.close();

        }

    }

