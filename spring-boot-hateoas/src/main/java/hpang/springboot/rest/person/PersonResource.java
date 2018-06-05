package hpang.springboot.rest.person;

import hpang.springboot.entity.person.Person;
import hpang.springboot.rest.membership.GymMembershipController;
import lombok.Getter;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class PersonResource extends ResourceSupport {

  private final Person person;

  public PersonResource(final Person person) {
    this.person = person;
    final long id = person.getId();
    add(linkTo(PersonController.class).withRel("people"));
    add(linkTo(methodOn(GymMembershipController.class).all(id)).withRel("memberships"));
    add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
  }
}
