package com.otess.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.otess.common.bean.DBType;
/**
 * @author jinweida
 */
public final  class JDBCUtils {
	private String url = null;
    private String user = null;
    private String driver = null;
    private String password = null;
     
    public JDBCUtils (Integer dbtype,String dbaddress,
			String dbport, String dbusername,
			String dbpassword, String dbname) {
    	switch(dbtype){
			case DBType.MSSQL:
				driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
				url="jdbc:sqlserver://"+dbaddress+":"+dbport+"; DatabaseName="+dbname+";";
				//url="jdbc:sqlserver://"+dbaddress+":"+dbport+";databaseName="+dbname+";IntegratedSecurity=True";
				user=dbusername;
				password=dbpassword;
				break;
			case DBType.MYSQL:
				driver="com.mysql.jdbc.Driver";
				url=("jdbc:mysql://"+dbaddress+":"+dbport+"/"+dbname+"?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
				user=dbusername;
				password=dbpassword;				
				break;
			case DBType.ORACLE:
				//cpd.setDriverClass("com.mysql.jdbc.Driver");
				driver="oracle.jdbc.driver.OracleDriver";
				url="jdbc:oracle:thin:@//"+dbaddress+":"+dbport+"/"+dbname;
				user=dbusername;
				password=dbpassword;
				break;
			case DBType.SYBASE:
				//cpd.setDriverClass("com.mysql.jdbc.Driver");
				break;
			case DBType.ACCESS:
				//cpd.setDriverClass("com.mysql.jdbc.Driver");
				break;
		}
        try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    private static JDBCUtils instance = null;
 
    public static JDBCUtils getInstance(Integer dbtype,String dbaddress,
			String dbport, String dbusername,
			String dbpassword, String dbname) {
    	System.out.println(instance);
        /*if (instance == null) {
            synchronized (JDBCUtils.class) {
                if (instance == null) {
                    instance = new JDBCUtils(dbtype,dbaddress,dbport, dbusername,dbpassword, dbname);
                }
 
            }
        } */
        instance = new JDBCUtils(dbtype,dbaddress,dbport, dbusername,dbpassword, dbname);
        return instance;
    }
     
    //该方法获得连接
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
     
    //释放资源
    public void free(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                  
                e.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                          
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                  
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
	/*private static ComboPooledDataSource cpd;
	private static Connection conn;
	private static Statement st;
		
	public JDBCUtils(Integer dbtype,String dbaddress,
			String dbport, String dbusername,
			String dbpassword, String dbname) throws Exception{
		try {
			cpd=new ComboPooledDataSource();
			switch(dbtype){
				case DBType.MSSQL:
					cpd.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					break;
				case DBType.MYSQL:
					cpd.setDriverClass("com.mysql.jdbc.Driver");
					cpd.setJdbcUrl("jdbc:mysql://"+dbaddress+":"+dbport+"/"+dbname+"?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");  
					cpd.setUser(dbusername);  
					cpd.setPassword(dbpassword);  
					break;
				case DBType.DB2:
					cpd.setDriverClass("com.mysql.jdbc.Driver");
					break;
				case DBType.SYBASE:
					cpd.setDriverClass("com.mysql.jdbc.Driver");
					break;
				case DBType.ACCESS:
					cpd.setDriverClass("com.mysql.jdbc.Driver");
					break;
			}
			cpd.setMinPoolSize(3); 
			//当连接池中的连接耗尽的时候c3p0一次同时获取的连接数 Default: 3 acquireIncrement  
			cpd.setAcquireIncrement(15);  
			//连接池中保留的最大连接数。Default: 15 maxPoolSize   
			cpd.setMaxPoolSize(10);  
			cpd.setMaxStatements(0);
			//最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃
			cpd.setMaxIdleTime(60);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
	}  
	public synchronized void close()  
    {  
        try  
        {  
            DataSources.destroy(cpd);  
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
    } 
	@SuppressWarnings({ "rawtypes", "rawtypes" })
	public List ResultSetToList(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData md = rs.getMetaData();    // 获得结果集结构信息（字段数、字段名等）
            int columnCount = md.getColumnCount();
            List list = new ArrayList();
            while (rs.next()) {
                Map rowData = new HashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally{
        	if (rs != null) rs.close();
        	if (st != null) st.close();
        	if (conn != null) conn.close();
        }
    }
	*//**
	 * 查询
	 * @param sql
	 * @return
	 * @throws SQLException 
	 *//*
	public ResultSet executeQuery(String sql) throws SQLException{
        ResultSet rs = null;  
        try  
        {  
        	conn = cpd.getConnection();  
        	st= conn.createStatement(  
                    ResultSet.TYPE_SCROLL_INSENSITIVE,  
                    ResultSet.CONCUR_READ_ONLY);  
            rs = st.executeQuery(sql);              
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return rs; 
	}
	*//**
	 * 获取全部表
	 * @return
	 * @throws SQLException 
	 *//*
	public ResultSet executeQueryTables() throws SQLException{ 
        ResultSet rs = null;  
        try  
        {  
        	Connection conn = cpd.getConnection();              
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), "root", null, new String[]{"TABLE"});
        }  
        catch (SQLException e)  
        {  
        	throw new SQLException(e.getMessage()); 
        }
        return rs; 
	}
	public ResultSet executeQueryColumns(String tableName) throws SQLException{ 
        ResultSet rs = null;  
        try  
        {  
        	conn = cpd.getConnection();              
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(null, "%", tableName, "%");
        }  
        catch (SQLException e)  
        {  
            e.printStackTrace();  
        }  
        return rs; 
	}
	public static void main(String[] args){
		JDBCUtils db;
		try {
			db = new JDBCUtils(DBType.MYSQL,"123.57.3.145","3306","root","Otess  008","otess");
			System.out.println(JsonKit.toJson(db.ResultSetToList(db.executeQueryColumns("mp_task"))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
