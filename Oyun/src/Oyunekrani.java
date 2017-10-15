
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import sun.java2d.pipe.DrawImage;





/**
 * Oyunla ilgili tüm olasılıkları bölümde oluşuruyoruz 
 * oyunun hareket koşullarını
 * oyun tahtamızın oluşumunu sağlayan komutları 
 * oyuncuları yerlerine yerleştirmemizi sağlayan komutlar burada
 

*/
public final class Oyunekrani extends javax.swing.JFrame {

    String img = "Brown.png";//Birinci oyuncunun img
    String img2 = "Green.png";// ikinci oyuncunun img
    String imgs;//Metotlara o anki img adresini aktarmak için kullanıyorum 
    String arkaplan = "arka.jpg";//arka planda yer alan img
    public int Row, Col;// oyunucunun satır ve sutunları
    int k, yas, yeni, eskisi, Cnt = 1;// Cnt =oyuncu sırasını belirlemede kullanıyorum, yeni ve eski = ilk konumdaki img degeri ile şimdiki img değerini 
    // ayırt etmemizi saglıyor ,yas= mause tıklaması yapıldığı andaki yerin konnumunu gönderiyor ,k =o anki oyuncuyu dönderiyor
    int kpyl[] = new int[1];// ilk basılma anında haritadaki adresi tutar
    int kNo = 0;// oyun tahtasını oluştururken atamada kullandığımız değişken
    int HedefNoktalar[] = new int[10];// bir kare uzaklıktaki gidilebilinecek noktaları tutuyor
    int Kopyala[] = new int[16];// iki kare uzaklıktaki gidilebilinecek alanları tutuyor
    Box kutu[][] = new Box[8][8];// oyun tahtasını ve üzerindekileri tuttugumuz dizi
    int Legal[][] = new int[8][8];//oyuncularımızın o anki hareketlerini tuttugumuz dizi
    public boolean SecimGoster = false;// ilk seçimle ikinci seçim arasındaki ayrımı fark etmemizi sağlayan durum
    ArrayList<String> adresler = new ArrayList<>();//adresleri tuttuğumuz array
    ArrayList<String> HareketSinirlari = new ArrayList<>();//hareket sınırlarını belirlediğimiz array
    static KayitEkran kekrn = new KayitEkran();//Kayıt ekranının verilerini hazırladığımız dizi
    String yeniadres = "";//Kayıt verilerini alırken kullandıgımız adres değişkeni 
    int Finis = 0, BirinciOSayac = 0, iKinciOSayac = 0;// Finis= tahtamızda boş karenın kalıp kalmadıgını ögrenmede kullanıyoruz 
    //BirinciOSayac,iKinciOSayac = oyuncuların puanlarının hesaplanmasını sağlıyor
  /**
   * Oyun ekranını new game aracılığı ile açıtığımız zaman bize oyun ekranını hazırlayan birim 
   */
    public Oyunekrani() {
        initComponents();

        baslangic(true);
        Dizilis();
        setLocationRelativeTo(null);
        pack();

    }
/**
 * Save game ile oyun açtığımızda bize oyun tahtsını veren metot
 */
   public Oyunekrani(int abc) {
        initComponents();

        baslangic(false);
        saveGame();
        setLocationRelativeTo(null);
        pack();

        
        
    }
    Box b;

    /**
     * Oyun başlarken oyun tahtasını şekillendiren ve oyunun başlangıç konumuna
     * hazırlanmssını sağlayan metot
     */
    public void baslangic(boolean ikinci) {

        if (Cnt == 1) {
            txtSira1.setBackground(Color.red);
            txtSira1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 204, 255), 2));
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (ikinci == true) {
                    b = new Box(kNo, j % 2);
                } else if (ikinci == false) {
                    b = new Box(kNo, j % 2, true);
                }
                b.setName(kNo + "");
                kNo++;
                jPanel2.add(b);

                kutu[i][j] = b;
                Legal[i][j] = 0;
                kutu[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (!SecimGoster) {
                            MouseClicked(evt);
                        } else if (SecimGoster) {
                            Hareket(evt, k);
                        }
                    }
                });

            }

        }
    }

    /**
     * Oyunucu seçimini yaptıktan sonra hareket edeceği alanı seçmek için
     * kullandığım Click evt eğer kurallara uygun ise oyuncu hareketini
     * gerçekleştirir
     */
    public void Hareket(MouseEvent evt, int k) {
        Box b = (Box) evt.getSource();
        int yas1 = Integer.parseInt(b.getName());
        int Row1 = yas1 / 8;
        int Col1 = yas1 - (Row1 * 8);

        if (k == 1) {

            BirinciOyunucu(Row1, Col1, yas1);

            //KopyalaDiz();
        }
        if (k == 2) {
            ikinciOyunucu(Row1, Col1, yas1);
       
        } else {
            if (!SecimGoster) {
                // KopyalaDiz();
            }  }

        Finis();
    }

    /**
     * İkinci oyuncunun oyun alanlarını seçmemizi ve oyuncunun hareketlerini
     * kontrol etmemizi sağlıyor
     *
     */
    public void ikinciOyunucu(int Row1, int Col1, int yas1) {
        if (Legal[Row1][Col1] == 0) {
            if (HedefNoktalar[0] == yas1 || HedefNoktalar[1] == yas1 || HedefNoktalar[2] == yas1 || HedefNoktalar[3] == yas1 || HedefNoktalar[4] == yas1
                    || HedefNoktalar[5] == yas1 || HedefNoktalar[6] == yas1 || HedefNoktalar[7] == yas1 || HedefNoktalar[8] == yas1) {

                kutu[Row1][Col1].setIcon(new javax.swing.ImageIcon(img2));
                kutu[Row1][Col1].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                Legal[Row1][Col1] = 2;
                SecimGoster = false;
                BirSatirGosterme();
                ikiSatrHedefGosterme();
                KopyalaDiz();
                Benzetme2Oyuncu(Row1, Col1);
                txtSira2.setBackground(null);
                txtSira2.setBorder(javax.swing.BorderFactory.createLineBorder(null));
                txtSira1.setBackground(Color.red);
                txtSira1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 204, 255), 2));
            } else {
                for (int i = 0; i < Kopyala.length; i++) {
                    if (Kopyala[i] == yas1) {
                        yas = kpyl[0];
                        int Row = yas / 8;
                        int Col = yas - (Row * 8);
                        Legal[Row][Col] = 0;
                        kutu[Row][Col].setIcon(null);
                        kutu[Row1][Col1].setIcon(new javax.swing.ImageIcon(img2));
                        kutu[Row1][Col1].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                        Legal[Row1][Col1] = 2;
                        BirSatirGosterme();
                        ikiSatrHedefGosterme();
                        SecimGoster = false;
                        KopyalaDiz();
                        Benzetme2Oyuncu(Row1, Col1);
                        txtSira2.setBackground(null);
                        txtSira2.setBorder(javax.swing.BorderFactory.createLineBorder(null));
                        txtSira1.setBackground(Color.red);
                        txtSira1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 204, 255), 2));
                    }
                }

            }

        } else {
            SecimGoster = true;
        }

    }

    /**
     * Oyunun kurallarına uygun ise oyuncuyu bir kare ileriye kopyalayan metot
     * ve kurallarının bulunduğu alan
     */
    public void BenzetmeMetot(int Row1, int Col1, String imgs, int eskisi, int yeni) {
        if (Row1 != 0 && Col1 != 0 && Row1 != 7 && Col1 != 7) {
            if (Legal[Row1 + 1][Col1] == eskisi) {
                kutu[Row1 + 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1] = yeni;
            }
            if (Legal[Row1 + 1][Col1 - 1] == eskisi) {
                kutu[Row1 + 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1 - 1] = yeni;
            }
            if (Legal[Row1][Col1 - 1] == eskisi) {
                kutu[Row1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 - 1] == eskisi) {
                kutu[Row1 - 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1] == eskisi) {
                kutu[Row1 - 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 + 1] == eskisi) {
                kutu[Row1 - 1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 + 1] = yeni;
            }
            if (Legal[Row1][Col1 + 1] == eskisi) {
                kutu[Row1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1 + 1] == eskisi) {
                kutu[Row1 + 1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1 + 1] = yeni;
            }

        }

        if (Row1 == 0 && Col1 != 0 && Col1 != 7) {
            if (Legal[Row1][Col1 - 1] == eskisi) {
                kutu[Row1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 - 1] = yeni;
            }
            if (Legal[Row1][Col1 - 1] == eskisi) {
                kutu[Row1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1] == eskisi) {
                kutu[Row1 + 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1] = yeni;
            }
            if (Legal[Row1 + 1][Col1 - 1] == eskisi) {
                kutu[Row1 + 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1 - 1] = yeni;
            }
            if (Legal[Row1][Col1 + 1] == eskisi) {
                kutu[Row1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 + 1] = yeni;
            }
        }
        if (Row1 == 7 && Col1 != 7 && Col1 != 0) {
            if (Legal[Row1 - 1][Col1] == eskisi) {
                kutu[Row1 - 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1] = yeni;
            }
            if (Legal[Row1][Col1 - 1] == eskisi) {
                kutu[Row1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 - 1] = yeni;
            }
            if (Legal[Row1][Col1 + 1] == eskisi) {
                kutu[Row1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 + 1] == eskisi) {
                kutu[Row1 - 1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 - 1] == eskisi) {
                kutu[Row1 - 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 - 1] = yeni;
            }

        }
        if (Row1 != 7 && Col1 == 0 && Row1 != 0) {
            if (Legal[Row1 - 1][Col1] == eskisi) {
                kutu[Row1 - 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1] = yeni;
            }
            if (Legal[Row1][Col1 + 1] == eskisi) {
                kutu[Row1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1 + 1] == eskisi) {
                kutu[Row1 + 1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 + 1] == eskisi) {
                kutu[Row1 - 1][Col1 + 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 + 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1] == eskisi) {
                kutu[Row1 + 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1] = yeni;
            }

        }
        if (Row1 != 7 && Col1 == 7 && Row1 != 0) {
            if (Legal[Row1 - 1][Col1] == eskisi) {
                kutu[Row1 - 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1] = yeni;
            }
            if (Legal[Row1][Col1 - 1] == eskisi) {
                kutu[Row1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1 - 1] == eskisi) {
                kutu[Row1 + 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 - 1][Col1 - 1] == eskisi) {
                kutu[Row1 - 1][Col1 - 1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 - 1][Col1 - 1] = yeni;
            }
            if (Legal[Row1 + 1][Col1] == eskisi) {
                kutu[Row1 + 1][Col1].setIcon(new javax.swing.ImageIcon(imgs));
                Legal[Row1 + 1][Col1] = yeni;
            }

        }

    }

    /**
     * ikinci oyuncu için değerleri alıp  benzetme metodunu kullanabilmemizi sağlayan metot 
    */
    public void Benzetme2Oyuncu(int Row1, int Col1) {
        imgs = img2;
        eskisi = 1;
        yeni = 2;
        BenzetmeMetot(Row1, Col1, imgs, eskisi, yeni);

    }

    /**
     * ikinci oyuncu için değerleri alıp benzetme metodunu kullanabilmemizi
     * sağlayan metot
     */
    public void Benzetme1Oyuncu(int Row1, int Col1) {
        imgs = img;
        eskisi = 2;
        yeni = 1;
        BenzetmeMetot(Row1, Col1, imgs, eskisi, yeni);
    }

    /**
     * Birinci oyuncunun oyun alanlarını seçmemizi ve oyuncunun hareketlerini
     * kontrol etmemizi sağlıyor
     */
    public void BirinciOyunucu(int Row1, int Col1, int yas1) {
        if (Legal[Row1][Col1] == 0) {
            if (HedefNoktalar[0] == yas1 || HedefNoktalar[1] == yas1 || HedefNoktalar[2] == yas1 || HedefNoktalar[3] == yas1 || HedefNoktalar[4] == yas1
                    || HedefNoktalar[5] == yas1 || HedefNoktalar[6] == yas1 || HedefNoktalar[7] == yas1 || HedefNoktalar[8] == yas1) {

                kutu[Row1][Col1].setIcon(new javax.swing.ImageIcon(img));
                kutu[Row1][Col1].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                Legal[Row1][Col1] = 1;
                SecimGoster = false;

                BirSatirGosterme();
                ikiSatrHedefGosterme();
                KopyalaDiz();
                Benzetme1Oyuncu(Row1, Col1);
                txtSira1.setBackground(null);
                txtSira1.setBorder(javax.swing.BorderFactory.createLineBorder(null));
                txtSira2.setBackground(Color.red);
                txtSira2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 204, 255), 2));
            } else {
                for (int i = 0; i < Kopyala.length; i++) {
                    if (Kopyala[i] == yas1) {

                        yas = kpyl[0];
                        int Row = yas / 8;
                        int Col = yas - (Row * 8);
                        Legal[Row][Col] = 0;
                        kutu[Row][Col].setIcon(null);
                        kutu[Row1][Col1].setIcon(new javax.swing.ImageIcon(img));
                        kutu[Row1][Col1].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                        Legal[Row1][Col1] = 1;
                        BirSatirGosterme();
                        ikiSatrHedefGosterme();
                        KopyalaDiz();
                        Benzetme1Oyuncu(Row1, Col1);
                        SecimGoster = false;
                        txtSira1.setBackground(null);
                        txtSira1.setBorder(javax.swing.BorderFactory.createLineBorder(null));
                        txtSira2.setBackground(Color.red);
                        txtSira2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(70, 204, 255), 2));
                    }

                }

            }

        }   }

    /**
     * Oyuncu seçimi sırasında mause Clicked metodu çalışır ve doğru oyuncuya
     * tıklandımı veya tıklanan alanda oyuncu varmı diye sorgular
     */
    public void MouseClicked(MouseEvent evt) {
        Box a = (Box) evt.getSource();
        int yas = Integer.parseInt(a.getName());

        int Row = yas / 8;

        int Col = yas - (Row * 8);

        kpyl[0] = yas;
        if (Legal[Row][Col] == 1) {
            if (Cnt % 2 == 1) {

              
                HareketSinirlariBelirleme(Row, Col);
                BirsatirHedefGoster();
                ikiSatirHedefGoster();
                SecimGoster = true;
                Cnt++;
                k = 1;
            } 

        } else if (Legal[Row][Col] == 2) {
            if (Cnt % 2 == 0) {

               
                HareketSinirlariBelirleme(Row, Col);
                BirsatirHedefGoster();
                ikiSatirHedefGoster();
                SecimGoster = true;
                Cnt++;
                k = 2;
            }

        } else if (Legal[Row][Col] == 0) {
            SecimGoster = false;
            k = 0;
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtSira1 = new javax.swing.JLabel();
        txtSira2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        buttonPes = new javax.swing.JButton();
        OyunuKaydet = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtiKinciOSkor1 = new javax.swing.JLabel();
        txtBirinciOSkor = new javax.swing.JLabel();
        AnaMenu = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(0, 153, 255));
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.green, java.awt.Color.green, java.awt.Color.green, java.awt.Color.green));
        jPanel2.setLayout(new java.awt.GridLayout(8, 8, 1, 1));

        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setLayout(null);

        txtSira1.setBackground(new java.awt.Color(0, 153, 204));
        txtSira1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Brown.png"))); // NOI18N
        txtSira1.setText("HADİ OYNAYALIM DOSTUM");
        txtSira1.setOpaque(true);
        jPanel1.add(txtSira1);
        txtSira1.setBounds(10, 170, 270, 60);

        txtSira2.setBackground(new java.awt.Color(0, 153, 204));
        txtSira2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Green.png"))); // NOI18N
        txtSira2.setText("SIRA BENDE DOSTUM");
        txtSira2.setOpaque(true);
        jPanel1.add(txtSira2);
        txtSira2.setBounds(10, 320, 270, 60);

        jLabel3.setBackground(new java.awt.Color(0, 153, 204));
        jLabel3.setText("1. Oyuncu Skor:  ");
        jLabel3.setOpaque(true);
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 240, 110, 30);

        buttonPes.setBackground(new java.awt.Color(0, 153, 204));
        buttonPes.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        buttonPes.setForeground(new java.awt.Color(0, 0, 0));
        buttonPes.setText("Pes Et");
        buttonPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPesActionPerformed(evt);
            }
        });
        jPanel1.add(buttonPes);
        buttonPes.setBounds(10, 110, 270, 50);

        OyunuKaydet.setBackground(new java.awt.Color(0, 153, 204));
        OyunuKaydet.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        OyunuKaydet.setText("Kaydet");
        OyunuKaydet.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 153, 255)));
        OyunuKaydet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OyunuKaydetMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                OyunuKaydetMouseExited(evt);
            }
        });
        OyunuKaydet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OyunuKaydetActionPerformed(evt);
            }
        });
        jPanel1.add(OyunuKaydet);
        OyunuKaydet.setBounds(10, 50, 270, 52);

        jLabel4.setBackground(new java.awt.Color(0, 153, 204));
        jLabel4.setText("2. Oyuncu Skor:  ");
        jLabel4.setOpaque(true);
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 400, 110, 30);

        txtiKinciOSkor1.setBackground(new java.awt.Color(0, 153, 204));
        txtiKinciOSkor1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtiKinciOSkor1.setOpaque(true);
        jPanel1.add(txtiKinciOSkor1);
        txtiKinciOSkor1.setBounds(170, 400, 70, 30);

        txtBirinciOSkor.setBackground(new java.awt.Color(0, 153, 204));
        txtBirinciOSkor.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtBirinciOSkor.setOpaque(true);
        jPanel1.add(txtBirinciOSkor);
        txtBirinciOSkor.setBounds(170, 240, 70, 30);

        AnaMenu.setBackground(new java.awt.Color(0, 153, 204));
        AnaMenu.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        AnaMenu.setText("Ana Menü");
        AnaMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 0, 255)));
        AnaMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                AnaMenuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                AnaMenuMouseExited(evt);
            }
        });
        AnaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnaMenuActionPerformed(evt);
            }
        });
        jPanel1.add(AnaMenu);
        AnaMenu.setBounds(10, 0, 270, 40);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/KFP3_Pos_Jumping_Adventure_lg.jpg"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1095, 1000));
        jLabel1.setMinimumSize(new java.awt.Dimension(1095, 1000));
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(-220, -80, 1200, 770);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *   Oyuncu verilerini aldığımız ve oyuncunun oyununu kaydetmesini sağlayan metot
     */
    private void OyunuKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OyunuKaydetActionPerformed

        String name = JOptionPane.showInputDialog(null, "Kayıt için bir isim seçiniz");

        String adrs = name + ".txt";
        String Saveadres = "Kayitliadresler.txt";
        File adrsdosya = new File(Saveadres);
        if (adrsdosya.exists()) {
            try {
                Scanner okum = new Scanner(adrsdosya, "UTF-8");
                while (okum.hasNextLine()) {
                    String a = okum.nextLine();

                    adresler.add(a);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Oyunekrani.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                adrsdosya.createNewFile();
            } catch (Exception e) {
                Logger.getLogger(Oyunekrani.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        try {
            PrintWriter yazim = new PrintWriter(adrsdosya, "UTF-8");
            for (int i = 0; i < adresler.size(); i++) {
                String adress = adresler.get(i);
                yazim.println(adress);
            }
            yazim.println(adrs);
            yazim.close();

        } catch (Exception e) {
            System.out.println(" hata " + e);
        }

        File AdresDosyasi = new File(adrs);

        File dosya = new File(adrs);
        try {
            dosya.createNewFile();
            PrintWriter yaz = new PrintWriter(dosya, "UTF-8");

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int point = Legal[i][j];
                    System.out.println(" " + point);
                    yaz.println(point);
                }
            }
            yaz.close();
        } catch (Exception e) {
            System.out.println(" hata : " + e);
        }

    }//GEN-LAST:event_OyunuKaydetActionPerformed
/**
 * Masue exited sırasında mause arkaplan renginin değiştirildiği alan
 */
    private void OyunuKaydetMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OyunuKaydetMouseExited
        OyunuKaydet.setBackground(Color.ORANGE);
    }//GEN-LAST:event_OyunuKaydetMouseExited
/**
 * Masue entered sırasında mause arkaplan renginin değiştirildiği alan
 */
    private void OyunuKaydetMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OyunuKaydetMouseEntered
        OyunuKaydet.setBackground(Color.YELLOW);
    }//GEN-LAST:event_OyunuKaydetMouseEntered
/**
 * Ana menü buttonuna basıldığı zaman çalışan ve bizi ana menu ekranına dönderen metot
*/
    private void AnaMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnaMenuActionPerformed
        Game game = new Game();
        game.setVisible(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setVisible(false);

    }//GEN-LAST:event_AnaMenuActionPerformed

    private void AnaMenuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AnaMenuMouseExited
        AnaMenu.setBackground(Color.ORANGE);
    }//GEN-LAST:event_AnaMenuMouseExited

    private void AnaMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AnaMenuMouseEntered
        AnaMenu.setBackground(Color.YELLOW);
    }//GEN-LAST:event_AnaMenuMouseEntered

    private void buttonPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPesActionPerformed
        Pes(k);
    }//GEN-LAST:event_buttonPesActionPerformed
public void Pes(int k)
{
    if(k==1)
    { JOptionPane.showMessageDialog(null, "Birinci Oyuncu Kazandı ");
    }
    if(k==2)
    { JOptionPane.showMessageDialog(null, "İkinci Oyuncu Kazandı ");
    }
   this.dispose();
   Game gm=new Game();
   gm.setVisible(true);
   
}
    /**
 * Oyuncunun hareket sınırlarını belirlediğimiz alan 
 * Bu alanda oyuncunun 2 kare ileri ve 1 kare ileri hangi noktalara gidebileceğini kesinleştiriyoruz 
 * ve burada kesinleştirdiğimiz kurallara bağlı olarak oyuncuyu hareket ettiriyoruz
 */
    public void HareketSinirlariBelirleme(int Row, int Col) {
        HedefNoktaDizi();
        if (Row != 0 && Col != 0 && Row != 7 && Col != 7) {

            if (Row > 1 && Col > 1 && Row < 6 && Col < 6) {
                Kopyala[0] = (8 * Row) + (Col - 2);
                Kopyala[1] = (8 * (Row - 1)) + (Col - 2);
                Kopyala[2] = (8 * (Row - 2)) + (Col - 2);
                Kopyala[3] = (8 * (Row - 2)) + (Col - 1);
                Kopyala[4] = (8 * (Row - 2)) + (Col);
                Kopyala[5] = (8 * (Row - 2)) + (Col + 1);
                Kopyala[6] = (8 * (Row - 2)) + (Col + 2);
                Kopyala[7] = (8 * (Row - 1)) + (Col + 2);
                Kopyala[8] = (8 * (Row)) + (Col + 2);
                Kopyala[9] = (8 * (Row + 1)) + (Col + 2);
                Kopyala[10] = (8 * (Row + 2)) + (Col + 2);
                Kopyala[11] = (8 * (Row + 2)) + (Col + 1);
                Kopyala[12] = (8 * (Row + 2)) + (Col);
                Kopyala[13] = (8 * (Row + 2)) + (Col - 1);
                Kopyala[14] = (8 * (Row + 2)) + (Col - 2);
                Kopyala[15] = (8 * (Row + 1)) + (Col - 2);
            } 
            else if(Row==6 && Col==6)
            {
   
                Kopyala[0] = (8 * Row) + (Col - 2);
                Kopyala[1] = (8 * (Row - 1)) + (Col - 2);
                Kopyala[2] = (8 * (Row - 2)) + (Col - 2);
                Kopyala[3] = (8 * (Row - 2)) + (Col - 1);
                Kopyala[4] = (8 * (Row - 2)) + (Col);
                Kopyala[5] = (8 * (Row - 2)) + (Col + 1);
            
              
                 Kopyala[10] = (8 * (Row + 1)) + (Col - 2);
            
            }
             else if (Row > 1 && Row<6  && Col==6) {

                Kopyala[0] = (8 * Row) + (Col - 2);
                Kopyala[1] = (8 * (Row - 1)) + (Col - 2);
                Kopyala[2] = (8 * (Row - 2)) + (Col - 2);
                Kopyala[3] = (8 * (Row - 2)) + (Col - 1);
                Kopyala[4] = (8 * (Row - 2)) + (Col);
                Kopyala[5] = (8 * (Row - 2)) + (Col + 1);
                Kopyala[6] = (8 * (Row + 1) + (Col -2));
                Kopyala[7] = (8 * (Row + 2) + (Col -2));
                Kopyala[8] = (8 * (Row + 2) + (Col -1));
                Kopyala[9] = (8 * (Row + 2) + (Col));
                Kopyala[10] = (8 * (Row + 2) + (Col +1));
             
            }
            else if (Row > 1 && Col > 1 && Col<6) {
    
                Kopyala[0] = (8 * Row) + (Col - 2);
                Kopyala[1] = (8 * (Row - 1)) + (Col - 2);
                Kopyala[2] = (8 * (Row - 2)) + (Col - 2);
                Kopyala[3] = (8 * (Row - 2)) + (Col - 1);
                Kopyala[4] = (8 * (Row - 2)) + (Col);
                Kopyala[5] = (8 * (Row - 2)) + (Col + 1);
                Kopyala[6] = (8 * (Row - 2)) + (Col + 2);
                Kopyala[7] = (8 * (Row - 1)) + (Col + 2);
                Kopyala[8] = (8 * (Row )) + (Col + 2);
                Kopyala[9] = (8 * (Row + 1)) + (Col + 2);
                 Kopyala[10] = (8 * (Row + 1)) + (Col - 2);
            } else if (Row == 1 && Col < 6 && Col > 1) {
               
                Kopyala[0] = (8 * Row) + (Col + 2);
                Kopyala[1] = (8 * (Row - 1)) + (Col + 2);
                Kopyala[2] = (8 * (Row + 2) + (Col + 2));
                Kopyala[3] = (8 * (Row + 1) + (Col + 2));
                Kopyala[4] = (8 * (Row + 2) + (Col + 1));
                Kopyala[5] = (8 * (Row + 2) + Col);
                Kopyala[6] = (8 * (Row + 2) + (Col - 1));
                Kopyala[7] = (8 * (Row + 2) + (Col - 2));
                Kopyala[8] = (8 * (Row + 1) + Col - 2);
                Kopyala[9] = (8 * (Row) + Col - 2);
                Kopyala[10] = (8 * (Row - 1) + Col - 2);

            } else if (Col == 1 && Row < 6 & Row > 1) {
                Kopyala[0] = (8 * (Row - 2) + (Col - 1));
                Kopyala[1] = (8 * (Row - 2) + (Col));
                Kopyala[2] = (8 * (Row - 2) + (Col + 1));
                Kopyala[3] = (8 * (Row - 2) + (Col + 2));
                Kopyala[4] = (8 * (Row - 1) + (Col + 2));
                Kopyala[5] = (8 * (Row) + (Col + 2));
                Kopyala[6] = (8 * (Row + 1) + (Col + 2));
                Kopyala[7] = (8 * (Row + 2) + (Col + 2));
                Kopyala[8] = (8 * (Row + 2) + (Col + 1));
                Kopyala[9] = (8 * (Row + 2) + (Col));
                Kopyala[10] = (8 * (Row + 2) + (Col - 1));

            } else if (Col == 1 && Row == 1) {
                Kopyala[0] = (8 * (Row - 1) + (Col + 2));
                Kopyala[1] = (8 * (Row) + (Col + 2));
                Kopyala[2] = (8 * (Row + 1) + (Col + 2));
                Kopyala[3] = (8 * (Row + 2) + (Col + 2));
                Kopyala[4] = (8 * (Row + 2) + (Col + 1));
                Kopyala[5] = (8 * (Row + 2) + (Col));
                Kopyala[6] = (8 * (Row + 2) + (Col - 1));
            } else if (Col == 1 && Row == 6) {
                Kopyala[0] = (8 * (Row - 2) + (Col - 1));
                Kopyala[1] = (8 * (Row - 2) + (Col));
                Kopyala[2] = (8 * (Row - 2) + (Col + 1));
                Kopyala[3] = (8 * (Row - 2) + (Col + 2));
                Kopyala[4] = (8 * (Row - 1) + (Col + 2));
                Kopyala[5] = (8 * (Row) + (Col + 2));
                Kopyala[6] = (8 * (Row + 1) + (Col + 2));

            }
            HedefNoktalar[0] = (8 * Row) + (Col + 1);
            HedefNoktalar[1] = (8 * (Row + 1)) + Col;
            HedefNoktalar[2] = (8 * (Row - 1) + Col);
            HedefNoktalar[3] = (8 * (Row) + (Col - 1));
            HedefNoktalar[4] = (8 * (Row + 1) + (Col - 1));
            HedefNoktalar[5] = (8 * (Row - 1) + (Col - 1));
            HedefNoktalar[6] = (8 * (Row - 1) + (Col + 1));
            HedefNoktalar[7] = (8 * (Row + 1) + (Col + 1));

        } else if (Row == 7 && Col == 0) {
            HedefNoktalar[0] = (8 * Row) + (Col + 1);
            HedefNoktalar[1] = (8 * (Row - 1)) + (Col);
            HedefNoktalar[2] = (8 * (Row - 1)) + (Col + 1);
            Kopyala[0] = (8 * (Row - 2) + Col);
            Kopyala[1] = (8 * (Row - 2) + (Col + 1));
            Kopyala[2] = (8 * (Row - 2) + (Col + 2));
            Kopyala[3] = (8 * (Row - 1) + (Col + 2));
            Kopyala[4] = (8 * (Row) + (Col + 2));
        } else if (Row == 0 && Col == 0) {

            Kopyala[0] = (8 * (Row + 2) + Col);
            Kopyala[1] = (8 * (Row + 2) + (Col + 1));
            Kopyala[2] = (8 * (Row + 2) + (Col + 2));
            Kopyala[3] = (8 * (Row + 1) + (Col + 2));
            Kopyala[4] = (8 * (Row) + (Col + 2));
            HedefNoktalar[0] = (8 * (Row + 1) + Col);
            HedefNoktalar[1] = (8 * (Row) + (Col + 1));
            HedefNoktalar[2] = (8 * (Row + 1) + (Col + 1));
        } else if (Row == 0 && Col == 7) {

            Kopyala[0] = (8 * (Row) + Col - 2);
            Kopyala[1] = (8 * (Row + 1) + (Col - 2));
            Kopyala[2] = (8 * (Row + 2) + (Col - 2));
            Kopyala[3] = (8 * (Row + 2) + (Col - 1));
            Kopyala[4] = (8 * (Row + 2) + (Col));
            HedefNoktalar[0] = (8 * (Row) + (Col - 1));
            HedefNoktalar[1] = (8 * (Row + 1) + (Col));
            HedefNoktalar[2] = (8 * (Row + 1) + (Col - 1));
        } else if (Row == 7 && Col == 7) {

            Kopyala[0] = (8 * (Row - 2) + Col);
            Kopyala[1] = (8 * (Row - 2) + (Col - 1));
            Kopyala[2] = (8 * (Row - 2) + (Col - 2));
            Kopyala[3] = (8 * (Row - 1) + (Col - 2));
            Kopyala[4] = (8 * (Row) + (Col - 2));
            HedefNoktalar[0] = (8 * (Row - 1) + Col);
            HedefNoktalar[1] = ((8 * Row) + (Col - 1));
            HedefNoktalar[2] = ((8 * (Row - 1)) + (Col - 1));
        } else if (Row == 0 && Col != 7 && Col != 0) {

            if (Col > 1 && Col < 6) {
                Kopyala[0] = (8 * (Row) + (Col + 2));
                Kopyala[1] = (8 * (Row + 1) + (Col + 2));
                Kopyala[2] = (8 * (Row + 2) + (Col + 2));
                Kopyala[3] = (8 * (Row + 2) + (Col + 1));
                Kopyala[4] = (8 * (Row + 2) + (Col));
                Kopyala[5] = (8 * (Row + 2) + Col - 1);
                Kopyala[6] = (8 * (Row + 2) + (Col - 2));
                Kopyala[7] = (8 * (Row + 1) + (Col - 2));
                Kopyala[8] = (8 * (Row) + (Col - 2));
            } else if (Col == 6) {
                Kopyala[0] = (8 * (Row) + (Col - 2));
                Kopyala[1] = (8 * (Row + 1) + (Col - 2));
                Kopyala[2] = (8 * (Row + 2) + (Col - 2));
                Kopyala[3] = (8 * (Row + 2) + (Col - 1));
                Kopyala[4] = (8 * (Row + 2) + (Col));
                Kopyala[5] = (8 * (Row + 2) + (Col + 1));
            } else if (Col == 1) {
                Kopyala[0] = (8 * (Row) + (Col + 2));
                Kopyala[1] = (8 * (Row + 1) + (Col + 2));
                Kopyala[2] = (8 * (Row + 2) + (Col + 2));
                Kopyala[3] = (8 * (Row + 2) + (Col + 1));
                Kopyala[4] = (8 * (Row + 2) + (Col));
                Kopyala[5] = (8 * (Row + 2) + (Col - 1));

            }
            HedefNoktalar[0] = (8 * (Row) + (Col - 1));
            HedefNoktalar[1] = (8 * (Row + 1) + Col);
            HedefNoktalar[2] = (8 * (Row + 1) + (Col + 1));

            HedefNoktalar[3] = (8 * (Row + 1) + (Col - 1));
            HedefNoktalar[4] = (8 * (Row) + (Col + 1));
        } else if (Row != 0 && Row != 7 && Col == 7) {

            if (Row > 1 && Row < 6) {
                Kopyala[0] = (8 * (Row - 2) + Col);
                Kopyala[1] = (8 * (Row - 2) + (Col - 1));
                Kopyala[2] = (8 * (Row - 2) + (Col - 2));
                Kopyala[3] = (8 * (Row - 1) + (Col - 2));
                Kopyala[4] = (8 * (Row) + (Col - 2));
                Kopyala[5] = (8 * (Row + 1) + (Col - 2));
                Kopyala[6] = (8 * (Row + 2) + (Col - 2));
                Kopyala[7] = (8 * (Row + 2) + (Col - 1));
                Kopyala[8] = (8 * (Row + 2) + (Col));
            }
            if (Row == 6) {
                Kopyala[0] = (8 * (Row + 1) + (Col - 2));
                Kopyala[1] = (8 * (Row) + (Col - 2));
                Kopyala[2] = (8 * (Row - 1) + (Col - 2));
                Kopyala[3] = (8 * (Row - 2) + (Col - 2));
                Kopyala[4] = (8 * (Row - 2) + (Col - 1));
                Kopyala[5] = (8 * (Row - 2) + (Col));

            }
            if (Row == 1) {
                Kopyala[0] = (8 * (Row - 1) + (Col - 2));
                Kopyala[1] = (8 * (Row) + (Col - 2));
                Kopyala[2] = (8 * (Row + 1) + (Col - 2));
                Kopyala[3] = (8 * (Row + 2) + (Col - 2));
                Kopyala[4] = (8 * (Row + 2) + (Col - 1));
                Kopyala[5] = (8 * (Row + 2) + (Col));
            }
            HedefNoktalar[0] = (8 * (Row + 1) + Col);
            HedefNoktalar[1] = (8 * (Row - 1) + Col);
            HedefNoktalar[2] = (8 * (Row) + (Col - 1));
            HedefNoktalar[3] = (8 * (Row - 1) + (Col - 1));
            HedefNoktalar[4] = (8 * (Row + 1) + (Col - 1));

        } else if (Row != 0 && Row != 7 && Col == 0) {
            if (Row > 1 && Row < 6) {
                Kopyala[0] = (8 * (Row - 2) + Col);
                Kopyala[1] = (8 * (Row - 2) + (Col + 1));
                Kopyala[2] = (8 * (Row - 2) + (Col + 2));
                Kopyala[3] = (8 * (Row - 1) + (Col + 2));
                Kopyala[4] = (8 * (Row) + (Col + 2));
                Kopyala[5] = (8 * (Row + 1) + (Col + 2));
                Kopyala[6] = (8 * (Row + 2) + (Col + 2));
                Kopyala[7] = (8 * (Row + 2) + (Col + 1));
                Kopyala[8] = (8 * (Row + 2) + (Col));

            }
            if (Row == 1) {
                Kopyala[0] = (8 * (Row - 1) + (Col + 2));
                Kopyala[1] = (8 * (Row) + (Col + 2));
                Kopyala[2] = (8 * (Row + 1) + (Col + 2));
                Kopyala[3] = (8 * (Row + 2) + (Col + 2));
                Kopyala[4] = (8 * (Row + 2) + (Col + 1));
                Kopyala[5] = (8 * (Row + 2) + (Col));
            } else if (Row == 6) {
                Kopyala[0] = (8 * (Row + 1) + (Col + 2));
                Kopyala[1] = (8 * (Row) + (Col + 2));
                Kopyala[2] = (8 * (Row - 1) + (Col + 2));
                Kopyala[3] = (8 * (Row - 2) + (Col + 2));
                Kopyala[4] = (8 * (Row - 2) + (Col + 1));
                Kopyala[5] = (8 * (Row - 2) + (Col));

            }
            HedefNoktalar[0] = (8 * (Row + 1) + Col);
            HedefNoktalar[1] = (8 * (Row - 1) + Col);

            HedefNoktalar[2] = (8 * (Row - 1) + (Col + 1));
            HedefNoktalar[3] = (8 * (Row) + (Col + 1));
            HedefNoktalar[4] = (8 * (Row + 1) + (Col + 1));

        } else if (Row == 7 && Col != 7 && Col != 0) {

            if (Col > 1 && Col < 6) {
                Kopyala[0] = (8 * (Row) + (Col - 2));
                Kopyala[1] = (8 * (Row - 1) + (Col - 2));
                Kopyala[2] = (8 * (Row - 2) + (Col - 2));
                Kopyala[3] = (8 * (Row - 2) + (Col - 1));
                Kopyala[4] = (8 * (Row - 2) + (Col));
                Kopyala[5] = (8 * (Row - 2) + (Col + 1));
                Kopyala[6] = (8 * (Row - 2) + (Col + 2));
                Kopyala[7] = (8 * (Row - 1) + (Col + 2));
                Kopyala[8] = (8 * (Row) + (Col + 2));
            }
            if (Col == 1) {
                Kopyala[0] = (8 * (Row - 2) + (Col - 1));
                Kopyala[1] = (8 * (Row - 2) + (Col));
                Kopyala[2] = (8 * (Row - 2) + (Col + 1));
                Kopyala[3] = (8 * (Row - 2) + (Col + 2));
                Kopyala[4] = (8 * (Row - 1) + (Col + 2));
                Kopyala[5] = (8 * (Row) + (Col + 2));
            }
            if (Col == 6) {
                Kopyala[0] = (8 * (Row) + (Col - 2));
                Kopyala[1] = (8 * (Row - 1) + (Col - 2));
                Kopyala[2] = (8 * (Row - 2) + (Col - 2));
                Kopyala[3] = (8 * (Row - 2) + (Col - 1));
                Kopyala[4] = (8 * (Row - 2) + (Col));
                Kopyala[5] = (8 * (Row - 2) + (Col + 1));
                
                
                
                
            }
            HedefNoktalar[0] = (8 * (Row - 1) + (Col - 1));
            HedefNoktalar[1] = (8 * (Row-1) + (Col));
            HedefNoktalar[2] = (8 * (Row - 1) + (Col + 1));
            HedefNoktalar[3] = (8 * (Row) + (Col + 1));
            HedefNoktalar[4] = (8 * (Row) + (Col - 1));
            System.out.println(""+HedefNoktalar[0]);
             System.out.println(""+HedefNoktalar[1]);
              System.out.println(""+HedefNoktalar[2]);
               System.out.println(""+HedefNoktalar[3]);
        }

    }
/**
 *HedefNoktalar dizisi oyuncunun bir kare ileriye gidebileceği alanların bilgilerini içinde tutan dizidir. 
 * Biz burada bu dizinin içini her oyuncu değişimde sıfırlıyoruz
 * Amacımız daha önceki hamlede gidilebilinecek noktaların bilgisiyle şimdiki hamlemizdeki biilgilerin çakışmaması
 * Bu yüzden dizinin içini -1 ile donatıyoruz
 */
    public void HedefNoktaDizi() {
        for (int i = 0; i < HedefNoktalar.length; i++) {
            HedefNoktalar[i] = -1;
        }

    }
/**Oyuncunun 1 satır sonra hareket edebilceği yerlerin border özelliğini değiştirmemizi sağlayan metot
 * buradaki amacımız oyuncuya gidebileceği yerleri göstermek 
 */
    public void BirsatirHedefGoster() {
        for (int i = 0; i < HedefNoktalar.length; i++) {
            if (HedefNoktalar[i] != -1) {

                int yas = HedefNoktalar[i];
                System.out.println(" yaş " + yas);
                int Row = yas / 8;
                int Col = yas - (Row * 8);
                System.out.println(" Row  : " + Row + "   Col :  " + Col);
                if (Legal[Row][Col] == 0) {
                    kutu[Row][Col].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 255), 5));
                }

            }
        }

    }
/**Hamle sonrası Border özelliği değiştirilmiş alanları eski haline getirdiğimiz metot
 */
    public void BirSatirGosterme() {
        for (int i = 0; i < HedefNoktalar.length; i++) {
            if (HedefNoktalar[i] != -1) {

                int yas = HedefNoktalar[i];

                int Row = yas / 8;
                int Col = yas - (Row * 8);

                if (Legal[Row][Col] == 0) {
                    kutu[Row][Col].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                }

            }
        }

    }
/**Hamle sonrası Border özelliği değiştirilmiş alanları eski haline getirdiğimiz metot
 */
    public void ikiSatirHedefGoster() {

        for (int i = 0; i < Kopyala.length; i++) {
            if (Kopyala[i] != -1) {
                int yas = Kopyala[i];

                int Row = yas / 8;
                int Col = yas - (Row * 8);

                if (Legal[Row][Col] == 0) {
                    kutu[Row][Col].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0), 5));
                }
            }

        }

    }
/**Oyuncunun 2 satır sonra hareket edebilceği yerlerin border özelliğini değiştirmemizi sağlayan metot
 * buradaki amacımız oyuncuya gidebileceği yerleri göstermek 
 */
    public void ikiSatrHedefGosterme() {

        for (int i = 0; i < Kopyala.length; i++) {
            if (Kopyala[i] != -1) {
                int yas = Kopyala[i];

                int Row = yas / 8;
                int Col = yas - (Row * 8);

                if (Legal[Row][Col] == 0) {
                    kutu[Row][Col].setBorder(javax.swing.BorderFactory.createLineBorder(null));
                }
            }

        }

    }
/**Legal dizime oyunucu hamleleri başlamadan önce tahta üzerindeki oyuncuların yerlerini konumlandırdığımız alan
 */
    public void Dizilis() {

        Legal[0][0] = 1;
        Legal[0][7] = 2;
        Legal[7][0] = 2;
        Legal[7][7] = 1;

    }
/**
 * Oyunu kaydettiğimiz bölüm 
 */
    public void saveGame() {
        String gidilecekOyn = kekrn.yenadres;
        File oyunn = new File(gidilecekOyn);
        String ag;
        int Cnt = 0;
        try {

            Scanner okum = new Scanner(oyunn, "UTF-8");
            while (okum.hasNextLine()) {
                ag = okum.nextLine();
                HareketSinirlari.add(ag);

            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Integer HK = new Integer(HareketSinirlari.get(Cnt));
                    Legal[i][j] = HK;

                    if (Legal[i][j] == 1) {
                        kutu[i][j].setIcon(new javax.swing.ImageIcon(img));
                    }
                    if (Legal[i][j] == 2) {
                        kutu[i][j].setIcon(new javax.swing.ImageIcon(img2));
                    }

                    Cnt++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 *Kopyala dizisi oyuncunun 2 kare ileriye gidebileceği alanların bilgilerini içinde tutan dizidir. 
 * Biz burada bu dizinin içini her oyuncu değişimde sıfırlıyoruz
 * Amacımız daha önceki hamlede gidilebilinecek noktaların bilgisiyle şimdiki hamlemizdeki biilgilerin çakışmaması
 * Bu yüzden dizinin içini -1 ile donatıyoruz
 */
    public void KopyalaDiz() {
        for (int i = 0; i < Kopyala.length; i++) {
            Kopyala[i] = -1;
        }

    }
/**
 * Oyunun Devam durumunu kontrol eden metot . Oyun bittiği zaman çalışarak bize kazanan oyuncuyu gösteriyor. Ayrıca oyun sırasında ki 
 * oyuncu puanlarını gösteren kontrol mekanizmasıda burada çalışıyor
 */
    public void Finis() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Legal[i][j] == 1) {
                    BirinciOSayac++;
                }
                if (Legal[i][j] == 2) {
                    iKinciOSayac++;
                }
                if (Legal[i][j] == 0) {
                    Finis++;
                }
            }

        }
        txtBirinciOSkor.setText(""+BirinciOSayac);
        txtiKinciOSkor1.setText(""+iKinciOSayac);
        if (iKinciOSayac == 0) {
            JOptionPane.showMessageDialog(null, "Birinci Oyuncu Kazandı ");
        }
        if (BirinciOSayac == 0) {
            JOptionPane.showMessageDialog(null, "İkinci Oyuncu Kazandı ");
        }
        if (Finis == 0) {
            if (iKinciOSayac < BirinciOSayac) {
                JOptionPane.showMessageDialog(null, "Birinci Oyuncu Kazandı ");
            }
            if (BirinciOSayac < iKinciOSayac) {
                JOptionPane.showMessageDialog(null, "İkinci Oyuncu Kazandı ");
            }

        } else {
            Finis = 0;
            iKinciOSayac = 0;
            BirinciOSayac = 0;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnaMenu;
    private javax.swing.JButton OyunuKaydet;
    private javax.swing.JButton buttonPes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel txtBirinciOSkor;
    private javax.swing.JLabel txtSira1;
    private javax.swing.JLabel txtSira2;
    private javax.swing.JLabel txtiKinciOSkor1;
    // End of variables declaration//GEN-END:variables

}
