package com.ssachtleben.play.plugin.compress;

import play.Play;
import play.api.templates.Html;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Results;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

/**
 * The PrettyController returns the {@link Content} or {@link Html} parameter
 * for each important method e.g. ok, badRequest, notFound, forbidden,
 * internalServerError and unauthorized prettified with yui compressor.
 * 
 * @author Sebastian Sachtleben
 */
public class PrettyController extends Controller {

	public static Results.Status ok(final Content content) {
		return Results.ok(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status ok(final Html html) {
		return Results.ok(prettify(html)).as("text/html; charset=utf-8");
	}

	public static Results.Status badRequest(final Content content) {
		return Results.badRequest(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status badRequest(final Html html) {
		return Results.badRequest(prettify(html)).as("text/html; charset=utf-8");
	}

	public static Results.Status notFound(final Content content) {
		return Results.notFound(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status notFound(final Html html) {
		return Results.notFound(prettify(html)).as("text/html; charset=utf-8");
	}

	public static Results.Status forbidden(final Content content) {
		return Results.forbidden(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status forbidden(final Html html) {
		return Results.forbidden(prettify(html)).as("text/html; charset=utf-8");
	}

	public static Results.Status internalServerError(final Content content) {
		return Results.internalServerError(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status internalServerError(final Html html) {
		return Results.internalServerError(prettify(html)).as("text/html; charset=utf-8");
	}

	public static Results.Status unauthorized(final Content content) {
		return Results.unauthorized(prettify(content)).as("text/html; charset=utf-8");
	}

	public static Results.Status unauthorized(final Html html) {
		return Results.unauthorized(prettify(html)).as("text/html; charset=utf-8");
	}

	/**
	 * Prettify the {@link Content}, e.g. removing whitespaces and linebreaks.
	 * 
	 * @param content
	 *          The {@link Content} to prettify.
	 * @return The prettified content.
	 */
	private static String prettify(final Content content) {
		if (Play.isDev()) {
			return content.body();
		}
		final HtmlCompressor compressor = new HtmlCompressor();
		final String output = content.body().trim();
		compressor.setRemoveComments(true);
		compressor.setCompressCss(true);
		compressor.setCompressJavaScript(true);
		compressor.setRemoveIntertagSpaces(true);
		return compressor.compress(output);
	}
}
