/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evp.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Егор
 */
public class HibernateUtil
{

    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal();

    private static SessionFactory configureSessionFactory()
    {
        try
        {

            Configuration configuration = new Configuration().configure();
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry);
            return sessionFactory;
        } catch (HibernateException e)
        {
            System.out.append("** Exception in SessionFactory **");
            e.printStackTrace();
        }
        return sessionFactory;
    }

    static
    {
        try
        {
            sessionFactory = configureSessionFactory();
        } catch (Exception e)
        {
            System.err.println("%%%% Error Creating SessionFactory %%%%");
            e.printStackTrace();
        }
    }

    public static Session getSession() throws HibernateException
    {
        Session session = threadLocal.get();

        if (session == null || !session.isOpen())
        {
            if (sessionFactory == null)
            {
                rebuildSessionFactory();
            }
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
            threadLocal.set(session);
        }

        return session;
    }

    public static void rebuildSessionFactory()
    {
        try
        {
            sessionFactory = configureSessionFactory();
        } catch (Exception e)
        {
            System.err.println("%%%% Error Creating SessionFactory %%%%");
            e.printStackTrace();
        }
    }

    public static void closeSession() throws HibernateException
    {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null)
        {
            session.close();
        }
    }

    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
}
