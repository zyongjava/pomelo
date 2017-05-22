## protobuf

#### 一、环境

> 操作系统：OS X Yosemite


#### 二、下载安装编译器

>地址：https://github.com/google/protobuf/releases/download/v3.3.0/protoc-3.3.0-osx-x86_64.zip

#### 三、编写序列化协议`.proto`

```
syntax = "proto2";

package tutorial;

option java_package = "protobuf";
option java_outer_classname = "AddressBookProtos";

message Person {
  required string name = 1;
  required int32 id = 2;
  optional string email = 3;

  enum PhoneType {
    MOBILE = 0;
    HOME = 1;
    WORK = 2;
  }

  message PhoneNumber {
    required string number = 1;
    optional PhoneType type = 2 [default = HOME];
  }

  repeated PhoneNumber phones = 4;
}

message AddressBook {
  repeated Person people = 1;
}
```

#### 四、编译`.proto`生成java代码

解压`protoc-3.3.0-osx-x86_64.zip`文件，到bin目录下执行以下命令：

```
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/addressbook.proto
```

比如：

```
 ./protoc -I=/Users/zhengyong/work_dev/pomelo/src/main/java/protobuf --java_out=/Users/zhengyong/work_dev/pomelo/src/main/java/protobuf /Users/zhengyong/work_dev/pomelo/src/main/java/protobuf/addressbook.proto
```


#### 五、修改编译源码：

所有java.nio.ByteBuffer声明的变量`data`,需要修改：

```
 java.nio.ByteBuffer data
```

修改成：

```
CodedInputStream.newInstance(data)
```

比如：

```
public static protobuf.AddressBookProtos.Person.PhoneNumber parseFrom(
  java.nio.ByteBuffer data)
  throws com.google.protobuf.InvalidProtocolBufferException {
return PARSER.parseFrom(CodedInputStream.newInstance(data));
}
```

#### 六、Maven依赖

```
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.2.0</version>
</dependency>
```

#### 七、序列化与反序列化

序列化：

```
AddressBookProtos.AddressBook.Builder addressBook = AddressBookProtos.AddressBook.newBuilder();

AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();
person.setId(1);
person.setName("张三");
person.setEmail("524806855@qq.com");

// Add an address.
addressBook.addPeople(person);
// Write the new address book back to disk.
FileOutputStream output = new FileOutputStream(filePath);
addressBook.build().writeTo(output);
output.close()
```

反序列化：

```
AddressBook addressBook = AddressBook.parseFrom(new FileInputStream(filePath));
```

