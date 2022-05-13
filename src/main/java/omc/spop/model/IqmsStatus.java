package omc.spop.model;

import java.io.Serializable;
//import java.util.Date;

//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.databind.ser.std.DateSerializer;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class IqmsStatus implements Serializable {

	private static final long serialVersionUID = -7788619177798333712L;

	private String compcd;
	private String cmid;
	private String status;

	public String getCompcd() {
		return compcd;
	}

	public void setCompcd(String compcd) {
		this.compcd = compcd;
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// @JsonSerialize(using=DateSerializer.class)

}
