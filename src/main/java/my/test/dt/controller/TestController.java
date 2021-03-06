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

/**
 * 
 * @author Shamsul Bahrin
 *
 */

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

		int start = recordStart != null && recordStart.matches("\\d+") ? Integer.parseInt(recordStart) : 0;
		int length = recordLength != null && recordLength.matches("\\d+") ? Integer.parseInt(recordLength) : 10;
		int order = orderColumn != null && orderColumn.matches("\\d+") ? Integer.parseInt(orderColumn) : 0;
		
		String[] fields = {"id", "userName", "firstName", "lastName"};
		
		String q = "select u from User u ";
		String qf = "";
		if ( !"".equals(searchValue)) {
			for ( int i=0; i < fields.length; i++ ) {
				qf += i == 0 ? " where " : " or ";
				qf += "u." + fields[i] + " like '%" + searchValue + "%' ";
			}
			q += qf;
		}
		
		
		q += " order by u." + fields[order];
		q += ("desc".equals(orderDirection) ? " desc" : " asc");
		
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
