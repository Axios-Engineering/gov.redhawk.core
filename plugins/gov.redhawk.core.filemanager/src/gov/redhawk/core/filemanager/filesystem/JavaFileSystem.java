/**
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.core.filemanager.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.DataType;
import CF.ErrorNumberType;
import CF.FileException;
import CF.FileHelper;
import CF.InvalidFileName;
import CF.PropertiesHolder;
import CF.FileSystemPackage.FileInformationType;
import CF.FileSystemPackage.FileType;
import CF.FileSystemPackage.UnknownFileSystemProperties;

/**
 * 
 *
 */
public class JavaFileSystem extends AbstractFileSystem {

	private static final int MILLIS_PER_SEC = 1000;
	private final File root;

	public JavaFileSystem(final ORB orb, final POA poa, final File root) {
		super(orb, poa);
		this.root = root;
	}

	@Override
	public void copy(final String sourceFileName, final String destinationFileName) throws InvalidFileName, FileException {
		if ("".equals(sourceFileName) || "".equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		if (sourceFileName.equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EINVAL, "Source file must be different from destination file.");
		}
		final File sourceFile = new File(this.root, sourceFileName);
		try {
			FileUtils.copyFile(sourceFile, new File(this.root, destinationFileName));
		} catch (final IOException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	@Override
	public void move(final String sourceFileName, final String destinationFileName) throws InvalidFileName, FileException {
		if ("".equals(sourceFileName) || "".equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		if (sourceFileName.equals(destinationFileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EINVAL, "Source file must be different from destination file.");
		}
		final File sourceFile = new File(this.root, sourceFileName);
		if (!sourceFile.renameTo(new File(this.root, destinationFileName))) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to rename file: " + sourceFileName + " to " + destinationFileName);
		}
	}

	@Override
	public CF.File create(final String fileName) throws InvalidFileName, FileException {
		if ("".equals(fileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final File file = new File(this.root, fileName);
		if (file.exists()) {
			throw new FileException(ErrorNumberType.CF_EEXIST, "File already exists of the name: " + fileName);
		}

		try {
			final JavaFileFileImpl impl = new JavaFileFileImpl(file, false);
			final byte[] id = this.poa.activate_object(impl);
			return FileHelper.narrow(this.poa.id_to_reference(id));
		} catch (final FileNotFoundException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ServantAlreadyActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final WrongPolicy e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ObjectNotActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	@Override
	public boolean exists(final String fileName) throws InvalidFileName {
		return new File(this.root, fileName).exists();
	}

	@Override
	public FileInformationType[] list(final String fullPattern) throws FileException, InvalidFileName {
		final int index = fullPattern.lastIndexOf('/');
		final File container;
		if (index > 0) {
			container = new File(this.root, fullPattern.substring(0, index));
		} else {
			container = this.root;
		}

		final String pattern = fullPattern.substring(index + 1, fullPattern.length());

		final String[] fileNames;

		if (pattern.length() == 0) {
			fileNames = new String[] {
				""
			};
		} else {
			fileNames = container.list(new FilenameFilter() {

				@Override
				public boolean accept(final File dir, final String name) {
					if (!dir.equals(container)) {
						return false;
					}
					return FilenameUtils.wildcardMatch(name, pattern);
				}
			});
		}
		
		if (fileNames == null) {
			return new FileInformationType[0];
		}

		final FileInformationType[] retVal = new FileInformationType[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			final FileInformationType fileInfo = new FileInformationType();
			final File file = new File(container, fileNames[i]);
			fileInfo.name = file.getName();
			fileInfo.size = file.length();
			if (file.isFile()) {
				fileInfo.kind = FileType.PLAIN;
			} else {
				fileInfo.kind = FileType.DIRECTORY;
			}
			final Any any = this.orb.create_any();
			any.insert_ulonglong(file.lastModified() / JavaFileSystem.MILLIS_PER_SEC);
			final DataType modifiedTime = new DataType("MODIFIED_TIME", any);

			fileInfo.fileProperties = new DataType[] {
				modifiedTime
			};

			retVal[i] = fileInfo;
		}
		return retVal;
	}

	@Override
	public void mkdir(final String directoryName) throws InvalidFileName, FileException {
		if ("".equals(directoryName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		new File(this.root, directoryName).mkdir();
	}

	@Override
	public CF.File open(final String fileName, final boolean readOnly) throws InvalidFileName, FileException {
		final File file = new File(this.root, fileName);
		if (!file.exists()) {
			throw new FileException(ErrorNumberType.CF_ENOENT, "No such file or directory\n");
		}
		if (file.isDirectory()) {
			throw new FileException(ErrorNumberType.CF_ENOENT, "Can not open a directory\n");
		}
		try {
			final JavaFileFileImpl impl = new JavaFileFileImpl(file, readOnly);
			
			final byte[] id = this.poa.activate_object(impl);
			return FileHelper.narrow(this.poa.id_to_reference(id));
		} catch (final FileNotFoundException e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ServantAlreadyActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final WrongPolicy e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		} catch (final ObjectNotActive e) {
			throw new FileException(ErrorNumberType.CF_EIO, e.getMessage());
		}
	}

	@Override
	public void query(final PropertiesHolder fileSystemProperties) throws UnknownFileSystemProperties {
		final List<DataType> unknownProperties = new ArrayList<DataType>();
		for (final DataType dataType : fileSystemProperties.value) {
			if (dataType.id.equals("SIZE")) {
				final Any any = this.orb.create_any();
				// TODO For now we don't support SIZE
				any.insert_ulonglong(0);
				//				any.insert_ulonglong(this.root..getTotalSpace());
				dataType.value = any;
			} else if (dataType.id.equals("AVAILABLE SPACE")) {
				final Any any = this.orb.create_any();
				long freeKb;
				try {
					freeKb = FileSystemUtils.freeSpaceKb(this.root.getAbsolutePath());
				} catch (final IOException e) {
					// PASS
					freeKb = 0;
				}
				any.insert_ulonglong(freeKb * 1024 / 8); // SUPPRESS CHECKSTYLE MagicNumber
				dataType.value = any;
			} else {
				unknownProperties.add(dataType);
			}
		}
		if (unknownProperties.size() > 0) {
			throw new UnknownFileSystemProperties(unknownProperties.toArray(new DataType[unknownProperties.size()]));
		}

	}

	@Override
	public void remove(final String fileName) throws FileException, InvalidFileName {
		if ("".equals(fileName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final boolean result = new File(this.root, fileName).delete();
		if (!result) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to delete file: " + fileName);
		}

	}

	@Override
	public void rmdir(final String directoryName) throws InvalidFileName, FileException {
		if ("".equals(directoryName)) {
			throw new InvalidFileName(ErrorNumberType.CF_EIO, "");
		}
		final File file = new File(this.root, directoryName);
		if (!file.isDirectory()) {
			throw new FileException(ErrorNumberType.CF_ENOTDIR, "Not a directory\n");
		}
		if (file.list().length != 0) {
			throw new FileException(ErrorNumberType.CF_ENOTEMPTY, "Directory not empty\n");
		}
		final boolean result = file.delete();
		if (!result) {
			throw new FileException(ErrorNumberType.CF_EIO, "Failed to delete directory: " + directoryName);
		}

	}
	
	public void dispose() {
		
	}

}
