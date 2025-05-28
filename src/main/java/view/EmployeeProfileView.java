/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ChucVuController;
import controller.PhongBanController;
import controller.nhanSuController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import model.NhanSuModel;

/**
 * @author Windows
 */
public class EmployeeProfileView extends JPanel {
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private JComboBox<String> positionFilter;
    private JComboBox<String> statusFilter;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private nhanSuController nhansu;
    private NhanSuModel selectedNhanSu;

    public EmployeeProfileView() {
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
        String[] columnNames = {"Mã NV", "Họ và tên","Giới Tính", "Ngày Sinh", "Địa Chỉ", "Thông tin liên hệ", "Trạng thái", "Thao tác"};
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
        //fill dữ liệu từ database
        loadDataFromDatabase();
        // Sample data (replace with actual data in a real application)
        setupAddButtonListener();

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
    
    private void loadDataFromDatabase(){
       nhansu = new nhanSuController();
       List<NhanSuModel> nhanSuList = nhansu.getAll();
         for (NhanSuModel nhanSu : nhanSuList) {
            Object[] row = {
                nhanSu.getMaSo(),
                nhanSu.getHoTen(),
                nhanSu.getGioiTinh(),
                nhanSu.getNgaySinh() != null ? nhanSu.getNgaySinh().toString() : "",
                nhanSu.getDiaChi(),
                "SDT: " + nhanSu.getSoDienThoai() + "\nEmail: " + nhanSu.getEmail(),
                nhanSu.getTinhTrang(),
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
    private void setupAddButtonListener() {
        addButton.addActionListener(e -> {
            insertDetailDialog();
        });
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
    private void insertDetailDialog(){
            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Chi tiết nhân sự", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(EmployeeProfileView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField();
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(true);
            dialog.add(maNhanVienField);

            JLabel hoTenLabel = new JLabel("Họ và tên:");
            hoTenLabel.setFont(labelFont);
            dialog.add(hoTenLabel);
            JTextField hoTenField = new JTextField();
            hoTenField.setFont(fieldFont);
            hoTenField.setEditable(true);
            dialog.add(hoTenField);

            JLabel gioiTinhLabel = new JLabel("Giới tính:");
            gioiTinhLabel.setFont(labelFont);
            dialog.add(gioiTinhLabel);
             String[] gioiTinhOptions = {"Nam", "Nu"};
            JComboBox<String> gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
            gioiTinhComboBox.setFont(fieldFont);
            gioiTinhComboBox.setSelectedIndex(0);
            dialog.add(gioiTinhComboBox);

            JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
            ngaySinhLabel.setFont(labelFont);
            dialog.add(ngaySinhLabel);
            JTextField ngaySinhField = new JTextField();
            ngaySinhField.setFont(fieldFont);
            ngaySinhField.setEditable(true);
            dialog.add(ngaySinhField);

            JLabel diaChiLabel = new JLabel("Địa chỉ:");
            diaChiLabel.setFont(labelFont);
            dialog.add(diaChiLabel);
            JTextField diaChiField = new JTextField();
            diaChiField.setFont(fieldFont);
            diaChiField.setEditable(true);
            dialog.add(diaChiField);

            JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
            soDienThoaiLabel.setFont(labelFont);
            dialog.add(soDienThoaiLabel);
            JTextField soDienThoaiField = new JTextField();
            soDienThoaiField.setFont(fieldFont);
            soDienThoaiField.setEditable(true);
            dialog.add(soDienThoaiField);

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            dialog.add(emailLabel);
            JTextField emailField = new JTextField();
            emailField.setFont(fieldFont);
            emailField.setEditable(true);
            dialog.add(emailField);

            JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
            trinhDoHocVanLabel.setFont(labelFont);
            dialog.add(trinhDoHocVanLabel);
            String[] trinhDoOptions = {"Dai_hoc", "Cao_dang", "Tot_nghiep_cap_3"};
            JComboBox<String> trinhDoHocVanComboBox = new JComboBox<>(trinhDoOptions);
            trinhDoHocVanComboBox.setFont(fieldFont);
            trinhDoHocVanComboBox.setSelectedIndex(0);
            dialog.add(trinhDoHocVanComboBox);

            JLabel maPhongBanLabel = new JLabel("Mã phòng ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            //  Load danh sách phòng ban từ database
            JComboBox<String> maPhongBanComboBox = new JComboBox<>();
            maPhongBanComboBox.setFont(fieldFont);
            
            PhongBanController phongBanController = new PhongBanController();
            List<String> phongbanlist = phongBanController.getPhongBanDisplayList();
            if (phongbanlist.isEmpty()) {
                maPhongBanComboBox.addItem("Không có phòng ban");
                maPhongBanComboBox.setEnabled(false);
            } else {
                for (String string : phongbanlist) {
                    maPhongBanComboBox.addItem(string);
                }
                maPhongBanComboBox.setSelectedIndex(0);
            }
            dialog.add(maPhongBanComboBox);

            JLabel maChucVuLabel = new JLabel("Mã chức vụ:");
            maChucVuLabel.setFont(labelFont);
            dialog.add(maChucVuLabel);
            // TODO: Load danh sách chức vụ từ database
            JComboBox<String> maChucVuComboBox = new JComboBox<>();
            maChucVuComboBox.setFont(fieldFont);
            // load chuc vu tu database
            ChucVuController chucVuController = new ChucVuController();
            List<String> chucvulist = chucVuController.getChucVuDisplayList();
            if (chucvulist.isEmpty()) {
                maChucVuComboBox.addItem("Không có chức vụ");
                maChucVuComboBox.setEnabled(false);
            } else {
                for (String string : chucvulist) {
                    maChucVuComboBox.addItem(string);
                }
                maChucVuComboBox.setSelectedIndex(0);
            }
            dialog.add(maChucVuComboBox);

            JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
            ngayVaoLamLabel.setFont(labelFont);
            dialog.add(ngayVaoLamLabel);
            JTextField ngayVaoLamField = new JTextField();
            ngayVaoLamField.setFont(fieldFont);
            ngayVaoLamField.setToolTipText("Nhập ngày vào làm theo định dạng yyyy-MM-dd");
            ngayVaoLamField.setEditable(true);
            dialog.add(ngayVaoLamField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            String[] tinhTrangOptions = {"Dang_lam", "Da_nghi"};
            JComboBox<String> tinhTrangComboBox = new JComboBox<>(tinhTrangOptions);
            tinhTrangComboBox.setFont(fieldFont);
            tinhTrangComboBox.setSelectedItem("Dang_lam");
            dialog.add(tinhTrangComboBox);
            
            JButton saveButton = new JButton("Lưu thay đổi");
            saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
            saveButton.addActionListener(e -> {
              try {
                    // Lấy dữ liệu từ các 
                    String maSo = maNhanVienField.getText().trim();
                    String hoTen = hoTenField.getText().trim();
                    String gioiTinh = (String) gioiTinhComboBox.getSelectedItem();
                    String ngaySinh = ngaySinhField.getText().trim();
                    String diaChi = diaChiField.getText().trim();
                    String soDienThoai = soDienThoaiField.getText().trim();
                    String email = emailField.getText().trim();
                    String trinhDoHocVan = (String) trinhDoHocVanComboBox.getSelectedItem();
                    String ngayVaoLam = ngayVaoLamField.getText().trim();
                    String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();

                    // Lấy mã phòng ban (format: "1 - Tên phòng ban")
                    String selectedPhongBan = (String) maPhongBanComboBox.getSelectedItem();
                    int maPhongBan = 0;
                    if (!selectedPhongBan.equals("Không có phòng ban")) {
                        maPhongBan = Integer.parseInt(selectedPhongBan.split(" - ")[0]);
                    }

                    // Lấy mã chức vụ (format: "1 - Tên chức vụ")
                    String selectedChucVu = (String) maChucVuComboBox.getSelectedItem();
                    int maChucVu = 0;
                    if (!selectedChucVu.equals("Không có chức vụ")) {
                        maChucVu = Integer.parseInt(selectedChucVu.split(" - ")[0]);
                    }

                    // Xác thực dữ liệu
                    if (hoTen.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (email.isEmpty() || !email.contains("@")) {
                        JOptionPane.showMessageDialog(dialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (soDienThoai.isEmpty() || !soDienThoai.matches("\\d{10,11}")) {
                        JOptionPane.showMessageDialog(dialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (maPhongBan == 0) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn phòng ban hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (maChucVu == 0) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng chọn chức vụ hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Tạo đối tượng NhanSuModel
                    NhanSuModel newNhanSu = new NhanSuModel();
                    newNhanSu.setMaSo(maSo);
                    newNhanSu.setHoTen(hoTen);
                    newNhanSu.setGioiTinh(gioiTinh);
                    newNhanSu.setDiaChi(diaChi);
                    newNhanSu.setSoDienThoai(soDienThoai);
                    newNhanSu.setEmail(email);
                    newNhanSu.setTrinhDoHocVan(trinhDoHocVan);
                    newNhanSu.setMaPhongBan(maPhongBan);
                    newNhanSu.setMaChucVu(maChucVu);
                    newNhanSu.setTinhTrang(tinhTrang);

                    // Xử lý ngày sinh
                    if (!ngaySinh.isEmpty()) {
                        try {
                            java.sql.Date parsedNgaySinh = java.sql.Date.valueOf(ngaySinh);
                            newNhanSu.setNgaySinh(parsedNgaySinh);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày sinh không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Xử lý ngày vào làm
                    if (!ngayVaoLam.isEmpty()) {
                        try {
                            java.sql.Date parsedNgayVaoLam = java.sql.Date.valueOf(ngayVaoLam);
                            newNhanSu.setNgayVaoLam(parsedNgayVaoLam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày vào làm không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Gọi hàm insertNhanVien từ nhanSuController
                    boolean success = nhansu.insertNhanVien(newNhanSu);

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        refreshTableData(); // Làm mới bảng
                        dialog.dispose(); // Đóng dialog
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                e -> showDetailDialog(row),
                e -> editDetailDialog(row),
                e -> {
                            // Lấy mã số từ cột 0
                        String maSo = (String) tableModel.getValueAt(row, 0);

                        // Tìm NhanSuModel tương ứng
                        List<NhanSuModel> nhanSuList = nhansu.getAll();
                        NhanSuModel selectedNhanSu = null;
                        for (NhanSuModel nhanSu : nhanSuList) {
                            if (nhanSu.getMaSo().equals(maSo)) {
                                selectedNhanSu = nhanSu;
                                break;
                            }
                        }

                        if (selectedNhanSu == null) {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            fireEditingStopped();
                            return;
                        }

                        // Xác nhận xóa
                        int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Bạn có chắc muốn xóa nhân viên: " + selectedNhanSu.getHoTen() + " (Mã: " + maSo + ")?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                // Gọi hàm delete
                                nhansu.delete(selectedNhanSu.getMaNhanVien());
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
             // Lấy mã nhân viên từ cột 0
            String maSo = (String) tableModel.getValueAt(row, 0);
            List<NhanSuModel> nhanSuList = nhansu.getAll();
            NhanSuModel selectedNhanSu = null;
            for (NhanSuModel nhanSu : nhanSuList) {
                if (nhanSu.getMaSo().equals(maSo)) {
                    selectedNhanSu = nhanSu;
                    break;
                }
            }

            if (selectedNhanSu == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Chi tiết nhân sự", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(EmployeeProfileView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField(selectedNhanSu.getMaSo());
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(false);
            dialog.add(maNhanVienField);

            JLabel hoTenLabel = new JLabel("Họ và tên:");
            hoTenLabel.setFont(labelFont);
            dialog.add(hoTenLabel);
            JTextField hoTenField = new JTextField(selectedNhanSu.getHoTen());
            hoTenField.setFont(fieldFont);
            hoTenField.setEditable(false);
            dialog.add(hoTenField);

            JLabel gioiTinhLabel = new JLabel("Giới tính:");
            gioiTinhLabel.setFont(labelFont);
            dialog.add(gioiTinhLabel);
            JTextField gioiTinhField = new JTextField(selectedNhanSu.getGioiTinh());
            gioiTinhField.setFont(fieldFont);
            gioiTinhField.setEditable(false);
            dialog.add(gioiTinhField);

            JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
            ngaySinhLabel.setFont(labelFont);
            dialog.add(ngaySinhLabel);
            JTextField ngaySinhField = new JTextField(selectedNhanSu.getNgaySinh() != null ? selectedNhanSu.getNgaySinh().toString() : "");
            ngaySinhField.setFont(fieldFont);
            ngaySinhField.setEditable(false);
            dialog.add(ngaySinhField);

            JLabel diaChiLabel = new JLabel("Địa chỉ:");
            diaChiLabel.setFont(labelFont);
            dialog.add(diaChiLabel);
            JTextField diaChiField = new JTextField(selectedNhanSu.getDiaChi());
            diaChiField.setFont(fieldFont);
            diaChiField.setEditable(false);
            dialog.add(diaChiField);

            JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
            soDienThoaiLabel.setFont(labelFont);
            dialog.add(soDienThoaiLabel);
            JTextField soDienThoaiField = new JTextField(selectedNhanSu.getSoDienThoai());
            soDienThoaiField.setFont(fieldFont);
            soDienThoaiField.setEditable(false);
            dialog.add(soDienThoaiField);

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            dialog.add(emailLabel);
            JTextField emailField = new JTextField(selectedNhanSu.getEmail());
            emailField.setFont(fieldFont);
            emailField.setEditable(false);
            dialog.add(emailField);

            JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
            trinhDoHocVanLabel.setFont(labelFont);
            dialog.add(trinhDoHocVanLabel);
            JTextField trinhDoHocVanField = new JTextField(selectedNhanSu.getTrinhDoHocVan());
            trinhDoHocVanField.setFont(fieldFont);
            trinhDoHocVanField.setEditable(false);
            dialog.add(trinhDoHocVanField);

            JLabel maPhongBanLabel = new JLabel("Mã phòng ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            JTextField maPhongBanField = new JTextField(String.valueOf(selectedNhanSu.getMaPhongBan()));
            maPhongBanField.setFont(fieldFont);
            maPhongBanField.setEditable(false);
            dialog.add(maPhongBanField);

            JLabel maChucVuLabel = new JLabel("Mã chức vụ:");
            maChucVuLabel.setFont(labelFont);
            dialog.add(maChucVuLabel);
            JTextField maChucVuField = new JTextField(String.valueOf(selectedNhanSu.getMaChucVu()));
            maChucVuField.setFont(fieldFont);
            maChucVuField.setEditable(false);
            dialog.add(maChucVuField);

            JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
            ngayVaoLamLabel.setFont(labelFont);
            dialog.add(ngayVaoLamLabel);
            JTextField ngayVaoLamField = new JTextField(selectedNhanSu.getNgayVaoLam() != null ? selectedNhanSu.getNgayVaoLam().toString() : "");
            ngayVaoLamField.setFont(fieldFont);
            ngayVaoLamField.setEditable(false);
            dialog.add(ngayVaoLamField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            JTextField tinhTrangField = new JTextField(selectedNhanSu.getTinhTrang());
            tinhTrangField.setFont(fieldFont);
            tinhTrangField.setEditable(false);
            dialog.add(tinhTrangField);

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
            String maSo = (String) tableModel.getValueAt(row, 0);
            List<NhanSuModel> nhanSuList = nhansu.getAll();
            selectedNhanSu = null;
            for (NhanSuModel nhanSu : nhanSuList) {
                if (nhanSu.getMaSo().equals(maSo)) {
                    selectedNhanSu = nhanSu;
                    break;
                }
            }

            if (selectedNhanSu == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin nhân viên!");
                return;
            }

            // Tạo dialog
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(EmployeeProfileView.this), "Chi tiết nhân sự", true);
            dialog.setLayout(new GridLayout(13, 2, 15, 15)); // 13 hàng (12 trường + 1 nút Quay lại), 2 cột (label + textfield)
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(EmployeeProfileView.this);
            

            // Font chữ lớn hơn
            Font labelFont = new Font("Arial", Font.PLAIN, 16);
            Font fieldFont = new Font("Arial", Font.PLAIN, 16);

            // Thêm các thành phần
            JLabel maNhanVienLabel = new JLabel("Mã nhân viên:");
            maNhanVienLabel.setFont(labelFont);
            dialog.add(maNhanVienLabel);
            JTextField maNhanVienField = new JTextField(selectedNhanSu.getMaSo());
            maNhanVienField.setFont(fieldFont);
            maNhanVienField.setEditable(false);
            dialog.add(maNhanVienField);

            JLabel hoTenLabel = new JLabel("Họ và tên:");
            hoTenLabel.setFont(labelFont);
            dialog.add(hoTenLabel);
            JTextField hoTenField = new JTextField(selectedNhanSu.getHoTen());
            hoTenField.setFont(fieldFont);
            hoTenField.setEditable(true);
            dialog.add(hoTenField);

            JLabel gioiTinhLabel = new JLabel("Giới tính:");
            gioiTinhLabel.setFont(labelFont);
            dialog.add(gioiTinhLabel);
             String[] gioiTinhOptions = {"Nam", "Nu"};
            JComboBox<String> gioiTinhComboBox = new JComboBox<>(gioiTinhOptions);
            gioiTinhComboBox.setFont(fieldFont);
            gioiTinhComboBox.setSelectedItem(selectedNhanSu.getGioiTinh());
            dialog.add(gioiTinhComboBox);

            JLabel ngaySinhLabel = new JLabel("Ngày sinh:");
            ngaySinhLabel.setFont(labelFont);
            dialog.add(ngaySinhLabel);
            JTextField ngaySinhField = new JTextField(selectedNhanSu.getNgaySinh() != null ? selectedNhanSu.getNgaySinh().toString() : "");
            ngaySinhField.setFont(fieldFont);
            ngaySinhField.setEditable(true);
            dialog.add(ngaySinhField);

            JLabel diaChiLabel = new JLabel("Địa chỉ:");
            diaChiLabel.setFont(labelFont);
            dialog.add(diaChiLabel);
            JTextField diaChiField = new JTextField(selectedNhanSu.getDiaChi());
            diaChiField.setFont(fieldFont);
            diaChiField.setEditable(true);
            dialog.add(diaChiField);

            JLabel soDienThoaiLabel = new JLabel("Số điện thoại:");
            soDienThoaiLabel.setFont(labelFont);
            dialog.add(soDienThoaiLabel);
            JTextField soDienThoaiField = new JTextField(selectedNhanSu.getSoDienThoai());
            soDienThoaiField.setFont(fieldFont);
            soDienThoaiField.setEditable(true);
            dialog.add(soDienThoaiField);

            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setFont(labelFont);
            dialog.add(emailLabel);
            JTextField emailField = new JTextField(selectedNhanSu.getEmail());
            emailField.setFont(fieldFont);
            emailField.setEditable(true);
            dialog.add(emailField);

            JLabel trinhDoHocVanLabel = new JLabel("Trình độ học vấn:");
            trinhDoHocVanLabel.setFont(labelFont);
            dialog.add(trinhDoHocVanLabel);
            String[] trinhDoOptions = {"Dai_hoc", "Cao_dang", "Tot_nghiep_cap_3"};
            JComboBox<String> trinhDoHocVanComboBox = new JComboBox<>(trinhDoOptions);
            trinhDoHocVanComboBox.setFont(fieldFont);
            trinhDoHocVanComboBox.setSelectedItem(selectedNhanSu.getTrinhDoHocVan());
            dialog.add(trinhDoHocVanComboBox);

            JLabel maPhongBanLabel = new JLabel("Mã phòng ban:");
            maPhongBanLabel.setFont(labelFont);
            dialog.add(maPhongBanLabel);
            //  Load danh sách phòng ban từ database
            JComboBox<String> maPhongBanComboBox = new JComboBox<>();
            maPhongBanComboBox.setFont(fieldFont);
            
            PhongBanController phongBanController = new PhongBanController();
            List<String> phongbanlist = phongBanController.getPhongBanDisplayList();
            for (String string : phongbanlist) {
                maPhongBanComboBox.addItem(string);
            }
            int selectedMaPhongBan = selectedNhanSu.getMaPhongBan();
            for (int i = 0; i < maPhongBanComboBox.getItemCount(); i++) {
                String item = maPhongBanComboBox.getItemAt(i);
                if (item.startsWith(selectedMaPhongBan + " -")) {
                    maPhongBanComboBox.setSelectedIndex(i);
                    break;
                }
            }
            dialog.add(maPhongBanComboBox);

            JLabel maChucVuLabel = new JLabel("Mã chức vụ:");
            maChucVuLabel.setFont(labelFont);
            dialog.add(maChucVuLabel);
            // TODO: Load danh sách chức vụ từ database
            JComboBox<String> maChucVuComboBox = new JComboBox<>();
            maChucVuComboBox.setFont(fieldFont);
            // load chuc vu tu database
            ChucVuController chucVuController = new ChucVuController();
            List<String> chucvulist = chucVuController.getChucVuDisplayList();
            for (String string : chucvulist) {
                maChucVuComboBox.addItem(string);
            }
            int selectedMaChucVu = selectedNhanSu.getMaChucVu();
            for (int i = 0; i < maChucVuComboBox.getItemCount(); i++) {
                String item = maChucVuComboBox.getItemAt(i);
                if (item.startsWith(selectedMaChucVu + " -")) {
                    maChucVuComboBox.setSelectedIndex(i);
                    break;
                }
            }
            dialog.add(maChucVuComboBox);

            JLabel ngayVaoLamLabel = new JLabel("Ngày vào làm:");
            ngayVaoLamLabel.setFont(labelFont);
            dialog.add(ngayVaoLamLabel);
            JTextField ngayVaoLamField = new JTextField(selectedNhanSu.getNgayVaoLam() != null ? selectedNhanSu.getNgayVaoLam().toString() : "");
            ngayVaoLamField.setFont(fieldFont);
            ngayVaoLamField.setEditable(true);
            dialog.add(ngayVaoLamField);

            JLabel tinhTrangLabel = new JLabel("Tình trạng:");
            tinhTrangLabel.setFont(labelFont);
            dialog.add(tinhTrangLabel);
            String[] tinhTrangOptions = {"Dang_lam", "Da_nghi"};
            JComboBox<String> tinhTrangComboBox = new JComboBox<>(tinhTrangOptions);
            tinhTrangComboBox.setFont(fieldFont);
            tinhTrangComboBox.setSelectedItem(selectedNhanSu.getTinhTrang());
            dialog.add(tinhTrangComboBox);
            
            JButton saveButton = new JButton("Lưu thay đổi");
            saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
            saveButton.addActionListener(e ->{
                 try {
                    // Lấy dữ liệu từ các component trong dialog
                    String hoTen = hoTenField.getText().trim();
                    String gioiTinh = (String) gioiTinhComboBox.getSelectedItem();
                    String ngaySinh = ngaySinhField.getText().trim();
                    String diaChi = diaChiField.getText().trim();
                    String soDienThoai = soDienThoaiField.getText().trim();
                    String email = emailField.getText().trim();
                    String trinhDoHocVan = (String) trinhDoHocVanComboBox.getSelectedItem();
                    String ngayVaoLam = ngayVaoLamField.getText().trim();
                    String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();

                    // Lấy mã phòng ban từ ComboBox (format: "1 - Tên phòng ban")
                    String selectedPhongBan = (String) maPhongBanComboBox.getSelectedItem();
                    int maPhongBan = Integer.parseInt(selectedPhongBan.split(" - ")[0]);

                    // Lấy mã chức vụ từ ComboBox (format: "1 - Tên chức vụ")
                    String selectedChucVu = (String) maChucVuComboBox.getSelectedItem();
                    int maChucVu = Integer.parseInt(selectedChucVu.split(" - ")[0]);

                    // Validate dữ liệu
                    if (hoTen.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (email.isEmpty() || !email.contains("@")) {
                        JOptionPane.showMessageDialog(dialog, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (soDienThoai.isEmpty() || !soDienThoai.matches("\\d{10,11}")) {
                        JOptionPane.showMessageDialog(dialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Tạo object NhanSuModel với dữ liệu mới
                    NhanSuModel updatedNhanSu = new NhanSuModel();
                    updatedNhanSu.setMaNhanVien(selectedNhanSu.getMaNhanVien()); // ID không đổi
                    updatedNhanSu.setMaSo(selectedNhanSu.getMaSo()); // Mã số không đổi
                    updatedNhanSu.setHoTen(hoTen);
                    updatedNhanSu.setGioiTinh(gioiTinh);
                    updatedNhanSu.setDiaChi(diaChi);
                    updatedNhanSu.setSoDienThoai(soDienThoai);
                    updatedNhanSu.setEmail(email);
                    updatedNhanSu.setTrinhDoHocVan(trinhDoHocVan);
                    updatedNhanSu.setMaPhongBan(maPhongBan);
                    updatedNhanSu.setMaChucVu(maChucVu);
                    updatedNhanSu.setTinhTrang(tinhTrang);

                    // Parse và set ngày sinh
                    if (!ngaySinh.isEmpty()) {
                        try {
                            java.sql.Date parsedNgaySinh = java.sql.Date.valueOf(ngaySinh);
                            updatedNhanSu.setNgaySinh(parsedNgaySinh);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày sinh không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Parse và set ngày vào làm
                    if (!ngayVaoLam.isEmpty()) {
                        try {
                            java.sql.Date parsedNgayVaoLam = java.sql.Date.valueOf(ngayVaoLam);
                            updatedNhanSu.setNgayVaoLam(parsedNgayVaoLam);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ngày vào làm không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    // Gọi method update trong controller
                    boolean success = nhansu.updateNhanVien(updatedNhanSu);

                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thông tin nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                        // Cập nhật lại dữ liệu trong bảng
                        refreshTableData();

                        // Đóng dialog
                        dialog.dispose();

                        // Dừng cell editing
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
 