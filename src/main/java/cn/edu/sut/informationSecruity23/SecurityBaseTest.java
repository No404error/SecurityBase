package cn.edu.sut.informationSecruity23;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class SecurityBaseTest
{
    public static void main( String[] args ){
//        Field zr = cn.edu.sut.informationSecruity23.base.ElementOperation.getG2();
//        Element element = zr.newRandomElement().getImmutable();
//        System.out.println(element);
//        String elementString = cn.edu.sut.informationSecruity23.base.ElementOperation.getElementString(element);
//        Element element1 = ElementOperation.getElementFromString(elementString, zr);
//        if(Objects.equals(element1,element))
//            System.out.println("yes");
        Properties properties = new Properties();
        InputStream input = null;

        try {
            // 加载properties文件
            input = SecurityBaseTest.class.getClassLoader().getResourceAsStream("params.properties");
            properties.load(input);

            // 读取属性值
            String g1 = properties.getProperty("g1");
            String g2 = properties.getProperty("g2");
            System.out.println(g1);
            System.out.println(g2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
