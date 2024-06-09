package project;

import java.sql.*;
import java.util.*;
import java.util.*;


public class Market {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Add a product");
            System.out.println("2. Print product list");
            System.out.println("3. Sell a product");
            System.out.println("4. Update Product Price");
            System.out.println("5. Update Product Quantity");
            System.out.println("6. Delete Product from list");
            System.out.println("0. Exit \n");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    printProductList();
                    break;
                case 3:
                    sellProduct();
                    break;
                case 4:
                	productUpdatePrice();
                	break;
                case 5:
                	productUpdateQuantity();
                case 6:
                	deleteProduct();
                	break;
                case 0:
                    System.out.println("\nExiting...\n");
                    
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }
        } while (choice != 0);
        
        sc.close();
    }

	// Add product in list of database
    
	public static void addProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter product details:");
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine(); 
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Price: ");
        double price = sc.nextDouble();
        System.out.print("Quantity: ");
        int quantity = sc.nextInt();
        try {

            TestConnection c = new TestConnection();
    		String query = "insert into shop(pid,pname,pprice,pqnt) values('"+id+"', '"+name+"', '"+price+"', '"+quantity+"')";
    		long res =  c.s.executeUpdate(query);
    		
    		if(res > 0) {
    			System.out.println("\n Product Added Successfully \n");
    		} else {
    			System.out.println("Failed");
    		}
    		
        } catch(Exception e) {
        	System.out.println(e);
        }
		
    }
	
	// Show product list in database
    
    public static void printProductList() {
        
        try {
        	TestConnection c = new TestConnection();
        	ResultSet rs = c.s.executeQuery("select * from shop");
        	if(rs != null) {
        		System.out.println("<<<<<<<<<<Product List>>>>>>>>>>");
				System.out.println("\nPID\tNAME\tPRICE\tQUANTITY");
				
				while(rs.next()) {
					System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getInt(3)+"\t"+rs.getInt(4));
					
				}
				System.out.println();
			} else {
				System.out.println("Connection Failed");
			}
        	
        } catch(Exception e) {
        	System.out.println(e);
        }
        
    }
    

	
	// update product price in database
	
    public static void productUpdatePrice() {
    	try {
    		TestConnection c = new TestConnection();
    		Scanner sc = new Scanner(System.in);
    		System.out.print("\nEnter product Id to Update: ");
	        int id = sc.nextInt();
	        System.out.print("Enter product Price to Update: ");
	        int price = sc.nextInt();
	        String query = "update shop set pprice="+"'"+price+"'"+" where pid="+"'"+id+"'";
			long res = c.s.executeUpdate(query);
	        if(res>0) {
	        	System.out.println("\nPrice Update\n");
	        } else {
	        	System.out.println("Failed");
	        }
    		    		
    	} catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    // update product quantity in database
    
    public static void productUpdateQuantity() {
    	try {
    		TestConnection c = new TestConnection();
    		Scanner sc = new Scanner(System.in);
    		System.out.print("\nEnter product Id to Update: ");
	        int id = sc.nextInt();
	        System.out.print("Enter product Quantity to Update: ");
	        int qnt = sc.nextInt();
	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
	        int nq=0;
	        while(rs.next()) {

	        	nq = qnt + rs.getInt("pqnt");
	        }
	        
	        String query = "update shop set pqnt="+"'"+nq+"'"+" where pid="+"'"+id+"'";
			long res = c.s.executeUpdate(query);
	        if(res>0) {
	        	System.out.println("\nQuantity Update\n");
	        } else {
	        	System.out.println("Failed");
	        }
    		
    	} catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    // Delete product in list and update in database
    
    public static void deleteProduct() {
    	try {
    		TestConnection c = new TestConnection();
    		Scanner sc = new Scanner(System.in);
    		System.out.println("Enter Product Id to Delete");
    		int id = sc.nextInt();
    		String query = "delete from shop " + "where pid = " + id;
    		long res = c.s.executeUpdate(query);
    		if(res>0) {
    			System.out.println("\nDelete Product Successfully\n");
    		} else {
    			System.out.println("Failed");
    		}
    		
    	} catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
// Sell Product and update quantity in database
    
    public static void sellProduct() {
		try {
			TestConnection c = new TestConnection();
	   	    Scanner sc = new Scanner(System.in);
	        System.out.print("\nEnter product Id to sell: ");
	        int id = sc.nextInt();
	        System.out.print("Enter quantity to sell: ");
	        int qnt = sc.nextInt();
	        
	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
	        int remain = 0;
	        
	        while(rs.next()) {
				if(rs.getInt("pqnt")<qnt) {
					
					System.out.println("\nQuantity not Available !\n");
					remain = remain+rs.getInt("pqnt");
					
				} else {
					remain = rs.getInt("pqnt")-qnt;
					
					System.out.println("\nProduct sell\n");
					updateInvoice(id, qnt);
					
				}
			}
	        
	        String query = "update shop set pqnt="+"'"+remain+"'"+" where pid="+"'"+id+"'";
			c.s.executeUpdate(query);
			System.out.println("You want to add Product");
			System.out.println("1. for Add");
			System.out.println("0. for exit and generate invoice");
			int ch = sc.nextInt();
			
			if(ch == 1) {
				sellProduct();
			}
			else if(ch == 0) {
				System.out.println("\nThank you for shopping\n");
				generateInvoice1();
				generateInvoice2();
			}
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
    
    // After sell product generate the invoice

	public static void updateInvoice(int id, int qnt) {
		try {
			String name="a";
			int price=0, total=0;
			TestConnection c = new TestConnection();
//	    	System.out.println("\n<<<<<<<<<<<<<Invoice>>>>>>>>>>>>>");
	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
	        while(rs.next()) {
	        	 name = rs.getString("pname");
	        	price = rs.getInt("pprice");
	        	
	        	total = qnt * rs.getInt("pprice");

	        }
	        
//	        System.out.println(id);
//	        System.out.println(name);
//	        System.out.println(price);
//	        System.out.println(qnt);
//	        System.out.println(total);
	        String query = "insert into bill(pid,pname,pprice,pqnt) values('"+id+"', '"+name+"', '"+price+"', '"+qnt+"')";
	        c.s.executeUpdate(query);
	        
	        String query1 = "insert into total(pid,tot) values('"+id+"','"+total+"')";
	        c.s.executeUpdate(query1);

		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	// for generateInvoice1 to get all data/list of sell products 
	
	public static void generateInvoice1() {
		try {
			TestConnection c = new TestConnection();
	    	System.out.println("\n<<<<<<<<<<<<<Invoice>>>>>>>>>>>>>");
	        ResultSet rs = c.s.executeQuery("select * from bill");
	        while(rs.next()) {
	        	System.out.println();
	        	System.out.println("Product Name  : "+rs.getString("pname"));
	        	System.out.println("Product Price : "+rs.getInt("pprice"));
	        	System.out.println("Product qnt   : "+rs.getInt("pqnt"));
	        	System.out.println();
	        }
	        String query = "delete from bill";
	        c.s.executeUpdate(query);
	        
	        
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	// for generate grand total and print
	
	public static void generateInvoice2() {
		try {
			TestConnection c = new TestConnection();
	    	
			int grandtotal=0;
	        ResultSet rs = c.s.executeQuery("select * from total");
	        while(rs.next()) {
	        	grandtotal = grandtotal + rs.getInt("tot");
	        }
	        System.out.println("Total Amount to Pay : "+grandtotal);
	        System.out.println();
	        String query1 = "delete from total";
	        c.s.executeUpdate(query1);
	        
		} catch(Exception e) {
			System.out.println(e);
		}
	}
    
    
    
    
    
    // updated class of sell data with multiple add product 
    
    
    
    
//    public static void sellProduct() {
//		try {
//			TestConnection c = new TestConnection();
//	   	    Scanner sc = new Scanner(System.in);
//	        System.out.print("\nEnter product Id to sell: ");
//	        int id = sc.nextInt();
//	        System.out.print("Enter quantity to sell: ");
//	        int qnt = sc.nextInt();
//	        
//	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
//	        int remain = 0;
//	        
//	        while(rs.next()) {
//				if(rs.getInt("pqnt")<qnt) {
//					
//					System.out.println("\nQuantity not Available !\n");
//					remain = remain+rs.getInt("pqnt");
//					
//				} else {
//					remain = rs.getInt("pqnt")-qnt;
//					
//					System.out.println("\nProduct sell\n");
////					generateInvoice(id, qnt);
//					
//				}
//			}
//	        
//	        String query = "update shop set pqnt="+"'"+remain+"'"+" where pid="+"'"+id+"'";
//			c.s.executeUpdate(query);
//			System.out.println("You want to add Product");
//			System.out.println("1. for Add");
//			System.out.println("0. for exit and generate invoice");
//			int ch = sc.nextInt();
//			
//			if(ch == 1) {
//				sellProduct();
//			}
//			else if(ch == 0) {
//				System.out.println("\nThank you for shopping\n");
//			}
//			
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
    
    
    
    
    // updated class of generate invoice after adding multiple products in 
    
    
    
    
    // old class of sell data and generate invoice with each other attatched for a single product
//	public static void generateInvoice2() {
//		try {
//			TestConnection c = new TestConnection();
//	    	
//			int grandtotal=0;
//	        ResultSet rs = c.s.executeQuery("select * from total");
//	        while(rs.next()) {
//	        	grandtotal = grandtotal + rs.getInt("tot");
//	        }
//	        System.out.println("Total Amount to Pay : "+grandtotal);
//	        System.out.println();
//	        String query1 = "delete from total";
//	        c.s.executeUpdate(query1);
//	        
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
    
    
    
    
//  // Sell Product and update quantity in database
    
//  public static void sellProduct() {
//		try {
//			TestConnection c = new TestConnection();
//	   	    Scanner sc = new Scanner(System.in);
//	        System.out.print("\nEnter product Id to sell: ");
//	        int id = sc.nextInt();
//	        System.out.print("Enter quantity to sell: ");
//	        int qnt = sc.nextInt();
//	        
//	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
//	        int remain = 0;
//	        
//	        while(rs.next()) {
//				if(rs.getInt("pqnt")<qnt) {
//					
//					System.out.println("\nQuantity not Available !\n");
//					remain = remain+rs.getInt("pqnt");
//					
//				} else {
//					remain = rs.getInt("pqnt")-qnt;
//					
//					System.out.println("\nProduct sell\n");
//					generateInvoice(id, qnt);
//					
//				}
//			}
//	        
//	        String query = "update shop set pqnt="+"'"+remain+"'"+" where pid="+"'"+id+"'";
//			c.s.executeUpdate(query);
//			
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
//  
//  // After sell product generate the invoice
//
//	public static void generateInvoice(int id, int qnt) {
//		try {
//			TestConnection c = new TestConnection();
//	    	System.out.println("\n<<<<<<<<<<<<<Invoice>>>>>>>>>>>>>");
//	        ResultSet rs = c.s.executeQuery("select * from shop where pid = '"+id+"'");
//	        while(rs.next()) {
//	        	System.out.println("Product Name  : "+rs.getString("pname"));
//	        	System.out.println("Product Price : "+rs.getInt("pprice")); 
//	        	System.out.println("Quantity      : "+qnt);
//	//        	int rate = rs.getInt("pprice");
//	        	int total = qnt * rs.getInt("pprice");
//	        	System.out.println("Total amount  : "+total+" Rs.\n");
//	        }
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//	}
    
}

