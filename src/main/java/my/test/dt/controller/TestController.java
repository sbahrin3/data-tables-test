package my.test.dt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import my.test.dt.Persistence;
import my.test.dt.entity.User;


@RestController
@RequestMapping("test")
public class TestController {
	
	Persistence db = Persistence.db();
	
	@RequestMapping("/users")
	public ResponseEntity<DataTable<User>> getUsers(HttpServletResponse res, HttpServletRequest req) {
		
		String orderColumn = req.getParameter("order[0][column]");
		String orderDirection = req.getParameter("order[0][dir]");
		String recordStart = req.getParameter("start");
		String recordLength = req.getParameter("length");
		String searchValue = req.getParameter("search[value]");
			
		int start = 0; 
		int length = 10;
		try {
			start = Integer.parseInt(recordStart);
		} catch ( Exception e ) { }
		
		try {
			length = Integer.parseInt(recordLength);
		} catch ( Exception e ) { }
		
		String q = "select u from User u ";
		String qf = "";
		if ( !"".equals(searchValue)) {
			qf += " where u.firstName like '%" + searchValue + "%' ";
			qf += " or u.lastName like '%" + searchValue + "%' ";
			q += qf;
		}
		
		int order = Integer.parseInt(orderColumn);
		switch ( order ) {
		case 0:
			q += " order by u.id ";
			break;
		case 1:
			q += " order by u.userName ";
			break;
		case 2:
			q += " order by u.firstName ";
			break;
		case 3:
			q += " order by u.lastName ";
			break;
		}
		q += ("desc".equals(orderDirection) ? "desc" : "asc");
		
		List<User> users = db.list(q, start, length);

		Long total = db.get("select count(u) from User u");
		Long totalFiltered = !"".equals(searchValue) ? db.get("select count(u) from User u " + qf) : total;
	    
	    DataTable<User> dataTable = new DataTable<User>();
	    dataTable.setData(users);
	    dataTable.setiTotalRecords(total);
	    dataTable.setiTotalDisplayRecords(totalFiltered);
	    	
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("My-Header", "Hello");
	    
		return new ResponseEntity<DataTable<User>>(dataTable, headers, HttpStatus.OK);
		
	}
	
}
