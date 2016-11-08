/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evp.service;

import com.evp.model.FileStats;
import com.evp.model.LineStats;
import com.evp.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Егор
 */
@javax.ejb.Stateless
@Path("com.evp.model.linestats")
public class LineStatsFacadeREST
{

    public LineStatsFacadeREST()
    {

    }

    @GET
    @Produces(
            {
                "application/xml", "application/json"
            })
    public List<LineStats> findAll()
    {
        List<LineStats> list = null;
        try
        {
            Session session = HibernateUtil.getSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from LineStats");
            list = (List<LineStats>) q.list();
            tx.commit();
            return list;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("{file_id}")
    @Produces(
            {
                "application/xml", "application/json"
            })
    public List<LineStats> find(@PathParam("file_id") Integer file_id)
    {
        List<LineStats> list = new ArrayList<>();
        try
        {
            Session session = HibernateUtil.getSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from LineStats as lineStats where lineStats.fileStats.fileId=" + file_id);
            list = q.list();
            tx.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        } 
        return list;
    }
}
