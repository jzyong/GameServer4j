package org.jzy.game.gate.struct;



import com.jzy.javalib.base.util.MathUtil;

import java.util.Arrays;

/**
 * 消息加解密
 */
public class RC4 {

	private byte[] S = new byte[256];

	public RC4(byte[] key) {
		int keylen = key.length;
		byte[] T = new byte[256];
		for (int j = 0; j < 256; j++) {
			S[j] = (byte)j;
			T[j] = key[j % keylen];
		}

		int j = 0;
		for (int i = 0; i < 256; i++) {
			j = ((j + S[i] + T[i]) % 256) & 0xFF;
			byte t = S[i];
			S[i] = S[j];
			S[j] = t;
		}
	}

	/**
	 * 随机密钥
	 * @return
	 */
	public static byte[] getRandomKey(){
		int len = 10 + MathUtil.random(21);
	//	int len=4;
		byte[] key = new byte[len];
		for(int i =0; i < len ; i ++ ){
			key[i] = (byte)MathUtil.random(255);
		}
		return key;
	}

	public void crypt(byte[] pt, int start, int end) {
		if(pt.length<1){
			return;
		}
		byte[] s = Arrays.copyOf(S,S.length);
		if(end < 0){
			end = pt.length;
		}
		int i = 0, j = 0;
		for (int n = start; n < end; n++) {
			i = ((i + 1) % 256) & 0xFF;
			j = ((j + s[i]) % 256) & 0xFF;
			byte temp = s[i];
			s[i] = s[j];
			s[j] = temp;
			int t = ((s[i] + s[j]) % 256) & 0xFF;
			byte k = s[t];
			pt[n] = (byte) (k ^ pt[n]);
		}
	}


	public static void main(String[] args) throws Exception{
		byte[] randomKey = RC4.getRandomKey();
		RC4 rc4=new RC4(randomKey);
		long start=System.currentTimeMillis();
		byte[] bytes = "gswdsfsdf".getBytes("UTF-8");
		for(int i=0;i<1000000;i++){
			rc4.crypt(bytes,0,-1);
			rc4.crypt(bytes,0,-1);
		}
		System.out.println(String.format("时间 %dms",System.currentTimeMillis()-start));
	}
}
