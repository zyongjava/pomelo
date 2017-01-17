package validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by zhengyong on 17/1/17.
 */
public class ValidationTest {

    public static void main(String[] args) {
        Address address = new Address();
        address.setAddressline1(null);
        address.setAddressline2(null);
        address.setCity("Llanfairpwllgwyngyllgogerychwyrndrobwyll-llantysiliogogogoch");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);

        for (ConstraintViolation<Address> constraintViolation : constraintViolations) {
            System.out.println(constraintViolation.getPropertyPath() + ":" + constraintViolation.getMessage());
        }
    }
}
