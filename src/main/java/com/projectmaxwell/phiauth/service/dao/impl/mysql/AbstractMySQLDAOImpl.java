package com.projectmaxwell.phiauth.service.dao.impl.mysql;

import java.sql.Connection;

import javax.ws.rs.WebApplicationException;

import com.projectmaxwell.datasource.DatasourceConnection;
import com.projectmaxwell.phiauth.service.dao.AbstractDAOImpl;

public class AbstractMySQLDAOImpl extends AbstractDAOImpl{

		protected Connection con;
		
		public AbstractMySQLDAOImpl() throws WebApplicationException{
			super();
			try{
				con = DatasourceConnection.getConnection();
			}catch(Exception e){
				throw new WebApplicationException(e);
			}
		}
		
		public void releaseConnection() throws WebApplicationException{
			try{
				con.close();
				con = null;
			}catch(Exception e){
				throw new WebApplicationException(e);
			}
		}
	}
