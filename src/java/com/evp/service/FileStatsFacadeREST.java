/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evp.service;

import com.evp.model.FileStats;
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
@Path("com.evp.model.filestats")
public class FileStatsFacadeREST
{

 

    public FileStatsFacadeREST()
    {

    }

    @GET
    @Produces(
            {
                "application/xml", "application/json"
            })
    public List<FileStats> findAll()
    {
        List<FileStats> list = null;
        try
        {
            Session session = HibernateUtil.getSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from FileStats");
            list = q.list();
            tx.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            HibernateUtil.closeSession();
        }
        return list;
    }

    @GET
    @Path("{min_lines_count}")
    @Produces(
            {
                "application/xml", "application/json"
            })
    public List<FileStats> find(@PathParam("min_lines_count") Integer min_lines_count)
    {
        List<FileStats> list = new ArrayList<>();
        try
        {
            Session session = HibernateUtil.getSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from FileStats as fileStats where size(fileStats.linesStats)>" + (min_lines_count - 1));
            list = q.list();
            tx.commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            HibernateUtil.closeSession();
        }
        return list;
    }

}
