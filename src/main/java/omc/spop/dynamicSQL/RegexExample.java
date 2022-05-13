package omc.spop.dynamicSQL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
        String pattern = "^[0-9]*$"; //숫자만
        String val = "123456789"; //대상문자열
    
        boolean regex = Pattern.matches(pattern, val);
        System.out.println(regex);
        */
        

		boolean regex;
		
        String pattern1 = "AND |OR";
        Pattern  pattern = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);

        String val1 = "(AND username=#{username}"; //대상문자열
        String val2 = "or password=#{password}"; //대상문자열
        String val3 = " or or password=#{password}"; //대상문자열

        Matcher matcher = pattern.matcher(val1);
        if (matcher.find()) {
        	System.out.println("Match String start(): " + matcher.start());
        	System.out.println("Match String end(): " + matcher.end());
        	System.out.println("Match String : " + val1.substring(matcher.start(), matcher.end()));        	
        }
        matcher = pattern.matcher(val2);
        if (matcher.find()) {
        	System.out.println("Match String start(): " + matcher.start());
        	System.out.println("Match String end(): " + matcher.end());
        	System.out.println("Match String : " + val2.substring(matcher.start(), matcher.end())); 
        }
        
        matcher = pattern.matcher(val3);
        if (matcher.find()) {
        	System.out.println("Match String start(): " + matcher.start());
        	System.out.println("Match String end(): " + matcher.end());
        	System.out.println("Match String : " + val3.substring(matcher.start(), matcher.end()));
        	String sub;
        	sub = val3.substring(0, matcher.start());
        	val3.substring(matcher.start(), matcher.end());
        	sub += val3.substring(matcher.end(), val3.length());
        	System.out.println("sub String : [" + sub + "]");
        	
        }
        
        Pattern p = Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(val3); // get a matcher object
        String REPLACE = "";
        StringBuffer sb = new StringBuffer();
        //m.end();
        if (m.find()) {
        //while (m.find()) {
          m.appendReplacement(sb, REPLACE);
          m.appendTail(sb);
          System.out.println("[" + sb.toString() +"]");
        //}
        }

        //String pattern2 = "\\)";
        String pattern2 = "AND | OR";
        String val4 = "OR para2) = #{data2),";
        System.out.println(val4.replaceFirst("pattern2", "X"));
        Pattern p1 = Pattern.compile(pattern2, Pattern.CASE_INSENSITIVE);
        Matcher m1 = p.matcher(val4); // get a matcher object
        String REPLACE1 = "X";
        StringBuffer sb1 = new StringBuffer();
        //m.end();
        //if (m1.find()) {
        
        //if (m1.find()) {
        while (m.find()) {
        	
          m1.appendReplacement(sb1, REPLACE);
          
          System.out.println("start : " + m1.start());
          System.out.println("end : " + m1.end());
          System.out.println("[" + sb1.toString() +"]");
        //}
        }
        m1.appendTail(sb1);
        System.out.println("[" + sb1.toString() +"]");

	}

}
