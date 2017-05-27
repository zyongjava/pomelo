package serialization.protobuf;

import serialization.protobuf.AddressBookProtos.AddressBook;
import serialization.protobuf.AddressBookProtos.Person;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>
 * protobuf 序列化和反序列化
 * </p>
 */
public class ProtobufTest {

    private static final String filePath = "/Users/zhengyong/Development/protoc-3.3.0-osx-x86_64/readme.txt";

    // Main function: Reads the entire address book from a file and prints all
    // the information inside.
    public static void main(String[] args) throws Exception {

        addPerson();
        // Read the existing address book.
        AddressBook addressBook = AddressBook.parseFrom(new FileInputStream(filePath));

        Print(addressBook);
    }

    // Iterates though all people in the AddressBook and prints info about them.
    private static void Print(AddressBook addressBook) {
        for (Person person : addressBook.getPeopleList()) {
            System.out.println("Person ID: " + person.getId());
            System.out.println("  Name: " + person.getName());
            if (person.hasEmail()) {
                System.out.println("  E-mail address: " + person.getEmail());
            }

            for (Person.PhoneNumber phoneNumber : person.getPhonesList()) {
                switch (phoneNumber.getType()) {
                    case MOBILE:
                        System.out.print("  Mobile phone #: ");
                        break;
                    case HOME:
                        System.out.print("  Home phone #: ");
                        break;
                    case WORK:
                        System.out.print("  Work phone #: ");
                        break;
                }
                System.out.println(phoneNumber.getNumber());
            }
        }
    }

    private static void addPerson() throws Exception {
        AddressBookProtos.AddressBook.Builder addressBook = AddressBookProtos.AddressBook.newBuilder();

        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();
        person.setId(1222);
        person.setName("张三");
        person.setEmail("524806855@qq.com");

        // Add an address.
        addressBook.addPeople(person);
        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream(filePath);
        addressBook.build().writeTo(output);
        output.close();

        System.out.println("add person to file success.");
    }

}
