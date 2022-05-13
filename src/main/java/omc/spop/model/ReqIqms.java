package omc.spop.model;

import java.io.Serializable;
/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class ReqIqms implements Serializable {

	private static final long serialVersionUID = -7788619177798333712L;

	private String p_cmid;
	private String p_systemgb;
	private String p_servicegb;
	private String p_resultyn;

	public String getP_cmid() {
		return p_cmid;
	}

	public void setP_cmid(String p_cmid) {
		this.p_cmid = p_cmid;
	}

	public String getP_systemgb() {
		return p_systemgb;
	}

	public void setP_systemgb(String p_systemgb) {
		this.p_systemgb = p_systemgb;
	}

	public String getP_servicegb() {
		return p_servicegb;
	}

	public void setP_servicegb(String p_servicegb) {
		this.p_servicegb = p_servicegb;
	}

	public String getP_resultyn() {
		return p_resultyn;
	}

	public void setP_resultyn(String p_resultyn) {
		this.p_resultyn = p_resultyn;
	}

}
