package Hibernate;

import Objektai.Company;
import Objektai.Project;
import Objektai.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static FXController.FxMain.passwordSalt;

public class CompanyHibernateController {
    public CompanyHibernateController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String bCryptedPassword = bCryptPasswordEncoder.encode(company.getPass() + passwordSalt);
            company.setPass(bCryptedPassword);

            em.getTransaction().begin();
            em.persist(company);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompany(company.getId()) != null) {
                throw new Exception("Company " + company + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Company company) throws  Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String bCryptedPassword = bCryptPasswordEncoder.encode(company.getPass() + passwordSalt);
            company.setPass(bCryptedPassword);

            em.getTransaction().begin();
            em.flush();
            company = em.merge(company);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = company.getId();
                if (findCompany(id) == null) {
                    throw new Exception("The company with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getId();
                for(User u:company.getUsers()){
                    u.removeCompany(company);
                }

                for(Project p:company.getProjects()){
                    p.removeCompany(company);
                }
                em.merge(company);
            } catch (EntityNotFoundException enfe) {
                throw new Exception("The company with id " + id + " no longer exists.", enfe);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Company.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);

            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Company findCompany(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }

    public Company findCompany(String login) {
        EntityManager em = getEntityManager();
        for(Company c:findCompanyEntities()){
            if(c.getLogin().equals(login)) return c;
        }
        em.close();
        return null;
    }

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Company> rt = cq.from(Company.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void AddUserToCompany(Company company, User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                company.addUser(user);
                user.addCompany(company);

                em.merge(company);
                em.merge(user);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("error", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void AddProjectToCompany(Company company, Project project) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                company.addProject(project);
                project.addCompany(company);

                em.merge(project);
                em.merge(company);
                em.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void removeUserFromCompany(Company company, User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                company.getUsers().remove(user);
                user.getCompanies().remove(company);

                em.merge(company);
                em.merge(user);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("error", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeProjectFromCompany(Company company, Project project) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            try {
                company.removeProject(project);
                project.removeCompany(company);

                em.merge(project);
                em.merge(company);
                em.flush();

            } catch (EntityNotFoundException enfe) {
                throw new Exception("error", enfe);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
