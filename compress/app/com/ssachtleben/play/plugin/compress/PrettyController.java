package com.ssachtleben.play.plugin.compress;

import play.Play;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Results;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

/**
 * @author Sebastian Sachtleben
 */
public class PrettyController extends Controller {

	public static Results.Status ok(Content content) {
		return Results.ok(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status badRequest(Content content) {
		return Results.badRequest(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status notFound(Content content) {
		return Results.notFound(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status forbidden(Content content) {
		return Results.forbidden(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status internalServerError(Content content) {
		return Results.internalServerError(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status unauthorized(Content content) {
		return Results.unauthorized(prettify(content)).as("text/html; charset=utf-8");
	}

	/**
	 * Prettify the {@link Content}, e.g. removing whitespaces and linebreaks.
	 * 
	 * @param content
	 *            The {@link Content} to prettify.
	 * @return The prettified content.
	 */
	private static String prettify(Content content) {
		HtmlCompressor compressor = new HtmlCompressor();
		String output = content.body().trim();
		if (Play.isDev()) {
			compressor.setPreserveLineBreaks(true);
		}
		return compressor.compress(output);
	}
}
