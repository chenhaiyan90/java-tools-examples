package com.dova.dev.test.secureTest;

import com.dova.dev.secure.RsaExample;
import com.dova.dev.secure.RsaUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by liuzhendong on 16/5/23.
 */
public class RsaTest {

    public Charset charset = Charset.forName("utf-8");
    public PublicKey publicKey;
    public PrivateKey privateKey;
    {
        try{
            privateKey = RsaUtils.loadPrivateKey(RsaExample.PRI_KEY);
            publicKey = RsaUtils.loadPublicKey(RsaExample.PUB_KEY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void testEncryptAndDecrypt(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 100; i++){
            sb.append("123456789012");
        }
        String encrypt = RsaUtils.encrypt(sb.toString(),publicKey);
        System.out.println("encrypt:" + encrypt);
        String decrypt = RsaUtils.decrypt(encrypt, privateKey);
        System.out.println("decrypt:" + decrypt);
        Assert.assertTrue(decrypt.equals(sb.toString()));
    }

    @Test
    public void testSign(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 1000; i++){
            sb.append("123456789012");
        }
        String sign = RsaUtils.sign(sb.toString(),privateKey);
        System.out.println("sign:" + sign);
        boolean verify = RsaUtils.verifySign(sb.toString(), publicKey, sign);
        Assert.assertTrue(verify);
    }

}
