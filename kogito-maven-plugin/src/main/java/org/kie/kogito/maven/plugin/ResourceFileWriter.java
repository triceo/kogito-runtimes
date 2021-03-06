package org.kie.kogito.maven.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.maven.plugin.MojoExecutionException;
import org.drools.compiler.compiler.io.memory.MemoryFile;
import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.modelcompiler.CanonicalKieModule;

public class ResourceFileWriter {

    private MemoryFileSystem mfs;
    private String targetDirectory;

    public ResourceFileWriter(final MemoryFileSystem mfs, final String targetDirectory) {
        this.mfs = mfs;
        this.targetDirectory = targetDirectory;
    }

    public void write() throws MojoExecutionException {
        // copy the META-INF packages file
        final MemoryFile packagesMemoryFile = (MemoryFile) mfs.getFile(CanonicalKieModule.MODEL_FILE);
        final String packagesMemoryFilePath = packagesMemoryFile.getFolder().getPath().toPortableString();
        final Path packagesDestinationPath = Paths.get(targetDirectory, "classes", packagesMemoryFilePath, packagesMemoryFile.getName());

        try {
            if (!packagesDestinationPath.toFile().exists()) {
                Files.createDirectories(packagesDestinationPath.getParent());
            }
            Files.copy(packagesMemoryFile.getContents(), packagesDestinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException e) {
            throw new MojoExecutionException("Unable to write file", e);
        }
    }
}
