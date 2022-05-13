package omc.spop.base;

import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

/***********************************************************
 * 2017.02.01 홍길동 최초작성
 **********************************************************/
public class Role implements GrantedAuthority {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Privilege> privileges;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return this.name;
	}

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
