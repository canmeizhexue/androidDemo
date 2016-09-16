package com.canmeizhexue.common.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

public class ZipUtils {
	private static final int GZIP_BUFFER_SIZE=131072;

	/**
	 * 解压缩
	 * @param inputStream
	 * @param newFile
	 * @throws IOException
	 */
	public static final void uncompressGZipFile(InputStream inputStream,File newFile) throws IOException{

		GZIPInputStream gzipInputStream=new GZIPInputStream(inputStream); 
		DataInputStream objIn = new DataInputStream(new BufferedInputStream(gzipInputStream));
		RandomAccessFile dbDownloadFile = new RandomAccessFile(newFile,"rwd");
		byte[] buffer=new byte[GZIP_BUFFER_SIZE];
		int offset=0;
		do {
			offset = objIn.read(buffer, 0, GZIP_BUFFER_SIZE);
			if (offset>0){
				dbDownloadFile.write(buffer, 0, offset);
			}
		}while(offset>0);
		dbDownloadFile.close();
		objIn.close();
		gzipInputStream.close();
	}
}
