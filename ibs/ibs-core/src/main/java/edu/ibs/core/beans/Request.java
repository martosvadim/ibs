package edu.ibs.core.beans;

/**
 *
 * @author Vadim Martos
 */
public class Request {

	private final long id;
	private final User user;
	private final long date;
	private final String info;

	public Request(long id, User user, long date, String info) {
		this.id = id;
		this.user = user;
		this.date = date;
		this.info = info;
	}
}
