package org.mmo.engine.util;

import java.util.HashSet;
import java.util.Set;

public class Symbol {

    public static final String Empty = "";
    public static final String ENTER = "\n";
    public static final String EmptyZero = "{0:0}";
    public static final String FENHAO = ";";//分号
    public static final String FENHAO_REG = ";|；";//分号
    public static final String MAOHAO = ":";//冒号
    public static final String MAOHAO_REG = ":|：";//冒号
    public static final String MAOHAO_1_REG = ":|：|=|_";//冒号
    public static final String DOUHAO = ",";
    public static final String DOUHAO_REG = ",|，";
    public static final String DUNHAO = "、";
    public static final String XIEGANG_REG = "/";
    public static final String SHUXIAN = "|";
    public static final String SHUXIAN_REG = "\\|";
    public static final String XIAHUAXIAN = "_";
    public static final String XIAHUAXIAN_REG = "_";
    public static final String JINGHAO_REG = "\\#";
    public static final String DENGHAO = "=";
    public static final String AT_REG = "@";
    public static final String STAR = "*";
    /**中横线*/
    public static final String ZHONG_HENG_XIAN="-";
    public static final String JIAHAO = "\\+";

    /**
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return null == str || str.trim().isEmpty();
    }

    public static int[] toIntArray(String str) {
        String[] strs = str.split(Symbol.DOUHAO_REG);
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.valueOf(strs[i]);
        }
        return ints;
    }
    
    public static Set<Integer> toIntSet(String str) {
        String[] strs = str.split(Symbol.DOUHAO_REG);
        Set<Integer> sets=new HashSet<>(strs.length);
        for (int i = 0; i < strs.length; i++) {
            sets.add(Integer.valueOf(strs[i]));
        }
        return sets;
    }
    
    public static Set<Long> toLongSet(String str) {
        String[] strs = str.split(Symbol.DOUHAO_REG);
        Set<Long> sets=new HashSet<>(strs.length);
        for (int i = 0; i < strs.length; i++) {
            sets.add(Long.valueOf(strs[i]));
        }
        return sets;
    }
}
