/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ChucVuController;
import controller.PhongBanController;
import controller.nhanSuController;
import controller.TaiKhoanController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import model.ContractModel;
import model.NhanSuModel;
import model.NhanVienDTO;
import model.TaiKhoanModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Admin
 */
//import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.*;
//import org.apache.poi.ss.util.*;

public class TaiKhoanView extends JPanel {
    private JTextField searchField;
    private JComboBox<String> roleFilter;
    private JComboBox<String> statusFilter;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private TaiKhoanController taiKhoanController;
    private TaiKhoanModel selectedTaiKhoan;

    public TaiKhoanView() {
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

        searchField = new JTextField();
        searchField.setForeground(Color.GRAY);
        searchField.setText("Tìm kiếm tài khoản...");
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Tìm kiếm tài khoản...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Tìm kiếm tài khoản...");
                }
            }
        });

        URL searchUrl = getClass().getResource("/images/search.png");
        ImageIcon searchIcon = (searchUrl != null) ? resizeIcon(new ImageIcon(searchUrl), 25, 25) : new ImageIcon();
        JLabel searchLabel = new JLabel(searchIcon);
        searchLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = searchField.getText().trim();
                if (searchText.equals("Tìm kiếm tài khoản...")) {
                    searchText = "";
                }
                searchTaiKhoan(searchText);
            }
        });

        searchField.setBorder(BorderFactory.createCompoundBorder(
            searchField.getBorder(),
            BorderFactory.createEmptyBorder(0, 25, 0, 0)
        ));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.WEST);

        // Filter and add button panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        roleFilter = new JComboBox<>(new String[]{"Tất cả vai trò", "Quan_tri", "Nhan_vien"});
        statusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Hoat_dong", "Bi_khoa"});
        filterPanel.add(roleFilter);
        filterPanel.add(statusFilter);

        addButton = new JButton("Thêm tài khoản");
        addButton.setBackground(new Color(103, 65, 217));
        addButton.setForeground(Color.WHITE);
        filterPanel.add(addButton);

        JButton exportExcelButton = new JButton("Xuất Excel");
        exportExcelButton.setBackground(new Color(46, 204, 113));
        exportExcelButton.setForeground(Color.WHITE);
        URL excelUrl = getClass().getResource("/images/export.png");
        ImageIcon excelIcon = (excelUrl != null) ? resizeIcon(new ImageIcon(excelUrl), 20, 20) : new ImageIcon();
        exportExcelButton.setIcon(excelIcon);
        exportExcelButton.addActionListener(e -> {
            exportExcelButton.setEnabled(false);
            try {
                exportToExcel();
            } finally {
                exportExcelButton.setEnabled(true);
            }
        });
        filterPanel.add(exportExcelButton);

        topPanel.add(filterPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table for account data
        String[] columnNames = {"Mã TK", "Tên NV", "Tên đăng nhập", "Vai trò", "Trạng thái", "Thao tác"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cột "Thao tác" có thể chỉnh sửa
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return JPanel.class;
                return String.class;
            }
        };

        // Lọc dữ liệu theo vai trò và trạng thái
        roleFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedRole = roleFilter.getSelectedItem().toString();
                filterTaiKhoanByRole(selectedRole);
            }
        });
        statusFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedStatus = statusFilter.getSelectedItem().toString();
                filterTaiKhoanByStatus(selectedStatus);
            }
        });

        // Load dữ liệu từ database
        loadDataFromDatabase();

        accountTable = new JTable(tableModel);
        accountTable.setRowHeight(50);
        accountTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        accountTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        accountTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(accountTable);
        add(tableScrollPane, BorderLayout.CENTER);

        setupAddButtonListener();
    }

    private void loadDataFromDatabase() {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> taiKhoanList = taiKhoanController.getAll();
        for (TaiKhoanModel taiKhoan : taiKhoanList) {
            Object[] row = {
                taiKhoan.getMaTaiKhoan(),
                taiKhoan.getTenNhanVien(),
                taiKhoan.getTenDangNhap(),
                taiKhoan.getVaiTro(),
                taiKhoan.getTrangThai(),
                null
            };
            tableModel.addRow(row);
        }
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        loadDataFromDatabase();
    }
    
    private void searchTaiKhoan(String searchText) {
    taiKhoanController = new TaiKhoanController();
    List<TaiKhoanModel> filteredList = taiKhoanController.searchByTenDangNhapOrTenNhanVien(searchText != null ? searchText.trim() : "");
    updateTableData(filteredList);
}

    private void filterTaiKhoanByRole(String selectedRole) {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> filteredList = taiKhoanController.getByVaiTro("Tất cả vai trò".equals(selectedRole) ? null : selectedRole);
        updateTableData(filteredList);
    }

    private void filterTaiKhoanByStatus(String selectedStatus) {
        taiKhoanController = new TaiKhoanController();
        List<TaiKhoanModel> filteredList = taiKhoanController.getByTrangThai("Tất cả trạng thái".equals(selectedStatus) ? null : selectedStatus);
        updateTableData(filteredList);
    }

    // Phương thức phụ để tái sử dụng logic cập nhật bảng
    private void updateTableData(List<TaiKhoanModel> taiKhoanList) {
        tableModel.setRowCount(0);
        for (TaiKhoanModel taiKhoan : taiKhoanList) {
            Object[] row = {
                taiKhoan.getMaTaiKhoan(),
                taiKhoan.getTenNhanVien(),
                taiKhoan.getTenDangNhap(),
                taiKhoan.getVaiTro(),
                taiKhoan.getTrangThai(),
                createButtonPanel(false, null, null, null) // Giữ cột nút hành động nếu cần
            };
            tableModel.addRow(row);
        }
    }

    private void setupAddButtonListener() {
        addButton.addActionListener(e -> insertDetailDialog());
    }

    private JPanel createButtonPanel(boolean withListeners, ActionListener viewListener, ActionListener editListener, ActionListener deleteListener) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);

        URL viewUrl = getClass().getResource("/images/eye.png");
        ImageIcon viewIcon = (viewUrl != null) ? resizeIcon(new ImageIcon(viewUrl), 20, 20) : new ImageIcon();
        JButton viewButton = new JButton(viewIcon);
        viewButton.setPreferredSize(new Dimension(30, 30));

        URL editUrl = getClass().getResource("/images/edit.png");
        ImageIcon editIcon = (editUrl != null) ? resizeIcon(new ImageIcon(editUrl), 20, 20) : new ImageIcon();
        JButton editButton = new JButton(editIcon);
        editButton.setPreferredSize(new Dimension(30, 30));

        URL deleteUrl = getClass().getResource("/images/delete.png");
        ImageIcon deleteIcon = (deleteUrl != null) ? resizeIcon(new ImageIcon(deleteUrl), 20, 20) : new ImageIcon();
        JButton deleteButton = new JButton(deleteIcon);
        deleteButton.setPreferredSize(new Dimension(30, 30));

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
        return icon;
    }

    private void insertDetailDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Thêm tài khoản mới", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
        maNhanVienLabel.setFont(labelFont);
        mainPanel.add(maNhanVienLabel);

        JComboBox<NhanVienDTO> maNhanVienComboBox = new JComboBox<>();
        maNhanVienComboBox.setFont(fieldFont);
        try {
            TaiKhoanController controller = new TaiKhoanController();
            List<NhanVienDTO> nhanVienList = controller.getNhanVienList();
            for (NhanVienDTO nhanVien : nhanVienList) {
                maNhanVienComboBox.addItem(nhanVien);
            }
            if (nhanVienList.isEmpty()) {
                maNhanVienComboBox.addItem(new NhanVienDTO(0, "Không có nhân viên"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            maNhanVienComboBox.addItem(new NhanVienDTO(0, "Lỗi khi tải danh sách"));
        }
        mainPanel.add(maNhanVienComboBox);

        JLabel tenDangNhapLabel = new JLabel("Tên đăng nhập:");
        tenDangNhapLabel.setFont(labelFont);
        mainPanel.add(tenDangNhapLabel);
        JTextField tenDangNhapField = new JTextField();
        tenDangNhapField.setFont(fieldFont);
        mainPanel.add(tenDangNhapField);

        JLabel matKhauLabel = new JLabel("Mật khẩu:");
        matKhauLabel.setFont(labelFont);
        mainPanel.add(matKhauLabel);
        JPasswordField matKhauField = new JPasswordField();
        matKhauField.setFont(fieldFont);
        mainPanel.add(matKhauField);

        JLabel vaiTroLabel = new JLabel("Vai trò:");
        vaiTroLabel.setFont(labelFont);
        mainPanel.add(vaiTroLabel);
        String[] vaiTroOptions = {"Quan_tri", "Nhan_vien"};
        JComboBox<String> vaiTroComboBox = new JComboBox<>(vaiTroOptions);
        vaiTroComboBox.setFont(fieldFont);
        mainPanel.add(vaiTroComboBox);

        JLabel trangThaiLabel = new JLabel("Trạng thái:");
        trangThaiLabel.setFont(labelFont);
        mainPanel.add(trangThaiLabel);
        String[] trangThaiOptions = {"Hoat_dong", "Bi_khoa"};
        JComboBox<String> trangThaiComboBox = new JComboBox<>(trangThaiOptions);
        trangThaiComboBox.setFont(fieldFont);
        trangThaiComboBox.setSelectedItem("Hoat_dong");
        mainPanel.add(trangThaiComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("Lưu thay đổi");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(0, 153, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                TaiKhoanModel newTaiKhoan = new TaiKhoanModel();
                NhanVienDTO selectedNhanVien = (NhanVienDTO) maNhanVienComboBox.getSelectedItem();
                newTaiKhoan.setMaNhanVien(selectedNhanVien.getMaNhanVien());
                newTaiKhoan.setTenDangNhap(tenDangNhapField.getText().trim());
                String plainPassword = new String(matKhauField.getPassword()).trim();
                newTaiKhoan.setMatKhau(plainPassword); // Chưa mã hóa tại đây
                newTaiKhoan.setVaiTro((String) vaiTroComboBox.getSelectedItem());
                newTaiKhoan.setTrangThai((String) trangThaiComboBox.getSelectedItem());

                if (newTaiKhoan.getTenDangNhap().isEmpty() || newTaiKhoan.getMatKhau().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tên đăng nhập và mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                taiKhoanController = new TaiKhoanController();
                boolean success = taiKhoanController.insertTaiKhoan(newTaiKhoan);

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Thêm tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void exportToExcel() {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DanhSachTaiKhoan");
            XSSFRow row = null;
            Cell cell = null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            row = sheet.createRow(0);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("DANH SÁCH TÀI KHOẢN");
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            row = sheet.createRow(1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Thời gian xuất: " + currentDate);
            cell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

            row = sheet.createRow(3);
            String[] headers = {"STT", "Mã TK", "Mã NV", "Tên đăng nhập", "Vai trò", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            taiKhoanController = new TaiKhoanController();
            List<TaiKhoanModel> taiKhoanList = taiKhoanController.getAll();
            for (int i = 0; i < taiKhoanList.size(); i++) {
                TaiKhoanModel taiKhoan = taiKhoanList.get(i);
                row = sheet.createRow(4 + i);

                cell = row.createCell(0, CellType.NUMERIC);
                cell.setCellValue(i + 1);

                cell = row.createCell(1, CellType.NUMERIC);
                cell.setCellValue(taiKhoan.getMaTaiKhoan());

                cell = row.createCell(2, CellType.NUMERIC);
                cell.setCellValue(taiKhoan.getMaNhanVien());

                cell = row.createCell(3, CellType.STRING);
                cell.setCellValue(taiKhoan.getTenDangNhap());

                cell = row.createCell(4, CellType.STRING);
                cell.setCellValue(taiKhoan.getVaiTro());

                cell = row.createCell(5, CellType.STRING);
                cell.setCellValue(taiKhoan.getTrangThai());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("DanhSachTaiKhoan.xlsx"));
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx");
                }

                @Override
                public String getDescription() {
                    return "Excel Files (*.xlsx)";
                }
            });

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                    file = new File(file.getAbsolutePath() + ".xlsx");
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(this, "Xuất Excel thành công tại " + file.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
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

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int row;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            panel = createButtonPanel(true,
                e -> {
                    if (table != null && row < table.getRowCount()) {
                        showDetailDialog(row);
                    }
                    fireEditingStopped();
                },
                e -> {
                    if (table != null && row < table.getRowCount()) {
                        editDetailDialog(row);
                    }
                    fireEditingStopped();
                },
                e -> {
                    if (table == null || row >= table.getRowCount()) {
                        fireEditingStopped();
                        return;
                    }
                    int maTaiKhoan = (Integer) tableModel.getValueAt(row, 0);
                    taiKhoanController = new TaiKhoanController();
                    TaiKhoanModel selectedTaiKhoan = taiKhoanController.getById(maTaiKhoan);

                    if (selectedTaiKhoan == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        fireEditingStopped();
                        return;
                    }

                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có chắc muốn xóa tài khoản: " + selectedTaiKhoan.getTenDangNhap() + " (Mã: " + maTaiKhoan + ")?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            taiKhoanController.delete(maTaiKhoan);
                            JOptionPane.showMessageDialog(null, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                            SwingUtilities.invokeLater(() -> refreshTableData());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Xóa tài khoản thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    fireEditingStopped();
                }
            );
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
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
            return panel;
        }

        private void showDetailDialog(int row) {
            int maTaiKhoan = (Integer) tableModel.getValueAt(row, 0);
            taiKhoanController = new TaiKhoanController();
//            selectedTaiKhoan = taiKhoanController.getById(maTaiKhoan);

            if (selectedTaiKhoan == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin tài khoản!");
                return;
            }

            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(TaiKhoanView.this), "Chi tiết tài khoản", true);
            dialog.setLayout(new GridLayout(6, 2, 15, 15));
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(TaiKhoanView.this);

            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            JLabel maTaiKhoanLabel = new JLabel("Mã tài khoản:");
            maTaiKhoanLabel.setFont(labelFont);
            dialog.add(maTaiKhoanLabel);
            JTextField maTaiKhoanField = new JTextField(String.valueOf(selectedTaiKhoan.getMaTaiKhoan()));
            maTaiKhoanField.setFont(fieldFont);
            maTaiKhoanField.setEditable(false);
            dialog.add(maTaiKhoanField);

            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField(String.valueOf(selectedTaiKhoan.getMaNhanVien()));
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(false);
            dialog.add(maNhanVienField);

            JLabel tenDangNhapLabel = new JLabel("Tên đăng nhập:");
            tenDangNhapLabel.setFont(labelFont);
            dialog.add(tenDangNhapLabel);
            JTextField tenDangNhapField = new JTextField(selectedTaiKhoan.getTenDangNhap());
            tenDangNhapField.setFont(fieldFont);
            tenDangNhapField.setEditable(false);
            dialog.add(tenDangNhapField);

            JLabel vaiTroLabel = new JLabel("Vai trò:");
            vaiTroLabel.setFont(labelFont);
            dialog.add(vaiTroLabel);
            JTextField vaiTroField = new JTextField(selectedTaiKhoan.getVaiTro());
            vaiTroField.setFont(fieldFont);
            vaiTroField.setEditable(false);
            dialog.add(vaiTroField);

            JLabel trangThaiLabel = new JLabel("Trạng thái:");
            trangThaiLabel.setFont(labelFont);
            dialog.add(trangThaiLabel);
            JTextField trangThaiField = new JTextField(selectedTaiKhoan.getTrangThai());
            trangThaiField.setFont(fieldFont);
            trangThaiField.setEditable(false);
            dialog.add(trangThaiField);

            JLabel placeholderLabel = new JLabel("");
            dialog.add(placeholderLabel);
            JButton backButton = new JButton("Quay trở lại");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16));
            backButton.addActionListener(e -> dialog.dispose());
            dialog.add(backButton);

            dialog.setVisible(true);
        }

        private void editDetailDialog(int row) {
            int maTaiKhoan = (Integer) tableModel.getValueAt(row, 0);
            taiKhoanController = new TaiKhoanController();
            selectedTaiKhoan = taiKhoanController.getById(maTaiKhoan);

            if (selectedTaiKhoan == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin tài khoản!");
                return;
            }

            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(TaiKhoanView.this), "Cập nhật tài khoản", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(TaiKhoanView.this);

            JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font fieldFont = new Font("Arial", Font.PLAIN, 14);

            JLabel maTaiKhoanLabel = new JLabel("Mã tài khoản:");
            maTaiKhoanLabel.setFont(labelFont);
            mainPanel.add(maTaiKhoanLabel);
            JTextField maTaiKhoanField = new JTextField(String.valueOf(selectedTaiKhoan.getMaTaiKhoan()));
            maTaiKhoanField.setFont(fieldFont);
            maTaiKhoanField.setEditable(false);
            mainPanel.add(maTaiKhoanField);

            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            mainPanel.add(maNhanVienLabel);
            JComboBox<NhanVienDTO> maNhanVienComboBox = new JComboBox<>();
            maNhanVienComboBox.setFont(fieldFont);
            try {
                List<NhanVienDTO> nhanVienList = taiKhoanController.getNhanVienList();
                for (NhanVienDTO nhanVien : nhanVienList) {
                    maNhanVienComboBox.addItem(nhanVien);
                    if (nhanVien.getMaNhanVien() == selectedTaiKhoan.getMaNhanVien()) {
                        maNhanVienComboBox.setSelectedItem(nhanVien);
                    }
                }
                if (nhanVienList.isEmpty()) {
                    maNhanVienComboBox.addItem(new NhanVienDTO(0, "Không có nhân viên"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                maNhanVienComboBox.addItem(new NhanVienDTO(0, "Lỗi khi tải danh sách"));
            }
            mainPanel.add(maNhanVienComboBox);

            JLabel tenDangNhapLabel = new JLabel("Tên đăng nhập:");
            tenDangNhapLabel.setFont(labelFont);
            mainPanel.add(tenDangNhapLabel);
            JTextField tenDangNhapField = new JTextField(selectedTaiKhoan.getTenDangNhap());
            tenDangNhapField.setFont(fieldFont);
            mainPanel.add(tenDangNhapField);

            JLabel matKhauLabel = new JLabel("Mật khẩu (để trống nếu không đổi):");
            matKhauLabel.setFont(labelFont);
            mainPanel.add(matKhauLabel);
            JPasswordField matKhauField = new JPasswordField();
            matKhauField.setFont(fieldFont);
            mainPanel.add(matKhauField);

            JLabel vaiTroLabel = new JLabel("Vai trò:");
            vaiTroLabel.setFont(labelFont);
            mainPanel.add(vaiTroLabel);
            String[] vaiTroOptions = {"Quan_tri", "Nhan_vien"};
            JComboBox<String> vaiTroComboBox = new JComboBox<>(vaiTroOptions);
            vaiTroComboBox.setFont(fieldFont);
            vaiTroComboBox.setSelectedItem(selectedTaiKhoan.getVaiTro());
            mainPanel.add(vaiTroComboBox);

            JLabel trangThaiLabel = new JLabel("Trạng thái:");
            trangThaiLabel.setFont(labelFont);
            mainPanel.add(trangThaiLabel);
            String[] trangThaiOptions = {"Hoat_dong", "Bi_khoa"};
            JComboBox<String> trangThaiComboBox = new JComboBox<>(trangThaiOptions);
            trangThaiComboBox.setFont(fieldFont);
            trangThaiComboBox.setSelectedItem(selectedTaiKhoan.getTrangThai());
            mainPanel.add(trangThaiComboBox);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("Lưu thay đổi");
            saveButton.setFont(new Font("Arial", Font.BOLD, 14));
            saveButton.setBackground(new Color(0, 153, 0));
            saveButton.setForeground(Color.WHITE);
            saveButton.addActionListener(e -> {
                try {
                    selectedTaiKhoan.setMaTaiKhoan(Integer.parseInt(maTaiKhoanField.getText()));
                    selectedTaiKhoan.setMaNhanVien(((NhanVienDTO) maNhanVienComboBox.getSelectedItem()).getMaNhanVien());
                    selectedTaiKhoan.setTenDangNhap(tenDangNhapField.getText().trim());
                    String newPassword = new String(matKhauField.getPassword()).trim();
                    selectedTaiKhoan.setMatKhau(newPassword); // Truyền mật khẩu thô, mã hóa trong Controller
                    selectedTaiKhoan.setVaiTro((String) vaiTroComboBox.getSelectedItem());
                    selectedTaiKhoan.setTrangThai((String) trangThaiComboBox.getSelectedItem());

                    // Không kiểm tra rỗng cho mật khẩu vì có thể không đổi
                    taiKhoanController = new TaiKhoanController();
                    boolean success = taiKhoanController.updateTaiKhoan(selectedTaiKhoan);

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        refreshTableData();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton cancelButton = new JButton("Hủy bỏ");
            cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
            cancelButton.setBackground(new Color(204, 0, 0));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            dialog.add(mainPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
    }
}
