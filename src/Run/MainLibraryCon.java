package Run;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import by.htp.dao.BookDao;
import  by.htp.dao.imple.BookDaoImple;
import by.htp.entity.Autor;
import by.htp.entity.Book;

public class MainLibraryCon {

	public static void main(String[] args)  {
		BookDaoImple dao =new BookDaoImple();
Book book= dao.read(2);
System.out.println(book);
System.out.println();
List <Book> listBook=dao.list();

for(Book b: listBook ) {
	System.out.println(b);
	
}

Autor au =new Autor();
Book newB =new Book("BestBook", au);
int z=  dao.getMaxId("book");
//System.out.println(z);

SimpleDateFormat sf= new SimpleDateFormat("yyyy,MM,dd");
Calendar gc=Calendar.getInstance();
try {
	gc.setTime(sf.parse("1230,03,03"));
} catch (ParseException e) {

	e.printStackTrace();
}

System.out.println();
Book b2=new Book("superBook2", new Autor("Oleg2","Kolev2", gc),1);
System.out.println(b2);
dao.add(b2);
//dao.delete(b2);
dao.update(b2);	
	

//System.out.println(sf.format(gc.getTime()));
//Date date =new Date(gc.getTime().getTime());
//System.out.println(date);
//
//Calendar gc2 =Calendar.getInstance();
//gc2.setTime(date);
//System.out.println(sf.format(gc2.getTime()));
//
//Date date2 =new Date(gc2.getTime().getTime());
//System.out.println(date2);






	}

}
