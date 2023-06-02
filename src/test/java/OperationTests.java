import cn.edu.sut.informationSecruity23.RegistrationCenter;
import cn.edu.sut.informationSecruity23.credential.AggregationCredential;
import cn.edu.sut.informationSecruity23.credential.Credential;
import cn.edu.sut.informationSecruity23.operation.common.CommonOperation;
import cn.edu.sut.informationSecruity23.operation.issuer.IssuerOperation;
import cn.edu.sut.informationSecruity23.operation.register.RegisterCenterOperation;
import cn.edu.sut.informationSecruity23.operation.result.Proof;
import cn.edu.sut.informationSecruity23.operation.user.UserOperation;
import cn.edu.sut.informationSecruity23.operation.verifier.VerifierOperation;
import cn.edu.sut.informationSecruity23.operation.result.UserProof;
import cn.edu.sut.informationSecruity23.param.*;
import cn.edu.sut.informationSecruity23.util.ElementOperation;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class OperationTests {
    @Test
    public void createParam() {
        Properties properties = new Properties();
        OutputStream output = null;

        try {
            Field G1 = PublicParam.G1;
            Field G2 = PublicParam.G2;
            // 设置属性值
            properties.setProperty("g1", ElementOperation.getElementString(G1.newRandomElement()));
            properties.setProperty("g_1", ElementOperation.getElementString(G1.newRandomElement()));
            properties.setProperty("g__1", ElementOperation.getElementString(G1.newRandomElement()));
            properties.setProperty("g2", ElementOperation.getElementString(G2.newRandomElement()));

            // 创建输出流
            output = Files.newOutputStream(Paths.get("params.properties"));

            // 存储数据到properties文件
            properties.store(output, "Store Public Params");

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
        Element g1 = PublicParam.g1;
        System.out.println(g1);
    }

    @Test
    public void testPairing(){
        Pairing pairing = PublicParam.pairing;
        Element g1 = PublicParam.g1;
        Element g2 = PublicParam.g2;
        Element num = PublicParam.Zr.newRandomElement();

        Element pairing1 = pairing.pairing(g1.duplicate().powZn(num), g2);
        Element pairing2 = pairing.pairing(g1, g2.duplicate().powZn(num));
        if(pairing1.isEqual(pairing2))
            System.out.println(1);
    }

    @Test
    public void testStoreField(){
        Field zr = PublicParam.Zr;
        Field g2 = PublicParam.G2;
        Field g1 = PublicParam.G1;

        Element g11 = g1.newRandomElement();
        Element g21 = g2.newRandomElement();
        Element zr1 = zr.newRandomElement();

        System.out.println(g11.getField().equals(g1));
        System.out.println(g11.getField().equals(g2));
        System.out.println(g11.getField().equals(zr));

        System.out.println(g21.getField().equals(g1));
        System.out.println(g21.getField().equals(g2));
        System.out.println(g21.getField().equals(zr));

        System.out.println(zr1.getField().equals(g1));
        System.out.println(zr1.getField().equals(g2));
        System.out.println(zr1.getField().equals(zr));
    }

    @Test
    public void testElementConvertString(){
        Element g2 = PublicParam.g2;
        String elementString = ElementOperation.getElementString(g2);
        Element elementFromString = ElementOperation.getElementFromString(elementString);
        if(g2.isEqual(elementFromString))
            System.out.println("yes");
    }


    @Test
    public void testNegateOperation() {
//        Element g2 = PublicParam.g2.duplicate();
//        Element g2i = g2.invert().getImmutable();
//        Element one = g2.duplicate().mul(g2i);
        Element zr = PublicParam.Zr.newRandomElement().getImmutable();
        Element zr1 = PublicParam.Zr.newRandomElement().getImmutable();
        Element element2 = PublicParam.G2.newRandomElement().getImmutable();
        Element element1 = PublicParam.G1.newRandomElement().getImmutable();
        Element e1 = element2.duplicate().powZn(zr);
        Element d2 = e1.duplicate().mul(element2.powZn(zr1.negate()));
        Element mul = d2.duplicate().mul(element2.powZn(zr1));
        if(e1.isEqual(mul))
            System.out.println(1);

        //System.out.println(PublicParam.G2.newRandomElement());
    }

    /**
     * 测试credential的流程
     * 使用operation要对param赋值
     */
    @Test
    public void testCredentialProcess(){

        Field Zr = PublicParam.Zr;
        Element g1 = PublicParam.g1;
        Element g2 = PublicParam.g2;

        //时间纪元
        String epoch=String.valueOf(System.currentTimeMillis());
        PublicParam.epoch=epoch;
        //user init
        Element usk= Zr.newRandomElement().getImmutable();
        Element uvk= g1.duplicate().powZn(usk).getImmutable();
        UserParam.usk=usk;
        UserParam.uvk=uvk;
        UserParam.nodeValue=Zr.newRandomElement().getImmutable();
        UserParam.fr= Zr.newRandomElement().getImmutable();
        UserParam.Rv= Zr.newRandomElement().getImmutable();

        Element[] rvk = RegisterParam.rvk;
        Element traceToken = RegisterCenterOperation.createUserTraceToken(UserOperation.createUserProof(rvk[0]),UserParam.nodeValue,UserParam.fr).getImmutable();
        UserParam.rToken= RegisterCenterOperation.createUserRevokeToken(UserParam.nodeValue, UserParam.Rv).getImmutable();
        UserParam.tToken=traceToken;

        System.out.println("UserParam Init");


        String[] attrs1 = Issuer1Param.attribute;
        Element[] iskList1 =Issuer1Param.isk;
        Element[] ivkList1 =Issuer1Param.ivk;

        boolean equal = g2.powZn(iskList1[0]).isEqual(ivkList1[0]);


        //issuer1
        UserProof userProof = UserOperation.createUserProof(ivkList1[0]);
        Boolean userHaveSecretKey = CommonOperation.isUserHaveSecretKey(ivkList1[0], userProof);

        Credential credential1 = IssuerOperation.getCredential(uvk, 2, ivkList1,iskList1, attrs1);
        //credential.setCredential(G1.newRandomElement());
        Boolean aBoolean1 = UserOperation.SingleCredVerify(credential1, ivkList1);

        String[] attrs2= Issuer2Param.attribute;

        Element[] iskList2 =Issuer2Param.isk;
        Element[] ivkList2 =Issuer2Param.ivk;
        Credential credential2 = IssuerOperation.getCredential(uvk, 2, ivkList2,iskList2, attrs2);
        //credential.setCredential(G1.newRandomElement());
        Boolean aBoolean2 = UserOperation.SingleCredVerify(credential2, ivkList2);

        AggregationCredential aggregationCredential = UserOperation.CredAgg(credential1, credential2);
        Boolean aBoolean = VerifierOperation.AggregationCredVerify(uvk, aggregationCredential, ivkList1, ivkList2);
        //System.out.println(aBoolean);

        String message = "OKKKKKK";
        Proof proof = UserOperation.Show(aggregationCredential, message);
        message="Ok";
        Boolean verify = VerifierOperation.Verify(proof, aggregationCredential.getIvkList(), message);
        System.out.println(verify);
    }
}
