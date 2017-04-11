package dbutil;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DbMigrationConfig;
import com.avaje.ebean.config.ServerConfig;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import models.Product;

public class DsHelper {
	EbeanServer serv;
	
	public DsHelper()
	{
		ServerConfig sc = new ServerConfig();
    	MysqlDataSource ds = new MysqlDataSource();
    	DbMigrationConfig dbmcfg = new DbMigrationConfig();
    	
    	ds.setUrl("jdbc:mysql://localhost:3306/playdb");
    	ds.setUser("root");
    	ds.setPassword("root");
    
    	sc.addClass(Product.class);
    	sc.setDataSource(ds);
    	sc.setRegister(true);
    	sc.setDefaultServer(true);
    	sc.setName("Ebeanus");
    	/*
    	sc.setDdlRun(true);
    	sc.setDdlGenerate(true);
    	*/
    	serv = EbeanServerFactory.create(sc);
    	
    	
	}
	
	
	public void generateDdl() {

	  System.setProperty("ddl.migration.generate", "true");

	  System.setProperty("ddl.migration.version", "1.1");
	  System.setProperty("ddl.migration.name", "support end dating");

	}
}
