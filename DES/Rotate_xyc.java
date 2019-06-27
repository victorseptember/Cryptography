package DES;

public class Rotate_xyc {
	//E盒
    private int[] E={32,1,2,3,4,5,
                     4,5,6,7,8,9,
                     8,9,10,11,12,13,
                     12,13,14,15,16,17,
                     16,17,18,19,20,21,
                     20,21,22,23,24,25,
                     24,25,26,27,28,29,
                     28,29,30,31,32,1};
    //P盒
    private int[] P={16,7,20,21,29,12,28,17,
                      1,15,23,26,5,18,31,10,
                      2,8,24,14,32,27,3,9,
                      19,13,30,6,22,11,4,25};

    private static final int[][][] S_Box = {//S-盒
            {// S_Box[1]
                    { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
            { // S_Box[2]
                    { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
            { // S_Box[3]
                    { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
            { // S_Box[4]
                    { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
            { // S_Box[5]
                    { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 }, 
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
            { // S_Box[6]
                    { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
            { // S_Box[7]
                    { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
            { // S_Box[8]
                    { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } }
    };
    
    
    /**
     * E扩展变换
     * @param a
     * @return
     */
    public int[] E_change(int[] a) {         
    	int[] temp = new int[48];
    	for(int i=0;i<E.length;i++) {
    		temp[i] = a[E[i]-1];
    	}
    	
    	return temp;
    }
    
    
    /**
     * 逐位异或运算
     * @param a
     * @param b
     * @return
     */
    public int[] xor(int[] a, int[] b) {       
    	for(int i=0;i<a.length;i++) {
    		a[i] = a[i]^b[i];
    	}
    	
    	return a;
    }
    
    
    /**
     * S盒置换
     * @param a(48bits)
     * @return 32bits array
     */
    public int[] S_change(int[] a) {          
    	int[] sub = new int[6];
    	int[] temp = new int[4];
    	int[] result = new int[32];
    	int r,l;
    	
    	for(int i=0;i<8;i++) {
    		System.arraycopy(a, 0+6*i, sub, 0, 6);
    		r = sub[5] + (sub[0]<<1);                //计算x5x0的值
    		l = (sub[1]<<3) + (sub[2]<<2) + (sub[3]<<1) + (sub[4]);   //计算x4x3x2x1的值
    		String str = Integer.toBinaryString(S_Box[i][r][l]);
    		
    		if(str.length()<4) {                  //将S盒中的十进制数转换成4bit二进制数   
    			do {
					str = '0' + str;
					
				} while (str.length()<4);
    		}
    		temp =  new Core_xyc().binStrToIntArray(str); //四位二进制字符串转成int数组
    		System.arraycopy(temp, 0, result, 0+4*i, 4);
    	}
    	
    	return result;
    }
    
    
    /**
     * P盒置换
     * @param a(int 类型数组)
     * @return
     */
    public int[] P_change(int[] a) {          
    	int[] temp = new int[32];
    	for(int i=0;i<P.length;i++) {
    		temp[i] = a[P[i]-1];
    	}
    	
    	return temp;
    }
    
    
    /**
     * 轮函数中的f函数
     * @param R
     * @param key(子密钥)
     * @return
     */
    public int[] F_function(int[] R, int[] key) {         
    	int[] a = new int[48];
    	
    	a = E_change(R);
    	a = xor(a, key);
    	R = S_change(a);
    	R = P_change(R); 
    	
    	return R;
    }
    
    
    /*
    public static void main(String args[]) { 
    	int r,l;
    	int[] sub = new int[] {1,1,1,0,1,1};
    	r = sub[5] + (sub[0]<<1);           //��һ�����λ��ʾ������
		l = (sub[1]<<3) + (sub[2]<<2) + (sub[3]<<1) + (sub[4]);
		
		//System.out.println(sub[1]<<3);
		//System.out.println(sub[2]<<2);
		//System.out.println(sub[3]<<1);
		//System.out.println(sub[4]);
		//System.out.println(r);
		//System.out.println(l);
		System.out.println(S_Box[5][r][l]);
		String str = Integer.toBinaryString(S_Box[5][r][l]);
		if(str.length()<4) {                //��str�ĳ��Ȳ���Ϊ4λ
			do {
				str = '0' + str;
			} while (str.length()<4);
		}
		System.out.println(str);
    }*/
}
