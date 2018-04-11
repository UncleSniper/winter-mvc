package org.unclesniper.winter.mvc.dispatch;

import java.util.Map;
import java.util.HashMap;

public class PathParameters<PathKeyT> {

	private static final class Piece {

		public final int startIndex;

		public final int endIndex;

		public Piece(int startIndex, int endIndex) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

	}

	private final String pathInfo;

	private final Map<PathKeyT, Piece> pieces = new HashMap<PathKeyT, Piece>();

	public PathParameters(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	public String getPathInfo() {
		return pathInfo;
	}

	public void putParameter(PathKeyT key, int startIndex, int endIndex) {
		pieces.put(key, new Piece(startIndex, endIndex));
	}

	public String getParameter(PathKeyT key) {
		Piece piece = pieces.get(key);
		return piece == null ? null : pathInfo.substring(piece.startIndex, piece.endIndex);
	}

	public void clearParameters() {
		pieces.clear();
	}

}
