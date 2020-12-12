package soa.eip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";

  @Override
  public void configure() {
    from(DIRECT_URI)
      .log("Body contains \"${body}\"")
      .log("Searching twitter for \"${body}\"!")
      .process(new analyceString())
      .toD("twitter-search:${body}")
      .log("Body now contains the response from twitter:\n${body}");
  }

  public class analyceString implements Processor{
    public void process (Exchange exchange) throws Exception{

      String[] queryParsed = exchange.getIn().getBody(String.class).split(" ");
      String finalQuery = "";
      String max = "";

      for (String i : queryParsed){
        if (i.startsWith("max:")) {
          String[] takeMax = i.split(":");
          max = takeMax[1];      
        } else {
          finalQuery += i + " ";
        }
      }
      finalQuery += "?count=" + max;
    //  System.out.println(finalQuery + " with max: " + max);
      exchange.getIn().setBody(finalQuery);
    }
  }
}