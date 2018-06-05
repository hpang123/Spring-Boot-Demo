package hpang.springboot.repository.membership;

import hpang.springboot.entity.membership.GymMembership;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {}
