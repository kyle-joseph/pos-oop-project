
package posystem;
import java.sql.*;
import java.util.*;
public class ProductList extends msqlAccess {
    ResultSet prod;
    ResultSet order;
    ResultSet orderline;
    public ArrayList<Products> prodList = new ArrayList<Products>();
    public ArrayList<orders> orderList = new ArrayList<orders>();
    public ArrayList<orderline> orderlineList = new ArrayList<orderline>();
    public ProductList(){
        
    }
    public void addToProdList(){
        connect();
        try{
        this.prod = select("SELECT * FROM product WHERE status=1 AND productQty != 0");
        
            while(prod.next()){
                 Products p = new Products();
                 p.setId(prod.getInt("productID"));
                 p.setProductName(prod.getString("productName"));
                 p.setProductPrice(prod.getInt("price"));
                 p.setProductQty(prod.getInt("productQty"));
                 prodList.add(p);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally{
            close();
        }
        
    }
    public void addToSales(){
        connect();
        try{
        this.order = select("SELECT * FROM orders");
        this.orderline = select("SELECT * FROM orderlineview");
        
            while(order.next()){
                 orders ord = new orders();
                 ord.setCustomerID(order.getLong("customerID"));
                 ord.setOrderID(order.getInt("orderID"));
                 ord.setDateTime(order.getString("orderDate"));
                 ord.setAmountPaid(order.getInt("amountPaid"));
                 orderList.add(ord);
            }
            while(orderline.next()){
                 orderline ordl = new orderline();
                 ordl.setOrderID(orderline.getInt("orderID"));
                 ordl.setProduct(orderline.getString("productName"));
                 ordl.setProductQuantity(orderline.getInt("orderQty"));
                 orderlineList.add(ordl);
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        finally{
            close();
        }
    }
    
}
