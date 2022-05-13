package omc.spop.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class Customer {
	private static Properties properties;
	String customer;

	{
		ResourceBundle bundle;
		Enumeration<?> enum1;
		String defaultResource = "";
        properties = new Properties();

        if(StringUtil.isNotEmpty(System.getProperty("configPath"))) { 
        	defaultResource=System.getProperty("configPath");
        		
        	Reader defaultReader=null ;
        	try {
        		defaultReader = new java.io.FileReader(defaultResource);
        	} catch (FileNotFoundException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}   
        	try {
        		properties.load(defaultReader);
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
        try {
            bundle = ResourceBundle.getBundle("customer");
            enum1 = bundle.getKeys();
            for (; enum1.hasMoreElements(); ) {
                String name = (String)enum1.nextElement();
                String value = bundle.getString(name);
                if(StringUtils.isEmpty(properties.getProperty(name)))
                properties.setProperty(name, value);
            }
        } catch (Exception e) {
        	Util.errPrtDumy(e);
        }

	}

	/** 
	 * 키로 값을 구한다.
	 * @param key  키 문자열
	 * @return 프라퍼티
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	public static Integer getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}

	public static boolean getBoolean(String key) {
		if(properties.getProperty(key).toUpperCase().equals("TRUE")) return true;
		return false;
	}
	
	public static Long getLong(String key) {
		return Long.parseLong(properties.getProperty(key));
	}
	
	
	/*********** 데이터 ***********/
	
	public Customer(String customer) {
		this.customer = customer;
	}

	public String getDriver() {
		System.out.println(Customer.getString("kbcd.iqms.driver"));
		return Customer.getString("kbcd.iqms.driver");
	}

	public String getUrl() {
		System.out.println(Customer.getString(customer + ".ip"));
		System.out.println(Customer.getString(customer + ".port"));
		System.out.println(Customer.getString(customer + ".sid"));
		String host = Customer.getString(customer + ".ip");
		String port = Customer.getString(customer + ".port");
		String sid = Customer.getString(customer + ".sid");
		return "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
	}

	public String getUser() {
		System.out.println(Customer.getString(customer + ".user"));
		return Customer.getString(customer + ".user");
	}

	public String getPasswd() {
		System.out.println(Customer.getString(customer + ".password"));
		return Customer.getString(customer + ".password");
	}

}
