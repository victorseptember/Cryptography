package DES;

import java.io.*;
import javax.swing.JFileChooser;

public class File_xyc {
	
	/**
	 * 为二进制字符串补零
	 * @param string
	 * @return
	 */
	public String Flie_stringAppend(String string) {    
		
		StringBuffer sb = new StringBuffer();
		sb.append(string);
		while(sb.length()<8) {
			sb.insert(0, '0');                     //在前面补零
		}
		
		return sb.toString();
	}
	
	/**
	 * 计算8位二进制字符串的值
	 * @param 16bits binStr
	 * @return char
	 */
	public int binStrToInt(String binStr) {       
		int[] temp = new Core_xyc().binStrToIntArray(binStr);      //先将二进制字符串转换成int数组
		int sum = 0;
		for(int i=0;i<temp.length;i++) {
			sum += temp[temp.length-1-i]<<i;        //二进制转换成十进制
		}
		
		return sum;
	}
	
	
	/**
	 * 对文件进行加解密(例如文本文件、声音、图片、视频文件等)
	 * @param fileSource
	 * @param fileDest
	 * @param key
	 * @param flag(0表示加密，1表示解密)
	 */
	public void File_DES(String fileSource, String fileDest,String key, int flag) {
		StringBuffer sBuffer = new StringBuffer();
		String stringTemp = new String();
		
		try {
			InputStream in = new FileInputStream(fileSource);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {  
			    String string = Integer.toBinaryString(tempbyte);
			    sBuffer.append(Flie_stringAppend(string));
			}
			
			in.close();
		}
		catch(IOException e) {
			System.out.print(e);
			e.printStackTrace();
		}
		
		stringTemp = new Core_xyc().DES(sBuffer.toString(), flag, key, 1);
		
		try {
			OutputStream out = new FileOutputStream(fileDest);
			for(int i=0;i<stringTemp.length()/8;i++) {
				String subStr = stringTemp.substring(0+8*i, 8+8*i);
				System.out.println(subStr);
				int temp = new File_xyc().binStrToInt(subStr);
				out.write(temp);    //write函数接收int型参数
				out.flush();
			}
			out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 打开文件导航，选择文件
	 * @return
	 */
	public String fileChooser() {
		String string = new String();
		
		/*默认在该目录打开文件导航*/
		JFileChooser dlg = new JFileChooser("C:\\Users\\xuyicong\\Documents\\report_files");
		dlg.setDialogTitle("选择文件");
		int result = dlg.showOpenDialog(null);
		
		if(result==JFileChooser.APPROVE_OPTION)
			string = dlg.getSelectedFile().getAbsolutePath();
		
		return string;
	}
	
	
	/**
	 * 打开文件导航，保存文件
	 * @return
	 */
	public String fileSave() {
		String string = new String();
		
		JFileChooser dlg2 = new JFileChooser("C:\\Users\\xuyicong\\Documents\\report_files");
		dlg2.setDialogTitle("新文件另存为");
		int result2 = dlg2.showSaveDialog(null);
		
		if (result2==JFileChooser.APPROVE_OPTION)
		{
			try {
				File newfile = dlg2.getSelectedFile();
				
				if(!newfile.exists()) {
					newfile.createNewFile();
					string = newfile.getAbsolutePath();
				}
			}catch (IOException a) {
				a.printStackTrace();
			}
		}
		
		return string;
	}
	public static void main(String args[]) {
		File_xyc file = new File_xyc();
		String fileSource = "C:\\Users\\xuyicong\\Documents\\report_files\\aa.png";
		String fileDest = "C:\\Users\\xuyicong\\Documents\\report_files\\bb.png";
		String fileSource_2 = "C:\\Users\\xuyicong\\Documents\\report_files\\cc.png";
		file.File_DES(fileSource, fileDest,"dsadfashfhd", 0);  //加密
		file.File_DES(fileDest, fileSource_2,"dsadfashfhd", 1);  //解密
		
		//file.a(fileSource,fileDest, 0);
		//file.a(fileDest, fileSource_2, 1);
	}
		
}
