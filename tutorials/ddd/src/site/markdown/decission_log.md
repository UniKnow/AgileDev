# Decission Log

## `javax.transaction.Transactional` vs `org.springframework.transaction.annotation.Transactional`

Spring has defined its own Transactional annotation to make Spring bean methods transactional, years ago.

Java EE 7 has finally done the same thing and now allows CDI bean methods to be transactional, in addition to EJB methods. So since Java EE 7, it also defines its own Transactional annotation (it obviously can't reuse the Spring one).

Depending on the Spring version in use the java.transaction.Transactional is ignored, support was added in Spring 4.

Since we are using Spring 3 within this framework `org.springframework.transaction.annotation.Transactional` annotation will be used to indicate class/method has to be transactional.

