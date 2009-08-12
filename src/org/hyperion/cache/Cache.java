package org.hyperion.cache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <p>Manages the game cache. Most of the code in this class is based on
 * decompiled work of Tom's cache suite, which is available here:
 * <a href="http://nuke-net.com/?page_id=169"
 * http://nuke-net.com/?page_id=169</a>.</p>
 * 
 * <p>A lot of the research for Tom's project was done by super_, I also
 * referred to a few of his topics when creating this system.</p>
 * @author Graham Edgecombe
 *
 */
public class Cache {
	
	/**
	 * An array of caches.
	 */
	private CacheIndex[] caches = new CacheIndex[5];
	
	/**
	 * Loads the cache.
	 * @param directory The directory.
	 * @throws FileNotFoundException if a file in the directory was not found.
	 */
	public Cache(String directory) throws FileNotFoundException {
		for(int i = 0; i < 5; i++) {
			caches[i] = new CacheIndex(new RandomAccessFile(directory + "main_file_cache.dat", "r"), new RandomAccessFile(directory + "main_file_cache.idx" + i, "r"), i+1);
		}
	}
	
	/**
	 * Reads a file from the cache.
	 * @param cacheId The cache id.
	 * @param fileId The file id.
	 * @return The file buffer.
	 * @throws IOException if an I/O error occurs.
	 */
	public byte[] read(int cacheId, int fileId) throws IOException {
		return caches[cacheId].read(fileId);
	}

}

