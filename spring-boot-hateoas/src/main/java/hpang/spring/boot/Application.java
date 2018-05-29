package hpang.spring.boot;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

@RestController
class GreetingController{
	/* Don't define default path here, otherwise it will not redirect to Hal browser */
	//@RequestMapping("/")
	Greet greet(){
		return new Greet("Hello World");
	}
	
	/*To Use Hal browser(http://localhost:8080/browser/index.html#)
	 * http://localhost:8080/browser/index.html#/greeting?name=pang
	 * 
	 * don't implement "/", ohterwise it cannot to redirect to  Hal Browser:
	 * 
	 * http://localhost:8080/greeting?name=Pang
	 * 
	 * {
  "message" : "Hello Pang",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/greeting?name=Pang"
    }
  }
}
	 */
	@RequestMapping("/greeting")
	@ResponseBody
	public HttpEntity<Greet> greeting(@RequestParam(value="name",
			required=false, defaultValue="HATEOAS") String name) {
		
		Greet greet = new Greet("Hello " + name);
		
		greet.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
		return new ResponseEntity<Greet>(greet, HttpStatus.OK);
	}
	
}

/*ResourceSupport for Hateoas adding link */
class Greet extends ResourceSupport {
	private String message;
	
	public Greet(){}
	public Greet(String message){
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
