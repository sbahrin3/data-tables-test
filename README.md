# data-tables-server-side-ajax
Testing DataTables server side ajax implementation.

To run: http://localhost:8080/data-tables-test/content/datatables.html

This is the simplest way on how to use the DataTables with the **server-side processing** enabled, using Java
with Spring MVC.

The client side is in src/main/webapp/WEB-INF/content/datatables.html, and the datatables script is as below:

```
$('#table_users').DataTable({
    ajax: { url:'../test/users',
    		dataSrc: 'data',
    		data: function (data) {
    	      for (var i = 0, len = data.columns.length; i < len; i++) {
    	        if (! data.columns[i].search.value) delete data.columns[i].search;
    	        if (data.columns[i].searchable === true) delete data.columns[i].searchable;
    	        if (data.columns[i].orderable === true) delete data.columns[i].orderable;
    	        if (data.columns[i].data === data.columns[i].name) delete data.columns[i].name;
    	      }
    	      delete data.search.regex;
    		}
    },
    serverSide: true,
    columns: [
        {
            data: 'id'
        },
        {
            data: 'userName'
        },	            
        {
            data: 'firstName'
        },
        {
            data: 'lastName'
        }
    ]
});
```

The call to server REST url is "../test/users", which is defined in the Java class TestController.java, and the code snippet dealing with the DataTables is as below:

```java
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
```

I did not consider these as finished yet, there will be updates/enhancements in the future.  If you think some parts need to be improved or I wrongly doing it, please tell me.

I am using Hibernate as the Persistence API for database operation.

Thank You.
Shamsul Bahrin

