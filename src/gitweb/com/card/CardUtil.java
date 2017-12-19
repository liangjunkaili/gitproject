package gitweb.com.card;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import gitweb.com.auth.AuthUtil;
import gitweb.com.httpUtil.HttpClientUtil;
import net.sf.json.JSONObject;

public class CardUtil {

	private static final String UPLOADIMG_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	private static final String CARD_CREATE = "https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";
	private static final String QRCODE_CREATE = "https://api.weixin.qq.com/card/qrcode/create?access_token=ACCESS_TOKEN";
	private static final String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
	private static final String GET = "https://api.weixin.qq.com/card/code/get?access_token=ACCESS_TOKEN";
	//获取用户已领取卡券接口
	private static final String getcardlist = "https://api.weixin.qq.com/card/user/getcardlist?access_token=TOKEN";
	public static String upload(String filePath,String accessToken) throws IOException{
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw new IOException("文件不存在");
		}
		String url = UPLOADIMG_URL.replace("ACCESS_TOKEN", accessToken);
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
		String mediaId = jsonObject.getString(typeName);
		return mediaId;
	}
	/**
	 * 创建卡券
	 * @param token
	 * @param json
	 * @return
	 */
	public static int createCard(String token,String json){
		int result = 0;
		String url = CARD_CREATE.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	public static int createQrcode(String token,String json){
		int result = 0;
		String url = QRCODE_CREATE.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	//查询Code接口
	public static int get(String token,String json){
		int result = 0;
		String url = GET.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = HttpClientUtil.doPostStr(url,json);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	/**
	 * {
		 "errcode": 0, 
		 "errmsg": "ok", 
		 "ticket": "gQEo8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyWWp0bkJLaURjSzMxNmZodU5xNEwAAgSHSh5aAwQIBwAA", 
		 "expire_seconds": 1800, 
		 "url": "http://weixin.qq.com/q/02YjtnBKiDcK316fhuNq4L", 
		 "show_qrcode_url": "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQEo8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyWWp0bkJLaURjSzMxNmZodU5xNEwAAgSHSh5aAwQIBwAA"
		}
	 * @param args
	 */
	public static int viewQrcode(String ticket){
		int result = 0;
		String url = SHOW_QRCODE.replace("TICKET", ticket);
		JSONObject jsonObject = HttpClientUtil.doGetStr(url);
		System.out.println(jsonObject.toString());
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	public static String initCard(){
		Card c = new Card();
		c.setCard_type("GROUPON");
		GrouponCard groupon = new GrouponCard();
		groupon.setDeal_detail("99元试听课");
		BaseInfo base_info = new BaseInfo();
		base_info.setLogo_url("http://mmbiz.qpic.cn/mmbiz_png/QKEv8GPCZibwHZrmbWcQ6KOb3IKwlYSOmKqicICViacgbWbkPuOvhhJ1Wqe7DmJ8qFShicnbX0yicVpRr0HhpNza68Q/0");
		base_info.setCode_type("CODE_TYPE_TEXT");
		base_info.setBrand_name("MagicABC");
		base_info.setTitle("100元券");
		base_info.setColor("Color010");
		base_info.setNotice("请联系MagicABC");
		base_info.setDescription("不可与其他优惠同享");
		Sku sku = new Sku();
		sku.setQuantity(100);
		base_info.setSku(sku );
		DateInfo date_info = new DateInfo();
		date_info.setType("DATE_TYPE_FIX_TIME_RANGE");
		date_info.setBegin_timestamp((int) (new Date().getTime()/1000));
		date_info.setEnd_timestamp((int) (new Date().getTime()/1000)+24*60*60);
		base_info.setDate_info(date_info );
		groupon.setBase_info(base_info );
		c.setGroupon(groupon );
		BaseCard bc = new BaseCard();
		bc.setCard(c);
		String card = JSONObject.fromObject(bc).toString();
		return card;
	}
	public static String initQrcode(String cardId,String code){
		QrCard qc = new QrCard();
		qc.setAction_name("QR_CARD");
		qc.setExpire_seconds(1800);
		ActionInfo action_info = new ActionInfo();
		QRCode card = new QRCode();
		card.setCard_id(cardId);
		card.setCode(code);
		card.setIs_unique_code(false);
		card.setOuter_str("12b");
		action_info.setCard(card );
		qc.setAction_info(action_info );
		String s = JSONObject.fromObject(qc).toString();
		return s;
	}
	public static void main(String[] args) {
		String token = AuthUtil.getInstance().get_access_token().get("access_token");
		System.out.println(token);
		/*try {
			upload("C:\\Users\\admin\\Desktop\\临时\\9.png",token);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*Card c = new Card();
		c.setCard_type("GROUPON");
		GrouponCard groupon = new GrouponCard();
		groupon.setDeal_detail("双人套餐\n -进口红酒一支。\n孜然牛肉一份。");
		BaseInfo base_info = new BaseInfo();
		base_info.setLogo_url("http://mmbiz.qpic.cn/mmbiz_png/QKEv8GPCZibwHZrmbWcQ6KOb3IKwlYSOmKqicICViacgbWbkPuOvhhJ1Wqe7DmJ8qFShicnbX0yicVpRr0HhpNza68Q/0");
		base_info.setCode_type("CODE_TYPE_TEXT");
		base_info.setBrand_name("MagicABC");
		base_info.setTitle("100元券");
		base_info.setColor("Color010");
		base_info.setNotice("请联系MagicABC");
		base_info.setDescription("不可与其他优惠同享");
		Sku sku = new Sku();
		sku.setQuantity(100);
		base_info.setSku(sku );
		DateInfo date_info = new DateInfo();
		date_info.setType("DATE_TYPE_FIX_TIME_RANGE");
		date_info.setBegin_timestamp((int) (new Date().getTime()/1000));
		date_info.setEnd_timestamp((int) (new Date().getTime()/1000)+24*60*60);
		base_info.setDate_info(date_info );
		groupon.setBase_info(base_info );
		c.setGroupon(groupon );
		BaseCard bc = new BaseCard();
		bc.setCard(c);
		String card = JSONObject.fromObject(bc).toString();
		System.out.println(card);
		int code = createCard(token, card);
		System.out.println(code);*/
		QrCard qc = new QrCard();
		qc.setAction_name("QR_CARD");
		qc.setExpire_seconds(1800);
		ActionInfo action_info = new ActionInfo();
		QRCode card = new QRCode();
		card.setCard_id("pUqcuw2GznmFzF9hVuWerFTVAkeU");
		card.setCode("pFS7Fjg8kV1IdDz01r4SQwMkuCKc");
		card.setIs_unique_code(false);
		card.setOuter_str("12b");
		action_info.setCard(card );
		qc.setAction_info(action_info );
		String s = JSONObject.fromObject(qc).toString();
		System.out.println(s);
		createQrcode(token,s);
	}
}
