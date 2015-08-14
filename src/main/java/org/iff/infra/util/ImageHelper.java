/*******************************************************************************
 * Copyright (c) 2014-7-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-7-20
 */
public class ImageHelper {

	public static void main(String[] args) throws Exception {
		ImageHelper
				.of(
						"E:/workspace/JeeGalileo/software-market-front-project/src/main/webapp/images/logo.png")
				.size(16, 16).toFile("C:\\Users\\Tyler\\Desktop\\testtest.ico");
	}

	public static void reize(int width, int height, File in, File out) {
		try {
			ImageHelper.of(in).size(width, height).toFile(out);
		} catch (Exception e) {
			Exceptions.runtime("[ImageHelper.reize", e);
		}
	}

	public static void reize(int width, int height, File in, OutputStream out) {
		try {
			ImageHelper.of(in).size(width, height).toOutputStream(out);
		} catch (Exception e) {
			Exceptions.runtime("[ImageHelper.reize", e);
		}
	}

	/**
	 * Indicate to make thumbnails for images with the specified filenames.
	 * 
	 * @param files		File names of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<File> of(String... files) {
		return Thumbnails.of(files);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link File}s.
	 * 
	 * @param files		{@link File} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<File> of(File... files) {
		return Thumbnails.of(files);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link URL}s.
	 * 
	 * @param urls		{@link URL} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<URL> of(URL... urls) {
		return Thumbnails.of(urls);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link InputStream}s.
	 * 
	 * @param inputStreams		{@link InputStream}s which provide the images
	 * 							for which thumbnails are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<? extends InputStream> of(InputStream... inputStreams) {
		return Thumbnails.of(inputStreams);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link BufferedImage}s.
	 * 
	 * @param images	{@link BufferedImage}s for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<BufferedImage> of(BufferedImage... images) {
		return Thumbnails.of(images);
	}

	/**
	 * Indicate to make thumbnails for images with the specified filenames.
	 * 
	 * @param files		File names of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<File> fromFilenames(Iterable<String> files) {
		return Thumbnails.fromFilenames(files);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link File}s.
	 * 
	 * @param files		{@link File} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<File> fromFiles(Iterable<File> files) {
		return Thumbnails.fromFiles(files);
	}

	/**
	 * Indicate to make thumbnails for images with the specified {@link URL}s.
	 * 
	 * @param urls		URLs of the images for which thumbnails
	 * 					are to be produced.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<URL> fromURLs(Iterable<URL> urls) {
		return Thumbnails.fromURLs(urls);
	}

	/**
	 * Indicate to make thumbnails for images obtained from the specified
	 * {@link InputStream}s.
	 * 
	 * @param inputStreams		{@link InputStream}s which provide images for
	 * 							which thumbnails are to be produced.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<InputStream> fromInputStreams(
			Iterable<? extends InputStream> inputStreams) {
		return Thumbnails.fromInputStreams(inputStreams);
	}

	/**
	 * Indicate to make thumbnails from the specified {@link BufferedImage}s.
	 * 
	 * @param images	{@link BufferedImage}s for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<BufferedImage> fromImages(
			Iterable<BufferedImage> images) {
		return Thumbnails.fromImages(images);
	}
}
