package edu.ucsc.cs.netEvo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class UtilsTest {

	@Test
	public void testCompile() throws IOException {
		String fileName = "resources/fixtures/App.java";
		String sourceCode = Files.toString(new File(fileName), Charsets.UTF_8);
		CompilationUnitDeclaration ast = Utils.compile(sourceCode, fileName);
		assertEquals("edu.ucsc.cs.netEvo", ast.currentPackage.toString());
	}

}
