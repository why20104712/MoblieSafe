package cn.why.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTools {
	/**
	 * 从输入流中读取数据到String里
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String readFromStream(InputStream inputStream) throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while((length = inputStream.read(buffer)) != -1){
			byteArrayOutputStream.write(buffer, 0, length);
		}
		inputStream.close();
		String result = byteArrayOutputStream.toString();
		byteArrayOutputStream.close();
		return result;
	}
}
