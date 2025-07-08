package view_admin;

import controller.HopDongController;
import controller.HopDongController.NhanVienItem;
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
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;
import javax.swing.JFormattedTextField;
import java.util.Calendar;


/**
 *
 * @author LAPTOP
 */
public class HopDongView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnThemHopDong;
    private JList<HopDongModel> list;
    private DefaultListModel<HopDongModel> listModel;
    private HopDongController controller;

    private final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private final Color GREEN_VIEW_BUTTON = new Color(46, 204, 113);
    private final Color ORANGE_EDIT_BUTTON = new Color(241, 196, 15);
    private final Color RED_DELETE_BUTTON = new Color(231, 76, 60);
    private final Color GREY_CANCEL_BUTTON = new Color(189, 195, 199);
    private final Color TEXT_COLOR_BLACK = Color.BLACK;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);

    public HopDongView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        controller = new HopDongController();
        controller.setView(this);

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

        pnNorth.add(lblTimKiem);
        pnNorth.add(txtTimKiem);
        pnNorth.add(Box.createHorizontalStrut(10));
        pnNorth.add(btnTimKiem);

        pnNorth.add(Box.createHorizontalGlue());

        pnNorth.add(btnThemHopDong);

        add(pnNorth, BorderLayout.NORTH);

        JPanel pnCenter = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách hợp đồng");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
        titledBorder.setTitleColor(TEXT_COLOR_BLACK);
        pnCenter.setBorder(titledBorder);


        String[] columnNames = {
            "Mã hợp đồng", "Họ tên", "Loại hợp đồng",
            "Ngày bắt đầu", "Ngày kết thúc", "Ngày ký", "Trạng Thái", "Lương cơ bản", "Thao tác"
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
        controller.loadAllHopDong();
    }

    private void addEventHandlers() {
        btnThemHopDong.addActionListener(e -> controller.themHopDong());
        btnTimKiem.addActionListener(e -> controller.timKiemHopDong(txtTimKiem.getText()));
        txtTimKiem.addActionListener(e -> controller.timKiemHopDong(txtTimKiem.getText()));
    }

    public void hienThiDanhSachHopDong(List<HopDongModel> danhSach) {
        tableModel.setRowCount(0);

        for (HopDongModel hd : danhSach) {
            String ngayBatDauStr = (hd.getNgayBatDau() != null) ? dateFormat.format(hd.getNgayBatDau()) : "";
            String ngayKetThucStr = (hd.getNgayKetThuc() != null) ? dateFormat.format(hd.getNgayKetThuc()) : "";
            String ngayKyStr = (hd.getNgayKy() != null) ? dateFormat.format(hd.getNgayKy()) : "";
            String luongCoBanFormatted = currencyFormat.format(hd.getLuongCoBan());

            tableModel.addRow(new Object[]{
                hd.getMaHopDong(),
                hd.getHoten(),
                hd.getLoaiHopDong().toString(),
                ngayBatDauStr,
                ngayKetThucStr,
                ngayKyStr,
                hd.getTrangThai().toString(),
                luongCoBanFormatted,
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

    // 1. Inner Class cho AddHopDongDialog
    class AddHopDongDialog extends JDialog {
        private JComboBox<NhanVienItem> cbNhanVien;
        // private JLabel lblHoTenDisplay; // REMOVE this line
        private JTextField txtLuongCoBan;
        private JComboBox<HopDongModel.LoaiHopDong> cbLoaiHopDong;
        private JDatePickerImpl datePickerNgayBatDau, datePickerNgayKetThuc, datePickerNgayKy;
        private JComboBox<HopDongModel.TrangThaiHopDong> cbTrangThai;
        private JButton btnSave, btnCancel;

        public AddHopDongDialog() {
            super(SwingUtilities.getWindowAncestor(HopDongView.this), "Thêm Hợp Đồng Mới", Dialog.ModalityType.APPLICATION_MODAL);
            setSize(400, 500);
            setLocationRelativeTo(HopDongView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);

            formPanel.add(createLabel("Nhân Viên:", labelFont));
            cbNhanVien = new JComboBox<>();
            cbNhanVien.setFont(componentFont);
            cbNhanVien.setForeground(TEXT_COLOR_BLACK);
            cbNhanVien.setEditable(false); // Make ComboBox not editable
            loadNhanVienToComboBox(cbNhanVien);
            formPanel.add(cbNhanVien);
            
            // REMOVE the JLabel for displaying hoten
            // formPanel.add(createLabel("Họ Tên:", labelFont));
            // lblHoTenDisplay = createLabel("", componentFont);
            // lblHoTenDisplay.setForeground(TEXT_COLOR_BLACK);
            // formPanel.add(lblHoTenDisplay);

            // REMOVE the ActionListener as hoten is now taken directly from selected item
            // cbNhanVien.addActionListener(new ActionListener() {
            //     @Override
            //     public void actionPerformed(ActionEvent e) {
            //         NhanVienItem selectedItem = (NhanVienItem) cbNhanVien.getSelectedItem();
            //         if (selectedItem != null) {
            //             lblHoTenDisplay.setText(selectedItem.getHoTen());
            //         } else {
            //             lblHoTenDisplay.setText("");
            //         }
            //     }
            // });
            // if (cbNhanVien.getSelectedItem() != null) {
            //     lblHoTenDisplay.setText(((NhanVienItem) cbNhanVien.getSelectedItem()).getHoTen());
            // }

            formPanel.add(createLabel("Loại Hợp Đồng:", labelFont));
            cbLoaiHopDong = new JComboBox<>(HopDongModel.LoaiHopDong.values());
            cbLoaiHopDong.setFont(componentFont);
            cbLoaiHopDong.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbLoaiHopDong);

            // JDatePicker cho Ngày Bắt Đầu
            formPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            datePickerNgayBatDau = createDatePicker(null);
            formPanel.add(datePickerNgayBatDau);

            // JDatePicker cho Ngày Kết Thúc
            formPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            datePickerNgayKetThuc = createDatePicker(null);
            formPanel.add(datePickerNgayKetThuc);

            // JDatePicker cho Ngày Ký
            formPanel.add(createLabel("Ngày Ký:", labelFont));
            datePickerNgayKy = createDatePicker(null);
            formPanel.add(datePickerNgayKy);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(HopDongModel.TrangThaiHopDong.values());
            cbTrangThai.setFont(componentFont);
            cbTrangThai.setForeground(TEXT_COLOR_BLACK);
            formPanel.add(cbTrangThai);

            formPanel.add(createLabel("Lương cơ bản:", labelFont));
            txtLuongCoBan = createTextField(componentFont);
            formPanel.add(txtLuongCoBan);


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
                        NhanVienItem selectedNhanVien = (NhanVienItem) cbNhanVien.getSelectedItem();
                        if (selectedNhanVien == null) {
                            JOptionPane.showMessageDialog(AddHopDongDialog.this, "Vui lòng chọn một nhân viên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Date ngayBD = (Date) datePickerNgayBatDau.getModel().getValue();
                        Date ngayKT = (Date) datePickerNgayKetThuc.getModel().getValue();
                        Date ngayKy = (Date) datePickerNgayKy.getModel().getValue();

                        if (ngayBD == null) {
                            JOptionPane.showMessageDialog(AddHopDongDialog.this, "Ngày bắt đầu không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (ngayKy == null) {
                            JOptionPane.showMessageDialog(AddHopDongDialog.this, "Ngày ký không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (txtLuongCoBan.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(AddHopDongDialog.this, "Lương cơ bản không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        int maNV = selectedNhanVien.getMaNhanVien();
                        String hoten = selectedNhanVien.getHoTen(); // Get hoten directly from selected NhanVienItem
                        HopDongModel.LoaiHopDong loaiHD = (HopDongModel.LoaiHopDong) cbLoaiHopDong.getSelectedItem();
                        HopDongModel.TrangThaiHopDong trangThai = (HopDongModel.TrangThaiHopDong) cbTrangThai.getSelectedItem();

                        double luongCB = Double.parseDouble(txtLuongCoBan.getText().replace(",", ""));

                        HopDongModel newHopDong = new HopDongModel(0, maNV, hoten, loaiHD, ngayBD, ngayKT, ngayKy, trangThai, luongCB);

                        controller.addHopDong(newHopDong);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AddHopDongDialog.this, "Lương cơ bản phải là số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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

        private void loadNhanVienToComboBox(JComboBox<NhanVienItem> comboBox) {
            List<NhanVienItem> nhanVienList = controller.getAllNhanVien();
            comboBox.removeAllItems();
            for (NhanVienItem nv : nhanVienList) {
                comboBox.addItem(nv);
            }
        }
    }

    // 2. Inner Class cho ViewHopDongDialog (không thay đổi)
    class ViewHopDongDialog extends JDialog {
        private JLabel lblMaHopDong, lblMaNhanVien, lblHoTen, lblLoaiHopDong;
        private JLabel lblNgayBatDau, lblNgayKetThuc, lblNgayKy, lblTrangThai, lblLuongCoBan;
        private JButton btnClose;

        public ViewHopDongDialog(HopDongModel hopDong) {
           super(SwingUtilities.getWindowAncestor(HopDongView.this), "Chi Tiết Hợp Đồng", Dialog.ModalityType.APPLICATION_MODAL);
            setSize(400, 400);
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
            lblLoaiHopDong = createLabel(hopDong.getLoaiHopDong().toString(), valueFont);
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
            lblTrangThai = createLabel(hopDong.getTrangThai().toString(), valueFont);
            infoPanel.add(lblTrangThai);

            infoPanel.add(createLabel("Lương cơ bản:", labelFont));
            lblLuongCoBan = createLabel(currencyFormat.format(hopDong.getLuongCoBan()), valueFont);
            infoPanel.add(lblLuongCoBan);


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
        private JTextField txtMaHopDong;
        private JComboBox<NhanVienItem> cbNhanVien;
        // private JLabel lblHoTenDisplay; // REMOVE this line
        private JTextField txtLuongCoBan;
        private JComboBox<HopDongModel.LoaiHopDong> cbLoaiHopDong;
        private JDatePickerImpl datePickerNgayBatDau, datePickerNgayKetThuc, datePickerNgayKy;
        private JComboBox<HopDongModel.TrangThaiHopDong> cbTrangThai;
        private JButton btnSave, btnCancel;

        private HopDongModel originalHopDong;

        public EditHopDongDialog(HopDongModel hopDongToEdit) {
            super(SwingUtilities.getWindowAncestor(HopDongView.this), "Sửa Thông Tin Hợp Đồng", Dialog.ModalityType.APPLICATION_MODAL);
            this.originalHopDong = hopDongToEdit;
            setSize(400, 550);
            setLocationRelativeTo(HopDongView.this);
            setLayout(new BorderLayout(10, 10));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            formPanel.setBackground(Color.WHITE);

            Font labelFont = new Font("Arial", Font.PLAIN, 14);
            Font componentFont = new Font("Arial", Font.PLAIN, 14);
            Font disabledTextFieldFont = new Font("Arial", Font.BOLD, 14);

            formPanel.add(createLabel("Mã Hợp Đồng:", labelFont));
            txtMaHopDong = createTextField(disabledTextFieldFont);
            txtMaHopDong.setText(String.valueOf(originalHopDong.getMaHopDong()));
            txtMaHopDong.setEditable(false);
            txtMaHopDong.setBackground(new Color(240, 240, 240));
            txtMaHopDong.setForeground(Color.BLACK);
            formPanel.add(txtMaHopDong);

            formPanel.add(createLabel("Nhân Viên:", labelFont));
            cbNhanVien = new JComboBox<>();
            cbNhanVien.setFont(componentFont);
            cbNhanVien.setForeground(TEXT_COLOR_BLACK);
            cbNhanVien.setEditable(false); // Make ComboBox not editable
            loadNhanVienToComboBox(cbNhanVien, originalHopDong.getMaNhanVien());
            formPanel.add(cbNhanVien);
            
            // REMOVE the JLabel for displaying hoten
            // formPanel.add(createLabel("Họ Tên:", labelFont));
            // lblHoTenDisplay = createLabel(originalHopDong.getHoten(), componentFont);
            // lblHoTenDisplay.setForeground(TEXT_COLOR_BLACK);
            // formPanel.add(lblHoTenDisplay);

            // REMOVE the ActionListener as hoten is now taken directly from selected item
            // cbNhanVien.addActionListener(new ActionListener() {
            //     @Override
            //     public void actionPerformed(ActionEvent e) {
            //         NhanVienItem selectedItem = (NhanVienItem) cbNhanVien.getSelectedItem();
            //         if (selectedItem != null) {
            //             lblHoTenDisplay.setText(selectedItem.getHoTen());
            //         } else {
            //             lblHoTenDisplay.setText("");
            //         }
            //     }
            // });

            formPanel.add(createLabel("Loại Hợp Đồng:", labelFont));
            cbLoaiHopDong = new JComboBox<>(HopDongModel.LoaiHopDong.values());
            cbLoaiHopDong.setSelectedItem(originalHopDong.getLoaiHopDong());
            cbLoaiHopDong.setFont(componentFont);
            cbLoaiHopDong.setForeground(Color.BLACK);
            cbLoaiHopDong.setBackground(Color.WHITE);
            formPanel.add(cbLoaiHopDong);

            // JDatePicker cho Ngày Bắt Đầu
            formPanel.add(createLabel("Ngày Bắt Đầu:", labelFont));
            datePickerNgayBatDau = createDatePicker(originalHopDong.getNgayBatDau());
            formPanel.add(datePickerNgayBatDau);

            // JDatePicker cho Ngày Kết Thúc
            formPanel.add(createLabel("Ngày Kết Thúc:", labelFont));
            datePickerNgayKetThuc = createDatePicker(originalHopDong.getNgayKetThuc());
            formPanel.add(datePickerNgayKetThuc);

            // JDatePicker cho Ngày Ký
            formPanel.add(createLabel("Ngày Ký:", labelFont));
            datePickerNgayKy = createDatePicker(originalHopDong.getNgayKy());
            formPanel.add(datePickerNgayKy);

            formPanel.add(createLabel("Trạng Thái:", labelFont));
            cbTrangThai = new JComboBox<>(HopDongModel.TrangThaiHopDong.values());
            cbTrangThai.setSelectedItem(originalHopDong.getTrangThai());
            cbTrangThai.setFont(componentFont);
            cbTrangThai.setForeground(Color.BLACK);
            cbTrangThai.setBackground(Color.WHITE);
            formPanel.add(cbTrangThai);

            formPanel.add(createLabel("Lương cơ bản:", labelFont));
            txtLuongCoBan = createTextField(componentFont);
            txtLuongCoBan.setText(currencyFormat.format(originalHopDong.getLuongCoBan()));
            formPanel.add(txtLuongCoBan);

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
                        NhanVienItem selectedNhanVien = (NhanVienItem) cbNhanVien.getSelectedItem();
                        if (selectedNhanVien == null) {
                            JOptionPane.showMessageDialog(EditHopDongDialog.this, "Vui lòng chọn một nhân viên.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Date ngayBD = (Date) datePickerNgayBatDau.getModel().getValue();
                        Date ngayKT = (Date) datePickerNgayKetThuc.getModel().getValue();
                        Date ngayKy = (Date) datePickerNgayKy.getModel().getValue();

                        if (ngayBD == null) {
                            JOptionPane.showMessageDialog(EditHopDongDialog.this, "Ngày bắt đầu không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (ngayKy == null) {
                            JOptionPane.showMessageDialog(EditHopDongDialog.this, "Ngày ký không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (txtLuongCoBan.getText().trim().isEmpty()) {
                            JOptionPane.showMessageDialog(EditHopDongDialog.this, "Lương cơ bản không được để trống.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        int maHD = originalHopDong.getMaHopDong();
                        int maNV = selectedNhanVien.getMaNhanVien();
                        String hoten = selectedNhanVien.getHoTen(); // Get hoten directly from selected NhanVienItem
                        HopDongModel.LoaiHopDong loaiHD = (HopDongModel.LoaiHopDong) cbLoaiHopDong.getSelectedItem();
                        HopDongModel.TrangThaiHopDong trangThai = (HopDongModel.TrangThaiHopDong) cbTrangThai.getSelectedItem();

                        double luongCB = currencyFormat.parse(txtLuongCoBan.getText()).doubleValue();

                        HopDongModel updatedHopDong = new HopDongModel(maHD, maNV, hoten, loaiHD, ngayBD, ngayKT, ngayKy, trangThai, luongCB);

                        controller.updateHopDong(updatedHopDong);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(EditHopDongDialog.this, "Lương cơ bản phải là số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(EditHopDongDialog.this, "Định dạng ngày tháng hoặc lương cơ bản không hợp lệ (dd/MM/yyyy hoặc định dạng số).", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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

        private void loadNhanVienToComboBox(JComboBox<NhanVienItem> comboBox, int currentMaNV) {
            List<NhanVienItem> nhanVienList = controller.getAllNhanVien();
            comboBox.removeAllItems();
            NhanVienItem selectedItem = null;
            for (NhanVienItem nv : nhanVienList) {
                comboBox.addItem(nv);
                if (nv.getMaNhanVien() == currentMaNV) {
                    selectedItem = nv;
                }
            }
            if (selectedItem != null) {
                comboBox.setSelectedItem(selectedItem);
            }
        }
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR_BLACK);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setForeground(TEXT_COLOR_BLACK);
        return textField;
    }

    // Helper method to create JDatePicker
    private JDatePickerImpl createDatePicker(Date initialDate) {
        UtilDateModel model = new UtilDateModel();
        if (initialDate != null) {
            model.setValue(initialDate);
            model.setSelected(true);
        }
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 14));
        datePicker.getJFormattedTextField().setForeground(TEXT_COLOR_BLACK);
        return datePicker;
    }

    // Helper class for formatting DatePicker
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

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
}