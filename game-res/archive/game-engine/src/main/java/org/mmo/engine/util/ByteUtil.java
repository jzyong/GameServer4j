package org.mmo.engine.util;
import java.nio.charset.Charset;

/**
 * 字节数组转换工具类
 */
public class ByteUtil {

    public static final String GBK = "GBK";
    public static final String UTF8 = "utf-8";
    public static final char[] ascii = "0123456789ABCDEF".toCharArray();
    private static char[] HEX_VOCABLE = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * 将short整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((data & 0xff00) >> 8);
        bytes[1] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将字符转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data >> 8);
        bytes[1] = (byte) (data);
        return bytes;
    }

    /**
     * 将布尔值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(boolean data) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (data ? 1 : 0);
        return bytes;
    }

    /**
     * 将整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((data & 0xff000000) >> 24);
        bytes[1] = (byte) ((data & 0xff0000) >> 16);
        bytes[2] = (byte) ((data & 0xff00) >> 8);
        bytes[3] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将long整型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((data >> 56) & 0xff);
        bytes[1] = (byte) ((data >> 48) & 0xff);
        bytes[2] = (byte) ((data >> 40) & 0xff);
        bytes[3] = (byte) ((data >> 32) & 0xff);
        bytes[4] = (byte) ((data >> 24) & 0xff);
        bytes[5] = (byte) ((data >> 16) & 0xff);
        bytes[6] = (byte) ((data >> 8) & 0xff);
        bytes[7] = (byte) (data & 0xff);
        return bytes;
    }

    /**
     * 将float型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    /**
     * 将double型数值转换为字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    /**
     * 将字符串按照charsetName编码格式的字节数组
     * 
     * @param data
     *            字符串
     * @param charsetName
     *            编码格式
     * @return
     */
    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    /**
     * 将字符串按照GBK编码格式的字节数组
     * 
     * @param data
     * @return
     */
    public static byte[] getBytes(String data) {
        return getBytes(data, GBK);
    }

    /**
     * 将字节数组第0字节转换为布尔值
     * 
     * @param bytes
     * @return
     */
    public static boolean getBoolean(byte[] bytes) {
        return bytes[0] == 1;
    }

    /**
     * 将字节数组的第index字节转换为布尔值
     * 
     * @param bytes
     * @param index
     * @return
     */
    public static boolean getBoolean(byte[] bytes, int index) {
        return bytes[index] == 1;
    }

    /**
     * 将字节数组前2字节转换为short整型数值
     * 
     * @param bytes
     * @return
     */
    public static short getShort(byte[] bytes) {
        return (short) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
    }

    /**
     * 将字节数组从startIndex开始的2个字节转换为short整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static short getShort(byte[] bytes, int startIndex) {
        return (short) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
    }

    /**
     * 将字节数组前2字节转换为字符
     * 
     * @param bytes
     * @return
     */
    public static char getChar(byte[] bytes) {
        return (char) ((0xff00 & (bytes[0] << 8)) | (0xff & bytes[1]));
    }

    /**
     * 将字节数组从startIndex开始的2个字节转换为字符
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static char getChar(byte[] bytes, int startIndex) {
        return (char) ((0xff00 & (bytes[startIndex] << 8)) | (0xff & bytes[startIndex + 1]));
    }

    /**
     * 将字节数组前4字节转换为整型数值
     * 
     * @param bytes
     * @return
     */
    public static int getInt(byte[] bytes) {
        return (0xff000000 & (bytes[0] << 24) | (0xff0000 & (bytes[1] << 16))
                | (0xff00 & (bytes[2] << 8)) | (0xff & bytes[3]));
    }

    /**
     * 将字节数组从startIndex开始的4个字节转换为整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static int getInt(byte[] bytes, int startIndex) {
        return (0xff000000 & (bytes[startIndex] << 24)
                | (0xff0000 & (bytes[startIndex + 1] << 16))
                | (0xff00 & (bytes[startIndex + 2] << 8)) | (0xff & bytes[startIndex + 3]));
    }

    /**
     * 将字节数组前8字节转换为long整型数值
     * 
     * @param bytes
     * @return
     */
    public static long getLong(byte[] bytes) {
        return (0xff00000000000000L & ((long) bytes[0] << 56)
                | (0xff000000000000L & ((long) bytes[1] << 48))
                | (0xff0000000000L & ((long) bytes[2] << 40))
                | (0xff00000000L & ((long) bytes[3] << 32))
                | (0xff000000L & ((long) bytes[4] << 24))
                | (0xff0000L & ((long) bytes[5] << 16))
                | (0xff00L & ((long) bytes[6] << 8)) | (0xffL & (long) bytes[7]));
    }

    /**
     * 将字节数组从startIndex开始的8个字节转换为long整型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static long getLong(byte[] bytes, int startIndex) {
        return (0xff00000000000000L & ((long) bytes[startIndex] << 56)
                | (0xff000000000000L & ((long) bytes[startIndex + 1] << 48))
                | (0xff0000000000L & ((long) bytes[startIndex + 2] << 40))
                | (0xff00000000L & ((long) bytes[startIndex + 3] << 32))
                | (0xff000000L & ((long) bytes[startIndex + 4] << 24))
                | (0xff0000L & ((long) bytes[startIndex + 5] << 16))
                | (0xff00L & ((long) bytes[startIndex + 6] << 8)) | (0xffL & (long) bytes[startIndex + 7]));
    }

    /**
     * 将字节数组前4字节转换为float型数值
     * 
     * @param bytes
     * @return
     */
    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    /**
     * 将字节数组从startIndex开始的4个字节转换为float型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static float getFloat(byte[] bytes, int startIndex) {
        byte[] result = new byte[4];
        System.arraycopy(bytes, startIndex, result, 0, 4);
        return Float.intBitsToFloat(getInt(result));
    }

    /**
     * 将字节数组前8字节转换为double型数值
     * 
     * @param bytes
     * @return
     */
    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将字节数组从startIndex开始的8个字节转换为double型数值
     * 
     * @param bytes
     * @param startIndex
     * @return
     */
    public static double getDouble(byte[] bytes, int startIndex) {
        byte[] result = new byte[8];
        System.arraycopy(bytes, startIndex, result, 0, 8);
        long l = getLong(result);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将charsetName编码格式的字节数组转换为字符串
     * 
     * @param bytes
     * @param charsetName
     * @return
     */
    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    /**
     * 将GBK编码格式的字节数组转换为字符串
     * 
     * @param bytes
     * @return
     */
    public static String getString(byte[] bytes) {
        return getString(bytes, GBK);
    }

    /**
     * 将16进制字符串转换为字节数组
     * 
     * @param hex
     * @return
     */
    public static byte[] hexStringToBytes(String hex) {
        if (hex == null || "".equals(hex)) {
            return null;
        }
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] chArr = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(chArr[pos]) << 4 | toByte(chArr[pos + 1]));
        }
        return result;
    }
    


    /**
     * 将BCD编码的字节数组转换为字符串
     * 
     * @param bcds
     * @return
     */
    public static String bcdToString(byte[] bcds) {
        if (bcds == null || bcds.length == 0) {
            return null;
        }
        byte[] temp = new byte[2 * bcds.length];
        for (int i = 0; i < bcds.length; i++) {
            temp[i * 2] = (byte) ((bcds[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bcds[i] & 0x0f);
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString();
    }

    /**
     * 字节转整形
     * @param value
     * @return
     */
    public static int bcdToInt(byte value){
        return ((value>>4) * 10) + (value&0x0F);
    }
    
    /**
     * 字节数组转16进制字符串
     * @param bs
     * @return
     */
    public static String bytesToHex(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bs) {
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    
    /**
     * 字节数组取前len个字节转16进制字符串
     * @param bs
     * @param len
     * @return
     */
    public static String bytesToHex(byte[] bs, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++ ) {
            byte b = bs[i];
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    /**
     * 字节数组偏移offset长度之后的取len个字节转16进制字符串
     * @param bs
     * @param offset
     * @param len
     * @return
     */
    public static String bytesToHex(byte[] bs, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<len; i++ ) {
            byte b = bs[offset + i];
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        }
        return sb.toString();
    }
    /**
     * 字节数组转16进制字符串
     * @return
     */
    public static String byteToHex(byte b) {
        StringBuilder sb = new StringBuilder();
            int high = (b >> 4) & 0x0f;
            int low = b & 0x0f;
            sb.append(HEX_VOCABLE[high]);
            sb.append(HEX_VOCABLE[low]);
        return sb.toString();
    }
    /**
     * 将字节数组取反
     * 
     * @param src
     * @return
     */
    public static String negate(byte[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        byte[] temp = new byte[2 * src.length];
        for (int i = 0; i < src.length; i++) {
            byte tmp = (byte) (0xFF ^ src[i]);
            temp[i * 2] = (byte) ((tmp >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (tmp & 0x0f);
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString();
    }

    /**
     * 比较字节数组是否相同
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean compareBytes(byte[] a, byte[] b) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length != b.length) {
            return false;
        }
        if (a.length == b.length) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    /**
     * 只比对指定长度byte
     * @param a
     * @param b
     * @param len
     * @return
     */
    public static boolean compareBytes(byte[] a, byte[] b, int len) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length < len || b.length < len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将字节数组转换为二进制字符串
     * 
     * @param items
     * @return
     */
    public static String bytesToBinaryString(byte[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (byte item : items) {
            buf.append(byteToBinaryString(item));
        }
        return buf.toString();
    }

    /**
     * 将字节转换为二进制字符串
     * 
     * @return
     */
    public static String byteToBinaryString(byte item) {
        byte a = item;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            buf.insert(0, a % 2);
            a = (byte) (a >> 1);
        }
        return buf.toString();
    }

    /**
     * 对数组a，b进行异或运算
     * 
     * @param a
     * @param b
     * @return
     */
    public static byte[] xor(byte[] a, byte[] b) {
        if (a == null || a.length == 0 || b == null || b.length == 0
                || a.length != b.length) {
            return null;
        }
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    /**
     * 对数组a，b进行异或运算 运算长度len
     * @param a
     * @param b
     * @param len
     * @return
     */
    public static byte[] xor(byte[] a, byte[] b, int len) {
        if (a == null || a.length == 0 || b == null || b.length == 0) {
            return null;
        }
        if (a.length < len || b.length < len){
            return null;
        }
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }
    /**
     * 将short整型数值转换为字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] shortToBytes(int num) {
        byte[] temp = new byte[2];
        for (int i = 0; i < 2; i++) {
            temp[i] = (byte) ((num >>> (8 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转为整型
     * 
     * @return
     */
    public static int bytesToShort(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 2; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将整型数值转换为指定长度的字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] intToBytes(int num) {
        byte[] temp = new byte[4];
        for (int i = 0; i < 4; i++) {
            temp[i] = (byte) ((num >>> (24 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将整型数值转换为指定长度的字节数组
     * 
     * @param src
     * @param len
     * @return
     */
    public static byte[] intToBytes(int src, int len) {
        if (len < 1 || len > 4) {
            return null;
        }
        byte[] temp = new byte[len];
        for (int i = 0; i < len; i++) {
            temp[len - 1 - i] = (byte) ((src >>> (8 * i)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转换为整型数值
     * 
     * @param arr
     * @return
     */
    public static int bytesToInt(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将long整型数值转换为字节数组
     * 
     * @param num
     * @return
     */
    public static byte[] longToBytes(long num) {
        byte[] temp = new byte[8];
        for (int i = 0; i < 8; i++) {
            temp[i] = (byte) ((num >>> (56 - i * 8)) & 0xFF);
        }
        return temp;
    }

    /**
     * 将字节数组转换为long整型数值
     * 
     * @param arr
     * @return
     */
    public static long bytesToLong(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        long result = 0;
        int len = Math.min(8, arr.length);
        for (int i = 0; i < len; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * 将16进制字符转换为字节
     * 
     * @param c
     * @return
     */
    public static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    
    /**
     * 功能描述：把两个字节的字节数组转化为整型数据，高位补零，例如：<br/>
     * 有字节数组byte[] data = new byte[]{1,2};转换后int数据的字节分布如下：<br/>
     * 00000000  00000000 00000001 00000010,函数返回258
     * @param lenData 需要进行转换的字节数组
     * @return  字节数组所表示整型值的大小
     */
    public static int bytesToIntWhereByteLengthEquals2(byte lenData[]) {
        if(lenData.length != 2){
            return -1;
        }
        byte fill[] = new byte[]{0,0};
        byte real[] = new byte[4];
        System.arraycopy(fill, 0, real, 0, 2);
        System.arraycopy(lenData, 0, real, 2, 2);
        int len = byteToInt(real);
        return len;
        
    }
    
    /**
     * 功能描述：将byte数组转化为int类型的数据
     * @param byteVal 需要转化的字节数组
     * @return 字节数组所表示的整型数据
     */
    public static int byteToInt(byte[] byteVal) {
        int result = 0;
        for(int i = 0;i < byteVal.length;i++) {
            int tmpVal = (byteVal[i]<<(8*(3-i)));
            switch(i) {
                case 0:
                    tmpVal = tmpVal & 0xFF000000;
                    break;
                case 1:
                    tmpVal = tmpVal & 0x00FF0000;
                    break;
                case 2:
                    tmpVal = tmpVal & 0x0000FF00;
                    break;
                case 3:
                    tmpVal = tmpVal & 0x000000FF;
                    break;
            }
        
            result = result | tmpVal;
        }
        return result;
    }
    public static byte CheckXORSum(byte[] bData){
        byte sum = 0x00;
        for (int i = 0; i < bData.length; i++) {
            sum ^= bData[i];
        }
        return sum;
    }
    /**
     * 从offset开始 将后续长度为len的byte字节转为int
     * @param data
     * @param offset
     * @param len
     * @return
     */
    public static int bytesToInt(byte[] data, int offset, int len){
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        len = Math.min(len, 4);
        for (int i = 0; i < len; i++) {
            result <<= 8;
            temp = data[offset + i] & mask;
            result |= temp;
        }
        return result;
    }
    
    /**
     * byte字节数组中的字符串的长度
     * @param data
     * @return 
     */
    public static int getBytesStringLen(byte[] data)
    {
        int count = 0;
        for (byte b : data) {
            if(b == 0x00)
                break;
            count++;
        }
        return count;
    }

    public static String BytesToStr(byte[] target) {
        StringBuilder buf = new StringBuilder("[");
        for (int i = 0, j = target.length; i < j; i++) {
            buf.append(target[i]).append(",");
        }
        if (buf.length()>2) {
            buf.setLength(buf.length()-1);
        }
        buf.append("]");
        return buf.toString();
    }
}