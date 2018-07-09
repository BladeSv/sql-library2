package by.htp.dao.imple;
import static by.htp.dao.util.MySqlPropretyManager.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import by.htp.dao.BookDao;
import by.htp.entity.Autor;
import by.htp.entity.Book;

public class BookDaoImple implements BookDao{
private static final String SELECT_BOOK_BYID="SELECT * FROM book JOIN autor ON book.id_autor=autor.id WHERE book.id=?";
private static final String INSERT_AUTOR="INSERT INTO autor (name, surname, birthday) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM autor WHERE name=? AND surname=? AND birthday=? ) ";
private static final String INSERT_BOOK="INSERT INTO book (title, id_autor) VALUES( ? ,(SELECT autor.id FROM autor WHERE autor.name=? AND autor.surname= ? AND autor.birthday=?))";
private static final String UPDATE_BOOK="UPDATE book SET book.title=?, book.id_autor=(SELECT autor.id FROM autor WHERE autor.name=? AND autor.surname= ? AND autor.birthday=?) WHERE book.id=?";
private static final String DELETE_BOOK="DELETE book FROM book JOIN autor ON book.id_autor=autor.id WHERE book.title=? AND  autor.name=? AND autor.surname=? AND autor.birthday=?";

private static final String SELECT_BOOK_LIST="SELECT * FROM book JOIN autor ON book.id_autor=autor.id";
private PreparedStatement ps;
ConnectionBD conDB= new ConnectionBD();


private Autor buildAutor(ResultSet rs ) throws SQLException {
	Autor autor=new Autor();
	autor.setId(rs.getInt("id_autor"));
	autor.setName(rs.getString("name"));
	autor.setSurname(rs.getString("surname"));
	
	Calendar cal=Calendar.getInstance();
	
	cal.setTime(rs.getDate("birthday"));
	autor.setBithDate(cal);
	return autor;
	
	
}

private Book buildBook(ResultSet rs ) throws SQLException {
	Book book=new Book();
	book.setTitle(rs.getString(2));
	book.setId(rs.getInt(1));
	book.setAutor(buildAutor(rs));
	return book;
}



	@Override
	public Book read(int id) {
		Book book=null;
		try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
			
		PreparedStatement ps=conn.prepareStatement(SELECT_BOOK_BYID);	
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			book=buildBook(rs);
			
			
		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return book;
	}

	
	
	@Override
	public List<Book> list() {
		List<Book> listBook=new ArrayList<>();
		
		try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
			PreparedStatement ps=conn.prepareStatement(SELECT_BOOK_LIST);
			ResultSet rs = ps.executeQuery();
			//int id=1;
			//int cheak=getMaxId("book");
			boolean cheak=false;
			while(cheak=rs.next()) {
			Book book=null;
			
			if(cheak) {
				book=buildBook(rs);
				listBook.add(book);
				
			}
		
			};
			} catch (SQLException e) {
				
				e.printStackTrace();
			}

		
		return listBook;
	}

	@Override
	public int add(Book book) {
		try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
			
			
			
			PreparedStatement ps=conn.prepareStatement(INSERT_AUTOR);	
			
			ps.setString(1, book.getAutor().getName());
			ps.setString(4, book.getAutor().getName());
			ps.setString(2, book.getAutor().getSurname());
			ps.setString(5, book.getAutor().getSurname());
		
			Date date =new Date( book.getAutor().getBithDate().getTime().getTime());
			date.setDate(date.getDate()+1);
		
			
			ps.setDate(3, date);
			ps.setDate(6, date);
		
			ps.executeUpdate();
			
			 ps=conn.prepareStatement(INSERT_BOOK);		
			
			 ps.setString(1, book.getTitle());
			 ps.setString(2, book.getAutor().getName());
			 ps.setString(3, book.getAutor().getSurname());
				ps.setDate(4, date);
				ps.executeUpdate();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
		
		
		return 0;
	}

	@Override
	public void delete(Book book) {
		
			try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
				
				
				ps=conn.prepareStatement(DELETE_BOOK);		
				
				 ps.setString(1, book.getTitle());
				 ps.setString(2, book.getAutor().getName());
				 ps.setString(3, book.getAutor().getSurname());
				 
				 Date date =new Date( book.getAutor().getBithDate().getTime().getTime());
					date.setDate(date.getDate()+1);
				
					
					ps.setDate(4, date);
				
					ps.executeUpdate();
				
				System.out.println("Book successfully deleted");
				
	
					
				
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
		
	}

	@Override
	public void update(Book book) {
		try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
			
	PreparedStatement ps=conn.prepareStatement(INSERT_AUTOR);	
			
			ps.setString(1, book.getAutor().getName());
			ps.setString(4, book.getAutor().getName());
			ps.setString(2, book.getAutor().getSurname());
			ps.setString(5, book.getAutor().getSurname());
		
			Date date =new Date( book.getAutor().getBithDate().getTime().getTime());
			date.setDate(date.getDate()+1);
		
			
			ps.setDate(3, date);
			ps.setDate(6, date);
		
			ps.executeUpdate();
			
			 ps=conn.prepareStatement(UPDATE_BOOK);		
			
			 ps.setString(1, book.getTitle());
			 ps.setString(2, book.getAutor().getName());
			 ps.setString(3, book.getAutor().getSurname());
				ps.setDate(4, date);
			ps.setInt(5, book.getId());	
				ps.executeUpdate();
			
				System.out.println("Book successfully update");	
			
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		
	}

	public int getMaxId(String table) {
		int i=0;
		
		
		
		try(Connection conn= DriverManager.getConnection(getUrl(), getLogin(), getPass() )){
			String url="SELECT max(id) FROM " +table;
			PreparedStatement ps=conn.prepareStatement(url);	
		
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				i=rs.getInt(1);
				
			
			}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		
		
		
		
		
		return i;
	}
	
}
