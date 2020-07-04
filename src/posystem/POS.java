
package posystem;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class POS extends javax.swing.JFrame {
    ProductList pd;
    Object [] row;
    DefaultTableModel model;
    DefaultTableModel nmodel;
    DefaultTableModel salesmodel;
    DefaultTableModel prodsalesmodel;
    boolean payState = true;
    boolean goToCashier = false;
    boolean goToInventory = false;
    
    ArrayList<String> productName = new ArrayList<>();
    ArrayList<Integer> pprice = new ArrayList<>();
    ArrayList<Integer> pqty = new ArrayList<>();
    
    int totalAmount = 0;
    int customerId = 0;
    int addToCust = 1000;
    long cId;
    long oId;
    Functions func;
    public POS() {
        func = new Functions();
        initComponents();
        initBtnPanel();
        setLoginSignPosition();
        adjustLocationSizes();
        tableHeaderStyle();
        initSalesPanel();
        
        loginIcon.setVisible(false);
    }
    private void initBtnPanel(){
        
        pd = new ProductList();
        nmodel = (DefaultTableModel) inventoryTable.getModel();
        
        JLabel edit = new JLabel("EDIT");
        JLabel delete = new JLabel("DELETE");
        edit.setHorizontalAlignment(SwingConstants.CENTER);
        delete.setHorizontalAlignment(SwingConstants.CENTER);
        edit.setFont(new Font("Segeo UI", Font.BOLD, 14));
        delete.setFont(new Font("Segeo UI", Font.BOLD, 14));
        edit.setForeground(new Color(23, 160, 93));
        delete.setForeground(new Color(221, 80, 68));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        
        inventoryTable.getColumnModel().getColumn(3).setCellRenderer(new labelRenderer());
        inventoryTable.getColumnModel().getColumn(4).setCellRenderer(new labelRenderer());
        
        //adding products
        pd.addToProdList();
        for(int i = 0; i < pd.prodList.size(); i++){
            String name = pd.prodList.get(i).getProductName();
            int price = pd.prodList.get(i).getProductPrice();
            int qty = pd.prodList.get(i).getProductQty();
            row = new Object[]{name, price, qty, edit, delete};
            nmodel.addRow(row);
            generateProductBtn(name, price);
        }
        productScrollPane.getVerticalScrollBar().setUnitIncrement(16);    
    }
    private void initSalesPanel(){
        salesmodel = (DefaultTableModel) salesTable.getModel();
        prodsalesmodel = (DefaultTableModel) prodsalesTable.getModel();
        
        pd.orderList.clear();
        pd.orderlineList.clear();
        pd.addToSales();
        for(int i = 0; i < pd.orderList.size(); i++){
            long customerID = pd.orderList.get(i).getCustomerID();
            int orderID = pd.orderList.get(i).getOrderID();
            String datetime = pd.orderList.get(i).getDateTime();
            int amountPaid = pd.orderList.get(i).getAmountPaid();
            row = new Object[]{customerID, orderID, datetime, amountPaid};
            salesmodel.addRow(row);
        }
        for(int i = 0; i < pd.orderlineList.size(); i++){
            int orderID = pd.orderlineList.get(i).getOrderID();
            String product = pd.orderlineList.get(i).getProduct();
            int qty = pd.orderlineList.get(i).getProductQuantity();
            row = new Object[]{orderID, product, qty};
            prodsalesmodel.addRow(row);
        }
        salesScrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        prodsalesScrollPane.getVerticalScrollBar().setUnitIncrement(16);  
        
    }
    private void tableHeaderStyle(){
        orderTable.getTableHeader().setOpaque(false);
        orderTable.getTableHeader().setBackground(new Color(0, 139, 190));
        orderTable.getTableHeader().setForeground(new Color(255, 255, 255));
        orderTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        inventoryTable.getTableHeader().setOpaque(false);
        inventoryTable.getTableHeader().setBackground(new Color(0, 139, 190));
        inventoryTable.getTableHeader().setForeground(new Color(255, 255, 255));
        inventoryTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        salesTable.getTableHeader().setOpaque(false);
        salesTable.getTableHeader().setBackground(new Color(0, 139, 190));
        salesTable.getTableHeader().setForeground(new Color(255, 255, 255));
        salesTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
        prodsalesTable.getTableHeader().setOpaque(false);
        prodsalesTable.getTableHeader().setBackground(new Color(0, 139, 190));
        prodsalesTable.getTableHeader().setForeground(new Color(255, 255, 255));
        prodsalesTable.getTableHeader().setFont(new Font("Segeo UI", Font.BOLD, 14));
    }
    private void setLoginSignPosition(){
        loginBox.setLocation((login.getWidth() - loginBox.getWidth()) / 2, (login.getHeight() - loginBox.getHeight()) / 2);
        choose.setLocation((choosePanel.getWidth() - choose.getWidth()) / 2, (choosePanel.getHeight() - choose.getHeight()) / 2);
        meatshopLogoPanel.setLocation((leftPanel.getWidth() - meatshopLogoPanel.getWidth()) / 2, (leftPanel.getHeight() - meatshopLogoPanel.getHeight()) / 2);
        welcomeLbl.setSize(choosePanel.getWidth(), 100);
        
    }
     private void adjustLocationSizes(){
        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized( ComponentEvent e ) {
                setLoginSignPosition();
            }
        } );
    }
    public void goToPanel(JPanel panel){
        holderPanel.removeAll();
        holderPanel.add(panel);
        holderPanel.repaint();
        holderPanel.revalidate();
    }
    
    public void setCustOrdId(){
        try{
            cId = func.generateCustomerId();
            oId = func.generateOrderId();
            custId.setText(Long.toString(cId));
            orderId.setText(Long.toString(oId));
        }
        catch(SQLException e){
                
        }
    }
    public void generateProductBtn(String name, int price){
        
        JLabel btn = new JLabel();
        btn.setSize(150, 150);
        try{
            btn.setIcon(new ImageIcon(getClass().getResource(String.format("images/%s.png", name))));
        }
        catch(Exception e){
            btn.setIcon(new ImageIcon(getClass().getResource("images/Default.png")));
        }
        btn.setText(String.format("%s", name));
        btn.setFont(new Font("Segeo UI", Font.BOLD, 15));
        btn.setForeground(new Color(255, 255, 255));
        btn.setHorizontalTextPosition(JLabel.CENTER);
        btn.setVerticalTextPosition(JLabel.CENTER);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setIcon(new ImageIcon(getClass().getResource("images/hover.png")));
                btn.setText(String.format("Php %d.00", price));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                try{
                    btn.setIcon(new ImageIcon(getClass().getResource(String.format("images/%s.png", name))));
                }
                catch(Exception e){
                    btn.setIcon(new ImageIcon(getClass().getResource("images/Default.png")));
                }
                btn.setText(String.format("%s", name));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(payState){
                    String product = name;
                    int qty = 1;
                    addDataToTable(product, price, qty);
                }
            }
        });
        initBtn.add(btn);
        initBtn.revalidate();
    }
    
    public void addDataToTable(String product, int price, int qty){
        JLabel plus = new JLabel();
        JLabel minus = new JLabel();
        JLabel trash = new JLabel();      
        
        plus.setHorizontalAlignment(SwingConstants.CENTER);
        minus.setHorizontalAlignment(SwingConstants.CENTER);
        trash.setHorizontalAlignment(SwingConstants.CENTER);
        
        plus.setIcon(new ImageIcon(getClass().getResource("images/plus.png")));
        minus.setIcon(new ImageIcon(getClass().getResource("images/minus.png")));
        trash.setIcon(new ImageIcon(getClass().getResource("images/trash.png")));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        
        row = new Object[]{product, price, qty, plus, minus, trash};
        model = (DefaultTableModel) orderTable.getModel();
        
        
        
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        orderTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(45);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(45);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(45);
        
        orderTable.getColumnModel().getColumn(3).setCellRenderer(new labelRenderer());
        orderTable.getColumnModel().getColumn(4).setCellRenderer(new labelRenderer());
        orderTable.getColumnModel().getColumn(5).setCellRenderer(new labelRenderer());
        
        
        boolean noExist = true;
        if(model.getRowCount() > 0){
            for(int i = 0; i < model.getRowCount(); i++){
                String temp = model.getValueAt(i, 0).toString();
                if(temp.equalsIgnoreCase(row[0].toString())){
                    String pric = model.getValueAt(i, 2).toString();
                    int p = Integer.parseInt(pric) + 1;
                    model.setValueAt(p, i, 2);
                    noExist = false;
                    break;
                }
            }
            if(noExist){
                model.addRow(row);
            }
        }
        else{
            model.addRow(row);
        }
        displayTotalAmount();
    }
    //Render Images to table
    class labelRenderer implements TableCellRenderer {
         
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            
            return (Component) value;
             
        }
         
    }
    
    //Amount to pay
    public void displayTotalAmount(){
        productName.clear();
        pprice.clear();
        pqty.clear();
        
        totalAmount = 0;
        int qty = 0;
        int price = 0;
        
        if(model.getRowCount() > 0){
            for(int i = 0; i < model.getRowCount(); i++){
                price = Integer.parseInt(model.getValueAt(i, 1).toString());
                qty = Integer.parseInt(model.getValueAt(i, 2).toString());
                totalAmount += (price * qty);
                
                productName.add(model.getValueAt(i, 0).toString());
                pprice.add(price);
                pqty.add(qty);
            }
        }
        tAmount.setText(String.format("Php %d.00", totalAmount));
    }

    public void addQty(){
        int row = orderTable.getSelectedRow();
        int add = Integer.parseInt(model.getValueAt(row, 2).toString()) + 1;
        model.setValueAt(add, row, 2);
        displayTotalAmount();
    }
    public void subtractQty(){
        int row = orderTable.getSelectedRow();
        int getNum = Integer.parseInt(model.getValueAt(row, 2).toString());
        
        if(getNum > 1){
            int minus = getNum - 1;
            model.setValueAt(minus, row, 2);
        }
        else{
            model.removeRow(row);
        }
        displayTotalAmount();
    }
    public void deleteRow(){
        int row = orderTable.getSelectedRow();
        model.removeRow(row);
        displayTotalAmount();
    }
    
    public void getChange(){
        if(payState){
            try{
                int amountPayed = Integer.parseInt(amountToPayText.getText());
                if(amountPayed >= totalAmount && totalAmount != 0){
                    changeDisplay.setText(String.format("Php %s.00", func.computeChange(amountPayed, totalAmount)));
                    amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 2));
                    payState = false;
                    amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(23, 160, 93), 2));
                    func.updateProducts(productName, pqty, cId, oId, totalAmount);
                }
                else{
                    amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(209, 78, 66), 3));
                    payState = false;
                }
            }
            catch(Exception e){
                amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(209, 78, 66), 3));
                payState = false;
            }
        }
    }
    public void resetOrder(){
        try{
            while(model.getRowCount() > 0){
                model.removeRow(0);
            }
            amountToPayText.setText("");
            changeDisplay.setText("Php 0.00");
            displayTotalAmount();
        }
        catch(Exception e){
        }
    }
    public void resetProducts(){
        while(nmodel.getRowCount() > 0){
            nmodel.removeRow(0);
        }
        initBtn.removeAll();
        initBtn.revalidate();
    }
    
    public void resetSales(){
        while(salesmodel.getRowCount() > 0){
            salesmodel.removeRow(0);
        }
        while(prodsalesmodel.getRowCount() > 0){
            prodsalesmodel.removeRow(0);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        logoutBtn = new javax.swing.JLabel();
        logoLbl = new javax.swing.JLabel();
        loginIcon = new javax.swing.JLabel();
        loginName = new javax.swing.JLabel();
        holderPanel = new javax.swing.JPanel();
        choosePanel = new javax.swing.JPanel();
        choose = new javax.swing.JPanel();
        inventory = new javax.swing.JLabel();
        cashier = new javax.swing.JLabel();
        welcomeLbl = new javax.swing.JLabel();
        loginPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        meatshopLogoPanel = new javax.swing.JPanel();
        baboy = new javax.swing.JLabel();
        meat = new javax.swing.JLabel();
        righttPanel = new javax.swing.JPanel();
        login = new javax.swing.JPanel();
        loginBox = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passText = new javax.swing.JPasswordField();
        usernameText = new javax.swing.JTextField();
        loginBtn = new javax.swing.JPanel();
        loginLbl = new javax.swing.JLabel();
        signage = new javax.swing.JLabel();
        signupBtn = new javax.swing.JPanel();
        signupLbl = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        allowInventory = new javax.swing.JCheckBox();
        inventoryPanel = new javax.swing.JPanel();
        inventoryScrollPane = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        editPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        pPrice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        pQuantity = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        pName = new javax.swing.JTextField();
        saveBtn = new javax.swing.JPanel();
        saveLbl = new javax.swing.JLabel();
        invCancelBtn = new javax.swing.JPanel();
        invCancelLbl = new javax.swing.JLabel();
        saveMsg = new javax.swing.JLabel();
        viewSalesBtn = new javax.swing.JPanel();
        viewSalesLbl = new javax.swing.JLabel();
        cashierPanel = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        btnPanel = new javax.swing.JPanel();
        productScrollPane = new javax.swing.JScrollPane();
        initBtn = new javax.swing.JPanel();
        displayPanel = new javax.swing.JPanel();
        customerOrderPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        orderId = new javax.swing.JLabel();
        custId = new javax.swing.JLabel();
        orderSPane = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        totalPanel = new javax.swing.JPanel();
        TotalAM = new javax.swing.JLabel();
        tAmount = new javax.swing.JLabel();
        Change = new javax.swing.JLabel();
        changeDisplay = new javax.swing.JLabel();
        amountToPayText = new javax.swing.JTextField();
        EnterAM = new javax.swing.JLabel();
        payBtn = new javax.swing.JLabel();
        transactionPanel = new javax.swing.JPanel();
        newOrderBtn = new javax.swing.JLabel();
        cancelOrderBtn = new javax.swing.JLabel();
        salesPanel = new javax.swing.JPanel();
        salesTitlePanel = new javax.swing.JPanel();
        ordersLabel = new javax.swing.JLabel();
        orderlineLabel = new javax.swing.JLabel();
        salesTablePanel = new javax.swing.JPanel();
        salesScrollPane = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        prodsalesScrollPane = new javax.swing.JScrollPane();
        prodsalesTable = new javax.swing.JTable();
        backInvBtn = new javax.swing.JPanel();
        backInvLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(234, 249, 244));
        setMinimumSize(new java.awt.Dimension(1366, 768));

        topPanel.setBackground(new java.awt.Color(0, 139, 190));

        logoutBtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/logoutBtn.png"))); // NOI18N
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                logoutBtnMouseReleased(evt);
            }
        });

        logoLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/logo.png"))); // NOI18N

        loginIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/currentLogin.png"))); // NOI18N

        loginName.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        loginName.setForeground(new java.awt.Color(255, 255, 255));
        loginName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(logoLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 796, Short.MAX_VALUE)
                .addComponent(loginName, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(topPanelLayout.createSequentialGroup()
                        .addGroup(topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(loginName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(loginIcon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(logoutBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        holderPanel.setLayout(new java.awt.CardLayout());

        choosePanel.setBackground(new java.awt.Color(255, 255, 255));
        choosePanel.setLayout(null);

        choose.setBackground(new java.awt.Color(0, 139, 190));
        choose.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        inventory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/inventoryLogo.png"))); // NOI18N
        inventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inventoryMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                inventoryMouseReleased(evt);
            }
        });
        choose.add(inventory);

        cashier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cashier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/cashierLogo.png"))); // NOI18N
        cashier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cashierMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cashierMouseReleased(evt);
            }
        });
        choose.add(cashier);

        choosePanel.add(choose);
        choose.setBounds(380, 210, 595, 268);

        welcomeLbl.setBackground(new java.awt.Color(0, 139, 190));
        welcomeLbl.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        welcomeLbl.setForeground(new java.awt.Color(0, 139, 190));
        welcomeLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeLbl.setText("Welcome to Pop's Meatshop");
        choosePanel.add(welcomeLbl);
        welcomeLbl.setBounds(0, 30, 1370, 100);

        holderPanel.add(choosePanel, "card4");

        loginPanel.setLayout(new java.awt.GridLayout(1, 2));

        leftPanel.setBackground(new java.awt.Color(0, 139, 190));
        leftPanel.setLayout(null);

        meatshopLogoPanel.setOpaque(false);

        baboy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        baboy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/piggy.png"))); // NOI18N

        meat.setFont(new java.awt.Font("Segoe UI", 3, 48)); // NOI18N
        meat.setForeground(new java.awt.Color(255, 255, 255));
        meat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        meat.setText("Pop's Meat Shop");
        meat.setAlignmentX(0.5F);

        javax.swing.GroupLayout meatshopLogoPanelLayout = new javax.swing.GroupLayout(meatshopLogoPanel);
        meatshopLogoPanel.setLayout(meatshopLogoPanelLayout);
        meatshopLogoPanelLayout.setHorizontalGroup(
            meatshopLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(meatshopLogoPanelLayout.createSequentialGroup()
                .addComponent(baboy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(meatshopLogoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(meat, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        meatshopLogoPanelLayout.setVerticalGroup(
            meatshopLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(meatshopLogoPanelLayout.createSequentialGroup()
                .addComponent(meat, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(baboy, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        leftPanel.add(meatshopLogoPanel);
        meatshopLogoPanel.setBounds(95, 148, 489, 402);

        loginPanel.add(leftPanel);

        righttPanel.setBackground(new java.awt.Color(255, 255, 255));
        righttPanel.setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));
        login.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(50, 50, 53));
        jLabel1.setText("Username");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(50, 50, 53));
        jLabel3.setText("Password");

        passText.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passText.setForeground(new java.awt.Color(50, 50, 53));
        passText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190)));

        usernameText.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        usernameText.setForeground(new java.awt.Color(50, 50, 53));
        usernameText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190)));

        loginBtn.setBackground(new java.awt.Color(0, 139, 190));
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                loginBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                loginBtnMouseReleased(evt);
            }
        });

        loginLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        loginLbl.setForeground(new java.awt.Color(255, 255, 255));
        loginLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginLbl.setText("LOGIN");

        javax.swing.GroupLayout loginBtnLayout = new javax.swing.GroupLayout(loginBtn);
        loginBtn.setLayout(loginBtnLayout);
        loginBtnLayout.setHorizontalGroup(
            loginBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loginLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        loginBtnLayout.setVerticalGroup(
            loginBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        signage.setFont(new java.awt.Font("Segoe UI Black", 0, 36)); // NOI18N
        signage.setForeground(new java.awt.Color(0, 139, 190));
        signage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signage.setText("WELCOME");

        signupBtn.setBackground(new java.awt.Color(27, 161, 96));
        signupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                signupBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                signupBtnMouseReleased(evt);
            }
        });

        signupLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        signupLbl.setForeground(new java.awt.Color(255, 255, 255));
        signupLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signupLbl.setText("SIGNUP");

        javax.swing.GroupLayout signupBtnLayout = new javax.swing.GroupLayout(signupBtn);
        signupBtn.setLayout(signupBtnLayout);
        signupBtnLayout.setHorizontalGroup(
            signupBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, signupBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(signupLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        signupBtnLayout.setVerticalGroup(
            signupBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(signupLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        errorLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(220, 78, 65));
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        allowInventory.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        allowInventory.setForeground(new java.awt.Color(50, 50, 53));
        allowInventory.setText("Allow Inventory use (Click for signup only)");

        javax.swing.GroupLayout loginBoxLayout = new javax.swing.GroupLayout(loginBox);
        loginBox.setLayout(loginBoxLayout);
        loginBoxLayout.setHorizontalGroup(
            loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginBoxLayout.createSequentialGroup()
                        .addComponent(allowInventory)
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addComponent(signage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(signupBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginBoxLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(usernameText, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(passText, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(loginBoxLayout.createSequentialGroup()
                            .addGroup(loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1))
                            .addGap(0, 169, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        loginBoxLayout.setVerticalGroup(
            loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginBoxLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(signage, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                .addComponent(allowInventory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(signupBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(loginBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(loginBoxLayout.createSequentialGroup()
                    .addGap(91, 91, 91)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(21, 21, 21)
                    .addComponent(jLabel3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(passText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(173, Short.MAX_VALUE)))
        );

        login.add(loginBox);
        loginBox.setBounds(220, 140, 274, 415);

        righttPanel.add(login, "card2");

        loginPanel.add(righttPanel);

        holderPanel.add(loginPanel, "card3");

        inventoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        inventoryScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190), 2));
        inventoryScrollPane.setFocusable(false);

        inventoryTable.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Price", "Quantity", "Action", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTable.setGridColor(new java.awt.Color(0, 139, 190));
        inventoryTable.setRowHeight(30);
        inventoryTable.setRowSelectionAllowed(false);
        inventoryTable.setShowGrid(true);
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryTableMouseClicked(evt);
            }
        });
        inventoryScrollPane.setViewportView(inventoryTable);
        if (inventoryTable.getColumnModel().getColumnCount() > 0) {
            inventoryTable.getColumnModel().getColumn(0).setResizable(false);
            inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(300);
            inventoryTable.getColumnModel().getColumn(1).setResizable(false);
            inventoryTable.getColumnModel().getColumn(2).setResizable(false);
            inventoryTable.getColumnModel().getColumn(3).setResizable(false);
            inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(15);
            inventoryTable.getColumnModel().getColumn(4).setResizable(false);
            inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(15);
        }

        editPanel.setBackground(new java.awt.Color(255, 255, 255));
        editPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190), 2));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Product Name");

        pPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Price");

        pQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Quantity");

        pName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        saveBtn.setBackground(new java.awt.Color(23, 160, 93));
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                saveBtnMouseReleased(evt);
            }
        });

        saveLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        saveLbl.setForeground(new java.awt.Color(255, 255, 255));
        saveLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        saveLbl.setText("SAVE");

        javax.swing.GroupLayout saveBtnLayout = new javax.swing.GroupLayout(saveBtn);
        saveBtn.setLayout(saveBtnLayout);
        saveBtnLayout.setHorizontalGroup(
            saveBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, saveBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        saveBtnLayout.setVerticalGroup(
            saveBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(saveLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        invCancelBtn.setBackground(new java.awt.Color(221, 80, 68));
        invCancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                invCancelBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                invCancelBtnMouseReleased(evt);
            }
        });

        invCancelLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        invCancelLbl.setForeground(new java.awt.Color(255, 255, 255));
        invCancelLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invCancelLbl.setText("CANCEL");

        javax.swing.GroupLayout invCancelBtnLayout = new javax.swing.GroupLayout(invCancelBtn);
        invCancelBtn.setLayout(invCancelBtnLayout);
        invCancelBtnLayout.setHorizontalGroup(
            invCancelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, invCancelBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invCancelLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        invCancelBtnLayout.setVerticalGroup(
            invCancelBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(invCancelLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        saveMsg.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        saveMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        viewSalesBtn.setBackground(new java.awt.Color(0, 139, 190));
        viewSalesBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                viewSalesBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                viewSalesBtnMouseReleased(evt);
            }
        });

        viewSalesLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        viewSalesLbl.setForeground(new java.awt.Color(255, 255, 255));
        viewSalesLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        viewSalesLbl.setText("VIEW SALES");

        javax.swing.GroupLayout viewSalesBtnLayout = new javax.swing.GroupLayout(viewSalesBtn);
        viewSalesBtn.setLayout(viewSalesBtnLayout);
        viewSalesBtnLayout.setHorizontalGroup(
            viewSalesBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewSalesBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewSalesLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        viewSalesBtnLayout.setVerticalGroup(
            viewSalesBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(viewSalesLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pName)
                    .addComponent(pPrice)
                    .addComponent(pQuantity)
                    .addGroup(editPanelLayout.createSequentialGroup()
                        .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 184, Short.MAX_VALUE))
                    .addComponent(invCancelBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewSalesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(jLabel5)
                .addGap(11, 11, 11)
                .addComponent(pName, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(invCancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewSalesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout inventoryPanelLayout = new javax.swing.GroupLayout(inventoryPanel);
        inventoryPanel.setLayout(inventoryPanelLayout);
        inventoryPanelLayout.setHorizontalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        inventoryPanelLayout.setVerticalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
                    .addComponent(editPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );

        holderPanel.add(inventoryPanel, "card5");

        cashierPanel.setOpaque(false);

        orderPanel.setBackground(new java.awt.Color(234, 249, 244));

        btnPanel.setBackground(new java.awt.Color(255, 255, 255));

        productScrollPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 139, 190), 2, true));
        productScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        initBtn.setBackground(new java.awt.Color(255, 255, 255));
        initBtn.setAutoscrolls(true);
        initBtn.setLayout(new java.awt.GridLayout(0, 5, 10, 10));
        productScrollPane.setViewportView(initBtn);

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productScrollPane)
                .addContainerGap())
        );
        btnPanelLayout.setVerticalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productScrollPane)
                .addContainerGap())
        );

        displayPanel.setBackground(new java.awt.Color(255, 255, 255));

        customerOrderPanel.setBackground(new java.awt.Color(255, 255, 255));
        customerOrderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 139, 190), 2, true));

        jLabel2.setBackground(new java.awt.Color(0, 139, 190));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 139, 190));
        jLabel2.setText("Order Number: ");

        jLabel4.setBackground(new java.awt.Color(0, 139, 190));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 139, 190));
        jLabel4.setText("Customer Number:");

        orderId.setBackground(new java.awt.Color(0, 139, 190));
        orderId.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        orderId.setForeground(new java.awt.Color(0, 139, 190));
        orderId.setText("0");
        orderId.setToolTipText("");

        custId.setBackground(new java.awt.Color(0, 139, 190));
        custId.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        custId.setForeground(new java.awt.Color(0, 139, 190));
        custId.setText("0");

        javax.swing.GroupLayout customerOrderPanelLayout = new javax.swing.GroupLayout(customerOrderPanel);
        customerOrderPanel.setLayout(customerOrderPanelLayout);
        customerOrderPanelLayout.setHorizontalGroup(
            customerOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerOrderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerOrderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderId))
                    .addGroup(customerOrderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(custId)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        customerOrderPanelLayout.setVerticalGroup(
            customerOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerOrderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(custId)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(customerOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(orderId))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        orderSPane.setBackground(new java.awt.Color(255, 255, 255));
        orderSPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 139, 190), 2, true));
        orderSPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        orderTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product", "Price(Php)", "Qty", "+", "-", "Del"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.setFocusable(false);
        orderTable.setGridColor(new java.awt.Color(0, 139, 190));
        orderTable.setRowHeight(30);
        orderTable.setRowSelectionAllowed(false);
        orderTable.setShowGrid(true);
        orderTable.getTableHeader().setResizingAllowed(false);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderTableMouseClicked(evt);
            }
        });
        orderSPane.setViewportView(orderTable);

        totalPanel.setBackground(new java.awt.Color(255, 255, 255));
        totalPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 139, 190), 2, true));

        TotalAM.setBackground(new java.awt.Color(0, 139, 190));
        TotalAM.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalAM.setForeground(new java.awt.Color(0, 139, 190));
        TotalAM.setText("Total Amount:");

        tAmount.setBackground(new java.awt.Color(0, 139, 190));
        tAmount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        tAmount.setForeground(new java.awt.Color(0, 139, 190));
        tAmount.setText("Php 0.00");

        Change.setBackground(new java.awt.Color(0, 139, 190));
        Change.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Change.setForeground(new java.awt.Color(0, 139, 190));
        Change.setText("Change:");

        changeDisplay.setBackground(new java.awt.Color(0, 139, 190));
        changeDisplay.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        changeDisplay.setForeground(new java.awt.Color(0, 139, 190));
        changeDisplay.setText("Php 0.00");

        amountToPayText.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        amountToPayText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190), 2));
        amountToPayText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                amountToPayTextKeyPressed(evt);
            }
        });

        EnterAM.setBackground(new java.awt.Color(0, 139, 190));
        EnterAM.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        EnterAM.setForeground(new java.awt.Color(0, 139, 190));
        EnterAM.setText("Enter Amount:");

        javax.swing.GroupLayout totalPanelLayout = new javax.swing.GroupLayout(totalPanel);
        totalPanel.setLayout(totalPanelLayout);
        totalPanelLayout.setHorizontalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(totalPanelLayout.createSequentialGroup()
                        .addComponent(EnterAM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(amountToPayText, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(totalPanelLayout.createSequentialGroup()
                        .addComponent(TotalAM)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tAmount))
                    .addGroup(totalPanelLayout.createSequentialGroup()
                        .addComponent(Change)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(changeDisplay)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        totalPanelLayout.setVerticalGroup(
            totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TotalAM)
                    .addComponent(tAmount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EnterAM)
                    .addComponent(amountToPayText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(totalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Change)
                    .addComponent(changeDisplay))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        payBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/PayBtn.png"))); // NOI18N
        payBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                payBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                payBtnMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout displayPanelLayout = new javax.swing.GroupLayout(displayPanel);
        displayPanel.setLayout(displayPanelLayout);
        displayPanelLayout.setHorizontalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, displayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customerOrderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderSPane)
                    .addComponent(payBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        displayPanelLayout.setVerticalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(displayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customerOrderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderSPane, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(payBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        transactionPanel.setBackground(new java.awt.Color(255, 255, 255));

        newOrderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/newOrderBtn.png"))); // NOI18N
        newOrderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                newOrderBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                newOrderBtnMouseReleased(evt);
            }
        });

        cancelOrderBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/posystem/images/cancelOrderBtn.png"))); // NOI18N
        cancelOrderBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cancelOrderBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cancelOrderBtnMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout transactionPanelLayout = new javax.swing.GroupLayout(transactionPanel);
        transactionPanel.setLayout(transactionPanelLayout);
        transactionPanelLayout.setHorizontalGroup(
            transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionPanelLayout.createSequentialGroup()
                .addContainerGap(734, Short.MAX_VALUE)
                .addComponent(newOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionPanelLayout.createSequentialGroup()
                    .addContainerGap(495, Short.MAX_VALUE)
                    .addComponent(cancelOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(161, 161, 161)))
        );
        transactionPanelLayout.setVerticalGroup(
            transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transactionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newOrderBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(transactionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(transactionPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(cancelOrderBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transactionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transactionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout cashierPanelLayout = new javax.swing.GroupLayout(cashierPanel);
        cashierPanel.setLayout(cashierPanelLayout);
        cashierPanelLayout.setHorizontalGroup(
            cashierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cashierPanelLayout.setVerticalGroup(
            cashierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        holderPanel.add(cashierPanel, "card2");

        salesPanel.setBackground(new java.awt.Color(255, 255, 255));

        salesTitlePanel.setBackground(new java.awt.Color(255, 255, 255));
        salesTitlePanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        ordersLabel.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        ordersLabel.setForeground(new java.awt.Color(0, 139, 190));
        ordersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ordersLabel.setText("Orders");
        salesTitlePanel.add(ordersLabel);

        orderlineLabel.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        orderlineLabel.setForeground(new java.awt.Color(0, 139, 190));
        orderlineLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        orderlineLabel.setText("Orderline");
        salesTitlePanel.add(orderlineLabel);

        salesTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        salesTablePanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        salesScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190), 2));

        salesTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CustomerID", "OrderID", "DateTime", "Amount Paid"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.setGridColor(new java.awt.Color(0, 139, 190));
        salesTable.setRowHeight(30);
        salesScrollPane.setViewportView(salesTable);
        if (salesTable.getColumnModel().getColumnCount() > 0) {
            salesTable.getColumnModel().getColumn(0).setResizable(false);
            salesTable.getColumnModel().getColumn(1).setResizable(false);
            salesTable.getColumnModel().getColumn(2).setResizable(false);
            salesTable.getColumnModel().getColumn(2).setPreferredWidth(130);
            salesTable.getColumnModel().getColumn(3).setResizable(false);
        }

        salesTablePanel.add(salesScrollPane);

        prodsalesScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 139, 190), 2));

        prodsalesTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        prodsalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OrderID", "Product", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prodsalesTable.setGridColor(new java.awt.Color(0, 139, 190));
        prodsalesTable.setRowHeight(30);
        prodsalesScrollPane.setViewportView(prodsalesTable);
        if (prodsalesTable.getColumnModel().getColumnCount() > 0) {
            prodsalesTable.getColumnModel().getColumn(0).setResizable(false);
            prodsalesTable.getColumnModel().getColumn(1).setResizable(false);
            prodsalesTable.getColumnModel().getColumn(2).setResizable(false);
        }

        salesTablePanel.add(prodsalesScrollPane);

        backInvBtn.setBackground(new java.awt.Color(221, 79, 67));
        backInvBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                backInvBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                backInvBtnMouseReleased(evt);
            }
        });

        backInvLbl.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        backInvLbl.setForeground(new java.awt.Color(255, 255, 255));
        backInvLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backInvLbl.setText("BACK TO INVENTORY");

        javax.swing.GroupLayout backInvBtnLayout = new javax.swing.GroupLayout(backInvBtn);
        backInvBtn.setLayout(backInvBtnLayout);
        backInvBtnLayout.setHorizontalGroup(
            backInvBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backInvBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backInvLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        backInvBtnLayout.setVerticalGroup(
            backInvBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backInvLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout salesPanelLayout = new javax.swing.GroupLayout(salesPanel);
        salesPanel.setLayout(salesPanelLayout);
        salesPanelLayout.setHorizontalGroup(
            salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salesTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1346, Short.MAX_VALUE)
                    .addComponent(salesTitlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(backInvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        salesPanelLayout.setVerticalGroup(
            salesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salesPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(backInvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(salesTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(salesTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );

        holderPanel.add(salesPanel, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(holderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(holderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 706, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void orderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderTableMouseClicked
        int col = orderTable.getSelectedColumn();
        switch (col) {
            case 3:
                addQty();
                break;
            case 4:
                subtractQty();
                break;
            case 5:
                deleteRow();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_orderTableMouseClicked

    private void newOrderBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newOrderBtnMousePressed
        newOrderBtn.setIcon(new ImageIcon(getClass().getResource("images/newOrderBtnClicked.png")));
        if(!payState){
            resetOrder();
            payState = true;
            amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 2));
            setCustOrdId();
        }
    }//GEN-LAST:event_newOrderBtnMousePressed

    private void newOrderBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newOrderBtnMouseReleased
        newOrderBtn.setIcon(new ImageIcon(getClass().getResource("images/newOrderBtn.png")));
//        payState = false;
        
    }//GEN-LAST:event_newOrderBtnMouseReleased

    private void payBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payBtnMousePressed
        payBtn.setIcon(new ImageIcon(getClass().getResource("images/payBtnClicked.png")));
        getChange();
    }//GEN-LAST:event_payBtnMousePressed

    private void payBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payBtnMouseReleased
        payBtn.setIcon(new ImageIcon(getClass().getResource("images/payBtn.png")));
    }//GEN-LAST:event_payBtnMouseReleased

    private void amountToPayTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountToPayTextKeyPressed
        String value = amountToPayText.getText();
            int l = value.length();
            if ((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9') || evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
               amountToPayText.setEditable(true);
               
            } else {
               amountToPayText.setEditable(false);
               amountToPayText.setText("");
               changeDisplay.setText("Enter only numeric digits");
            }
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
               payState = true;
               getChange();
               
            }
    }//GEN-LAST:event_amountToPayTextKeyPressed

    private void cancelOrderBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelOrderBtnMousePressed
        cancelOrderBtn.setIcon(new ImageIcon(getClass().getResource("images/cancelOrderBtnClicked.png")));
        if(payState){
            resetOrder();
            amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 2));
        }
        changeDisplay.setText("Php 0.00");
        amountToPayText.setText("");
    }//GEN-LAST:event_cancelOrderBtnMousePressed

    private void cancelOrderBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelOrderBtnMouseReleased
        cancelOrderBtn.setIcon(new ImageIcon(getClass().getResource("images/cancelOrderBtn.png")));
    }//GEN-LAST:event_cancelOrderBtnMouseReleased

    private void inventoryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryMousePressed
        inventory.setIcon(new ImageIcon(getClass().getResource("images/inventoryLogoClicked.png")));
        goToInventory = true;
        
    }//GEN-LAST:event_inventoryMousePressed

    private void inventoryMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryMouseReleased
        inventory.setIcon(new ImageIcon(getClass().getResource("images/inventoryLogo.png")));
        goToPanel(loginPanel);
    }//GEN-LAST:event_inventoryMouseReleased

    private void cashierMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashierMousePressed
        cashier.setIcon(new ImageIcon(getClass().getResource("images/cashierLogoClicked.png")));
        goToCashier = true;
    }//GEN-LAST:event_cashierMousePressed

    private void cashierMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashierMouseReleased
        cashier.setIcon(new ImageIcon(getClass().getResource("images/cashierLogo.png")));
        goToPanel(loginPanel);
    }//GEN-LAST:event_cashierMouseReleased

    private void loginBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginBtnMousePressed
        loginBtn.setBackground(new Color(255, 255, 255));
        loginLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_loginBtnMousePressed

    private void loginBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginBtnMouseReleased
        loginBtn.setBackground(new Color(0, 139, 190));
        loginLbl.setForeground(new Color(255, 255, 255));
        usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
        passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
        errorLabel.setText("");
        String proceed = "";
        String uname = usernameText.getText();
        String pass = String.valueOf(passText.getPassword());
        if(!(uname.equals("") || pass.equals(""))){
            if(goToInventory){
                proceed = func.loginCheck(uname, pass, true);
                usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                resetProducts();
                initBtnPanel();
                switch (proceed) {
                    case "true true":
                        usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                        passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                        loginName.setText(uname);
                        loginIcon.setVisible(true);
                        goToPanel(inventoryPanel);
                        break;
                    case "true":
                        errorLabel.setText("Restricted access");
                        break;
                    default:
                        errorLabel.setText("Incorrect Username or Password");
                        break;
                }
            }
            else{
                proceed = func.loginCheck(uname, pass, false);
                if(proceed.equals("true")){
                    usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                    passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
                    usernameText.setText("");
                    passText.setText("");
                    errorLabel.setText("");
                    resetProducts();
                    initBtnPanel();
                    setCustOrdId();
                    loginName.setText(uname);
                    loginIcon.setVisible(true);
                    goToPanel(cashierPanel);
                }
                else{
                    errorLabel.setText("Incorrect Username or Password");
                }
            }
        }
        else{
            if(uname.equals("")){
                usernameText.setBorder(BorderFactory.createLineBorder(new Color(220, 78, 65), 2));
            }
            else{
                usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
            }
            if(pass.equals("")){
                passText.setBorder(BorderFactory.createLineBorder(new Color(220, 78, 65), 2));
            }
            else{
                passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
            }
        }
    }//GEN-LAST:event_loginBtnMouseReleased

    private void signupBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signupBtnMousePressed
        signupBtn.setBackground(new Color(255, 255, 255));
        signupLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_signupBtnMousePressed

    private void signupBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signupBtnMouseReleased
        signupBtn.setBackground(new Color(27, 161, 96));
        signupLbl.setForeground(new Color(255, 255, 255));
        
        boolean proceed = false;
        String uname = usernameText.getText();
        String pass = String.valueOf(passText.getPassword());
        if(!(uname.equals("") || pass.equals(""))){
           if(allowInventory.isSelected()){
               try {
                   proceed = func.insertUser(uname, pass, true);
               } catch (SQLException ex) {
                   proceed = false;
               }
           }
           else{
               try {
                   proceed = func.insertUser(uname, pass, false);
               } catch (SQLException ex) {
                  
               }
           }
           if(!proceed){
               errorLabel.setText("Username exists. Try another username.");
           }
           else{
               errorLabel.setText("Username added successfully.");
           }
        }
        else{
            if(uname.equals("")){
                usernameText.setBorder(BorderFactory.createLineBorder(new Color(220, 78, 65), 2));
            }
            else{
                usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
            }
            if(pass.equals("")){
                passText.setBorder(BorderFactory.createLineBorder(new Color(220, 78, 65), 2));
            }
            else{
                passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
            }
        }
    }//GEN-LAST:event_signupBtnMouseReleased

    private void inventoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryTableMouseClicked
        int col = inventoryTable.getSelectedColumn();
        if(col == 3){
            int row = inventoryTable.getSelectedRow();
            pName.setText(nmodel.getValueAt(row, 0).toString());
            pPrice.setText(nmodel.getValueAt(row, 1).toString());
            pQuantity.setText(nmodel.getValueAt(row, 2).toString());
            saveMsg.setText("");
        }
        else if(col == 4){
            int row = inventoryTable.getSelectedRow();
            String product = nmodel.getValueAt(row, 0).toString();
            boolean deleted = func.deleteProduct(product);
            if(deleted){
                saveMsg.setText("Product deleted");
                resetProducts();
                initBtnPanel();
            }
        }
    }//GEN-LAST:event_inventoryTableMouseClicked

    private void saveBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMousePressed
        saveBtn.setBackground(new Color(255, 255, 255));
        saveLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_saveBtnMousePressed

    private void saveBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMouseReleased
        saveBtn.setBackground(new Color(23, 160, 93));
        saveLbl.setForeground(new Color(255, 255, 255));
        if(!(pName.getText().equals("") || pPrice.getText().equals("") || pQuantity.getText().equals(""))){
            boolean saved = false;
            try{
                double price = Double.parseDouble(pPrice.getText());
                double qty = Double.parseDouble(pQuantity.getText());
                saved = func.updateProduct(pName.getText(), (int)price, (int)qty);
            }
            catch(Exception e){
                saveMsg.setText("Invalid input");
            }
            if(saved){
                resetProducts();
                initBtnPanel();
                pPrice.setText("");
                pQuantity.setText("");
                pName.setText("");
                saveMsg.setText("Product Saved");
                pName.requestFocus();
            }
        }
    }//GEN-LAST:event_saveBtnMouseReleased

    private void invCancelBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invCancelBtnMousePressed
        invCancelBtn.setBackground(new Color(255, 255, 255));
        invCancelLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_invCancelBtnMousePressed

    private void invCancelBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invCancelBtnMouseReleased
        invCancelBtn.setBackground(new Color(221, 80, 68));
        invCancelLbl.setForeground(new Color(255, 255, 255));
        pName.setText("");
        pPrice.setText("");
        pQuantity.setText("");
        saveMsg.setText("");
        pName.requestFocus();
    }//GEN-LAST:event_invCancelBtnMouseReleased

    private void logoutBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtnMousePressed
        logoutBtn.setIcon(new ImageIcon(getClass().getResource("images/logoutBtnClicked.png")));
    }//GEN-LAST:event_logoutBtnMousePressed

    private void logoutBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtnMouseReleased
        logoutBtn.setIcon(new ImageIcon(getClass().getResource("images/logoutBtn.png")));
        goToCashier = false;
        goToInventory = false;
        resetProducts();
        resetOrder();
        amountToPayText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 2));
        amountToPayText.setText("");
        payState = true;
        allowInventory.setSelected(false);
        usernameText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
        passText.setBorder(BorderFactory.createLineBorder(new Color(0, 139, 190), 1));
        usernameText.setText("");
        passText.setText("");
        errorLabel.setText("");
        
        pPrice.setText("");
        pQuantity.setText("");
        pName.setText("");
        
        loginName.setText("");
        loginIcon.setVisible(false);
        
        resetSales();
        
        holderPanel.removeAll();
        holderPanel.add(choosePanel);
        holderPanel.repaint();
        holderPanel.revalidate();  
        
    }//GEN-LAST:event_logoutBtnMouseReleased

    private void backInvBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backInvBtnMousePressed
        backInvBtn.setBackground(new Color(255, 255, 255));
        backInvLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_backInvBtnMousePressed

    private void backInvBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_backInvBtnMouseReleased
        backInvBtn.setBackground(new Color(221, 79, 67));
        backInvLbl.setForeground(new Color(255, 255, 255));
        resetSales();
        goToPanel(inventoryPanel);
        
    }//GEN-LAST:event_backInvBtnMouseReleased

    private void viewSalesBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSalesBtnMousePressed
        viewSalesBtn.setBackground(new Color(255, 255, 255));
        viewSalesLbl.setForeground(new Color(50, 50, 53));
    }//GEN-LAST:event_viewSalesBtnMousePressed

    private void viewSalesBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSalesBtnMouseReleased
        viewSalesBtn.setBackground(new Color(0, 139, 190));
        viewSalesLbl.setForeground(new Color(255, 255, 255));
        pName.setText("");
        pPrice.setText("");
        pQuantity.setText("");
        saveMsg.setText("");
        resetSales();
        initSalesPanel();
        goToPanel(salesPanel);
    }//GEN-LAST:event_viewSalesBtnMouseReleased
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(POS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(POS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(POS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(POS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POS().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Change;
    private javax.swing.JLabel EnterAM;
    private javax.swing.JLabel TotalAM;
    private javax.swing.JCheckBox allowInventory;
    private javax.swing.JTextField amountToPayText;
    private javax.swing.JLabel baboy;
    private javax.swing.JPanel backInvBtn;
    private javax.swing.JLabel backInvLbl;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JLabel cancelOrderBtn;
    private javax.swing.JLabel cashier;
    private javax.swing.JPanel cashierPanel;
    public javax.swing.JLabel changeDisplay;
    private javax.swing.JPanel choose;
    private javax.swing.JPanel choosePanel;
    private javax.swing.JLabel custId;
    private javax.swing.JPanel customerOrderPanel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JPanel editPanel;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel holderPanel;
    private javax.swing.JPanel initBtn;
    private javax.swing.JPanel invCancelBtn;
    private javax.swing.JLabel invCancelLbl;
    private javax.swing.JLabel inventory;
    private javax.swing.JPanel inventoryPanel;
    private javax.swing.JScrollPane inventoryScrollPane;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel login;
    private javax.swing.JPanel loginBox;
    private javax.swing.JPanel loginBtn;
    private javax.swing.JLabel loginIcon;
    private javax.swing.JLabel loginLbl;
    private javax.swing.JLabel loginName;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel logoLbl;
    private javax.swing.JLabel logoutBtn;
    private javax.swing.JLabel meat;
    private javax.swing.JPanel meatshopLogoPanel;
    private javax.swing.JLabel newOrderBtn;
    private javax.swing.JLabel orderId;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JScrollPane orderSPane;
    public javax.swing.JTable orderTable;
    private javax.swing.JLabel orderlineLabel;
    private javax.swing.JLabel ordersLabel;
    private javax.swing.JTextField pName;
    private javax.swing.JTextField pPrice;
    private javax.swing.JTextField pQuantity;
    private javax.swing.JPasswordField passText;
    private javax.swing.JLabel payBtn;
    private javax.swing.JScrollPane prodsalesScrollPane;
    private javax.swing.JTable prodsalesTable;
    private javax.swing.JScrollPane productScrollPane;
    private javax.swing.JPanel righttPanel;
    private javax.swing.JPanel salesPanel;
    private javax.swing.JScrollPane salesScrollPane;
    private javax.swing.JTable salesTable;
    private javax.swing.JPanel salesTablePanel;
    private javax.swing.JPanel salesTitlePanel;
    private javax.swing.JPanel saveBtn;
    private javax.swing.JLabel saveLbl;
    private javax.swing.JLabel saveMsg;
    private javax.swing.JLabel signage;
    private javax.swing.JPanel signupBtn;
    private javax.swing.JLabel signupLbl;
    public javax.swing.JLabel tAmount;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JPanel transactionPanel;
    private javax.swing.JTextField usernameText;
    private javax.swing.JPanel viewSalesBtn;
    private javax.swing.JLabel viewSalesLbl;
    private javax.swing.JLabel welcomeLbl;
    // End of variables declaration//GEN-END:variables
    
}

