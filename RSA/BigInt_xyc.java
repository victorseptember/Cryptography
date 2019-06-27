package RSA;

import java.util.Arrays;
import java.util.Random;

public class BigInt_xyc {

    public static final int SIZE = 32;

    /**
     * 大数加法运算
     * @param a
     * @param b
     * @return  返回与参数同长的数组
     */
    short[] add(short[] a, short[] b) {
        int length = a.length;
        short[] c = new short[length];
        short temp;                    //存储临时和
        short carry = 0;                    //存储进位

        for(int i = 0; i< length; i++){
            temp = (short) (a[i] + b[i] + carry);
            c[i] = (short)(temp & 0x00ff);   //?
            carry = (short)((temp>>>8)&0x01);    //byte 移位（会先将byte类型转换成int）
        }

        return c;
    }

    /**
     * 比较函数 a>b, a=b, a<b分别返回1,0,-1
     * @param a
     * @param b
     * @return
     */
    public int compare(short[] a, short[] b) {
//        System.out.println("a:"+a.length);
//        System.out.println("b:"+b.length);
        int length_a = Length(a);
        int length_b = Length(b);

        if(length_a>length_b)
            return 1;
        else if(length_a<length_b)
            return -1;
        else {
            int i = length_a-1;
            do {
                if (a[i] > b[i])
                    return 1;
                if (a[i] < b[i])
                    return -1;
                i--;
            } while (i >= 0);
        }

        return 0;
    }

    /**
     * 大数减法运算(a - b)
     * @param a
     * @param b
     * @param size (决定多少bit的减法操作)
     * @return
     */

    public short[] sub(short[] a, short[] b, int size) {
        if(compare(a,b) == -1) {              //如果a<b则报错并返回
            System.out.println("ERROR!(In the sub(), a<b)");
            return null;
        }

        short[] c = new short[size];
        short carry = 0;         //存储借位值
        short temp;

        for(int i = 0; i< size; i++) {
            temp = (short)(a[i] - b[i] - carry);
            c[i] = (short) (temp & 0x00ff);          //?//temp为负数的话相当于加256，正数则不改变其值
            carry = (short) ((temp>>>15) & 0x01);
        }

        return c;
    }

    /**
     * 大数乘法运算
     * @param a
     * @param b
     * @return
     */
    public short[] multi(short[] a, short[] b) {

        int length = a.length;

        short[] c = new short[2* length];
        short temp,carry;

        for(int i = 0; i< length; i++) {

            carry = 0;
            for (int j = 0; j< length; j++) {
                temp = (short) (a[i]*b[j] + carry + c[i+j]);
                c[i+j] = (short) (temp & 0xff);
                carry = (short) ((temp>>>8)&0xff);
            }

            c[i+ length] = carry;  //?
        }

        return c;
    }


    /**
     * 大数除法运算(a/b)
     * @param a
     * @param b
     * @return
     */
    public short[] divide(short[] a,short[] b) {    //需要优化
        short[] temp;
        int array_length = a.length;                //确定操作数的长度
        short[] c = new short[array_length];


        int length = Length(a) - Length(b);
        while(length>=0) {

            temp = leftShift(b,length,array_length);   //通过移位将a，b对齐后再减，可提高速度
            while(compare(a,temp)>=0){
                a = sub(a,temp,array_length);
                c[length]++;
            }
            length--;
        }

        return c;
    }

    /**
     * 计算数组的有效长度
     * @param a
     * @return
     */
    public int Length(short[] a) {
//        System.out.println(a.length);
        int i = a.length;       //bug!!! int i = SIZE
        while(i > 0 && a[i-1] == 0){    //bug  没判断i>0
            i--;
        }

        return i;
    }


    /**
     * 数组a左移
     * @param a
     * @param b
     * @param size
     * @return
     */
    public short[] leftShift(short[] a,int b,int size) {
        short[] temp = new short[size];
        int length = Length(a);

        System.arraycopy(a,0,temp,b,length);

        return temp;
    }




    /**
     * 大整数的求模(a mod b)
     * @param a
     * @param b
     * @param size
     * @return
     */
    public short[] mod(short[] a,short[] b,int size) {   //?size
        if(compare(a,b)<0)
            return a;
        else{
            short[] temp;
            int length = Length(a) - Length(b);

            while(length>=0) {

                temp = leftShift(b,length,size);
                while(compare(a,temp)>=0){
                    a = sub(a,temp,size);
                }

                //System.out.println(a.toString());
                length--;
            }

            return a;
        }

    }


    /**
     * 大整数模逆运算
     * @param e  (2*SIZE)
     * @param N  (2*SIZE)
     * @return
     */
    public short[] mod_inverse(short[] e, short[] N){   //bug死循环乘法长度不匹配
        int length = e.length;

        short[] r1 = new short[length];
        short[] r2 = new short[length];

        System.arraycopy(e,0,r1,0,length);
        System.arraycopy(N,0,r2,0,length);
        short[] s1 = new short[length];
        s1[0] = 1;
        short[] s2 = new short[length];
        short[] s ;
        short[] r ;

        while(true){
            if (Length(r1)==0){
                System.out.println("取逆失败");
                return null;
            }
            if(Length(r1)==1 && r1[0]==1){
                return s1;
            }

            short[] q = divide(r1,r2);

            short[] temp = mulMod(q,s2,N);

            s = subMod2(s1,temp,N);      //保证s为正数

            short[] temp2 = Arrays.copyOf(multi(q,r2),length);

            r = sub(r1,temp2,length);

            r1 = Arrays.copyOf(r2,length);
            s1 = Arrays.copyOf(s2,length);
            s2 = Arrays.copyOf(s,length);
            r2 = Arrays.copyOf(r,length);
        }

    }

    public short[] subMod2(short[] a,short[] b, short[] n) {
        while(compare(a,b)<0) {        //进行判断，可能需要多次加法
            a = add(a,n);
        }

        return sub(a,b,a.length);
    }

    /**
     * a*b(mod c)
     * @param a
     * @param b
     * @param c
     * @return
     */
    public short[] mulMod(short[] a,short[] b,short[] c) {
        short[] result;
        short[] temp = multi(a,b);

        result = mod(temp,c,temp.length);      //此时result数组与temp同长，需要截取

        result = Arrays.copyOf(result,c.length);
        /*
        System.out.println("截取后result数组：");
        for (int i=0;i<result.length;i++)  {
            System.out.println(result[i]);
        }*/
//        System.out.println("effective length"+Length(result));
//        System.out.println("length:" + result.length);
        return result;   //需要截取
    }



    /**
     * a-b(mod c)
     * @param a
     * @param b
     * @param c
     * @return
     */
    public short[] subMod(short[] a,short[] b,short[] c) {
        int length = a.length;
        short[] result = sub(a,b,length);
        return mod(result,c,length);
    }



    /**
     * 利用模重复平方法计算a^b mod n
     * @param a
     * @param b
     * @param n
     * @return
     */
    public short[] powMod(short[] a, short[] b, short[] n) {  //bug计算结果全为0

        int length = a.length;

        short[] c = new short[length];
        c[0] = 1;
        short[] temp = new short[length];
        temp[0] = 1;
        short[] divisor = new short[length];
        divisor[0] = 2;

        while(Length(b) > 0) {
            while((b[0] & 0x01) == 0) {   //若b为偶数
                b = divide(b,divisor);
                a = mulMod(a,a,n);
            }
            b = sub(b,temp,length);

            c = mulMod(a,c,n);
        }

        return c;
     }



    /**
     * 生成一个 8*a bit的随机数
     * @param a
     * @return
     */
    public short[] genRand(int a) {
        short[] rand = new short[a];
        Random random = new Random();
        for(int i = 0;i<a-1;i++) {
            rand[i] = (short) random.nextInt(256);
        }
        rand[a-1] = (short) (128 + random.nextInt(128));

        return rand;
    }

    /**
     * 生成8 * a bit的随机奇数
     * @param a
     * @return
     */
    public short[] genRandOdd(int a) {
        short[] rand = new short[a];
        Random random = new Random();
        for(int i = 0;i<a-1;i++) {
            rand[i] = (short) random.nextInt(256);
            //if((rand[i] & 0x01) == 0)
                //rand[i] = (short)(rand[i] + 1);

        }
        rand[a-1] = (short) (128 + random.nextInt(128));

        if((rand[0] & 0x01) == 0)
            rand[0] = (short)(rand[0] + 1);

        return rand;

    }

    /**
     * 生成一个0~a之间的随机数
     * @param a
     * @return
     */
    public short[] bigRand(short[] a) {

        int length = a.length;

        short[] temp = new short[length];
        Random random = new Random();
        for (int i=0;i<length;i++) {
            temp[i] = (short)(random.nextInt(256));
        }
        temp = mod(temp,a,length);

        return temp;
    }

    /**
     * 单次素性检测
     * @param n
     * @return
     */
    boolean MillerRabin_one(short[] n) {

        int length = n.length;

        short[] a,m,v,temp;
        short[] j = new short[length];
        short[] one = new short[length];
        one[0] = 1;
        short[] two = new short[length];
        two[0] = 2;
        short[] three = new short[length];
        three[0] = 3;

        m = sub(n,one,length);

        while ((m[0] & 0x01)==0) {   //计算n-1=(2^j)*m
            j = add(j,one);
            m = divide(m,two);
        }

        a = add(two, bigRand(sub(n,three,length)));   //随机选取a属于[2,n-1]

        v = powMod(a,m,n);

        if(compare(v,one)==0)    //若(a^m)mod n = 1,则n可能为素数
            return true;

        short[] i = new short[length];
        i[0] = 1;
        temp = sub(n,one,length);   //-1

        while (compare(v,temp)!=0) {
            if (compare(i,j)==0) {
                return false;
            }
            v = mulMod(v,v,n);
            i = add(i,one);
            System.out.println("检测中i"+i[0]);
        }

        return true;

    }

    boolean MillerRabin(short[] n,int loop) {
        for(int i=0;i<loop;i++) {
            if(MillerRabin_one(n)==false) {
                System.out.println("n为合数");
                return false;
            }
            System.out.println("pass one detection");
        }

        return true;
    }

    /**
     * 生成8*a bit的素数
     * @param a
     * @return
     */
    public short[] genPrime(int a) {
        short[] temp = genRandOdd(a);
        int loop = 5;
        while(!MillerRabin(temp,loop)) {
            temp = genRandOdd(a);
            System.out.println("Generate prime failed,restart");
        }
        return temp;
    }

    /**
     * 求解最大公因数
     * @param a
     * @param b
     * @return
     */
    public short[] gcd(short[] a,short[] b) {
        short[] c;
        int length = a.length;

        while(Length(a)>0) {
            c = mod(b,a,length);

            //System.arraycopy(a,0,b,0,length);//bug
            b = Arrays.copyOf(a,length);

            //System.arraycopy(c,0,a,0,length);
            a = Arrays.copyOf(c,length);

        }

        return b;
   }

    /**
     * 生成公钥，与N的欧拉值互素
     * @param phiN   N的欧拉值
     * @return
     */
   public short[] genE(short[] phiN) {
        short[] e = bigRand(phiN);
        short[] g = gcd(phiN,e);
        while (Length(g)!= 1 || g[0] != 1) {
            e = bigRand(phiN);
            g = gcd(phiN,e);
        }

        return e;
   }

    /**
     *计算私钥
     * @param e  公钥
     * @param phiN  欧拉值
     * @return  私钥
     */
    public short[] genD(short[] e, short[] phiN) {
       short[] g = gcd(e,phiN);
       if (Length(g)!=1 || g[0]!=1) {
           System.out.println("公钥与N的欧拉值不互素，取逆失败");
           return null;
       }

       short[] temp = mod_inverse(e,phiN);

       return temp;

    }

    /**
     * 将字符串转换成数组
     * @param string
     * @return
     */
    public short[] getMessage(String string) {
        char[] chars = string.toCharArray();
        short[] message = new short[chars.length];

//        System.out.println("message length"+message.length);
        for (int i=0;i<chars.length;i++) {
            message[i] = (short) chars[i];
        }

          return message;
    }

    public short[] encrypt(short[] m,short[] e,short[] N) {

        int length = N.length;
        short[] temp = new short[length];
        System.arraycopy(m,0,temp,0,m.length);

        return powMod(temp,e,N);

    }

    public short[] decrypt(short[] c, short[] d, short[] N) {

        return powMod(c,d,N);
    }

    /**
     * 将数组转换成字符串
     * @param a
     * @return
     */
    public String getString(short[] a) {
        StringBuilder sbuilder = new StringBuilder();
        for (int i=0;i<Length(a);i++) {
            char c = (char)a[i];
            sbuilder.append(c);
        }

        return sbuilder.toString();
    }

    /**
     * 将数组存储的大数转换成十六进制
     * @param a
     * @return
     */
    public String output_hex(short[] a) {
        int length = Length(a);
        System.out.println(length);
        StringBuilder sbuilder = new StringBuilder();

        for (int i=length-1;i>=0;i--) {
            String str = Integer.toHexString(a[i]);
            if (str.length()<2) {
                do {
                    str = '0'+str;
                }while(str.length()<2);
            }
            sbuilder.append(str);

        }

        return sbuilder.toString();
    }


    public static void main(String[] args) {
        BigInt_xyc test = new BigInt_xyc();

        short[] p = test.genPrime(SIZE);
        short[] q = test.genPrime(SIZE);

        short[] a = test.gcd(p,q);
        for (int i=0;i<a.length;i++) {
            System.out.println(a[i]);
        }

        /*
        short[] N = test.multi(p,q);
        p[0] = (short) (p[0] - 1);
        q[0] = (short) (q[0] - 1);
        short[] phiN = test.multi(p,q);

        short[] e = test.genE(phiN);  //公钥

        System.out.println(e.length);
        System.out.println(phiN.length);

        System.out.println("e:");
        for (int i=0;i<e.length;i++) {
            System.out.println(e[i]);
        }

        System.out.println("phiN:");
        for (int i=0;i<phiN.length;i++) {
            System.out.println(phiN[i]);
        }

        short[] d = test.genD(e, phiN);   //私钥

        String str = "%&*#&(*#)(=-234dy";
        short[] temp = test.getMessage(str);

        System.out.println("temp数组长度："+temp.length);
        for (int i=0;i<temp.length;i++) {
            System.out.println(temp[i]);
        }

        short[] c = test.encrypt(temp,e,N);
        System.out.println("c数组长度：");
        for (int i=0;i<c.length;i++) {
            System.out.println(c[i]);
        }
        System.out.println("密文："+test.getString(c));

        short[] m = test.decrypt(c,d,N);
        System.out.println("明文："+test.getString(m));
        System.out.println("m数组长度：");
        for (int i=0;i<m.length;i++) {
            System.out.println(m[i]);
        }*/

    }

}
