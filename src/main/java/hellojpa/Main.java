package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory emf = 
				Persistence.createEntityManagerFactory("hello");
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		try {
			
			Team team = new Team();
			team.setName("TeamA");
			em.persist(team);
			
			Member member = new Member();
			member.setName("hello");
			em.persist(member);
			
			team.getMembers().add(member);
			member.setTeam(team);
			
			em.flush();
			em.clear();
			
			String jpql = "select m from Member m join fetch m.team";
			
			List<Member> members = em.createQuery(jpql, Member.class)
					.setFirstResult(10)
					.setMaxResults(20)
					.getResultList();
			
			em.close();
			
			for(Member member1 : members) {
				System.out.println("username = " + member1.getName());
				System.out.println("teamname = " + member1.getTeam().getName());
			}
			
			
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
		
	}
}
