
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 Kayıtlı olan oyunları görebildiğimiz ve açmamızı sağlayan ekran
 */
public class KayitEkran extends javax.swing.JFrame {

    public static Game eskiGame;
    static int indx;
    DefaultTableModel dtm;
    static String yenadres;
    ArrayList<String> adresler = new ArrayList<>();

    public KayitEkran() {
        initComponents();
        Desing();
        dtm = (DefaultTableModel) jTable1.getModel();
        SavE();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel2.setText("  Kayıtlı oyunlar");
        jLabel2.setOpaque(true);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 20, 270, 16);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "oyun"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 40, 270, 110);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arkaSAve.jpg"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -30, 400, 280);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        indx = jTable1.getSelectedRow();

        if (indx != -1) {
        
            yenadres = adresler.get(indx);
            setVisible(false);
            Oyunekrani oE = new Oyunekrani(1);
            oE.setVisible(true);
            
            
            eskiGame.dispose();
         
        }
    }//GEN-LAST:event_jTable1MouseClicked

    public void Desing() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
        );
        pack();
    }

    public void SavE() {
        String Saveadres = "Kayitliadresler.txt";
        File adrsdosya = new File(Saveadres);
        if (adrsdosya.exists()) {
            try {
                Scanner okum = new Scanner(adrsdosya, "UTF-8");
                while (okum.hasNext()) {
                    String a = okum.next();

                    adresler.add(a);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Oyunekrani.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Table();
    }

    public void Table() {
        for (int i = 0; i < adresler.size(); i++) {
            String adres = adresler.get(i);
            Object o[] = {adres};
            dtm.addRow(o);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
