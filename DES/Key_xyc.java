package DES;

public class Key_xyc {
	
	private int[] PC1={
			57,49,41,33,25,17,9,
            1,58,50,42,34,26,18,
            10,2,59,51,43,35,27,
            19,11,3,60,52,44,36,
            63,55,47,39,31,23,15,
            7,62,54,46,38,30,22,
            14,6,61,53,45,37,29,
            21,13,5,28,20,12,4};

	private int[] PC2={
			14,17,11,24,1,5,3,28,
            15,6,21,10,23,19,12,4,
            26,8,16,7,27,20,13,2,
            41,52,31,37,47,55,30,40,
            51,45,33,48,44,49,39,56,
            34,53,46,42,50,36,29,32};

	private int[] LFT={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
	
	/**
	 * 对密钥进行处理
	 * @param Original key string
	 * @return 64-bits key string
	 */
	public String key_Handle(String str) {       //handle the key
		char[] chars = str.toCharArray();
		String string = new String();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<chars.length;i++) {
			string = Integer.toBinaryString(chars[i]);
			sb.append(string);
		}
		if(sb.length()<56) {                      //如果密钥不够56bits进行填充
			do {
				sb.append('0'); 
			} while (sb.length()<56);
		}
		str = sb.toString();
		str = str.substring(0, 56);               //截取前56bits
		
		return add_Parity(str);                   //返回添加奇偶校验位后的64bits密钥
	}
	
	
	/**
	 * 对密钥添加奇偶校验位
	 * @param 56bits str
	 * @return 64bits key
	 */
	public String add_Parity(String str) {         
		
		String subStr = new String();
		int count = 0;
		StringBuffer sBuffer = new StringBuffer();
		
		for(int i=0;i<8;i++) {
			subStr = str.substring(0+7*i, 7+7*i);
			count = count(subStr);
			if (count%2==0) {                    //若是奇数个1则添加'0',否则添加'1'
  				subStr = subStr + '1';
			}else {
				subStr = subStr + '0';
			}
			sBuffer.append(subStr);
		}
		 
		return sBuffer.toString();
	}
	
	
	/**
	 * 计算一段字符串中有多少个'1'
	 * @param str
	 * @return
	 */
	public int count(String str) {                
		
		int count[] = new Core_xyc().binStrToIntArray(str);
		int flag = 0;
		
		for(int i=0;i<count.length;i++) {
			if (count[i]==1) {
				flag++;
			}
		}
		
		return flag;
	}
	
	
	/**
	 * 密钥的PC_1置换
	 * @param str
	 * @return   56bits array
	 */
	public int[] PC_1(String str) {           
		int a[] = new Core_xyc().binStrToIntArray(str);
		int[] b = new int[56];
		for(int i=0;i<PC1.length;i++) {
			b[i] = a[PC1[i]-1];
		}
		
		return b;
	}
	
	
	/**
	 * 循环左移
	 * @param a
	 * @param i(左移数量)
	 * @return
	 */
	public int[] rotate_left(int a[],int i) {   
	
		if (i==1) {
			int temp = a[0];
			for(int j=1;j<=27;j++) {
				a[j-1] = a[j];
			}
			a[27] = temp;
		}else {
			int temp1 = a[0];
			int temp2 = a[1];
			for(int j=2;j<=27;j++) {
				a[j-2] = a[j];
			}
			a[26] = temp1;
			a[27] = temp2;
		}
		
		return a;
	}
	
	
	/**
	 * 生成16轮48bits的密钥
	 * @param str
	 * @return
	 */
	public int[][] generateKeys(String str){       
		int[][] subKey =new int[16][48];
		str = key_Handle(str);            //对密钥进行处理，返回64bits密钥
		int a[] = PC_1(str);              //对密钥进行初始的PC_1置换
		int[] C = new int[28];
		int[] D = new int[28];
		int[] merge = new int[56];
		
		System.arraycopy(a, 0, C, 0, 28);          //将56bits分为两部分
		System.arraycopy(a, 28, D, 0, 28);
		
		for(int i=0;i<16;i++) {
			C = rotate_left(C, LFT[i]);
			D = rotate_left(D, LFT[i]);
			System.arraycopy(C, 0, merge, 0, 28);
			System.arraycopy(D, 0, merge, 28, 28);
			for(int j=0;j<PC2.length;j++) {             //PC-2置换，返回48bits数组
				subKey[i][j] = merge[PC2[j]-1];
			}
		}
		
		return subKey;
	}
	/*
	public static void main(String args[]) {
		String string = new String();
		//int[][] test = new int[16][48];
		//test = new Key_xyc().generateKeys(string);
		
		for(int i=0;i<16;i++) {
			for(int j=0;j<48;j++)
				System.out.print(test[i][j]);
			System.out.print("\n");
		}
		
		string = Integer.toBinaryString('��');
		System.out.println(string);
	}*/
}
