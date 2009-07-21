package org.hyperion.cache;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents a single cache index. For more information aobut the credits of
 * the cache system, see the Cache class.
 * @author Graham
 *
 */
public class CacheIndex {
	
	/**
	 * The size of an index.
	 */
	public static final int INDEX_SIZE = 6;
	
	/**
	 * The size of a block.
	 */
	public static final int BLOCK_SIZE = 520;
	
	/**
	 * The data file.
	 */
	private RandomAccessFile dataFile;
	
	/**
	 * The index file.
	 */
	private RandomAccessFile indexFile;
	
	/**
	 * The cache number.
	 */
	private int cacheNo;
	
	/**
	 * Creates a cache.
	 * @param dataFile The common data file.
	 * @param indexFile This cache's index file.
	 * @param cacheNo The cache number.
	 */
	public CacheIndex(RandomAccessFile dataFile, RandomAccessFile indexFile, int cacheNo) {
		this.dataFile = dataFile;
		this.indexFile = indexFile;
		this.cacheNo = cacheNo;
	}

	/**
	 * Reads a file from this cache.
	 * @param fileId The file id.
	 * @return The file data.
	 * @throws IOException
	 */
	public byte[] read(int fileId) throws IOException {
		/*
		 * MINA uses at thread pool when dispatching read requests, so here we
		 * synchronize to ensure the file pointers are not corrupted due to
		 * access across multiple threads.
		 */
		synchronized(CacheIndex.class) {
			
			/*
			 * Lookup the index file pointer and check the bounds.
			 */
			int index = fileId * INDEX_SIZE;
			if(index < 0 || index >= indexFile.length()) {
				throw new IOException("Index file index out of bounds.");
			}
			
			/*
			 * Read the data in the index file.
			 */
			byte[] bytes = new byte[6];
			indexFile.seek(index);
			indexFile.readFully(bytes);
			
			/*
			 * Compute the file size and block id.
			 */
			int fileSize = ((bytes[0] & 0xFF) << 16) + ((bytes[1] & 0xFF) << 8) + (bytes[2] & 0xFF);
			int currentBlockId = ((bytes[3] & 0xFF) << 16) + ((bytes[4] & 0xFF) << 8) + (bytes[5] & 0xFF);
			
			/*
			 * Begin reading the file.
			 */
			int read = 0;
			byte[] fileBuffer = new byte[fileSize];
			int currentFileBlockNo = 0;
			while(fileSize > read) {
				
				/*
				 * Lookup the pointer in the data file and check the bounds.
				 */
				int dataIndex = currentBlockId * BLOCK_SIZE;
				if(dataIndex < 0 || dataIndex >= dataFile.length()) {
					throw new IOException("Data file index out of bounds.");
				}
				
				/*
				 * Seek to that position.
				 */
				dataFile.seek(dataIndex);
				
				/*
				 * Compute how much data we still have remaining.
				 */
				int toRead = fileSize - read;
				if(toRead > 512) {
					toRead = 512;
				}
				
				/*
				 * Read the block header.
				 */
				byte[] blockData = new byte[8 + toRead];
				dataFile.readFully(blockData, 0, blockData.length);
				
				int nextFileId = ((blockData[0]) & 0xFF << 8) + (blockData[1] & 0xFF);
				int nextFileBlockNo = ((blockData[2] & 0xFF) << 8) + (blockData[3] & 0xFF);
				int nextBlockId = ((blockData[4] & 0xFF) << 16) +((blockData[5] & 0xFF) << 8) + (blockData[6] & 0xFF);
				int nextCacheId = (blockData[7] & 0xFF);
				
				/*
				 * Read the block.
				 */
				for(int i = 0; i < toRead; i++) {
					fileBuffer[read++] = blockData[i + 8];
				}
				
				/*
				 * Check the next file id, block number and cache ids match what
				 * we expect.
				 */
				// although in Tom's cache suite version these seem to work as they should, here we have to ensure
				// they are a byte for some reason. it works!
				if((nextFileId & 0xFF) != (fileId & 0xFF)) {
					throw new IOException("Next file id " + nextFileId + " does not match request file id " + fileId);
				}
				if(nextFileBlockNo != currentFileBlockNo) {
					throw new IOException("Next file block does not match current file block");
				}
				if(nextCacheId != cacheNo) {
					throw new IOException("Next cache id does not match current cache id");
				}
				
				/*
				 * Calculate the next block numbers.
				 */
				currentBlockId = nextBlockId;
				currentFileBlockNo++;
				
			}
			return fileBuffer;
		}
	}

}
