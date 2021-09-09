package com.example.auction.Configuration;



import com.mchange.v2.util.ComparatorUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class H_Format {

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**下划线转驼峰*/
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**驼峰转下划线*/
    public static String humpToLine(String str){
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, "_"+matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //判断是否为整数
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //unix time 转普通时间  Date -> mysql（timestamp）
    public static Date unixTime2Date(String source){
        if (H_Format.isInteger(source)) {
            Long timestamp = Long.parseLong(source) * 1000;
            return new Date(timestamp);
        }
        return null;
    }

    /**
     * 普通时间转unix time  ,
     * source 默认source为null，则将当前时间进行转换
     */
    public static long unixTime(String source){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (source == null){
            source = format.format(new Date());
        }

        try {
            return format.parse(source).getTime()/1000;
        } catch (ParseException e) { }
        return 0;//在mysql里面默认为1970.01.01
    }

    public static long Date2unixTime(Date source){
        if (source == null){
            source = new Date();
        }
        return source.getTime()/1000;
    }

    //获取时间+6位随机数,表示某个时间点生成的随机数
    public static long getRandomNum(){
       return Long.valueOf( Date2unixTime(null) + "" + Long.parseLong( getRandomStr(null,6)));
    }

    /**
     * sources可以是字母+数字，生成pc站的验证码了
     * @param sources 长度必须大于10位
     * @return 默认返回6位数字
     */
    public static String  getRandomStr( String sources, int lenght){
        if (sources == null) {
            sources = "0123456789";
        }
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < lenght; j++ ) {
            flag.append(sources.charAt(rand.nextInt(9))).append("");
        }
        return flag.toString();
    }

   /* //用户明文密码，生成新的密码
    public static String secureRandomNumber(String password ){
        RandomNumberGenerator cryptoRandomDataGenerator = new SecureRandomNumberGenerator();
        String algorithmName = "md5";
        int hashIterations = 2;
        String newPassword = new SimpleHash(algorithmName, password,
                ByteSource.Util.bytes(cryptoRandomDataGenerator.nextBytes().toHex()), hashIterations).toHex();

        return newPassword;
    }*/

    //用于数字签名时候，传输参数进行排序
    public static String buildSignStr(Map<String, String> params){
        return buildSignStr(params, "", "");
    }
    public static String buildSignStr(Map<String, String> params, String SEPARATOR, String KEY_VALUE_SEPARATOR ){
        StringBuilder sb = new StringBuilder();
        // 将参数以参数名的字典升序排序
        Map<String, String> sortParams = new TreeMap<String, String>(params);
        // 遍历排序的字典,并拼接"key=value"格式
        for (Map.Entry<String, String> entry : sortParams.entrySet()) {
            if (sb.length()!=0) {
                sb.append(SEPARATOR);
            }
            sb.append(entry.getKey()).append(KEY_VALUE_SEPARATOR).append(entry.getValue());
        }
        return  sb.toString();
    }
    private final static String SORT_ASC = "ASC";
    private final static String SORT_DESC = "DESC";



    public static <T> void sortList(List<T> list, String property, String sort) {
        Collections.sort(list, (o1, o2) -> {
            try {
                Field field1 = o1.getClass().getDeclaredField(property);
                field1.setAccessible(true);
                Integer i1 = Integer.parseInt(getNumber(field1.get(o1).toString()));

                Field field2 = o2.getClass().getDeclaredField(property);
                field2.setAccessible(true);
                Integer i2 = Integer.parseInt(getNumber(field2.get(o2).toString()));

                //降序
                if (sort.toUpperCase().equals(SORT_DESC))
                    return i2.compareTo(i1);

                //升序
                if (sort.toUpperCase().equals(SORT_ASC))
                    return i1.compareTo(i2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }
    public static String getNumber(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


  /*  public static void sort(List list, String filedName, boolean ascFlag) {
        if (list.size() == 0 || filedName.equals("")) {
            return;
        }
        Comparator<?> cmp = ComparableComparator.getInstance();
        if (ascFlag) {
            cmp = ComparatorUtils.nullLowComparator(cmp);
        } else {
            cmp = ComparatorUtils.reversedComparator(cmp);

        }
        Collections.sort(list, new BeanComparator(filedName, cmp));
    }
*/
}
