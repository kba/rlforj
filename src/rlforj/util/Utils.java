package rlforj.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils
{

	public static String getStringFromResource(String resourceName) {
		InputStream iin=Utils.class.getResourceAsStream(resourceName);
		if(iin==null){
			System.err.println("Resource not found : "+resourceName);
			return "";
		}
		BufferedInputStream in=
			new BufferedInputStream(iin);

		byte[] buffer=new byte[1000];
		StringBuilder sb=new StringBuilder();

		while(true) {
			int len=0;
			try
			{
				len = in.read(buffer);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			if(len==-1)
				break;
			sb.append(new String(buffer, 0, len));
		}
		return sb.toString();
	}
}
