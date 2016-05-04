package adriftbook.utils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.HashSet;
public class PingyinUtils
{

    /**
     * 汉字转换为汉语拼音首字母，英文字符不变
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines)
    {
        String pinyinName = "";
        // 转化为字符
        char[] nameChar = chines.toCharArray();
        // for(int i=0;i<nameChar.length;i++){
        // System.out.println(nameChar[i]);
        // }
        // 汉语拼音格式输出类
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出设置,大小写,音标方式等
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++)
        {
            // 如果是中文
            if (nameChar[i] > 128)
            {
                try
                {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat)[0].charAt(0);
                }
                catch (BadHanyuPinyinOutputFormatCombination e)
                {
                    e.printStackTrace();
                }
            }
            else
            {// 为英文字符
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }
    public static String filterPunctuations(String s)
    {
        String[] punctuations = new String[]{",", ".", "?", "\'", "\"", "-", "，",
                "。", "？", "-",
                "——", "\t", "\n"," ","!","、","！"};
        HashSet<String> punctuationSet = new HashSet<String>();
        int i = 0;
        for (i = 0; i < punctuations.length; i++)
            punctuationSet.add(punctuations[i]);
        StringBuilder res = new StringBuilder("");
        for (i = 0; i < s.length(); i++)
        {
            if (punctuationSet.contains(s.substring(i, i + 1)))
                continue;
            res.append(s.substring(i, i + 1));
        }
        return res.toString();
    }
    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines)
    {
        chines = filterPunctuations(chines);
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++)
        {
            if (nameChar[i] > 128)
            {
                try
                {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat)[0];
                }
                catch (BadHanyuPinyinOutputFormatCombination e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName.toLowerCase();
    }
    public static void main(String[] args)
    {
        System.out.println(converterToFirstSpell("长城有限公司"));
        // System.out.println(converterToSpell("康师傅").toLowerCase());
        System.out.println(converterToSpell("周建国是一个伟大的人,?好人"));
    }
}