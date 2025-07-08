/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_nhanvien;

import controller.nhanSuController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.ContractModel;
import model.NhanSuModel;
import view_admin.DangNhapView;
/**
 * @author Windows
 */
public class NhanvienView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JList<String> menuList;
    private DefaultListModel<String> menuModel;
    private int maNhanVien;
    private NhanSuModel nhanVien;
    private ContractModel hopDong;
    private final String[] menuItems = {
        "👤 Thông Tin Cá Nhân",
        "💰 Lương & Phụ Cấp"
    };

    private final HashMap<String, JPanel> contentPanels = new HashMap<>();

    public NhanvienView(int maNhanVien) {
        this.maNhanVien = maNhanVien;
        nhanSuController controller = new nhanSuController();
        this.nhanVien = controller.getById(maNhanVien);
        this.hopDong = controller.getHopDongByNhanVien(maNhanVien);
        setTitle("Hệ Thống Quản Lý Hồ Sơ Nhân Sự");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        
        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(33, 150, 243));
        header.setPreferredSize(new Dimension(getWidth(), 50));
        header.setLayout(new BorderLayout()); // Sử dụng BorderLayout để căn chỉnh
        // Tiêu đề
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);
        
       // Nút đăng xuất
        JButton logoutButton = new JButton();
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logout.png"));
        if (icon.getImageLoadStatus() == MediaTracker.ERRORED || icon.getImage() == null) {
            System.out.println("Không thể tải file ảnh: /images/logout.png, sử dụng biểu tượng Unicode");
            logoutButton.setText("🡦 Đăng Xuất"); // Dự phòng bằng Unicode
        } else {
            // Điều chỉnh kích thước icon để phù hợp với nút
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Kích thước 16x16
            icon = new ImageIcon(img);
            logoutButton.setIcon(icon);
            logoutButton.setText("Đăng Xuất"); // Đảm bảo text hiển thị
            logoutButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text bên phải icon
            logoutButton.setIconTextGap(5); // Khoảng cách giữa icon và text
        }
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(33, 150, 243)); // Cùng màu nền header
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Viền trắng
        logoutButton.setFocusPainted(false); // Bỏ viền focus mặc định
        logoutButton.setPreferredSize(new Dimension(120, 30));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ tay khi hover
        // Hiệu ứng hover
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(Color.WHITE); // Đổi nền khi hover
                logoutButton.setForeground(new Color(33, 150, 243)); // Đổi màu chữ
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(33, 150, 243)); // Trở lại màu ban đầu
                logoutButton.setForeground(Color.WHITE);
            }
        });
        // Xử lý sự kiện đăng xuất
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                NhanvienView.this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("=== ĐĂNG XUẤT ===");
                new DangNhapView().setVisible(true);
                dispose();
            }
        });
        // Đặt nút vào góc phải
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // Trong suốt để hòa hợp với header
        buttonPanel.add(logoutButton);
        header.add(buttonPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);


        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(66, 66, 66));
        footer.setPreferredSize(new Dimension(getWidth(), 30));
        JLabel footerLabel = new JLabel("Hệ thống quản lý nhân sự");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        // Menu (List)
        menuModel = new DefaultListModel<>();
        for (String item : menuItems) menuModel.addElement(item);

        menuList = new JList<>(menuModel);
        menuList.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setBackground(new Color(33, 150, 243));
        menuList.setForeground(Color.WHITE);
        menuList.setFixedCellHeight(50);
        menuList.setCellRenderer(new MenuRenderer());

        JPanel menuPanel = new JPanel(new BorderLayout());
        // Hiển thị "Nhân Sự: Tên Nhân Sự"
        String tenNhanSu = (nhanVien != null && nhanVien.getHoTen() != null) ? nhanVien.getHoTen() : "Không xác định";
        JLabel menuTitle = new JLabel("Nhân Sự: " + tenNhanSu);
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menuTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.add(menuTitle, BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        add(menuPanel, BorderLayout.WEST);

        // Content panels with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        for (String item : menuItems) {
            String key = item.trim();
            JPanel panel = createPanel("Chào mừng đến với " + item.substring(2));
            contentPanels.put(key, panel);
            mainContentPanel.add(panel, key);
        }
        add(mainContentPanel, BorderLayout.CENTER);

        // List selection listener với xử lý sự kiện riêng cho từng tab
        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = menuList.getSelectedValue();
                if (selected != null) {
                    String key = selected.trim();
                    
                    // Chuyển tab
                    cardLayout.show(mainContentPanel, key);
                    
                    // Xử lý sự kiện riêng cho từng tab
                    handleTabSelection(selected);
                    
                    menuList.repaint(); // cập nhật màu sắc
                }
            }
        });

        // Mặc định chọn tab đầu tiên
        menuList.setSelectedIndex(0);
    }

    // Phương thức xử lý sự kiện cho từng tab
    private void handleTabSelection(String selectedTab)  {
        System.out.println("Đã chuyển đến tab: " + selectedTab);
        
        switch (selectedTab.trim()) {
            case "👤 Thông Tin Cá Nhân": // Thêm xử lý cho Quản Lý Tài Khoản
                handleAccountTab();
                break;
            case "💰 Lương & Phụ Cấp":
                handleSalaryTab();
                break;
            default:
                System.out.println("Tab không xác định: " + selectedTab);
        }
    }

    // Các phương thức xử lý riêng cho từng tab
    private void handleAccountTab() {
        System.out.println("=== XỬ LÝ TAB Thông Tin Cá Nhân ===");
        String key = "👤 Thông Tin Cá Nhân".trim();

        ProfileView employeeProfilePanel = new ProfileView(nhanVien, hopDong);
        contentPanels.put(key, employeeProfilePanel); // Cập nhật panel cho tab này
        mainContentPanel.removeAll(); // Xóa các panel cũ
        for (String item : menuItems) {
            String panelKey = item.trim();
            mainContentPanel.add(contentPanels.getOrDefault(panelKey, createPanel("Chào mừng đến với " + item.substring(2))), panelKey);
        }
        cardLayout.show(mainContentPanel, key); // Hiển thị panel mới
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }


    private void handleSalaryTab()  {
        System.out.println("=== XỬ LÝ TAB LƯƠNG ===");
        String key = "💰 Lương & Phụ Cấp".trim();
        ProfileLuongView luong = new ProfileLuongView(maNhanVien);
        contentPanels.put(key, luong);
        mainContentPanel.removeAll();
        for (String item : menuItems) {
            String panelKey = item.trim();
            mainContentPanel.add(contentPanels.getOrDefault(panelKey, createPanel("Chào mừng đến với " + item.substring(2))), panelKey);
        }
        cardLayout.show(mainContentPanel, key);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel createPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        panel.add(label, BorderLayout.CENTER);
        panel.setBackground(Color.WHITE);
        return panel;
    }



    // Tùy chỉnh màu sắc item menu
    private class MenuRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

            if (isSelected) {
                label.setBackground(Color.WHITE);
                label.setForeground(new Color(33, 150, 243));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setBackground(new Color(33, 150, 243));
                label.setForeground(Color.WHITE);
            }

            return label;
        }
    }

    
    // Phương thức công khai để lấy panel hiện tại (nếu cần)
    public JPanel getCurrentPanel() {
        String selectedItem = menuList.getSelectedValue();
        if (selectedItem != null) {
            return contentPanels.get(selectedItem.trim());
        }
        return null;
    }
    
    // Phương thức để chuyển tab từ code
    public void switchToTab(String tabName) {
        for (int i = 0; i < menuModel.getSize(); i++) {
            String item = menuModel.getElementAt(i);
            if (item.contains(tabName)) {
                menuList.setSelectedIndex(i);
                break;
            }
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            int maNhanVien = 1;
//            new NhanvienView(maNhanVien).setVisible(true);
//        });
//    }
}