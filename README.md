if maven is installed on system choose env/profile (eg dev,stage,prod) mvn spring-boot:run -Dspring-boot.run.profiles=dev

if not installed run this in terminal ./mvnw spring-boot:run

use api doc for api documentation in link below
http://localhost:8080/swagger-ui/index.html

simple test
- create user with "ROLE_ADMIN,ROLE_USER" for testing set pk=1
- note id,user,password created
- request auth/authenticate - http://localhost:8080/v1/auth/authenticate
- copy token. used in posting items and adding to cart
- add an item. set id=1,set price=xxx and so on
- add to cart. use cartId=1, itemId=yousetuponcreation,quantity=xx

TODO:
CICD
    Dockerize

Note:
this is not complete but showcased a production like app base.