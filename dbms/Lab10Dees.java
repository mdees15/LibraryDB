import java.sql.*;
import javax.swing.*;

public class Lab10Dees {



	//Query library database for valid member id --> DONE
	static boolean verify(Statement stmt, String id){
		String sqlQuery = "SELECT * FROM Member WHERE MemberID = " + id;
		try{
			ResultSet rs = stmt.executeQuery(sqlQuery); 
			if(rs.next() == false){
				return false;
			}
			JOptionPane.showMessageDialog(null, "Found member " + id + " in the database, proceeding to book look up");
			return true;
		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching for memberID");
		}
		return false;
	}



	//Create a new member in the library databse --> DONE
	static void createMember(Statement stmt){

		//Get the memberID by finding the highest value id in the database and adding one --> DONE
		String memberID = "";
		int memberInt = 0;
		String sqlQuery = "SELECT MAX(MemberID) AS \"Max_ID\" FROM Member";
		try{
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while(rs.next()){
				memberInt = rs.getInt("Max_ID");
			}
			memberInt += 1;
			memberID = Integer.toString(memberInt);
		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching for the max memberID int");
		}

		//Get user info and create a new member --> DONE
		String first = JOptionPane.showInputDialog ("Input first name:");
		String last = JOptionPane.showInputDialog ("Input last name:");
		String dob = JOptionPane.showInputDialog ("Input date of birth (YYYY-MM-DD):");
		String gender = JOptionPane.showInputDialog ("Input gender (M or F)");
		
		String sqlInsert = "INSERT INTO Member VALUES (" + memberID + ",\'" + first + "\',\'" + last + "\',\'" + dob + "\',\'" + gender + "\')";
		try{
			stmt.executeUpdate(sqlInsert);
			JOptionPane.showMessageDialog(null, "Successfully created new member " + memberID);
		}
		catch(Exception e){
			System.out.println("Connection/sql error while inserting new member into database");
		}
	
	}



	//Find the book stuff, maybe an arraylist of strings about the books? --> DONE
	static String bookLookupRunner(Statement stmt){

		String books = "You need to enter a valid option or field to find books";

		String option = JOptionPane.showInputDialog ("Input ISBN to look up books by isbn, TITLE to look up books by title, or AUTHOR to look up a list of books from the author");

		if(option.equals("ISBN")){
			books = isbnSearch(stmt);
		}

		if(option.equals("TITLE")){
			books = titleSearch(stmt);
		}

		if(option.equals("AUTHOR")){
			books = authorSearch(stmt);
		}

		return books;
	}



	static String isbnSearch(Statement stmt){ //--> DONE

		String input = JOptionPane.showInputDialog ("Input desired ISBN (xx-xxxxx-xxxxx)");
		String sqlQuery = "SELECT * FROM LocatedAt WHERE ISBN = \'" + input + "\'";
		String found = "";
		boolean available = false;
		boolean stock = false;
		
		try{
			ResultSet rs = stmt.executeQuery(sqlQuery); 

			while(rs.next()){

				stock = true;
				if(rs.getInt("CopiesNotCheckedOut") > 0){ //If there is more than one avaliable copy, then append data to string, else do nothing
					available = true;
					String lib = rs.getString("Name");
					int shelf = rs.getInt("Shelf");
					found += "Library: " + lib + " | Shelf: " + Integer.toString(shelf) + "\n";
				}

			}

		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching by isbn: " + input);
		}
		
		if(!available && stock){ //no available copies
			String msg = "There are no currently available copies";
			return msg;

		}

		if(!stock){//not in stock at all
			String msg = "Neither library currently has the book in stock";
			return msg;
		}

		return found;

	}



	static String titleSearch(Statement stmt){ //--> DONE

		String input = JOptionPane.showInputDialog ("Input desired title");
		String sqlQuery = "SELECT * FROM LocatedAt l, Book b WHERE b.Title = \'" + input + "\' AND b.ISBN = l.ISBN";
		String found = "";
		boolean available = false;
		boolean stock = false;
		
		try{
			ResultSet rs = stmt.executeQuery(sqlQuery); 

			while(rs.next()){
				
				stock = true;
				if(rs.getInt("CopiesNotCheckedOut") > 0){ //If there is more than one avaliable copy, then append data to string, else do nothing
					available = true;
					String lib = rs.getString("Name");
					int shelf = rs.getInt("Shelf");
					found += "Library: " + lib + " | Shelf: " + Integer.toString(shelf) + "\n";
				}

			}

		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching by isbn: " + input);
		}
		
		if(!available && stock){ //no available copies
			String msg = "There are no currently available copies";
			return msg;

		}

		if(!stock){//not in stock at all
			String msg = "Neither library currently has the book in stock";
			return msg;
		}

		return found;

	}


	
	static String authorSearch(Statement stmt){ //--> DONE

		String authorId = JOptionPane.showInputDialog ("Input desired author ID for a book list");
		String sqlAuthorQuery = "SELECT b.Title, b.ISBN FROM Author a, WrittenBy wb, Book b WHERE a.AuthorID = \'" + authorId + "\' AND wb.AuthorID = a.AuthorID AND b.ISBN = wb.ISBN";
		String list = "";
		boolean exists = false;

		try{
			ResultSet rs = stmt.executeQuery(sqlAuthorQuery);

			while(rs.next()){
				exists = true;
				list += "Title: " + rs.getString("Title") + " | ISBN: " + rs.getString("ISBN") + "\n";

			}

		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching by Author: " + authorId);
		}

		String input = JOptionPane.showInputDialog ("Input desired title from: \n" + list);
		String sqlQuery = "SELECT * FROM LocatedAt l, Book b WHERE b.Title = \'" + input + "\' AND b.ISBN = l.ISBN";
		String found = "";
		boolean available = false;
		boolean stock = false;
		
		try{
			ResultSet rs = stmt.executeQuery(sqlQuery); 

			while(rs.next()){
				
				stock = true;
				if(rs.getInt("CopiesNotCheckedOut") > 0){ //If there is more than one avaliable copy, then append data to string, else do nothing
					available = true;
					String lib = rs.getString("Name");
					int shelf = rs.getInt("Shelf");
					found += "Library: " + lib + " | Shelf: " + Integer.toString(shelf) + "\n";
				}

			}

		}
		catch(Exception e){
			System.out.println("Connection/sql error while searching by isbn: " + input);
		}
		
		if(!available && stock){ //no available copies
			String msg = "There are no currently available copies";
			return msg;

		}

		if(!stock){//not in stock at all
			String msg = "Neither library currently has the book in stock";
			return msg;
		}

		return found;

	}



	public static void main(String args[]){

		Connection con = null;

		try {
			//SQL Connection Stuff---------------------------------------------------------------
			Statement stmt;
			ResultSet rs;

			Class.forName("com.mysql.cj.jdbc.Driver");

			String url ="jdbc:mysql://faure/mdees15";

			con = DriverManager.getConnection(url,"mdees15", "831900280");

			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);

			stmt = con.createStatement();

			//GUI Stuff in here------------------------------------------------------------------

			boolean run = true;
			while(run){		
				String value = JOptionPane.showInputDialog ("Input member identification or QUIT to quit:");
				
				if(value.equals("QUIT")){
					run = false;
				}
				else{
					boolean inLibrary = verify(stmt, value);

					if(!inLibrary){

						JOptionPane.showMessageDialog(null, "MemberID: " + value +", is not in the database, creating new member");
						createMember(stmt);

					}

					String info = bookLookupRunner(stmt);
					JOptionPane.showMessageDialog(null, info);
				}
			}
			


			//GUI Stuff in here------------------------------------------------------------------
			con.close();
		}
		catch( Exception e ) {

			e.printStackTrace();

		}//end catch

	}//end main

}//end class Lab4A_ex
