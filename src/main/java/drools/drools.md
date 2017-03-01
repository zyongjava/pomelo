## Drools
参考地址：https://www.tutorialspoint.com/drools/drools_rule_syntax.htm

### Rule Syntax

##### 1. Conditions in Rules

A rule can contain many conditions and patterns such as:

```
Account (balance == 200)
Customer (name == “Vivek”)
```
The above conditions check if the Account balance is 200 or the Customer name is “Vivek”.

##### 2. Variables in Rules
A variable name in Drools starts with a Dollar($) symbol.

```
$account : Account( )
$account is the variable for Account() class
```
Drools can work with all the native Java types and even Enum.

##### 3. Comments in Rules
The special characters, # or //, can be used to mark single-line comments.

For multi-line comments, use the following format:

```
/*
   Another line
   .........
   .........
*/
```

##### 4.Functions in Rules
Functions are a convenience feature. They can be used in conditions and consequences. Functions represent an alternative to the utility/helper classes. For example:

```
function double calculateSquare (double value) {
   return value * value;
}
```

##### 5. Salience
Salience is a very important feature of Rule Syntax. Salience is used by the conflict resolution strategy to decide which rule to fire first. By default, it is the main criterion.

We can use salience to define the order of firing rules. Salience has one attribute, which takes any expression that returns a number of type int (positive as well as negative numbers are valid). The higher the value, the more likely a rule will be picked up by the conflict resolution strategy to fire.

```
salience ($account.balance * 5)
```
The default salience value is 0. We should keep this in mind when assigning salience values to some rules only.

