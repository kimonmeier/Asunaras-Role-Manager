package me.lucky.roleManager.data.dao;

import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDAO<T extends Serializable> {

    private Class< T > clazz;
    protected SessionFactory sessionFactory;

    public AbstractDAO(SessionFactory sessionFactory, Class< T > clazz) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    public T findOne(long id){
        var session = sessionFactory.openSession();

        try {
            return session.find(clazz, id);
        } finally {
            session.close();
        }
    }

    public List<T> findAll(){
        var session = sessionFactory.openSession();

        try {
            return session.createQuery("from " + clazz.getName())
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public void create(T entity){
        var session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public T update(T entity){
        var session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            var updatedEntity = session.merge(entity);
            session.getTransaction().commit();

            return updatedEntity;
        } finally {
            session.close();
        }
    }

    public void delete(T entity){
        var session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteById(long entityId){
        T entity = findOne(entityId);
        delete(entity);
    }
}
