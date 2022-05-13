package omc.spop.model;

import java.io.Serializable;
import java.util.List;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class RtnMsg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String err_msg = "";
	private List<Status> ret_data = null;
	private String ret_code = "";

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public List<Status> getRet_data() {
		return ret_data;
	}

	public void setRet_data(List<Status> ret_data) {
		this.ret_data = ret_data;
	}

	public String getRet_code() {
		return ret_code;
	}

	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}

}
