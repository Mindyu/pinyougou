package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果类
 * @author YCQ
 *
 */
public class PageResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long total;
	private List rows;
	
	public PageResult(long total,List rows) {
		this.total = total;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "PageResult [total=" + total + ", rows=" + rows + "]";
	}
	
}
