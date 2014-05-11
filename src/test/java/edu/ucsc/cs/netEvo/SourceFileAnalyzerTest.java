package edu.ucsc.cs.netEvo;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.hamcrest.CustomMatcher;
import static org.hamcrest.collection.IsArrayContaining.*;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class SourceFileAnalyzerTest {

	@Test
	public void shouldGetClassName() throws IOException {
		String fileName = "resources/fixtures/App.java";
		String sourceCode = Files.toString(new File(fileName), Charsets.UTF_8);
		CompilationUnitDeclaration ast = Utils.compile(sourceCode, fileName);
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer(0);
		ast.traverse(analyzer, ast.scope);
		CodeEntity[] vertices = analyzer.vertices.toArray(new CodeEntity[analyzer.vertices.size()]);
		assertThat(vertices, hasItemInArray(new HasName<>("edu.ucsc.cs.netEvo.App")));
	}

}

class HasName<T> extends CustomMatcher<T> {
	private String name;
	public HasName(String name) {
		super("Has a name" + name);
		this.name = name;
	}

	@Override
	public boolean matches(Object item) {
		return ((CodeEntity)item).qualifiedName.equals(name);
	}
	
}