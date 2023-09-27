package com.ims.db;

import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ims.common.AppRestAPIBaseVerticle;
import com.ims.crypto.Crypto;
import com.ims.process.PostProcessor;
import com.ims.process.builder.PostProcessBuilder;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlConnection;
import io.vertx.reactivex.sqlclient.Tuple;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseConnectionVerticle extends AppRestAPIBaseVerticle {

	Logger log = LoggerFactory.getLogger(DatabaseConnectionVerticle.class.getName());
	PgConnectOptions connectOptions;
	PoolOptions poolOptions;
	PgPool pool;

	public void start() throws Exception {
		log.info("DatabaseConnectionVerticle started");
		super.start();
		// TODO Auto-generated method stub
		this.databaseConnection();
	}

	public void databaseConnection() {

		try {
			connectOptions = new PgConnectOptions()
					.setPort(Integer.parseInt(Crypto.decryptData(config().getString("db.port.no"))))
					.setHost(Crypto.decryptData(config().getString("db.host")))
					.setDatabase(Crypto.decryptData(config().getString("database.name")))
					.setUser(Crypto.decryptData(config().getString("db.user.name")))
					.setPassword(Crypto.decryptData(config().getString("db.password")));
			poolOptions = new PoolOptions().setMaxSize(Integer.parseInt(config().getString("db.conn.pool.size")));
			pool = PgPool.pool(vertx, connectOptions, poolOptions);
			log.info("Database connection started");
		} catch (Exception e) {
			e.getStackTrace();
			log.error("Error in Connection Handling");
		}
	}


	protected void runQuery(String query, JsonObject data, String methodAddress,
			Handler<AsyncResult<Object>> handler) {

		log.info("--->" + query + data);

		pool.getConnection(connectionHandler -> {
			if (connectionHandler.succeeded()) {
				SqlConnection poolConnection = connectionHandler.result();
				Tuple tuple = Tuple.of(data);

				poolConnection.preparedQuery(query).execute(tuple, ar -> {
					if (ar.succeeded()) {
						try {
							RowSet<Row> result = ar.result();
//							System.out.println(result);
							for (Row row : result) {
								JsonObject jsonObj = new JsonObject();
								for (int i = 0; i < result.columnsNames().size(); i++) {
									jsonObj.put(row.getColumnName(i), row.getValue(i));
								}
//								System.out.println("***"+jsonObj);
//								handler.handle(
//										Future.succeededFuture(jsonObj));
								if (jsonObj != null) {
									PostProcessor.doProcess(jsonObj,
											PostProcessBuilder.getPostProcessList(methodAddress), methodAddress,
											object -> {
												if (object.succeeded()) {
//													System.out.println("***"+object.result());
													handler.handle(
															Future.succeededFuture(object.result()));
													poolConnection.close();
												} else {
													poolConnection.close();
													log.info("Failure: " + object.cause().getMessage());
												}
											});
								}

							}

						} catch (Exception e) {
							// TODO: handle exception
							poolConnection.close();
							handler.handle(Future.failedFuture(e));
						}
					} else {
						log.info("Failure: " + ar.cause().getMessage());
						poolConnection.close();
						handler.handle(Future.failedFuture(ar.cause()));
					}

				});

			} else {
				log.info("Failure: " + connectionHandler.cause().getMessage());
				handler.handle(Future.failedFuture(connectionHandler.cause()));
			}
		});
	}

	private static Key key = null;
	private static String cryptoAlgorithm = null;

	public static String decryptData(String data) throws Exception {
		if (key != null) {
			Cipher cipher = Cipher.getInstance(cryptoAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encrypted = Base64.decodeBase64(data.getBytes());
			return new String(cipher.doFinal(encrypted));
		}
		return null;
	}

	

	@SuppressWarnings("unchecked")
	protected void runQuery(String query, Handler<AsyncResult<JSONArray>> handler) {

		//log.info("--->" + query);

		pool.getConnection(connectionHandler -> {
			if (connectionHandler.succeeded()) {
				SqlConnection poolConnection = connectionHandler.result();
//				 = Tuple.of(data);
				poolConnection.preparedQuery(query).execute(ar -> {
					if (ar.succeeded()) {
						try {
							RowSet<Row> result = ar.result();
							JSONArray resultList = new JSONArray();
							for (Row row : result) {
								JSONObject jsonObj = new JSONObject();
								for (int i = 0; i < result.columnsNames().size(); i++) {
									jsonObj.put(row.getColumnName(i), row.getValue(i));
								}
								resultList.add(jsonObj);
							}

							poolConnection.close();
							handler.handle(Future.succeededFuture(resultList));
						} catch (Exception e) {
							// TODO: handle exception
							poolConnection.close();
							handler.handle(Future.failedFuture(e));
						}
					} else {
						log.info("Failure: " + ar.cause().getMessage());
						poolConnection.close();
						handler.handle(Future.failedFuture(ar.cause()));
					}

				});

			} else {
				log.info("Failure: " + connectionHandler.cause().getMessage());
				handler.handle(Future.failedFuture(connectionHandler.cause()));
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void runSP(String query, JsonObject data, Handler<AsyncResult<JSONArray>> handler) {

		log.info("--->" + query);

		pool.getConnection(connectionHandler -> {
			if (connectionHandler.succeeded()) {
				SqlConnection poolConnection = connectionHandler.result();
				Tuple tuple = Tuple.of(data);
				poolConnection.preparedQuery(query).execute(tuple, ar -> {
					if (ar.succeeded()) {
						try {
							RowSet<Row> result = ar.result();
							JSONArray resultList = new JSONArray();
							for (Row row : result) {
								JSONObject jsonObj = new JSONObject();
								for (int i = 0; i < result.columnsNames().size(); i++) {
									jsonObj.put(row.getColumnName(i), row.getValue(i));
								}
								resultList.add(jsonObj);
							}

							poolConnection.close();
							handler.handle(Future.succeededFuture(resultList));
						} catch (Exception e) {
							// TODO: handle exception
							poolConnection.close();
							handler.handle(Future.failedFuture(e));
						}
					} else {
						log.info("Failure: " + ar.cause().getMessage());
						poolConnection.close();
						handler.handle(Future.failedFuture(ar.cause()));
					}

				});

			} else {
				log.info("Failure: " + connectionHandler.cause().getMessage());
				handler.handle(Future.failedFuture(connectionHandler.cause()));
			}
		});
	}
	
	
}