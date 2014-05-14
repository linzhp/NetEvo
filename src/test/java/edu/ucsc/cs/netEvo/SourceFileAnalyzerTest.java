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
		CodeEntity[] vertices = getVertices();
		assertThat(vertices, hasItemInArray(new HasName<>("edu.ucsc.cs.netEvo.App")));
	}
	
	@Test
	public void shouldGetMethodName() throws IOException {
		CodeEntity[] vertices = getVertices();
		assertThat(vertices, hasItemInArray(new HasName<>("edu.ucsc.cs.netEvo.App.main")));
	}
	
	@Test
	public void shouldGetInnerClassName() throws IOException {
		CodeEntity[] vertices = getVertices();
		assertThat(vertices, hasItemInArray(new HasName<>("edu.ucsc.cs.netEvo.App.Component")));		
	}

	@Test
	public void shouldGetInnerClassMethodName() throws IOException {
		CodeEntity[] vertices = getVertices();
		assertThat(vertices, hasItemInArray(new HasName<>("edu.ucsc.cs.netEvo.App.Component.process")));		
	}
	
	@Test
	public void shouldGetSingleTypeReference() throws IOException {
		Dependency[] edges = getEdges();
		assertThat(edges,hasItemInArray(new HasTarget<>("SourceFileAnalyzer")));
	}

	@Test
	public void shouldGetQualifiedTypeReference() throws IOException {
		Dependency[] edges = getEdges();
		assertThat(edges,hasItemInArray(new HasTarget<>("com.google.common.base.Joiner")));
	}

	@Test
	public void shouldGetQualifiedNameReference() throws IOException {
		Dependency[] edges = getEdges();
		assertThat(edges,hasItemInArray(new HasTarget<>("com.google.common.collect.Lists")));
		assertThat(edges,hasItemInArray(new HasTarget<>("System.out")));
	}

	private CodeEntity[] getVertices() throws IOException {
		SourceFileAnalyzer analyzer = parseFile();
		CodeEntity[] vertices = analyzer.vertices.toArray(new CodeEntity[analyzer.vertices.size()]);
		return vertices;
	}
	
	private Dependency[] getEdges() throws IOException {
		SourceFileAnalyzer analyzer = parseFile();
		return analyzer.edges.toArray(new Dependency[analyzer.edges.size()]);
	}

	private SourceFileAnalyzer parseFile() throws IOException {
		String fileName = "resources/fixtures/App.java";
		String sourceCode = Files.toString(new File(fileName), Charsets.UTF_8);
		CompilationUnitDeclaration ast = Utils.compile(sourceCode, fileName);
		SourceFileAnalyzer analyzer = new SourceFileAnalyzer(0);
		ast.traverse(analyzer, ast.scope);
		return analyzer;
	}

	
}

class HasName<T> extends CustomMatcher<T> {
	private String name;
	public HasName(String name) {
		super("an source code entity called " + name);
		this.name = name;
	}

	@Override
	public boolean matches(Object item) {
		return ((CodeEntity)item).qualifiedName.equals(name);
	}
}

class HasTarget<T> extends CustomMatcher<T> {
	private String target;
	
	public HasTarget(String target) {
		super("a dependency on " + target);
		this.target = target;
	}

	@Override
	public boolean matches(Object item) {
		return ((Dependency)item).target.equals(target);
	}

}