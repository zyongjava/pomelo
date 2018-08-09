package drools;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

import java.math.BigDecimal;

/* This is a sample class to launch a rule. */

/**
 * 6.X       API  KIE
 */
public class DroolsTest {

    public static final void main(String[] args) {

        KieSession kieSession = buildKieSession1();
        KieSession kieSession2 = buildKieSession2();

        ItemCity item1 = new ItemCity();
        item1.setPurchaseCity(ItemCity.City.HANGZHOU);
        item1.setTypeofItem(ItemCity.Type.LEISURE);
        item1.setSellPrice(new BigDecimal(10));
        kieSession.insert(item1);

        // 创建Fact对象
        ItemCity item2 = new ItemCity();
        item2.setPurchaseCity(ItemCity.City.HANGZHOU);
        item2.setTypeofItem(ItemCity.Type.TOURISM);
        item2.setSellPrice(new BigDecimal(10));
        kieSession.insert(item2);

        ItemCity item3 = new ItemCity();
        item3.setPurchaseCity(ItemCity.City.CHENGDU);
        item3.setTypeofItem(ItemCity.Type.LEISURE);
        item3.setSellPrice(new BigDecimal(10));
        kieSession.insert(item3);

        ItemCity item4 = new ItemCity();
        item4.setPurchaseCity(ItemCity.City.CHENGDU);
        item4.setTypeofItem(ItemCity.Type.TOURISM);
        item4.setSellPrice(new BigDecimal(10));
        kieSession.insert(item4);

        kieSession.fireAllRules();

        System.out.println(item1.getPurchaseCity().toString() + ": " + item1.getLocalTax().intValue());
        System.out.println(item2.getPurchaseCity().toString() + ": " + item2.getLocalTax().intValue());
        System.out.println(item3.getPurchaseCity().toString() + ": " + item3.getLocalTax().intValue());
        System.out.println(item4.getPurchaseCity().toString() + ": " + item4.getLocalTax().intValue());

        kieSession.dispose();

    }

    /**
     * 方式一：创建KieSession
     *
     * @return KieSession
     */
    private static KieSession buildKieSession1() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        // 依赖META-INF下kmodule.xml文件
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");
        return kieSession;
    }

    /**
     * 方式二：创建KieSession
     *
     * @return KieSession
     */
    private static KieSession buildKieSession2() {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addResource(ResourceFactory.newClassPathResource("rules/chengdu.drl"));
        kieHelper.addResource(ResourceFactory.newClassPathResource("rules/hangzhou.drl"));
        KieBase kieBase = kieHelper.build();
        KieSession kieSession = kieBase.newKieSession();
        return kieSession;
    }

}
