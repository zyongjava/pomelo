package json;

import java.util.Set;

/**
 * Created by zhengyong on 16/12/16.
 */
public class Attribute {

    @AttributeConfig(includes = { "accountMobile", "myMobile/accountMobile" })
    private Set<String> accountMobile;
    @AttributeConfig(includes = { "idNumber", "myIdNumber/idNumber" })
    private Set<String> idNumber;

    @AttributeConfig(includes = { "cardNumber", "myCard/cardNumber" })
    private Set<String> cardNumber;

    @AttributeConfig(includes = { "qqNumber", "myQQ/qqNumber" })

    private Set<String> qqNumber;

    @AttributeConfig(includes = { "accountEmail" })
    private String      accountEmail;

    public Set<String> getAccountMobile() {
        return accountMobile;
    }

    public void setAccountMobile(Set<String> accountMobile) {
        this.accountMobile = accountMobile;
    }

    public Set<String> getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Set<String> idNumber) {
        this.idNumber = idNumber;
    }

    public Set<String> getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Set<String> cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Set<String> getQqNumber() {
        return qqNumber;
    }

    public void setQqNumber(Set<String> qqNumber) {
        this.qqNumber = qqNumber;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }
}
