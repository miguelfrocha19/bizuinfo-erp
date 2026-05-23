package com.bizuinfo.usuario.dao;

import com.bizuinfo.acesso.util.JPAutil;
import com.bizuinfo.usuario.model.LogAuditoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class LogAuditoriaDAO {

    public void salvar(LogAuditoria log) {

        EntityManager em = JPAutil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(log);

            em.getTransaction().commit();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {
            em.close();
        }
    }

    public List<LogAuditoria> listarTodos() {

        EntityManager em = JPAutil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT l FROM LogAuditoria l ORDER BY l.dataHora DESC",
                    LogAuditoria.class
            ).getResultList();

        } finally {
            em.close();
        }
    }
}