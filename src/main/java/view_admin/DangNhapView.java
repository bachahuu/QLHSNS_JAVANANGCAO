/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_admin;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import controller.TaiKhoanController;
import model.TaiKhoanModel;

public class DangNhapView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private TaiKhoanController taiKhoanController;

    public DangNhapView() {
        // Cấu hình cửa sổ
        setTitle("Đăng nhập");
        setSize(400, 300); // Tăng kích thước cho giao diện đẹp hơn
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa màn hình
        setResizable(false);

        // Khởi tạo controller
        taiKhoanController = new TaiKhoanController();

        // Tạo panel chính với background gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(144, 238, 144));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Tăng khoảng cách giữa các thành phần

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đăng nhập vào hệ thống");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Nhãn và trường nhập liệu
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 1));

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(144, 238, 144), 1));

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Nút đăng nhập và hủy với hiệu ứng hover
        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 153, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 180, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 153, 0));
            }
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(255, 51, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(204, 0, 0));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);

        // Thêm panel vào frame
        add(mainPanel);

        // Xử lý sự kiện nút Đăng nhập
        // Xử lý sự kiện nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(DangNhapView.this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TaiKhoanModel taiKhoan = taiKhoanController.getByTenDangNhap(username);
                if (taiKhoan != null && taiKhoan.getMatKhau() != null) {

                    // BỔ SUNG KIỂM TRA TRẠNG THÁI TÀI KHOẢN
                    if (!"Hoat_dong".equalsIgnoreCase(taiKhoan.getTrangThai())) {
                        JOptionPane.showMessageDialog(DangNhapView.this, "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên!", "Tài khoản bị khóa", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        // Giải mã Base64 để lấy salt và hash đã lưu
                        byte[] saltAndHash = Base64.getDecoder().decode(taiKhoan.getMatKhau());
                        byte[] salt = new byte[16];
                        System.arraycopy(saltAndHash, 0, salt, 0, 16);
                        byte[] storedHash = new byte[saltAndHash.length - 16];
                        System.arraycopy(saltAndHash, 16, storedHash, 0, storedHash.length);

                        // Tái tạo hash từ password nhập vào với salt đã lưu
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        digest.update(salt);
                        byte[] inputHashedBytes = digest.digest(password.getBytes());

                        // So sánh hash
                        if (MessageDigest.isEqual(storedHash, inputHashedBytes)) {
                            dispose(); // Đóng form đăng nhập
                            String vaiTro = taiKhoan.getVaiTro();
                            if ("Quan_tri".equals(vaiTro)) {
                                new mainActivityView().setVisible(true);
                            } else if ("Nhan_vien".equals(vaiTro)) {
                                new view_nhanvien.NhanvienView(taiKhoan.getMaNhanVien()).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(DangNhapView.this, "Vai trò không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(DangNhapView.this, "Mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NoSuchAlgorithmException ex) {
                        JOptionPane.showMessageDialog(DangNhapView.this, "Lỗi mã hóa mật khẩu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(DangNhapView.this, "Tên đăng nhập không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Xử lý sự kiện nút Hủy
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
                dispose(); // Đóng form
            }
        });
    }

    // Phương thức main để chạy form
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DangNhapView dangNhapView = new DangNhapView();
            dangNhapView.setVisible(true);
        });
    }
}
