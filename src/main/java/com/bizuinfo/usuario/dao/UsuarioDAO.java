package com.bizuinfo.usuario.dao;

import com.bizuinfo.acesso.util.JPAutil;
import com.bizuinfo.usuario.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioDAO {

    public Optional<Usuario> buscarPorEmail(String email) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            Usuario u = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email",
                            Usuario.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.of(u);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            return Optional.ofNullable(em.find(Usuario.class, id));
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {

        EntityManager em = JPAutil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u ORDER BY u.id",
                    Usuario.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public void salvar(Usuario usuario) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            em.getTransaction().begin();

            Usuario managed = em.merge(usuario);

            em.flush();

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public void remover(Long id) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            em.getTransaction().begin();

            Usuario u = em.find(Usuario.class, id);

            if (u != null) {
                em.remove(u);
            }

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public Optional<Usuario> buscarPorToken(String token) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            Usuario u = em.createQuery(
                            """
                            SELECT u
                            FROM Usuario u
                            WHERE u.tokenVerificacao = :token
                               OR u.tokenReset = :token
                            """,
                            Usuario.class
                    )
                    .setParameter("token", token)
                    .getSingleResult();

            return Optional.of(u);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}