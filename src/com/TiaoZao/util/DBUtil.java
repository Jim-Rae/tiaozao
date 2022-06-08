package com.TiaoZao.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBUtil {

private static String driverName = null;
private static String url = null;
private static String dateName = null;
private static String sqlSetting = null;
private static String user = null;
private static String pwd = null;

/**
* 锟斤拷取锟斤拷锟斤拷锟侥硷拷锟斤拷为锟斤拷始锟斤拷锟斤拷员锟斤拷锟斤拷
*/
		static{
		InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("mysqlconfig.properties");
		Properties properties = new Properties();
		try {
			if(is != null)
			properties.load(is);
		} catch (IOException e) {
		e.printStackTrace();
		}
		driverName = properties.getProperty("driverName");
		url = properties.getProperty("url");
		user = properties.getProperty("user");
		pwd = properties.getProperty("pwd");
		}

/**
* 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷
*/
static{
try {
Class.forName(driverName);
} catch (ClassNotFoundException e) {
throw new ExceptionInInitializerError("锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷");
}
}

/**
* 锟斤拷取锟斤拷锟捷匡拷锟斤拷锟斤拷
* @return 锟斤拷锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟�
*/
public static Connection getConnection(){
try {
return DriverManager.getConnection(url, user, pwd);
} catch (SQLException e) {
e.printStackTrace();
}
return null;
}

/**
* 锟截憋拷锟斤拷锟捷匡拷锟斤拷锟斤拷
* @param rs 要锟截闭斤拷锟斤拷锟斤拷锟斤拷锟�
* @param pstat 要锟截闭碉拷预锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
* @param conn 要锟截闭碉拷锟斤拷锟捷匡拷锟斤拷锟斤拷
*/
public static void close(ResultSet rs,PreparedStatement pstat,Connection conn){
try {
if(rs != null)
rs.close();
} catch (SQLException e) {
e.printStackTrace();
}finally{
try {
if(pstat != null)
pstat.close();
} catch (SQLException e) {
e.printStackTrace();
}finally{
try {
if(conn != null)
conn.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
}
}
}

//然锟斤拷锟斤拷欠锟斤拷捅锟斤拷锟斤拷删锟侥诧拷锟洁：
 class DBDao<T> {

/**
* 锟斤拷锟斤拷锟斤拷锟斤拷锟絫锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷锟捷匡拷锟斤拷
* 
* @param t 要锟斤拷锟芥到锟斤拷锟捷匡拷亩锟斤拷锟�
*/
public static <T> void insert(T t) {
// 锟斤拷取锟斤拷锟斤拷t锟斤拷class锟斤拷锟斤拷
@SuppressWarnings("unchecked")
Class<T> cla = (Class<T>) t.getClass();
// 锟斤拷取锟斤拷锟斤拷t锟斤拷锟斤拷锟斤拷锟街讹拷
Field[] fields = cla.getDeclaredFields();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷哦锟斤拷锟絫锟斤拷锟街段憋拷锟斤拷锟斤拷
List<String> keys = new ArrayList<String>();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷哦锟斤拷锟絫锟斤拷锟街段碉拷值
List<Object> values = new ArrayList<Object>();
// 锟斤拷锟斤拷Method锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街段碉拷get锟斤拷锟斤拷
Method method = null;
// 锟斤拷锟斤拷Object锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街讹拷值
Object obj = null;
// 锟斤拷锟斤拷侄锟斤拷锟斤拷椴晃拷眨锟斤拷锟斤拷锟斤拷锟斤拷锟絫锟斤拷锟街讹拷锟斤拷锟斤拷
if (fields != null && fields.length > 0) {
for (Field field : fields) {
// 锟斤拷锟斤拷锟斤拷侄尾锟斤拷锟絀D锟街段ｏ拷锟酵憋拷锟芥到锟街讹拷锟叫憋拷锟斤拷
if (!field.getName().equals("id")) {
keys.add(field.getName());
try {
// 锟斤拷取锟斤拷锟街段讹拷应锟斤拷get锟斤拷锟斤拷
method = cla.getDeclaredMethod(getMethodName(field
.getName()));
} catch (NoSuchMethodException e) {
e.printStackTrace();
} catch (SecurityException e) {
e.printStackTrace();
}
try {
// 执锟叫革拷锟街段碉拷get锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷值
obj = method.invoke(t);
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
// 锟斤拷锟斤拷锟截的斤拷锟斤拷锟斤拷娴斤拷侄锟街碉拷斜锟斤拷锟�
values.add(obj);
}
}
}
// 锟斤拷拼sql锟斤拷锟�
StringBuffer sql = new StringBuffer("insert into "
+ cla.getName().substring(cla.getName().lastIndexOf(".") + 1)
+ "(");
StringBuffer sqlValues = new StringBuffer("values(");
for (int i = 0; i < keys.size() - 1; i++) {
sql.append(keys.get(i) + ",");
sqlValues.append("?,");
}
sql.append(keys.get(keys.size() - 1) + ") ");
sqlValues.append("?)");
sql.append(sqlValues);
Connection conn = null;
PreparedStatement pstat = null;
try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
for (int i = 0; i < values.size(); i++) {
pstat.setObject(i + 1, values.get(i));
}
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
}
DBUtil.close(null, pstat, conn);
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷Class锟斤拷锟斤拷锟絠d锟斤拷询锟斤拷应锟侥斤拷锟�
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
* @param id 锟斤拷锟斤拷锟斤拷id
* @return 锟斤拷锟截诧拷询锟斤拷锟斤拷锟斤拷应锟斤拷锟斤拷亩锟斤拷锟�
*/
public static <T> T select(Class<T> cla, int id) {
// 锟斤拷锟斤拷SQL锟斤拷锟�
String sql = "select * from "
+ cla.getName().substring(cla.getName().lastIndexOf(".") + 1)
+ " where id = ?";
// 锟斤拷取锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫的凤拷锟斤拷
Method[] methods = cla.getDeclaredMethods();
Connection conn = null;
PreparedStatement pstat = null;
ResultSet rs = null;
T t = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql);
pstat.setInt(1, id);
// 锟斤拷取锟斤拷询锟斤拷锟�
rs = pstat.executeQuery();
// 锟斤拷取锟斤拷询锟斤拷锟斤拷锟斤拷械母锟斤拷锟斤拷械锟斤拷锟斤拷锟�
ResultSetMetaData rsmd = pstat.getMetaData();
// 锟斤拷取锟斤拷询锟斤拷锟斤拷锟斤拷锟斤拷械母锟斤拷锟�
int columnNum = rsmd.getColumnCount();
// 锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟脚斤拷锟斤拷锟斤拷械锟斤拷锟斤拷锟�
String[] columnNames = new String[columnNum];
// 锟斤拷取锟斤拷锟斤拷锟斤拷懈锟斤拷械锟斤拷锟斤拷锟斤拷锟斤拷锟脚碉拷锟斤拷锟斤拷锟斤拷
for (int i = 0; i < columnNum; i++) {
columnNames[i] = rsmd.getColumnName(i + 1);
}
if (rs.next()) {
t = cla.newInstance();
for (String columnName : columnNames) {
// 锟斤拷取锟斤拷锟斤拷锟斤拷懈锟斤拷卸锟接︼拷锟絪et锟斤拷锟斤拷锟斤拷
String cName = setMethodName(columnName);
// 锟斤拷锟捷凤拷锟斤拷锟斤拷锟斤拷取锟斤拷锟斤拷
for (int i = 0; i < methods.length; i++) {
if (cName.equals(methods[i].getName())) {
methods[i].invoke(t, rs.getObject(columnName));
break;
}
}
}
}
} catch (Exception e) {
e.printStackTrace();
} finally {
DBUtil.close(rs, pstat, conn);
}
return t;
}


/**
* 锟斤拷锟捷革拷锟斤拷锟侥讹拷锟斤拷锟絠d锟斤拷锟斤拷锟斤拷锟斤拷
* 
* @param t 锟斤拷锟斤拷锟侥讹拷锟斤拷
* @param id 锟斤拷锟斤拷锟斤拷id
*/
public static <T> void update(T t, int id) {
// 锟斤拷取锟斤拷锟斤拷t锟斤拷class锟斤拷锟斤拷
@SuppressWarnings("unchecked")
Class<T> cla = (Class<T>) t.getClass();
// 锟斤拷取t锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷锟街讹拷
Field[] fields = cla.getDeclaredFields();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷锟絫锟斤拷锟斤拷锟叫碉拷锟街讹拷锟斤拷锟斤拷ID锟斤拷锟解）
List<String> keys = new ArrayList<String>();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷锟絫锟斤拷锟斤拷锟叫碉拷锟街讹拷值锟斤拷ID锟斤拷锟解）
List<Object> values = new ArrayList<Object>();
// 锟斤拷锟斤拷Method锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街段碉拷get锟斤拷锟斤拷
Method method = null;
// 锟斤拷锟斤拷Object锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街讹拷值
Object obj = null;
// 锟斤拷锟斤拷侄锟斤拷锟斤拷椴晃拷眨锟斤拷锟斤拷锟斤拷锟斤拷锟絫锟斤拷锟街讹拷锟斤拷锟斤拷
if (fields != null && fields.length > 0) {
for (Field field : fields) {
// 锟斤拷锟斤拷锟斤拷侄尾锟斤拷锟絀D锟街段ｏ拷锟酵憋拷锟芥到锟街讹拷锟叫憋拷锟斤拷
if (!field.getName().equals("id")) {
keys.add(field.getName());
try {
// 锟斤拷取锟斤拷锟街段讹拷应锟斤拷get锟斤拷锟斤拷
method = cla.getDeclaredMethod(getMethodName(field
.getName()));
} catch (NoSuchMethodException e) {
e.printStackTrace();
} catch (SecurityException e) {
e.printStackTrace();
}
try {
// 执锟叫革拷锟街段碉拷get锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷值
obj = method.invoke(t);
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
// 锟斤拷锟斤拷锟截的斤拷锟斤拷锟斤拷娴斤拷侄锟街碉拷斜锟斤拷锟�
values.add(obj);
}
}
}


// 拼锟斤拷SQL锟斤拷锟�
String table = t.getClass().getName()
.substring(t.getClass().getName().lastIndexOf(".") + 1);
StringBuffer sql = new StringBuffer("update " + table + " set ");
for (int i = 0; i < keys.size() - 1; i++) {
sql.append(keys.get(i) + " = ? ,");
}
sql.append(keys.get(keys.size() - 1) + " = ? where id = ?");


// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
// 为要执锟叫碉拷SQL锟斤拷锟斤拷锟斤拷貌锟斤拷锟�
for (int i = 0; i < values.size(); i++) {
pstat.setObject(i + 1, values.get(i));
}
pstat.setInt(values.size() + 1, id);
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
} finally {
DBUtil.close(null, pstat, conn);
}
}


/**
* 锟斤拷锟捷革拷锟斤拷锟侥讹拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟捷革拷锟斤拷锟斤拷锟斤拷
* 
* @param t 锟斤拷锟斤拷锟侥讹拷锟斤拷
* @param where 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
* @param value 锟斤拷锟斤拷锟斤拷值
*/
public static <T> void update(T t, String where, Object[] value) {


// 锟斤拷取锟斤拷锟斤拷t锟斤拷class锟斤拷锟斤拷
@SuppressWarnings("unchecked")
Class<T> cla = (Class<T>) t.getClass();
// 锟斤拷取t锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷锟街讹拷
Field[] fields = cla.getDeclaredFields();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷锟絫锟斤拷锟斤拷锟叫碉拷锟街讹拷锟斤拷锟斤拷ID锟斤拷锟解）
List<String> keys = new ArrayList<String>();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节达拷锟絫锟斤拷锟斤拷锟叫碉拷锟街讹拷值锟斤拷ID锟斤拷锟解）
List<Object> values = new ArrayList<Object>();
// 锟斤拷锟斤拷Method锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街段碉拷get锟斤拷锟斤拷
Method method = null;
// 锟斤拷锟斤拷Object锟斤拷锟斤拷锟斤拷锟节斤拷锟斤拷锟街讹拷值
Object obj = null;
// 锟斤拷锟斤拷侄锟斤拷锟斤拷椴晃拷眨锟斤拷锟斤拷锟斤拷锟斤拷锟絫锟斤拷锟街讹拷锟斤拷锟斤拷
if (fields != null && fields.length > 0) {
for (Field field : fields) {
// 锟斤拷锟斤拷锟斤拷侄尾锟斤拷锟絀D锟街段ｏ拷锟酵憋拷锟芥到锟街讹拷锟叫憋拷锟斤拷
if (!field.getName().equals("id")) {
keys.add(field.getName());
try {
// 锟斤拷取锟斤拷锟街段讹拷应锟斤拷get锟斤拷锟斤拷
method = cla.getDeclaredMethod(getMethodName(field
.getName()));
} catch (NoSuchMethodException e) {
e.printStackTrace();
} catch (SecurityException e) {
e.printStackTrace();
}
try {
// 执锟叫革拷锟街段碉拷get锟斤拷锟斤拷锟斤拷锟斤拷锟秸凤拷锟斤拷值
obj = method.invoke(t);
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
// 锟斤拷锟斤拷锟截的斤拷锟斤拷锟斤拷娴斤拷侄锟街碉拷斜锟斤拷锟�
values.add(obj);
}
}
}


String table = t.getClass().getName()
.substring(t.getClass().getName().lastIndexOf(".") + 1);
StringBuffer sql = new StringBuffer("update " + table + " set ");
for (int i = 0; i < keys.size() - 1; i++) {
sql.append(keys.get(i) + " = ? ,");
}
sql.append(keys.get(keys.size() - 1) + " = ? ");
if (where != null && where.length() > 0) {
sql.append(where);
}
Connection conn = null;
PreparedStatement pstat = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
for (int i = 0; i < values.size(); i++) {
pstat.setObject(i + 1, values.get(i));
}
for (int i = 0, j = values.size(); i < value.length; i++, j++) {
pstat.setObject(j + 1, value[i]);
}
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
} finally {
DBUtil.close(null, pstat, conn);
}
}


/**
* 锟斤拷询锟斤拷锟叫斤拷锟�
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
* @return 锟斤拷锟斤拷锟斤拷锟叫的斤拷锟�
*/
public static <T> List<T> queryAll(Class<T> cla) {


// 锟斤拷锟斤拷SQL锟斤拷锟�
StringBuffer sql = new StringBuffer("select * from "+cla.getName().substring(cla.getName().lastIndexOf(".") + 1));
// 锟斤拷取cla锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷姆锟斤拷锟�
Method[] methods = cla.getDeclaredMethods();
// 锟斤拷锟斤拷锟叫憋拷锟斤拷锟节憋拷锟斤拷锟窖拷慕锟斤拷锟斤拷
List<T> listResult = new ArrayList<T>();
// 锟斤拷锟斤拷锟斤拷锟斤拷t锟斤拷锟节憋拷锟斤拷锟斤拷锟斤拷锟�
T t = null;


// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;
ResultSet rs = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
rs = pstat.executeQuery();
// 锟斤拷取锟斤拷询锟侥斤拷锟斤拷锟斤拷锟斤拷械锟斤拷锟斤拷锟斤拷锟较�
ResultSetMetaData rsmd = pstat.getMetaData();
// 锟斤拷取锟斤拷锟斤拷锟斤拷械锟斤拷械母锟斤拷锟�
int columnNum = rsmd.getColumnCount();
// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟节达拷沤锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷
String[] columnNames = new String[columnNum];
for (int i = 0; i < columnNum; i++) {
columnNames[i] = rsmd.getColumnName(i + 1);
}
// 锟斤拷锟斤拷锟斤拷锟斤拷锟�
while (rs.next()) {
try {
t = cla.newInstance();
} catch (InstantiationException e1) {
e1.printStackTrace();
} catch (IllegalAccessException e1) {
e1.printStackTrace();
}
for (String columnName : columnNames) {
// 锟斤拷锟斤拷锟街讹拷锟斤拷锟斤拷取锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷
String methodName = setMethodName(columnName);
for (int i = 0; i < methods.length; i++) {
// 锟斤拷锟斤拷锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟揭筹拷锟斤拷应锟斤拷set锟斤拷锟斤拷
if (methodName.equals(methods[i].getName())) {
try {
// 执锟斤拷锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷t锟斤拷锟斤拷锟斤拷锟斤拷值
methods[i].invoke(t, rs.getObject(columnName));
break;
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
}
}
}
// 锟斤拷锟斤拷锟斤拷锟斤拷锟侥讹拷锟斤拷锟斤拷拥锟街革拷锟斤拷锟斤拷斜锟斤拷锟�
listResult.add(t);
}
} catch (SQLException e) {
e.printStackTrace();
} finally {
// 锟截憋拷锟斤拷锟捷匡拷锟斤拷锟斤拷
DBUtil.close(rs, pstat, conn);
}
// 锟斤拷锟截斤拷锟斤拷斜锟�
return listResult;
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷询一锟斤拷锟斤拷锟斤拷
* @param cla 锟斤拷锟斤拷锟斤拷锟斤拷锟紺lass锟斤拷锟斤拷
* @param where 锟斤拷锟斤拷锟侥诧拷询锟斤拷锟斤拷
* @param value 锟斤拷锟斤拷锟侥诧拷询锟斤拷锟斤拷锟叫的诧拷锟斤拷值
* @return 锟斤拷锟截诧拷询锟斤拷锟侥斤拷锟�
*/
public static <T> T find(Class<T> cla, String where, Object[] value) {
// 锟斤拷锟絊QL锟斤拷锟�
StringBuffer sql = new StringBuffer("select * from "
+ cla.getName().substring(cla.getName().lastIndexOf(".") + 1)
+ " ");
if (where != null && where.length() > 0) {
sql.append(where);
}


// 锟斤拷取Class锟斤拷锟斤拷cla锟斤拷应锟斤拷锟斤拷锟叫凤拷锟斤拷
Method[] methods = cla.getDeclaredMethods();
// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;
ResultSet rs = null;
T t = null;
try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
// 锟斤拷锟斤拷SQL锟斤拷锟斤拷械牟锟斤拷锟�
for (int i = 0; i < value.length; i++) {
pstat.setObject(i + 1, value[i]);
}
// 锟斤拷取锟斤拷锟斤拷锟�
rs = pstat.executeQuery();
// 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷械锟斤拷锟斤拷锟斤拷锟较�
ResultSetMetaData rsmd = pstat.getMetaData();
// 锟斤拷取锟斤拷锟斤拷锟斤拷械锟斤拷械母锟斤拷锟�
int columnNum = rsmd.getColumnCount();
// 锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟节憋拷锟斤拷锟斤拷锟斤拷锟叫碉拷锟叫碉拷锟斤拷锟斤拷
String[] columnNames = new String[columnNum];
// 锟斤拷取锟斤拷锟斤拷锟斤拷械母锟斤拷锟斤拷械锟斤拷锟斤拷撇锟斤拷锟斤拷娴斤拷锟斤拷锟斤拷锟�
for (int i = 0; i < columnNum; i++) {
columnNames[i] = rsmd.getColumnName(i + 1);
}
// 锟斤拷锟斤拷锟斤拷锟斤拷锟�
if (rs.next()) {
try {
t = cla.newInstance();
} catch (InstantiationException e1) {
e1.printStackTrace();
} catch (IllegalAccessException e1) {
e1.printStackTrace();
}
for (String columnName : columnNames) {
// 锟斤拷锟斤拷锟街讹拷锟斤拷锟斤拷取锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷
String methodName = setMethodName(columnName);
for (int i = 0; i < methods.length; i++) {
// 锟斤拷锟斤拷锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟揭筹拷锟斤拷应锟斤拷set锟斤拷锟斤拷
if (methodName.equals(methods[i].getName())) {
try {
// 执锟斤拷锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷t锟斤拷锟斤拷锟斤拷锟斤拷值
methods[i].invoke(t, rs.getObject(columnName));
break;
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
}
}
}
}


} catch (SQLException e) {
e.printStackTrace();
} finally {
DBUtil.close(rs, pstat, conn);
}
return t;
}

/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷询锟斤拷锟�
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
* @param where 锟斤拷锟斤拷锟侥诧拷询锟斤拷锟斤拷
* @param value 锟斤拷锟斤拷锟侥诧拷询锟斤拷锟斤拷锟叫的诧拷锟斤拷值
* @return 锟斤拷锟截诧拷询锟斤拷锟侥斤拷锟斤拷锟�
*/
public static <T> List<T> query(Class<T> cla, String where, Object[] value) {
// 锟斤拷锟絊QL锟斤拷锟�
StringBuffer sql = new StringBuffer("select * from "
+ cla.getName().substring(cla.getName().lastIndexOf(".") + 1)
+ " ");
if (where != null && where.length() > 0) {
sql.append(where);
}


// 锟斤拷取Class锟斤拷锟斤拷cla锟斤拷应锟斤拷锟斤拷锟叫凤拷锟斤拷
Method[] methods = cla.getDeclaredMethods();
// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;
ResultSet rs = null;
List<T> listResult = new ArrayList<T>();
T t = null;
try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
// 锟斤拷锟斤拷SQL锟斤拷锟斤拷械牟锟斤拷锟�
for (int i = 0; i < value.length; i++) {
pstat.setObject(i + 1, value[i]);
}
// 锟斤拷取锟斤拷锟斤拷锟�
rs = pstat.executeQuery();
// 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷械锟斤拷锟斤拷锟斤拷锟较�
ResultSetMetaData rsmd = pstat.getMetaData();
// 锟斤拷取锟斤拷锟斤拷锟斤拷械锟斤拷械母锟斤拷锟�
int columnNum = rsmd.getColumnCount();
// 锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟节憋拷锟斤拷锟斤拷锟斤拷锟叫碉拷锟叫碉拷锟斤拷锟斤拷
String[] columnNames = new String[columnNum];
// 锟斤拷取锟斤拷锟斤拷锟斤拷械母锟斤拷锟斤拷械锟斤拷锟斤拷撇锟斤拷锟斤拷娴斤拷锟斤拷锟斤拷锟�
for (int i = 0; i < columnNum; i++) {
columnNames[i] = rsmd.getColumnName(i + 1);
}
// 锟斤拷锟斤拷锟斤拷锟斤拷锟�
while (rs.next()) {
try {
t = cla.newInstance();
} catch (InstantiationException e1) {
e1.printStackTrace();
} catch (IllegalAccessException e1) {
e1.printStackTrace();
}
for (String columnName : columnNames) {
// 锟斤拷锟斤拷锟街讹拷锟斤拷锟斤拷取锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷
String methodName = setMethodName(columnName);
for (int i = 0; i < methods.length; i++) {
// 锟斤拷锟斤拷锟斤拷锟节凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟揭筹拷锟斤拷应锟斤拷set锟斤拷锟斤拷
if (methodName.equals(methods[i].getName())) {
try {
// 执锟斤拷锟斤拷应锟斤拷set锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷t锟斤拷锟斤拷锟斤拷锟斤拷值
methods[i].invoke(t, rs.getObject(columnName));
break;
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
}
}
}
// 锟斤拷锟斤拷锟斤拷锟斤拷锟侥讹拷锟斤拷锟斤拷拥锟街革拷锟斤拷锟斤拷斜锟斤拷锟�
listResult.add(t);
}


} catch (SQLException e) {
e.printStackTrace();
} finally {
DBUtil.close(rs, pstat, conn);
}
return listResult;
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷Class锟斤拷锟斤拷锟絀D删锟斤拷锟斤拷应锟斤拷锟斤拷锟斤拷
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
* @param id 锟斤拷锟斤拷锟斤拷ID
*/
public static <T> void delete(Class<T> cla, int id) {
// 拼锟斤拷SQL锟斤拷锟�
String tableName = cla.getName().substring(
cla.getName().lastIndexOf(".") + 1);
String sql = new String("delete from " + tableName + " where id = ?");


// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql);
pstat.setInt(1, id);
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
} finally {
// 锟截憋拷锟斤拷锟捷匡拷锟斤拷锟斤拷
DBUtil.close(null, pstat, conn);
}
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷Class锟斤拷锟斤拷锟斤拷锟斤拷锟酵诧拷锟斤拷值删锟斤拷锟斤拷应锟斤拷锟斤拷锟斤拷
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
* @param where 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
* @param value 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥诧拷锟斤拷值
*/
public static <T> void delete(Class<T> cla, String where, Object[] value) {
String tableName = cla.getName().substring(
cla.getName().lastIndexOf(".") + 1);
StringBuffer sql = new StringBuffer("delete from " + tableName + " ");
if (where != null && where.length() > 0) {
sql.append(where);
}


// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
for (int i = 0; i < value.length; i++) {
pstat.setObject(i + 1, value[i]);
}
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
} finally {
// 锟截憋拷锟斤拷锟捷匡拷锟斤拷锟斤拷
DBUtil.close(null, pstat, conn);
}


}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷Class锟斤拷锟斤拷锟斤拷锟斤拷锟接︼拷锟斤拷锟斤拷荼锟�
* 
* @param cla 锟斤拷锟斤拷锟斤拷Class锟斤拷锟斤拷
*/
public static <T> void clear(Class<T> cla) {
String tableName = cla.getName().substring(
cla.getName().lastIndexOf(".") + 1);
String sql = new String("delete from " + tableName);


// 锟斤拷锟斤拷锟斤拷锟捷匡拷
Connection conn = null;
PreparedStatement pstat = null;


try {
conn = DBUtil.getConnection();
pstat = conn.prepareStatement(sql.toString());
pstat.execute();
} catch (SQLException e) {
e.printStackTrace();
} finally {
// 锟截憋拷锟斤拷锟捷匡拷锟斤拷锟斤拷
DBUtil.close(null, pstat, conn);
}
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷锟街讹拷锟斤拷锟斤拷取锟斤拷应锟斤拷get锟斤拷锟斤拷
* 
* @param name 锟斤拷锟斤拷锟斤拷锟街讹拷锟斤拷
* @return 锟斤拷锟斤拷锟斤拷应锟街段碉拷get锟斤拷锟斤拷
*/
private static String getMethodName(String name) {
char[] ch = name.toCharArray();
ch[0] -= 32;
String str = new String(ch);
return "get" + str;
}


/**
* 锟斤拷锟捷革拷锟斤拷锟斤拷锟街讹拷锟斤拷锟斤拷取锟斤拷应锟斤拷set锟斤拷锟斤拷
* 
* @param name 锟斤拷锟斤拷锟斤拷锟街讹拷锟斤拷
* @return 锟斤拷锟斤拷锟斤拷应锟街段碉拷set锟斤拷锟斤拷
*/
private static String setMethodName(String name) {
char[] ch = name.toCharArray();
ch[0] -= 32;
String str = new String(ch);
return "set" + str;
}
}