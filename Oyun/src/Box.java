
import java.awt.Color;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Box extends JLabel {

    int kNo;
    int renk;
    public boolean simgeTikli = false;
    int Row, Col;
    int s1 = 1, s2 = 0;
    String img = "Brown.png";
    String img2 = "Green.png";
/**
 * Bu sınıfımız bize oyun tahtasının özelliklerini oluşturmamıza yardımcı oluyor . JFrame oluşturduumuz oyun alanını bu sınıfta ki ilgileri çağırarak oluşturuyoruz
 */
    public Box(int kNo, int renk) {
        this.kNo = kNo;
        this.renk = renk;
        this.setOpaque(true);
        if (kNo % 16 < 8) {
            s1 = 0;
            s2 = 1;
        }
        if (kNo % 2 == s1) {
            setBackground(Color.white);
        } else if (kNo % 2 == s2) {
            setBackground(Color.BLACK);
        }

        if (kNo == 0 || kNo == 63) {
            setIcon(new javax.swing.ImageIcon(img));
        }
        if (kNo == 7 || kNo == 56) {
            setIcon(new javax.swing.ImageIcon(img2));
        }
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

    }
    public Box(int kNo, int renk, boolean sahin) {
        this.kNo = kNo;
        this.renk = renk;
        this.setOpaque(true);
        if (kNo % 16 < 8) {
            s1 = 0;
            s2 = 1;
        }
        if (kNo % 2 == s1) {
            setBackground(Color.white);
        } else if (kNo % 2 == s2) {
            setBackground(Color.BLACK);
        }
       
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

    }
    

}
