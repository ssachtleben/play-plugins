package com.ssachtleben.play.plugin.imports;

import java.io.File;
import java.io.Serializable;

import play.Play;

/**
 * The FolderImporter runs over the base folder and import data.
 * 
 * @author Sebastian Sachtleben
 * 
 * @param <E>
 *          The entity which will be created.
 */
public abstract class FolderImporter<E extends Serializable> extends AbstractImporter<E> {

	@Override
	public void process() {
		File baseFolder = new File(Play.application().path(), getBaseFolder());
		readDirectory(baseFolder, null, true);
	}

}
