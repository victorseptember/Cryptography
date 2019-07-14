package DES;

public class Core_xyc {
	//IP initial replacement
    private int[] IP={58,50,42,34,26,18,10,2,
                     60,52,44,36,28,20,12,4,
                     62,54,46,38,30,22,14,6,
                     64,56,48,40,32,24,16,8,
                     57,49,41,33,25,17,9,1,
                     59,51,43,35,27,19,11,3,
                     61,53,45,37,29,21,13,5,
                     63,55,47,39,31,23,15,7};
    //IP inverse replacement
    private int[] IP_1={40,8,48,16,56,24,64,32,
                       39,7,47,15,55,23,63,31,
                       38,6,46,14,54,22,62,30,
                       37,5,45,13,53,21,61,29,
                       36,4,44,12,52,20,60,28,
                       35,3,43,11,51,19,59,27,
                       34,2,42,10,50,18,58,26, 
                       33,1,41,9,49,17,57,25};

    /**
     * 为二进制字符串补零
     * @param binary string
     * @return 16bits binary string
     */
	public String stringAppend(String string) {    
		
		StringBuffer sb = new StringBuffer();
		sb.append(string);
		while(sb.length()<16) {
			sb.insert(0, '0');                     //在前面补零
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 字符串转换成二进制字符串 
	 * @param initial string
	 * @return binary string
	 */
	public String stringToBinary(String str) {    
		char[] chars = str.toCharArray();
		String string = new String();
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<chars.length;i++) {
			string = Integer.toBinaryString(chars[i]);  
			sb.append(stringAppend(string));
		}
		
		return sb.toString();
	}
	
	/**
	 * 二进制字符串转换成字符串
	 * @param binary string
	 * @return string
	 */
	public String binStrToString(String string) {     
		
		 char c;
		 String sub = new String();
		 StringBuffer sBuffer = new StringBuffer();
		 
		 for(int i=0;i<string.length()/16;i++) {
			 sub = string.substring(0+16*i, 16+16*i);
			 c = binStrToChar(sub);                 //先将16bits的二进制字符串转换成字符
			 sBuffer.append(c);
		 } 
		 
		 return sBuffer.toString();
	}
	
	
	/**
	 * 二进制字符串转成数组
	 * @param binStr
	 * @return (int)array
	 */
	public int[] binStrToIntArray(String binStr) {   
		char chars[] = binStr.toCharArray();
		int[] temp = new int[binStr.length()];
		
		for(int i=0;i<binStr.length();i++) {
			temp[i] = chars[i] - '0';
		}
		
		return temp;	
	}
	
	
	/**
	 * int数组转换成二进制字符串
	 * @param (int)array
	 * @return binary string
	 */
	public String intArraytoString(int[] a) {      
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0;i<a.length;i++) {
			sBuffer.append(a[i]);
		}
		
		return sBuffer.toString();
	}
	
	
	/**
	 * 16位二进制字符串转换成相应字符
	 * @param 16bits binStr
	 * @return char
	 */
	public char binStrToChar(String binStr) {       
		int[] temp = binStrToIntArray(binStr);      //先将二进制字符串转换成int数组
		int sum = 0;
		for(int i=0;i<temp.length;i++) {
			sum += temp[temp.length-1-i]<<i;        //二进制转换成十进制
		}
		
		return (char)sum;
	}
	
	
	/**
	 * 将初始字符串转换成填充后的二进制字符串
	 * @param flag(0表示字符串填充，1表示对bit流填充)
	 * @param original plaintext or ciphertext
	 * @return filled bit flows
	 */
	public String fill_Message(String str, int flag) {  
		if(flag==0) {
			int add_length = (4 - str.length()%4)%4;              //计算要还要填充的字符数
			str = stringToBinary(str);
			StringBuffer sb = new StringBuffer();
			sb.append(str);
			for(int i=0;i<add_length*16;i++) {
				sb.append('0');
			}
			
			return sb.toString();   
		}else {
			int add_length = (64 - str.length()%64)%64;
			for(int i=0;i<add_length;i++) {
				str = str + '0';
			}
			
			return str;
		}
	}
	
	/**
	 * IP置换
	 * @param initial array a
	 * @return substituted array
	 */
	public int[] IP_change(int[] a) {        
		int[] temp = new int[64];
		for(int i=0;i<a.length;i++) {
			temp[i] = a[IP[i]-1];
		}
		
		return temp;
	}
	
	
	/**
	 * IP逆置换
	 * @param array a
	 * @return substituted array
	 */
	public int[] IP_reverse(int[] a) {        
		int[] temp = new int[64];
		for(int i=0;i<a.length;i++) {
			temp[i] = a[IP_1[i]-1];
		}
		
		return temp;
	}
	
	
	/**
	 * 完成加密或解密功能(取决于flag的值)
	 * @param key(密钥字符串)
	 * @param original string
	 * @param flag(determine whether encrypt or decrypt)
	 * @param File_flag(决定是否对文件加解密，1表示对文件加解密)
	 * @return ciphertext or plaintext
	 */
	public String DES(String str, int flag, String strkey, int File_flag) {    
		//System.out.println(str);
		
		String subStr = new String();
		StringBuffer sBuffer = new StringBuffer();
		String string = new String();
		int[] temp = new int[64];
		int[] temp2 = new int[32];
		int[] L = new int[32];
		int[] R = new int[32];
		            
		int[][] key = new Key_xyc().generateKeys(strkey);    // 生成子密钥
		
	
		str = fill_Message(str,File_flag);       //字符串转换成bit流
		
		System.out.println("加解密开始前bit流"+str);
		
		for(int i=0;i<str.length()/64;i++) {   //每64bit一组
			subStr = str.substring(0+64*i, 64+64*i);
			temp = binStrToIntArray(subStr);
			temp = IP_change(temp);
			System.arraycopy(temp, 0, L, 0, 32);
			System.arraycopy(temp, 32, R, 0, 32);
			
			if(flag==0) {             //if flag=0; then encrypt	
				for(int j=0;j<15;j++) {
					temp2 = new Rotate_xyc().F_function(R, key[j]);
					temp2 =  new Rotate_xyc().xor(temp2, L);
					System.arraycopy(R, 0, L, 0, 32);
					System.arraycopy(temp2, 0, R, 0, 32);
				}
				
				temp2 = new Rotate_xyc().F_function(R, key[15]);
				L = new Rotate_xyc().xor(temp2, L);
			}
			else {                   //else decrypt
				for(int j=15;j>0;j--) {                     //密钥逆序使用
					temp2 = new Rotate_xyc().F_function(R, key[j]);
					temp2 =  new Rotate_xyc().xor(temp2, L);
					System.arraycopy(R, 0, L, 0, 32);
					System.arraycopy(temp2, 0, R, 0, 32);
				}
				
				temp2 = new Rotate_xyc().F_function(R, key[0]);
				L = new Rotate_xyc().xor(temp2, L);
			}
			
		
			
			System.arraycopy(L, 0, temp, 0, 32);
			System.arraycopy(R, 0, temp, 32, 32);
			
			temp = IP_reverse(temp);
			string = intArraytoString(temp);
			sBuffer.append(string);
		}
		
		System.out.println("加解密开始后bit流"+sBuffer.toString());
		
		if(File_flag==0) {
			string = binStrToString(sBuffer.toString());
			return string.trim();
		}else {
			return sBuffer.toString();
		}
		 
	}
	
	
	//主函数用于测试各个函数的功能
	public static void main(String args[]) {
		String str = "倱춗ﷆ䀆ࣵ郎?顶뙜輖羚︠꜏〦忆ꯡㄅ왆ꑑ잒䧎輍동휘⤔̐宒வ緢?౗坶⠆ᬼ✠둺蒗ᠦ欨說츥ⷕ쨡ᅕ㊽⓽᾿鎹χ腪ㇸ蘦ἔ鋔䙀ʽ랙";
		Core_xyc test = new Core_xyc(); 
		//str = test.DES(str, 1, "qwertyuiop");
		System.out.println(str);
		
		//System.out.println(str.length());
		//str = test.fill_Message(str);
		//System.out.println(str);
		
		//String string = new String("0000000000000000");
		//string = test.binStrToString(string);
		//System.out.println(string);
		
		char c = test.binStrToChar("0000000000010000");
		System.out.println(c);
		//System.out.println('\u0512');
	}
}
