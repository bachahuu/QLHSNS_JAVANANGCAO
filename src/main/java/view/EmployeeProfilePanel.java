/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author Grok
 */
public class EmployeeProfilePanel extends JPanel {
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private JComboBox<String> positionFilter;
    private JComboBox<String> statusFilter;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton;

    public EmployeeProfilePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchField = new JTextField("Tìm kiếm nhân sự...");
        searchField.setForeground(Color.GRAY);

        // Load search icon
        URL searchUrl = getClass().getResource("/images/search.png");
        if (searchUrl == null) {
            System.err.println("Không tìm thấy icon: /images/search.png");
        }
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);

        // Thêm biểu tượng vào bên trái của JTextField
        searchField.setBorder(BorderFactory.createCompoundBorder(
            searchField.getBorder(),
            BorderFactory.createEmptyBorder(0, 25, 0, 0) // Tạo khoảng cách bên trái để chứa icon
        ));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and add button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        departmentFilter = new JComboBox<>(new String[]{"Tất cả phòng ban", "Ban Lãnh đạo", "Phòng Kinh doanh", "Phòng Nhân sự"});
        positionFilter = new JComboBox<>(new String[]{"Tất cả chức vụ", "Giám đốc", "Nhân viên", "Trưởng phòng"});
        statusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang làm việc", "Đã nghỉ việc"});

        filterPanel.add(departmentFilter);
        filterPanel.add(positionFilter);
        filterPanel.add(statusFilter);

        addButton = new JButton("Thêm nhân sự");
        addButton.setBackground(new Color(103, 65, 217));
        addButton.setForeground(Color.WHITE);
        filterPanel.add(addButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table for employee data
        String[] columnNames = {"Mã NV", "Họ và tên","Giới Tính", "Chức vụ", "Phòng ban", "Thông tin liên hệ", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cột "Thao tác" có thể chỉnh sửa (các nút)
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) return JPanel.class; // For action buttons
                return String.class;
            }
        };

        // Sample data (replace with actual data in a real application)
        addSampleData();

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(50);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(50); // Avatar column width
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Actions column width

        // Áp dụng renderer và editor cho cột "Thao tác"
        employeeTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        employeeTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Pagination panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JButton prevButton = new JButton("<");
        JLabel pageLabel = new JLabel("1");
        JButton nextButton = new JButton(">");
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void addSampleData() {
        // Sample data for the table
        Object[][] data = {
            {"NV001","Hà Hữu Bắc" , "Nam", "Giám đốc", "Ban Lãnh đạo", "SDT: 09... \nEmail: A1@gmail.com", "Đang làm việc", null},
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    // Phương thức chung để tạo panel chứa các nút
    private JPanel createButtonPanel(boolean withListeners, ActionListener viewListener, ActionListener editListener, ActionListener deleteListener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);

// Nút View (xanh lam sáng)
        URL viewUrl = getClass().getResource("/images/eye.png");
        if (viewUrl == null) {
            System.err.println("Không tìm thấy icon: /images/eye.png");
        }
        ImageIcon viewIcon = (viewUrl != null) ? resizeIcon(new ImageIcon(viewUrl), 20, 20) : new ImageIcon();
        JButton viewButton = new JButton(viewIcon);
//        viewButton.setBackground(new Color(100, 181, 246)); // Xanh lam sáng
        viewButton.setForeground(Color.WHITE);
        viewButton.setPreferredSize(new Dimension(30, 30));

        // Nút Edit (xanh lá sáng)
        URL editUrl = getClass().getResource("/images/edit.png");
        if (editUrl == null) {
            System.err.println("Không tìm thấy icon: /images/edit.png");
        }
        ImageIcon editIcon = (editUrl != null) ? resizeIcon(new ImageIcon(editUrl), 20, 20) : new ImageIcon();
        JButton editButton = new JButton(editIcon);
//        editButton.setBackground(new Color(105, 240, 174)); // Xanh lá sáng
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(30, 30));

        // Nút Delete (đỏ nhạt sáng)
        URL deleteUrl = getClass().getResource("/images/delete.png");
        if (deleteUrl == null) {
            System.err.println("Không tìm thấy icon: /images/delete.png");
        }
        ImageIcon deleteIcon = (deleteUrl != null) ? resizeIcon(new ImageIcon(deleteUrl), 20, 20) : new ImageIcon();
        JButton deleteButton = new JButton(deleteIcon);
//        deleteButton.setBackground(new Color(255, 138, 128)); // Đỏ nhạt sáng
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(30, 30));

        // Gắn sự kiện nếu cần (chỉ cho editor)
        if (withListeners) {
            viewButton.addActionListener(viewListener);
            editButton.addActionListener(editListener);
            deleteButton.addActionListener(deleteListener);
        }

        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        return panel;
    }
    // Phương thức để thay đổi kích thước icon
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon != null) {
            Image img = icon.getImage();
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(img, 0, 0, width, height, null);
            g2d.dispose();
            return new ImageIcon(resizedImg);
        }
        return icon; // Trả về icon gốc nếu không thể resize
    }

  // Renderer cho cột "Thao tác"
    class ButtonRenderer implements TableCellRenderer {
        private JPanel panel;

        public ButtonRenderer() {
            panel = createButtonPanel(false, null, null, null);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }
    }

    // Editor cho cột "Thao tác"
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            panel = createButtonPanel(true,
                e -> {
                    JOptionPane.showMessageDialog(null, "Xem thông tin nhân viên: " + tableModel.getValueAt(row, 0));
                    fireEditingStopped();
                },
                e -> {
                    JOptionPane.showMessageDialog(null, "Chỉnh sửa nhân viên: " + tableModel.getValueAt(row, 0));
                    fireEditingStopped();
                },
                e -> {
                    JOptionPane.showMessageDialog(null, "Xóa nhân viên: " + tableModel.getValueAt(row, 0));
                    fireEditingStopped();
                }
            );
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
 