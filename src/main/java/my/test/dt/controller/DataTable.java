package my.test.dt.controller;

import java.util.ArrayList;
import java.util.List;

public class DataTable<T> {
	
	private List<T> data = new ArrayList<T>();
	private Long iTotalRecords;
	private Long iTotalDisplayRecords;

	public List<T> setData() {
		return data;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(Long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public Long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	

}
