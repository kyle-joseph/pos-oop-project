
package posystem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class Functions extends msqlAccess{
    SimpleDateFormat formatter;
    Date date = new Date(); 
    int tempRand;
    ResultSet res;
    public long generateCustomerId() throws SQLException{
        int rand  = (int) ((Math.random() * ((9999 - 1000) + 1)) + 1000);
        tempRand = rand;
        formatter = new SimpleDateFormat("yyyyMMdd");
        long custID = Long.parseLong(formatter.format(date) + "" + rand);
        connect();
        res = select(String.format("SELECT * FROM customer WHERE customerID = %d", custID));
        if(res.next() == true){
            close();
            generateCustomerId();
        }
        close();
        return custID;
        
    }
    public long generateOrderId()throws SQLException{
        formatter = new SimpleDateFormat("yyMMdd");
        long orderID = Long.parseLong(formatter.format(date) + "" + tempRand);
        return orderID;
    }
    public int computeChange(int amountPayed, int totalAmount){
        return amountPayed - totalAmount;
    }
    public void updateProducts(ArrayList <String> productName, ArrayList <Integer> qty, long custId, long ordId, int totalAmount){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        
        String insertCust = String.format("INSERT INTO customer(customerID) VALUES(%d)", custId);
        String insertOrd = String.format("INSERT INTO orders(orderID, orderDate, customerID, amountPaid) VALUES(%d, '%s', %d, %d)", ordId, formattedDate, custId, totalAmount);
        connect();
        insert(insertCust);
        insert(insertOrd);
        for(int i = 0; i < productName.size(); i++){
            String insertOrderLine = String.format("INSERT INTO orderline(orderID, productId, orderQty) VALUES(%d, (SELECT productID FROM product WHERE productName = '%s'), %d)", ordId, productName.get(i), qty.get(i));
            String up = String.format("UPDATE product SET productQty = productQty - %d WHERE productName = '%s'", qty.get(i), productName.get(i));
            insert(insertOrderLine);
            update(up);
        }
        close();
    }
    public String loginCheck(String uname, String pass, boolean inventory){
        connect();
        String response = "false";
        boolean loginProceed = false;
        boolean found = false;
        String query = String.format("SELECT username, password FROM user_account WHERE username='%s' AND password='%s' AND status=1", uname, pass);
        String query1 = String.format("SELECT username, password FROM user_account WHERE username='%s' AND password='%s' AND status=1 AND inventoryAllowed=1", uname, pass);
        
        if(inventory){
            found = loginSelect(query);
            loginProceed = loginSelect(query1);
            if(found){
                response = "true";
                if(loginProceed){
                    response = "true true";
                }
            }
        }
        else{
            found = loginSelect(query);
            if(found){
                response = "true";
            }
        }
        close();
        return response;
    }
    public boolean insertUser(String uname, String pass, boolean allowInventory) throws SQLException{
        boolean proceed = false;
        connect();
        String query = String.format("SELECT username FROM user_account WHERE username='%s' AND status=1", uname);
        
        ResultSet rs = select(query);
        
        if(rs.next() == false){
            if(allowInventory){
                insert(String.format("INSERT INTO user_account(username, password, status, inventoryAllowed) VALUES('%s', '%s', 1, 1)", uname, pass));
            }
            else{
                insert(String.format("INSERT INTO user_account(username, password, status, inventoryAllowed) VALUES('%s', '%s', 1, 0)", uname, pass));
            }
            proceed = true;
        }
        close();
        return proceed;
    }
    public boolean updateProduct(String product, int price, int qty){
        connect();
        String query = String.format("SELECT productName FROM product WHERE lower(productName)='%s'", product.toLowerCase());
        boolean found = loginSelect(query);
        boolean proceed = false;
        if (found){
            update(String.format("UPDATE product SET productName='%s', price=%d, productQty=%d, status=1 WHERE productName='%s'", product, price, qty, product));
            proceed = true;
        }
        else{
            insert(String.format("INSERT INTO product(productName, price, productQty, status) VALUES('%s', %d, %d, 1)", product, price, qty));
        }
        close();
        
        return proceed;
    }
    public boolean deleteProduct(String product){
        connect();
        update(String.format("UPDATE product SET status=0 WHERE productName='%s'", product));
        close();
        return true;
    }
}
