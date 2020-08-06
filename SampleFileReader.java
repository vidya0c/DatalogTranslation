package mypackage;

import java.util.List;
import java.util.Set;
import java.io.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SampleFileReader {

	private static final int BUFFER_SIZE = 4096;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String homeFolder = "/Users/vishwanathlhugar/Desktop/Project/";
		String inputZipFile = "sample.zip";
		String inputUnzippedFile = homeFolder+"sampleUnzipped/";
		String outputFiles= homeFolder+"outputFiles/";

		/*try {
			unzip(homeFolder+inputZipFile, inputUnzippedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File folder = new File(inputUnzippedFile);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.getAbsolutePath().equals(inputUnzippedFile+inputZipFile.replace(".zip", ""))){	

				File folderInside = new File(file.getAbsolutePath());
				File[] listOfFinalFiles = folderInside.listFiles();
				*/
				
			//	for (File file1 : listOfFinalFiles) {
					
					File file1 = new File("/Users/vishwanathlhugar/Desktop/Project/sampleUnzipped/yagoLiteralFacts.tsv");
					try {
						List<String> allLines = Files.readAllLines(Paths.get(file1.getPath()));
						for (String line : allLines) {

							String[] obj = line.split("\t");
							for(int i =0;i<obj.length;i++) {
								//System.out.println(obj[2]);

								File output = new File(outputFiles);
								if(!output.isDirectory()) {
									output.mkdir();
								}

								FileWriter writer = new FileWriter(outputFiles+obj[2].replaceAll("<", "").replaceAll(">", "")+".csv", true);

								writer.write(obj[1].replaceAll("<", "").replaceAll(">", "")+","+obj[3].replaceAll("<", "").replaceAll(">", ""));
								writer.write("\n");

								writer.close();
							}
						}
						System.out.println("Done");
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			//}
		//}


	//}
	/*	try {
			unzip("/Users/vishwanathlhugar/Desktop/test.zip", "/Users/vishwanathlhugar/Desktop/sampleunzipped");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
copycontent("/Users/vishwanathlhugar/Desktop/sampleunzipped/test/test.txt", "/Users/vishwanathlhugar/Desktop/sampleunzipped/test/new.txt");
String[] myFiles = {"/Users/vishwanathlhugar/Desktop/sampleunzipped/test/new.txt"};
		String zipFile = "/Users/vishwanathlhugar/Desktop/newtest.zip";


		 try { zip(myFiles, zipFile); } catch (Exception ex) { // some errors occurred
		 ex.printStackTrace(); }

		 System.out.println(readCompressedFile("/Users/vishwanathlhugar/Desktop/test.zip"));
		 writeCompressedFile("/Users/vishwanathlhugar/Desktop/vidya.zip",readCompressedFile("/Users/vishwanathlhugar/Desktop/test.zip"));
	 */ 




	private static String readCompressedFile(String fileName) {
		try {
			ZipInputStream gis = new ZipInputStream(new FileInputStream(fileName));
			ByteArrayOutputStream fos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			ZipEntry entry = gis.getNextEntry();
			while (entry != null) {
				String filePath =  entry.getName();

				gis.closeEntry();
				entry = gis.getNextEntry();
			}
			while ((len = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			gis.close();
			System.out.println("Zip content:"+fos.toByteArray().toString());
			return new String(fos.toByteArray());
		} catch (IOException ex) {
			System.out.println( ex.getMessage());
			return null;
		}
	}
	private static void writeCompressedFile(String fileName, String value) {
		try {
			InputStream is = new ByteArrayInputStream(value.getBytes());
			ZipOutputStream gzipOS = new ZipOutputStream(new FileOutputStream(fileName));
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
				gzipOS.write(buffer, 0, len);
			}
			gzipOS.close();
			is.close();
		} catch (IOException ex) {
			// Handle exception
			System.out.println( ex.getMessage());
		}
	}
	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
		zipIn.closeEntry();
	}

	public static void copycontent(String src, String dest) {

		try {
			FileReader fr = new FileReader(src);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(dest, true);
			String s;

			while ((s = br.readLine()) != null) { // read a line
				fw.write(s); // write to output file
				System.out.println(s);
				fw.flush();
			}
			br.close();
			fw.close();
			System.out.println("file copied");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void zipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException {
		zos.putNextEntry(new ZipEntry(file.getName()));
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		long bytesRead = 0;
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = bis.read(bytesIn)) != -1) {
			zos.write(bytesIn, 0, read);
			bytesRead += read;
		}
		zos.closeEntry();
	}

	private static void zipDirectory(File folder, String parentFolder, ZipOutputStream zos)
			throws FileNotFoundException, IOException {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipDirectory(file, parentFolder + "/" + file.getName(), zos);
				continue;
			}
			zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			long bytesRead = 0;
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = bis.read(bytesIn)) != -1) {
				zos.write(bytesIn, 0, read);
				bytesRead += read;
			}
			zos.closeEntry();
		}
	}

	public static void zip(String[] files, String destZipFile) throws FileNotFoundException, IOException {
		List<File> listFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			listFiles.add(new File(files[i]));
		}
		zip(listFiles, destZipFile);
	}

	public static void zip(List<File> listFiles, String destZipFile) throws FileNotFoundException, IOException {
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile));
		for (File file : listFiles) {
			if (file.isDirectory()) {
				zipDirectory(file, file.getName(), zos);
			} else {
				zipFile(file, zos);
			}
		}
		zos.flush();
		zos.close();
	}
}
