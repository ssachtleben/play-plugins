package com.ssachtleben.play.plugin.imports;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import play.Logger;

/**
 * The AbstractImporter provides basic functionality to import data.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractImporter<E extends Serializable> {
	protected final Logger.ALogger log = Logger.of(getClass());

	protected Class<E> clazz;

	public AbstractImporter() {
		this.clazz = getTypeParameterClass();
	}

	protected void readDirectory(File folder, E parent, boolean recursive) {
		try {
			for (File file : getFiles(folder)) {
				E entity = createEntry(file, parent);
				if (file.isDirectory() && recursive) {
					log.info("Found directory: " + file.getAbsolutePath());
					readDirectory(file, entity, recursive);
				} else {
					log.info("Found file: " + file.getAbsolutePath());
					updateEntity(file, entity);
				}
				saveEntity(entity, parent);
			}

		} catch (Exception e) {
			log.error("Exception occured during import data", e);
		}
	}

	protected File[] getFiles(File folder) {
		return folder.listFiles();
	}

	@SuppressWarnings("unchecked")
	private Class<E> getTypeParameterClass() {
		Type type = getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) type;
		return (Class<E>) paramType.getActualTypeArguments()[0];
	}

	protected abstract void updateEntity(File file, E model);

	protected abstract void saveEntity(E entity, E parent);

	protected abstract E createEntry(File file, E parent);

	public abstract void process();

	public abstract String getBaseFolder();

	protected abstract boolean accept(File file);
}
