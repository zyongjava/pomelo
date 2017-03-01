package drools;

import org.drools.core.spi.KnowledgeHelper;

/**
 * 工具类, debug drl文件
 */
public class Utility {

    public static void help(final KnowledgeHelper drools, final String message) {
        System.out.println(message + ", rule triggered: " + drools.getRule().getName());
    }

    public static void helper(final KnowledgeHelper drools) {
        System.out.println("rule triggered: " + drools.getRule().getName());
    }
}
