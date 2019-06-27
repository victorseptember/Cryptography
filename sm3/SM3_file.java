package sm3;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;

public class SM3_file {

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
	
	
	public String file_sm3(String fileSource) {
		StringBuilder sBuilder = new StringBuilder();
		//String stringTemp = new String();
		String result = new String();
		
		try {
			InputStream in = new FileInputStream(fileSource);
			int tempbyte;
			while ((tempbyte = in.read()) != -1) {  
			    String string = Integer.toBinaryString(tempbyte);
			    if(string.length()<8) {
			    	do {
						string = '0' + string;
					} while (string.length()<8);
			    }
			    
			    sBuilder.append(string);
			}
			in.close();
			result = new SM3_xyc().iteration_xyc(sBuilder.toString(), 1);
			//result = new SM3_xyc().intArrayToHexString(a);
		}
		catch(IOException e) {
			System.out.print(e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		SM3_file test = new SM3_file();
		String fileSource = test.fileChooser();
		String str = test.file_sm3(fileSource);
		System.out.println(str);
	}
}
