package com.cn.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class ExchangerKafkaHelper {
	private static final int fillchar = '=';

	private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";

	public static byte[] getFileContent(String file) throws Exception {
		byte[] bs = null;
		File f = new File(file);
		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile(f, "r");
			bs = new byte[(int)f.length()];
			reader.read(bs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception ex) {}
		}
		return bs;
	}

	public static void writerToFile(String file, byte[] bs) throws Exception {
		File f = new File(file);
		RandomAccessFile writer = null;
		try {
			if (f.exists() && f.isFile()) {
				f.delete();
			}
			f.createNewFile();
			writer = new RandomAccessFile(f, "rw");
			writer.write(bs);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
			}
		}
	}

	public static String encodeBase64(byte[] data) {
		int c;
		int len = data.length;
		StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
		for (int i = 0; i < len; ++i) {
			c = (data[i] >> 2) & 0x3f;
			ret.append(cvt.charAt(c));
			c = (data[i] << 4) & 0x3f;
			if (++i < len)
				c |= (data[i] >> 4) & 0x0f;

			ret.append(cvt.charAt(c));
			if (i < len) {
				c = (data[i] << 2) & 0x3f;
				if (++i < len)
					c |= (data[i] >> 6) & 0x03;

				ret.append(cvt.charAt(c));
			} else {
				++i;
				ret.append((char) fillchar);
			}

			if (i < len) {
				c = data[i] & 0x3f;
				ret.append(cvt.charAt(c));
			} else {
				ret.append((char) fillchar);
			}
		}
		return ret.toString();
	}

	public static String decodeBase64(byte[] data) {
		int c, c1;
		int len = data.length;
		StringBuffer ret = new StringBuffer((len * 3) / 4);
		for (int i = 0; i < len; ++i) {
			c = cvt.indexOf(data[i]);
			++i;
			c1 = cvt.indexOf(data[i]);
			c = ((c << 2) | ((c1 >> 4) & 0x3));
			ret.append((char) c);
			if (++i < len) {
				c = data[i];
				if (fillchar == c)
					break;

				c = cvt.indexOf((char) c);
				c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
				ret.append((char) c1);
			}

			if (++i < len) {
				c1 = data[i];
				if (fillchar == c1)
					break;

				c1 = cvt.indexOf((char) c1);
				c = ((c << 6) & 0xc0) | c1;
				ret.append((char) c);
			}
		}
		return ret.toString();
	}

	public static byte[] deCode(String src)
	{
		if (src.length() < 2)
		{
			return new byte[0];
		}
		byte dest[] = new byte[src.length() / 2];
		Arrays.fill(dest, (byte) 0);
		int index = 0;
		for (int ii = 0; ii < src.length() - 1; ii++)
		{
			String str = "#" + src.substring(ii, ii + 2);
			byte rb = (byte) Integer.decode(str).intValue();
			dest[index++] = rb;
			ii++;
		}

		return dest;
	}

	public static String enCode(byte bsrc[])
	{
		String dest = "";
		if (bsrc == null)
		{
			return "";
		}
		for (int ii = 0; ii < bsrc.length; ii++)
		{
			byte bb = bsrc[ii];
			int num;
			if (bb >= 0)
			{
				num = bb;
			}
			else
			{
				num = (bb & 0x7f) + 128;
			}
			String str = Integer.toHexString(num);
			if (str.length() < 2)
			{
				str = "0" + str;
			}
			dest = dest + str.toUpperCase();
		}

		return dest;
	}


	private static void _$20940(InputStream in, OutputStream out) throws Exception {
		int count = 0;
		byte[] temp = new byte[8192];
		while (count >= 0) {
			count = in.read(temp);
			if (count > 0)
				out.write(temp, 0, count);
		}
		in.close();
		out.close();
	}

	public static byte[] getFileContentToByte(String fileUrl) throws Exception/*fileUrl网络资源地址*/
	{
		HttpURLConnection connection=null;

		    URL url = new URL(fileUrl);
			connection = (HttpURLConnection)url.openConnection();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream out=new ByteArrayOutputStream(1024*1024);
			byte[] temp=new byte[1024*1024*1024];

			int n=0;
			while((n=bufferedInputStream.read(temp))!=-1)
			{
				out.write(temp, 0, n);
			}
			byte[] bytes_new=out.toByteArray();
			bufferedInputStream.close();
			connection.disconnect();

			return bytes_new;







	}



}
