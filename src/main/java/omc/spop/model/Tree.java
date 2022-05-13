package omc.spop.model;

import java.util.ArrayList;
import java.util.List;

/***********************************************************
 * 2017.08.10	이원식	최초작성
 **********************************************************/

public class Tree{
	
    private String id;
    private String pid;    
    private String iconCls;
    private String query;
	private String qblock_name;
    private String partition_start;
    private String distribution;
    private String access_predicates;
    private String filter_predicates;    
    private Tree attributes;
    private List<Tree> children = new ArrayList<Tree>();
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getQblock_name() {
		return qblock_name;
	}
	public void setQblock_name(String qblock_name) {
		this.qblock_name = qblock_name;
	}
	public String getPartition_start() {
		return partition_start;
	}
	public void setPartition_start(String partition_start) {
		this.partition_start = partition_start;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	public String getAccess_predicates() {
		return access_predicates;
	}
	public void setAccess_predicates(String access_predicates) {
		this.access_predicates = access_predicates;
	}
	public String getFilter_predicates() {
		return filter_predicates;
	}
	public void setFilter_predicates(String filter_predicates) {
		this.filter_predicates = filter_predicates;
	}
	public Tree getAttributes() {
		return attributes;
	}
	public void setAttributes(Tree attributes) {
		this.attributes = attributes;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}    
}
