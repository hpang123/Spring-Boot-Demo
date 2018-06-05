package hpang.springboot.entity.membership;

import hpang.springboot.entity.person.Person;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "memberships")
public class GymMembership {

  @Id @GeneratedValue private Long id;

  @JsonIgnore @ManyToOne private Person owner;

  private String name;

  private long cost;

  public GymMembership(final Person owner, final String name, final long cost) {
    this.owner = owner;
    this.name = name;
    this.cost = cost;
  }
}
