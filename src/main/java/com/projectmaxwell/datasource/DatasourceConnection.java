package com.projectmaxwell.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties; 

import com.projectmaxwell.exception.MaxwellException;

public class DatasourceConnection {   
	
	private static DataSource datasource = null;     
	
	DatasourceConnection() {    }     
	
	public static Connection getConnection() throws WebApplicationException {        
		try {            
			if (datasource == null) {  
				DatasourceConnection.initDatasource();        
			}
			if(datasource.getConnection() == null){
				throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(
						new MaxwellException("1234blah", "NO_CONNECTION", "Could not get connection"))
						.build());
			}
			return datasource.getConnection();        
		} catch (SQLException e) {            
			throw new WebApplicationException(e);        
		}    
	} 
	
	public static void initDatasource() {         
		PoolProperties p = new PoolProperties();         
		p.setUrl("");        
		p.setDriverClassName("com.mysql.jdbc.Driver");        
		p.setUsername("");        
		p.setPassword("");        
		p.setJmxEnabled(true);       
		p.setTestWhileIdle(false);        
		p.setTestOnBorrow(true);        
		p.setValidationQuery("SELECT 1");    
		p.setTestOnReturn(false);     
		p.setValidationInterval(30000);      
		p.setTimeBetweenEvictionRunsMillis(30000);    
		p.setMaxActive(75);      
		p.setMaxIdle(75);      
		p.setInitialSize(10);     
		p.setMaxWait(10000);      
		p.setRemoveAbandonedTimeout(60);     
		p.setMinEvictableIdleTimeMillis(30000);     
		p.setMinIdle(10);     
		p.setLogAbandoned(true);           
		p.setRemoveAbandoned(true);    
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"             
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");        
		datasource = new DataSource();      
		datasource.setPoolProperties(p);
	} 
	
	public static void closeDatasource() {      
		datasource.close();   
	}
}
