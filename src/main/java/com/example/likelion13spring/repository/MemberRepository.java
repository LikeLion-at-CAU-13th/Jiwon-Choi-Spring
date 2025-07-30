package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//이 클래스가 스프링이 관리하는 빈이라는 걸 알려줌
@Repository
public class MemberRepository {

    //entity매니저 주입을 위한 어노테이션
    @PersistenceContext
    private EntityManager em; //DB 조작을 위한 객체

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //jpql임
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> result = em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
        return result.stream().findFirst();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("SELECT m FROM Member m WHERE m.name = :=name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
    //위와 동일한 코드임!
    public interface MemberRepository extends JpaRepository<Member, Long> {
        Optional<Member> findByName(String name);

    }//Spring Data JPA가 자동으로 인식해서 내부적으로 JPQL 쿼리를 생성해준다!

    public void delete(Member member) {
        em.remove(member);
    }
}
