package hpang.springboot.rest.membership;

import hpang.springboot.entity.membership.GymMembership;
import hpang.springboot.entity.person.Person;
import hpang.springboot.repository.membership.GymMembershipRepository;
import hpang.springboot.repository.membership.exception.GymMembershipNotFoundException;
import hpang.springboot.respository.person.PersonRepository;
import hpang.springboot.respository.person.exception.PersonNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/*
 * In order to return hateoas, make sure to add produces = "application/hal+json" 
 */
@RestController
@RequestMapping(value ="/people/{personId}/memberships", produces = "application/hal+json")
public class GymMembershipController {

  private PersonRepository personRepository;

  private GymMembershipRepository gymMembershipRepository;

  public GymMembershipController(
      final PersonRepository personRepository,
      final GymMembershipRepository gymMembershipRepository) {
    this.personRepository = personRepository;
    this.gymMembershipRepository = gymMembershipRepository;
  }

  @GetMapping
  public ResponseEntity<Resources<GymMembershipResource>> all(@PathVariable final long personId) {
    final List<GymMembershipResource> collection = getMembershipsForPerson(personId);
    final Resources<GymMembershipResource> resources = new Resources<>(collection);
	 //final Resources<GymMembershipResource> resources = new Resources<GymMembershipResource>(new ArrayList<GymMembershipResource>());
    final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    resources.add(new Link(uriString, "self"));
    return ResponseEntity.ok(resources);
  }

  private List<GymMembershipResource> getMembershipsForPerson(final long personId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .map(GymMembershipResource::new)
                    .collect(Collectors.toList()))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private void validatePerson(final long personId) {
    personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId));
  }

  @GetMapping("/{membershipId}")
  public ResponseEntity<GymMembershipResource> get(
      @PathVariable final long personId, @PathVariable final long membershipId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .filter(m -> m.getId().equals(membershipId))
                    .findAny()
                    .map(m -> ResponseEntity.ok(new GymMembershipResource(m)))
                    .orElseThrow(() -> new GymMembershipNotFoundException(membershipId)))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  @PostMapping
  public ResponseEntity<GymMembershipResource> post(
      @PathVariable final long personId, @RequestBody final GymMembership inputMembership) {
    return personRepository
        .findById(personId)
        .map(
            p -> {
              final GymMembership membership = saveMembership(p, inputMembership);
              final URI uri = createPostUri(membership);
              return ResponseEntity.created(uri).body(new GymMembershipResource(membership));
            })
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private GymMembership saveMembership(final Person person, final GymMembership membership) {
    return gymMembershipRepository.save(
        new GymMembership(person, membership.getName(), membership.getCost()));
  }

  private URI createPostUri(final GymMembership membership) {
    return MvcUriComponentsBuilder.fromController(getClass())
        .path("/{membershipId}")
        .buildAndExpand(membership.getOwner().getId(), membership.getId())
        .toUri();
  }

  @PutMapping("/{membershipId}")
  public ResponseEntity<GymMembershipResource> put(
      @PathVariable final long personId,
      @PathVariable final long membershipId,
      @RequestBody final GymMembership inputMembership) {
    return personRepository
        .findById(personId)
        .map(
            p -> {
              final GymMembership membership = updateMembership(p, membershipId, inputMembership);
              final URI uri =
                  URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
              return ResponseEntity.created(uri).body(new GymMembershipResource(membership));
            })
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private GymMembership updateMembership(
      final Person person, final long id, final GymMembership membership) {
    return gymMembershipRepository.save(
        new GymMembership(id, person, membership.getName(), membership.getCost()));
  }

  @DeleteMapping("/{membershipId}")
  public ResponseEntity<?> delete(
      @PathVariable final long personId, @PathVariable final long membershipId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .filter(m -> m.getId().equals(membershipId))
                    .findAny()
                    .map(
                        m -> {
                          gymMembershipRepository.delete(m);
                          return ResponseEntity.noContent().build();
                        })
                    .orElseThrow(() -> new GymMembershipNotFoundException(membershipId)))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }
}
