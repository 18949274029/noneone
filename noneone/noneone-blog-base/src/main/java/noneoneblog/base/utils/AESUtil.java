
package noneoneblog.base.utils;
/**
 * 类ZHAesUtil.java的实现描述：TODO 类实现描述 
 * @author lifeifei002 18 Jul 2017 3:42:48 Ntambama
 */
    import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

    public class AESUtil
    {
        
        private static String sKey;
        
        public AESUtil(String key){
            this.sKey=key;
        }
        // 加密     
        public static String Encrypt(String sSrc) throws Exception {         
            if (sKey == null) {             
                System.out.print("Key为空null");
                return null;         
            }         
            // 判断Key是否为16位         
            if (sKey.length() != 16) {             
                System.out.print("Key长度不是16位");             
                return null;         
            }     
            sSrc =rpadEncrypt(sSrc,' ');
            System.out.println("密钥======"+sKey);
            byte[] raw = sKey.getBytes();   
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec("ABCHINA..ANIHCBA".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//          byte[] encrypted = cipher.doFinal(sSrc.getBytes());//zsh yuan
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));//zsh change
//          System.out.println("TEXT = " + new String(encrypted));
            return byte2hex(encrypted);     
        }       
        
        // 解密     
        public static String Decrypt(String sSrc) throws Exception {
            try {             
                // 判断Key是否正确             
                if (sKey == null) {
                    System.out.print("Key为空null");
                    return null;             
                }             
                // 判断Key是否为16位             
                if (sKey.length() != 16) {
                    System.out.print("Key长度不是16位");
                    return null;             
                }             
                System.out.println("密钥为=====1===："+sKey);
                System.out.println("加密保文为=====1===："+sSrc);
                byte[] raw = sKey.getBytes();
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");// 创建密码器 
                IvParameterSpec iv = new IvParameterSpec("ABCHINA..ANIHCBA".getBytes());
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);             
                byte[] encrypted1 = hex2byte(sSrc);             
                try {                 
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original, "UTF-8");
                    return originalString;             
                } catch (Exception e) {
                    System.out.println("AES.Decrypt()异常："+e.toString());
                    return null;             
                }         
            } catch (Exception ex) {
                System.out.println(ex.toString());
                return null;         
            }     
        }       
        
        public static byte[] hex2byte(String strhex) {
            if (strhex == null) {             
                return null;         
            }         
            int l = strhex.length();
            if (l % 2 == 1) {             
                return null;         
            }         
            byte[] b = new byte[l / 2];
            for (int i = 0; i != l / 2; i++) {
                b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
            }         
            return b;     
        }       
        
        public static String byte2hex(byte[] b) {
            String hs = "";         
            String stmp = "";         
            for (int n = 0; n < b.length; n++) { 
                stmp = (java.lang.Integer.toHexString(b[n] & 0XFF)); 
                if (stmp.length() == 1) {                
                    hs = hs + "0" + stmp;  
                } else {   
                    hs = hs + stmp;  
                }         
            }        
            return hs.toUpperCase();     
        }     
        
        public static String rpadEncrypt(String strValue, char tmp) {

            int strLen = 0;
            try {
                strLen = strValue.getBytes("UTF-8").length;
                System.out.println("strLen:" + strLen);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int num = strLen%16;
            int shortLen = 0;
            if (num != 0) {
                shortLen = 16 - num;
            }
            
            String strReturn = "";
            
            strReturn = strValue;
            if (shortLen == 0) {
                return strReturn;
            } else {
                for (int i = strLen; i < strLen + shortLen; i++) {
                    strReturn = strReturn + tmp;
                }   
            }
            System.out.println("buqizhihou de changdu=="+new String(strReturn));
//          return new String(strReturn);
            return strReturn;
        }
        public static String getSKey() {
            return sKey;
        }

        public static void setSKey(String key) {
            sKey = key;
        } 
        
        public static void main(String[] args) throws Exception {
            /*          * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定          */
            AESUtil aes =new AESUtil("10260d1b4385194a");
//          String cKey = "10260d1b4385194a";         
//          // 需要加密的字串   
//          String cSrc = "ABCHINA..ANIHCBA";   
//          cSrc = rpadEncrypt(cSrc, ' ');
//          System.out.println(cSrc);         
//          // 加密         
//          String enString = Encrypt(cSrc);
//          System.out.println("加密后的字串是：" + enString); 
//          // 解密          
//          String DeString = Decrypt(enString);  
//          System.out.println("解密后的字串是：" + DeString);  
//          String a = "<?xml version='1.0' encoding='UTF-8'?><ABCB2I><Header><RetCode>000000</RetCode><RetMsg>交易成功</RetMsg><SerialNo></SerialNo><InsuSerial>20140120710100008886</InsuSerial><TransTime>092551</TransTime><TransDate>20140120</TransDate><BankCode>04</BankCode><CorpNo>1108</CorpNo><TransCode>1014</TransCode></Header><App><Ret><RiskCode>510</RiskCode><PolicyNo>210917010069</PolicyNo><PolicyStatus>03</PolicyStatus><ApplyDate>20140120</ApplyDate><ValidDate>20140120</ValidDate><BusinType>01</BusinType><BQStatus>S</BQStatus></Ret></App></ABCB2I>";
//          System.out.println("length1:" + a.getBytes("UTF-8").length);
//          a = rpadEncrypt(a, ' ');
//          System.out.println("length2:" + a.getBytes("UTF-8").length);
//          System.out.println("X1.000000378".substring(3, 12));
            String a1= "<ABCB2I><Header><RetCode>000000</RetCode><RetMsg>交易成功</RetMsg><SerialNo>862000000061</SerialNo><InsuSerial>20140507862000000061</InsuSerial><TransDate>20140507</TransDate><TransTime>155605</TransTime><BankCode>04</BankCode><CorpNo>1118</CorpNo><TransCode>1004</TransCode></Header><App><Ret><PolicyNo>99010100000191</PolicyNo><VchNo>201403060879</VchNo><AcceptDate>20140507</AcceptDate><ValidDate>20140508</ValidDate><PolicyDuedate>20150507</PolicyDuedate><DueDate>0</DueDate><UserId>TK00022483</UserId><PayAccount /><Prem>80.00</Prem><Risks><Name>泰康借款人意外伤害保险</Name><Share>1</Share><Prem>60.00</Prem><PayDueDate>20150507</PayDueDate><PayType>0</PayType><RiskCode>607</RiskCode></Risks><Addt><Count>1</Count><Name1>泰康附加借款人疾病身故定期寿险</Name1><Share1>1</Share1><Prem1>20.00</Prem1><PayDueDate1>20150507</PayDueDate1><PayType1>0</PayType1><RiskCode1>655</RiskCode1></Addt><Prnts><Count>29</Count><Prnt1>　</Prnt1><Prnt2>　</Prnt2><Prnt3>　</Prnt3><Prnt4>　</Prnt4><Prnt5>　</Prnt5><Prnt6>　</Prnt6><Prnt7>　　保险单号码：99010100000191 生效日期：2014-05-08</Prnt7><Prnt8>　</Prnt8><Prnt9>　　投保人/被保险人/借款人姓名：汪长 　 　 　　 职业代码：001</Prnt9><Prnt10>　</Prnt10><Prnt11>　 证件类型：居民身份证　　　　　　　　　　　　　　　　　　证件号码：220103195507211252</Prnt11><Prnt12>　</Prnt12><Prnt13>　 出生日期：1955-07-21　　　　 　　　　　　　　　 　　 性 别:男</Prnt13><Prnt14>　</Prnt14>　<Prnt15>　　险种名称： 泰康借款人意外伤害保险</Prnt15><Prnt16>　</Prnt16><Prnt17>　　保险金额：20000.00元　　　 　　　　　　　　　　 保险费：60.00元</Prnt17><Prnt18>　</Prnt18><Prnt19>　　缴费方式：趸交　　 　　　　保险期间：自 2014-05-08 零时起至 2015-05-07 二十四时止</Prnt19><Prnt20>　</Prnt20><Prnt21>　　险种名称： 泰康附加借款人疾病身故定期寿险</Prnt21><Prnt22>　</Prnt22><Prnt23>　　保险金额：10000.00元　　　 　　　　　　　　　　 　 保险费：20.00元</Prnt23><Prnt24>　</Prnt24><Prnt25>　　缴费方式：趸交　　 　　　　保险期间：自 2014-05-08 零时起至 2015-05-07 二十四时止</Prnt25><Prnt26>　</Prnt26><Prnt27>　 保险费合计 (大写)捌拾元整元 　　　　　　　　　　　　　（小写）80.00元</Prnt27>　 　　<Prnt28>　</Prnt28><Prnt29>　 身故受益人：中国农业银行测试4　</Prnt29></Prnts></Ret></App></ABCB2I> ";
            a1 =rpadEncrypt(a1,' ');
            String at = Encrypt(a1);
            System.out.println("加密之后的报文:"+at);
            String a2="A5970E7A96C083692B50DA943B79077EA2FD8C419C5C5D344E27FB7CDC9CBE87D42D7EA0294F3EFE757F9870432932F8A285BB83E57AC420B258F376D81633076F0FE58285378E25906638A1411618371C24ACA77F58C7AFD38F76DF450E7A5089AAC4BBACE4572DD511A814C7AC8720BE10DFEA2C83A18BC078A16F77D5FCD92E5029C2F139D1D7295428171F3EBA6698355775165D7E09F563856A407ABE272961505BBEC20191F0ABC942D28DF605CE90277E47B435BAAB4AB81D9FB6F82359D036A076899B0E23B5F433126F639E6BB8F4AF34FD0B1678C4D1B17DD5C232D95E65EB8DEE467C08336AC6889D127520B26C7352C73DC2922934346A62B291E8A5E52B104A11186916FDEB62DDBD305CB687F8006CF90A52DB31710DD1CFF597B212FE7B2307D0663564550159C97E58F418B1B72CBB190B5D0A7309CCCEADCFDC0C8FCB4298FB889603A85B0F622B2240CF866DD17660F2D5A0C0527363ACB479635D72945FF065284961189ADEBFD2FCDAC4235FF32EF24033A98D09DE02B22412B1583A1986D49CA1EED65DDDC9A166811384FC99DC6442BB974614AA7BD69ED2558617BA88E73C53FE1976F7F5EB2331347C4273B99C6C63E8FCB2C99C3B5A8CD95A2C94045A8F9ED34ACFB13016343447CB7B3F7169EFDF7C1DDC5BF2C402DDCD684B800DB3502584AB847B13F2097395B8A64BCA40C727873C02B95553ED4DCC08AF488E3E75A76B2811F11E3DB03E6A96286863EEC38197AFA0D58B48EA7A4D2673279F1CA66DC22BABDC16C2B100996B10C1E9213BB9FE1560B821890E88F3384B85F65DF3C38AD4570B2EA23A6F77404853ACE269621B781640F20F999F3309B072D07771F426B835D2115E2BC7319515340D1E6AF529B89D7A0D064DBAF2C397D9972CCEF90769EC89775E48B780C00018DA48B6AD4282304DD55D39A650FB7D515D3AF65567C280890A42D7BC0685C44A5B16F75C636E5735195CC6BA505D3B600CA43F4A612A66CC1924FB62D3C8BA5A1F44548E5F19B6A673D27E8F7D902FD0D047EF638A2C30E26E33E8CB772BF6044A8C1FCDA5119C12F1B6C43F587D9FD48442F5C70999036CE8F3B72DDA7F6CAA585CDFE392A7F4A5A3AC3E1BF86BD31F3D601614C7C7B4E3792F1C6FF5B2E153832B6C8A40117A45602351B10559D7B407E87BE6B775408D6BFB52B8B0AE3F388B36B96F0FA2798836279EA2DC1FF1D4AED5AAE42B118EAEC0D884E2784C1E9E66BD1D80F27A791F5E21C4343ABB0CDED04B49AE40FD50175E6E5B485459C03E3C43C58F717194965918460A505E9FC6387606A329673A599197C674BEC2374A6ED539BE76596F58A5ECE2FDF455D056A91B0F31E8B2DD23E39A917D62EDDA3017A5C2664F92626E9D955BAC5D3C62193F7BE8BBA73917D0661EF6217FC8DEC3D086A118ABAC061A8127BB9250506A646BA6438E5042DCCD4B44A78D949F9F9A3A7430F2270DFADF572B1EB0677F7500B1DAA129D7CC20C70905D836A08EDC76D5D80FDF8D76BC39D804A5D7B4128B612E940C558B34E9CDD47344DD8760AD5F341FCA1241D63015CFF907DF674512E8B34386C4249D35E6DF870CF947EF50C5BA8AA90E447C1A764E8496735AB61BE866A75CF4B868739D29C3585913E715C74C46278F214C122B5C50139690C39E3CC270D33CBE7032DD34CF175F20B110F3ADD16C98FC59CA77258C00DEDDE281BF9B273E47B13E1BDA918A7890398056B028E74E94A6FA158DFFF39F954C7ED298A314230785257BD2870E880FBED67591213034123B9B6DA7D0E7ACCC80AFB2DB2DF7FE9066DF82369902FBB4A47B8DD556BA54903A251D3D822025ACBC60E9A39F311798773F0C0457CD94F5D78E9660A70B56FD3C7B6901AAD6A83472F1177CB11E2EABE763E8C25DD7E22503F5BFBC9D380F7C7C9C409CC4A465382A67DFC28B7749B4082847D4A62FFB341B9805270BBF72A4B11F16DC241E3544F1EC4B04033E8F5A2C6B48A671A47764425FF267AFC8456D0641F626BDA3A96B8C68DAEEBC67685165EBED5475F189A2F4F1E781F88733945CA8A639C346A20F1F2610EB2279FD197D80C10E962A2BD28B7C88F9E71A1EEECE0D63FE480F6F57BE530537153C82BDBDF9D9606F01F391C383A6F8DF9DA4FC6E5C4B701BA810D45F67FFFD1B81436CD2CB526004DF6BB86EC0E3730D491AECE7745CFE5668C5A01104018F607D2E7A7CBA1F6EA4F1A590A183B27CD7F23B80026A913D6FD6B17BE1463CAF3DDC1C7422D494F7085BC109E018450BE9CBBBE35A9B1015DFCDAA67879EEF4D14AB288BAF8593066D168BFCF5BFE7D787DEC272D27F8D07687A9E607117DF3E64B4FBF6EFC1BD2BF4DC48EA2F992612DBAC1CC4CCB932C2504CDC46B0389376BC291714297F9EBDC464B7230D275CD846F6D89FC0D96A0FCEAD4256A7D6F5F7A498AAD1CD2F9B7A47BA9DDC57D1AE3ADD89D16E108826BCB3497A6676BC3F420A253B0C11BFF326FD93D262FE38B9BF9E538599413C1757BE5EE0B27165F7FBA09777D80F9991F73401BB44FE34EE379681661A9C26DA6486E721B617C503CFB66637AB6669F4D61E063FF89F85C6A0E79B60C2D76D6459B8A2BB2FD94819F8F62C2AE6E2166426B7E062E3DD36A9B71ED5C08FA0D7D5C1956E228575AB4E7D5B693D4E73FC345F612B458A1B22F36A325C9852ACCF304BF0F9C1FEF31CD3268603F510928F4F39AA3D86927939420B1B0E873C870E6A9AAE7FA5AC9A2607870A5DF9DDFB649135D140E010311A5876FD04263D51967FE030C8093F3FBE3AE7D86F3FE71342D4912DD5749C304B8922A561A6AFF5646B060D308CD169FF0746367840D2B149F752A5E17E1063AAB01E491D8D34FB4CCEC23F23D7B0663A192F8071F10BA17F511C94A41713F679D1682A2EEA50478B9474EA360C1EF493E5F16FF369D4C38215E1F4956E24C4DC2B683F08E7FF68F218F7F02B880797786B864A62B776439957C2BE2C035A3163638223FC9855A22FF49FFA1095274E3C2A011347BC14441034E402D2DC14B6E2220228EE9A8B0D478ED53180279EE19D33EA76A326E14630CD2C46FD53ED3553D152044E89D68C79B158FB3887CABFB25977ACA627DBDA0B2325313E1E31EFEB0BE74AB372DF026F7A730A623243F8F0C96336EB9C26DB019869ABEB73DE0AABB6D7E102C535CF7F48D6D85AD9A4E8E5117AD7050877835A7C72469A72CB39A6DE8D479BABCE7B5E7BA4420D3A9193CC65B916D11780C53AEA75CFB5151C521B8B7E06F0428E4C13F4AE03DEDBB4C8DE91B92127AFC25BD327B6672D0FF59C6BAF38EB860800458B38D101564B3B8EF41344049F2783F33C93D7A99D840C96329C96471BDEEE0F3329090B0FA7884C7883F1EFAF6BFF6E068F90E8D7879178660A3A085B8435E76ECF776EBF2B4D0DD4343B1949D11F099AEF029F4EB0F607C791B0B02F48122D3170E2CC70A11FEB696995C45F0E4B9AFDB5FBB42699F8E4755B74114E7557CF96CAB1ED9C63BECA0808666A197B2BD90B77D4F4E4317928609DAA88DB1A159B437C5030216D7964C8AD5969E443690C05D690F5A995522C57B38145A5104D4EA6BE3AAB59E628160453D8801FF482595BB27B3ECF227FCF0D6AE81D6542BB89AD7166C42F4E8268694181E323C116C1FA36A973873F752494FA3E0B15FE064A1CE859D7";
            String at1=Decrypt(a2);
            System.out.println("解密之后的报文："+at1);
        }

      
    }

