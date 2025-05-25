/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.border.EmptyBorder;

/**
 * @author Windows
 */
public class mainActivityView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JList<String> menuList;
    private DefaultListModel<String> menuModel;

    private final String[] menuItems = {
        "🏠 Trang Chủ",
        "👤 Hồ Sơ Nhân Sự",
        "🏢 Phòng Ban",
        "💼 Chức Vụ",
        "💰 Lương & Phụ Cấp",
        "📝 Hợp Đồng",
        "🕒 Nghỉ Phép & Nghỉ Việc"
    };

    private final HashMap<String, JPanel> contentPanels = new HashMap<>();

    public mainActivityView() {
        setTitle("Hệ Thống Quản Lý Hồ Sơ Nhân Sự");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(33, 150, 243));
        header.setPreferredSize(new Dimension(getWidth(), 50));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);
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
        JLabel menuTitle = new JLabel("  Quản lý:");
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

        // List selection listener
        menuList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = menuList.getSelectedValue().trim();
                cardLayout.show(mainContentPanel, selected);
                menuList.repaint(); // cập nhật màu sắc
            }
        });

        // Mặc định chọn tab đầu tiên
        menuList.setSelectedIndex(0);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new mainActivityView().setVisible(true);
        });
    }
}
