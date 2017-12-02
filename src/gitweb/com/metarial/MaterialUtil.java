package gitweb.com.metarial;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONObject;

public class MaterialUtil {

	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String QUERY_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	private static final String QUERY_MEDIA_URL_VIDEO = "http://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("文件不存在");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		
		String BOUNDARY = "-------"+System.currentTimeMillis();
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("UTF-8");
		OutputStream out = new DataOutputStream(connection.getOutputStream());
		
		out.write(head);
		
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut))!=-1){
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		
		byte[] foot = ("\r\n--"+BOUNDARY+"--\r\n").getBytes("utf-8");
		out.write(foot);
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result =null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line = reader.readLine())!=null){
				buffer.append(line);
			}
			if(result==null){
				result = buffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(reader!=null){
				reader.close();
			}
		}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type+"_media_id";
		}
		String mediaId = jsonObject.getString(typeName);
		return mediaId;
	}
	
	
	protected static File fetchTmpFile(String media_id, String type,String token){
		  try {
		   String url = null;
		   //视频是http协议
		   if("video".equalsIgnoreCase(type)){
			   url = String.format(QUERY_MEDIA_URL_VIDEO, token, media_id);
		   }else{
			   url = String.format(QUERY_MEDIA_URL, token, media_id);;
		   }
		   URL u = new URL(url);
		   HttpURLConnection  conn = (HttpURLConnection) u.openConnection();
		   conn.setRequestMethod("POST");
		   conn.connect();
		   BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		   String content_disposition = conn.getHeaderField("content-disposition");
		   //微信服务器生成的文件名称
		   String file_name ="";
		   String[] content_arr = content_disposition.split(";");
		   if(content_arr.length  == 2){
		    String tmp = content_arr[1];
		    int index = tmp.indexOf("\"");
		    file_name =tmp.substring(index+1, tmp.length()-1);
		   }
		   //生成不同文件名称
		   File file = new File(file_name);
		   BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		   byte[] buf = new byte[2048];
		   int length = bis.read(buf);
		   while(length != -1){
		    bos.write(buf, 0, length);
		    length = bis.read(buf);
		   }
		   bos.close();
		   bis.close();
		   return file;
		 } catch (MalformedURLException e) {
		  e.printStackTrace();
		 } catch (IOException e) {
		  e.printStackTrace();
		 }
		 return null;
		 }
}
