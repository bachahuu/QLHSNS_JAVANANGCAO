package view_admin;

import controller.NghiPhepController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import model.NghiPhepModel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date; // Chú ý: java.util.Date cho JDatePickerImpl và format
import java.awt.GridLayout;
import javax.swing.SwingUtilities;

import java.util.Map;
import java.util.LinkedHashMap;

// Import JDatePickerImpl
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;
import java.util.Calendar;
import javax.swing.JFormattedTextField;


public class NghiPhepView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnThemNghiPhep; // Removed btnXuatFile
    private JList<NghiPhepModel> list;
    private DefaultListModel<NghiPhepModel> listModel;
    private NghiPhepController controller;

    private final Color PRIMARY_BLUE = new Color(33, 150, 243); // Đồng bộ với mainActivityView
    private final Color GREEN_VIEW_BUTTON = new Color(46, 204, 113);
    private final Color ORANGE_EDIT_BUTTON = new Color(241, 196, 15);
    private final Color RED_DELETE_BUTTON = new Color(231, 76, 60);
    private final Color GREY_CANCEL_BUTTON = new Color(189, 195, 199);
    private final Color TEXT_COLOR_BLACK = Color.BLACK;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Defined common values for LoaiNghi and TrangThai based on the screenshot
    private final String[] LOAI_NGHI_OPTIONS = {"Nghi_phep_nam", "Nghi_om_dau", "Nghi_thai_san", "Nghi_le", "Nghi_khong_luong"};
    private final String[] TRANG_THAI_OPTIONS = {"Da_duyet", "Cho_duyet", "Tu_choi"};

    public NghiPhepView() {
        // Khởi tạo layout cho JPanel
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        controller = new NghiPhepController();
        controller.setView(this);

        // ===== NORTH PANEL (Tìm kiếm, Thêm, Xuất file) =====
        JPanel pnNorth = new JPanel();
        pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.X_AXIS));
        pnNorth.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTimKiem = new JLabel("Tìm kiếm: ");
        lblTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTimKiem.setForeground(TEXT_COLOR_BLACK);

        txtTimKiem = new JTextField(20);
        txtTimKiem.putClientProperty("JComponent.roundRect", true);
        txtTimKiem.setMaximumSize(new Dimension(200, 30));
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setForeground(TEXT_COLOR_BLACK);

        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Arial", Font.BOLD, 14));
        btnTimKiem.setBackground(PRIMARY_BLUE);
        btnTimKiem.setForeground(TEXT_COLOR_BLACK);
        btnTimKiem.setFocusPainted(false);

        btnThemNghiPhep = new JButton("Thêm nghỉ phép");
        btnThemNghiPhep.setFont(new Font("Arial", Font.BOLD, 14));
        btnThemNghiPhep.setBackground(PRIMARY_BLUE);
        btnThemNghiPhep.setForeground(TEXT_COLOR_BLACK);
        btnThemNghiPhep.setFocusPainted(false);

        // Removed btnXuatFile and its related code
        // btnXuatFile = new JButton("Xuất file");
        // btnXuatFile.setFont(new Font("Arial", Font.BOLD, 14));
        // btnXuatFile.setBackground(PRIMARY_BLUE);
        // btnXuatFile.setForeground(TEXT_COLOR_BLACK);
        // btnXuatFile.setFocusPainted(false);

        pnNorth.add(lblTimKiem);
        pnNorth.add(txtTimKiem);
        pnNorth.add(Box.createHorizontalStrut(10));
        pnNorth.add(btnTimKiem);

        pnNorth.add(Box.createHorizontalGlue());

        pnNorth.add(btnThemNghiPhep);
        // pnNorth.add(Box.createHorizontalStrut(10)); // Removed this strut as btnXuatFile is gone
        // pnNorth.add(btnXuatFile); // Removed btnXuatFile

        add(pnNorth, BorderLayout.NORTH);

        // ===== TABLE CENTER =====
        JPanel pnCenter = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách nghỉ phép");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder.setTitleColor(TEXT_COLOR_BLACK);
        pnCenter.setBorder(titledBorder);

        String[] columnNames = {
            "Mã nghỉ phép", "Họ tên", "Loại nghỉ",
            "Ngày bắt đầu", "Ngày kết thúc", "Lý do", "Trạng Thái", "Thao tác"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(TEXT_COLOR_BLACK);

        JScrollPane sp = new JScrollPane(table);

        table.getColumn("Thao tác").setCellRenderer(new ButtonRenderer());
        table.getColumn("Thao tác").setCellEditor(new ButtonEditor(new JTextField()));
        table.getColumn("Thao tác").setPreferredWidth(210);

        pnCenter.add(sp, BorderLayout.CENTER);
        add(pnCenter, BorderLayout.CENTER);

        addEventHandlers();

        loadDataToTable();
    }

    private void loadDataToTable() {
        controller.loadAllNghiPhep();
    }

    private void addEventHandlers() {
        btnThemNghiPhep.addActionListener(e -> controller.themNghiPhep());
        btnTimKiem.addActionListener(e -> controller.timKiemNghiPhep(txtTimKiem.getText())); 
        txtTimKiem.addActionListener(e -> controller.timKiemNghiPhep(txtTimKiem.getText())); 
        // Removed btnXuatFile.addActionListener(e -> controller.xuatFile(tableModel));
    }

    public void hienThiDanhSachNghiPhep(List<NghiPhepModel> danhSach) {
        tableModel.setRowCount(0);

        for (NghiPhepModel np : danhSach) {
            String ngayBatDauStr = (np.getNgayBatDau() != null) ? dateFormat.format(np.getNgayBatDau()) : "";
            String ngayKetThucStr = (np.getNgayKetThuc() != null) ? dateFormat.format(np.getNgayKetThuc()) : "";

            tableModel.addRow(new Object[]{
                np.getMaNghiPhep(),
                np.getHoten(),
                formatLoaiNghi(np.getLoaiNghi()), // Apply formatting
                ngayBatDauStr,
                ngayKetThucStr,
                np.getLyDo(),
                formatTrangThai(np.getTrangThai()), // Apply formatting
                ""
            });
        }
    }

    // Helper method to format "Loai nghi"
    private String formatLoaiNghi(String loaiNghi) {
        if (loaiNghi == null) {
            return "";
        }
        switch (loaiNghi) {
            case "Nghi_phep_nam":
                return "Nghỉ phép năm";
            case "Nghi_om_dau":
                return "Nghỉ ốm đau";
            case "Nghi_thai_san":
                return "Nghỉ thai sản";
            case "Nghi_le":
                return "Nghỉ lễ";
            case "Nghi_khong_luong":
                return "Nghỉ không lương";
            // Add more cases if there are other types of leave
            default:
                // Replace underscores with spaces and capitalize first letter of each word
                String formatted = loaiNghi.replace("_", " ").toLowerCase();
                StringBuilder result = new StringBuilder();
                boolean capitalizeNext = true;
                for (char c : formatted.toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        result.append(c);
                        capitalizeNext = true;
                    } else if (capitalizeNext) {
                        result.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        result.append(c);
                    }
                }
                return result.toString();
        }
    }

    // Helper method to format "Trang thai"
    private String formatTrangThai(String trangThai) {
        if (trangThai == null) {
            return "";
        }
        switch (trangThai) {
            case "Da_duyet":
                return "Đã duyệt";
            case "Cho_duyet":
                return "Chờ duyệt";
            case "Tu_choi":
                return "Từ chối";
            default:
                // Replace underscores with spaces and capitalize first letter of each word
                String formatted = trangThai.replace("_", " ").toLowerCase();
                StringBuilder result = new StringBuilder();
                boolean capitalizeNext = true;
                for (char c : formatted.toCharArray()) {
                    if (Character.isWhitespace(c)) {
                        result.append(c);
                        capitalizeNext = true;
                    } else if (capitalizeNext) {
                        result.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        result.append(c);
                    }
                }
                return result.toString();
        }
    }

    public void hienThiThongBao(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void hienThiLoi(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public int hienThiXacNhan(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
    }

    // =========================================================
    //         CÁC PHƯƠNG THỨC MỞ DIALOG ĐƯỢC GỌI TỪ CONTROLLER
    // =========================================================

    public void openAddNghiPhepDialog(Map<Integer, String> danhSachNhanVien) {
        // Truyền danh sách nhân viên đã tải vào dialog
        AddNghiPhepDialog addDialog = new AddNghiPhepDialog(danhSachNhanVien);
        addDialog.setVisible(true);
    }

    public void openViewNghiPhepDialog(NghiPhepModel nghiPhep) {
        ViewNghiPhepDialog viewDialog = new ViewNghiPhepDialog(nghiPhep);
        viewDialog.setVisible(true);
    }

    public void openEditNghiPhepDialog(NghiPhepModel nghiPhepToEdit, Map<Integer, String> danhSachNhanVien) {
        EditNghiPhepDialog editDialog = new EditNghiPhepDialog(nghiPhepToEdit, danhSachNhanVien);
        editDialog.setVisible(true);
    }


    // =========================================================
    //                 INNER CLASSES CHO TABLE BUTTONS
    // =========================================================

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton viewButton;
        private JButton editButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            viewButton = new JButton("Xem");
            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");

            viewButton.setBackground(GREEN_VIEW_BUTTON);
            viewButton.setForeground(TEXT_COLOR_BLACK);
            viewButton.setFocusPainted(false);
            viewButton.setFont(new Font("Arial", Font.BOLD, 12));
            viewButton.setPreferredSize(new Dimension(65, 28));

            editButton.setBackground(ORANGE_EDIT_BUTTON);
            editButton.setForeground(TEXT_COLOR_BLACK);
            editButton.setFocusPainted(false);
            editButton.setFont(new Font("Arial", Font.BOLD, 12));
            editButton.setPreferredSize(new Dimension(65, 28));

            deleteButton.setBackground(RED_DELETE_BUTTON);
            deleteButton.setForeground(TEXT_COLOR_BLACK);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
            deleteButton.setPreferredSize(new Dimension(65, 28));

            add(viewButton);
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel editorPanel;
        private JButton viewButton;
        private JButton editButton;
        private JButton deleteButton;
        private JTable table;
        private int clickedRow;

        public ButtonEditor(JTextField tf) {
            editorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editorPanel.setOpaque(true);

            viewButton = new JButton("Xem");
            editButton = new JButton("Sửa");
            deleteButton = new JButton("Xóa");

            viewButton.setBackground(GREEN_VIEW_BUTTON);
            viewButton.setForeground(TEXT_COLOR_BLACK);
            viewButton.setFocusPainted(false);
            viewButton.setFont(new Font("Arial", Font.BOLD, 12));
            viewButton.setPreferredSize(new Dimension(65, 28));

            editButton.setBackground(ORANGE_EDIT_BUTTON);
            editButton.setForeground(TEXT_COLOR_BLACK);
            editButton.setFocusPainted(false);
            editButton.setFont(new Font("Arial", Font.BOLD, 12));
            editButton.setPreferredSize(new Dimension(65, 28));

            deleteButton.setBackground(RED_DELETE_BUTTON);
            deleteButton.setForeground(TEXT_COLOR_BLACK);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
            deleteButton.setPreferredSize(new Dimension(65, 28));

            editorPanel.add(viewButton);
            editorPanel.add(editButton);
            editorPanel.add(deleteButton);

            viewButton.addActionListener(this);
            editButton.addActionListener(this);
            deleteButton.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.clickedRow = row;
            return editorPanel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();

            int maNghiPhep = (int) tableModel.getValueAt(clickedRow, 0);
            String hoten = (String) tableModel.getValueAt(clickedRow, 1);

            if (e.getSource() == viewButton) {
                controller.xemChiTietNghiPhep(maNghiPhep, hoten);
            } else if (e.getSource() == editButton) {
                controller.suaNghiPhep(maNghiPhep, hoten);
            } else if (e.getSource() == deleteButton) {
                controller.deleteNghiPhep(maNghiPhep, hoten); // Truyền hoten nếu cần cho thông báo xác nhận
            }
        }
    }

    // =========================================================
    //                 INNER CLASSES CHO DIALOGS
    // =========================================================

    // Thêm DateLabelFormatter vào đây hoặc tạo file riêng
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "dd/MM/yyyy";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    // 1. Inner Class cho AddNghiPhepDialog
    class AddNghiPhepDialog extends JDialog {
        private JComboBox<Integer> cbMaNhanVien;
        private JTextField txtTenNhanVien;
        private JComboBox<String> cbLoaiNghi;
        // Sử dụng JDatePickerImpl thay vì JDateChooser
        private JDatePickerImpl dpNgayBatDau, dpNgayKetThuc;
        private JTextField txtLyDo;
        private JComboBox<String> cbTrangThai;
        private JButton btnSave, btnCancel;

        private Map<Integer, String> danhSachNhanVien;

        public AddNghiPhepDialog(Map<Integer, String> danhSachNhanVien) {
            super(SwingUtilities.getWindowAncestor(NghiPhepView.this), "Thêm Đơn Nghỉ Phép Mới", Dialog.ModalityType.APPLICATION_MODAL);
            this.danhSachNhanVien = danhSachNhanVien;
            setSize(450, 480);
            setLocationRelativeTo(NghiPhepView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);

            formPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            cbMaNhanVien = new JComboBox<>();
            for (Integer maNV : danhSachNhanVien.keySet()) {
                cbMaNhanVien.addItem(maNV);
            }
            cbMaNhanVien.setFont(componentFont);
            cbMaNhanVien.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbMaNhanVien);

            formPanel.add(createLabel("Họ Tên Nhân Viên:", labelFont));
            txtTenNhanVien = createTextField(componentFont);
            txtTenNhanVien.setEditable(false);
            txtTenNhanVien.setBackground(new Color(240, 240, 240));
            formPanel.add(txtTenNhanVien);

            cbMaNhanVien.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Integer selectedMaNV = (Integer) cbMaNhanVien.getSelectedItem();
                    if (selectedMaNV != null) {
                        txtTenNhanVien.setText(danhSachNhanVien.get(selectedMaNV));
                    } else {
                        txtTenNhanVien.setText("");
                    }
                }
            });

            if (cbMaNhanVien.getItemCount() > 0) {
                cbMaNhanVien.setSelectedIndex(0);
            }

            formPanel.add(createLabel("Loại Nghỉ:", labelFont));
            cbLoaiNghi = new JComboBox<>(LOAI_NGHI_OPTIONS);
            cbLoaiNghi.setFont(componentFont);
            cbLoaiNghi.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbLoaiNghi);

            formPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            // Sử dụng JDatePickerImpl
            UtilDateModel modelNgayBatDau = new UtilDateModel();
            Properties p1 = new Properties();
            p1.put("text.today", "Today");
            p1.put("text.month", "Month");
            p1.put("text.year", "Year");
            JDatePanelImpl datePanelNgayBatDau = new JDatePanelImpl(modelNgayBatDau, p1);
            dpNgayBatDau = new JDatePickerImpl(datePanelNgayBatDau, new DateLabelFormatter());
            dpNgayBatDau.getJFormattedTextField().setFont(componentFont); // Set font for the text field
            dpNgayBatDau.getJFormattedTextField().setForeground(TEXT_COLOR_BLACK);
            formPanel.add(dpNgayBatDau);

            formPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            // Sử dụng JDatePickerImpl
            UtilDateModel modelNgayKetThuc = new UtilDateModel();
            Properties p2 = new Properties();
            p2.put("text.today", "Today");
            p2.put("text.month", "Month");
            p2.put("text.year", "Year");
            JDatePanelImpl datePanelNgayKetThuc = new JDatePanelImpl(modelNgayKetThuc, p2);
            dpNgayKetThuc = new JDatePickerImpl(datePanelNgayKetThuc, new DateLabelFormatter());
            dpNgayKetThuc.getJFormattedTextField().setFont(componentFont); // Set font for the text field
            dpNgayKetThuc.getJFormattedTextField().setForeground(TEXT_COLOR_BLACK);
            formPanel.add(dpNgayKetThuc);

            formPanel.add(createLabel("Lý Do:", labelFont));
            txtLyDo = createTextField(componentFont);
            formPanel.add(txtLyDo);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(TRANG_THAI_OPTIONS);
            cbTrangThai.setFont(componentFont);
            cbTrangThai.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbTrangThai);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnSave = new JButton("Lưu");
            btnCancel = new JButton("Hủy");

            btnSave.setFont(new Font("Arial", Font.BOLD, 16));
            btnSave.setBackground(PRIMARY_BLUE);
            btnSave.setForeground(TEXT_COLOR_BLACK);
            btnSave.setFocusPainted(false);

            btnCancel.setFont(new Font("Arial", Font.BOLD, 16));
            btnCancel.setBackground(GREY_CANCEL_BUTTON);
            btnCancel.setForeground(TEXT_COLOR_BLACK);
            btnCancel.setFocusPainted(false);

            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Integer selectedMaNV = (Integer) cbMaNhanVien.getSelectedItem();
                        if (selectedMaNV == null) {
                            JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Vui lòng chọn Mã Nhân Viên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int maNV = selectedMaNV;
                        String hoten = txtTenNhanVien.getText();
                        String loaiNghi = (String) cbLoaiNghi.getSelectedItem();

                        // Lấy ngày từ JDatePickerImpl
                        java.util.Date ngayBDUtil = (java.util.Date) dpNgayBatDau.getModel().getValue();
                        java.util.Date ngayKTUtil = (java.util.Date) dpNgayKetThuc.getModel().getValue();

                        if (ngayBDUtil == null) {
                            JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Ngày bắt đầu không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (ngayKTUtil == null) {
                            JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Ngày kết thúc không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (ngayBDUtil.after(ngayKTUtil)) {
                            JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Ngày kết thúc không thể trước ngày bắt đầu.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String lyDo = txtLyDo.getText();
                        if (lyDo.isEmpty()) {
                            JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Lý do không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String trangThai = (String) cbTrangThai.getSelectedItem();

                        // Chuyển đổi java.util.Date sang java.sql.Date
                        NghiPhepModel newNghiPhep = new NghiPhepModel(0, maNV, loaiNghi, new java.sql.Date(ngayBDUtil.getTime()), new java.sql.Date(ngayKTUtil.getTime()), lyDo, trangThai, hoten);

                        controller.addNghiPhep(newNghiPhep);
                        dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AddNghiPhepDialog.this, "Lỗi khi thêm nghỉ phép: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    // 2. Inner Class cho ViewNghiPhepDialog (giữ nguyên)
    class ViewNghiPhepDialog extends JDialog {
        private JLabel lblMaNghiPhep, lblMaNhanVien, lblHoTen, lblLoaiNghi;
        private JLabel lblNgayBatDau, lblNgayKetThuc, lblLyDo, lblTrangThai;
        private JButton btnClose;

        public ViewNghiPhepDialog(NghiPhepModel nghiPhep) {
            super(SwingUtilities.getWindowAncestor(NghiPhepView.this), "Thêm Đơn Nghỉ Phép Mới", Dialog.ModalityType.APPLICATION_MODAL);
            setSize(400, 350);
            setLocationRelativeTo(NghiPhepView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font valueFont = new Font("Arial", Font.BOLD, 14);

            String ngayBatDauStr = (nghiPhep.getNgayBatDau() != null) ? dateFormat.format(nghiPhep.getNgayBatDau()) : "N/A";
            String ngayKetThucStr = (nghiPhep.getNgayKetThuc() != null) ? dateFormat.format(nghiPhep.getNgayKetThuc()) : "N/A";

            infoPanel.add(createLabel("Mã Nghỉ Phép:", labelFont));
            lblMaNghiPhep = createLabel(String.valueOf(nghiPhep.getMaNghiPhep()), valueFont);
            infoPanel.add(lblMaNghiPhep);

            infoPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            lblMaNhanVien = createLabel(String.valueOf(nghiPhep.getMaNhanVien()), valueFont);
            infoPanel.add(lblMaNhanVien);

            infoPanel.add(createLabel("Họ Tên:", labelFont));
            lblHoTen = createLabel(nghiPhep.getHoten(), valueFont);
            infoPanel.add(lblHoTen);

            infoPanel.add(createLabel("Loại Nghỉ:", labelFont));
            lblLoaiNghi = createLabel(formatLoaiNghi(nghiPhep.getLoaiNghi()), valueFont); // Apply formatting
            infoPanel.add(lblLoaiNghi);

            infoPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            lblNgayBatDau = createLabel(ngayBatDauStr, valueFont);
            infoPanel.add(lblNgayBatDau);

            infoPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            lblNgayKetThuc = createLabel(ngayKetThucStr, valueFont);
            infoPanel.add(lblNgayKetThuc);

            infoPanel.add(createLabel("Lý Do:", labelFont));
            lblLyDo = createLabel(nghiPhep.getLyDo(), valueFont);
            infoPanel.add(lblLyDo);

            infoPanel.add(createLabel("Trạng Thái:", labelFont));
            lblTrangThai = createLabel(formatTrangThai(nghiPhep.getTrangThai()), valueFont); // Apply formatting
            infoPanel.add(lblTrangThai);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnClose = new JButton("Đóng");
            btnClose.setFont(new Font("Arial", Font.BOLD, 16));
            btnClose.setBackground(GREY_CANCEL_BUTTON);
            btnClose.setForeground(TEXT_COLOR_BLACK);
            btnClose.setFocusPainted(false);

            btnClose.addActionListener(e -> dispose());
            buttonPanel.add(btnClose);

            add(infoPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    // 3. Inner Class cho EditNghiPhepDialog
    class EditNghiPhepDialog extends JDialog {
        private JTextField txtMaNghiPhep, txtMaNhanVien, txtHoTen;
        private JComboBox<String> cbLoaiNghi;
        // Sử dụng JDatePickerImpl thay vì JDateChooser
        private JDatePickerImpl dpNgayBatDau, dpNgayKetThuc;
        private JTextField txtLyDo;
        private JComboBox<String> cbTrangThai;
        private JButton btnSave, btnCancel;

        private NghiPhepModel originalNghiPhep;
        private Map<Integer, String> danhSachNhanVien; // Thêm danh sách nhân viên

        public EditNghiPhepDialog(NghiPhepModel nghiPhepToEdit, Map<Integer, String> danhSachNhanVien) {
            super(SwingUtilities.getWindowAncestor(NghiPhepView.this), "Thêm Đơn Nghỉ Phép Mới", Dialog.ModalityType.APPLICATION_MODAL);
            this.originalNghiPhep = nghiPhepToEdit;
            this.danhSachNhanVien = danhSachNhanVien; // Gán danh sách nhân viên
            setSize(450, 500);
            setLocationRelativeTo(NghiPhepView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);
            Font disabledTextFieldFont = new Font("Arial", Font.BOLD, 14);

            formPanel.add(createLabel("Mã Nghỉ Phép:", labelFont));
            txtMaNghiPhep = createTextField(disabledTextFieldFont);
            txtMaNghiPhep.setText(String.valueOf(originalNghiPhep.getMaNghiPhep()));
            txtMaNghiPhep.setEditable(false);
            txtMaNghiPhep.setBackground(new Color(240, 240, 240));
            txtMaNghiPhep.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(txtMaNghiPhep);

            formPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            txtMaNhanVien = createTextField(componentFont);
            txtMaNhanVien.setEditable(false);
            txtMaNhanVien.setText(String.valueOf(originalNghiPhep.getMaNhanVien()));
            txtMaNhanVien.setBackground(new Color(240, 240, 240)); // Ensure disabled fields are consistent
            formPanel.add(txtMaNhanVien);

            formPanel.add(createLabel("Họ Tên Nhân Viên:", labelFont));
            txtHoTen = createTextField(componentFont);
            txtHoTen.setEditable(false);
            txtHoTen.setText(originalNghiPhep.getHoten());
            txtHoTen.setBackground(new Color(240, 240, 240));
            formPanel.add(txtHoTen);

            formPanel.add(createLabel("Loại Nghỉ:", labelFont));
            cbLoaiNghi = new JComboBox<>(LOAI_NGHI_OPTIONS);
            cbLoaiNghi.setSelectedItem(originalNghiPhep.getLoaiNghi());
            cbLoaiNghi.setFont(componentFont);
            cbLoaiNghi.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbLoaiNghi);

            formPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            // Sử dụng JDatePickerImpl
            UtilDateModel modelNgayBatDau = new UtilDateModel();
            if (originalNghiPhep.getNgayBatDau() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(originalNghiPhep.getNgayBatDau());
                modelNgayBatDau.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                modelNgayBatDau.setSelected(true);
            }
            Properties p1 = new Properties();
            p1.put("text.today", "Today");
            p1.put("text.month", "Month");
            p1.put("text.year", "Year");
            JDatePanelImpl datePanelNgayBatDau = new JDatePanelImpl(modelNgayBatDau, p1);
            dpNgayBatDau = new JDatePickerImpl(datePanelNgayBatDau, new DateLabelFormatter());
            dpNgayBatDau.getJFormattedTextField().setFont(componentFont); // Set font for the text field
            dpNgayBatDau.getJFormattedTextField().setForeground(TEXT_COLOR_BLACK);
            formPanel.add(dpNgayBatDau);

            formPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            // Sử dụng JDatePickerImpl
            UtilDateModel modelNgayKetThuc = new UtilDateModel();
            if (originalNghiPhep.getNgayKetThuc() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(originalNghiPhep.getNgayKetThuc());
                modelNgayKetThuc.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                modelNgayKetThuc.setSelected(true);
            }
            Properties p2 = new Properties();
            p2.put("text.today", "Today");
            p2.put("text.month", "Month");
            p2.put("text.year", "Year");
            JDatePanelImpl datePanelNgayKetThuc = new JDatePanelImpl(modelNgayKetThuc, p2);
            dpNgayKetThuc = new JDatePickerImpl(datePanelNgayKetThuc, new DateLabelFormatter());
            dpNgayKetThuc.getJFormattedTextField().setFont(componentFont); // Set font for the text field
            dpNgayKetThuc.getJFormattedTextField().setForeground(TEXT_COLOR_BLACK);
            formPanel.add(dpNgayKetThuc);

            formPanel.add(createLabel("Lý Do:", labelFont));
            txtLyDo = createTextField(componentFont);
            txtLyDo.setText(originalNghiPhep.getLyDo());
            formPanel.add(txtLyDo);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(TRANG_THAI_OPTIONS);
            cbTrangThai.setSelectedItem(originalNghiPhep.getTrangThai());
            cbTrangThai.setFont(componentFont);
            cbTrangThai.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbTrangThai);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnSave = new JButton("Cập Nhật");
            btnCancel = new JButton("Hủy");

            btnSave.setFont(new Font("Arial", Font.BOLD, 16));
            btnSave.setBackground(PRIMARY_BLUE);
            btnSave.setForeground(TEXT_COLOR_BLACK);
            btnSave.setFocusPainted(false);

            btnCancel.setFont(new Font("Arial", Font.BOLD, 16));
            btnCancel.setBackground(GREY_CANCEL_BUTTON);
            btnCancel.setForeground(TEXT_COLOR_BLACK);
            btnCancel.setFocusPainted(false);

            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int maNP = originalNghiPhep.getMaNghiPhep();
                        int maNV = Integer.parseInt(txtMaNhanVien.getText());
                        String hoten = txtHoTen.getText();
                        String loaiNghi = (String) cbLoaiNghi.getSelectedItem();

                        // Lấy ngày từ JDatePickerImpl
                        java.util.Date ngayBDUtil = (java.util.Date) dpNgayBatDau.getModel().getValue();
                        java.util.Date ngayKTUtil = (java.util.Date) dpNgayKetThuc.getModel().getValue();

                        if (ngayBDUtil == null) {
                            JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Ngày bắt đầu không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (ngayKTUtil == null) {
                            JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Ngày kết thúc không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (ngayBDUtil.after(ngayKTUtil)) {
                            JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Ngày kết thúc không thể trước ngày bắt đầu.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String lyDo = txtLyDo.getText();
                        if (lyDo.isEmpty()) {
                            JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Lý do không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String trangThai = (String) cbTrangThai.getSelectedItem();

                        // Chuyển đổi java.util.Date sang java.sql.Date
                        NghiPhepModel updatedNghiPhep = new NghiPhepModel(maNP, maNV, loaiNghi, new java.sql.Date(ngayBDUtil.getTime()), new java.sql.Date(ngayKTUtil.getTime()), lyDo, trangThai, hoten);

                        controller.updateNghiPhep(updatedNghiPhep);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Mã nhân viên phải là số nguyên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EditNghiPhepDialog.this, "Lỗi khi cập nhật nghỉ phép: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    // Helper method to create consistent JLabels
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR_BLACK);
        return label;
    }

    // Helper method to create consistent JTextFields
    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setForeground(TEXT_COLOR_BLACK);
        return textField;
    }

//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new NghiPhepView().setVisible(true);
//            }
//        });
//    }
}