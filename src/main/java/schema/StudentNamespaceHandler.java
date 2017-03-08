package schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class StudentNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("student", new StudentBeanDefinitionParser());
    }

}
