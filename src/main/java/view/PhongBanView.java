/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import controller.PhongBanController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.PhongBanModel;

/**
 *
 * @author Windows
 */
public class PhongBanView extends JPanel{
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JTable phongbanTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private PhongBanController phongban;
    private PhongBanModel selectedPhongBan;
    public PhongBanView(){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        phongban = new PhongBanController();

        // Top panel for search and filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(Color.WHITE);

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchField = new JTextField("Tìm kiếm Phòng ban...");
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
        statusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Hoat_dong", "Ngung_hoat_dongc"});

        filterPanel.add(statusFilter);

        addButton = new JButton("Thêm Phòng Ban");
        addButton.setBackground(new Color(103, 65, 217));
        addButton.setForeground(Color.WHITE);
        filterPanel.add(addButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table for employee data
        String[] columnNames = {"Mã PB", "Tên phòng ban","mô tả", "Số Nhân Viên", "Ngày Thành Lập","Trạng thái","Ngày Tạo", "Thao tác"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cột "Thao tác" có thể chỉnh sửa (các nút)
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class; // Cột Mã PB là int
                if (columnIndex == 7) return JPanel.class; // For action buttons
                return String.class;
            }
        };
        //fill dữ liệu từ database
        loadDataFromDatabase();
        // Sample data (replace with actual data in a real application)
//        setupAddButtonListener();

        phongbanTable = new JTable(tableModel);
        phongbanTable.setRowHeight(50);
        phongbanTable.getColumnModel().getColumn(1).setPreferredWidth(50); // Avatar column width
        phongbanTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Actions column width

        // Áp dụng renderer và editor cho cột "Thao tác"
        phongbanTable.getColumnModel().getColumn(7).setCellRenderer(new PhongBanView.ButtonRenderer());
        phongbanTable.getColumnModel().getColumn(7).setCellEditor(new PhongBanView.ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(phongbanTable);
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
    private void loadDataFromDatabase(){
       phongban = new PhongBanController();
       List<PhongBanModel> phongBanList = phongban.getAll();
       if (phongBanList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu hoặc lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
       }
         for (PhongBanModel phongBan : phongBanList) {
            Object[] row = {
                phongBan.getMaPhongBan(),
                phongBan.getTenPhongBan(),
                phongBan.getMoTa(),
                phongBan.getSoNhanVien(),
                phongBan.getNgayThanhLap() != null ? phongBan.getNgayThanhLap().toString() : "",
                phongBan.getTrangThai(),
                phongBan.getNgayTao() != null ? phongBan.getNgayTao().toString() : "",
                null
            };
            tableModel.addRow(row);
        }
    }
    // Method để refresh lại dữ liệu bảng
    private void refreshTableData() {
        // Xóa tất cả dữ liệu hiện tại
        tableModel.setRowCount(0);
        
        // Load lại dữ liệu từ database
        loadDataFromDatabase();
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
                e -> showDetailDialog(row),
                e -> editDetailDialog(row),
                e -> {
                            // Lấy mã số từ cột 0
                        int maPhongBan = (int) tableModel.getValueAt(row, 0);

                        // Tìm NhanSuModel tương ứng
                        List<PhongBanModel> phongBanList = phongban.getAll();
                        selectedPhongBan = null;
                        for (PhongBanModel phongBan : phongBanList) {
                            if (phongBan.getMaPhongBan() == maPhongBan) {
                                selectedPhongBan = phongBan;
                                break;
                            }
                        }

                        if (selectedPhongBan == null) {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            fireEditingStopped();
                            return;
                        }

                        // Xác nhận xóa
                        int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Bạn có chắc muốn xóa nhân viên: " + selectedPhongBan.getTenPhongBan() + " (Mã: " + maPhongBan + ")?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                // Gọi hàm delete
                                phongban.delete(selectedPhongBan.getMaPhongBan());
                                JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                                refreshTableData(); // Làm mới bảng
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                        }

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
        private void showDetailDialog(int row){
             // Lấy mã phong ban từ cột 0
            Object maPhongBanObj = tableModel.getValueAt(row, 0);
            int maPhongBan = (maPhongBanObj instanceof Integer) ? (int) maPhongBanObj : Integer.parseInt(maPhongBanObj.toString());
            List<PhongBanModel> phongBanList = phongban.getAll();
            if (phongBanList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không có dữ liệu phòng ban hoặc lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedPhongBan = null;
            for (PhongBanModel phongBan : phongBanList) {
                if (phongBan.getMaPhongBan() == maPhongBan) {
                    selectedPhongBan = phongBan;
                    break;
                }
            }

            if (selectedPhongBan == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin phòng ban!");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(PhongBanView.this), "Chi tiết Phòng Ban", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(PhongBanView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maPhongBanLabel = new JLabel("Mã Phòng Ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            JTextField maPhongBanField = new JTextField(String.valueOf(selectedPhongBan.getMaPhongBan()));
            maPhongBanField.setFont(fieldFont);
            maPhongBanField.setEditable(false);
            dialog.add(maPhongBanField);

            JLabel tenphongbanLabel = new JLabel("Tên Phòng Ban:");
            tenphongbanLabel.setFont(labelFont);
            dialog.add(tenphongbanLabel);
            JTextField tenphongbanField = new JTextField(selectedPhongBan.getTenPhongBan());
            tenphongbanField.setFont(fieldFont);
            tenphongbanField.setEditable(false);
            dialog.add(tenphongbanField);

            JLabel moTaLabel = new JLabel("Mô tả:");
            moTaLabel.setFont(labelFont);
            dialog.add(moTaLabel);
            JTextField moTaField = new JTextField(selectedPhongBan.getMoTa());
            moTaField.setFont(fieldFont);
            moTaField.setEditable(false);
            dialog.add(moTaField);
            
            JLabel soThanhVienLabel = new JLabel("Số Thành Viên:");
            soThanhVienLabel.setFont(labelFont);
            dialog.add(soThanhVienLabel);
            JTextField soThanhVienField = new JTextField(String.valueOf(selectedPhongBan.getSoNhanVien()));
            soThanhVienField.setFont(fieldFont);
            soThanhVienField.setEditable(false);
            dialog.add(soThanhVienField);

            JLabel ngayThanhLapLabel = new JLabel("Ngày Thành Lập:");
            ngayThanhLapLabel.setFont(labelFont);
            dialog.add(ngayThanhLapLabel);
            JTextField ngayThanhLapField = new JTextField(selectedPhongBan.getNgayThanhLap() != null ? selectedPhongBan.getNgayThanhLap().toString() : "");
            ngayThanhLapField.setFont(fieldFont);
            ngayThanhLapField.setEditable(false);
            dialog.add(ngayThanhLapField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            JTextField tinhTrangField = new JTextField(selectedPhongBan.getTrangThai().toString());
            tinhTrangField.setFont(fieldFont);
            tinhTrangField.setEditable(false);
            dialog.add(tinhTrangField);
            
            JLabel ngayTaoLabel = new JLabel("Ngày Tạo:");
            ngayTaoLabel.setFont(labelFont);
            dialog.add(ngayTaoLabel);
            JTextField ngayTaoField = new JTextField(selectedPhongBan.getNgayTao() != null ? selectedPhongBan.getNgayTao().toString() : "");
            ngayTaoField.setFont(fieldFont);
            ngayTaoField.setEditable(false);
            dialog.add(ngayTaoField);

            // Nút Quay trở lại
            JLabel placeholderLabel = new JLabel(""); // Placeholder để căn giữa nút
            dialog.add(placeholderLabel);
            JButton backButton = new JButton("Quay trở lại");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Tăng font cho nút
            backButton.addActionListener(e -> dialog.dispose());
            dialog.add(backButton);

            // Hiển thị dialog
            dialog.setVisible(true);
        }
        
        //editdialog
        private void editDetailDialog(int row){
             // Lấy mã nhân viên từ cột 0
            Object maPhongBanObj = tableModel.getValueAt(row, 0);
            int maPhongBan = (maPhongBanObj instanceof Integer) ? (int) maPhongBanObj : Integer.parseInt(maPhongBanObj.toString());
            List<PhongBanModel> phongBanList = phongban.getAll();
            selectedPhongBan = null;
            for (PhongBanModel phongBan : phongBanList) {
                if (phongBan.getMaPhongBan() == maPhongBan) {
                    selectedPhongBan = phongBan;
                    break;
                }
            }

            if (selectedPhongBan == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin Phòng Ban !");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(PhongBanView.this), "Chi tiết Phòng Ban", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(PhongBanView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maPhongBanLabel = new JLabel("Mã Phòng Ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            JTextField maPhongBanField = new JTextField(String.valueOf(selectedPhongBan.getMaPhongBan()));
            maPhongBanField.setFont(fieldFont);
            maPhongBanField.setEditable(false);
            dialog.add(maPhongBanField);

            JLabel tenphongbanLabel = new JLabel("Tên Phòng Ban:");
            tenphongbanLabel.setFont(labelFont);
            dialog.add(tenphongbanLabel);
            JTextField tenphongbanField = new JTextField(selectedPhongBan.getTenPhongBan());
            tenphongbanField.setFont(fieldFont);
            tenphongbanField.setEditable(true);
            dialog.add(tenphongbanField);

            JLabel moTaLabel = new JLabel("Mô tả:");
            moTaLabel.setFont(labelFont);
            dialog.add(moTaLabel);
            JTextField moTaField = new JTextField(selectedPhongBan.getMoTa());
            moTaField.setFont(fieldFont);
            moTaField.setEditable(true);
            dialog.add(moTaField);
            
            JLabel soThanhVienLabel = new JLabel("Số Thành Viên:");
            soThanhVienLabel.setFont(labelFont);
            dialog.add(soThanhVienLabel);
            JTextField soThanhVienField = new JTextField(String.valueOf(selectedPhongBan.getSoNhanVien()));
            soThanhVienField.setFont(fieldFont);
            soThanhVienField.setEditable(true);
            dialog.add(soThanhVienField);

            JLabel ngayThanhLapLabel = new JLabel("Ngày Thành Lập:");
            ngayThanhLapLabel.setFont(labelFont);
            dialog.add(ngayThanhLapLabel);
            JTextField ngayThanhLapField = new JTextField(selectedPhongBan.getNgayThanhLap() != null ? selectedPhongBan.getNgayThanhLap().toString() : "");
            ngayThanhLapField.setFont(fieldFont);
            ngayThanhLapField.setEditable(true);
            dialog.add(ngayThanhLapField);

           JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            String[] tinhTrangOptions = {"Hoat_dong", "Ngung_hoat_dong"};
            JComboBox<String> tinhTrangComboBox = new JComboBox<>(tinhTrangOptions);
            tinhTrangComboBox.setFont(fieldFont);
            tinhTrangComboBox.setSelectedItem(selectedPhongBan.getTrangThai().toString());
            dialog.add(tinhTrangComboBox);
            
            JLabel ngayTaoLabel = new JLabel("Ngày Tạo:");
            ngayTaoLabel.setFont(labelFont);
            dialog.add(ngayTaoLabel);
            JTextField ngayTaoField = new JTextField(selectedPhongBan.getNgayTao() != null ? selectedPhongBan.getNgayTao().toString() : "");
            ngayTaoField.setFont(fieldFont);
            ngayTaoField.setEditable(true);
            dialog.add(ngayTaoField);
            
            JButton saveButton = new JButton("Lưu thay đổi");
            saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
            saveButton.addActionListener(e ->{
                 try {
                        String tenPhongBan = tenphongbanField.getText().trim();
                        String moTa = moTaField.getText().trim();
                        String soThanhVienText = soThanhVienField.getText().trim();
                        String ngayThanhLap = ngayThanhLapField.getText().trim();
                        String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();
                        String ngayTao = ngayTaoField.getText().trim();

                        if (tenPhongBan.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        int soThanhVien;
                        try {
                            soThanhVien = Integer.parseInt(soThanhVienText);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog, "Số thành viên phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        PhongBanModel updatedPhongBan = new PhongBanModel();
                        updatedPhongBan.setMaPhongBan(selectedPhongBan.getMaPhongBan());
                        updatedPhongBan.setTenPhongBan(tenPhongBan);
                        updatedPhongBan.setMoTa(moTa.isEmpty() ? null : moTa);
                        updatedPhongBan.setSoNhanVien(soThanhVien);
                        updatedPhongBan.setTrangThai(PhongBanModel.TrangThaiPhongBan.valueOf(tinhTrang));

                        if (!ngayThanhLap.isEmpty()) {
                            try {
                                java.sql.Date parsedNgayThanhLap = java.sql.Date.valueOf(ngayThanhLap);
                                updatedPhongBan.setNgayThanhLap(parsedNgayThanhLap);
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(dialog, "Ngày Thành Lập không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            updatedPhongBan.setNgayThanhLap(null);
                        }

                        if (!ngayTao.isEmpty()) {
                            try {
                                java.sql.Timestamp parsedNgayTao = java.sql.Timestamp.valueOf(ngayTao);
                                updatedPhongBan.setNgayTao(parsedNgayTao);
                            } catch (IllegalArgumentException ex) {
                                JOptionPane.showMessageDialog(dialog, "Ngày Tạo không đúng định dạng (yyyy-MM-dd HH:mm:ss)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            updatedPhongBan.setNgayTao(selectedPhongBan.getNgayTao());
                        }

                        boolean success = phongban.updatePhongBan(updatedPhongBan);
                        if (success) {
                            JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin phòng ban thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            refreshTableData();
                            dialog.dispose();
                            fireEditingStopped();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
            });
            dialog.add(saveButton);

            // Nút Quay trở lại
            JButton backButton = new JButton("Quay trở lại");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16));
            backButton.addActionListener(e -> dialog.dispose());
            dialog.add(backButton);

            // Hiển thị dialog
            dialog.setVisible(true);
        }
        
    }
    
}
