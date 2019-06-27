package sm3;

public class SM3_xyc {

	private int[] W1 = new int[68];
	private int[] W2 = new int[64];
	private int[] IV = {0x7380166f, 0x4914b2b9, 0x172442d7, 0xda8a0600, 0xa96f30bc, 0x163138aa, 0xe38dee4d, 0xb0fb0e4e

};
	
	/**
	 * 字符串转换成二进制字符串 
	 * @param  str
	 * @return binary string
	 */
	public String stringToBinary(String str) {    
		char[] chars = str.toCharArray();
		String string = new String();
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<chars.length;i++) {
			string = Integer.toBinaryString(chars[i]); 
			string = '0'+string;           //在前补零
			sb.append(string);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 对消息进行填充为512的倍数bit
	 * @param str(初始消息的bit串)
	 * @return
	 */
	public String fill(String str) {    
		String string = new String();
		int length = str.length();
		int add_length = (512 - (length+65)%512)%512;   //需要补零的个数
		
		str = str+'1';                            //先将1添加到
		string = Integer.toBinaryString(length);
		while(string.length()<64) {              //将长度表示成64bit
			string = '0'+string;
		}
		
		for(int i=0;i<add_length;i++) {
			str = str+'0';
		}

		return str+string;
	}
	
	
	/**
	 * 二进制字符串转成数组
	 * @param binStr
	 * @return int
	 */
	public int binStrToInt(String binStr) {   
		char chars[] = binStr.toCharArray();
		int[] temp = new int[binStr.length()];
		int sum = 0;
		
		for(int i=0;i<binStr.length();i++) {
			temp[i] = chars[i] - '0';
		}
		
		for(int j=0;j<temp.length;j++) {
			sum += temp[j]<<(31-j);
		}
		
		return sum;	
	}
	
	/**
	 * 把二进制字符串转换成int类型数组
	 * @param string(512bit 二进制字符串)
	 * @return
	 */
	public int[] stringToIntArray(String string) {
		int[] result = new int[16];
		String substr = new String();
		for(int i=0;i<16;i++) {
			substr = string.substring(0+i*32, 32+32*i);
			result[i] = binStrToInt(substr);
		}
		
		return result;
	}
	
	
	/**
	 * int数组转换成十六进制字符串
	 * @param a
	 * @return
	 */
	public String intArrayToHexString(int[] a) {
		StringBuilder sBuilder = new StringBuilder();
		String string = new String();
		for(int i=0;i<a.length;i++) {
			string = Integer.toHexString(a[i]);
			if(string.length()<8) {
				do {
					string = '0' + string;  //若不足8位，则在前面补零
				} while (string.length()<8);
			}
			//string = string + ' ';   //每8位后面添加空格
			sBuilder.append(string);
		}
		
		return sBuilder.toString();
	}
	
	public int T_func(int j) {
		
		if(j>=0&&j<=15) {
			return 0x79cc4519;
		}
		else if(j>=16&&j<=63) {
			return 0x7a879d8a;
		}else {
			System.out.println("Wrong param j in T_func");
			return 0;
		}
	}
	
	public int FF_func(int x,int y,int z,int j) {
		
		if(j>=0&&j<=15) {
			return x^y^z;
		}else if(j>=16&&j<=63) {
			return (x&y)|(x&z)|(y&z);
		}else {
			System.out.println("Wrong param j in FF_func");
			return 0;
		}
	}
	
	public int GG_func(int x,int y,int z,int j) {
		
		if(j>=0&&j<=15) {
			return x^y^z;
		}else if(j>=16&&j<=63) {
			return (x&y)|(~x&z);
		}else {
			System.out.println("Wrong param j in GG_func");
			return 0;
		}
	}
	
	public int p0_func(int x) {

		return x ^ loopLeftShift(x, 9) ^ loopLeftShift(x, 17);
	}
    
	public int p1_func(int x) {

		return x ^ loopLeftShift(x, 15) ^ loopLeftShift(x, 23);
	}
	
	
	/**
	 * 消息扩展
	 * @param result
	 */
	public void messageExpand(int[] result) {
		System.arraycopy(result, 0, W1, 0, 16);
		
		for(int j=16;j<68;j++) {
			int temp = W1[j-16]^W1[j-9]^loopLeftShift(W1[j-3], 15);
			W1[j] = p1_func(temp)^loopLeftShift(W1[j-13], 7)^W1[j-6];
		}
		
		for(int j=0;j<64;j++) {
			W2[j] = W1[j]^W1[j+4];
		}
		
	
	}
	
	
	/**
	 * 压缩函数
	 * @param V
	 * @param block(明文分组)
	 * @return
	 */
	public int[] CF_func(int[] V,String block) {
		int A,B,C,D,E,F,G,H,SS1,SS2,TT1,TT2;
		int[] result = stringToIntArray(block);     //将明文分割成16个字
		
		messageExpand(result);      //消息扩展
		A = V[0];
		B = V[1];
		C = V[2];
		D = V[3];
		E = V[4];
		F = V[5];
		G = V[6];
		H = V[7];
		
		for(int j=0;j<64;j++) {
			int temp = loopLeftShift(A, 12) + E + loopLeftShift(T_func(j), j);
			SS1 = loopLeftShift(temp, 7);
			SS2 = SS1^loopLeftShift(A, 12);
			TT1 = FF_func(A, B, C, j) + D + SS2 + W2[j];
			TT2 = GG_func(E, F, G, j) + H + SS1 + W1[j];
			D = C;
			C = loopLeftShift(B, 9);
			B = A;
			A = TT1;
			H = G;
			G = loopLeftShift(F, 19);
			F = E;
			E = p0_func(TT2);

		}
		
		V[0] = A^V[0];
		V[1] = B^V[1];
		V[2] = C^V[2];
		V[3] = D^V[3];
		V[4] = E^V[4];
		V[5] = F^V[5];
		V[6] = G^V[6];
		V[7] = H^V[7];
		
		return V;
	}
	
	/**
	 * 对明文分组
	 * @param string(原始字符串)
	 * @param flag(0表示对字符串分组，1表示对文件)
	 * @return
	 */
	public String[] group(String string, int flag) {
		if(flag==0) {
			string = stringToBinary(string);   //字符串转换成二进制字符串
			string = fill(string);
		}
		String[] substr = new String[string.length()/512];
		
		for(int i=0;i<substr.length;i++) {
			substr[i] = string.substring(0+512*i, 512+512*i);
		}
		
		return substr;
	}
	
	/**
	 * 迭代函数
	 * @param string(初始字符串)
	 * @param flag（0表示对字符串，1表示对文件）
	 * @return
	 */
	public String iteration_xyc(String string, int flag) {
		String[] str = group(string, flag);	   //
		int[] V = new int[8];
		System.arraycopy(IV, 0, V, 0, 8);    //先把IV数组复制到V数组
		
		
		for(int i=0;i<str.length;i++) {
			V = CF_func(V, str[i]);
		}

		String result = intArrayToHexString(V);
		return result;
	}
	
    /**
     * 循环左移k bit运算
     * @param a
     * @param k
     * @return
     */
    public int loopLeftShift(int a, int k) {
    	
    	return (a << k) | (a >>> (32 - k));   //右移时要无符号移位，否则负数的移位会在高位补‘1’
    }
    
    
	public static void main(String[] args) {
		SM3_xyc test = new SM3_xyc();
		String string = new String("abcd");
		String a = test.iteration_xyc(string, 0);
		//String ss = test.intArrayToHexString(a);
		System.out.println(a);
		//String string = test.stringToBinary("abc");
		///string = test.fill(string);
		//System.out.println(string);
//		int[] temp = test.stringToIntArray(string);
//		for(int i=0;i<temp.length;i++) {
//			System.out.println(temp[i]);
//		}
		//String string = new String("abv");
		//string = Integer.toHexString();
		//byte[] bts = string.getBytes();
		
	}
}
