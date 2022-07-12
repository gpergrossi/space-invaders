package com.gpergrossi.spaceinvaders;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * A resource manager for sprites in the game. Its often quite important
 * how and where you get your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 * <p>
 * [singleton]
 * <p>
 * @author Kevin Glass
 */
public class AssetStore {
	/** The single instance of this class */
	private static AssetStore single = new AssetStore();

	/**
	 * Get the single instance of this class
	 *
	 * @return The single instance of this class
	 */
	public static AssetStore get() {
		return single;
	}

	/** The cached sprite map, from reference to sprite instance */
	private HashMap<String, Sprite> sprites;

	/** The cached font map, from reference to font instance */
	private HashMap<String, Font> fonts;

	/** private constructor */
	private AssetStore() {
		sprites = new HashMap<>();
		fonts = new HashMap<>();
	}


	/**
	 * Retrieve a sprite from the store
	 *
	 * @param ref The reference to the image to use for the sprite
	 * @return A sprite instance containing an accelerate image of the request reference
	 */
	public Sprite getSprite(String ref) {
		// If we've already got the sprite in the cache then just return the existing version.
		if (sprites.containsKey(ref)) {
			return sprites.get(ref);
		}

		// Otherwise, go away and grab the sprite from the resource loader.
		BufferedImage sourceImage = null;

		// Get an input stream from the reference URL.
		InputStream istream = openStream(ref);

		try {
			// Use ImageIO to read the image in.
			sourceImage = ImageIO.read(istream);
		} catch (IOException e) {
			fail("Failed to load " + ref);
		}

		// Create an accelerated image of the right size to store our sprite in.
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

		// Draw our source image into the accelerated image.
		image.getGraphics().drawImage(sourceImage,0,0,null);

		// Create a sprite, add it the cache then return it.
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);

		return sprite;
	}

	/**
	 * Retrieve a font from the store
	 *
	 * @param ref The reference to the font (a font URL relative to the src dir)
	 * @return A font instance
	 */
	public Font getFont(String ref, int fontStyle, float fontSize) {
		Font font = null;

		// If we've already got the font in the cache then just return the existing version.
		if (fonts.containsKey(ref)) {
			font = fonts.get(ref);
		}
		else
		{
			// Otherwise, go away and grab the sprite from the resource loader.
			InputStream istream = openStream(ref);

			try {
				font = Font.createFont(Font.TRUETYPE_FONT, istream);
			} catch (IOException | FontFormatException e) {
				fail("Could not load Font '" + ref + "': " + e.getMessage());
			}

			if (font == null) {
				fail("Font '" + ref + "' failed to load");
			}

			// Add our new font object to the font map
			fonts.put(ref, font);

			// Register the font with the GraphicsEnvironment, which allows some more complex font
			// processing libraries to access variations of the font by the name of the font family.
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			if (!ge.registerFont(font)) {
				warn("Failed to register font '" + ref + "' with GraphicsEnvironment!");
			}
		}

		if (font == null) {
			fail("Font '" + ref + "' failed to load");
		}

		Font result = font.deriveFont(fontStyle, fontSize);
		if (result == null) {
			fail("Failed to derive font '" + ref + "' for style " + fontStyle + " and size " + fontSize);
		}

		// Finally, we have to get the right font style and size
		return result;
	}

	private InputStream openStream(String ref) {
		// The ClassLoader.getResource() ensures we get the sprite
		// from the appropriate place, this helps with deploying the game
		// with things like webstart. You could equally do a file look
		// up here.
		URL url = this.getClass().getClassLoader().getResource(ref);

		if (url == null) {
			fail("Can't find ref: " + ref);
		}

		InputStream istream = null;
		try {
			istream = url.openStream();
		} catch (IOException e) {
			fail("Can't get input stream from URL: " + url);
		}

		return istream;
	}

	/**
	 * Utility method to handle resource loading failure
	 *
	 * @param message The message to display on failure
	 */
	private void fail(String message) {
		// we're pretty dramatic here, if a resource isn't available
		// we dump the message and exit the game
		System.err.println(message);
		System.exit(0);
	}

	/**
	 * Utility method to handle resource loading warnings
	 *
	 * @param message The warning message to display
	 */
	private void warn(String message) {
		System.err.println(message);
	}
}