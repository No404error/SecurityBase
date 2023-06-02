import cn.edu.sut.informationSecruity23.param.PublicParam;
import cn.edu.sut.informationSecruity23.param.RegisterParam;
import cn.edu.sut.informationSecruity23.util.ElementOperation;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class RegisterParamTest {
    private static final String fileName="register.properties";
    private static final int size=4;

    @Test
    public void createParam() {
        Properties properties = new Properties();
        OutputStream output = null;

        try {
            Field G1 = PublicParam.G1;
            Field G2 = PublicParam.G2;
            Field Zr = PublicParam.Zr;

            //rsk存储属性值
            for(int i=0;i<4;i++){
                properties.setProperty("rsk_" + i, ElementOperation.getElementString(Zr.newRandomElement()));
            }

            //rvk读取属性值
            for(int i=0;i<4;i++){
                properties.setProperty("rvk_" + i, ElementOperation.getElementString(G2.newRandomElement()));
            }

            //rtsk获取属性值
            properties.setProperty("rtsk", ElementOperation.getElementString(Zr.newRandomElement()));
            //rtvk获取属性值
            properties.setProperty("rtvk", ElementOperation.getElementString(G1.newRandomElement()));

            // 创建输出流
            output = Files.newOutputStream(Paths.get(fileName));

            // 存储数据到properties文件
            properties.store(output, "Store Register Params");

            System.out.println("Data stored successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readParam(){
        Element rtvk = RegisterParam.rtvk;
        System.out.println(rtvk);
//        Properties properties = new Properties();
//        InputStream input = null;
//
//        try {
//            // 加载properties文件
//            input = Files.newInputStream(Paths.get("params.properties"));
//            properties.load(input);
//
//            // 读取属性值
//            String g1 = properties.getProperty("g1");
//            String g2 = properties.getProperty("g2");
//            System.out.println(g1);
//            System.out.println(g2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (input != null) {
//                try {
//                    input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
