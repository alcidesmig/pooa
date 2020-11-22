**Alcides Mignoso e Silva - 760479**

# SOLID

SOLID é um acrônimo para um conjunto de cinco princípios definidos dentro da programação orientada a objetos com o objetivo de "facilitar a compreensão, o desenvolvimento e a manutenção de software".

# Single Responsibility Principle

O primeiro princípio, que é o qual a letra S se refere, é o Single Responsibility Principle (SRP), ou Princípio da Responsabilidade Única, e consiste na expansão da frase:

> "[A class should have only one reason to change](https://books.google.com/books?id=0HYhAQAAIAAJ&redir_esc=y)" - Robert C. Martin (criador)

Em outras palavras, é tido como objetivo que uma classe, dentro de um projeto, tenha somente uma responsabilidade, chamada pelo autor de razão para mudar. Aparece, então, a questão: 

> "Mas o que significa responsabilidade?"

Afim de facilitar a compreensão, pode-se observar o seguinte exemplo: imagine que exista um programa que precise receber dados de um servidor HTTP, extrair informações relevantes desses dados e então gerar um gráfico para o usuário com as informações relevantes extraídas. Nesse exemplo, pode-se elencar três responsabilidades principais:

 1. Comunicação com o servidor web HTTP (para receber dados)
 2. Extração de informações relevantes
 3. Geração do gráfico a partir das informações relevantes

Dessa forma, o SRP determina que não deve existir uma classe, dentro do projeto, com mais do que uma responsabilidade. Sendo assim, uma classe separada deve ser criada para tratar de cada responsabilidade de forma avulsa.

Diversos são os benefícios que o SRP pode trazer, como facilidade para reuso de código, diminuição do acoplamento, facilidade na manutenção de classes e implementação de testes e até mesmo benefícios no versionamento do código, imagine quão trágico seria três programadores alterando uma única classe que é responsável por três responsabilidades diferentes...

Tendo isso em vista, pode-se partir para um exemplo mais prático.

# Exemplo

## Código

Dado o código em Java:

```java
// ...
public class Email {

    public static void main(String[] args) {
        String[] data = readData();
        sendEmail(doQuery(), data[0], data[1]);
    }

    // faz a query no banco de dados para buscar os emails de todos os clientes
    public static List<String> doQuery() {
        Connection conn = DriverManager.getConnection("...");
        Statement stmt = conn.createStatement();
        String query = "SELECT email FROM CLIENTE WHERE ativo = True;";
        ResultSet rs = stmt.executeQuery(query);
        List<String> emailList;
        while (rs.next()) {
            // itera pelo resultado da query e popula a lista emailList
            emailList.add(rs.getString("email"));
        }
        return emailList;
    }

    public static boolean sendEmail(List<String> emailList, String subject, String text) {
        String from = "no-reply@system.com";
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            // define remetente
            message.setFrom(new InternetAddress(from));
            // adiciona os clientes como destinatários
            for (String email : emailList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            // seta título e texto
            message.setSubject(subject);
            message.setText(text);
            // envia os e-mails
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String[] readData() {
        Scanner in = new Scanner(System.in);
        System.out.print("Título: ");
        String subject = in.next();
        System.out.print("Texto: ");
        String text = in.next();
        return new String[]{subject, text};
    }
}
```
## Análise 

Pode-se perceber que o código tem um objetivo principal: enviar um e-mail para uma lista de pessoas. Entretanto, será que ele segue o SRP?
Podemos, para verificar isso, elencar as responsabilidades da classe do código:

 1. Receber dados do usuário (título e texto do e-mail);
 2. Fazer uma consulta no banco de dados pelos e-mails dos clientes;
 3. Enviar e-mail para os clientes.

Sendo assim, podemos afirmar que existe, no código acima, uma classe com três responsabilidades distintas. Dessa forma, pode-se concluir que esse projeto não respeita o SRP. Como poderíamos, então, adaptar esse projeto para seguir o padrão desejado?

## Quebrar responsabilidades em classes distintas

Seguindo o SRP, devemos criar uma classe para cada responsabilidade.

### Responsabilidade 1

Começando pela primeira responsabilidade, é possível criar uma classe separada para interagir com o usuário:

```java
public class GetUserData {

    private static Scanner in = new Scanner(System.in);

    public static String readTitulo() {
        System.out.print("Título: ");
        String subject = in.next();
        return subject;
    }

    public static String readText() {
        System.out.print("Texto: ");
        String text = in.next();
        return text;
    }

}
```

### Responsabilidade 2

A segunda responsabilidade diz respeito a fazer uma consulta em um banco de dados, afim de resgatar os e-mails dos clientes existentes. Pode-se, então, criar uma classe para tratar dos assuntos relacionados ao banco de dados:

```java
public class Database {

    private static Connection conn = DriverManager.getConnection("...");
    private static List<String> emailList;

    // faz a query no banco de dados para buscar os emails de todos os clientes
    public static void doRefreshQuery() {
        String query = "SELECT email FROM CLIENTE WHERE ativo = True;";
        ResultSet rs = stmt.executeQuery(query);
        emailList.clear();
        while (rs.next()) {
            // itera pelo resultado da query e popula a lista emailList
            emailList.add(rs.getString("email"));
        }
    }

    public static List<String> getEmailList() {
        doRefreshQuery();
        return emailList;
    }
}
```

### Responsabilidade 3

Para a terceira responsabilidade do projeto, é necessário enviar e-mail para a lista de e-mails que será provida por `Database.getEmailList()`. Assim como para as outras responsabilidades, deve-se criar uma classe para fazer as operações relacionadas ao envio de e-mail:

```java
public class Email {

    private static final String from = "no-reply@system.com";
    private static final String host = "localhost";

    public static boolean sendEmail(List<String> emailList, String subject, String text) {

        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            // define remetente
            message.setFrom(new InternetAddress(from));
            // adiciona os clientes como destinatários
            for (String email : emailList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            // seta título e texto
            message.setSubject(subject);
            message.setText(text);
            // envia os e-mails
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

### Final

Por fim, pode-se criar uma classe que una todas as outras classes para cumprir com o objetivo do projeto:

```java
public class SRP {

    public static void main(String[] args) {
        Email.sendEmail(
                emailList = Database.getEmailList(),
                subject = GetUserData.readTitulo(),
                text = GetUserData.readText());
    }
    
}
```

Dessa forma, agora o projeto está organizado em quatro classes, sendo que cada uma delas são responsáveis por partes distintas do mesmo. Quando uma atualização precisar ser feita na implementação do método de envio de e-mail, por exemplo, pode-se buscar pelo módulo (classe) responsável pela tarefa e atualizá-lo de forma a minimizar as alterações nos módulos restantes. Têm-se, então, módulos separados de acordo com suas responsabilidades/tarefas.

Conclue-se, portanto, que esse novo projeto respeita o SRP.
