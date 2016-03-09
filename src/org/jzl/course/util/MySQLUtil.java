package org.jzl.course.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jzl.weixin.pojo.Game;
import org.jzl.weixin.pojo.GameRound;


public class MySQLUtil {
	/**
	 * 获取Mysql数据库连接	
	 * 
	 * @return Connection
	 */
    private Connection getConn(HttpServletRequest request){
    	Connection conn = null;
    	//从request请求头去除IP、端口、用户名和密码
//    	String host = request.getHeader("SAE_MYSQL_HOST");
//    	String port = request.getHeader("SAE_MYSQL_PORT");
//    	String username = request.getHeader("SAE_MYSQL_USER");
//    	String password = request.getHeader("SAE_MYSQL_PASS");
//    	//数据库名称
//    	String dbName = "app_jzl1991";
//    	//JDBC URL
//    	String url = String.format("jdbc:mysql://%s:%s/%s", host,port,dbName);
    	 //参数配置
    	
    	    String username= "2zkwnn4mwj";
    	
    	    String password= "zhywhwz1ywlx0hzhjjy4xj5kjljjiwzkl4hz0li1";
    	    
    	 //主库连接  jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码
    	   String url= "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_jzl1991?useUnicode=true&characterEncoding=utf-8"; 
    	
    	    //从库连接  jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码
    	
//    	    String urlS= "jdbc:mysql://r.rdc.sae.sina.com.cn:3307/app_jzl1991?useUnicode=true&characterEncoding=utf-8"; 
    	try {
			//加载MySQL驱动
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		//获取数据库连接
    		conn = DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
    }
	
	/**
	 * 释放JDBC资源
	 * @param conn 数据库连接
	 * @param ps
	 * @param rs 记录集
	 */
	private void releaseResources(Connection conn,PreparedStatement ps,ResultSet rs){
		try {
			if(null!=rs){
				rs.close();
			}
			if(null!=ps){
				ps.close();
			}
			if(null!=conn){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存游戏信息
	 * @param request 请求对象
	 * @param game 游戏对象
	 * @return gameId
	 */
	public static int saveGame(HttpServletRequest request,Game game){
		int gameId = -1;
		String sql = "insert into game(open_id,game_answer,create_time,game_status,finish_time) values(?,?,?,?,?)";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn(request);
			//保存游戏
			ps = conn.prepareStatement(sql);
			ps.setString(1, game.getOpenId());
			ps.setString(2, game.getGameAnswer());
			ps.setString(3, game.getCreateTime());
			ps.setInt(4, game.getGameStatus());
			ps.setString(5, game.getFinishTime());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return gameId;
		
	}
	/**
	 * 获取用户最近一次创建的游戏
	 * @param request 请求对象
	 * @param openId 用户的OpenID
	 * @return
	 */
	public static Game getLastGame(HttpServletRequest request,String openId){
		Game game = null;
		String sql = "select * from game where open_id=? order by game_id desc limit 0,1";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn(request);
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			rs = ps.executeQuery();
			if(rs.next()){
				game = new Game();
				game.setGameId(rs.getInt("game_id"));
				game.setOpenId(rs.getString("open_id"));
				game.setGameAnswer(rs.getString("game_answer"));
				game.setCreateTime(rs.getString("create_time"));
				game.setGameStatus(rs.getInt("game_status"));
				game.setFinishTime(rs.getString("finish_time"));
			}
		} catch (SQLException e) {
			game = null;
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return game;
	}
	/**
	 * 根据游戏id修改游戏状态和完成时间
	 * @param request 请求对象
	 * @param gameId 游戏id
	 * @param gameStatus 游戏状态（0：游戏中 1：成功 2：失败 3：取消）
	 * @param finishTime 游戏完成时间
	 */
	public static void updateGame(HttpServletRequest request,int gameId,int gameStatus,String finishTime){
		String sql = "update game set game_status=?,finish_time=? where game_id=?";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = mysqlUtil.getConn(request);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, gameStatus);
			ps.setString(2,finishTime);
			ps.setInt(3,gameId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, null);
		}		
	}
	/**
	 * 保存游戏的回合信息
	 * @param request 请求对象
	 * @param gameRound 游戏回合数
	 */
	public static void saveGameRound(HttpServletRequest request,GameRound gameRound){
		String sql = "insert into game_round(game_id,open_id,guess_number,guess_time,guess_result) values (?,?,?,?,?)";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = mysqlUtil.getConn(request);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, gameRound.getGameId());
			ps.setString(2, gameRound.getOpenId());
			ps.setString(3, gameRound.getGuessNumber());
			ps.setString(4, gameRound.getGuessTime());
			ps.setString(5, gameRound.getGuessResult());
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, null);
		}
	}
	/**
	 * 根据游戏id获取游戏的全部回合<br>
	 * @param request 请求对象
	 * @param gameId 游戏id
	 */
	public static List<GameRound> findAllRoundByGameId(HttpServletRequest request,int gameId){
		List<GameRound> roundList = new ArrayList<GameRound>();
		//根据id升序排序
		String sql = "select * from game_round where game_id=? order by id asc";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn(request);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, gameId);
			rs = ps.executeQuery();
			GameRound round = null;
			while(rs.next()){
				round = new GameRound();
				round.setGameId(rs.getInt("game_id"));
				round.setOpenId(rs.getString("open_id"));
				round.setGuessNumber(rs.getString("guess_number"));
				round.setGuessTime(rs.getString("guess_time"));
				round.setGuessResult(rs.getString("guess_result"));
				roundList.add(round);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return roundList;
		
	}
	/**
	 * 获取用户的战绩
	 * @param request 请求对象
	 * @param openId 用户的OpenId
	 * @return HashMap<Integer,Integer>
	 */
	public static HashMap<Integer,Integer> getScoreByOpenId(HttpServletRequest request,String openId){
		HashMap<Integer,Integer> scoreMap = new HashMap<Integer,Integer>();
		//根据id升序排序
		String sql = "select game_status,count(*) from game where open_id=? group by game_status order by game_status asc";
		MySQLUtil mysqlUtil = new MySQLUtil();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = mysqlUtil.getConn(request);
			ps = conn.prepareStatement(sql);
			ps.setString(1, openId);
			rs = ps.executeQuery();
			while(rs.next()){
				scoreMap.put(rs.getInt(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			//释放资源
			mysqlUtil.releaseResources(conn, ps, rs);
		}
		return scoreMap;
		
	}

}
