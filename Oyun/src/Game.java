
import static com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.close;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**
  * Bu sınıf oyun başlama ekranını ve oyunun çalıştığı pencere,kayıt ekranı ve oyun talimatları ekranının bizlere ulaşabilemesi için kullanıyor
  * 
  */

public class Game extends javax.swing.JFrame {

  int close;
    public Game() {
        initComponents();
        Desing();
       
      

    }
   
    /**
     * Desing aracılığı ile ekaranımızın tasarımını istediğimiz gibi olmasını saglıyoruz 
     */
    public void Desing(){
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
            pack();}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        buttonKurallar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/playy.png"))); // NOI18N
        jButton1.setText("Yeni Oyun");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(260, 180, 180, 70);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        jButton2.setText("Kayıtlı oyunlar");
        jButton2.setFocusTraversalPolicyProvider(true);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setIconTextGap(20);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(260, 260, 180, 70);

        buttonKurallar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/kural.png"))); // NOI18N
        buttonKurallar.setText("Kurallar");
        buttonKurallar.setFocusTraversalPolicyProvider(true);
        buttonKurallar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        buttonKurallar.setIconTextGap(20);
        buttonKurallar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonKurallarActionPerformed(evt);
            }
        });
        getContentPane().add(buttonKurallar);
        buttonKurallar.setBounds(260, 340, 180, 60);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arka2.jpg"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(699, 510));
        jLabel1.setMinimumSize(new java.awt.Dimension(690, 500));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(690, 500));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 690, 500);

        pack();
    }// </editor-fold>//GEN-END:initComponents
/**
 * New game butonunun tetiklendiği metot
 */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       Oyunekrani oE=new Oyunekrani();
       oE.setVisible(true);
      
        setVisible(false);
       
    }//GEN-LAST:event_jButton1ActionPerformed
/**
 * Kayıt ekranını butonunun tetiklendiği metot
 */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
   KayitEkran kE=new KayitEkran();
   KayitEkran.eskiGame = this;
   kE.setVisible(true);
   //this.dispose();
   
    }//GEN-LAST:event_jButton2ActionPerformed
/**
 *Kurallar butonunun tetiklendiği metot
 */
    private void buttonKurallarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonKurallarActionPerformed
       Kurallar krllr=new Kurallar();
       krllr.setVisible(true);           
    }//GEN-LAST:event_buttonKurallarActionPerformed

  
    public static void main(String args[]) {
      
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonKurallar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
