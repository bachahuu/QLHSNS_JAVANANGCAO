/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view_admin;

import controller.HopDongController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import model.HopDongModel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.GridLayout;
import javax.swing.SwingUtilities;

/**
 *
 * @author LAPTOP
 */
public class HopDongView extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnThemHopDong, btnXuatFile;
    private JList<HopDongModel> list;
    private DefaultListModel<HopDongModel> listModel;
    private HopDongController controller;

    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color GREEN_VIEW_BUTTON = new Color(46, 204, 113);
    private final Color ORANGE_EDIT_BUTTON = new Color(241, 196, 15);
    private final Color RED_DELETE_BUTTON = new Color(231, 76, 60);
    private final Color GREY_CANCEL_BUTTON = new Color(189, 195, 199);
    // Màu chữ chính cho tất cả các text trên các thành phần (label, textfield, combobox, table header, table content)
    private final Color TEXT_COLOR_BLACK = Color.BLACK;

    // Đối tượng SimpleDateFormat dùng chung để parse và format ngày tháng
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public HopDongView() {
        setTitle("Quản lý hợp đồng");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controller = new HopDongController();
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

        btnThemHopDong = new JButton("Thêm hợp đồng");
        btnThemHopDong.setFont(new Font("Arial", Font.BOLD, 14));
        btnThemHopDong.setBackground(PRIMARY_BLUE);
        btnThemHopDong.setForeground(TEXT_COLOR_BLACK);
        btnThemHopDong.setFocusPainted(false);

        btnXuatFile = new JButton("Xuất file");
        btnXuatFile.setFont(new Font("Arial", Font.BOLD, 14));
        btnXuatFile.setBackground(PRIMARY_BLUE);
        btnXuatFile.setForeground(TEXT_COLOR_BLACK);
        btnXuatFile.setFocusPainted(false);

        // Thêm các thành phần tìm kiếm vào bên trái
        pnNorth.add(lblTimKiem);
        pnNorth.add(txtTimKiem);
        pnNorth.add(Box.createHorizontalStrut(10));
        pnNorth.add(btnTimKiem);

        // Thêm khoảng trống co giãn để đẩy các nút "Thêm" và "Xuất file" sang phải
        pnNorth.add(Box.createHorizontalGlue());

        // Thêm các nút "Thêm hợp đồng" và "Xuất file" vào bên phải
        pnNorth.add(btnThemHopDong);
        pnNorth.add(Box.createHorizontalStrut(10)); // Khoảng cách giữa 2 nút
        pnNorth.add(btnXuatFile);


        add(pnNorth, BorderLayout.NORTH);

        // ===== TABLE CENTER =====
        JPanel pnCenter = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách hợp đồng");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder.setTitleColor(TEXT_COLOR_BLACK);
        pnCenter.setBorder(titledBorder);


        String[] columnNames = {
            "Mã hợp đồng", "Họ tên", "Loại hợp đồng",
            "Ngày bắt đầu", "Ngày kết thúc", "Ngày ký", "Trạng Thái", "Thao tác"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(TEXT_COLOR_BLACK);

        JScrollPane sp = new JScrollPane(table);

        // Đặt Renderer và Editor cho cột "Thao tác"
        table.getColumn("Thao tác").setCellRenderer(new ButtonRenderer());
        table.getColumn("Thao tác").setCellEditor(new ButtonEditor(new JTextField()));
        table.getColumn("Thao tác").setPreferredWidth(210);

        pnCenter.add(sp, BorderLayout.CENTER);
        add(pnCenter, BorderLayout.CENTER);

        // Gắn sự kiện cho các nút
        addEventHandlers();

        // Tải dữ liệu ban đầu
        loadDataToTable();
    }

    private void loadDataToTable() {
        controller.loadAllHopDong();
    }

    private void addEventHandlers() {
        btnThemHopDong.addActionListener(e -> controller.themHopDong());
        btnTimKiem.addActionListener(e -> controller.timKiemHopDong(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> controller.timKiemHopDong(txtTimKiem.getText()));
        btnXuatFile.addActionListener(e -> controller.xuatFile());
    }

    public void hienThiDanhSachHopDong(List<HopDongModel> danhSach) {
        tableModel.setRowCount(0);

        for (HopDongModel hd : danhSach) {
            String ngayBatDauStr = (hd.getNgayBatDau() != null) ? dateFormat.format(hd.getNgayBatDau()) : "";
            String ngayKetThucStr = (hd.getNgayKetThuc() != null) ? dateFormat.format(hd.getNgayKetThuc()) : "";
            String ngayKyStr = (hd.getNgayKy() != null) ? dateFormat.format(hd.getNgayKy()) : "";

            tableModel.addRow(new Object[]{
                hd.getMaHopDong(),
                hd.getHoten(),
                hd.getLoaiHopDong().name(),
                ngayBatDauStr,
                ngayKetThucStr,
                ngayKyStr,
                hd.getTrangThai().name(),
                ""
            });
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

    public void openAddHopDongDialog() {
        AddHopDongDialog addDialog = new AddHopDongDialog();
        addDialog.setVisible(true);
    }

    public void openViewHopDongDialog(HopDongModel hopDong) {
        ViewHopDongDialog viewDialog = new ViewHopDongDialog(hopDong);
        viewDialog.setVisible(true);
    }

    public void openEditHopDongDialog(HopDongModel hopDongToEdit) {
        EditHopDongDialog editDialog = new EditHopDongDialog(hopDongToEdit);
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

            // Màu sắc và font cho nút consistent với PhongBanView
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

            int maHopDong = (int) tableModel.getValueAt(clickedRow, 0);
            String hoten = (String) tableModel.getValueAt(clickedRow, 1);

            if (e.getSource() == viewButton) {
                controller.xemChiTietHopDong(maHopDong, hoten);
            } else if (e.getSource() == editButton) {
                controller.suaHopDong(maHopDong, hoten);
            } else if (e.getSource() == deleteButton) {
                controller.xoaHopDong(maHopDong, hoten);
            }
        }
    }

    // =========================================================
    //                  INNER CLASSES CHO DIALOGS
    // =========================================================

    // 1. Inner Class cho AddHopDongDialog
    class AddHopDongDialog extends JDialog {
        private JTextField txtMaNhanVien, txtHoTen;
        private JComboBox<HopDongModel.LoaiHopDong> cbLoaiHopDong;
        private JTextField txtNgayBatDau, txtNgayKetThuc, txtNgayKy; // Revert to JTextField
        private JComboBox<HopDongModel.TrangThaiHopDong> cbTrangThai;
        private JButton btnSave, btnCancel;

        public AddHopDongDialog() {
            super(HopDongView.this, "Thêm Hợp Đồng Mới", true);
            setSize(400, 450);
            setLocationRelativeTo(HopDongView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);

            formPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            txtMaNhanVien = createTextField(componentFont);
            formPanel.add(txtMaNhanVien);

            formPanel.add(createLabel("Họ Tên:", labelFont));
            txtHoTen = createTextField(componentFont);
            formPanel.add(txtHoTen);

            formPanel.add(createLabel("Loại Hợp Đồng:", labelFont));
            cbLoaiHopDong = new JComboBox<>(HopDongModel.LoaiHopDong.values());
            cbLoaiHopDong.setFont(componentFont);
            cbLoaiHopDong.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbLoaiHopDong);

            formPanel.add(createLabel("Ngày Bắt Đầu (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayBatDau = createTextField(componentFont);
            formPanel.add(txtNgayBatDau);

            formPanel.add(createLabel("Ngày Kết Thúc (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayKetThuc = createTextField(componentFont);
            formPanel.add(txtNgayKetThuc);

            formPanel.add(createLabel("Ngày Ký (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayKy = createTextField(componentFont);
            formPanel.add(txtNgayKy);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(HopDongModel.TrangThaiHopDong.values());
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
                        int maNV = Integer.parseInt(txtMaNhanVien.getText());
                        String hoten = txtHoTen.getText();
                        HopDongModel.LoaiHopDong loaiHD = (HopDongModel.LoaiHopDong) cbLoaiHopDong.getSelectedItem();

                        // Parse ngày tháng từ JTextField
                        Date ngayBD = null;
                        if (!txtNgayBatDau.getText().isEmpty()) {
                            ngayBD = dateFormat.parse(txtNgayBatDau.getText());
                        }
                        Date ngayKT = null;
                        if (!txtNgayKetThuc.getText().isEmpty()) {
                            ngayKT = dateFormat.parse(txtNgayKetThuc.getText());
                        }
                        Date ngayKy = null;
                        if (!txtNgayKy.getText().isEmpty()) {
                            ngayKy = dateFormat.parse(txtNgayKy.getText());
                        }

                        HopDongModel.TrangThaiHopDong trangThai = (HopDongModel.TrangThaiHopDong) cbTrangThai.getSelectedItem();

                        HopDongModel newHopDong = new HopDongModel(0, maNV, hoten, loaiHD, ngayBD, ngayKT, ngayKy, trangThai);

                        controller.addHopDong(newHopDong);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AddHopDongDialog.this, "Mã nhân viên phải là số nguyên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(AddHopDongDialog.this, "Định dạng ngày tháng không hợp lệ (dd/MM/yyyy).", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AddHopDongDialog.this, "Lỗi khi thêm hợp đồng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    // 2. Inner Class cho ViewHopDongDialog (Không thay đổi)
    class ViewHopDongDialog extends JDialog {
        private JLabel lblMaHopDong, lblMaNhanVien, lblHoTen, lblLoaiHopDong;
        private JLabel lblNgayBatDau, lblNgayKetThuc, lblNgayKy, lblTrangThai;
        private JButton btnClose;

        public ViewHopDongDialog(HopDongModel hopDong) {
            super(HopDongView.this, "Chi Tiết Hợp Đồng", true);
            setSize(400, 350);
            setLocationRelativeTo(HopDongView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font valueFont = new Font("Arial", Font.BOLD, 14);

            String ngayBatDauStr = (hopDong.getNgayBatDau() != null) ? dateFormat.format(hopDong.getNgayBatDau()) : "N/A";
            String ngayKetThucStr = (hopDong.getNgayKetThuc() != null) ? dateFormat.format(hopDong.getNgayKetThuc()) : "N/A";
            String ngayKyStr = (hopDong.getNgayKy() != null) ? dateFormat.format(hopDong.getNgayKy()) : "N/A";

            infoPanel.add(createLabel("Mã Hợp Đồng:", labelFont));
            lblMaHopDong = createLabel(String.valueOf(hopDong.getMaHopDong()), valueFont);
            infoPanel.add(lblMaHopDong);

            infoPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            lblMaNhanVien = createLabel(String.valueOf(hopDong.getMaNhanVien()), valueFont);
            infoPanel.add(lblMaNhanVien);

            infoPanel.add(createLabel("Họ Tên:", labelFont));
            lblHoTen = createLabel(hopDong.getHoten(), valueFont);
            infoPanel.add(lblHoTen);

            infoPanel.add(createLabel("Loại Hợp Đồng:", labelFont));
            lblLoaiHopDong = createLabel(hopDong.getLoaiHopDong().name(), valueFont);
            infoPanel.add(lblLoaiHopDong);

            infoPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            lblNgayBatDau = createLabel(ngayBatDauStr, valueFont);
            infoPanel.add(lblNgayBatDau);

            infoPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            lblNgayKetThuc = createLabel(ngayKetThucStr, valueFont);
            infoPanel.add(lblNgayKetThuc);

            infoPanel.add(createLabel("Ngày Ký:", labelFont));
            lblNgayKy = createLabel(ngayKyStr, valueFont);
            infoPanel.add(lblNgayKy);

            infoPanel.add(createLabel("Trạng Thái:", labelFont));
            lblTrangThai = createLabel(hopDong.getTrangThai().name(), valueFont);
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

    // 3. Inner Class cho EditHopDongDialog
    class EditHopDongDialog extends JDialog {
        private JTextField txtMaHopDong, txtMaNhanVien, txtHoTen;
        private JComboBox<HopDongModel.LoaiHopDong> cbLoaiHopDong;
        private JTextField txtNgayBatDau, txtNgayKetThuc, txtNgayKy; // Revert to JTextField
        private JComboBox<HopDongModel.TrangThaiHopDong> cbTrangThai;
        private JButton btnSave, btnCancel;

        private HopDongModel originalHopDong;

        public EditHopDongDialog(HopDongModel hopDongToEdit) {
            super(HopDongView.this, "Sửa Thông Tin Hợp Đồng", true);
            this.originalHopDong = hopDongToEdit;
            setSize(400, 500);
            setLocationRelativeTo(HopDongView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);
            Font disabledTextFieldFont = new Font("Arial", Font.BOLD, 14);

            formPanel.add(createLabel("Mã Hợp Đồng:", labelFont));
            txtMaHopDong = createTextField(disabledTextFieldFont);
            txtMaHopDong.setText(String.valueOf(originalHopDong.getMaHopDong()));
            txtMaHopDong.setEditable(false);
            txtMaHopDong.setBackground(new Color(240, 240, 240));
            txtMaHopDong.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(txtMaHopDong);

            formPanel.add(createLabel("Mã Nhân Viên:", labelFont));
            txtMaNhanVien = createTextField(componentFont);
            txtMaNhanVien.setEditable(false);
            txtMaNhanVien.setText(String.valueOf(originalHopDong.getMaNhanVien()));
            formPanel.add(txtMaNhanVien);

            formPanel.add(createLabel("Họ Tên Nhân Viên:", labelFont));
            txtHoTen = createTextField(componentFont);
            txtHoTen.setEditable(false);
            txtHoTen.setText(originalHopDong.getHoten());
            formPanel.add(txtHoTen);

            formPanel.add(createLabel("Loại Hợp Đồng:", labelFont));
            cbLoaiHopDong = new JComboBox<>(HopDongModel.LoaiHopDong.values());
            cbLoaiHopDong.setSelectedItem(originalHopDong.getLoaiHopDong());
            cbLoaiHopDong.setFont(componentFont);
            cbLoaiHopDong.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbLoaiHopDong);

            formPanel.add(createLabel("Ngày Bắt Đầu (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayBatDau = createTextField(componentFont);
            // Hiển thị ngày hiện có theo định dạng dd/MM/yyyy
            if (originalHopDong.getNgayBatDau() != null) {
                txtNgayBatDau.setText(dateFormat.format(originalHopDong.getNgayBatDau()));
            }
            formPanel.add(txtNgayBatDau);

            formPanel.add(createLabel("Ngày Kết Thúc (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayKetThuc = createTextField(componentFont);
            if (originalHopDong.getNgayKetThuc() != null) {
                txtNgayKetThuc.setText(dateFormat.format(originalHopDong.getNgayKetThuc()));
            }
            formPanel.add(txtNgayKetThuc);

            formPanel.add(createLabel("Ngày Ký (dd/MM/yyyy):", labelFont)); // Hướng dẫn định dạng
            txtNgayKy = createTextField(componentFont);
            if (originalHopDong.getNgayKy() != null) {
                txtNgayKy.setText(dateFormat.format(originalHopDong.getNgayKy()));
            }
            formPanel.add(txtNgayKy);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(HopDongModel.TrangThaiHopDong.values());
            cbTrangThai.setSelectedItem(originalHopDong.getTrangThai());
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
                        int maHD = originalHopDong.getMaHopDong();
                        int maNV = Integer.parseInt(txtMaNhanVien.getText());
                        String hoten = txtHoTen.getText();
                        HopDongModel.LoaiHopDong loaiHD = (HopDongModel.LoaiHopDong) cbLoaiHopDong.getSelectedItem();

                        // Parse ngày tháng từ JTextField
                        Date ngayBD = null;
                        if (!txtNgayBatDau.getText().isEmpty()) {
                            ngayBD = dateFormat.parse(txtNgayBatDau.getText());
                        }
                        Date ngayKT = null;
                        if (!txtNgayKetThuc.getText().isEmpty()) {
                            ngayKT = dateFormat.parse(txtNgayKetThuc.getText());
                        }
                        Date ngayKy = null;
                        if (!txtNgayKy.getText().isEmpty()) {
                            ngayKy = dateFormat.parse(txtNgayKy.getText());
                        }

                        HopDongModel.TrangThaiHopDong trangThai = (HopDongModel.TrangThaiHopDong) cbTrangThai.getSelectedItem();

                        HopDongModel updatedHopDong = new HopDongModel(maHD, maNV, hoten, loaiHD, ngayBD, ngayKT, ngayKy, trangThai);

                        controller.updateHopDong(updatedHopDong);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(EditHopDongDialog.this, "Mã nhân viên phải là số nguyên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(EditHopDongDialog.this, "Định dạng ngày tháng không hợp lệ (dd/MM/yyyy).", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(EditHopDongDialog.this, "Lỗi khi cập nhật hợp đồng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HopDongView().setVisible(true);
            }
        });
    }
}