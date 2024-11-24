import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author USER
 */
public class AplikasiPengelolaanNomorFrame extends javax.swing.JFrame {
    private DefaultTableModel model;
    /**
     * Creates new form AplikasiPengelolaanNomorFrame
     */
    public AplikasiPengelolaanNomorFrame() {
        initComponents();
        model = (DefaultTableModel) TbKategori.getModel();
        model.setColumnIdentifiers(new String[]{"ID", "Nama", "Nomor Telepon", "Kategori"});
        loadData();
    }
    
    private boolean validasiNomorTelepon(String nomor){
        return nomor.matches("\\d{10,13}");
    }
    
    private void tambahKontak() {
        System.out.println("Tombol Tambah ditekan.");
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();
        String nomorTelepon = TNoTelepon.getText();
        
        if (!validasiNomorTelepon(nomor)) {
                JOptionPane.showMessageDialog(this, "Nomor telepon harus terdiri dari 10-13 digit angka.");
            return;
        }

        String sql = "INSERT INTO kontak(nama, nomor_telepon, kategori) VALUES(?,?,?)";

            try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, nomor);
                pstmt.setString(3, kategori);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan");
                System.out.println("Data berhasil ditambahkan ke database.");
                
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
        }    
    }
    
    private void eksporKeCSV() {
    JFileChooser fileChooser = new JFileChooser();
    FcFile.setDialogTitle("Simpan sebagai CSV");
    int userSelection = FcFile.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = FcFile.getSelectedFile();
        
            try (FileWriter fw = new FileWriter(fileToSave + ".csv")) {
            // Menulis header kolom ke file CSV
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + ",");
                }
                fw.write("\n");

            // Menulis baris data dari JTable ke file CSV
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        fw.write(model.getValueAt(i, j).toString() + ",");
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke CSV!");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengekspor data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void imporDariCSV() {
        FcFile.setDialogTitle("Pilih file CSV");
        int userSelection = FcFile.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = FcFile.getSelectedFile();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileToOpen))) {
            String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    // Melewati baris pertama jika ada header
                    if (isFirstLine) {
                        isFirstLine = false;
                    continue;
                }

                    // Memisahkan setiap kolom dengan koma
                    String[] data = line.split(",");

                    // Pastikan jumlah kolom sesuai dengan tabel (misalnya ada 4 kolom: ID, Nama, Nomor Telepon, Kategori)
                if (data.length == 4) {
                    // Masukkan data ke dalam database
                    String sql = "INSERT INTO kontak(id, nama, nomor_telepon, kategori) VALUES(?, ?, ?, ?)";
                    try (Connection conn = DatabaseHelper.connect();
                        PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setInt(1, Integer.parseInt(data[0]));
                        pstmt.setString(2, data[1]);
                        pstmt.setString(3, data[2]);
                        pstmt.setString(4, data[3]);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Tambahkan data ke JTable
                    model.addRow(new Object[]{data[0], data[1], data[2], data[3]});
                }
            }
            JOptionPane.showMessageDialog(this, "Data berhasil diimpor dari CSV!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membaca file CSV.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

 
    private void loadData(){
        DefaultTableModel model = (DefaultTableModel) TbKategori.getModel();
        model.setRowCount(0);
        
        String sql = "SELECT * FROM kontak";
        
        try (Connection conn = DatabaseHelper.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("nomor_telepon"),
                    rs.getString("kategori")
                });
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TNama = new javax.swing.JTextField();
        TNoTelepon = new javax.swing.JTextField();
        CbKategori = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TbKategori = new javax.swing.JTable();
        BEksporData = new javax.swing.JButton();
        FcFile = new javax.swing.JFileChooser();
        BImporData = new javax.swing.JButton();
        JButton3 = new javax.swing.JButton();
        JButton4 = new javax.swing.JButton();
        JButton2 = new javax.swing.JButton();
        JButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel1.setText("Aplikasi Pengelolaan Nomor");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setText("Nama");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel3.setText("No Telepon");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Kategori");

        TNama.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        TNoTelepon.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        TNoTelepon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TNoTeleponKeyTyped(evt);
            }
        });

        CbKategori.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        CbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Keluarga", "Teman", "Rekan Kerja", "Sahabat", "Orang Asing" }));

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 397, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        TbKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Nama", "No Telepon", "Kategori"
            }
        ));
        jScrollPane1.setViewportView(TbKategori);

        BEksporData.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        BEksporData.setText("Ekspor Data");
        BEksporData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BEksporDataActionPerformed(evt);
            }
        });

        BImporData.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        BImporData.setText("Impor Data");
        BImporData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BImporDataActionPerformed(evt);
            }
        });

        JButton3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        JButton3.setText("Tambah");
        JButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton3ActionPerformed(evt);
            }
        });

        JButton4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        JButton4.setText("Edit");
        JButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton4ActionPerformed(evt);
            }
        });

        JButton2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        JButton2.setText("Hapus");
        JButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton2ActionPerformed(evt);
            }
        });

        JButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        JButton1.setText("Cari");
        JButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FcFile, javax.swing.GroupLayout.DEFAULT_SIZE, 1241, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                        .addComponent(TNama, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(37, 37, 37)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(CbKategori, 0, 267, Short.MAX_VALUE)
                                            .addComponent(TNoTelepon))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BEksporData)
                            .addComponent(BImporData))
                        .addGap(33, 33, 33)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(JButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(JButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(JButton3)
                .addGap(73, 73, 73)
                .addComponent(JButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TNoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(CbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BEksporData)
                        .addGap(26, 26, 26)
                        .addComponent(BImporData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JButton2)
                    .addComponent(JButton3)
                    .addComponent(JButton4)
                    .addComponent(JButton1))
                .addGap(135, 135, 135)
                .addComponent(FcFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton3ActionPerformed
        // TODO add your handling code here:
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();
        
        String sql = "INSERT INTO kontak(nama, nomor_telepon, kategori) VALUES(?,?,?)";
        
            try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, nama);
                pstmt.setString(2, nomor);
                pstmt.setString(3, kategori);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan");
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
        }
    }//GEN-LAST:event_JButton3ActionPerformed

    private void JButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton4ActionPerformed
        // TODO add your handling code here:
        eksporKeCSV();
        int selectedRow = TbKategori.getSelectedRow();
        if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin diedit");
        return;
    }
        int id = (int) model.getValueAt(selectedRow, 0);
        String nama = TNama.getText();
        String nomor = TNoTelepon.getText();
        String kategori = CbKategori.getSelectedItem().toString();
        
        String sql = "UPDATE kontak SET nama = ?, nomor_telepon = ?, kategori = ? WHERE id = ?";
        
            try (Connection conn = DatabaseHelper.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nama);
                pstmt.setString(2, nomor);
                pstmt.setString(3, kategori);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Kontak berhasil diperbarui");
                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
        }
    }//GEN-LAST:event_JButton4ActionPerformed

    private void JButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton2ActionPerformed
        // TODO add your handling code here:
        int selectedRow = TbKategori.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin dihapus");
        return;
    }
    int id = (int) model.getValueAt(selectedRow, 0);

    String sql = "DELETE FROM kontak WHERE id = ?";
    
        try (Connection conn = DatabaseHelper.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kontak berhasil dihapus");
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_JButton2ActionPerformed

    private void JButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton1ActionPerformed
        // TODO add your handling code here:
        String keyword = TNama.getText();
        model.setRowCount(0);
        String sql = "SELECT * FROM kontak WHERE nama LIKE ? OR nomor_telepon LIKE ?";
        
        try (Connection conn = DatabaseHelper.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, "%" + keyword + "%");
        pstmt.setString(2, "%" + keyword + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nama"),
                rs.getString("nomor_telepon"),
                rs.getString("kategori")
            });
        }
        } catch (SQLException e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_JButton1ActionPerformed

    private void TNoTeleponKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoTeleponKeyTyped
        char karakter = evt.getKeyChar();
            if (!Character.isDigit(karakter)) {
                evt.consume();
        }

            if (TNoTelepon.getText().length() >= 13) {
            evt.consume(); 
        }
    }//GEN-LAST:event_TNoTeleponKeyTyped

    private void BEksporDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BEksporDataActionPerformed
        eksporKeCSV();
    }//GEN-LAST:event_BEksporDataActionPerformed

    private void BImporDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BImporDataActionPerformed
        imporDariCSV();
    }//GEN-LAST:event_BImporDataActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AplikasiPengelolaanNomorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplikasiPengelolaanNomorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BEksporData;
    private javax.swing.JButton BImporData;
    private javax.swing.JComboBox<String> CbKategori;
    private javax.swing.JFileChooser FcFile;
    private javax.swing.JButton JButton1;
    private javax.swing.JButton JButton2;
    private javax.swing.JButton JButton3;
    private javax.swing.JButton JButton4;
    private javax.swing.JTextField TNama;
    private javax.swing.JTextField TNoTelepon;
    private javax.swing.JTable TbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
